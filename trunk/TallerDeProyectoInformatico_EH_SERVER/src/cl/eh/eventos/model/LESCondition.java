/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos.model;

/**
 *
 * @author Usuario
 */
public interface LESCondition {
    public static final int LOGICAL_AND = 1;
    public static final int LOGICAL_OR = 2;
    public static final int NULL = -1;
    
    int getNextCondicion();
    void setNextCondition(int condition);
    
    
}
