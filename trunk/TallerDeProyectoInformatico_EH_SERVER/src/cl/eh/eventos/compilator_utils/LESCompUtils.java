/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos.compilator_utils;

import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class LESCompUtils {

    public static final String[] PALABRAS_RECERVADAS = 
    {"en", "para", "numero", "fijar", "cada", "desde", "millisegundos","horas","minutos","segundos"};
    public static final String[] TIPOS_DE_FIJADOS =
    {"cambiar","encendido","apagado"};

    public static boolean isAPablabraRecervada(String palabra){
        for(String a : PALABRAS_RECERVADAS){
            if(a.equalsIgnoreCase(palabra)){
                return true;
            }
        }
        return false;
    }
    public static int getNumVectorPalabra(String palabra){
        for(int i = 0; i < PALABRAS_RECERVADAS.length ; i++){
            if(PALABRAS_RECERVADAS[i].equalsIgnoreCase(palabra)){
                return i;
            }
        }
        return -1;
    }
    
    public static String getTipoDeFijado(String b){
        for(String a: TIPOS_DE_FIJADOS){
            if(a.equalsIgnoreCase(b)){
                return a;
            }
        }
        return null;
    }
    public static boolean isAnTipoDeFijado(String b){
        for(String a: TIPOS_DE_FIJADOS){
            if(a.equalsIgnoreCase(b)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isDateWhithowtHour(String fec) {
        return fec.length() > 10 ? false : true;
    }
    
    /**
     * @param fecha_hora dd-mm-YYYY_hh:mm:ss o dd-mm-YYYY
     * @return null en caso de error del texto
     */
    public static Calendar getCalendar(String fechaHora) { // puede ser fecha o la hora
        if (fechaHora.length() > 10) { // viene con la hora
            String regex[] = fechaHora.split("_");
            String fec[] = regex[0].split("-");
            if (fec.length == 3) {
                int[] diaMesAño = getDiaMesAño(fec);
                if (diaMesAño != null) {
                    String hora[] = regex[1].split(":");
                    if (hora.length == 3) {
                        int[] horaMinSeg = getHoraMinutoSegundo(hora);
                        if (horaMinSeg != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(diaMesAño[2], (diaMesAño[1] - 1), diaMesAño[0],
                                    horaMinSeg[0], horaMinSeg[1], horaMinSeg[2]);
                            return cal;
                        }
                    }
                }
            }
        } else {
            String regex[] = fechaHora.split("-");
            if (regex.length == 3) {
                int[] diaMesAño = getDiaMesAño(regex);
                if (diaMesAño != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(diaMesAño[2], (diaMesAño[1] - 1), diaMesAño[0]);
                    
                    return cal;
                }
            }
        }
        return null;
    }

    private static int[] getDiaMesAño(String regex[]) {
        try {
            int dia = Integer.parseInt(regex[0]);
            int mes = Integer.parseInt(regex[1]);
            int año = Integer.parseInt(regex[2]);
            return new int[]{dia, mes, año};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int[] getHoraMinutoSegundo(String regex[]) {
        try {
            int hora = Integer.parseInt(regex[0]);
            int minuto = Integer.parseInt(regex[1]);
            int segundo = Integer.parseInt(regex[2]);
            return new int[]{hora, minuto, segundo};
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}
