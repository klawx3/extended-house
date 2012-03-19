/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.logger;

import java.util.EventListener;

/**
 *
 * @author Administrador
 */
public interface LogEventListener extends EventListener {
    void logginPerformed(LogginLine logginLine);
}
