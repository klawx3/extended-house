/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.compress.DeflateCompressor;
import com.esotericsoftware.kryo.serialize.FieldSerializer;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;

public class Network {

    private static final int PORT = 54555;
    public static  final String LOCALUSER = "-231257235";
    
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Object.class, new DeflateCompressor(new FieldSerializer(kryo, Object.class)));
        kryo.register(int[].class, new DeflateCompressor(new FieldSerializer(kryo, int[].class)));
        kryo.register(int[][].class, new DeflateCompressor(new FieldSerializer(kryo, int[][].class)));
        kryo.register(String[].class, new DeflateCompressor(new FieldSerializer(kryo, String[].class)));
        kryo.register(java.util.ArrayList.class, new DeflateCompressor(new FieldSerializer(kryo, java.util.ArrayList.class)));
        /*----------------Network Classes----------------*/
        kryo.register(ArduinoInput.class, new DeflateCompressor(new FieldSerializer(kryo, ArduinoInput.class)));
        kryo.register(ArduinoOutput.class, new DeflateCompressor(new FieldSerializer(kryo, ArduinoOutput.class)));
        kryo.register(DatabaseQuery.class, new DeflateCompressor(new FieldSerializer(kryo, DatabaseQuery.class)));
        kryo.register(HistorialList.class, new DeflateCompressor(new FieldSerializer(kryo, HistorialList.class)));
        kryo.register(ServerInfo.class, new DeflateCompressor(new FieldSerializer(kryo, ServerInfo.class)));
        kryo.register(DatabasePrimitiveResponse.class, new DeflateCompressor(new FieldSerializer(kryo, DatabasePrimitiveResponse.class)));
        kryo.register(InvalidConnection.class, new DeflateCompressor(new FieldSerializer(kryo, InvalidConnection.class)));
        kryo.register(ValidacionConnection.class, new DeflateCompressor(new FieldSerializer(kryo, ValidacionConnection.class)));
        kryo.register(AdvanceCommand.class, new DeflateCompressor(new FieldSerializer(kryo, AdvanceCommand.class)));
        kryo.register(ServerStatusRequest.class, new DeflateCompressor(new FieldSerializer(kryo, ServerStatusRequest.class)));
        kryo.register(UsersOnline.class, new DeflateCompressor(new FieldSerializer(kryo, UsersOnline.class)));
        kryo.register(ServerMesage.class, new DeflateCompressor(new FieldSerializer(kryo, ServerMesage.class)));
    }

    public static int getNetworkPort() {
        return PORT;
    }


    public static class ArduinoInput {
        public int señal;
        public int valor;
        public int dispositivo;
    }
    
    public static class ServerStatusRequest{
    
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
    public static class InvalidConnection{}
    
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
