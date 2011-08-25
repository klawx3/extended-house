/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Usuario
 * esta clase esta hecha para ser heredada
 */

public class Propiedades {

    protected Properties tabla;
    private String nombre_Archivo;
    private String info_Archivo;

    public Propiedades(String nom_Archivo,String info_Archivo){
        tabla = new Properties();
        this.nombre_Archivo = nom_Archivo;
        this.info_Archivo = info_Archivo;
    }

    public static boolean isPropiedadesExist(String archivo){
        File f = new File(archivo);
        return (f.isFile() && f.exists() );
    }

    protected void addPropiedadTabla(String prop,String valor){
        tabla.setProperty(prop, valor);
    }

    protected void cargarPropiedades() throws IOException{
        FileInputStream entrada = new FileInputStream(nombre_Archivo);
        tabla.load(entrada);
    }

    protected void guardadPropiedades() throws IOException{
        FileOutputStream salida = new FileOutputStream(nombre_Archivo);
        tabla.store(salida, info_Archivo);
        salida.close();
    }

}
