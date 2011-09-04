/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.exceptions;

/**
 *
 * @author Usuario
 */
public class Nivel8Exception extends Exception{
    public Nivel8Exception(String causa){
        super("Nivel 8 Exception :"+causa);
    }
}
