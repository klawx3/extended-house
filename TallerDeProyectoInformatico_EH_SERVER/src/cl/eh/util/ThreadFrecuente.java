/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;


import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadFrecuente implements Runnable {

    private boolean isThreadFinishWork;
    private boolean detenido;
    private long millisegundos;
    private boolean startAgain;
    
    public ThreadFrecuente(long millisegundos){
        detenido = false;
        isThreadFinishWork = false;
        startAgain = false;
        this.millisegundos = millisegundos;
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (!detenido) {
            while (startAgain) {
                isThreadFinishWork = false;
                try {
                    Thread.sleep(millisegundos);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadFrecuente.class.getName()).log(Level.SEVERE, null, ex);
                }
                startAgain = false;
                isThreadFinishWork = true;
            }
            
        }
    }
    
    public void startThreadAgain(){
        startAgain = true;
    }
    
    public boolean isThreadFinishWork(){
        return isThreadFinishWork;
    }
    public void stopThread(){
        detenido = true;
    }
    
    
    
  
    
    
}
