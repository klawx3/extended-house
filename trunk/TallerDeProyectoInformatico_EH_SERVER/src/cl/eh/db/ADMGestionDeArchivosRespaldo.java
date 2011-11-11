/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import cl.eh.util.Fecha2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
class ADMGestionDeArchivosRespaldo implements FilenameFilter{

    private final static String extencion = ".respaldo";
    private List<Respaldo> ls_respaldo;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private FileOutputStream salida_formato;
    private FileInputStream entrada_formato;
    private String ruta_respaldos;
    private File f;

    public ADMGestionDeArchivosRespaldo(String ruta_respaldos) {
        this.ruta_respaldos = ruta_respaldos;
        ls_respaldo = new ArrayList<Respaldo>();
    }

    public List<Respaldo> getListRespaldo() {
        return ls_respaldo;
    }

    public void addNuevoRespaldo(Respaldo res) throws IOException {
        f = new File(ruta_respaldos + File.separatorChar
                + Fecha2.getFecha(res.getFecha(), '-') + "_" + Fecha2.getHora(res.getFecha(), ';')
                + extencion);
        salida_formato = new FileOutputStream(f);
        salida = new ObjectOutputStream(salida_formato);
        salida.writeObject(res);
        salida.flush();
        cerrarFlujosSalida(salida);
        cerrarFlujosSalida(salida_formato);
        
        salida = null;
        salida_formato = null;
        f = null;
    }

    public void searchRespaldos() {
        f = new File(ruta_respaldos);
        ls_respaldo.clear();
        for(File file: f.listFiles(this)){
            try {
                File archivo_respaldo = file.getAbsoluteFile();
                entrada_formato = new FileInputStream(archivo_respaldo);
                entrada = new ObjectInputStream(entrada_formato);
                Respaldo res = (Respaldo) entrada.readObject();
                ls_respaldo.add(res);
                System.out.println("asdasdasd!!!!");
            } catch (IOException ex) {
                Logger.getLogger(ADMGestionDeArchivosRespaldo.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ADMGestionDeArchivosRespaldo.class.getName())
                        .log(Level.SEVERE, null, ex);
            }finally{
                cerrarFlujoEntrada(entrada);
                cerrarFlujoEntrada(entrada_formato);
            }
        }
        
        entrada = null;
        salida_formato = null;
        f = null;

    }

    public boolean accept(File dir, String name) {
        if (name.endsWith(extencion)) {
            return true;
        }
        return false;
    }

    public void cerrarFlujoEntrada(InputStream a) {
        if (a != null) {
            try {
                a.close();
            } catch (IOException ex) {
                Logger.getLogger(ADMGestionDeArchivosRespaldo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cerrarFlujosSalida(OutputStream o) {
        if (o != null) {
            try {
                o.close();
            } catch (IOException ex) {
                Logger.getLogger(ADMGestionDeArchivosRespaldo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
