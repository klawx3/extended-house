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
class Respaldo implements Serializable {
    
    private String nom;
    private Calendar fecha;
    private String contenido;

    

    public Respaldo(String nom, Calendar fecha, String contenido) {
        this.nom = nom;
        this.fecha = fecha;
        this.contenido = contenido;
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

    public boolean equals(Respaldo otro_res){
        return contenido.equals(otro_res.getContenido()) 
                && fecha.equals(otro_res.getFecha())
                && nom.equals(otro_res.nom);
    }
    
}
