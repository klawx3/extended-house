/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.main_program;


;
import cl.eh.server.ServerExtendedHouse;
import cl.eh.properties.Propiedades;
import cl.eh.properties.PropiedadesClient;
import cl.eh.properties.PropiedadesServer;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author Usuario
 */
public class MainDispatcher {
    
    private static BufferedReader leer;
    private static PropiedadesClient propiedades_cliente;
    private static PropiedadesServer propiedades_server;
    
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-c")) {
                if (Propiedades.isPropiedadesExist(PropiedadesClient.NOM_ARCHIVO)) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                           // new Cliente_Prototipo01().setVisible(true); ----------------
                        }
                    });
                    return;
                } else {
                    String ip = JOptionPane.showInputDialog("Servidor No Ingresado..Ingrese IP");
                    propiedades_cliente = new PropiedadesClient();
                    propiedades_cliente.setAllClientPropiedadesToFile(ip);
                    System.exit(0);
                }
            }
            if (args[0].equalsIgnoreCase("-s")) {
                if (Propiedades.isPropiedadesExist(PropiedadesServer.NOM_ARCHIVO)) {
                    new ServerExtendedHouse().serverThreadsStarts();
                    
                    return;
                } else {
                    String arduino_port,db_ip,db_name,db_user,db_pass;
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
                    }else{
                        arduino_port = db_ip = db_name = db_user = db_pass = "NULL";
                    }

                    propiedades_server.setAllServerPropiedadesToFile
                            (arduino_port, db_ip, db_name, db_user, db_pass);
                    System.out.println("DATOS INGRESADOS CORRECTAMENTE...QUIT");
                }
            }
        }
        System.gc();
        System.out.println("\tEl programa requiere algun argumento");
        System.out.println("\t\t-c\t[Inicia en modo Cliente (test)]");
        System.out.println("\t\t-s\t[Inicia en modo Servidor]");
        System.out.println("\tej:java -jar <nom_programa>.jar -s");
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
