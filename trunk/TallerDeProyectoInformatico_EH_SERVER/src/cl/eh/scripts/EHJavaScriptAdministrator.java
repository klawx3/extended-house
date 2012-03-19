/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.io.File;
import cl.eh.server.ExtendedHouseSERVER.ExtendedHouseGeneralAdministrator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.script.Invocable;
import javax.script.ScriptException;
import static com.esotericsoftware.minlog.Log.*;

/**
 *
 * @author Administrador
 */
public class EHJavaScriptAdministrator extends ScriptFileManager {
    private static final String SECTOR = "EHJavaScriptAdministrator";
    private static final String STANDAR_SLEEP_TIME_VAR_NAME = "refresh";
    public static final String SCRIPT_DEFAULT_DIRECTORY = "scripts";
    private static final long STANDAR_SCRIPT_SLEEP_TIME = 10000;
    private final int SCRIPT_MAX_POOL = 20;
    private final long REFRESH_FILE_MILLISECONDS = 5000;
    private JavaScriptModule js;
    private List<RunnableScript> scripts_info;
    private Timer timer;
    private boolean tasksStarted;
    private ExecutorService pool;
    public ExtendedHouseGeneralAdministrator god;

    public EHJavaScriptAdministrator(String directoryName, ExtendedHouseGeneralAdministrator god) throws Exception {
        super(directoryName);
        pool = Executors.newFixedThreadPool(SCRIPT_MAX_POOL);
        scripts_info = new ArrayList<>();
        js = new JavaScriptModule();
        tasksStarted = false;
        this.god = god;
        js.getScriptEngine().put(
                ExtendedHouseGeneralAdministrator.SCRIPT_PARAMETER_NAME,
                god);
        addNewAddonEventEventListener(new ScriptEventListenerClass() {
            @Override
            public void newScriptEventListener(ScriptEvent evt) {
                try {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(evt.getScriptFile());
                        Reader reader = new InputStreamReader(is);
                        if (reader != null) {
                            String fileName = getNameOfScript(evt.getScriptFile());
                            RunnableScript rs = getRunnableScript(reader, fileName);
                            if (rs != null) { // si existe metodo run
                                pool.submit(rs); // ejecuto el wea
                                info(SECTOR,"Script ["+fileName+"] ejecutandose");
                                scripts_info.add(rs); // lo agrego a una lista
                            } else {
                                error(SECTOR, "No existe el metodo run en ["+fileName+"]");
                                removeFileScript(evt.getScriptFile());
                            }
                        }else{
                            removeFileScript(evt.getScriptFile());
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } catch (ScriptException ex) {
                    error(SECTOR, "Error en el archivo ["
                            + evt.getScriptFile() + "]:"
                            + ex.getMessage());
                    removeFileScript(evt.getScriptFile());
                }
            }

            @Override
            public void removedScriptEvent(ScriptEvent evt) {
                String name = getNameOfScript(evt.getScriptFile());
                if (!stopScriptExecution(name)) {
                    error(SECTOR, new Exception("No se ha podido remover "
                            + name + "de la lista"));
                } else {
                    info(SECTOR, "Script [" + name + "] Removido");
                }

            }
        });
    }
    
    private String getNameOfScript(File file){
        return file.getName().replaceAll(FILE_EXTENCION, "");
    }

    public void startTasks() {
        if (!tasksStarted) {
            timer = new Timer();
            timer.schedule(this, 0, REFRESH_FILE_MILLISECONDS);
            tasksStarted = true;
        }
    }
    
    public void restartScripts() {
        if (tasksStarted) {
            timer.cancel();
            scripts_info.clear();
            getScripts().clear();
            pool.shutdown();
            try {
                pool.awaitTermination(0, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            pool = Executors.newFixedThreadPool(SCRIPT_MAX_POOL);
        }

        timer = new Timer();
        timer.schedule(this, 0, REFRESH_FILE_MILLISECONDS);
        tasksStarted = true;
    }

    private RunnableScript getRunnableScript(Reader si, String fileName) throws ScriptException {
        Invocable invocableEngine = (Invocable) js.getScriptEngine();
        js.getScriptEngine().eval(si);
        Object sleep_time_millis = js.getScriptEngine().get(STANDAR_SLEEP_TIME_VAR_NAME);
        long refreshTime = STANDAR_SCRIPT_SLEEP_TIME;
        if (sleep_time_millis != null) {
            if (sleep_time_millis instanceof Number) {
                refreshTime = ((Number) sleep_time_millis).longValue();
            }
        }
        try {
            si.close();
        } catch (IOException ex) {
            error(SECTOR,ex);
        }
        Object runnableJSClass = js.getScriptEngine().get(fileName);
        if (runnableJSClass != null) {
            Runnable run = invocableEngine.getInterface(runnableJSClass,
                    Runnable.class);
            if (run != null) {
                RunnableScript runnableScript = new RunnableScript(run,
                        fileName,
                        refreshTime);
                return runnableScript;
            } else {
                error(SECTOR, "NO EXISTE el [metodo run] en el objeto [identificador de archivo] en ["+fileName+"]");
            }
        } else {
            error(SECTOR, "NO EXISTE el objeto [identificador de archivo] en ["+fileName+"]");
        }
        return null;
    }
    
    private boolean stopScriptExecution(String name){
        for(RunnableScript si : scripts_info){
            if(si.getClassName().equals(name)){
                si.stopScript();
                scripts_info.remove(si);
                return true;
            }
        }
        return false;
    }


}
