/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.server;

import cl.eh.arduino.*;
import cl.eh.arduino.model.*;
import cl.eh.exceptions.*;
import cl.eh.db.model.*;
import cl.eh.arduino.ReleeShield;
import cl.eh.server.ExtendedHouseSERVER.ExtendedHouseConnection;
import cl.eh.common.Network.ValidacionConnection;
import cl.eh.arduino.SerialArduino;
import cl.eh.arduino.model.ArduinoEventListener;
import cl.eh.common.*;
import cl.eh.common.Network.*;
import cl.eh.db.AutomaticDatabaseMaintenance;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.db.RespaldoBd;
import cl.eh.eventos.HiloDeEventoLES;
import cl.eh.eventos.LESAdministador;
import cl.eh.eventos.compilator_utils.LESCompUtils;
import cl.eh.eventos.model.EventoEvent;
import cl.eh.eventos.model.EventoListener;
import cl.eh.exceptions.ArduinoIOException;
import cl.eh.properties.ConfiguracionServidor;
import cl.eh.properties.PropiedadesServer;
import cl.eh.scripts.EHJavaScriptAdministrator;
import cl.eh.scripts.God;
import cl.eh.util.ArchivoObjectosJava;
import cl.eh.util.ThreadFrecuente;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
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
public final class ExtendedHouseSERVER {

    private static final String VERSION = "0.5.3";
    private static final String SECTOR = ExtendedHouseSERVER.class.getSimpleName();
    private static final String NOM_ARCH_CONF_SERV = "conf.eh";
    private static final String WELCOME = "##########>>>>}Bienvenid@ a EXTENDED HOUSE v" + VERSION + "{<<<<<##########";
    private static final String SEPARADOR = "=============================================";
    private static int numero_de_usuarios_conectados;
    private static SerialArduino serial_arduino;
    private static Server server;
    private static ReleeShield relee_sh;
    private static ArduinoCommands arduinoComandosEh;
    private static ConfiguracionServidor conf_server_interno;
    private static ServerCommandAdministrator serverCommandAdministrator;
    private EHJavaScriptAdministrator scripts;
    private ThreadFrecuente thread_sen_tmp, thread_sen_luz;
    private PropiedadesServer propiedades_server;
    private static ConexionExtendedHouse conexion_basedatos;
    private ExecutorService exService;
    private static AutomaticDatabaseMaintenance auto_db;
    private LESAdministador lesAdm;
    private God god;

    public ExtendedHouseSERVER() {
        serverCommandAdministrator = new ServerCommandAdministrator();
        numero_de_usuarios_conectados = 0;
        propiedades_server = new PropiedadesServer();
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
        info(SECTOR, conf_server_interno.tiempoRestanteNuevoRespaldo + " MILLISEGUNDOS para nuevo respaldo de BD.");
        conexion_basedatos = new ConexionExtendedHouse(
                propiedades_server.getDatabase_ip(),
                propiedades_server.getDatabase_name(),
                propiedades_server.getDatabase_user(),
                propiedades_server.getDatabase_pass());
        
        serial_arduino = new SerialArduino(propiedades_server.getArduino_port());
        relee_sh = new ReleeShield(serial_arduino);
        thread_sen_tmp = thread_sen_luz = new ThreadFrecuente(propiedades_server.getDatabase_millisencods_insert());
        server = new Server() {

            @Override
            protected Connection newConnection() {
                return new ExtendedHouseConnection();
            }
        };
        Network.register(server);
        /*------------------------START SERVER LISTENER----------------------*/
        Listener serverListener = new Listener() {

            @Override
            public void received(Connection c, Object object) {
                ExtendedHouseConnection ex_con = (ExtendedHouseConnection) c;
                if (object instanceof ValidacionConnection) { // seria la primera señal
                    ValidacionConnection val_con = (ValidacionConnection) object;
                    if (conexion_basedatos.isConnected()) {
                        if (conexion_basedatos.isaValidUser(val_con.user)) {
                            ex_con.isValidConnection = true;
                            info(SECTOR, "Coneccion [" + val_con.user
                                    + "] Aceptada ip[" + ex_con.ip + "]");
                            ex_con.ip = val_con.client_ip;
                            ex_con.user = val_con.user;
                            ex_con.isAnAdministrador = conexion_basedatos.isaAdministrador(val_con.user);
                            numero_de_usuarios_conectados++;
                            actualizarUsuarios();
                        } else {
                            ex_con.isValidConnection = false;
                            info(SECTOR, "Coneccion [" + val_con.user
                                    + "] rechasada ip[" + ex_con.ip + "]");
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
                        AdvanceCommand ac = (AdvanceCommand)object;
                        serverCommandAdministrator.checkCommand(ac.commando);
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
                        
                        ActuadorInfoList ail = new ActuadorInfoList();
                        ail.infoActuador = new ArrayList();
                        for(Actuador act : conexion_basedatos.getActuadores()){
                            InfoActuador infoAct = new InfoActuador();
                            infoAct.id = act.getId();
                            infoAct.nombre = act.getNombre();
                            infoAct.numero = act.getNumero();
                            ail.infoActuador.add(infoAct);
                        }
                        server.sendToTCP(ex_con.getID(), ail);
                        
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
                            sendMessage(ex_con.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                        }

                        //server.sendToTCP(ex_con.getID(), );
                    } else if (object instanceof MakeDatabaseRestore) {
                        if (ex_con.isAnAdministrador) {
                            MakeDatabaseRestore mdb = (MakeDatabaseRestore) object;
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(mdb.fecha);
                            if (auto_db.restoreDatabase(cal, mdb.restaurarYEliminarDatosHastaLaFecha)) {
                                sendMessage(ex_con.getID(), "Restaurado con Exito "
                                        + (mdb.restaurarYEliminarDatosHastaLaFecha
                                        ? ",Datos posteriores a la fecha eliminados"
                                        : null), JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                sendMessage(ex_con.getID(), "Se ha producido un error al restaurar", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            sendMessage(ex_con.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                        }

                    } else if (object instanceof MakeDatabaseBackup) {
                        if (ex_con.isAnAdministrador) {
                            auto_db.backupDatabaseNow();
                        } else {
                            sendMessage(ex_con.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (object instanceof PacketePrueba) {
                        PacketePrueba pp = (PacketePrueba) object;
                        switch (pp.numero_prueba) {
                            case 1: {
                                sendMessage(ex_con.getID(), "Mensaje de prueba", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                        }
                    } else if(object instanceof EventoRequest){
                        Network.ListaEventos listE = new ListaEventos();
                        listE.eventos = new ArrayList();
                        for(HiloDeEventoLES les : lesAdm.getEventInExecutionList()){
                            Network.Evento evt = new Network.Evento();
                            evt.LesString = les.getEventoString();
                            evt.LesHTMLString = LESCompUtils.getHtmlLESString(les.getEventoString());
                            evt.user = les.getUser();
                            listE.eventos.add(evt);
                        }
                        server.sendToTCP(ex_con.getID(), listE);
                        
                    } else if(object instanceof CreacionEvento){
                        CreacionEvento cEvt = (CreacionEvento) object;
                        try {
                            lesAdm.addEventoSimpleString(cEvt.LES, ex_con.user);
                            sendMessage(ex_con.getID()
                                    , "Evento creado exitosamente"
                                    , JOptionPane.INFORMATION_MESSAGE);
                        } catch (LESException ex) {
                            error(SECTOR,ex.toString());
                            ex.printStackTrace();
                            sendMessage(ex_con.getID(),ex.toString(),JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (object instanceof EliminarEventoRequest) {
                        EliminarEventoRequest el = (EliminarEventoRequest) object;
                        if (lesAdm.removeEventoSiemple(el.lesString)) {
                            sendMessage(ex_con.getID()
                                    , "Evento removido exitosamente"
                                    , JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            sendMessage(ex_con.getID()
                                    , "Error al remover evento"
                                    , JOptionPane.ERROR);
                        }
                    }
                    /*--------------------------END OBJECTOS A MANIPULAR-----------------------*/
                }
            }

            @Override
            public void disconnected(Connection c) {
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
        };
        server.addListener(serverListener);
        /*------------------------END SERVER LISTENER-------------------------*/

        try {
            server.bind(Network.getNetworkPort());
        } catch (IOException ex) {
            error(SECTOR, ex.getMessage());
            error(SECTOR, "Verifique que el puerto ["
                    + Network.getNetworkPort() + "]que no este en uso...");
            System.exit(1);
        }
        server.start();

        /*-------------------------------END SERVIDOR-------------------------*/
        if (serial_arduino.isConnecionArduinoEstablecida()) {
            serial_arduino.addArduinoEventListener(new ArduinoEventListener() {

                public void ArduinoEventListener(ArduinoEvent evt) {
                    if (god != null) { // se actualiza la informacion de god
                        god.updateSensorValor(evt);
                    }
                    ArduinoOutput arduino_output = new ArduinoOutput();
                    arduino_output.dispositivo = evt.getNombreDisositivo();
                    arduino_output.numero = Integer.toString(evt.getNumeroDispositovo());
                    arduino_output.valor = evt.getValorDispositivo();
                    server.sendToAllTCP(arduino_output);
                    arduino_output = null;
                    if (conexion_basedatos.isConnected()) {
                        if (!ArduinoHelp.isAnActuador(evt.getNombreDisositivo())) {
                            Sensor sen = new Sensor();
                            sen.setNombre(evt.getNombreDisositivo());
                            sen.setNumero(evt.getNumeroDispositovo());
                            String val = Float.toString(evt.getValorDispositivo());
                            if (ArduinoHelp.isARecurrentSensor(evt.getNombreDisositivo())) {
                                if (thread_sen_tmp.isThreadFinishWork()
                                        && ArduinoHelp.TEMPERATURA_SIGNAL.equals(sen.getNombre())) {
                                    addSensorToDatabase(sen, val);
                                    thread_sen_tmp.startThreadAgain(); // ojoooo .. esto esta mal
                                }
                                if (thread_sen_luz.isThreadFinishWork()
                                        && ArduinoHelp.LUZ_SIGNAL.equals(sen.getNombre())) {
                                    addSensorToDatabase(sen, val);
                                    thread_sen_luz.startThreadAgain();// ojoooo .. esto esta mal
                                }
                            } else {
                                addSensorToDatabase(sen, val);
                            }
                        }
                    }
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
            });
        }
        arduinoComandosEh = new ArduinoCommands(serial_arduino, relee_sh);
        if(conexion_basedatos.isConnected()){
            try {
                lesAdm = new LESAdministador(conexion_basedatos);
                
                lesAdm.addEventoListener(new EventoListener() { // solo funcional para rl.. creo
                    private boolean eventoHaEfecuandoAlgunaOperacion;
                    private int valor;
                    public void eventoPerformed(EventoEvent e) {
                        eventoHaEfecuandoAlgunaOperacion = false;
                        if (e.getActuador().equalsIgnoreCase("rl")) {
                            switch (e.getAccion()) {
                                case EventoEvent.ACCI_APAGAR:
                                    relee_sh.powerOffRelee(e.getNumero_actuador());
                                    eventoHaEfecuandoAlgunaOperacion = true;
                                    valor = 0;
                                    break;
                                case EventoEvent.ACCI_CAMBIAR:
                                    if (relee_sh.isReleePowerOn(e.getNumero_actuador())) {
                                        relee_sh.powerOffRelee(e.getNumero_actuador());
                                        valor = 0;
                                    } else {
                                        relee_sh.powerOnRelee(e.getNumero_actuador());
                                        valor = 1;
                                    }
                                    eventoHaEfecuandoAlgunaOperacion = true;
                                    //info(SECTOR,"Paso por cambiar");
                                    break;
                                case EventoEvent.ACCI_ENCENDER:
                                    eventoHaEfecuandoAlgunaOperacion = true;
                                    relee_sh.powerOnRelee(e.getNumero_actuador());
                                    valor = 1;
                                    break;
                                default:
                                    error(SECTOR, "Operacion Inesperada; accion:[" + e.getAccion() + "]");
                            }
                            if(eventoHaEfecuandoAlgunaOperacion){
                                ArduinoOutput ao = new ArduinoOutput();
                                ao.dispositivo = e.getActuador();
                                ao.numero = Integer.toString(e.getNumero_actuador());
                                ao.valor = valor;
                                server.sendToAllTCP(ao);
                                Actuador ef = new Actuador();
                                ef.setNombre(e.getActuador());//RL
                                ef.setNumero(e.getNumero_actuador()); // ej: 0
                                ef.setId(conexion_basedatos.getIdOfActuador(ef));
                                if (ef.getId() != ConexionExtendedHouse.NULL_ID) { // se registra en bd
                                    Historial his = new Historial(0, ef, null,
                                            Calendar.getInstance(),
                                            ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER,
                                            "Localhost",
                                            (valor == 0) ? "Apagado" : "Encendido");
                                    conexion_basedatos.addHistorial(his);
                                } else {
                                    try {
                                        throw new Nivel8Exception("weon te dio -1 el id");
                                    } catch (Nivel8Exception ex) {
                                        java.util.logging.Logger.getLogger(ExtendedHouseSERVER.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        } else {
                            error(SECTOR,"Nombre actuador distinto de [rl]");
                        }
                    }
                });
                lesAdm.start();
                info(SECTOR,"Administrador de Eventos Simple [INICIADO]");
            } catch (LESException ex) {
                java.util.logging.Logger
                        .getLogger(ExtendedHouseSERVER.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        info(SECTOR, "Ingresos a la BD en ([ "
                + ((double) propiedades_server.getDatabase_millisencods_insert()) / (double) (60 * 1000)
                + " ]min)");

        System.out.println(SEPARADOR);
        if (conexion_basedatos.isConnected()) {
            god = new God(conexion_basedatos, serial_arduino, server, relee_sh);
            info(SECTOR, "Administrador God [INICIADO]");
        } else {
            info(SECTOR, "Administrador God [ERROR->NO HAY CONECCION A BD]");
        }
        System.out.println(SEPARADOR);
        try {
            info(SECTOR, "Iniciando modulo de Scripting de EH.");
            if (god != null) {
                scripts = new EHJavaScriptAdministrator(
                        EHJavaScriptAdministrator.SCRIPT_DEFAULT_DIRECTORY,
                        god);
                scripts.startTasks();
                info(SECTOR, "Modulo de Scripting [INICIANDO]");
            } else {
                info(SECTOR, "Modulo de Scripting [ERROR->NO ESTA EL ADMINISTRADOR GOD]");
            }

        } catch (Exception ex) {
            error(SECTOR, "Modulo de Scripting [ERROR->" + ex.getMessage() + "]");
        }
        System.out.println(SEPARADOR);
    }

    public void serverThreadsStarts() {
        exService = Executors.newCachedThreadPool();
        exService.execute(serverCommandAdministrator);
        exService.execute(thread_sen_tmp);
        exService.execute(thread_sen_luz);
        if (conexion_basedatos.isConnected()) {
            auto_db = new AutomaticDatabaseMaintenance(conexion_basedatos, conf_server_interno);
            auto_db.start();
        }
        thread_sen_tmp.startThreadAgain();
        thread_sen_luz.startThreadAgain();
        exService.shutdown();
    }

    public void serverSafetyClose() {
        info(SECTOR,"Saving settings...");
        saveServerConfigurations();
        info(SECTOR,"Closing services...");
        Connection[] connections = server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            connections[i].close();
        }
        if (server != null) {
            server.getUpdateThread().stop();
            server.stop();
            server.close();
        }
        if (serial_arduino != null) {
            serial_arduino.close();
        }
        if (conexion_basedatos != null) {
            conexion_basedatos.close();
        }
        if (auto_db != null) {
            auto_db.stop();
        }
        System.exit(0);
    }

    public void saveServerConfigurations() {
        if (conexion_basedatos.isConnected()) {
            auto_db.save();
        }
        ArchivoObjectosJava.guardarObjecto(conf_server_interno, NOM_ARCH_CONF_SERV);
    }

    /**
     * Clase que administra el servidor via shell
     */
    public class ServerCommandAdministrator implements Runnable {
        private BufferedReader leer;
        private boolean stopInputConsole;
        
        public ServerCommandAdministrator() {
            leer = new BufferedReader(new InputStreamReader(System.in));
            stopInputConsole = false;
        }
        
        @Override
        public void run() {
            try {
                info(SECTOR, "Comandos Disponibles para digitar"
                        + ":'exit','restart','send <arg>',ard <arg[n]>");
                System.out.println(WELCOME);
                while (!stopInputConsole) {
                    String line = leer.readLine().trim();
                    checkCommand(line);
                }
            } catch (IOException ex) {
                warn(SECTOR, "Error al Leer comando ingresado");
            }
        }
        private void stopInputConsole(){
            stopInputConsole = false;
        }
        
        public boolean isStopInputConsole(){
            return stopInputConsole;
        }

        public void checkCommand(String line) {
            if (line.startsWith("exit")) {
                serverSafetyClose();
            } else if (line.startsWith("restart")) {
                /*FALTA COMO HACER EL RESTART*/
            } else if (line.startsWith("send")) {
                try {
                    String mensaje = line.trim().split(" ", 2)[1];
                    ServerMesage sm = new ServerMesage();
                    sm.mensaje = mensaje;
                    server.sendToAllTCP(sm);
                    info(SECTOR, "Mensaje [" + mensaje + "] enviado");
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    error(SECTOR, "El comando [send] requiere argumento");
                }

            } else if (line.startsWith("ard")) {
                try {
                    String mensaje = line.trim().split(" ", 2)[1];
                    try {
                        arduinoComandosEh.sendCommand(mensaje, true);
                    } catch (ArduinoIOException ex) {
                        error(SECTOR, ex.getMessage());
                        ex.printStackTrace();
                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    error(SECTOR, "El comando [ard] requiere argumento");

                }
            } else {
                info(SECTOR, "Comando '" + line + "' no reconocido");
            }
        }
    }

    static class ExtendedHouseConnection extends Connection {

        public boolean isValidConnection;
        public boolean isInvalidConnectionRequestSended;
        public boolean isAnAdministrador;
        public String user;
        public String ip;
    }
}
