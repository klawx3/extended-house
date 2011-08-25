/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db;

import cl.eh.db.model.Actuador;
import cl.eh.db.model.Evento;
import cl.eh.db.model.Historial;
import cl.eh.db.model.Rol;
import cl.eh.db.model.Sensor;
import cl.eh.db.model.Usuario;

/**
 *
 * @author Usuario
 */
public final class ConexionExtendedHouse extends Conexion implements ExtendedHouseDatabaseModel {

    public ConexionExtendedHouse(String ip_db, String nom_db, String user_db, String pass_db) {
        super("jdbc:mysql://" + ip_db + "/" + nom_db, user_db, pass_db);
    }

    public void addActuador(Actuador obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addEvento(Evento obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addHistorial(Historial obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addRol(Rol obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addSensor(Sensor obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addUsuario(Usuario obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
