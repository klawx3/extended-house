/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import cl.eh.eventos.model.EventoEvent;
import cl.eh.exceptions.ArduinoIOException;
import cl.eh.exceptions.LESException;
import cl.eh.properties.Propiedades;
import cl.eh.util.Interface;
import cl.eh.util.Ip;
import cl.eh.util.NetworksInterfaces;
import cl.eh.db.util.MysqlCalendar;
import cl.eh.eventos.model.EventoListener;
import cl.eh.eventos.LESAdministador;
import cl.eh.eventos.LESObject;
import cl.eh.eventos.compilator_utils.LESCompUtils;
import cl.eh.eventos.model.LESCondition;
import cl.eh.eventos.model.LESDateCondition;
import cl.eh.util.Archivo;
import cl.eh.util.Arreglos;
import cl.eh.util.Fecha2;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ArduinoIOException, IOException {
//        long[] a = new long[]{5,4,1,55,5,23,-67,2};
//        int posMax = Arreglos.getPositonOfMaxValue(a);
//        System.out.println(a[posMax]);
//        
        String lesString = "EN rl NUMERO 2 FIJAR encendido "
                + "PARA 12-12-2012";
        String lesString2 = "EN rl NUMERO 7 FIJAR apagado "
                + "PARA 01-01-2012 CADA 3000 MILLISEGUNDOS "
                + "Y "
                + "PARA 01-02-2012 CADA 1 HORAS "
                + "Y "
                + "PARA 13-03-2012_23:55:02";
                
        try {
            LESObject les = new LESObject(lesString2);
            EventoEvent evt = les.getEventoObject();
            System.out.println("Actuador:\r\t\t" + evt.getActuador());
            if (evt.getAccion() == EventoEvent.ACCI_APAGAR) {
                System.out.println("Accion:\r\t\tApagar");
            } else if (evt.getAccion() == EventoEvent.ACCI_CAMBIAR) {
                System.out.println("Accion:\r\t\tCambiar");
            } else {
                System.out.println("Accion:\r\t\tEncender");
            }
            System.out.println("NumeroActuador:\r\t\t" + evt.getNumero_actuador());
            for (LESDateCondition con : les.getLESConditions()) {
                System.out.println("|----------------------------|");
                System.out.println("Fecha:\r\t\t" + Fecha2.getFecha(con.getFecha(), '-'));
                if(!con.isSinHoras()){
                    System.out.println("Hora:\r\t\t" + Fecha2.getHora(con.getFecha(), ':'));
                }
                System.out.println("NextCondicion:\r\t\t" + con.getNextCondicion());
                System.out.println("Tiempo:\r\t\t" + con.getTiempo());
                String time = null;
                switch (con.getUnidadTiempo()) {
                    case LESDateCondition.TIEMPO_HORAS:
                        time = "Horas";
                        break;
                    case LESDateCondition.TIEMPO_MILLISEGUNDOS:
                        time = "Millisegundos";
                        break;
                    case LESDateCondition.TIEMPO_SEGUNDOS:
                        time = "Segundos";
                        break;
                    case LESDateCondition.TIEMPO_MINUTOS:
                        time = "Minutos";
                        break;
                }
                System.out.println("UnidadTiempo:\r\t\t" + time);
            }
        } catch (LESException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    //        LESAdministador les = new LESAdministador(20);
    //        
    //        les.addEventoListener(new EventoListener() {
    //
    //            public void eventoPerformed(EventoEvent e) {
    //                System.out.println(e.getNumero_actuador());
    //                System.out.println(e.getAccion());
    //                System.out.println(e.getActuador());
    //                
    //            }
    //        });
            
    //        Calendar cal = LESCompUtils.getCalendar("12-04-2004");
    //        if(cal != null){
    //            String fecha = Fecha2.getFecha(cal, '/');
    //            String hora = Fecha2.getHora(cal, ':');
    //            System.out.println(fecha+" "+hora);
    //        }else{
    //            }     
    //        }
                
        //        NewClass nc = new NewClass();
                //System.out.println(Archivo.recuperarTextoArchivo("c:\\hola.txt"));
                
                /*String a = "2011-07-13 21:21:5";
                String date = "";
                Calendar asd = MysqlCalendar.getCalendarFromMysqString(a);
                Calendar s = GregorianCalendar.getInstance();

                System.out.println(MysqlCalendar.getMysqlDateString(s));*/
        //        Propiedades prop = new Propiedades();
        //        prop.crearPropiedades("localhost");
        //        try {
        //            prop.guardadPropiedades();
        //        } catch (IOException ex) {
        //            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        ////        }
        //        throw new ArduinoIOException("Error al leer:");
        //        NetworksInterfaces nf = new NetworksInterfaces();
        //        List<Interface> interf = nf.getInterfaces();
        //        for(Interface inter : interf){
        //            if (inter.isActive()) {
        //                System.out.println(inter.getNombreInterface());
        //                System.out.println(inter.getNombreDispositivo());
        //                for(Ip ips:inter.getIps()){
        //                    System.out.println(ips.getIpString());
        //                    System.out.println(ips.getNetworkString());
        //                }
        //                System.out.println("-----------------------------------");
        //            }     
        //        }


        
   }

}
