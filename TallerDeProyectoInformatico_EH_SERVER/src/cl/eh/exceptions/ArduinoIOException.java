/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.exceptions;

/**
 *
 * @author Usuario
 */
public class ArduinoIOException extends Exception {
    public ArduinoIOException(String causa){
        super("Arduino I/O Exception :"+causa);
    }
}
