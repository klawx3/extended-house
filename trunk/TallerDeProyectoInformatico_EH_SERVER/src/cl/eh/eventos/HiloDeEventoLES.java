/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

import cl.eh.eventos.model.EventoListener;
import cl.eh.eventos.model.EventoEvent;
import cl.eh.exceptions.LESException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloDeEventoLES implements Runnable {
    EventoListener show;
    LESObject lesObj;
    String eventoString;
    boolean isStop;

    public HiloDeEventoLES(String eventoString,EventoListener show){
        this.show = show;
        this.eventoString = eventoString;
        isStop = false;
    }
    
    public void run() {
        try {
            lesObj = new LESObject(eventoString);
            while (lesObj.hasMoreThinksToDo()) {
                try {
                    Thread.sleep(lesObj.getMillisLeftToActivate());
                    EventoEvent eventoAux = lesObj.getEventoObject();
                    if (eventoAux != null) {
                        eventProduced(eventoAux);
                    } else {
                        try {
                            throw new Exception("El evento es null");
                        } catch (Exception ex) {
                            Logger.getLogger(HiloDeEventoLES.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloDeEventoLES.class.getName()).log(Level.SEVERE, null, ex);
                }
                lesObj = new LESObject(lesObj);
            }
        } catch (LESException ex) {
            Logger.getLogger(HiloDeEventoLES.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void eventProduced(EventoEvent e) {
        if (show != null) {
            show.eventoPerformed(e);
        }
    }
    public void stop(){
        isStop = true;
    }
    
}
