/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.server;

import cl.eh.arduino.model.*;
import cl.eh.exceptions.*;
import cl.eh.db.model.*;
import cl.eh.arduino.ReleeShield;
import cl.eh.common.Network.ValidacionConnection;
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
import cl.eh.scripts.EHJavaScriptAdministrator;
import cl.eh.server.MyServer.ExtendedHouseConnection;
import cl.eh.util.ArchivoObjectosJava;
import cl.eh.util.ThreadFrecuente;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.concurrent.Executors;

import static com.esotericsoftware.minlog.Log.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public final class ExtendedHouseSERVER extends MyServer implements ServerInfo {

    private static final String SECTOR = ExtendedHouseSERVER.class.getSimpleName();
    
    private int numero_de_usuarios_conectados;
    private EHJavaScriptAdministrator scripts;
    private ThreadFrecuente thread_sen_tmp, thread_sen_luz;
    private ExecutorService exService;
    private AutomaticDatabaseMaintenance auto_db;
    private ExtendedHouseGeneralAdministrator god;
    private ServerCommandAdministrator serverCommandAdministrator;

    public ExtendedHouseSERVER() {
        super();
        serverCommandAdministrator = new ServerCommandAdministrator();
        addServerListener(new ServerListener());
        startServerTCP();
        thread_sen_tmp = thread_sen_luz = new ThreadFrecuente(_propiedades_server.getDatabase_millisencods_insert());
        if (_serial_arduino.isConnecionArduinoEstablecida()) {
            _serial_arduino.addArduinoEventListener(new ArduinoListener());
        }
        if(_conexion_basedatos.isConnected()){
            auto_db = new AutomaticDatabaseMaintenance(_conexion_basedatos, _conf_server_interno);
            auto_db.start();
            info(SECTOR, "AutomaticDatabaseMaintenance [INICIADO]");
            god = new ExtendedHouseGeneralAdministrator();
            info(SECTOR, "Administrador God [INICIADO]");
            try {
                _lesAdm = new LESAdministador(_conexion_basedatos);
                _lesAdm.addEventoListener(new EventoAutomaticoListener());// solo para rl .. creo
                _lesAdm.start();
                info(SECTOR, "Administrador de Eventos Simple [INICIADO]");
            } catch (LESException ex) {
                error(SECTOR,ex);
            }
        } else {
            info(SECTOR, "Administrador God [ERROR-> NO HAY CONEXION A BD]");
            info(SECTOR, "AutomaticDatabaseMaintenance [ERROR -> NO HAY CONEXION A BD]");
        }
        try {
            if (god != null) {
                scripts = new EHJavaScriptAdministrator(
                        EHJavaScriptAdministrator.SCRIPT_DEFAULT_DIRECTORY,
                        god);
                scripts.startTasks();
                info(SECTOR, "Modulo de Scripting [INICIANDO]");
            } else {
                info(SECTOR, "Modulo de Scripting [ERROR->NO ESTA EL ADMINISTRADOR 'GOD']");
            }
        } catch (Exception ex) {
            error(SECTOR, "Modulo de Scripting [ERROR->" + ex.getMessage() + "]");
        }
    }

    public void serverThreadsStarts() {
        exService = Executors.newCachedThreadPool();
        exService.execute(serverCommandAdministrator);
        exService.execute(thread_sen_tmp);
        exService.execute(thread_sen_luz);
        thread_sen_tmp.startThreadAgain();
        thread_sen_luz.startThreadAgain();
        exService.shutdown();
    }

    public void serverSafetyClose() {
        info(SECTOR,"Saving settings...");
        saveServerConfigurations();
        info(SECTOR,"Closing services...");
        Connection[] connections = _server.getConnections();
        for (int i = 0; i < connections.length; i++) {
            connections[i].close();
        }
        if (_server != null) {
            _server.getUpdateThread().stop();
            _server.stop();
            _server.close();
        }
        if (_serial_arduino != null) {
            _serial_arduino.close();
        }
        if (_conexion_basedatos != null) {
            _conexion_basedatos.close();
        }
        if (auto_db != null) {
            auto_db.stop();
        }
        System.exit(0);
    }

    public void saveServerConfigurations() {
        if (_conexion_basedatos.isConnected()) {
            auto_db.save();
        }
        ArchivoObjectosJava.guardarObjecto(_conf_server_interno, NOM_ARCH_CONF_SERV);
    }
    /**
     * Escucha y administra todos lo eventos programados
     */
    public class EventoAutomaticoListener implements EventoListener {

        private boolean eventoHaEfecuandoAlgunaOperacion;
        private int valor;

        public void eventoPerformed(EventoEvent e) {
            eventoHaEfecuandoAlgunaOperacion = false;
            if (e.getActuador().equalsIgnoreCase("rl")) {
                try {
                    switch (e.getAccion()) {
                        case EventoEvent.ACCI_APAGAR:
                            _relee_sh.powerOffRelee(e.getNumero_actuador());
                            eventoHaEfecuandoAlgunaOperacion = true;
                            valor = 0;
                            break;
                        case EventoEvent.ACCI_CAMBIAR:
                            if (_relee_sh.isReleePowerOn(e.getNumero_actuador())) {
                                _relee_sh.powerOffRelee(e.getNumero_actuador());
                                valor = 0;
                            } else {
                                _relee_sh.powerOnRelee(e.getNumero_actuador());
                                valor = 1;
                            }
                            eventoHaEfecuandoAlgunaOperacion = true;
                            //info(SECTOR,"Paso por cambiar");
                            break;
                        case EventoEvent.ACCI_ENCENDER:
                            eventoHaEfecuandoAlgunaOperacion = true;
                            _relee_sh.powerOnRelee(e.getNumero_actuador());
                            valor = 1;
                            break;
                        default:
                            error(SECTOR, "Operacion Inesperada; accion:[" + e.getAccion() + "]");
                    }
                }catch(RelayException re){
                    error(SECTOR,re);
                }

                if (eventoHaEfecuandoAlgunaOperacion) {
                    ArduinoOutput ao = new ArduinoOutput();
                    ao.dispositivo = e.getActuador();
                    ao.numero = Integer.toString(e.getNumero_actuador());
                    ao.valor = valor;
                    _server.sendToAllTCP(ao);
                    Actuador ef = new Actuador();
                    ef.setNombre(e.getActuador());//RL
                    ef.setNumero(e.getNumero_actuador()); // ej: 0
                    ef.setId(_conexion_basedatos.getIdOfActuador(ef));
                    if (ef.getId() != ConexionExtendedHouse.NULL_ID) { // se registra en bd
                        Historial his = new Historial(0, ef, null,
                                Calendar.getInstance(),
                                ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER,
                                "Localhost",
                                (valor == 0) ? "Apagado" : "Encendido");
                        _conexion_basedatos.addHistorial(his);
                    } else {
                        try {
                            throw new Nivel8Exception("weon te dio -1 el id");
                        } catch (Nivel8Exception ex) {
                            java.util.logging.Logger.getLogger(ExtendedHouseSERVER.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                error(SECTOR, "Nombre actuador distinto de [rl]");
            }
        }
    }

    /**
     * Conosido como el objeto GOD
     * Usado basicamente en los scrips
     */
    public class ExtendedHouseGeneralAdministrator implements ClientArduinoSignal{ //falta el de actuador

        private final String SECTOR = ExtendedHouseGeneralAdministrator.class.getSimpleName();
        public static final String SCRIPT_PARAMETER_NAME = "god";
        private HashMap<Sensor, Float> sensores; // id,valor
        
        public ExtendedHouseGeneralAdministrator(){
            sensores = new HashMap<>();
            updateSensoresMap();
            arduinoGetAllStatus();
        }
        
        private void updateSensoresMap() { // solo 1 llamada desde el contruc.
            Iterator<Sensor> it_sen = _conexion_basedatos.getSensores().iterator();
            while (it_sen.hasNext()) {
                sensores.put(it_sen.next(), Float.NaN);
            }
        }

        public synchronized void print(String string) {
            info(SECTOR, string);
        }

        public void updateSensorValor(ArduinoEvent ardEvt) { // puede sobrecargarse
            Set set = sensores.entrySet();
            Iterator it = set.iterator();
            boolean actualizado = false;
            while (it.hasNext()) {
                Sensor key = (Sensor) ((Map.Entry) it.next()).getKey();
                if (key.getNombre().equalsIgnoreCase(ardEvt.getNombreDisositivo())) {
                    if (key.getNumero() == ardEvt.getNumeroDispositovo()) {
                        sensores.put(key, ardEvt.getValorDispositivo());
                        actualizado = true;
                        break;
                    }
                }

            }
            if (actualizado) {
                info(SECTOR,
                        "Dispositivo [" + ardEvt.getNombreDisositivo()
                        + "," + ardEvt.getNumeroDispositovo() + "] Actualizado (valor actualizado)");
            } else {
                info(SECTOR, "No se encontro el dispositivo [" + ardEvt.getNombreDisositivo()
                        + "," + ardEvt.getNumeroDispositovo() + "] en el cache de la BD.. Imposible actualizar valor");
            }
        }

        public Float getSensorValor(String sensor, int number) {
            Set set = sensores.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                Sensor key = (Sensor) me.getKey();
                Float value = (Float) me.getValue();
                if (key.getNombre().equalsIgnoreCase(sensor)
                        && key.getNumero() == number) {
                    trace(SECTOR, "Sensor [" + key.getNombre() + "," + key.getNumero() + "] Encontrado");
                    return value;
                }
            }
            trace(SECTOR, "Sensor [" + sensor + "," + number + "] NO Encontrado");
            return null;
        }
        
        public boolean accionar(String actuador,int number,boolean activar){ // TRABAJAR ACA
            actuador = actuador.toUpperCase();
            switch (actuador) {
                case RELEE_SIGNAL: {
                    try {
                        if (activar) {
                            _relee_sh.powerOnRelee(number);
                            
                        } else {
                            _relee_sh.powerOffRelee(number);
                        }
                        _EHActionActuador(actuador,number,activar);
                        info(SECTOR,"Se ha prendido ["+actuador+","+number+","+activar+"]");
                        return true;
                    } catch (RelayException ex) {
                        error(SECTOR,ex);
                    }
                }
            }
            return false;
        }
        
        public Float getActuadorValor(String actuador,int number){
            actuador = actuador.toUpperCase();
            switch (actuador) {
                case RELEE_SIGNAL: {
                    try {
                        boolean valor = _relee_sh.isReleePowerOn(number);
                        return valor ? 1F : 0F;
                    } catch (RelayException ex) {
                        error(SECTOR, ex);
                    }
                }
            }
            return Float.NaN; // o no existe
        }
        private void _EHActionActuador(String actuador,int number,boolean activar){
            ArduinoOutput ao = new ArduinoOutput();
            ao.dispositivo = ClientArduinoSignal.RELEE_SIGNAL;
            ao.numero = Integer.toString(number);
            ao.valor = activar ? 1 : 0;
            _server.sendToAllTCP(ao);
            if (_conexion_basedatos.isConnected()) {
                Actuador ef = new Actuador();
                ef.setNombre(ClientArduinoSignal.RELEE_SIGNAL);//RL
                ef.setNumero(number); // ej: 0
                int id = _conexion_basedatos.getIdOfActuador(ef);
                ef.setId(id);
                if (id != ConexionExtendedHouse.NULL_ID) { // se registra en bd
                    Historial his = new Historial(0, ef, null,
                            Calendar.getInstance(),
                            ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER,
                            "Localhost",
                            activar ? "Apagado" : "Encendido");
                    _conexion_basedatos.addHistorial(his);
                } else {
                    error(SECTOR, new Nivel8Exception("No existe el ID en la Base de datos"));
                }
            }
        }

        private void arduinoGetAllStatus() {
            _serial_arduino.enviarSeñal("da");
        }
    }

    /**
     * Escucha todas las salidas de arduino
     */
    public class ArduinoListener implements ArduinoEventListener {

        public void ArduinoEventListener(ArduinoEvent evt) {
            if (god != null) { // se actualiza la informacion de god
                god.updateSensorValor(evt);
            }
            ArduinoOutput arduino_output = new ArduinoOutput();
            arduino_output.dispositivo = evt.getNombreDisositivo();
            arduino_output.numero = Integer.toString(evt.getNumeroDispositovo());
            arduino_output.valor = evt.getValorDispositivo();
            _server.sendToAllTCP(arduino_output);
            arduino_output = null;
            if (_conexion_basedatos.isConnected()) {
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
            int id = _conexion_basedatos.getIdOfSensor(
                    sen);
            sen.setId(id);
            if (id != ConexionExtendedHouse.NULL_ID) {
                _conexion_basedatos.addHistorial(
                        new Historial(0, null, sen,
                        Calendar.getInstance(),
                        ConexionExtendedHouse.EXTENDEDHOUSE_DEFAULT_USER,
                        "localhost",
                        valor));
            }
        }
    }
    /**
     * Escucha todas las conexiones existentes
     */
    public class ServerListener extends Listener {

        @Override
        public void received(Connection c, Object object) {
            ExtendedHouseConnection cc = (ExtendedHouseConnection) c;
            if (object instanceof ValidacionConnection) { // seria la primera señal
                ValidacionConnection val_con = (ValidacionConnection) object;
                if (_conexion_basedatos.isConnected()) {
                    if (_conexion_basedatos.isaValidUser(val_con.user)) {
                        cc.isValidConnection = true;
                        info(SECTOR, "Coneccion [" + val_con.user
                                + "] Aceptada ip[" + cc.ip + "]");
                        cc.ip = val_con.client_ip;
                        cc.user = val_con.user;
                        cc.isAnAdministrador = _conexion_basedatos.isaAdministrador(val_con.user);
                        numero_de_usuarios_conectados++;
                        actualizarUsuarios();
                    } else {
                        cc.isValidConnection = false;
                        info(SECTOR, "Coneccion [" + val_con.user
                                + "] rechasada ip[" + cc.ip + "]");
                        _server.sendToTCP(cc.getID(), new InvalidConnection());
                        cc.isInvalidConnectionRequestSended = true;
                        cc.close();
                    }
                } else {
                    cc.isValidConnection = false;
                    _server.sendToTCP(cc.getID(), new InvalidConnection());
                    cc.isInvalidConnectionRequestSended = true;
                    cc.close();
                    warn(SECTOR, "Coneccion nueva RECHAZADA, ya que, NO EXISTE CONECCION A BD");
                }
            } else if (cc.isValidConnection) { // si la coneccion el valida me comunico con el cliente
                    /*--------------------------OBJECTOS A MANIPULAR-----------------------*/
                if (object instanceof ArduinoInput) {
                    ArduinoInput ai = (ArduinoInput) object;
                    switch (ai.señal) {
                        case ArduinoSignal.RELEE_SIGNAL: {
                            try {
                                if (ai.valor == 1) {
                                    _relee_sh.powerOnRelee(ai.dispositivo);
                                } else {
                                    _relee_sh.powerOffRelee(ai.dispositivo);
                                }
                                ArduinoOutput ao = new ArduinoOutput();
                                ao.dispositivo = ClientArduinoSignal.RELEE_SIGNAL;
                                ao.numero = Integer.toString(ai.dispositivo);
                                ao.valor = ai.valor;
                                _server.sendToAllTCP(ao);
                                //ahora enviar a la bd
                                if (_conexion_basedatos.isConnected()) {
                                    Actuador ef = new Actuador();
                                    ef.setNombre(ClientArduinoSignal.RELEE_SIGNAL);//RL
                                    ef.setNumero(ai.dispositivo); // ej: 0
                                    int id = _conexion_basedatos.getIdOfActuador(ef);
                                    ef.setId(id);
                                    if (id != ConexionExtendedHouse.NULL_ID) { // se registra en bd
                                        Historial his = new Historial(0, ef, null,
                                                Calendar.getInstance(),
                                                cc.user,
                                                cc.ip,
                                                (ai.valor == 0) ? "Apagado" : "Encendido");
                                        _conexion_basedatos.addHistorial(his);
                                    } else {
                                        error(SECTOR, new Nivel8Exception("weon te dio -1 el id"));
                                    }
                                }
                            } catch (RelayException ex) {
                                error(SECTOR,ex);
                            }
                        }
                    }
                    //serial_arduino.enviarSeñal(ai.señal);
                } else if (object instanceof DatabaseQuery) {
                    DatabaseQuery dq = (DatabaseQuery) object;
                } else if (object instanceof AdvanceCommand) {
                    AdvanceCommand ac = (AdvanceCommand) object;
                    serverCommandAdministrator.checkCommand(ac.commando);
                } else if (object instanceof ServerStatusRequest) { // status de to2 el server
                    for (int i = 0; i < ReleeShield.MAX_RELES; i++) {
                        try {
                            ArduinoOutput ao = new ArduinoOutput();
                            ao.dispositivo = ClientArduinoSignal.RELEE_SIGNAL;
                            ao.numero = Integer.toString(i);
                            ao.valor = (_relee_sh.isReleePowerOn(i)) ? 1 : 0;
                            _server.sendToTCP(cc.getID(), ao);
                        } catch (RelayException ex) {
                            error(SECTOR,ex);
                        }
                    }
                    UsersOnline users = new UsersOnline();
                    users.users = numero_de_usuarios_conectados;
                    _server.sendToTCP(cc.getID(), users);

                    ActuadorInfoList ail = new ActuadorInfoList();
                    ail.infoActuador = new ArrayList();
                    for (Actuador act : _conexion_basedatos.getActuadores()) {
                        InfoActuador infoAct = new InfoActuador();
                        infoAct.id = act.getId();
                        infoAct.nombre = act.getNombre();
                        infoAct.numero = act.getNumero();
                        ail.infoActuador.add(infoAct);
                    }
                    _server.sendToTCP(cc.getID(), ail);

                } else if (object instanceof RespaldoRequest) {
                    if (cc.isAnAdministrador) {
                        Network.ListaRespaldos lr = new ListaRespaldos();
                        lr.respaldos = new ArrayList();
                        for (RespaldoBd res : auto_db.getRespaldos()) {
                            Network.Respaldo r = new Network.Respaldo();
                            r.fecha = res.getFecha().getTimeInMillis();
                            r.isRespaldoByUsuario = res.isIsRespaldoByUsuario();
                            r.nom = res.getNom();
                            lr.respaldos.add(r);
                        }
                        _server.sendToTCP(cc.getID(), lr);
                    } else { // enviar wea que es un usuario invalido
                        sendMessage(cc.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                    }

                    //server.sendToTCP(ex_con.getID(), );
                } else if (object instanceof MakeDatabaseRestore) {
                    if (cc.isAnAdministrador) {
                        MakeDatabaseRestore mdb = (MakeDatabaseRestore) object;
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(mdb.fecha);
                        if (auto_db.restoreDatabase(cal, mdb.restaurarYEliminarDatosHastaLaFecha)) {
                            sendMessage(cc.getID(), "Restaurado con Exito "
                                    + (mdb.restaurarYEliminarDatosHastaLaFecha
                                    ? ",Datos posteriores a la fecha eliminados"
                                    : null), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            sendMessage(cc.getID(), "Se ha producido un error al restaurar", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        sendMessage(cc.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (object instanceof MakeDatabaseBackup) {
                    if (cc.isAnAdministrador) {
                        auto_db.backupDatabaseNow();
                    } else {
                        sendMessage(cc.getID(), "No tiene los privilegios suficientes", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (object instanceof PacketePrueba) {
                    PacketePrueba pp = (PacketePrueba) object;
                    switch (pp.numero_prueba) {
                        case 1: {
                            sendMessage(cc.getID(), "Mensaje de prueba", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                } else if (object instanceof EventoRequest) {
                    Network.ListaEventos listE = new ListaEventos();
                    listE.eventos = new ArrayList();
                    for (HiloDeEventoLES les : _lesAdm.getEventInExecutionList()) {
                        Network.Evento evt = new Network.Evento();
                        evt.LesString = les.getEventoString();
                        evt.LesHTMLString = LESCompUtils.getHtmlLESString(les.getEventoString());
                        evt.user = les.getUser();
                        listE.eventos.add(evt);
                    }
                    _server.sendToTCP(cc.getID(), listE);

                } else if (object instanceof CreacionEvento) {
                    CreacionEvento cEvt = (CreacionEvento) object;
                    try {
                        _lesAdm.addEventoSimpleString(cEvt.LES, cc.user);
                        sendMessage(cc.getID(), "Evento creado exitosamente", JOptionPane.INFORMATION_MESSAGE);
                    } catch (LESException ex) {
                        error(SECTOR, ex.toString());
                        ex.printStackTrace();
                        sendMessage(cc.getID(), ex.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                } else if (object instanceof EliminarEventoRequest) {
                    EliminarEventoRequest el = (EliminarEventoRequest) object;
                    if (_lesAdm.removeEventoSiemple(el.lesString)) {
                        sendMessage(cc.getID(), "Evento removido exitosamente", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        sendMessage(cc.getID(), "Error al remover evento", JOptionPane.ERROR);
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
            _server.sendToTCP(iD, srrr);
        }

        private void actualizarUsuarios() {
            UsersOnline users = new UsersOnline();
            users.users = numero_de_usuarios_conectados;
            _server.sendToAllTCP(users);
            if (_serial_arduino.isConnecionArduinoEstablecida()) {
                _serial_arduino.enviarSeñal((int) 'u');
                _serial_arduino.enviarSeñal((numero_de_usuarios_conectados == 0)
                        ? 48 : numero_de_usuarios_conectados);
            }
        }
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
                    _server.sendToAllTCP(sm);
                    info(SECTOR, "Mensaje [" + mensaje + "] enviado");
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    error(SECTOR, "El comando [send] requiere argumento");
                }

            } else if (line.startsWith("ard")) {
                try {
                    String mensaje = line.trim().split(" ", 2)[1];
                    try {
                        _arduinoComandosEh.sendCommand(mensaje, true);
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


}
