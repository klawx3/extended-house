/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui.model;

/**
 *
 * @author Usuario
 */
public class LESCellObject {
    private LESString string;
    private String user;

    public LESCellObject(LESString string, String user) {
        this.string = string;
        this.user = user;
    }

    public LESString getString() {
        return string;
    }

    public String getUser() {
        return user;
    }
    
    
}
