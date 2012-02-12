/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

import cl.eh.db.ConexionExtendedHouse;
import cl.eh.db.model.Evento;
import cl.eh.db.model.Usuario;
import cl.eh.eventos.model.EventoListener;
import cl.eh.exceptions.LESException;
import cl.eh.properties.Guardable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Lenguaje de Evento Simple
 * @author Usuario
 */
public class LESAdministador implements Guardable {

    private EventoListener show;
    private List<HiloDeEventoLES> eventos;
    private ConexionExtendedHouse con;

    public LESAdministador(ConexionExtendedHouse con) throws LESException {
        this.con = con;
        show = null;
        eventos = Collections.synchronizedList(new ArrayList<HiloDeEventoLES>());
        buscarEventos();
    }
    
    public void start() {
//        for (HiloDeEventoLES hilo : eventos) {
//            hilo.start();
//        }
    }

    public void addEventoSimpleString(String eventoString) throws LESException {
        addEventoSimpleConstructor(eventoString,ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER);
    }

    public void addEventoSimpleString(String eventoString, String user) throws LESException {
        addEventoSimpleConstructor(eventoString,user);
    }
    
    private void addEventoSimpleConstructor(String eventoString, String user) throws LESException{
        Usuario usr = new Usuario();
        usr.setUsuario(user);
        Evento evnt = new Evento(eventoString, true, usr);
        int v = con.addEvento(evnt);
        if(v == ConexionExtendedHouse.QUERYSTATUS_OK){
            buscarEventos();
        }else{
            throw new LESException("Registro existente o relativo a error");
        }
    }

    public boolean removeEventoSiemple(String eventoString) {
        boolean existe = false;
        for (HiloDeEventoLES hdeles : eventos) {
            System.err.println(hdeles.getEventoString());
            if (hdeles.getEventoString().equals(eventoString)) {
                hdeles.interrupt();
                hdeles.stopIt();
                eventos.remove(hdeles);
                hdeles = null;
                existe = true;
                break;
            }
        }
        return existe;
    }
    
    private void startYAddEventoLES(String eventoString,String user) throws LESException{
        HiloDeEventoLES aux = new HiloDeEventoLES(eventoString,user, this);
        eventos.add(aux);
        aux.start();
    }
    /**
     * NOTA: Al recorrer esta lista deve estar con una condicion sycronized plz
     * @return lista de eventos actuales en ejecucion
     */
    public synchronized  List<HiloDeEventoLES> getEventInExecutionList(){
        return eventos;
    }

    public void addEventoListener(EventoListener l) {
        show = l;
    }
    
    public EventoListener getEventoListener(){
        return show;
    }

    public void save() {
        for(HiloDeEventoLES les : eventos){
            if(!les.isStop()){
                les.interrupt();
            }
        }
    }
    public void sincronizarEventosEnRAMtoBD(){ // nose si esta bien >_<
        for (Evento evtBd : con.getEventos()) {
            String eventoStringBd = evtBd.getEvento_simple();
            boolean existeEnRam = false;
            for(HiloDeEventoLES eventoLesRam: eventos){
                if(eventoLesRam.getEventoString().equals(eventoStringBd)){
                    existeEnRam = true;
                }
            }
            if(!existeEnRam){
                con.removeEvento(eventoStringBd);
            }
        }
    }

    private void buscarEventos() throws LESException {
        boolean existe = false;
        for (Evento evt : con.getEventos()) {
            if(evt.isActivo()){
                String eventoString = evt.getEvento_simple();
                Iterator<HiloDeEventoLES> it = eventos.iterator();
                existe = false;
                while(it.hasNext()){
                    HiloDeEventoLES les = it.next();
                    if(les.getEventoString().equals(eventoString)){
                        existe = true;
                        break;
                    }
                }
                if(!existe){
                    try{
                        startYAddEventoLES(eventoString,evt.getUsuario().getUsuario());
                    }catch(LESException e){
                        con.removeEvento(eventoString);
                        throw e;
                    }
                }
                
            }
        }

    }


}
