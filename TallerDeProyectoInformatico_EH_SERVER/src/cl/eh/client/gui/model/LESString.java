/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui.model;

/**
 *
 * @author Usuario
 */
public class LESString {
    private String LESString;
    private String HTMLString;

    public String getHTMLString() {
        return HTMLString;
    }

    public String getLESString() {
        return LESString;
    }

    public LESString(String LESString, String HTMLString) {
        this.LESString = LESString;
        this.HTMLString = HTMLString;
    }
    
    
}
