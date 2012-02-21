/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.server;


import java.io.IOException;
import com.esotericsoftware.kryonet.Listener;
import cl.eh.common.Network;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import cl.eh.util.ArchivoObjectosJava;
import cl.eh.eventos.LESAdministador;
import cl.eh.db.AutomaticDatabaseMaintenance;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.properties.PropiedadesServer;
import cl.eh.properties.ConfiguracionServidor;
import cl.eh.arduino.ArduinoCommands;
import cl.eh.arduino.ReleeShield;
import cl.eh.arduino.SerialArduino;
import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Administrador
 */
public class MyServer {
    private static final String SECTOR = MyServer.class.getSimpleName();
    private static final String NOM_ARCH_CONF_SERV = "conf.eh";
    
    protected SerialArduino         _serial_arduino;
    protected Server                _server;
    protected ReleeShield           _relee_sh;
    protected ArduinoCommands       _arduinoComandosEh;
    protected ConfiguracionServidor _conf_server_interno;
    protected PropiedadesServer     _propiedades_server;
    protected ConexionExtendedHouse _conexion_basedatos;
    protected LESAdministador       _lesAdm;
    
    private Listener listener;
    public MyServer(){
        _propiedades_server = new PropiedadesServer();
        _propiedades_server.getAllServerPropiedadesOfFile();
        _conf_server_interno = (ConfiguracionServidor) ArchivoObjectosJava.abrirObjecto(NOM_ARCH_CONF_SERV);
        if (_conf_server_interno == null) {
            _conf_server_interno = new ConfiguracionServidor();
            try {
                long millis_backup = Long.parseLong(_propiedades_server.getDatabase_millisecontsToBackup());
                _conf_server_interno.tiempoRestanteNuevoRespaldo = millis_backup;
            } catch (NumberFormatException e) {
                _conf_server_interno.tiempoRestanteNuevoRespaldo = AutomaticDatabaseMaintenance.TIEMPORESTANTEDEFAULT_MILLISECONDS;
            }
            ArchivoObjectosJava.guardarObjecto(_conf_server_interno, NOM_ARCH_CONF_SERV);
            trace(SECTOR, "Detectada 1º Ejecuccion de EH... Archivo " + NOM_ARCH_CONF_SERV + " Creado");
        } else {
            trace(SECTOR, "Archivo " + NOM_ARCH_CONF_SERV + " Abierto Exitosamente!");
        }
        info(SECTOR, _conf_server_interno.tiempoRestanteNuevoRespaldo + " MILLISEGUNDOS para nuevo respaldo de BD.");
        _conexion_basedatos = new ConexionExtendedHouse(
                _propiedades_server.getDatabase_ip(),
                _propiedades_server.getDatabase_name(),
                _propiedades_server.getDatabase_user(),
                _propiedades_server.getDatabase_pass());
        _serial_arduino = new SerialArduino(_propiedades_server.getArduino_port());
        _relee_sh = new ReleeShield(_serial_arduino);
        
        _server = new Server() {

            @Override
            protected Connection newConnection() {
                return new ExtendedHouseConnection();
            }
        };
        Network.register(_server);
        if(_serial_arduino.isConnecionArduinoEstablecida()){
            _arduinoComandosEh = new ArduinoCommands(_serial_arduino, _relee_sh);
        }
        info(SECTOR, "Ingresos a la BD en ([ "
                + ((double) _propiedades_server.getDatabase_millisencods_insert()) / (double) (60 * 1000)
                + " ]min)");

    }
    protected void addServerListener(Listener listener){
        this.listener = listener;
    }
    public void startServerTCP() {
        if (listener != null) {
            _server.addListener(listener);
            try {
                _server.bind(Network.getNetworkPort());
            } catch (IOException ex) {
                error(SECTOR, ex.getMessage());
                error(SECTOR, "Verifique que el puerto ["
                        + Network.getNetworkPort() + "]que no este en uso...");
                System.exit(1);
            }
            _server.start();
        }else{
            error(SECTOR,"No existe ningun listener para el servidor..."
                    + "Utilize el metodo 'addServerListener()' antes de iniciar");
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
