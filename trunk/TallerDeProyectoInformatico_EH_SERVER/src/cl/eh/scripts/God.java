/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import cl.eh.arduino.ReleeShield;
import cl.eh.arduino.SerialArduino;
import cl.eh.arduino.model.ArduinoEvent;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.db.model.Sensor;
import com.esotericsoftware.kryonet.Server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.esotericsoftware.minlog.Log.*;
/**
 * God clase que administra entradas y salidas para script y otros
 * @author Administrador
 */
public class God { //falta el de actuador
    public static final String SCRIPT_PARAMETER_NAME = "god";
    private ConexionExtendedHouse con_ex;
    private SerialArduino sa;
    private Server server;
    private ReleeShield rs;
    private HashMap<Sensor,Float> sensores; // id,valor
    
    public God(ConexionExtendedHouse con_ex, SerialArduino sa, Server server,ReleeShield rs) {
        this.con_ex = con_ex;
        this.server = server;
        this.sa = sa;
        this.rs = rs;
        sensores = new HashMap<>();
        updateSensoresKey();
        
        
    }
    private void updateSensoresKey() {
        Iterator<Sensor> it_sen = con_ex.getSensores().iterator();
        while (it_sen.hasNext()) {
            sensores.put(it_sen.next(), Float.NaN);
        }
    }
    public void print(String string){
        debug("GOD",string);
    }
    public void updateSensorValor(ArduinoEvent ardEvt){ // puede sobrecargarse
        Set set = sensores.entrySet();
        Iterator it = set.iterator();
        boolean actualizado = false;
        while(it.hasNext()){
            Map.Entry me = (Map.Entry) it.next();
            Sensor key = (Sensor) me.getKey();
            if(key.getNombre().equalsIgnoreCase(ardEvt.getNombreDisositivo())){
                if(key.getNumero() == ardEvt.getNumeroDispositovo()){
                    sensores.put(key, ardEvt.getValorDispositivo());
                    actualizado = true;
                    break;
                }
            }
            
        }
        if(actualizado){
            debug("God",
                    "Dispositivo ["+ardEvt.getNombreDisositivo()
                    +","+ardEvt.getNumeroDispositovo()+"] Actualizado (valor actualizado)");
        }else{
            debug("God","No se encontro el dispositivo ["+ardEvt.getNombreDisositivo()
                    +","+ardEvt.getNumeroDispositovo()+"] en el cache de la BD.. Imposible actualizar valor");
        }
    }
    
    public Float getSensorValor(String sensor,int number){
        Set set = sensores.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry) it.next();
            Sensor key = (Sensor) me.getKey();
            Float value = (Float) me.getValue();
            if(key.getNombre().equalsIgnoreCase(sensor) 
                    && key.getNumero() == number){
                return value;
            }
        }
        return null;
    }
    
}
