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
    private int id_actuador;
    private int id_sensor;
    private Calendar fecha;
    private String detalle;

    public Historial(int id, int id_actuador, int id_sensor, Calendar fecha, String detalle) {
        this.id = id;
        this.id_actuador = id_actuador;
        this.id_sensor = id_sensor;
        this.fecha = fecha;
        this.detalle = detalle;
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




}
