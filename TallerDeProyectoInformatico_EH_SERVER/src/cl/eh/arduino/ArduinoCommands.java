/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

import cl.eh.common.ArduinoSignal;
import cl.eh.exceptions.ArduinoIOException;

/**
 *
 * @author Usuario
 */
public class ArduinoCommands implements ArduinoSignal{
    private static final int NULL = -1;
    private static final int ALL = 255;
    private static final String STR_SEP = " ";
    private static final int TIPO = 0;
    private static final int COMMANDO = 1;
    private static final int N_DISPOSITIVO = 2;
    private static final int VALOR = 3;
    private SerialArduino serialArduino;
    private ReleeShield rs;


    public ArduinoCommands(SerialArduino serialArduino,ReleeShield rs) {
        this.serialArduino = serialArduino;
        this.rs = rs;
    }
    /**
     * 
     * @param command la cadena fomando se conforma de:<br/>
     * [<b>tipo</b>,<b>comando</b>,<b>nº dispositivo</b>,<b>valor</b>] separados por espacios.<br/>
     * los posibles tipos: [<b>"get"</b> (usado en sensores),<b>set</b>usado en efectores] <br/>
     * los posibles comandos : [<b>"relay"</b>]
     * ,<b>"temp"</b>,<b>"luz"</b>
     * ,<b>"mov"</b>,<b>"mag"</b>,<b>"all"</b>(bonus)] <br/>
     * los posibles dispositivos : (numeros reales positivos enteros). <br/>
     * los posibles valores : "<b>1</b>"(encender o verdadero)
     * ,"<b>0</b>"(apagar o falso)
     * ,"<b>null</b>"(sin valor aparente o valor por defecto). <br/>
     * Ejemplo de uso:<br/>
     * "set relay 0 1" -> acciona el relay numero 0 a encendido <br/>
     * "get temp 0 null" -> arduino imprime por serial
     *  la info de el sensor de temperatura 0<br/>
     * "get mag 3 null" -» arduino imprime por serial
     *  la info de el sensor magnetico 3<br/>
     * "get all null null" -»obtiene el estado de todos los sensores(to2s)</br>
     * "get temp all null" -» obtiene el estado de todos los sensores de temperatura</br>
     * notas: null == -1 ; all == 255
     * @return true si el comando es enviado exitosamente , false en caso contrario
     */
    public synchronized void sendCommand(String command, boolean print_in_console)
            throws ArduinoIOException {
        String c[];
        int numero_dispositivo;
        if ((c = command.split(STR_SEP)).length != 4) {
            throw new ArduinoIOException("Se nececitan 4 arg para enviar commando\n"
                    + "\t[Tipo,Commando,N_Dispositivo,Valor]");
        }
        
        if (isNull(c[N_DISPOSITIVO])) {
            numero_dispositivo = NULL;
        } else if (isAll(c[N_DISPOSITIVO])) {
            numero_dispositivo = ALL;
        } else {
            try {
                numero_dispositivo = Integer.parseInt(c[N_DISPOSITIVO].trim());
            } catch (NumberFormatException e) {
                throw new ArduinoIOException("Valor '" + c[N_DISPOSITIVO] + "' Inesperado");
            }
        }
        /*
        final char RELEE_SIGNAL = 'r';
        final char USERS_SIGNAL = 'u';
        final char DISPLAY_INFO_SIGNAL = 'd';
        final char DISPLAY_INTERRUPTOR_LENGUETA ='l';
        final char DISPLAY_SENSOR_LUZ ='z';
        final char DISPLAY_SENSOR_MOVIMIENTO  ='m';
        final char DISPLAY_SENSOR_TEMPERATURA ='t';
        final char DISPLAY_ALL = 'a';
         */
        if (c[TIPO].equalsIgnoreCase("get")) {
            if (c[COMMANDO].equalsIgnoreCase("temp")) {
                if(isAll(numero_dispositivo)){
                    serialArduino.enviarSeñal(DISPLAY_INFO_SIGNAL);
                    serialArduino.enviarSeñal(DISPLAY_SENSOR_TEMPERATURA);
                }
            } else if (c[COMMANDO].equalsIgnoreCase("mov")) {
                if(isAll(numero_dispositivo)){
                    serialArduino.enviarSeñal(DISPLAY_INFO_SIGNAL);
                    serialArduino.enviarSeñal(DISPLAY_SENSOR_MOVIMIENTO);
                }
            }else if (c[COMMANDO].equalsIgnoreCase("luz")) {
                if(isAll(numero_dispositivo)){
                    serialArduino.enviarSeñal(DISPLAY_INFO_SIGNAL);
                    serialArduino.enviarSeñal(DISPLAY_SENSOR_LUZ);
                }
            } else if (c[COMMANDO].equalsIgnoreCase("mag")) {
                if(isAll(numero_dispositivo)){
                    serialArduino.enviarSeñal(DISPLAY_INFO_SIGNAL);
                    serialArduino.enviarSeñal(DISPLAY_INTERRUPTOR_LENGUETA);
                }
            } else if(isAll(c[COMMANDO])){
                serialArduino.enviarSeñal(DISPLAY_INFO_SIGNAL);
                serialArduino.enviarSeñal(DISPLAY_ALL);
            }else {
                throw new ArduinoIOException("comando '" + c[COMMANDO] + "' Inesperado");
            }
        } else if(c[TIPO].equalsIgnoreCase("set")){
            int valor_dispositivo;
            if (isNull(c[VALOR])) {
                valor_dispositivo = NULL;
            } else {
                try {
                    valor_dispositivo = Integer.parseInt(c[VALOR]);
                    if (valor_dispositivo != 0 && valor_dispositivo != 1) {
                        throw new ArduinoIOException("valor '" + valor_dispositivo + "' invalido para set");
                    }
                } catch (NumberFormatException e) {
                    throw new ArduinoIOException("valor '" + c[VALOR] + "' invalido para set");
                }
            }

            
            if (c[COMMANDO].equalsIgnoreCase("relay")) {
                if(valor_dispositivo == 1){
                    rs.powerOnRelee(numero_dispositivo);
                }else{
                    rs.powerOffRelee(numero_dispositivo);
                }
            } else {
                throw new ArduinoIOException("comando '" + c[COMMANDO] + "' Inesperado");
            }
        }else{
            throw new ArduinoIOException("El tipo deve ser [get] o [set]");
        }

    }
    private boolean isGet(String c[]){
        return c[TIPO].equalsIgnoreCase(STR_SEP);
    }
    private boolean isAll(String s){
       return s.equalsIgnoreCase("all");
    }
    private boolean isAll(int s){
       return s == ALL;
    }
    private boolean isNull(String s){
       return s.equalsIgnoreCase("null");
    }
    private boolean isNull(int s){
        return s == NULL;
    }
}
