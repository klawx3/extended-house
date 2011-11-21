/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui;

import cl.eh.client.status.StatusRespaldos;
import cl.eh.util.Fecha2;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Usuario
 */
public class ModeloListaRestauracion implements ListModel {
    private List<StatusRespaldos> listaStatusRespaldo;

    
    public void actualizarLista(List<StatusRespaldos> listaStatusRespaldo){
        this.listaStatusRespaldo = listaStatusRespaldo;
    }
    
    public int getSize() {
        return listaStatusRespaldo.size();
    }

    public Object getElementAt(int index) {
        StatusRespaldos sr = listaStatusRespaldo.get(index);
        return sr;
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
    
}
