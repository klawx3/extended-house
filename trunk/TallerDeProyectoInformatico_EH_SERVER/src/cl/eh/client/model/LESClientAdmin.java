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
    private boolean isExtendedFechaSetBeffore;
    private boolean correctSentence;

    public boolean isCorrectSentence() {
        return correctSentence;
    }

    public void setCorrectSentence(boolean correctSentence) {
        this.correctSentence = correctSentence;
    }

    public boolean isIsExtendedFechaSetBeffore() {
        return isExtendedFechaSetBeffore;
    }

    public void setIsExtendedFechaSetBeffore(boolean isExtendedFechaSetBeffore) {
        this.isExtendedFechaSetBeffore = isExtendedFechaSetBeffore;
    }

    private String LES;

    public LESClientAdmin() {
        isConfiguracionReady = isExtendedFechaSetBeffore = isExtendedFechaSet = correctSentence = false;
        LES = null;
    }
    
    public void resetAllValues(){
        isConfiguracionReady = isExtendedFechaSetBeffore = isExtendedFechaSet = correctSentence = false;
        LES = null;
    }

    public final String getLES() {
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
