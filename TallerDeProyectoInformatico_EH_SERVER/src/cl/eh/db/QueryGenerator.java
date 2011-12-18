/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db;

import cl.eh.db.model.Evento;
import cl.eh.db.model.Historial;
import cl.eh.db.util.MysqlCalendar;

/**
 *
 * @author Usuario
 */
public class QueryGenerator {

    public static String getQuery(Object object){
        String query = null;
        if(object instanceof Historial){

            Historial his = (Historial)object;
            String v2 = his.getActuador() == null ? "NULL" : String.valueOf(his.getActuador().getId());
            String v3 = his.getSensor()   == null ? "NULL" : String.valueOf(his.getSensor().getId());
            //ejemplo fecha mysql latin -> 2011-07-13 21:21:5
            String v4 = MysqlCalendar.getMysqlDateString(his.getFecha());
            String v5 = his.getUser();
            String v6 = his.getIp();
            query = "INSERT INTO historial "
                    + "VALUES(NULL,"+v2+","+v3+",'"+v4+"','"+v5+"','"+v6+"','"+his.getDetalle()+"')";
        }else if(object instanceof Evento){

            Evento evnt = (Evento)object;
            String v1 = evnt.getEvento_simple();
            String v2 = evnt.isActivo() ? "1" : "0";
            String v3 = evnt.getUsuario().getUsuario();
            query = "INSERT INTO evento_simple VALUES(NULL,'"+v1+"',"+v2+",'"+v3+"')";
        }
        //System.out.println("QGEN:"+query);
        return query;
    }

}
