/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.server;

import cl.eh.arduino.ArduinoOutputErrorDetection;
import cl.eh.exceptions.Nivel8Exception;
import cl.eh.db.model.Actuador;
import cl.eh.db.model.Historial;
import cl.eh.db.model.Sensor;
import cl.eh.common.ArduinoSignal;
import cl.eh.arduino.ReleeShield;
import cl.eh.util.NetworksInterfaces;
import cl.eh.util.Ip;
import cl.eh.util.Interface;
import java.util.Vector;
import cl.eh.server.ServerExtendedHouse.ExtendedHouseConnection;
import cl.eh.common.Network.ValidacionConnection;
import cl.eh.arduino.SerialArduino;
import cl.eh.common.ArduinoHelp;
import cl.eh.common.ClientArduinoSignal;
import cl.eh.common.Network;
import cl.eh.common.Network.*;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.exceptions.ArduinoIOException;
import cl.eh.properties.PropiedadesServer;
import cl.eh.util.ThreadFrecuente;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

import static com.esotericsoftware.minlog.Log.*;
import java.util.logging.Level;

/**
 *
 * @author Usuario
 */
public final class ServerExtendedHouse implements Runnable,SerialPortEventListener {
    private static final String VERSION = "0.3.0";
    private static final String SECTOR = ServerExtendedHouse.class.getSimpleName();
    private static final String WELCOME = ">>Bienvenid@ a EXTENDED HOUSE v"+VERSION+"<<";
    private static Vector<String> vector_adru_str;
    private static  SerialArduino serial_arduino;
    private static int posicion_lista_arduino_string = 0;
    private static Server server;
    private static ReleeShield relee_sh;
    private PropiedadesServer propiedades_server;
    private BufferedReader leer;
    private ConexionExtendedHouse conexion_basedatos;
    private NetworksInterfaces net_interfaces;
    private byte BufferChunk[];
    private int bufferChunkDeComienzo;
    private ThreadFrecuente threadTemp,threadLuz;
    private static int numero_de_usuarios_conectados;
    
    public ServerExtendedHouse(){
        numero_de_usuarios_conectados = 0;
        threadLuz = new ThreadFrecuente(5); // min
        threadTemp = new ThreadFrecuente(5); // min
        BufferChunk = new byte[64];
        bufferChunkDeComienzo = 0;
        clearBuffer();
        Log.set(Log.LEVEL_DEBUG);
        vector_adru_str = new Vector<String>();
        leer = new BufferedReader(new InputStreamReader(System.in));
        propiedades_server = new PropiedadesServer();
        propiedades_server.getAllServerPropiedadesOfFile();
        serial_arduino = new SerialArduino(propiedades_server.getArduino_port());
        relee_sh = new ReleeShield(serial_arduino);
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
                        numero_de_usuarios_conectados++;
                    }else{ // se valida en la base de datos .. ya que es externo
                        debug(SECTOR,"Coneccion ["+val_con.user+"] rechasada");
                        server.sendToTCP(ex_con.getID(), new InvalidConnection());
                        ex_con.isInvalidConnectionRequestSended = true;
                        ex_con.close();
                    }
                }else if(ex_con.isValidConnection){ // si la coneccion el valida me comunico con el cliente
                    /*--------------------------OBJECTOS A MANIPULAR-----------------------*/
                    if (object instanceof ArduinoInput) {
                        ArduinoInput ai = (ArduinoInput) object;
                        switch (ai.señal) {
                            case ArduinoSignal.RELEE_SIGNAL: {
                                assert (ai.dispositivo >= 0 && ai.dispositivo <= 7);
                                assert (ai.valor == 0 || ai.valor == 1);
                                if(ai.valor == 1){
                                    relee_sh.powerOnRelee(ai.dispositivo);
                                }else{
                                    relee_sh.powerOffRelee(ai.dispositivo);
                                } 
                                ArduinoOutput ao = new ArduinoOutput();
                                ao.dispositivo = ClientArduinoSignal.RELEE_SIGNAL;
                                ao.numero = Integer.toString(ai.dispositivo);
                                ao.valor = ai.valor;
                                server.sendToAllTCP(ao);
                                //ahora enviar a la bd
                                if (conexion_basedatos.isConnected()) {
                                    Actuador ef = new Actuador();
                                    ef.setNombre(ClientArduinoSignal.RELEE_SIGNAL);//RL
                                    ef.setNumero(ai.dispositivo); // ej: 0
                                    int id = conexion_basedatos.getIdOfActuador(ef);
                                    ef.setId(id);
                                    if (id != -1) { // se registra en bd
                                        Historial his = new Historial(0,ef,null,
                                                Calendar.getInstance(),
                                                ex_con.getRemoteAddressTCP().getAddress().toString(),
                                                (ai.valor == 0) ? "Apagado":"Encendido");
                                        conexion_basedatos.addHistorial(his);
                                    } else {
                                        try {
                                            throw new Nivel8Exception("weon te dio -1 el id");
                                        } catch (Nivel8Exception ex) {
                                            java.util.logging.Logger.getLogger(ServerExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }


                            }
                        }
                        //serial_arduino.enviarSeñal(ai.señal);
                    } else if (object instanceof DatabaseQuery) {
                        DatabaseQuery dq = (DatabaseQuery) object;
                    } else if (object instanceof AdvanceCommand) {
                        verificarComando(((AdvanceCommand) object).commando);
                    } else if (object instanceof ServerStatusRequest) { // status de to2 el server
                        for (int i = 0; i < ReleeShield.MAX_RELES; i++) {
                            ArduinoOutput ao = new ArduinoOutput();
                            ao.dispositivo = ClientArduinoSignal.RELEE_SIGNAL;
                            ao.numero = Integer.toString(i);
                            ao.valor = (relee_sh.isReleePowerOn(i)) ? 1 : 0;
                            server.sendToTCP(ex_con.getID(),ao);
                        }
                        UsersOnline users = new UsersOnline();
                        users.users = numero_de_usuarios_conectados;
                        server.sendToTCP(ex_con.getID(), users);
                    }
                    /*--------------------------END OBJECTOS A MANIPULAR-----------------------*/
                }
            }
            @Override
            public void disconnected (Connection c) {
                numero_de_usuarios_conectados--;

            }
        });
        /*------------------------END SERVER LISTENER-------------------------*/
        new Thread(){ // envio de cantidad de usuarios en el sistema a usuarios
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(ServerExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    UsersOnline users = new UsersOnline();
                    users.users = numero_de_usuarios_conectados;
                    server.sendToAllTCP(users);
                }
            }
        }.start();
        
        
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
                int byteDeCorte = 0;
                SerialArduino.input.read(chunk, 0, available);
                /*if (BufferChunk[0] != -1) { //tiene algo
                    for (int i = 0; i < BufferChunk.length; i++) {
                        if (BufferChunk[i] != -1) {
                            bufferChunkDeComienzo = i;
                            break;
                        }
                    }
                }

                for (int i = 0; i < chunk.length; i++) {//byte de corte ";"
                    if (((char) chunk[i]) == ';') {
                        byteDeCorte = i;
                        break;
                    }
                }
                if (byteDeCorte != 0) { // encontro el ;
                    //char buffer[] = new char[byteDeCorte];
                    for (int i = 0; i < byteDeCorte; i++) {
                        BufferChunk[bufferChunkDeComienzo] = chunk[i];
                        bufferChunkDeComienzo++;
                    }
                    byte[] byteFull = new byte[bufferChunkDeComienzo];
                    for(int i = 0; i < bufferChunkDeComienzo; i++){
                        byteFull[i] = BufferChunk[i];
                    }
                    String mensaje = new String(byteFull).trim();
                    System.out.println(mensaje);
                    clearBuffer();
                    bufferChunkDeComienzo=0;
                    if(chunk.length != (byteDeCorte + 1)){//existe una cadena despues del ;
                        for(int i = byteDeCorte; i < chunk.length ; i++){
                            BufferChunk[bufferChunkDeComienzo] = chunk[i];
                            bufferChunkDeComienzo++;
                        }
                    }
                } else {
                    for (int i = 0; i < byteDeCorte; i++) {
                        BufferChunk[bufferChunkDeComienzo] = chunk[i];
                        bufferChunkDeComienzo++;
                    }
                }
                */
                
                String incomplete_arduino_string = new String(chunk);
                //System.out.println("incomplete_arduino_string:"+incomplete_arduino_string);

                vector_adru_str.add(
                        posicion_lista_arduino_string,
                        incomplete_arduino_string);
                if (incomplete_arduino_string.contains(";")) {
                    String contenido = "";
                    for (int i = 0; i < vector_adru_str.size(); i++) {
                        contenido += vector_adru_str.get(i).trim();
                    }//despues de for me qda la lineaAR bien echa...
                    vector_adru_str.clear();
                    posicion_lista_arduino_string = 0;
                    /*correcto-------------*/
                    String[] punto_coma_buffer_error = contenido.split(";");
                    for (int i = 0; i < punto_coma_buffer_error.length; i++) {
                        String[] nomAndVal = punto_coma_buffer_error[i].split(
                                "-");
                        if (nomAndVal.length != 3) {
                            throw new ArduinoIOException(
                                    "Error al leer:" + contenido);
                        } else {
                            ArduinoOutput arduino_output = new ArduinoOutput();
                            arduino_output.dispositivo = nomAndVal[0].trim();
                            arduino_output.numero = nomAndVal[1].trim();
                            try {
                                arduino_output.valor = Float.parseFloat(
                                        nomAndVal[2].trim());
                                if (ArduinoOutputErrorDetection.isAArduinoCorrectOutput(
                                        arduino_output.dispositivo)) {
//                                    System.err.println("Disp:"+arduino_output.dispositivo);
//                                    System.err.println("numero:"+arduino_output.numero);
//                                    System.err.println("valor:"+arduino_output.valor);
                                    server.sendToAllTCP(arduino_output);//<<<<<-------------
                                    if (conexion_basedatos.isConnected()) {
                                        if (!ArduinoHelp.isAnActuador(
                                                arduino_output.dispositivo)) {
                                            Sensor sen = new Sensor();
                                            sen.setNombre(arduino_output.dispositivo);
                                            sen.setNumero(Integer.parseInt(
                                                    arduino_output.numero));
                                            int id = conexion_basedatos.getIdOfSensor(
                                                    sen);
                                            sen.setId(id);
                                            if (id != -1) {
                                                conexion_basedatos.addHistorial(
                                                        new Historial(0, null, sen,
                                                        Calendar.getInstance(),
                                                        "localhost", nomAndVal[2].trim())); // el ultimo es el valor
                                            }
                                        }
                                    }
                                }
                            } catch (NumberFormatException e) {
                                error(SECTOR, e.getMessage()); //SAKER LUEGO
                                //e.printStackTrace();
                            }
                        }
                    }
                    /*correcto---------*/
                } else {
                    posicion_lista_arduino_string++;
                }
            } catch (Exception e) {
                error(SECTOR,e.toString()); //SAKER LUEGO
                //e.printStackTrace();
            }
        }

    }
    
    public void clearBuffer(){
        for(int i = 0; i < BufferChunk.length ; i++){
            BufferChunk[i] = -1;
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
                    + ":'exit','restart','r0'(de prueba)");
            System.out.println(WELCOME);
            while (true) {
                String line = leer.readLine().trim();
                if(line.equals("r0")){
                    relee_sh.powerOnRelee(1);
                }// solo de prueba
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
