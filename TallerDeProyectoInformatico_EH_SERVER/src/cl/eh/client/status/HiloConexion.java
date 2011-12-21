/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.status;

import cl.eh.client.Parametros;
import cl.eh.common.Network;
import com.esotericsoftware.kryonet.Client;
import java.io.IOException;

/**
 *
 * @author Administrador
 */
public class HiloConexion extends Thread {
    private boolean tryToConect;
    private boolean connected;
    private Client client;

    private Parametros params;
    
    public HiloConexion(Client client,Parametros params){
        this.client = client;
        connected = false;
        tryToConect = false;
        this.params = params;
    }
    
    @Override
    public void run(){
        try {
            tryToConect = true;
            client.connect(5000, params.getIp_server(), Network.getNetworkPort());
            connected = true;
        } catch (IOException ex) {
            connected = false;
        }
        tryToConect = false;
    }
    
    public boolean isConnected() {
        return connected;
    }

    public boolean isTryToConect() {
        return tryToConect;
    }
}
