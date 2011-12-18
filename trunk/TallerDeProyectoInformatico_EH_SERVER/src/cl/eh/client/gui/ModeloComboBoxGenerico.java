/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Usuario
 */
public class ModeloComboBoxGenerico<T> implements ComboBoxModel{
    private List<T> lista;
    private T objectoSeleccionado;

    public ModeloComboBoxGenerico(List<T> lista){
        this.lista = lista;
    }
    public void setSelectedItem(Object anItem) {
        objectoSeleccionado = (T) anItem;
    }
    public Object getSelectedItem() {
        return objectoSeleccionado;
    }
    public int getSize() {
        return lista.size();
    }

    public Object getElementAt(int index) {
        return lista.get(index);
    }
    public void addListDataListener(ListDataListener l){}
    public void removeListDataListener(ListDataListener l){}
}
