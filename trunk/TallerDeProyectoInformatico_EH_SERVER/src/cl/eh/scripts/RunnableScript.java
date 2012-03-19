/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;


/**
 *
 * @author Administrador
 */
public class RunnableScript implements Runnable {
    private Runnable run;
    private boolean stop;
    private long refreshTime;
    private String className;


    
    public RunnableScript(Runnable run,String className,long refreshTime){
        this.run = run;
        this.refreshTime = refreshTime;
        this.className = className;
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
    }
    
    public void stopScript(){
        stop = true;
    }

    public Runnable getRunnable() {
        return run;
    }

    public String getClassName() {
        return className;
    }
}
