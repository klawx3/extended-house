/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino.model;

import java.util.EventListener;

/**
 *
 * @author Usuario
 */
public interface ArduinoEventListener extends EventListener {

    public void ArduinoEventListener(ArduinoEvent evt);
}
