/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.common;

/**
 *
 * @author Usuario
 */
public interface ClientArduinoSignal { // debe estar en concordancia con el print de arduino (PREFIJO)
    final String RELEE_SIGNAL                = "RL"; //numeros 0 - 7
    
    final String TEMPERATURA_SIGNAL          = "TMP"; //numero 0
    final String INTERRUPTOR_LENGUETA_SIGNAL = "ILG"; //numero 0 - 4
    final String LUZ_SIGNAL                  = "LUZ"; //numero 0
}
