/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class RespaldoBd implements Serializable,Cloneable {
    
    private String nom;
    private Calendar fecha;
    private String contenido;
    private boolean isRespaldoByUsuario;

    
    
    public RespaldoBd(String nom, Calendar fecha, String contenido,boolean isRespaldoByUsuario) {
        this.nom = nom;
        this.fecha = fecha;
        this.contenido = contenido;
        this.isRespaldoByUsuario = isRespaldoByUsuario;
    }


    public Calendar getFecha() {
        return fecha;
    }

    public String getNom() {
        return nom;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha; 
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean equals(RespaldoBd otro_res){
        return contenido.equals(otro_res.getContenido()) 
                && fecha.equals(otro_res.getFecha())
                && nom.equals(otro_res.nom);
    }
    
    public boolean isIsRespaldoByUsuario() {
        return isRespaldoByUsuario;
    }

    public void setIsRespaldoByUsuario(boolean isRespaldoByUsuario) {
        this.isRespaldoByUsuario = isRespaldoByUsuario;
    }
    
    @Override
    public Object clone(){
        try {
            RespaldoBd other = (RespaldoBd) super.clone();
            other.contenido = this.contenido;
            other.fecha = (Calendar) this.fecha.clone();
            other.nom = this.nom;
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
    
}
