/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.properties;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Usuario
 */
public final class PropiedadesServer extends Propiedades {
    public static final String NOM_ARCHIVO    = "propServer.dat";
    private static final String CARAC_ARCHIVO = "Propiedades del Servidor";
    private static final String ARDUINO_PORT  = "arduino.port";
    private static final String DATABASE_IP   = "database.ip";
    private static final String DATABASE_NOM  = "database.name";
    private static final String DATABASE_USER = "database.user";
    private static final String DATABASE_PASS = "database.pass";

    private String arduino_port;
    private String database_ip;
    private String database_name;
    private String database_user;
    private String database_pass;

    public PropiedadesServer(){
        super(NOM_ARCHIVO,CARAC_ARCHIVO);
        arduino_port = null;
    }

     public void getAllServerPropiedadesOfFile(){
        try {
            cargarPropiedades();
        } catch (IOException ex) {
            error(PropiedadesServer.class.getSimpleName()
                    ,"Error obteniendo las cofiguraciones del servidor");
        }
        arduino_port = tabla.getProperty(ARDUINO_PORT);
        database_ip = tabla.getProperty(DATABASE_IP);
        database_name = tabla.getProperty(DATABASE_NOM);
        database_user = tabla.getProperty(DATABASE_USER);
        database_pass = tabla.getProperty(DATABASE_PASS);
    }

     public void setAllServerPropiedadesToFile
             (String arduino_port,String db_ip,String db_name,String db_user,String db_pass) {
        addPropiedadTabla(ARDUINO_PORT, arduino_port);
        addPropiedadTabla(DATABASE_IP, db_ip);
        addPropiedadTabla(DATABASE_NOM, db_name);
        addPropiedadTabla(DATABASE_USER, db_user);
        addPropiedadTabla(DATABASE_PASS, db_pass);
        try {
            super.guardadPropiedades();
        } catch (IOException ex) {
            error(PropiedadesServer.class.getSimpleName()
                    ,"Error guardando las cofiguraciones del servidor");
        }
    }

    public String getArduino_port() {
        return arduino_port;
    }
    
    public String getDatabase_ip() {
        return database_ip;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public String getDatabase_pass() {
        return database_pass;
    }

    public String getDatabase_user() {
        return database_user;
    }


    

}
