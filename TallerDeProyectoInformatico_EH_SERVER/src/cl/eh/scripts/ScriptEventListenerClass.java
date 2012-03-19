/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.util.EventListener;

/**
 *
 * @author Administrador
 */
public interface ScriptEventListenerClass extends EventListener {

    public void newScriptEventListener(ScriptEvent evt);
    public void removedScriptEvent(ScriptEvent evt);
}
