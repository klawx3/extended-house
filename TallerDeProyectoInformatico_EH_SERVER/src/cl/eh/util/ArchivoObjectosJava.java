/*
 * To change this template, choose Tools | Templates
// * and open the template in the editor.
 */
package cl.eh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ArchivoObjectosJava {

    public static Object abrirObjecto(String ruta_nombre) {
        try {
            FileInputStream entrada_formato = null;
            ObjectInputStream entrada = null;
            try {
                File archivo_respaldo = new File(ruta_nombre);
                entrada_formato = new FileInputStream(archivo_respaldo);
                entrada = new ObjectInputStream(entrada_formato);
                return entrada.readObject();
            } catch(java.io.FileNotFoundException ex){
                return null;
            }
            catch (IOException ex) {
                return null;
            }
        } catch (ClassNotFoundException ex) {
            return null;
        }
        
    }
    
    public static void guardarObjecto(Object obj,String ruta_nombre){
        try {
            FileOutputStream salida_formato;
            ObjectOutputStream salida;
            File f = new File(ruta_nombre);
            salida_formato = new FileOutputStream(f);
            salida = new ObjectOutputStream(salida_formato);
            salida.writeObject(obj);
            salida.flush();
            cerrarFlujosSalida(salida);
            cerrarFlujosSalida(salida_formato);
        } catch (IOException ex) {
            Logger.getLogger(ArchivoObjectosJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void cerrarFlujosSalida(OutputStream o) {
        if (o != null) {
            try {
                o.close();
            } catch (IOException ex) {}
        }
    }
    
}
