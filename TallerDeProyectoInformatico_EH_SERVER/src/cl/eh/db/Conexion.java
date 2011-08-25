/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import cl.eh.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.esotericsoftware.minlog.Log.*;

/**
 *
 * @author Usuario
 */
public class Conexion {

    private static final String SECTOR = Conexion.class.getSimpleName();
    private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
    protected Connection con = null;
    protected Statement est = null;
    protected ResultSet rs = null;
    private boolean isConnected;

    public Conexion(String url_db, String user_db, String pass_db) {
        isConnected = false;
        try {
            Class.forName(CONTROLADOR);
            try {
                con = DriverManager.getConnection(url_db, user_db, pass_db);
                isConnected = true;
                info(SECTOR,"Database Connection open");
            } catch (SQLException ex) {
                warn(SECTOR, "No se ha podido conectar a la db["
                        + url_db + "," + user_db + "," + pass_db + "]");
            }
        } catch (ClassNotFoundException ex) {
            error(SECTOR, "No se ha encontrado el Driver para mysql");
        }
    }
    
    public boolean isConnected(){
        return isConnected;
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (est != null) {
                if (est.isClosed()) {
                    est.close();
                }
            }
            if (con != null) {
                if (!con.isClosed()) {
                    con.close();
                }
            }
            info(SECTOR, "Database conecction close");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
