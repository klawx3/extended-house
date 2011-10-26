/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.exceptions;

/**
 *
 * @author Usuario
 */
public class ExtendedHouseDatabaseException extends Exception  {
    public ExtendedHouseDatabaseException(String causa){
        super("Exepcion en la Base de datos :"+causa);
    }
}
