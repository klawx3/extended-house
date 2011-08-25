/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db.model;

/**
 *
 * @author Usuario
 */
public class Actuador {

    private int id;
    private String nombre;
    private String ubicacion;
    private String puntero; //en binario
    private int valor;

    public Actuador
            (int id, String nombre, String ubicacion, String puntero, int valor){
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.puntero = puntero;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntero() {
        return puntero;
    }

    public void setPuntero(String puntero) {
        this.puntero = puntero;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }





}
