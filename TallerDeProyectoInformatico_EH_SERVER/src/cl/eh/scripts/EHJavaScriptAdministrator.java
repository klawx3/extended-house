/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptException;
import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Administrador
 */
public class EHJavaScriptAdministrator extends ScriptFileManager{
    public static final String SCRIPT_DEFAULT_DIRECTORY = "scripts";
    private final int SCRIPT_MAX_POOL = 20;
    private final long REFRESH_FILE_MILLISECONDS = 10000;
    private JavaScriptModule js;
    private ScriptFileManager script_fm;
    private List<ScriptInfo> scripts_info;
    private Timer timer;
    private boolean tasksStarted;
    private ExecutorService pool;
    public God god;

    public EHJavaScriptAdministrator(String directoryName,God god) throws Exception {
        super(directoryName);
        pool = Executors.newFixedThreadPool(SCRIPT_MAX_POOL);
        scripts_info = new ArrayList<>();
        js = new JavaScriptModule();
        tasksStarted = false;
        this.god = god;
        js.getSE().put(God.SCRIPT_PARAMETER_NAME, god);
        addNewAddonEventEventListener(new NewScriptEventListener() {

            @Override
            public void newScriptEventListener(NewScriptEvent evt) {
                try {
                    boolean existeScript = false;
                    String nomArchivo = evt.getScriptFile().getName();
                    for(ScriptInfo s_info : scripts_info){
                        if(s_info.getFileName().equals(nomArchivo)){
                            existeScript = true;
                            break;
                        }
                    }
                    if (!existeScript) {
                        InputStream is = new FileInputStream(evt.getScriptFile());
                        Reader reader = new InputStreamReader(is);
                        if (reader != null) {
                            RunnableScript rs = getRunnableScript(reader);
                            if (rs != null) { // si existe metodo run
                                pool.submit(rs); // ejecuto el script
                                ScriptInfo si = new ScriptInfo(rs,
                                        evt.getScriptFile().getName());
                                scripts_info.add(si); // lo agrego a una lista
                                
                            }else{
                                debug("ehjAVAcRIPTaDMIN","NO EXISTE METODO RUN");
                            }
                        }
                    }

                } catch (FileNotFoundException | ScriptException ex) {
                    Logger.getLogger(JavaScriptModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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

    private RunnableScript getRunnableScript(Reader si) throws ScriptException {
        
        js.getSE().eval(si);
        Object obj = js.getSE().get("refresh");
        long refreshTime = 1000;
        if(obj != null){
            if(obj instanceof Number){
                refreshTime = ((Number)obj).longValue();
            }
        }
        Invocable invocableEngine = (Invocable) js.getSE();
        Runnable r = invocableEngine.getInterface(Runnable.class);
        if (r != null) {
            RunnableScript runnableScript = new RunnableScript(r, refreshTime);
            return runnableScript;
        }
        return null;
    }
    private boolean stopScriptExecution(String name){
        for(ScriptInfo si : scripts_info){
            if(si.getFileName().equals(name)){
                si.getRunableScript().stopScript();
                scripts_info.remove(si);
                return true;
            }
        }
        return false;
    }


}
