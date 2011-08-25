/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db.model;

import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class Evento {
    private int id;
    private String nombre_evento;
    private int id_actuador;
    private int id_sensor;
    private int id_sensor_sec;
    private Calendar tiempo;
    private boolean incluyente;

    public Evento(int id, String nombre_evento, int id_actuador, int id_sensor, int id_sensor_sec, Calendar tiempo, boolean incluyente) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.id_actuador = id_actuador;
        this.id_sensor = id_sensor;
        this.id_sensor_sec = id_sensor_sec;
        this.tiempo = tiempo;
        this.incluyente = incluyente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_actuador() {
        return id_actuador;
    }

    public void setId_actuador(int id_actuador) {
        this.id_actuador = id_actuador;
    }

    public int getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(int id_sensor) {
        this.id_sensor = id_sensor;
    }

    public int getId_sensor_sec() {
        return id_sensor_sec;
    }

    public void setId_sensor_sec(int id_sensor_sec) {
        this.id_sensor_sec = id_sensor_sec;
    }

    public boolean isIncluyente() {
        return incluyente;
    }

    public void setIncluyente(boolean incluyente) {
        this.incluyente = incluyente;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public Calendar getTiempo() {
        return tiempo;
    }

    public void setTiempo(Calendar tiempo) {
        this.tiempo = tiempo;
    }


    
    

}
