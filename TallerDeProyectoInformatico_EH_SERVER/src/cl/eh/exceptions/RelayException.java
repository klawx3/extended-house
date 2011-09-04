/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.exceptions;

/**
 *
 * @author Usuario
 */
public class RelayException extends Exception {
    public RelayException(String causa){
        super("Relay Shield:"+causa);
    }
}
