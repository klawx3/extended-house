/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino.model;

import java.util.EventObject;

/**
 *
 * @author Usuario
 */
public class ArduinoEvent extends EventObject {
    private String nombreDisositivo;
    private int numeroDispositovo;
    private float valorDispositivo;

    public ArduinoEvent(Object source, String nombreDisositivo, int numeroDispositovo, float valorDispositivo) {
        super(source);
        this.nombreDisositivo = nombreDisositivo;
        this.numeroDispositovo = numeroDispositovo;
        this.valorDispositivo = valorDispositivo;
    }

    public String getNombreDisositivo() {
        return nombreDisositivo;
    }

    public int getNumeroDispositovo() {
        return numeroDispositovo;
    }

    public float getValorDispositivo() {
        return valorDispositivo;
    }

}
