/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import javax.swing.event.EventListenerList;

import static com.esotericsoftware.minlog.Log.*;

/**
 *
 * @author Administrador
 */
public class ScriptFileManager extends TimerTask implements FileFilter {
    private static final String SECTOR = "ScriptFileManager";
    public static final String FILE_EXTENCION = ".js";
    private String path;
    private List<File> scripts;

    protected EventListenerList listenerList = new EventListenerList();

    public ScriptFileManager(String path) {

        this.path = path;
        scripts = Collections.synchronizedList(new ArrayList<File>());
        File f = new File(path);
        if (!f.isDirectory()) {
            f.mkdir();
        }
    }

    private void addFileScripts() {
        File f = new File(path);
        File[] listFiles = f.listFiles(this);
        int fileLength = listFiles.length;
        if (fileLength != 0) {
            for (File n : listFiles) {
                if (!scripts.contains(n)) {
                    scripts.add(n);
                    fireEvent(new ScriptEvent(this, n), true);
                }
            }
        }
        if(fileLength != scripts.size() && fileLength < scripts.size()){
            int indexOfDeletingFiles[] = new int[scripts.size() - fileLength];
            int indexOfVector = 0; 
            for (int i = 0 ; i < scripts.size() ; i++) {
                File ff = scripts.get(i);
                if (!ff.exists()) {
                    indexOfDeletingFiles[indexOfVector] = i;
                    indexOfVector++;
                }
            }
            int removidasAux = 0;
            for (int i = 0; i < indexOfDeletingFiles.length; i++) {
                File ff = scripts.get(indexOfDeletingFiles[i] - removidasAux);
                fireEvent(new ScriptEvent(this, ff), false);
                if (!removeFileScript(ff)) {
                    error(SECTOR, new Exception("No se puede remover el "
                            + "archivo de la ram"));
                }else{
                    removidasAux++;
                }
            }
        }
    }
    protected boolean removeFileScript(File ff){
        return scripts.remove(ff);
    }

    public List<File> getScripts() {
        return scripts;
    }

    @Override
    public void run() {
        addFileScripts();
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.getName().endsWith(FILE_EXTENCION)) {
            return true;
        }
        return false;
    }

    protected void addNewAddonEventEventListener(ScriptEventListenerClass listener) {
        listenerList.add(ScriptEventListenerClass.class, listener);
    }

    protected void removeNewAddonEventListener(ScriptEventListenerClass listener) {
        listenerList.remove(ScriptEventListenerClass.class, listener);
    }

    protected void fireEvent(ScriptEvent evt,boolean newScriptFile) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ScriptEventListenerClass.class) {
                if(newScriptFile){
                    ((ScriptEventListenerClass) listeners[i + 1]).newScriptEventListener(evt);
                }else{
                    ((ScriptEventListenerClass) listeners[i + 1]).removedScriptEvent(evt);
                }
                
            }
        }
    }






}
