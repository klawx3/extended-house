/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import cl.eh.db.util.MysqlBackUp;
import cl.eh.util.Archivo;
import cl.eh.util.Fecha2;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class AutomaticDatabaseMaintenance extends Thread {
    public static final int SEMANAL = 0;
    public static final int MENSUAL = 1;
    public static final int DIARIOS = 2;
    private ConexionExtendedHouse ehCon;
    private MysqlBackUp mysqlUp;
    private static final String RUTA_RESPALDOS = "backups";
    private ADMGestionDeArchivosRespaldo adm_gdar;
    private boolean parado = true;
    
    
    public AutomaticDatabaseMaintenance(final ConexionExtendedHouse ehCon,int parametros_respaldo){
        this.ehCon = ehCon;
        mysqlUp = new MysqlBackUp(ehCon.getIp_db(),ConexionExtendedHouse.PORT,
                ehCon.getUser_db(),ehCon.getPass_db(),ehCon.getNom_db());
        adm_gdar = new ADMGestionDeArchivosRespaldo(RUTA_RESPALDOS);
        parado = false;
    }
    
    public Thread getThread(){
        return this;
    }
    
    public void parar(){
        parado = true;
    }
//    public List<Respaldo> getRespaldos(){
//        return null;
//    }

    private String getDatabaseBackup() {
        try {
            return mysqlUp.getData();
        } catch (Exception ex) {
            Logger.getLogger(AutomaticDatabaseMaintenance.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private boolean restoreDatabase(boolean sobreEscritura){
        return false;
    }

    @Override
    public void run() {
        try {
            comprobacionArchivos();
            //------------------------<<
            adm_gdar.addNuevoRespaldo(new Respaldo("asd",Calendar.getInstance(),getDatabaseBackup()));
            adm_gdar.searchRespaldos();
            for (Respaldo res : adm_gdar.getListRespaldo()) {
                System.err.println(Fecha2.getHora(res.getFecha(), '-'));
            }
        } catch (IOException ex) {
            Logger.getLogger(AutomaticDatabaseMaintenance.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void comprobacionArchivos() {
        if (!Archivo.existeDirectorio(RUTA_RESPALDOS)) {
            Archivo.crearDirectorio(RUTA_RESPALDOS);
        }
    }
    
   
    

}
