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
    public static final String NOM_ARCHIVO    = "server_properties.ini";
    private static final String CARAC_ARCHIVO = "Propiedades del Servidor"
            + "\n Nota:Deve estar mysql y mysqldump en la path";
    private static final String ARDUINO_PORT  = "arduino.port";
    private static final String DATABASE_IP   = "database.ip";
    private static final String DATABASE_NOM  = "database.name";
    private static final String DATABASE_USER = "database.user";
    private static final String DATABASE_PASS = "database.pass";
    private static final String MILLIS_INSERT_DB="database.millisecond.insert";
    private static final String DEVELOPER_LOG_LEVEL = "loggin.developer";
    private static final String MILLIS_BACKUP_DB = "database.backup.milliseconds";

    private String arduino_port;
    private String database_ip;
    private String database_name;
    private String database_user;
    private String database_pass;
    private String database_millisencods_insert;
    private String loggin_level;
    private String database_millisecontsToBackup;

   

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
        database_millisencods_insert = tabla.getProperty(MILLIS_INSERT_DB);
        loggin_level = tabla.getProperty(DEVELOPER_LOG_LEVEL);
        database_millisecontsToBackup = tabla.getProperty(MILLIS_BACKUP_DB);
    }

     public void setAllServerPropiedadesToFile
             (String arduino_port,String db_ip,String db_name,String db_user,
             String db_pass,String db_milli_insert,String log_level,String db_milli_backup) {
        addPropiedadTabla(ARDUINO_PORT, arduino_port);
        addPropiedadTabla(DATABASE_IP, db_ip);
        addPropiedadTabla(DATABASE_NOM, db_name);
        addPropiedadTabla(DATABASE_USER, db_user);
        addPropiedadTabla(DATABASE_PASS, db_pass);
        addPropiedadTabla(MILLIS_INSERT_DB, db_milli_insert);
        addPropiedadTabla(DEVELOPER_LOG_LEVEL,log_level);
        addPropiedadTabla(MILLIS_BACKUP_DB,db_milli_backup);
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
    
    public String getDatabase_millisecontsToBackup(){
        return database_millisecontsToBackup;
    }

    public long getDatabase_millisencods_insert() {
        try{
            return Long.parseLong(database_millisencods_insert);
        }catch(NumberFormatException e){
            error("Verifique el archivo de configuracion at [database.millisecond.insert]");
            System.exit(1);

        }
        return -1;
    }

    public boolean getIsDeveloperLoggin_level() {
        return loggin_level.equalsIgnoreCase("true") ? true : false;
    }


    

}
