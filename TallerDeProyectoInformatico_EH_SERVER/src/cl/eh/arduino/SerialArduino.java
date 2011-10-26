/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

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
    private boolean connecionArduinoEstablecida;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    public static InputStream input;
    public static OutputStream output;
    private SerialPort serialPort;
    private String puerto;

    public SerialArduino(String puerto) {
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
    }

    public void addEventListener(SerialPortEventListener evento)
            throws TooManyListenersException {
        serialPort.addEventListener(evento);
        serialPort.notifyOnDataAvailable(true);
    }

    public void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
        info(SECTOR,"Serial port comunication closed");
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
            info(SECTOR, "No se ha podido enviar señal ["+s+"] a [" + puerto + "], ya que no hay coneccion");
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

    public boolean isConnecionArduinoEstablecida() {
        return connecionArduinoEstablecida;
    }

}
