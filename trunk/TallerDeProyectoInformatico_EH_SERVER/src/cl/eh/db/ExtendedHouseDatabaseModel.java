/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import cl.eh.db.model.*;

/**
 *
 * @author Usuario
 */
public interface ExtendedHouseDatabaseModel {
    
    public void addActuador(Actuador obj);
    public void addEvento(Evento obj);
    public void addHistorial(Historial obj);
    public void addRol(Rol obj);
    public void addSensor(Sensor obj);
    public void addUsuario(Usuario obj);
    
}
