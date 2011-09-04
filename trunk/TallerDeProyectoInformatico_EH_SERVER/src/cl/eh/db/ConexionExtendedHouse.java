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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (isConnected()) {
            try {
                synchronized (con) {
                    est = con.createStatement();
                    est.execute(QueryGenerator.getQuery(obj));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.err.println("No esta conectado.. imposible registar");
        }
        
       
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

    public int getIdOfActuador(Actuador obj) {
        if (obj != null) {
            assert (obj.getNombre() == null || obj.getNumero() == 0);
            String query = "SELECT id FROM actuador "
                    + "WHERE nombre = '" + obj.getNombre() + "' "
                    + "and numero = '" + obj.getNumero() + "'";

            try {
                synchronized (con) {
                    est = con.createStatement();
                    rs = est.executeQuery(query);
                    if (rs.next()) {
                        int i = rs.getInt("id");
                        return i;
                    }
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    public int getIdOfSensor(Sensor obj) {
        if (obj != null) {
            assert (obj.getNombre() == null || obj.getNumero() == 0);
            String query = "SELECT id FROM SENSOR "
                    + "WHERE nombre = '" + obj.getNombre() + "' "
                    + "and numero = '" + obj.getNumero() + "'";
            //System.err.println("-->String sql:"+query);
            try {
                synchronized (con) {
                    est = con.createStatement();
                    rs = est.executeQuery(query);
                    if (rs.next()) {
                        int i = rs.getInt("id");
                        return i;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }
}
