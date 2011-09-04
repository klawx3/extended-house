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
public class Historial {
    private int id;
    private Actuador actuador;
    private Sensor sensor;
    private Calendar fecha;
    private String ip;
    private String detalle;
    
     public Historial(int id, Actuador actuador, Sensor sensor, Calendar fecha, String ip, String detalle) {
        this.id = id;
        this.actuador = actuador;
        this.sensor = sensor;
        this.fecha = fecha;
        this.ip = ip;
        this.detalle = detalle;
    }

    public Actuador getActuador() {
        return actuador;
    }

    public void setActuador(Actuador actuador) {
        this.actuador = actuador;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
