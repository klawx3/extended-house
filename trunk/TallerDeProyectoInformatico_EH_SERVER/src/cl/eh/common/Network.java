/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;

public class Network {

    private static final int PORT = 54555;
    public static  final String LOCALUSER = "-231257235";
    
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Object.class);
        kryo.register(int[].class);
        kryo.register(int[][].class);
        kryo.register(String[].class);
        kryo.register(java.util.ArrayList.class);
        /*----------------Network Classes----------------*/
        kryo.register(ArduinoInput.class);
        kryo.register(ArduinoOutput.class);
        kryo.register(DatabaseQuery.class);
        kryo.register(HistorialList.class);
        kryo.register(Advice.class);
        kryo.register(DatabasePrimitiveResponse.class);
        kryo.register(InvalidConnection.class);
        kryo.register(ValidacionConnection.class);
        kryo.register(AdvanceCommand.class);
        kryo.register(Msg.class);
        kryo.register(ServerStatusRequest.class);

    }

    public static int getNetworkPort() {
        return PORT;
    }


    public static class ArduinoInput {
        public int señal;
        public int valor;
        public int dispositivo; // <------ opcional
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

    public static class Advice{
        public String advice;
    }
    public static class InvalidConnection{}
    
    public static class ValidacionConnection{
        public String user;
        public String pass;
        public String client_ip;
    }
    public static class AdvanceCommand{
        public String commando;
    }
    
    public static class Msg{
        public String mensaje;
    }
}
