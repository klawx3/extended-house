/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.util.TimerTask;

/**
 *
 * @author Administrador
 */
public class RunnableScript implements Runnable {
    private Runnable run;
    private boolean stop;
    private long refreshTime;
    
    public RunnableScript(Runnable run,long refreshTime){
        this.run = run;
        this.refreshTime = refreshTime;
        stop = false;
    }
    
    @Override
    public void run() {
        while(!stop){
            try {
                run.run();
                Thread.sleep(refreshTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
        System.err.println("Se salio de run !!!!!!!!!");
    }
    
    public void stopScript(){
        stop = true;
    }
    
    public Runnable getRunnable(){
        return run;
    }
    
}
