/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.logger;
import java.text.DateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import static com.esotericsoftware.minlog.Log.*;
/**
 *
 * @author Administrador
 */
public class MyLogger extends Logger{
    private java.io.BufferedWriter escritor = null;
    private cl.eh.db.ConexionExtendedHouse conexion = null;
    @Override
    public void log(int level, String category, String message, Throwable ex) {
        StringBuilder builder = new StringBuilder(256);
        Date now = new Date();
        builder.append(DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.MEDIUM).format(now));
        switch (level) {
            case LEVEL_ERROR:
                builder.append(" ERROR: ");
                break;
            case LEVEL_WARN:
                builder.append("  WARN: ");
                break;
            case LEVEL_INFO:
                builder.append("  INFO: ");
                break;
            case LEVEL_DEBUG:
                builder.append(" DEBUG: ");
                break;
            case LEVEL_TRACE:
                builder.append(" TRACE: ");
                break;
        }
        builder.append('[');
        builder.append(category);
        builder.append("] ");
        builder.append(message);
        if (ex != null) {
            StringWriter writer = new StringWriter(256);
            ex.printStackTrace(new PrintWriter(writer));
            builder.append('\n');
            builder.append(writer.toString().trim());
        }
        /*-----------------------------*/
        System.out.println(builder);
        escribirToFile(builder);
        
    }
    public void setLoggingFileName(String fileName) throws java.io.IOException{
        escritor = new java.io.BufferedWriter(new java.io.FileWriter(fileName,true));
    }
    public void setDatabaseLogging(cl.eh.db.ConexionExtendedHouse conexion){
        conexion = conexion;
    }

    public void removeLoggingFileName() {
        if (escritor != null) {
            try {
                escritor.flush();
                escritor.close();
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
        escritor = null;
    }
    public void removeDatabaseLogging(){
        conexion = null;
    }
    
    private void escribirToFile(StringBuilder texto){
        if(escritor != null){
            try {
                escritor.write(texto.toString());
                escritor.newLine();
                escritor.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
