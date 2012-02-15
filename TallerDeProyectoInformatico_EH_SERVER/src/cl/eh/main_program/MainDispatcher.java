package cl.eh.main_program;

import cl.eh.logger.MyLogger;
import com.esotericsoftware.minlog.Log;
import cl.eh.server.ExtendedHouseSERVER;
import cl.eh.properties.Propiedades;
import cl.eh.properties.PropiedadesServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import static com.esotericsoftware.minlog.Log.*;

public class MainDispatcher {

    private static BufferedReader leer;
    private static PropiedadesServer propiedades_server;

    public static void main(String[] args) {
        propiedades_server = new PropiedadesServer();
        stablishLogginPlataform();
        if (Propiedades.isPropiedadesExist(PropiedadesServer.NOM_ARCHIVO)) {
            startExtendedHouse();
        } else {
            configuringExtendedHouse();
        }
    }

    public static void startExtendedHouse() {
        trace(MainDispatcher.class.getSimpleName(), "Starting ExtendedHouse...");
        propiedades_server.getAllServerPropiedadesOfFile();
        if (propiedades_server.getIsDeveloperLoggin_level()) {
            Log.set(Log.LEVEL_DEBUG);
            debug(MainDispatcher.class.getSimpleName(), "LEVEL_DEBUG ON");
        } else {
            Log.set(Log.LEVEL_INFO);
        }
        new ExtendedHouseSERVER().serverThreadsStarts();
    }

    public static void configuringExtendedHouse() {
        trace(MainDispatcher.class.getSimpleName(), "Configuring ExtendedHouse...");
        String arduino_port, db_ip, db_name, db_user, db_pass, db_insert, log_developer, db_backup;
        leer = new BufferedReader(new InputStreamReader(System.in));
        String ingreso_sn = readEntry("¨¿Desea ingresar las configuraciones para el servidor (s/n)?");
        if (isAffirmative(ingreso_sn)) {
            arduino_port = readEntry("Ingrese el PUERTO de ARDUINO:");
            db_ip = readEntry("Ingrese la IP para la BASEDATOS:");
            db_name = readEntry("Ingrese el NOMBRE de la BASEDATOS:");
            db_user = readEntry("Ingrese el USUARIO de la BASEDATOS:");
            db_pass = readEntry("Ingrese la CONTRASEÑA de la BASEDATOS:");
            db_insert = readEntry("Ingrese los MILLISEGUNDOS DE UPDATE de la BASEDATOS:");
            db_backup = readEntry("Ingrese los MILLISEGUNDOS para el BACKUP de la BASEDATOS:");
            String entrada = readEntry("¿ Desea activar el MODO DESAROLLADOR (s/n)?:");
            if (isAffirmative(entrada)) {
                log_developer = "true";
            } else {
                log_developer = "false";
            }
        } else {
            arduino_port = db_ip = db_name = db_user = db_pass = "NULL";
            db_backup = "10080000"; // 1 semana
            db_insert = "600000";
            log_developer = "false";
            System.out.println("Algunos datos deveran ser ingresados"
                    + " manualmente en el archivo de configuracion");
        }

        propiedades_server.setAllServerPropiedadesToFile(arduino_port, db_ip, db_name, db_user, db_pass, db_insert, log_developer, db_backup);
        System.out.println("DATOS INGRESADOS CORRECTAMENTE...QUIT");
    }

    public static boolean isAffirmative(String entrada) {
        return entrada.equalsIgnoreCase("si")
                || entrada.equalsIgnoreCase("yes")
                || entrada.equalsIgnoreCase("y")
                || entrada.equalsIgnoreCase("s") ? true : false;
    }

    public static String readEntry(String cadena) {
        System.out.println(cadena);
        String entrada = null;
        try {
            entrada = leer.readLine();
        } catch (IOException ex) {
            System.err.println("Error al leer entrada de usuario...Reintentar");
            readEntry(cadena);
        }
        if (entrada.isEmpty()) {
            entrada = "NULL";
        }
        return entrada;
    }

    public static void stablishLogginPlataform() {
        MyLogger logger = new MyLogger();
        try {
            logger.setLoggingFileName(PropiedadesServer.LOGGING_DEFAULT_NAME);
        } catch (IOException ex) {
            error(MainDispatcher.class.getSimpleName(), ex);
        }
        Log.setLogger(logger);
    }
}
