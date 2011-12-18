/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cl.eh.arduino.SerialArduino;
import cl.eh.arduino.model.ArduinoEvent;
import cl.eh.arduino.model.ArduinoEventListener;

/**
 *
 * @author Usuario
 */
public class ArduinoTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SerialArduino sa = new SerialArduino("COM4");
        sa.addArduinoEventListener(new ArduinoEventListener() {

            public void ArduinoEventListener(ArduinoEvent evt) {
                System.out.println("disp"+evt.getNombreDisositivo());
                System.out.println("num"+evt.getNumeroDispositovo());
                System.out.println("val"+evt.getValorDispositivo());
            }
        });
        
    }
}
