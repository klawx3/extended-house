/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.status;

import cl.eh.common.ClientArduinoSignal;

/**
 *
 * @author Usuario
 */
public class StatusExtendedHouse {
    //esto varia segun la cantidad de sensores "reales" que esten conectados
    public static final int MAX_RELAY       = 8;
    public static final int MAX_TEMPERATURA = 1;
    public static final int MAX_MOVIMIENTO  = 1;
    public static final int MAX_MAGNETICO   = 4;
    public static final int MAX_LUMINICO    = 1;
    
    private StatusActuador[]   relay;
    private StatusSensor[]     temperatura;
    private StatusSensor[]     movimiento;
    private StatusSensor[]     magnetico; 
    private StatusSensor[]     luminico;
    
    public StatusExtendedHouse(){ // nota los numero parten del 0
        relay = new StatusActuador[MAX_RELAY];
        for(int i = 0; i < relay.length ; i++){
            relay[i] = new StatusActuador(ClientArduinoSignal.RELEE_SIGNAL,i);
        }
        temperatura = new StatusSensor[MAX_TEMPERATURA];
        for(int i = 0; i  < temperatura.length ; i++){
            temperatura[i] = new StatusSensor(ClientArduinoSignal.TEMPERATURA_SIGNAL,i);
        }
        movimiento = new StatusSensor[MAX_MOVIMIENTO];
        for(int i = 0;  i < movimiento.length ; i++){
            movimiento[i] = new StatusSensor(ClientArduinoSignal.MOVIMIENTO_SIGNAL,i);
        }
        magnetico = new StatusSensor[MAX_MAGNETICO];
        for(int i = 0; i < magnetico.length ; i++){
            magnetico[i] = new StatusSensor(ClientArduinoSignal.INTERRUPTOR_LENGUETA_SIGNAL,i);
        }
        luminico = new StatusSensor[MAX_LUMINICO];
        for(int i = 0;i < luminico.length ; i++){
            luminico[i] = new StatusSensor(ClientArduinoSignal.LUZ_SIGNAL,i);
        }
    }
    
    public void updateSensorTemperatura(int numero_sensor,int nuevo_valor_sensor){
        temperatura[numero_sensor].setValor(nuevo_valor_sensor);
    }
    public void updateSensorMovimiento(int numero_sensor,int nuevo_valor_sensor){
        movimiento[numero_sensor].setValor(nuevo_valor_sensor);
    }
    public void updateSensorMagnetico(int numero_sensor,int nuevo_valor_sensor){
        magnetico[numero_sensor].setValor(nuevo_valor_sensor);
    }
    public void updateActuadorRelay(int numero_sensor,int nuevo_valor_sensor){
        relay[numero_sensor].setValor(nuevo_valor_sensor);
    }
    public void updateSensorLuminico(int numero_sensor,int nuevo_valor_sensor){
        luminico[numero_sensor].setValor(nuevo_valor_sensor);
    }
    
    public int getValorSensorTemperatura(int numero_sensor){
        return temperatura[numero_sensor].getValor();
    }
    public int getValorSensorMovimiento(int numero_sensor){
        return movimiento[numero_sensor].getValor();
    }
    public int getValorSensorMagnetico(int numero_sensor){
        return magnetico[numero_sensor].getValor();
    }
    public int getValorActuadorRelay(int numero_sensor){
        return relay[numero_sensor].getValor();
    }
    public int getValorSensorLuminico(int numero_sensor){
        return luminico[numero_sensor].getValor();
    }
    
    
    
    
}
