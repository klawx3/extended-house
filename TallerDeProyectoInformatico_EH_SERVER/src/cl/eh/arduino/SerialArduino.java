/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

import cl.eh.exceptions.ArduinoIOException;
import cl.eh.common.ArduinoHelp;
import cl.eh.serial.SerialOutput;
import cl.eh.arduino.model.ArduinoEvent;
import cl.eh.arduino.model.ArduinoEventListener;
import javax.swing.event.EventListenerList;
import cl.eh.util.Log;
import gnu.io.*;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Usuario
 */
public final class SerialArduino {

    public static final String SECTOR = SerialArduino.class.getSimpleName();
    protected EventListenerList listenerList = new EventListenerList();
    private boolean connecionArduinoEstablecida;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    public static InputStream input;
    public static OutputStream output;
    private SerialPort serialPort;
    private String puerto;
    private SerialOutput serial_output;

    public SerialArduino(String puerto) {
        serial_output        = new SerialOutput(ArduinoHelp.ENDOFSTRING);
        this.puerto = puerto;
        connecionArduinoEstablecida = false;
        CommPortIdentifier portId = null;
        Enumeration portEnum = null;
        try {
            portEnum = CommPortIdentifier.getPortIdentifiers();
        } catch (Exception e) {
            error(SECTOR,e.getMessage());
            error(SECTOR,"Drivers de arduino no encontrados");
            System.exit(1);
        }
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            if (currPortId.getName().equals(this.puerto)) {
                portId = currPortId;
                break;
            }
        }
        if (portId == null) {
            warn(SECTOR,"No se encontro el puerto '" + puerto + "'");
            return;
        }
        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            connecionArduinoEstablecida = true;
            info(SECTOR,"Arduino ["+puerto+"] Connection open");
        } catch (Exception e) {
            error(e.toString());
        }
        try {
            serialPort.addEventListener(new SerialPortEventListener() {
                public void serialEvent(SerialPortEvent evento) {
                    if (evento.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                        try {
                            int available = SerialArduino.input.available();
                            byte chunk[] = new byte[available];
                            SerialArduino.input.read(chunk, 0, available);
                            serial_output.setChuck(chunk);
                            while (serial_output.hasNext()) {
                                String[] dispNumVal = serial_output.next().split(ArduinoHelp.SEPARADOR);
                                if (dispNumVal.length != 3) {
                                    throw new ArduinoIOException("Error al leer:" + serial_output.next());
                                } else {
                                    String nom = dispNumVal[ArduinoHelp.NOMBREDISPOSITIVO_SERIAL_PRINT_POS];
                                    try{
                                        int num = Integer.parseInt(
                                                dispNumVal[
                                                ArduinoHelp.NUMERODISPOSITIVO_SERIAL_PRINT_POS]);
                                        float val = Float.parseFloat(
                                                dispNumVal[
                                                ArduinoHelp.VALORDISPOSITIVO_SERIAL_PRINT_POS]);

                                        ArduinoEvent ardEvent = new ArduinoEvent(this,nom,num,val);
                                        fireArduinoEvent(ardEvent);
                                    }catch(NumberFormatException e){
                                        error(SECTOR, e.toString());
                                    }                               
                                }
                            }
                        } catch (Exception e) {
                            error(SECTOR, e.toString());
                        }
                    }
                }
            });
        } catch (TooManyListenersException ex) {
            Logger.getLogger(SerialArduino.class.getName()).log(Level.SEVERE, null, ex);
        } catch(java.lang.NullPointerException ex){
            error(SECTOR,"Probable utilizacion del puerto en otra aplicacion");
        }
        serialPort.notifyOnDataAvailable(true);
    }
    public void addArduinoEventListener(ArduinoEventListener listener) {
        listenerList.add(ArduinoEventListener.class, listener);
    }
    
    public void removeMyEventListener(ArduinoEventListener listener) {
        listenerList.remove(ArduinoEventListener.class, listener);
    }
    
    protected void fireArduinoEvent(ArduinoEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ArduinoEventListener.class) {
                ((ArduinoEventListener) listeners[i + 1])
                        .ArduinoEventListener(evt);
            }
        }
    }
    
    public boolean isConnecionArduinoEstablecida() {
        return connecionArduinoEstablecida;
    }
    public void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
        info(SECTOR,"Serial port comunication closed");
    }
    
    public synchronized void enviarSeñal(String s) {
        if (isConnecionArduinoEstablecida()) {
            for (int i = 0; i < s.length(); i++) {
                int _char = s.charAt(i);
                try {
                    output.write(_char);
                    output.flush();
                } catch (IOException ex) {
                    Logger.getLogger(SerialArduino.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            info(SECTOR, "No se ha podido enviar señal [" + s + "] a [" + puerto + "], ya que no hay coneccion");
        }

    }

    public synchronized void enviarSeñal(int s) {
        if (isConnecionArduinoEstablecida()) {
            try {
                output.write(s);
                output.flush();
                
            } catch (IOException ex) {
                error(ex.getMessage());
            }
        } else {
            info(SECTOR, "No se ha podido enviar señal ["+(char)s+"] a [" + puerto + "], ya que no hay coneccion");
        }
    }
    
    public synchronized void enviarSeñal(byte[] s) {
        if (isConnecionArduinoEstablecida()) {
            try {
                output.write(s);
                output.flush();
            } catch (IOException ex) {
                error(ex.getMessage());
            }
        } else {
            info(SECTOR, "No se ha podido enviar señal ["+s+"] a [" + puerto + "], ya que no hay coneccion");
        }
    }



}
