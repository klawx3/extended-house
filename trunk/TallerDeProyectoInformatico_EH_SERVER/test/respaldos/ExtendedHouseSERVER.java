/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package respaldos;

import cl.eh.arduino.ArduinoCommands;
import cl.eh.arduino.ArduinoOutputErrorDetection;
import cl.eh.arduino.model.ArduinoEvent;
import cl.eh.exceptions.Nivel8Exception;
import cl.eh.db.model.Actuador;
import cl.eh.db.model.Historial;
import cl.eh.db.model.Sensor;
import cl.eh.common.ArduinoSignal;
import cl.eh.arduino.ReleeShield;
import cl.eh.util.NetworksInterfaces;
import cl.eh.util.Ip;
import cl.eh.util.Interface;
import cl.eh.common.Network.ValidacionConnection;
import cl.eh.arduino.SerialArduino;
import cl.eh.arduino.model.ArduinoEventListener;
import cl.eh.common.ArduinoHelp;
import cl.eh.common.ClientArduinoSignal;
import cl.eh.common.Network;
import cl.eh.common.Network.*;
import cl.eh.db.AutomaticDatabaseMaintenance;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.db.RespaldoBd;
import cl.eh.exceptions.ArduinoIOException;
import cl.eh.properties.ConfiguracionServidor;
import cl.eh.properties.PropiedadesServer;
import cl.eh.serial.SerialOutput;
import cl.eh.util.ArchivoObjectosJava;
import cl.eh.util.Fecha;
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
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Executors;

import static com.esotericsoftware.minlog.Log.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public final class ExtendedHouseSERVER implements Runnable {
    private static final String VERSION = "0.3.3a";
    private static final String SECTOR = ExtendedHouseSERVER.class.getSimpleName();
    private static final String NOM_ARCH_CONF_SERV = "conf.eh";
    private static final String WELCOME = "------>>>>Bienvenid@ a EXTENDED HOUSE v"+VERSION+"<<<<<------";
    private static final int segundosIntervaloDeEnvioInfoUsuarios = 2;
    private static int numero_de_usuarios_conectados;
    private static  SerialArduino serial_arduino;
    private static Server server;
    private static ReleeShield relee_sh;
    private static ArduinoCommands arduinoComandosEh;
    private static ConfiguracionServidor conf_server_interno;
    private ThreadFrecuente thread_sen_tmp,thread_sen_luz;
    private PropiedadesServer propiedades_server;
    private BufferedReader leer;
    private ConexionExtendedHouse conexion_basedatos;
    private NetworksInterfaces net_interfaces;
    private ExecutorService exService;
    private AutomaticDatabaseMaintenance auto_db;
    private SerialOutput serial_output;

    
    public ExtendedHouseSERVER(){
        numero_de_usuarios_conectados = 0;
        propiedades_server   = new PropiedadesServer();
        propiedades_server.getAllServerPropiedadesOfFile();
        conf_server_interno = (ConfiguracionServidor) ArchivoObjectosJava.abrirObjecto(NOM_ARCH_CONF_SERV);
        if (conf_server_interno == null) {
            conf_server_interno = new ConfiguracionServidor();
            try {
                long millis_backup = Long.parseLong(propiedades_server.getDatabase_millisecontsToBackup());
                conf_server_interno.tiempoRestanteNuevoRespaldo = millis_backup;
            } catch (NumberFormatException e) {
                conf_server_interno.tiempoRestanteNuevoRespaldo = AutomaticDatabaseMaintenance.TIEMPORESTANTEDEFAULT_MILLISECONDS;
            }
            ArchivoObjectosJava.guardarObjecto(conf_server_interno, NOM_ARCH_CONF_SERV);
            debug(SECTOR, "Detectada 1º Ejecuccion de EH... Archivo " + NOM_ARCH_CONF_SERV + " Creado");
        } else {
            debug(SECTOR, "Archivo " + NOM_ARCH_CONF_SERV + " Abierto Exitosamente!");
        }
        info(SECTOR, conf_server_interno.tiempoRestanteNuevoRespaldo+" MILLISEGUNDOS para nuevo respaldo de BD.");
        
        serial_output        = new SerialOutput(ArduinoHelp.ENDOFSTRING);
        conexion_basedatos   = new ConexionExtendedHouse(
                propiedades_server.getDatabase_ip(),
                propiedades_server.getDatabase_name(),
                propiedades_server.getDatabase_user(),
                propiedades_server.getDatabase_pass());
        leer                 = new BufferedReader(new InputStreamReader(System.in));
        serial_arduino       = new SerialArduino(propiedades_server.getArduino_port());

        relee_sh             = new ReleeShield(serial_arduino);
        thread_sen_tmp  = thread_sen_luz
                             = new ThreadFrecuente(propiedades_server.getDatabase_millisencods_insert());
        server               = new Server() {
            @Override
            protected Connection newConnection() {
                return new ExtendedHouseConnection();
            }
        };
        Network.register(server);
        /*------------------------START SERVER LISTENER----------------------*/
        server.addListener(new Listener() {

            @Override
            public void received(Connection c, Object object) {
                ExtendedHouseConnection ex_con = (ExtendedHouseConnection) c;
                if (object instanceof ValidacionConnection) { // seria la primera señal
                    ValidacionConnection val_con = (ValidacionConnection) object;
                    if (conexion_basedatos.isConnected()) {
                        if (conexion_basedatos.isaValidUser(val_con.user)) {
                            ex_con.isValidConnection = true;
                            info(SECTOR, "Coneccion [" + val_con.user
                                    + "] Aceptada (" + ex_con.ip + ")");
                            ex_con.ip = val_con.client_ip;
                            ex_con.user = val_con.user;
                            ex_con.isAnAdministrador = conexion_basedatos.isaAdministrador(val_con.user);
                            numero_de_usuarios_conectados++;
                            actualizarUsuarios();
                        } else {
                            ex_con.isValidConnection = false;
                            info(SECTOR, "Coneccion [" + val_con.user
                                    + "] rechasada (" + ex_con.ip + ")");
                            server.sendToTCP(ex_con.getID(), new InvalidConnection());
                            ex_con.isInvalidConnectionRequestSended = true;
                            ex_con.close();
                        }
                    } else {
                        ex_con.isValidConnection = false;

                        server.sendToTCP(ex_con.getID(), new InvalidConnection());
                        ex_con.isInvalidConnectionRequestSended = true;
                        ex_con.close();
                        warn(SECTOR, "Coneccion nueva RECHAZADA, ya que, NO EXISTE CONECCION A BD");
                    }
                } else if (ex_con.isValidConnection) { // si la coneccion el valida me comunico con el cliente
                    /*--------------------------OBJECTOS A MANIPULAR-----------------------*/
                    if (object instanceof ArduinoInput) {
                        ArduinoInput ai = (ArduinoInput) object;
                        switch (ai.señal) {
                            case ArduinoSignal.RELEE_SIGNAL: {
                                assert (ai.dispositivo >= 0 && ai.dispositivo <= 7);
                                assert (ai.valor == 0 || ai.valor == 1);
                                if (ai.valor == 1) {
                                    relee_sh.powerOnRelee(ai.dispositivo);
                                } else {
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
                                    if (id != ConexionExtendedHouse.NULL_ID) { // se registra en bd
                                        Historial his = new Historial(0, ef, null,
                                                Calendar.getInstance(),
                                                ex_con.user,
                                                ex_con.ip,
                                                (ai.valor == 0) ? "Apagado" : "Encendido");
                                        conexion_basedatos.addHistorial(his);
                                    } else {
                                        try {
                                            throw new Nivel8Exception("weon te dio -1 el id");
                                        } catch (Nivel8Exception ex) {
                                            java.util.logging.Logger.getLogger(ExtendedHouseSERVER.class.getName()).log(Level.SEVERE, null, ex);
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
                            server.sendToTCP(ex_con.getID(), ao);
                        }
                        UsersOnline users = new UsersOnline();
                        users.users = numero_de_usuarios_conectados;
                        server.sendToTCP(ex_con.getID(), users);
                    } else if (object instanceof RespaldoRequest) {
                        if (ex_con.isAnAdministrador) {
                            Network.ListaRespaldos lr = new ListaRespaldos();
                            lr.respaldos = new ArrayList();
                            for (RespaldoBd res : auto_db.getRespaldos()) {
                                Network.Respaldo r = new Network.Respaldo();
                                r.fecha = res.getFecha().getTimeInMillis();
                                r.isRespaldoByUsuario = res.isIsRespaldoByUsuario();
                                r.nom = res.getNom();
                                lr.respaldos.add(r);
                            }
                            server.sendToTCP(ex_con.getID(), lr);
                        } else { // enviar wea que es un usuario invalido
                            sendMessage(ex_con.getID()
                                    , "No tiene los privilegios suficientes"
                                    ,JOptionPane.ERROR_MESSAGE);
                        }

                        //server.sendToTCP(ex_con.getID(), );
                    } else if (object instanceof MakeDatabaseRestore) {
                        if (ex_con.isAnAdministrador) {
                            MakeDatabaseRestore mdb = (MakeDatabaseRestore) object;
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(mdb.fecha);
                            if (auto_db.restoreDatabase(cal, mdb.restaurarYEliminarDatosHastaLaFecha)) {
                                sendMessage(ex_con.getID(), "Restaurado con Exito "
                                        +(mdb.restaurarYEliminarDatosHastaLaFecha ? 
                                        ",Datos posteriores a la fecha eliminados"
                                        :null), JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                sendMessage(ex_con.getID(), "Se ha producido un error al restaurar", JOptionPane.ERROR_MESSAGE);
                            }
                            
                        } else {
                            sendMessage(ex_con.getID()
                                    , "No tiene los privilegios suficientes"
                                    ,JOptionPane.ERROR_MESSAGE);
                        }

                    } else if (object instanceof MakeDatabaseBackup) {
                        if (ex_con.isAnAdministrador) {
                            auto_db.backupDatabaseNow();
                        } else {
                            sendMessage(ex_con.getID()
                                    , "No tiene los privilegios suficientes"
                                    ,JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (object instanceof PacketePrueba) {
                        PacketePrueba pp = (PacketePrueba) object;
                        switch (pp.numero_prueba) {
                            case 1: {
                                sendMessage(ex_con.getID(), "Mensaje de prueba",JOptionPane.ERROR_MESSAGE);
                                break;
                            }

                        }
                    }
                    /*--------------------------END OBJECTOS A MANIPULAR-----------------------*/
                }
            }
            @Override
            public void disconnected (Connection c) {
                numero_de_usuarios_conectados--;
                actualizarUsuarios();
                
            }

            private void sendMessage(int iD, String mensaje, int tipo_error) {
                ServerErrorInfoToUser srrr = new ServerErrorInfoToUser();
                srrr.mensaje = mensaje;
                srrr.tipo_error = tipo_error;
                server.sendToTCP(iD, srrr);
            }
            
            private void actualizarUsuarios() {
                UsersOnline users = new UsersOnline();
                users.users = numero_de_usuarios_conectados;
                server.sendToAllTCP(users);
                if (serial_arduino.isConnecionArduinoEstablecida()) {
                    serial_arduino.enviarSeñal((int) 'u');
                    serial_arduino.enviarSeñal((numero_de_usuarios_conectados == 0)
                            ? 48 : numero_de_usuarios_conectados);
                }
            }
        });
        /*------------------------END SERVER LISTENER-------------------------*/
        
        
        
        try {
            server.bind(Network.getNetworkPort());
        } catch (IOException ex) {
            error(SECTOR,ex.getMessage());
            error(SECTOR,"Verifique que el puerto '"
                    + Network.getNetworkPort() + "'que no este en uso...");
            System.exit(1);
        }
        server.start();

        /*-------------------------------END SERVIDOR-------------------------*/
        if (serial_arduino.isConnecionArduinoEstablecida()) {
            serial_arduino.addArduinoEventListener(new ArduinoEventListener() {

                public void ArduinoEventListener(ArduinoEvent evt) {
                    ArduinoOutput arduino_output = new ArduinoOutput();
                    arduino_output.dispositivo = evt.getNombreDisositivo();
                    arduino_output.numero = Integer.toString(evt.getNumeroDispositovo());
                    arduino_output.valor = evt.getValorDispositivo();
                    server.sendToAllTCP(arduino_output);
                    arduino_output = null;
                    if (conexion_basedatos.isConnected()) {
                        if (!ArduinoHelp.isAnActuador(arduino_output.dispositivo)) {
                            Sensor sen = new Sensor();
                            sen.setNombre(evt.getNombreDisositivo());
                            sen.setNumero(evt.getNumeroDispositovo());
                            String val = Float.toString(evt.getValorDispositivo());
                            if (ArduinoHelp.isARecurrentSensor(arduino_output.dispositivo)) {
                                if (thread_sen_tmp.isThreadFinishWork()
                                        && ArduinoHelp.TEMPERATURA_SIGNAL.equals(sen.getNombre())) {
                                    addSensorToDatabase(sen, val);
                                    thread_sen_tmp.startThreadAgain();
                                }
                                if (thread_sen_luz.isThreadFinishWork()
                                        && ArduinoHelp.LUZ_SIGNAL.equals(sen.getNombre())) {
                                    addSensorToDatabase(sen, val);
                                    thread_sen_luz.startThreadAgain();
                                }
                            } else {
                                addSensorToDatabase(sen, val);
                            }
                        }
                    }
                }
            });
        }
        arduinoComandosEh = new ArduinoCommands(serial_arduino,relee_sh);        
//        net_interfaces = new NetworksInterfaces();
//        List<Interface> interfaces = net_interfaces.getInterfaces();
//        for(Interface inter: interfaces){
//            if(inter.isActive()){
//                info(SECTOR,"[Dispositivo]:"+ inter.getNombreDispositivo()
//                        +"\n\t\t\t[Interface]:"+inter.getNombreInterface() );
//                for(Ip ip : inter.getIps()){
//                        System.out.println("\t\t\t"+ip.getIpString()+"\\"+ip.getMask());     
//                }
//            }
//        }
        info(SECTOR,"Ingresos a la BD en ([ "
                +((double)propiedades_server.getDatabase_millisencods_insert())/(double)(60*1000)
                +" ]min)");

        System.out.println("=========================================");
        info(SECTOR,Fecha.getFecha(false, false));
        
        
    }    
    
    static class ExtendedHouseConnection extends Connection{
        public boolean isValidConnection;
        public boolean isInvalidConnectionRequestSended;
        public boolean isAnAdministrador;
        public String user;
        public String ip;
    }
    
    public void addSensorToDatabase(Sensor sen, String valor) {
        int id = conexion_basedatos.getIdOfSensor(
                sen);
        sen.setId(id);
        if (id != ConexionExtendedHouse.NULL_ID) {
            conexion_basedatos.addHistorial(
                    new Historial(0, null, sen,
                    Calendar.getInstance(),
                    ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER,
                    "localhost",
                    valor));
        }
    }

    public void serverThreadsStarts(){
        Thread s1 = new Thread(this, "Server Sniffer");
        Thread s3 = new Thread(thread_sen_tmp);
        Thread s4 = new Thread(thread_sen_luz);
        exService = Executors.newCachedThreadPool();
        exService.execute(s1);
        exService.execute(s3);
        exService.execute(s4);
        if(conexion_basedatos.isConnected()){
            auto_db = new AutomaticDatabaseMaintenance(conexion_basedatos
                    ,conf_server_interno);
            auto_db.start();
        }
        exService.shutdown();
        thread_sen_tmp.startThreadAgain();
        thread_sen_luz.startThreadAgain();
    }

    public void run() {
        try {
            info(SECTOR,"Comandos Disponibles para digitar"
                    + ":'exit','restart','send <arg>',ard <arg[n]>");
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
        if (line.startsWith("exit")) {
            cerrarServicios();
        } else if (line.startsWith("restart")) {
            /*FALTA COMO HACER EL RESTART*/
            
        }else if(line.startsWith("send")){
            try{
                String mensaje = line.trim().split(" ",2)[1];
            ServerMesage sm = new ServerMesage();
            sm.mensaje = mensaje;
            server.sendToAllTCP(sm);
            info(SECTOR,"Mensaje ["+mensaje+"] enviado");
            }catch(java.lang.ArrayIndexOutOfBoundsException e){
                error(SECTOR,"El comando [send] requiere argumento");
            }
            
        } else if(line.startsWith("ard")){
            try{
                String mensaje = line.trim().split(" ",2)[1];
                try {
                    arduinoComandosEh.sendCommand(mensaje, true);
                } catch (ArduinoIOException ex) {
                    error(SECTOR,ex.getMessage());
                    ex.printStackTrace();
                }
            }catch(java.lang.ArrayIndexOutOfBoundsException e){
                error(SECTOR,"El comando [ard] requiere argumento");
                
            }
        }else {
            info(SECTOR,"Comando '" + line + "' no reconocido");
        }
    }
    
    private void guardarConfServer(){
        if(conexion_basedatos.isConnected()){
            auto_db.save();
        }
        ArchivoObjectosJava.guardarObjecto(conf_server_interno,NOM_ARCH_CONF_SERV);
    }

    private void cerrarServicios() {
        info("Saving settings...");
        guardarConfServer();
        info("Closing services...");
        Connection[] connections = server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            connections[i].close();
        }
        server.getUpdateThread().stop();
        server.stop();
        server.close();
        serial_arduino.close();
        conexion_basedatos.close();
        auto_db.stop();
        System.exit(0);
    }



}
