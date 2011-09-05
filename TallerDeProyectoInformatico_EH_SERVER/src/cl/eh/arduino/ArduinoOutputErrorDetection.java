/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

import cl.eh.common.ClientArduinoSignal;

/**
 *
 * @author Usuario
 */
public class ArduinoOutputErrorDetection implements ClientArduinoSignal{
    public static boolean isAArduinoCorrectOutput(String arduino_output){
        if(arduino_output.equals(ClientArduinoSignal.LUZ_SIGNAL) ){
            return true;
        }else if(arduino_output.equals(ClientArduinoSignal.INTERRUPTOR_LENGUETA_SIGNAL)){
            return true;
        }else if(arduino_output.equals(ClientArduinoSignal.MOVIMIENTO_SIGNAL )){
            return true;
        }else if(arduino_output.equals(ClientArduinoSignal.RELEE_SIGNAL )){
            return true;
        }else if(arduino_output.equals(ClientArduinoSignal.TEMPERATURA_SIGNAL )){
            return true;
        }
        return false;
    }
}
