/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.logger;

import java.util.EventObject;

/**
 *
 * @author Administrador
 */
class LogginLine extends EventObject{
    private StringBuilder line;
    public LogginLine(Object source,StringBuilder line){
        super(source);
        this.line = line;
    }
    public StringBuilder getLogginLine(){
        return line;
    }
    
}
