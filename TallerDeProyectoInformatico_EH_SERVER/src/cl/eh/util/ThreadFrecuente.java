/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ThreadFrecuente implements Runnable {
    //al sobrepasar los minutos se queda "pegado"

    private int minutos ;
    private boolean stuck;
    
    public ThreadFrecuente(int minutos){
        minutos = minutos * (60 * 1000);
    }
    
    public void run() {
        while(true){
            while(!stuck){
                try {
                    Thread.sleep(minutos);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadFrecuente.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
                stuck = true;
            }            
        }

    }
    
    public boolean isParado(){
        return stuck;
    }
    
    public void parar(){
        stuck = true;
    }
    
    public void continuar(){
        stuck = false;
    }
    
    
}
