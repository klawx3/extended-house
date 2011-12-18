/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.compress.DeflateCompressor;
import com.esotericsoftware.kryo.serialize.CollectionSerializer;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Network {

    private static final int PORT = 54555;
    public static int getNetworkPort() {
        return PORT;
    }
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ArrayList.class,
                new DeflateCompressor(new CollectionSerializer(kryo)));
        kryo.register(int[].class);
        kryo.register(boolean[].class);
        kryo.register(long[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        kryo.register(String[].class);
        kryo.register(Calendar.class);
        kryo.register(GregorianCalendar.class);
        /*----------------Network Classes----------------*/
        kryo.register(ArduinoInput.class);
        kryo.register(ArduinoOutput.class);
        kryo.register(DatabaseQuery.class);
        kryo.register(HistorialList.class);
        kryo.register(ServerInfo.class);
        kryo.register(DatabasePrimitiveResponse.class);
        kryo.register(InvalidConnection.class);
        kryo.register(ValidacionConnection.class);
        kryo.register(AdvanceCommand.class);
        kryo.register(ServerStatusRequest.class);
        kryo.register(UsersOnline.class);
        kryo.register(Respaldo.class);
        kryo.register(ServerMesage.class);
        kryo.register(ListaRespaldos.class);
        kryo.register(RespaldoRequest.class);
        kryo.register(MakeDatabaseRestore.class);
        kryo.register(MakeDatabaseBackup.class);
        kryo.register(ServerErrorInfoToUser.class);
        kryo.register(PacketePrueba.class);
        kryo.register(EventoRequest.class);
        kryo.register(Evento.class);
        kryo.register(CreacionEvento.class);
        kryo.register(ListaEventos.class);
        kryo.register(EliminarEventoRequest.class);
        kryo.register(InfoActuador.class);
        kryo.register(ActuadorInfoList.class);
        
    }

    public static class MakeDatabaseBackup{}
    public static class ServerStatusRequest{}
    public static class RespaldoRequest{}
    public static class InvalidConnection{}
    /*---------------------MANIPULACION EVENTOS---------------------*/
    public static class CreacionEvento{
        public String LES;
    }
    public static class EventoRequest{}
    public static class ListaEventos{
        public ArrayList eventos;
    }
    public static class Evento{
        public String LesString;
        public String LesHTMLString;
        public String user;
    }
    public static class EliminarEventoRequest{
        public String lesString;
    }
    /*------------------------*/
    public static class InfoActuador{
        public int id;
        public String nombre;
        public int numero;
    }
    
    public static class ActuadorInfoList{
        public ArrayList infoActuador;
    }
    
    public static class PacketePrueba{
        public int numero_prueba;
    }
    public static class ServerErrorInfoToUser{
        public int tipo_error;
        public String mensaje;
    }
    public static class MakeDatabaseRestore{
        public long fecha;
        public boolean restaurarYEliminarDatosHastaLaFecha;
    }
    public static class ListaRespaldos{
        public ArrayList respaldos;
    }
    public static class ArduinoInput {
        public int señal;
        public int valor;
        public int dispositivo;
    }
    public static class Respaldo {
        public String nom;
        public long fecha;
        public boolean isRespaldoByUsuario;
    }
    public static class ArduinoOutput {
        public String dispositivo;
        public String numero;
        public float valor;
    }
    public static class DatabaseQuery {
        public String query;
        public int tipo;
    }
    public static class DatabasePrimitiveResponse{
        public String tabla;
        public String campo;
        public String valor;
    }
    public static class HistorialList {
        public ArrayList lista;
    }
    public static class ServerInfo{
        public String server_info;
    }
    public static class ValidacionConnection{
        public String user;
        public String client_ip;
    }
    public static class AdvanceCommand{
        public String commando;
    }
    public static class ServerMesage{
        public String mensaje;
    }
    public static class UsersOnline{
        public int users;
    }
}
