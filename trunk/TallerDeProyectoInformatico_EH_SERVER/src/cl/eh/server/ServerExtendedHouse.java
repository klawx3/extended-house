/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.server;

import cl.eh.util.NetworksInterfaces;
import cl.eh.util.Ip;
import cl.eh.util.Interface;
import java.util.Vector;
import cl.eh.server.ServerExtendedHouse.ExtendedHouseConnection;
import cl.eh.common.Network.ValidacionConnection;
import cl.eh.arduino.SerialArduino;
import cl.eh.common.Network;
import cl.eh.common.Network.*;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.exceptions.ArduinoIOException;
import cl.eh.properties.PropiedadesServer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

import static com.esotericsoftware.minlog.Log.*;

/**
 *
 * @author Usuario
 */
public final class ServerExtendedHouse implements Runnable,SerialPortEventListener {
    private static final String VERSION = "0.2.2";
    private static final String SECTOR = ServerExtendedHouse.class.getSimpleName();
    private static final String WELCOME = ">>Bienvenid@ a EXTENDED HOUSE v"+VERSION+"<<";
    private static Vector<String> vector_adru_str;
    private static  SerialArduino serial_arduino;
    private static int posicion_lista_arduino_string = 0;
    private static Server server;
    private PropiedadesServer propiedades_server;
    private BufferedReader leer;
    private ConexionExtendedHouse conexion_basedatos;
    private NetworksInterfaces net_interfaces;

    public ServerExtendedHouse(){
        Log.set(Log.LEVEL_DEBUG);
        vector_adru_str = new Vector<String>();
        leer = new BufferedReader(new InputStreamReader(System.in));
        propiedades_server = new PropiedadesServer();
        propiedades_server.getAllServerPropiedadesOfFile();
        serial_arduino = new SerialArduino(propiedades_server.getArduino_port());
        server = new Server() {

            @Override
            protected Connection newConnection() {
                return new ExtendedHouseConnection();
            }
        };
        Network.register(server);
        /*------------------------START SERVER LISTENER----------------------*/
        server.addListener(new Listener(){
            @Override
            public void received (Connection c, Object object) {
                ExtendedHouseConnection ex_con = (ExtendedHouseConnection)c;
                if(object instanceof ValidacionConnection){ // seria la primera señal
                    ValidacionConnection val_con = (ValidacionConnection)object;
                    if(val_con.user.equals(Network.LOCALUSER)){
                        ex_con.isValidConnection = true;
                        debug(SECTOR,"Coneccion ["+val_con.user+"] Aceptada");
                    }else{ // se valida en la base de datos .. ya que es externo
                        debug(SECTOR,"Coneccion ["+val_con.user+"] rechasada");
                        server.sendToTCP(ex_con.getID(), new InvalidConnection());
                        ex_con.isInvalidConnectionRequestSended = true;
                        ex_con.close();
                    }
                }else if(ex_con.isValidConnection){ // si la coneccion el valida me comunico con el cliente
                    
                    /*--------------------------END OBJECTOS A MANIPULAR-----------------------*/
                    if (object instanceof ArduinoInput) {
                        ArduinoInput ai = (ArduinoInput) object;
                        serial_arduino.enviarSeñal(ai.señal);
                    } else if (object instanceof DatabaseQuery) {
                        DatabaseQuery dq = (DatabaseQuery) object;
                    } else if (object instanceof AdvanceCommand) {
                        verificarComando(((AdvanceCommand) object).commando);
                    }
                    /*--------------------------END OBJECTOS A MANIPULAR-----------------------*/
                }
            }
            @Override
            public void disconnected (Connection c) {

            }
        });
        /*------------------------END SERVER LISTENER-------------------------*/

        try {
            server.bind(Network.getNetworkPort());
        } catch (IOException ex) {
            error(SECTOR,ex.getMessage());
            error(SECTOR,"Verifique que el puerto '"
                    +Network.getNetworkPort()+"' no este en uso...");
            System.exit(1);
        }
        server.start();

        /*-------------------------------END SERVIDOR-------------------------*/
        if (serial_arduino.isConnecionArduinoEstablecida()) {
            try {
                serial_arduino.addEventListener(this);
            } catch (TooManyListenersException ex) {
                error(SECTOR,ex.getMessage());
            }
        }
        conexion_basedatos = new ConexionExtendedHouse(propiedades_server.getDatabase_ip(),
                propiedades_server.getDatabase_name(),
                propiedades_server.getDatabase_user(),
                propiedades_server.getDatabase_pass()); // Conexion a Base de datos
        
        net_interfaces = new NetworksInterfaces();
        List<Interface> interfaces = net_interfaces.getInterfaces();
        for(Interface inter: interfaces){
            if(inter.isActive()){
                debug(SECTOR,"[Dispositivo]:"+ inter.getNombreDispositivo()
                        +"\n\t\t\t[Interface]:"+inter.getNombreInterface() );
                for(Ip ip : inter.getIps()){
                        System.out.println("\t\t\t"+ip.getIpString()+"\\"+ip.getMask());     
                }
            }
        }
        System.out.println("=========================================");   
        
    }

    public static void main(String[] args){
        ServerExtendedHouse ss = new ServerExtendedHouse();
        ss.serverThreadsStarts();
    }

    public void serialEvent(SerialPortEvent evento) {
        if (evento.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int available = SerialArduino.input.available();
                byte chunk[] = new byte[available];
                SerialArduino.input.read(chunk, 0, available);
                String incomplete_arduino_string = new String(chunk);

                vector_adru_str.add(posicion_lista_arduino_string, incomplete_arduino_string);
                if (incomplete_arduino_string.contains(";")) {
                    String contenido = "";
                    for (int i = 0; i < vector_adru_str.size(); i++) {
                        contenido += vector_adru_str.get(i).trim();
                    }//despues de for me qda la lineaAR bien echa...
                    vector_adru_str.clear();
                    posicion_lista_arduino_string = 0;
                    
                    String[] nomAndVal = contenido.split("|");
                    if (nomAndVal.length != 2) {
                        throw new ArduinoIOException("Error al leer:"+contenido);
                    } else {
                        ArduinoOutput arduino_output = new ArduinoOutput();
                        arduino_output.dispositivo = nomAndVal[0];
                        try {
                            arduino_output.valor = Long.parseLong(nomAndVal[1]);
                            arduino_output.dispositivo = contenido;
                            server.sendToAllTCP(arduino_output); /*OJOO !!!!!!*/
                        } catch (NumberFormatException e) {
                            debug(SECTOR,e.getMessage());
                        }
                    }
                } else {
                    posicion_lista_arduino_string++;
                }
            } catch (Exception e) {
                error(e.getMessage());
            }
        }

    }
    
    static class ExtendedHouseConnection extends Connection{
        public boolean isValidConnection;
        public boolean isInvalidConnectionRequestSended;
    }
    
    public void serverThreadsStarts(){
        new Thread(this,"Server Sniffer").start();
    }

    public void run() {
        try {
            info(SECTOR,"Comandos Disponibles para digitar"
                    + ":'exit','restart'");
            System.out.println(WELCOME);
            while (true) {
                String line = leer.readLine().trim();
                verificarComando(line);
            }
        } catch (IOException ex) {
            warn(SECTOR,"Error al Leer comando ingresado");
        }
    }

    private void verificarComando(String line) {
        if (line.equals("exit")) {
            cerrarServicios();
        } else if (line.equals("restart")) {
            /*FALTA COMO HACER EL RESTART*/
        } else {
            info(SECTOR,"Comando '" + line + "' no reconocido");
        }
    }

    private void cerrarServicios() {
        info("Closing services....");
        Connection[] connections = server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            connections[i].close();
        }
        server.getUpdateThread().stop();
        server.stop();
        server.close();
        serial_arduino.close();
        conexion_basedatos.close();
        System.exit(0);
    }


}
