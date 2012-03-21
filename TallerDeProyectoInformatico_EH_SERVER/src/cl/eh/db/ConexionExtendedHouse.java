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
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    
    public static final int QUERYSTATUS_OK = 1;
    public static final int QUERYSTATUS_DUPLICATE = 2;
    public static final int QUERYSTATUS_CRITICAL_ERROR= 3;
    
    public static enum Tabla{
       ACTUADOR("actuador"),
       SENSOR("sensor"),
       UBICACION("ubicacion"),
       USUARIO("usuario"),
       EVENTO("evento_simple"),
       HISTORIAL("historial"),
       ROL("rol");
       
       private final String nom_tabla;
       Tabla(String nom_tabla){
           this.nom_tabla = nom_tabla;
       }
       public String getDbName(){
           return this.nom_tabla;
       }
    }
    
    
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

    public int addEvento(Evento obj)  {
        try {
            addObjectToDatabase(obj);
            return QUERYSTATUS_OK;
        }catch(MySQLIntegrityConstraintViolationException ex){
            ex.printStackTrace();
            return QUERYSTATUS_CRITICAL_ERROR;
        }catch (SQLException ex) {
            ex.printStackTrace();
            return QUERYSTATUS_DUPLICATE;
        }
    }
    
    public boolean removeEvento(String eventoSimpleString){
        String query = "DELETE FROM "+Tabla.EVENTO.getDbName()+" "
                + "WHERE evento_simple = '"+eventoSimpleString+"'";
        return customQuery(query);
    }

    public int addHistorial(Historial obj) {
        try {
            addObjectToDatabase(obj);
            return QUERYSTATUS_OK;
        }catch(MySQLIntegrityConstraintViolationException ex){
            return QUERYSTATUS_CRITICAL_ERROR;
        }catch (SQLException ex) {
            return QUERYSTATUS_DUPLICATE;
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
    
    public List<Actuador> getActuadores() {
        String query = "SELECT actuador.id,actuador.nombre,actuador.numero,ubicacion.nombre as ubicacion,actuador.caracteristicas "
                + "FROM actuador,ubicacion "
                + "WHERE actuador.ubicacion = ubicacion.id";
        if (isConnected()) {
            try {
                List<Actuador> lista = new ArrayList<>();
                synchronized (this) {
                    est = con.createStatement();
                    rs = est.executeQuery(query);
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        int numero = rs.getInt("numero");
                        String ubicacion = rs.getString("ubicacion");
                        String caracteristicas = rs.getString("caracteristicas");
                        Actuador actuador = 
                                new Actuador(id,nombre,numero,ubicacion
                                ,caracteristicas,null,-1);
                        lista.add(actuador);
                    }
                }
                return lista;
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("No esta conectado.. imposible registar");
        }
        return null;
    }
    public List<Sensor> getSensores(){
        String query = "SELECT sensor.id,sensor.nombre,sensor.numero,ubicacion.nombre as ubicacion,sensor.caracteristicas "
                + "FROM sensor,ubicacion "
                + "WHERE sensor.ubicacion = ubicacion.id";
        if (isConnected()) {
            try {
                List<Sensor> lista = new ArrayList<>();
                synchronized (this) {
                    est = con.createStatement();
                    rs = est.executeQuery(query);
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        int numero = rs.getInt("numero");
                        String ubicacion = rs.getString("ubicacion");
                        String caracteristicas = rs.getString("caracteristicas");
                        Sensor sen = new Sensor(id,nombre,numero,ubicacion,caracteristicas,-1);
                        lista.add(sen);
                    }
                }
                return lista;
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("No esta conectado.. imposible registar");
        }
        return null;
    }

    public List<Evento> getEventos() {
        String query = "SELECT * FROM evento_simple";
        if (isConnected()) {
            try {
                List<Evento> lista = new ArrayList<>();
                synchronized (this) {
                    est = con.createStatement();
                    rs = est.executeQuery(query);
                    while (rs.next()) {
                        String eventoSimple = rs.getString("evento_simple");
                        boolean activo = rs.getInt("activo") == 1 ? true : false;
                        String user = rs.getString("usuario");
                        Evento evtoS = new Evento();
                        Usuario usr = new Usuario();
                        usr.setUsuario(user);
                        evtoS.setUsuario(usr);
                        evtoS.setActivo(activo);
                        evtoS.setEvento_simple(eventoSimple);
                        lista.add(evtoS);
                    }
                }
                return lista;
            } catch (SQLException ex) {
                Logger.getLogger(ConexionExtendedHouse.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("No esta conectado.. imposible registar");
        }
        return null;
    }

    public int getIdOfActuador(Actuador obj) {
        if (obj != null) {
            assert (obj.getNombre() == null || obj.getNumero() == 0);
            String query = "SELECT id FROM actuador "
                    + "WHERE nombre = '" + obj.getNombre() + "' "
                    + "and numero = '" + obj.getNumero() + "'";

            try {
                synchronized (this) {
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
                    synchronized (this) {
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
                    synchronized (this) {
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
            String query = "SELECT id FROM sensor "
                    + "WHERE nombre = '" + obj.getNombre() + "' "
                    + "and numero = '" + obj.getNumero() + "'";
            //System.err.println("-->String sql:"+query);
            try {
                synchronized (this) {
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
    
    public boolean customQuery(String query) {
        if (query != null) {
            if (isConnected()) {
                try {
                    synchronized (this) {
                        est = con.createStatement();
                        est.execute(query);
                        return true;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionExtendedHouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return false;
    }
    
    public void addObjectToDatabase(Object obj) 
            throws MySQLIntegrityConstraintViolationException, SQLException{
        if (obj != null) {
            if (isConnected()) {

                    synchronized (this) {
                        est = con.createStatement();
                        String query = QueryGenerator.getQuery(obj);
                        if(query != null){
                            est.execute(query);
                        }else{
                            System.err.println("Error en el generador de consultas");
                        }
                    }

            } else {
                System.err.println("No esta conectado.. imposible registar");
            }
        }else{
            System.err.println("Objeto es null");
        }
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
