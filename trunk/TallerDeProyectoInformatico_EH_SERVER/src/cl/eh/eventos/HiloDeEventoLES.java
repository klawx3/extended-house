/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

import cl.eh.db.ConexionExtendedHouse;
import cl.eh.eventos.model.EventoListener;
import cl.eh.eventos.model.EventoEvent;
import cl.eh.exceptions.LESException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloDeEventoLES extends Thread {
    private EventoListener show;
    private LESObject lesObj;
    private String eventoString;
    private boolean isStop;
    private List<HiloDeEventoLES> listaDeEventosLES;
    private ConexionExtendedHouse con;
    private LESAdministador lesAdm;
    private String user;


    public HiloDeEventoLES(String eventoString,String user,LESAdministador lesAdm) throws LESException {
        this.show = lesAdm.getEventoListener();
        this.eventoString = eventoString;
        this.listaDeEventosLES = lesAdm.getEventInExecutionList();
        this.lesObj = new LESObject(eventoString);
        isStop = false;
        this.lesAdm = lesAdm;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            while (lesObj.hasMoreThinksToDo() && !isStop) {
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
                lesObj = new LESObject(eventoString);
            }
            synchronized (lesAdm.getEventInExecutionList()) {
                lesAdm.getEventInExecutionList().remove(this);
                lesAdm.sincronizarEventosEnRAMtoBD();
            }
        } catch (LESException ex) {
            Logger.getLogger(HiloDeEventoLES.class.getName())
                    .log(Level.SEVERE, null, ex); // no deveria tirarla
        } catch (InterruptedException ex){
            System.out.println("Interrupcion en la clase");
            synchronized (lesAdm.getEventInExecutionList()) {
                lesAdm.getEventInExecutionList().remove(this);
                lesAdm.sincronizarEventosEnRAMtoBD();
            }
        }
    }
    
    private void eventProduced(EventoEvent e) {
        if (show != null) {
            show.eventoPerformed(e);
        }
    }
    public String getUser() {
        return user;
    }
    public void stopIt(){
        this.isStop = true;
    }

    public String getEventoString() {
        return eventoString;
    }

    public boolean isStop() {
        return isStop;
    }

    public LESObject getLesObj() {
        return lesObj;
    }
    
    @Override
    public String toString(){
        return this.eventoString;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.eventoString != null ? this.eventoString.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object e){
        if(e instanceof HiloDeEventoLES){
            HiloDeEventoLES objLES = (HiloDeEventoLES) e;
            if(objLES.hashCode() == this.hashCode()){
                return true;
            }
        }
        return false;
    }



    
}
