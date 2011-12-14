/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos.model;

import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class LESDateCondition implements LESCondition {
    public static final int NULL = -1;
    
    public static final int TIEMPO_MILLISEGUNDOS = 100;
    public static final int TIEMPO_SEGUNDOS = 101;
    public static final int TIEMPO_MINUTOS = 102;
    public static final int TIEMPO_HORAS = 103;
    
    private Calendar fecha;
    private long tiempo;
    private int unidadTiempo;
    private String desdeFecha;
    private int nextCondition;
    private boolean sinHoras;
    
    public LESDateCondition(Calendar fecha,boolean sinHoras){
        this.fecha = fecha;
        nextCondition = unidadTiempo = NULL;
        tiempo = -1l;
        desdeFecha = null;
        this.sinHoras = sinHoras;
    }
    public boolean isSinHoras(){
        return sinHoras;
    }
    
    public String getDesdeFecha() {
        return desdeFecha;
    }
    public void setDesdeFecha(String desdeFecha) {
        this.desdeFecha = desdeFecha;
    }
    public Calendar getFecha() {
        return fecha;
    }
    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }
    public long getTiempo() {
        return tiempo;
    }
    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
    public int getUnidadTiempo() {
        return unidadTiempo;
    }
    public void setUnidadTiempo(int unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }
    public int getNextCondicion() {
        return nextCondition;
    }
    public void setNextCondition(int condition) {
        nextCondition = condition;
    }
}
