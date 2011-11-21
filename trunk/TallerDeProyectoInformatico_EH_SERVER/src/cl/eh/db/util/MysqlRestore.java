/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db.util;

import cl.eh.util.Archivo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class MysqlRestore {
    
    private static final String ARCHIVOAUXILIAR = "mysqlRestore.bat";
    private String user;
    private String password;
    private String db;
    private String mysql;

    public MysqlRestore(String database_user, String database_password, String database_name,String mysqlPath) {
        setSettings(database_user,database_password, database_name, mysqlPath);
    }

    public MysqlRestore(String database_user, String database_password, String database_name) {
        setSettings( database_user,database_password, database_name, null);
    }

    public final void setSettings(
            String database_user, String database_password, String database_name,
            String mysqlPath) {

        user = database_user;
        password = database_password;
        db = database_name;
        mysql = mysqlPath;
    }

    public boolean restoreDbMysql(String ruta_script) {//SOLO WINDOWS POR EL MOMENTO
        String exec = "mysql " + db + " --user=" + user + " --password=" + password + " < " + "\"" + ruta_script + "\"";
        Archivo.guardarTextoAArchivo(exec, ARCHIVOAUXILIAR, true);
        String ruta_archivo_aux = new File(ARCHIVOAUXILIAR).getAbsolutePath();
        try {
            Process run = Runtime.getRuntime().exec("\""+ruta_archivo_aux+"\"" );
            //Archivo.eliminarArchivo(ARCHIVOAUXILIAR);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MysqlRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
