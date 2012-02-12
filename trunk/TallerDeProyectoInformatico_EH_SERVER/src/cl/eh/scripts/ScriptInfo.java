/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.io.Reader;

/**
 *
 * @author Administrador
 */
public class ScriptInfo {
    private boolean errors;
    private RunnableScript runableScript;
    private String fileName;

    public ScriptInfo(RunnableScript runableScript, String fileName) {
        this.runableScript = runableScript;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public RunnableScript getRunableScript() {
        return runableScript;
    }

    public void setRunableScript(RunnableScript runableScript) {
        this.runableScript = runableScript;
    }

}
