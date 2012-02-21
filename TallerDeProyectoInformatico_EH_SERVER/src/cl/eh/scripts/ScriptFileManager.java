/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.TimerTask;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Administrador
 */
public class ScriptFileManager extends TimerTask implements FileFilter {
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

    protected void addNewAddonEventEventListener(NewScriptEventListener listener) {
        listenerList.add(NewScriptEventListener.class, listener);
    }

    protected void removeNewAddonEventListener(NewScriptEventListener listener) {
        listenerList.remove(NewScriptEventListener.class, listener);
    }

    protected void fireEvent(ScriptEvent evt,boolean newScriptFile) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == NewScriptEventListener.class) {
                if(newScriptFile){
                    ((NewScriptEventListener) listeners[i + 1]).newScriptEventListener(evt);
                }else{
                    ((NewScriptEventListener) listeners[i + 1]).removedScriptEvent(evt);
                }
                
            }
        }
    }

    private void addFileScripts() {
        File f = new File(path);
        File[] listFiles = f.listFiles(this);
        if (listFiles.length != 0) {
            for (File n : listFiles) {
                if (!scripts.contains(n)) {
                    scripts.add(n);
                    fireEvent(new ScriptEvent(this, n),true);
                }
            }
        }
    }




}
