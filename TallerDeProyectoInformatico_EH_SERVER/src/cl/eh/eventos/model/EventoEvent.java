/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos.model;

/**
 *
 * @author Usuario
 */
public class EventoEvent {
    public static final int ACCI_CAMBIAR = 1;
    public static final int ACCI_ENCENDER = 2;
    public static final int ACCI_APAGAR = 3;
    
    
    private String actuador;
    private int numero_actuador;
    private int accion;
    
    public EventoEvent(String actuador, int numero_actuador, int accion) {
        this.actuador = actuador;
        this.numero_actuador = numero_actuador;
        this.accion = accion;
    }
    
    public int getAccion() {
        return accion;
    }

    public String getActuador() {
        return actuador;
    }

    public int getNumero_actuador() {
        return numero_actuador;
    }
    
    
}
