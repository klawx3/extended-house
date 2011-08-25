/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db;

import cl.eh.db.model.Evento;
import cl.eh.db.model.Historial;
import cl.eh.util.MysqlCalendar;

/**
 *
 * @author Usuario
 */
public class QueryGenerator {

    public static String getQuery(Object object){
        String query = null;
        if(object instanceof Historial){

            Historial his = (Historial)object;
            String v2 = his.getId_actuador() == 0 ? "NULL" : String.valueOf(his.getId_actuador());
            String v3 = his.getId_sensor() == 0 ? "NULL" : String.valueOf(his.getId_actuador());
            //ejemplo fecha mysql latin -> 2011-07-13 21:21:5
            String v4 = MysqlCalendar.getMysqlDateString(his.getFecha());
            query = "INSERT INTO historial "
                    + "VALUES(NULL,'"+v2+"','"+v3+"','"+v4+"','"+his.getDetalle()+"')";
        }else if(object instanceof Evento){

            Evento evnt = (Evento)object;
            String v3, v4, v5, v6, v7;
            v3 = Integer.toString(evnt.getId_actuador());
            v4 = (evnt.getId_sensor() == 0)
                    ? "NULL" : Integer.toString(evnt.getId_sensor());
            v5 = (evnt.getId_sensor_sec() == 0)
                    ? "NULL" : Integer.toString(evnt.getId_sensor_sec());
            v6 = MysqlCalendar.getMysqlDateString(evnt.getTiempo());
            v7 = evnt.isIncluyente() ? "1" : "0";
            query = "INSERT INTO evento "
                    + "VALUES(NULL,'" + evnt.getNombre_evento()
                    + "'," + v3 + "," + v4 + "," + v5 + ",'" + v6 + "'," + v7 + ")";
        }
        return query;
    }

}
