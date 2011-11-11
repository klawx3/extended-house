/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Archivo {
    /**
     * 
     * @param texto texto a guardar
     * @param ruta ruta del texto a guardar
     * @param sobreEscritura indica si quiere sobre escribir el archivo si existe
     */
    public static void guardarTextoAArchivo(String texto, String ruta, boolean sobreEscritura) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta,!sobreEscritura);
            pw = new PrintWriter(fichero);

            pw.println(texto);

        } catch (Exception e) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e2);
            }
        }
    }
    public static boolean crearDirectorio(String ruta){
        File f = new File(ruta);
        return f.mkdir();
    }
    public static boolean existeDirectorio(String ruta){
        File f = new File(ruta);
        if(f.exists()){
            return f.isDirectory();
        }
        return false;
    }

    public static String recuperarTextoArchivo(String ruta) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder buff = null;
        try {
            archivo = new File(ruta);
            if (archivo.isFile()) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                buff = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    buff.append(linea);
                }
            }else{
                return null;
            }

        } catch (Exception e) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e2);
            }
        }
        return buff.toString();
    }

    public static void copia(String rutaArchivoOrigen, String rutaArchivoDestino) {
        try {
            FileInputStream fileInput = new FileInputStream(rutaArchivoOrigen);
            BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
            FileOutputStream fileOutput = new FileOutputStream(rutaArchivoDestino);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            byte[] array = new byte[1000];
            int leidos = bufferedInput.read(array);
            while (leidos > 0) {
                bufferedOutput.write(array, 0, leidos);
                leidos = bufferedInput.read(array);
            }
            bufferedInput.close();
            bufferedOutput.close();
        } catch (Exception e) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
        }

    }
    public static boolean existeArchivo(String ruta){
        File archivo = new File(ruta);
        return archivo.isFile();
    }
    public static long getFileSize(String ruta){
        File archivo = new File(ruta);
        if(archivo.isFile()){
            return archivo.length();
        }
        return 0;
    }
}
