/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

import cl.eh.eventos.model.EventoEvent;
import cl.eh.eventos.compilator_utils.LESCompUtils;
import cl.eh.eventos.model.LESCondition;
import cl.eh.eventos.model.LESDateCondition;
import cl.eh.exceptions.LESException;
import cl.eh.util.Arreglos;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 *
 * @author Usuario
 */
public class LESObject {

    private long millisecondsToActivate;
    private boolean hasMoreThinksToDo;
    private ArrayList<String> arrOfTokens;
    private String eventoString;
    /*--------------------------------------*/
    private EventoEvent datos;
    private ArrayList<LESDateCondition> condiciones;

    public LESObject(String eventoString) throws LESException {
        this.eventoString = eventoString;
        StringTokenizer stToken = new StringTokenizer(eventoString);
        arrOfTokens = new ArrayList<String>();
        condiciones = new ArrayList<LESDateCondition>();
        while (stToken.hasMoreTokens()) {
            arrOfTokens.add(stToken.nextToken());
        }
        analisisDeTexto();
        analisisDeCondiciones();

    }

    public LESObject(LESObject lesObj) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean hasMoreThinksToDo() {
        return hasMoreThinksToDo;
    }

    public long getMillisLeftToActivate() {
        return millisecondsToActivate;
    }

    public EventoEvent getEventoObject() {
        return datos;
    }
    
    @Override
    public String toString(){
        return eventoString;
    }

    /**
     * uso: el arraylist deve hacer casting
     * @return arreglo de LESCondition 
     */
    public ArrayList<LESDateCondition> getLESConditions() {
        return condiciones;
    }

    private void analisisDeTexto() throws LESException {
        String actuador = null;
        int formaActivacion = -1;
        int numero = -1;
        boolean isInConfig = true;
        int configStepts = 0;
        for (int i = 0; i < arrOfTokens.size(); i++) {
            String at = arrOfTokens.get(i);
            if (isInConfig) {
                if (i == 0) {
                    if (at.equalsIgnoreCase("en")) {
                        actuador = arrOfTokens.get(++i);
                        configStepts++;
                    } else {
                        throw getException(1);
                    }
                }
                int a = LESCompUtils.getNumVectorPalabra(at);
                if (a != -1 && i != 0) {
                    if (!LESCompUtils.isAPablabraRecervada(at)) {
                        throw getException(i + 1);
                    } else { // esta bien
                        if (at.equalsIgnoreCase("numero")) {
                            try {
                                numero = Integer.parseInt(arrOfTokens.get(++i));
                                configStepts++;
                            } catch (NumberFormatException e) {
                                throw getException(i + 1);
                            }
                        } else if (at.equalsIgnoreCase("fijar")) {
                            if (LESCompUtils.isAnTipoDeFijado(arrOfTokens.get(i + 1))) {
                                String fa = arrOfTokens.get(i + 1);
                                if (fa.equalsIgnoreCase("cambiar")) {
                                    formaActivacion = EventoEvent.ACCI_CAMBIAR;
                                } else if (fa.equalsIgnoreCase("encendido")) {
                                    formaActivacion = EventoEvent.ACCI_ENCENDER;
                                } else {
                                    formaActivacion = EventoEvent.ACCI_APAGAR;
                                }
                                configStepts++;
                                i++;
                            } else {
                                throw getException(i + 1);
                            }
                        }
                    }
                } else {
                    throw getException(i + 1);
                }
                if (configStepts == 3) {
                    isInConfig = false;
                    datos = new EventoEvent(actuador, numero, formaActivacion);
                }
            } else {
                if (at.equalsIgnoreCase("para")) { // ojo pq asumo q esta "para"
                    String fec, tiempo, condicionTiempo, aux;
                    long tiempo_long = -1;
                    int condicionTiempo_int = -1;
                    aux = arrOfTokens.get(i + 1); // fecha
                    if ((i + 1) > arrOfTokens.size()) {
                        throw getException(i + 1);
                    }
                    if (!LESCompUtils.isAPablabraRecervada(aux)) {
                        fec = aux;
                        i++;
                    } else {
                        throw getException(i + 1);
                    }
                    if ((i+1) < arrOfTokens.size()) { // existen mas weas q analizar
                        aux = arrOfTokens.get(i + 1); // veo si es y o cada
                        if (LESCompUtils.isAPablabraRecervada(aux)) { // solo si es y
                            if (aux.equalsIgnoreCase("y")) { // termino la conf de  1 evento
                                Calendar fechaCalendar = LESCompUtils.getCalendar(fec);
                                if(fechaCalendar  != null ){
                                    boolean sinHoras = LESCompUtils.isDateWhithowtHour(fec);
                                    LESDateCondition lesCon = new LESDateCondition(fechaCalendar,sinHoras);
                                    lesCon.setNextCondition(LESCondition.LOGICAL_AND);
                                    condiciones.add(lesCon);
                                    i++;
                                    continue;
                                }else{
                                    throw getException(99999);
                                }
                                
                            } else { // millis
                                if (aux.equalsIgnoreCase("cada")) {
                                    i++;
                                    tiempo = arrOfTokens.get(i + 1); // tiempo
                                    try{
                                        tiempo_long = Long.parseLong(tiempo);
                                    }catch(NumberFormatException e){
                                        throw getException(i+1);
                                    }
                                    i++;
                                    if ((i + 1) < arrOfTokens.size()) {
                                        condicionTiempo = arrOfTokens.get(i + 1); // condcion tiempo
                                        if(LESCompUtils.isAPablabraRecervada(condicionTiempo)){
                                            if(condicionTiempo.equalsIgnoreCase("millisegundos")){
                                                condicionTiempo_int = LESDateCondition.TIEMPO_MILLISEGUNDOS;
                                            }else if(condicionTiempo.equalsIgnoreCase("segundos")){
                                                condicionTiempo_int = LESDateCondition.TIEMPO_SEGUNDOS;
                                            }else if(condicionTiempo.equalsIgnoreCase("minutos")){
                                                condicionTiempo_int = LESDateCondition.TIEMPO_MINUTOS;
                                            }else if(condicionTiempo.equalsIgnoreCase("horas")){
                                                condicionTiempo_int = LESDateCondition.TIEMPO_HORAS;
                                            }
                                        }else{
                                            throw getException(i+1);
                                        }
                                        i++;
                                        Calendar fechaCalendar = LESCompUtils.getCalendar(fec);
                                        boolean sinHoras;
                                        if(fechaCalendar  != null ){
                                           sinHoras = LESCompUtils.isDateWhithowtHour(fec);
                                        }else{
                                            throw getException(99999);
                                        }
                                        if ((i+1) >= arrOfTokens.size()) { // no existen mas weas
                                            LESDateCondition lesCon = new LESDateCondition(fechaCalendar,sinHoras);
                                            lesCon.setNextCondition(LESCondition.NULL);
                                            lesCon.setTiempo(tiempo_long);
                                            lesCon.setUnidadTiempo(condicionTiempo_int);
                                            condiciones.add(lesCon);
                                        } else { // en caso de extender el lenguaje poner weas desde aka
                                            LESDateCondition lesCon = new LESDateCondition(fechaCalendar,sinHoras);
                                            lesCon.setNextCondition(LESCondition.NULL);
                                            lesCon.setTiempo(tiempo_long);
                                            lesCon.setUnidadTiempo(condicionTiempo_int);
                                            aux = arrOfTokens.get(i + 1);
                                            if (aux.equalsIgnoreCase("y")) {
                                                lesCon.setNextCondition(LESDateCondition.LOGICAL_AND);
                                                condiciones.add(lesCon);
                                                continue; // creo
                                            }else{
                                                throw getException(i+1);
                                            }
                                        }
                                    } else {
                                        throw getException(i); // falta argumentos de tiempo
                                    }
                                } else {
                                    throw getException(i);
                                }
                            }
                        } else {
                            throw getException(i + 1);
                        }
                    } else {
                        Calendar fechaCalendar = LESCompUtils.getCalendar(fec);
                        boolean sinHoras;
                        if (fechaCalendar != null) {
                            sinHoras = LESCompUtils.isDateWhithowtHour(fec);
                        } else {
                            throw getException(99999);
                        }
                        LESDateCondition lesCon = new LESDateCondition(fechaCalendar,sinHoras);
                        lesCon.setNextCondition(LESCondition.NULL);
                        condiciones.add(lesCon);
                        return;
                    }

                }
            }
           // System.out.println("isInConfig:" + isInConfig);
        }


    }

    private void //<editor-fold defaultstate="collapsed" desc="comment">
            analisisDeCondiciones
            //</editor-fold>
            () {
        int i = 0;
        
        int TAMAÑO = condiciones.size();
        long[] fecha = new long[TAMAÑO];
        long[] tiempoSiguienteEjecucion = new long[TAMAÑO];
        long tiempoActual = Calendar.getInstance().getTimeInMillis();
        for(LESDateCondition con :condiciones){
            fecha[i] = con.getFecha().getTimeInMillis();
            long tiempoDespuesFecha = con.getTiempo();
            if (tiempoDespuesFecha != LESDateCondition.NULL) {
                int tipoTiempo = con.getUnidadTiempo();
                switch (tipoTiempo) { // to2 se  deja en funcion de millisegundos
                    case LESDateCondition.TIEMPO_HORAS:
                        tiempoDespuesFecha =
                                (tiempoDespuesFecha * (60 * 60 * 1000))
                                + ((tiempoActual >= fecha[i]) ? tiempoActual : fecha[i]);

                        break;
                    case LESDateCondition.TIEMPO_MINUTOS:
                        tiempoDespuesFecha =
                                (tiempoDespuesFecha * (60 * 1000))
                                + ((tiempoActual >= fecha[i]) ? tiempoActual : fecha[i]);
                        break;
                    case LESDateCondition.TIEMPO_SEGUNDOS:
                        tiempoDespuesFecha =
                                (tiempoDespuesFecha * (1000))
                                + ((tiempoActual >= fecha[i]) ? tiempoActual : fecha[i]);
                        break;
                    case LESDateCondition.TIEMPO_MILLISEGUNDOS:
                        tiempoDespuesFecha =
                                tiempoDespuesFecha
                                + ((tiempoActual >= fecha[i]) ? tiempoActual : fecha[i]);
                        break;
                }
                tiempoSiguienteEjecucion[i] = tiempoDespuesFecha;
            } else {
                tiempoSiguienteEjecucion[i] = fecha[i];
            }

            i++;
        }
        /*-------------------------------------*/
        long _min = -1;
        int _posMin = -1;
        for(int h = 0 ; h < TAMAÑO ; h++ ){
            long diff = tiempoSiguienteEjecucion[h] - tiempoActual;
            if(h == 0 && diff > 0){
                _min = tiempoSiguienteEjecucion[h];
                _posMin = h;
            }else{
                if(tiempoSiguienteEjecucion[h] < _min && diff > 0){
                    _min = tiempoSiguienteEjecucion[h];
                    _posMin = h;
                }
            }

        }
        if (_posMin != -1) {
            hasMoreThinksToDo = true;
            millisecondsToActivate = tiempoSiguienteEjecucion[_posMin] - tiempoActual;
        } else {
            hasMoreThinksToDo = false;
            millisecondsToActivate = -1;
        }
        
    }


    
    private LESException getException(int columna) {
        return new LESException("Error en columna [" + columna + "] de " + eventoString);
    }

    
}
