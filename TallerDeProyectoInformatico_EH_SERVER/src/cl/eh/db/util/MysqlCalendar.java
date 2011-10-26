/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Usuario
 */
public class MysqlCalendar {

    //ejemplo fecha mysql latin -> 2011-07-13 21:21:5
    public static String getMysqlDateString(Calendar cal){
        String date = "";
        date+=cal.get(Calendar.YEAR)+"-";
        date+=(cal.get(Calendar.MONTH)+1)+"-"; // ojo con el mes
        date+=cal.get(Calendar.DATE)+" ";
        date+=cal.get(Calendar.HOUR_OF_DAY)+":";
        date+=cal.get(Calendar.MINUTE)+":";
        date+=cal.get(Calendar.SECOND);
        return date;
    }

    public static Calendar getCalendarFromMysqString(String linea){
        Calendar cal = GregorianCalendar.getInstance();
        try {
            linea = linea.trim();
            String ano, mes, dia, hora, minuto, segundo;
            String[] sd = linea.split(" ");
            String[] amd = sd[0].split("-");
            ano = amd[0];
            mes = amd[1];
            System.out.println("mes" + mes);
            dia = amd[2];
            //fin date
            String[] hms = sd[1].split(":");
            hora = hms[0];
            minuto = hms[1];
            segundo = hms[2];
            System.out.println(Integer.parseInt(ano) + " " + Integer.parseInt(mes) + 1 + " " + Integer.parseInt(dia)
                    + " " + Integer.parseInt(hora) + " " + Integer.parseInt(minuto) + " " + Integer.parseInt(segundo));
            cal.set(Integer.parseInt(ano), Integer.parseInt(mes) + 1, Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto), Integer.parseInt(segundo));
        } catch (Exception e) {
            return null;
        }

        return cal;
    }
}
