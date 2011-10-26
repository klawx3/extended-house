/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import cl.eh.exceptions.ArduinoIOException;
import cl.eh.properties.Propiedades;
import cl.eh.util.Interface;
import cl.eh.util.Ip;
import cl.eh.util.NetworksInterfaces;
import cl.eh.db.util.MysqlCalendar;
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
    public static void main(String[] args) throws ArduinoIOException {
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
        NetworksInterfaces nf = new NetworksInterfaces();
        List<Interface> interf = nf.getInterfaces();
        for(Interface inter : interf){
            if (inter.isActive()) {
                System.out.println(inter.getNombreInterface());
                System.out.println(inter.getNombreDispositivo());
                for(Ip ips:inter.getIps()){
                    System.out.println(ips.getIpString());
                    System.out.println(ips.getNetworkString());
                }
                System.out.println("-----------------------------------");
            }     
        }
    }

}
