/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.db;

import java.io.File;
import cl.eh.db.util.MysqlRestore;
import cl.eh.db.util.MysqlBackUp;
import cl.eh.properties.ConfiguracionServidor;
import cl.eh.properties.Guardable;
import cl.eh.util.Archivo;
import cl.eh.util.Fecha;
import cl.eh.util.Fecha2;
import cl.eh.util.Hora;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.esotericsoftware.minlog.Log.*;

/**
 *
 * @author Usuario
 */
public final class AutomaticDatabaseMaintenance implements Runnable, Guardable {
    
    private static final String SECTOR = AutomaticDatabaseMaintenance.class.getSimpleName();
    public static final long TIEMPORESTANTEDEFAULT_MILLISECONDS = 10000; //(1000 * 60 * 24 * 7); // 10080000
    private static final String RUTA_RESPALDOS = "backups";
    private static final String BackupSQLFileScript = "tempSQLScript.sql";
    private long hilo_millis_start, hilo_millis_last_backup,tiempoRestanteNuevoRespaldo;
    private ScheduledExecutorService exe_programado;
    private ADMGestionDeArchivosRespaldo adm_gdar;
    private ConexionExtendedHouse ehCon;
    private MysqlBackUp mysqlUp;
    private ConfiguracionServidor conf_serv;
    private MysqlRestore mysqlRes;
    private boolean isRespaldoOnceExcecuted;

    public AutomaticDatabaseMaintenance(final ConexionExtendedHouse ehCon, ConfiguracionServidor conf_serv) {
        this.ehCon = ehCon;
        mysqlUp = new MysqlBackUp(ehCon.getIp_db(), ConexionExtendedHouse.PORT,
                ehCon.getUser_db(), ehCon.getPass_db(), ehCon.getNom_db());
        mysqlRes = new MysqlRestore(ehCon.getUser_db(),ehCon.getPass_db(),ehCon.getNom_db());
        adm_gdar = new ADMGestionDeArchivosRespaldo(RUTA_RESPALDOS);
        this.conf_serv = conf_serv;
        tiempoRestanteNuevoRespaldo = this.conf_serv.tiempoRestanteNuevoRespaldo;
        isRespaldoOnceExcecuted = false;
    }

    /**
     * Inicia el hilo
     */
    public void start() {
        comprobacionArchivos();
        exe_programado = Executors.newScheduledThreadPool(2);
        exe_programado.scheduleAtFixedRate(this, tiempoRestanteNuevoRespaldo, TIEMPORESTANTEDEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); // ojjo
        hilo_millis_start = System.currentTimeMillis();
    }

    /**
     * Para el hilo  
     */
    public void stop() {
        exe_programado.shutdown();
    }

    /**
     * Guarda las configuraciones en ConfiguracionServidor
     */
    public void save() {
        long hilo_millis_finish = System.currentTimeMillis();
        if (isRespaldoOnceExcecuted) {
            conf_serv.tiempoRestanteNuevoRespaldo = Math.abs(TIEMPORESTANTEDEFAULT_MILLISECONDS
                    - (hilo_millis_finish - hilo_millis_last_backup));
        } else {
            conf_serv.tiempoRestanteNuevoRespaldo = Math.abs(conf_serv.tiempoRestanteNuevoRespaldo
                    - (hilo_millis_finish - hilo_millis_start));
        }
        System.out.println("ASD -conf_serv.tiempoRestanteNuevoRespaldo:" + conf_serv.tiempoRestanteNuevoRespaldo);
    }
    /**
     * 
     * @return List<Respaldo> obtiene una lista de respaldos
     */
    public List<RespaldoBd> getRespaldos() {
        List<RespaldoBd> ls = null;
        synchronized(adm_gdar){
            ls = adm_gdar.getListRespaldo();
        }
        return ls;
    }

    private String getDatabaseBackup() {
        try {
            return mysqlUp.getData();
        } catch (Exception ex) {
            Logger.getLogger(AutomaticDatabaseMaintenance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void backupDatabaseNow(){
        try {
            synchronized (adm_gdar) {
                adm_gdar.addNuevoRespaldo(
                        new RespaldoBd("EH.RESTORE", Calendar.getInstance(), getDatabaseBackup(), true));
                info(SECTOR, "Respaldo REMOTO efectuado Exitosamente [" + Fecha.getFecha(true, true)
                        + " A las "
                        + Hora.getHoraActual() + "]");
            }
        } catch (IOException ex) {
            Logger.getLogger(AutomaticDatabaseMaintenance.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    
    public boolean restoreDatabase(Calendar cal, boolean borrarTodoLoAnterior) {
        if (!adm_gdar.getListRespaldo().isEmpty()) {
            RespaldoBd res = null;
            for (int i = 0; i < adm_gdar.getListRespaldo().size(); i++) {
                if ((res = adm_gdar.getListRespaldo().get(i)).getFecha().equals(cal)) {
                    respaldoToSQLScriptFile(res);
                    String rutaResp = new File(BackupSQLFileScript).getAbsolutePath();
                    mysqlRes.restoreDbMysql(rutaResp);
                    info(SECTOR, "BD. Restaurado EXITOSAMENTE a la fecha:"
                            + Fecha2.getFecha(res.getFecha(), '/')
                            + " "
                            + Fecha2.getHora(res.getFecha(), ':'));
                    break;
                }
            }
        }
        return true;
    }
    /**
     * restaura el ultimo respaldo efectuado
     * @return true si se ha restaurado la base de datos
     */
    public boolean restoreDatabase() {
        if (!adm_gdar.getListRespaldo().isEmpty()) {
            RespaldoBd res = adm_gdar.getListRespaldo().get(adm_gdar.getListRespaldo().size());
            return ehCon.customQuery(res.getContenido());
        }
        return false;

    }

    @Override
    public void run() {
        try {
            synchronized (adm_gdar) {
                adm_gdar.addNuevoRespaldo(
                        new RespaldoBd("EH.RESTORE", Calendar.getInstance(), getDatabaseBackup(), false));
                info(SECTOR, "Respaldo efectuado Exitosamente [" + Fecha.getFecha(true, true)
                        + " A las "
                        + Hora.getHoraActual() + "]");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(AutomaticDatabaseMaintenance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!isRespaldoOnceExcecuted) {
            isRespaldoOnceExcecuted = true;
        }
        hilo_millis_last_backup = System.currentTimeMillis();
    }

    private void comprobacionArchivos() {
        if (!Archivo.existeDirectorio(RUTA_RESPALDOS)) {
            Archivo.crearDirectorio(RUTA_RESPALDOS);
        }
    }
    private void respaldoToSQLScriptFile(RespaldoBd res){
        Archivo.guardarTextoAArchivo(res.getContenido(), BackupSQLFileScript, false);
    }
}
