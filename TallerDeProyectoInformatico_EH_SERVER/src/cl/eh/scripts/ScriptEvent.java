/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.io.File;
import java.util.EventObject;

/**
 *
 * @author Administrador
 */
    public class ScriptEvent extends EventObject {

        private File scriptFile;

        public ScriptEvent(Object source, File f) {
            super(source);
            scriptFile = f;
        }

        public File getScriptFile() {
            return scriptFile;
        }
    }