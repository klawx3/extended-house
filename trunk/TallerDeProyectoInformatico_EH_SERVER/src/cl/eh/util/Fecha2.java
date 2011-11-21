/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class Fecha2 {
    
    public static String getFecha(final Calendar cal,char separador){
        StringBuilder sb = new StringBuilder();
        int dia,mes,año;
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH)+1;
        año = cal.get(Calendar.YEAR);
        sb.append(dia);
        sb.append(separador);
        sb.append(mes);
        sb.append(separador);
        sb.append(año);
        return sb.toString();
    }
    
    public static String getHora(final Calendar cal,char separador){
        StringBuilder sb = new StringBuilder();
        int hora,min,seg;
        hora = cal.get(Calendar.HOUR);
        min = cal.get(Calendar.MINUTE);
        seg = cal.get(Calendar.SECOND);
        sb.append(hora);
        sb.append(separador);
        sb.append(min);
        sb.append(separador);
        sb.append(seg);
        sb.append(" ");
        sb.append(cal.get(Calendar.AM_PM) == 1 ? "PM" : "AM");
        return sb.toString();
    }
    
}
