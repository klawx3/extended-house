/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class MysqlRestore {

    public static boolean restoreDbMysql(Connection con, String script_Db) {
        if (script_Db != null) {
            try {
                return con.createStatement().execute(script_Db);

            } catch (SQLException ex) {
                Logger.getLogger(MysqlRestore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
