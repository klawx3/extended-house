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
    public static final String SEPARADOR = "-";
    public static final char ENDOFSTRING = ';';
    public static final int NOMBREDISPOSITIVO_SERIAL_PRINT_POS = 0;
    public static final int NUMERODISPOSITIVO_SERIAL_PRINT_POS = 1;
    public static final int VALORDISPOSITIVO_SERIAL_PRINT_POS = 2;
    
    public static boolean isAnActuador(String prefix){
        if(prefix.equals(RELEE_SIGNAL)){ // actuador.. luego agregar + sensores
            return true;
        }
        return false;
    }
    
    public static boolean isARecurrentSensor(String prefix){
        if(prefix.equals(TEMPERATURA_SIGNAL) || prefix.equals(LUZ_SIGNAL)){
            return true;
        }
        return false;
    }
}
