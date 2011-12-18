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

    private String evento_simple;
    private boolean activo;
    private Usuario usuario;

    public Evento() {
    }

    public Evento(String evento_simple, boolean activo, Usuario usuario) {
        this.evento_simple = evento_simple;
        this.activo = activo;
        this.usuario = usuario;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setEvento_simple(String evento_simple) {
        this.evento_simple = evento_simple;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getEvento_simple() {
        return evento_simple;
    }

    public Usuario getUsuario() {
        return usuario;
    }


    
    

}
