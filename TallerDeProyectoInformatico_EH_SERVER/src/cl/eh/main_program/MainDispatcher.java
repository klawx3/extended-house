/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.main_program;





import com.esotericsoftware.minlog.Log;
import cl.eh.server.ExtendedHouseSERVER;
import cl.eh.properties.Propiedades;
import cl.eh.properties.PropiedadesServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Usuario
 */
public class MainDispatcher {
    
    private static BufferedReader leer;
    private static PropiedadesServer propiedades_server;

    public static void main(String[] args) {

        if (Propiedades.isPropiedadesExist(PropiedadesServer.NOM_ARCHIVO)) {
            propiedades_server = new PropiedadesServer();
            propiedades_server.getAllServerPropiedadesOfFile();
            if(propiedades_server.getIsDeveloperLoggin_level()){
                Log.set(Log.LEVEL_DEBUG);
                debug("MainDispatcher","Developer Mode ON");
            }else{
                Log.set(Log.LEVEL_INFO);
            }
            new ExtendedHouseSERVER().serverThreadsStarts();
            return;
        } else {
            String arduino_port, db_ip, db_name, db_user, db_pass, db_insert,log_developer,db_backup;

            leer = new BufferedReader(new InputStreamReader(System.in));
            propiedades_server = new PropiedadesServer();
            System.out.println("¨¿Desea ingresar las configuraciones para el servidor (s/n)?");
            String ingreso_sn = leerEntrada();
            if (ingreso_sn.equalsIgnoreCase("s") || ingreso_sn.equalsIgnoreCase("y")
                    || ingreso_sn.equalsIgnoreCase("yes") || ingreso_sn.equalsIgnoreCase("si")) {
                System.out.println("Ingrese el PUERTO de ARDUINO:");
                arduino_port = leerEntrada();
                System.out.println("Ingrese la IP para la BASEDATOS:");
                db_ip = leerEntrada();
                System.out.println("Ingrese el NOMBRE de la BASEDATOS:");
                db_name = leerEntrada();
                System.out.println("Ingrese el USUARIO de la BASEDATOS:");
                db_user = leerEntrada();
                System.out.println("Ingrese la CONTRASEÑA de la BASEDATOS:");
                db_pass = leerEntrada();
                System.out.println("Ingrese los MILLISEGUNDOS DE UPDATE de la BASEDATOS:");
                db_insert = leerEntrada();
                System.out.println("Ingrese los MILLISEGUNDOS para el BACKUP de la BASEDATOS:");
                db_backup = leerEntrada();
                System.out.println("¿ Desea activar el MODO DESAROLLADOR (s/n)?:");
                String entrada = leerEntrada();
                if (entrada.equalsIgnoreCase("si")
                        || entrada.equalsIgnoreCase("yes")
                        || entrada.equalsIgnoreCase("y")
                        || entrada.equalsIgnoreCase("s")) {
                    log_developer = "true";
                } else {
                    log_developer = "false";
                }
            } else {
                arduino_port = db_ip = db_name = db_user = db_pass = "NULL";
                db_backup = "10080000"; // 1 semana
                db_insert = "600000";
                log_developer ="false";
            }

            propiedades_server.setAllServerPropiedadesToFile
                    (arduino_port, db_ip, db_name, db_user, db_pass, db_insert,log_developer,db_backup);
            System.out.println("DATOS INGRESADOS CORRECTAMENTE...QUIT");
        }
    }
    public static String leerEntrada(){
        String entrada = null;
        try {
            entrada = leer.readLine();
        } catch (IOException ex) {
            System.err.println("Error al leer entrada de usuario...Reintentar");
            leerEntrada();
        }
        if (entrada.isEmpty()) {
            entrada = "NULL";
        }
        return entrada;
    }
    
}
