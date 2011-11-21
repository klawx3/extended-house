/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.status;

import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class StatusRespaldos {
    private boolean isCreadoByUser;
    private Calendar fecha_res;

    public StatusRespaldos(boolean isCreadoByUser, Calendar fecha_res) {
        this.isCreadoByUser = isCreadoByUser;
        this.fecha_res = fecha_res;
    }

    public Calendar getFecha_res() {
        return fecha_res;
    }

    public void setFecha_res(Calendar fecha_res) {
        this.fecha_res = fecha_res;
    }

    public boolean isIsCreadoByUser() {
        return isCreadoByUser;
    }

    public void setIsCreadoByUser(boolean isCreadoByUser) {
        this.isCreadoByUser = isCreadoByUser;
    }
    
    
}
