/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.properties;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public final class PropiedadesClient extends Propiedades {
    public static final String NOM_ARCHIVO   = "propClient.dat";
    private static final String CARAC_ARCHIVO = "Propiedades del cliente";
    private static final String IPSERVER      = "ip.server";

    private String ip;


    public PropiedadesClient(){
        super(NOM_ARCHIVO,CARAC_ARCHIVO);
        ip = null;
    }

    public void setAllClientPropiedadesToFile(String ip){

        super.addPropiedadTabla(IPSERVER, ip);
        try {
            super.guardadPropiedades();
        } catch (IOException ex) {
            Logger.getLogger(PropiedadesClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllClientPropiedadesOfFile(){
        try {
            super.cargarPropiedades();
        } catch (IOException ex) {
            Logger.getLogger(PropiedadesClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ip = super.tabla.getProperty(IPSERVER);
    }

    public String getIp() {
        return ip;
    }
}
