/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.common;

/**
 *
 * @author Usuario
 */
public class ArduinoHelp implements ClientArduinoSignal {
    public static final int ACTUADOR = 0;
    public static final int SENSOR   = 1;
    public static boolean isAnActuador(String prefix){
        if(prefix.equals(RELEE_SIGNAL)){ // actuador.. luego agregar + sensores
            return true;
        }
        return false;
    }
}
