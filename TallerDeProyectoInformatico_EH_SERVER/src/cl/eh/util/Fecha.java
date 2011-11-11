/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.util;

import java.util.Calendar;

/**
 *
 * @author Administrador
 */
public class Fecha {
    private static String dia, mes, anio;
    private static String nomDia;
    private static int semanaDelAño;
    private static Calendar fecha = Calendar.getInstance();
    private static final int DIA = 0, MES = 1, ANIO = 2;

    public static String getFecha(boolean fechaSoloNumeros, boolean fechaConSlash){
        construirFecha();
        if(fechaSoloNumeros){
            return dia + (fechaConSlash?"/":"-") + (fecha.get(Calendar.MONTH)+1) + (fechaConSlash?"/":"-") +anio;
        }else{
            return dia + (fechaConSlash?"/":"-") + mes + (fechaConSlash?"/":"-") +anio;
        }
    }

    public static String getMes(){
        construirFecha();
        return mes;
    }

    public static int getNumeroDeMes(){
        fecha = Calendar.getInstance();
        return fecha.get(Calendar.MONTH)+1;
    }

    /**
     *
     * @param mes del tipo ene, feb, mar etc.
     * @return
     */
    public static int getNumeroDeMes(String mes){
        int nmes = 0;
        if(mes.equalsIgnoreCase("ene")){
            nmes = Calendar.JANUARY;
        }else if(mes.equalsIgnoreCase("feb")){
            nmes = Calendar.FEBRUARY;
        }else if(mes.equalsIgnoreCase("mar")){
            nmes = Calendar.MARCH;
        }else if(mes.equalsIgnoreCase("abr")){
            nmes = Calendar.APRIL;
        }else if(mes.equalsIgnoreCase("may")){
            nmes = Calendar.MAY;
        }else if(mes.equalsIgnoreCase("jun")){
            nmes = Calendar.JUNE;
        }else if(mes.equalsIgnoreCase("jul")){
            nmes = Calendar.JULY;
        }else if(mes.equalsIgnoreCase("ago")){
            nmes = Calendar.AUGUST;
        }else if(mes.equalsIgnoreCase("sep")){
           nmes = Calendar.SEPTEMBER;
        }else if(mes.equalsIgnoreCase("oct")){
            nmes = Calendar.OCTOBER;
        }else if(mes.equalsIgnoreCase("nov")){
            nmes = Calendar.NOVEMBER;
        }else if(mes.equalsIgnoreCase("dic")){
            nmes = Calendar.DECEMBER;
        }else return Integer.parseInt(mes);


        fecha = Calendar.getInstance();
        fecha.set(Calendar.MONTH, nmes);
        return fecha.get(Calendar.MONTH)+1;
    }

    public static String getMes(String mes){
        construirFecha();
        if(mes.equalsIgnoreCase("ene")){
            return "Enero";
        }else if(mes.equalsIgnoreCase("feb")){
            return "Febrero";
        }else if(mes.equalsIgnoreCase("mar")){
            return "Marzo";
        }else if(mes.equalsIgnoreCase("abr")){
            return "Abril";
        }else if(mes.equalsIgnoreCase("may")){
            return "Mayo";
        }else if(mes.equalsIgnoreCase("jun")){
            return "Junio";
        }else if(mes.equalsIgnoreCase("jul")){
            return "Julio";
        }else if(mes.equalsIgnoreCase("ago")){
            return "Agosto";
        }else if(mes.equalsIgnoreCase("sep")){
            return "Septiembre";
        }else if(mes.equalsIgnoreCase("oct")){
            return "Octubre";
        }else if(mes.equalsIgnoreCase("nov")){
            return "Noviembre";
        }else if(mes.equalsIgnoreCase("dic")){
            return "Diciembre";
        }
        return null;
    }

    public static String getDia(){
        construirFecha();
        return dia;
    }

    /**
     * @return the nomDia
     */
    public static String getNomDia() {
        construirFecha();
        return nomDia;
    }

    public static String getAño(){
        construirFecha();
        return anio;
    }

    /**
     * @return the semanaDelAño
     */
    public static int getSemanaDelAño() {
        construirFecha();
        return semanaDelAño;
    }

    private static void construirFecha() {
        fecha = Calendar.getInstance();
        semanaDelAño = fecha.get(Calendar.WEEK_OF_YEAR);
        dia = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
        mes = Integer.toString(fecha.get(Calendar.MONTH)+1);
        nomDia = Integer.toString(fecha.get(Calendar.DAY_OF_WEEK));


        if(nomDia.equalsIgnoreCase("1")){
            nomDia = "Domingo";
        }else if(nomDia.equalsIgnoreCase("2")){
            nomDia = "Lunes";
        }else if(nomDia.equalsIgnoreCase("3")){
            nomDia = "Martes";
        }else if(nomDia.equalsIgnoreCase("4")){
            nomDia = "Miércoles";
        }else if(nomDia.equalsIgnoreCase("5")){
            nomDia = "Jueves";
        }else if(nomDia.equalsIgnoreCase("6")){
            nomDia = "Viernes";
        }else if(nomDia.equalsIgnoreCase("7")){
            nomDia = "Sábado";
        }

        if(mes.equalsIgnoreCase("1")){
            mes = "ene";
        }else if(mes.equalsIgnoreCase("2")){
            mes = "feb";
        }else if(mes.equalsIgnoreCase("3")){
            mes = "mar";
        }else if(mes.equalsIgnoreCase("4")){
            mes = "abr";
        }else if(mes.equalsIgnoreCase("5")){
            mes = "may";
        }else if(mes.equalsIgnoreCase("6")){
            mes = "jun";
        }else if(mes.equalsIgnoreCase("7")){
            mes = "jul";
        }else if(mes.equalsIgnoreCase("8")){
            mes = "ago";
        }else if(mes.equalsIgnoreCase("9")){
            mes = "sep";
        }else if(mes.equalsIgnoreCase("10")){
            mes = "oct";
        }else if(mes.equalsIgnoreCase("11")){
            mes = "nov";
        }else if(mes.equalsIgnoreCase("12")){
            mes = "dic";
        }
        anio = Integer.toString(fecha.get(Calendar.YEAR));
    }

    private static int getDiaMayor(int mes, int anio){
        fecha = Calendar.getInstance();
        fecha.set(Calendar.MONTH, mes-1);
        fecha.set(Calendar.YEAR, anio);
        return fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    /**
     *
     * @param fecha1 del tipo "dia-mes-año"
     * @param fecha2 del tipo "dia-mes-año"
     *
     * fecha 1 tiene que ser menor a fecha 2
     * @return
     */
    public static int getDiferenciaEnDias(String fecha1, String fecha2){
        int d1, m1, a1, d2, m2, a2;

        String[] f1 = fecha1.split("-");
        String[] f2 = fecha2.split("-");

        d1 = Integer.parseInt(f1[DIA]);
        m1 = Integer.parseInt(f1[MES]);
        a1 = Integer.parseInt(f1[ANIO]);

        d2 = Integer.parseInt(f2[DIA]);
        m2 = Integer.parseInt(f2[MES]);
        a2 = Integer.parseInt(f2[ANIO]);

        //acumulador de dias
        int acu = 0;
        //mientras los meses sean distintos o los años sean distintos
        while(m1 != m2 || a1 != a2){
            //acumulo los dias del mes y del año
            acu += getDiaMayor(m1, a1);
            //aumento en uno el mes, osea avanzo
            m1++;
            //aca pregunto si el mes que recien procese es Diciembre
            if(m1 == 13){
                //porque si es asi, aumento el año en 1
                a1++;
                //y dejo el mes en enero
                m1 = 1;
            }
        }
        //cuando se sale del while, es porque las fechas son iguales
        //y otra vez acumulo los dias del mes y año
        acu += getDiaMayor(m1, a1);

        //aca a la suma de los dias actual (acu), le tengo que restar el numero
        //de dias de la fecha uno (d1), mas los dias restantes del mes 2(getDiaMayor(m1, a1)-d2)
        return Math.abs(acu-(d1+(getDiaMayor(m1, a1)-d2)));
    }

    /**
     *
     * @param fecha del tipo "dia/mes/año" donde mes, esta en palabras como ene, feb etc;
     *
     * @return
     * retorna el equivalente en numeros, ejemplo
     * si le pasas 01/ene/2010 retornara 01-01-2010
     */
    public static String cambiarFecha(String fecha, boolean laFechaEstaConSlash){
        int m;
        String[] fec;
        fecha = fecha.replaceAll((!laFechaEstaConSlash ? "/":"-"), (!laFechaEstaConSlash ? "-":"/"));
        fec = fecha.split((!laFechaEstaConSlash ? "-":"/"));

        m = getNumeroDeMes(fec[MES]);

        return fec[DIA]+(!laFechaEstaConSlash ? "-":"/")+m+(!laFechaEstaConSlash ? "-":"/")+fec[ANIO];
    }
//    public static void main(String args[]) {
//        Fecha f = new Fecha();
//        System.out.println(f.getDiferenciaEnDias("14-11-2010", "31-12-2010"));
//    }
}
