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
    public static final String DB_ADMIN = "ADMIN";
    public static final String DB_NORMAL_USER = "USER";
    public static final int NULL_ID = -1;
    public static final String EXTENDEDHOUSE_DEFAULT_USER = "extended_house";
    public static final String PORT = "3306";
    private String ip_db;
    private String nom_db;
    private String user_db;
    private String pass_db;

    public ConexionExtendedHouse(String ip_db, String nom_db, String user_db, String pass_db) {
        super("jdbc:mysql://" + ip_db + "/" + nom_db, user_db, pass_db);
        this.ip_db = ip_db;
        this.nom_db = nom_db;
        this.user_db = user_db;
        this.pass_db = pass_db;
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
        } else {
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
        return ConexionExtendedHouse.NULL_ID;
    }

    public boolean isaValidUser(String user) {
        if (user != null) {
            if (!user.isEmpty()) {
                String query = "SELECT * FROM usuario WHERE usuario = '" + user + "'";
                try {
                    synchronized (con) {
                        est = con.createStatement();
                        rs = est.executeQuery(query);
                        if (rs.next()) {
                            return true;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public boolean isaAdministrador(String user){
        if (user != null) {
            if (!user.isEmpty()) {
                String query = "SELECT * FROM usuario,rol "
                        + "WHERE usuario.rol = rol.id and rol.nombre = '"+DB_ADMIN+"'"
                        + " and usuario.usuario = '"+user+"' ";
                try {
                    synchronized (con) {
                        est = con.createStatement();
                        rs = est.executeQuery(query);
                        if (rs.next()) {
                            return true;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
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
        return ConexionExtendedHouse.NULL_ID;
    }
    
    public boolean eliminarDatosNoInmportantes(){
        String[] tablasAEliminar = {"historial","evento"};
        boolean borrado_ok = true;
        for(int i = 0; i<tablasAEliminar.length;i++){
            borrado_ok = borrado_ok && customQuery("DELETE FROM "+tablasAEliminar[i]);
        }
        return borrado_ok;
    }
    
    public boolean customQuery(String query){
        if (query != null) {
            try {
                synchronized (con) {
                    est = con.createStatement();
                    est.execute(query);
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public String getIp_db() {
        return ip_db;
    }

    public String getNom_db() {
        return nom_db;
    }

    public String getPass_db() {
        return pass_db;
    }

    public String getUser_db() {
        return user_db;
    }
}
