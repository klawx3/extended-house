/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.model;

/**
 *
 * @author Usuario
 */
public class LESClientAdmin {


    private boolean isConfiguracionReady;
    private boolean isExtendedFechaSet;


    private String LES;

    public LESClientAdmin() {
        isConfiguracionReady = isExtendedFechaSet = false;
        LES = null;
    }

    public String getLES() {
        return LES;
    }

    public void setLES(String LES) {
        this.LES = LES;
    }

    public boolean isIsConfiguracionReady() {
        return isConfiguracionReady;
    }

    public void setIsConfiguracionReady(boolean isConfiguracionReady) {
        this.isConfiguracionReady = isConfiguracionReady;
    }

    public boolean isIsExtendedFechaSet() {
        return isExtendedFechaSet;
    }

    public void setIsExtendedFechaSet(boolean isExtendedFechaSet) {
        this.isExtendedFechaSet = isExtendedFechaSet;
    }


    
    
}
