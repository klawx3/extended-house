/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.model;

/**
 *
 * @author Usuario
 */
public class ActuadorInfo {
    private int id;
    private String nombre;
    private Integer numero;

    public ActuadorInfo(int id, String nombre, Integer numero) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
    }


    public String getNombre() {
        return nombre;
    }

    
    public int getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }
    
    
    
}
