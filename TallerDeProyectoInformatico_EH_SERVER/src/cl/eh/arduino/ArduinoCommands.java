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
    private static final String STR_SEP = " ";
    private static final int TIPO = 0;
    private static final int COMMANDO = 1;
    private static final int N_DISPOSITIVO = 1;
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
     * los posibles tipos: [<b>"get"</b>,<b>set</b>] <br/>
     * los posibles comandos : [<b>"relay"</b>]
     * ,<b>"temp"</b>,<b>"luz"</b>
     * ,<b>"mov"</b>,<b>"mag"</b>] <br/>
     * los posibles dispositivos : (numeros reales positivos enteros). <br/>
     * los posibles valores : "<b>1</b>"(encender o verdadero)
     * ,"<b>0</b>"(apagar o falso)
     * ,"<b>null</b>"(sin valor aparente o valor por defecto). <br/>
     * Ejemplo de uso:<br/>
     * "set relay 0 1" -> acciona el relay numero 0 a encendido <br/>
     * "get temp 0 null" -> arduino imprime por serial
     *  la info de el sensor de temperatura 0<br/>
     * "get mag 3 null" -> arduino imprime por serial
     *  la info de el sensor magnetico 3<br/>
     * 
     * 
     * 
     * 
     * @return true si el comando es enviado exitosamente , false en caso contrario
     */
    public boolean sendCommand(String command,boolean print_in_console)
            throws ArduinoIOException {
        String c[];
        int d;
        if ((c = command.split(STR_SEP)).length != 4) {
            throw new ArduinoIOException("Se nececitan 4 arg para enviar commando\n"
                    + "\t[Tipo,Commando,N_Dispositivo,Valor]");
        }
        try{
            d = Integer.parseInt(c[N_DISPOSITIVO].trim());
        }catch(NumberFormatException e){
            throw new ArduinoIOException("Valor '"+c[VALOR]+"' Inesperado");
        }
        if(isGet(c)){
            if(c[COMMANDO].equalsIgnoreCase("relay")){
            
            }else if(c[COMMANDO].equalsIgnoreCase("temp")){
            
            }else if(c[COMMANDO].equalsIgnoreCase("mov")){

            }else if(c[COMMANDO].equalsIgnoreCase("mag")){
            
            }
        }else{
            if(c[COMMANDO].equalsIgnoreCase("relay")){
            
            }else if(c[COMMANDO].equalsIgnoreCase("temp")){
            
            }else if(c[COMMANDO].equalsIgnoreCase("mov")){

            }else if(c[COMMANDO].equalsIgnoreCase("mag")){
            
            }
        }
        
        return true;

    }
    private boolean isGet(String c[]){
        return c[TIPO].equalsIgnoreCase(STR_SEP);
    }
}
