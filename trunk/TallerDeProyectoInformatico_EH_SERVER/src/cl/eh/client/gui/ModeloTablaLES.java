/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui;

import cl.eh.client.gui.model.LESCellObject;
import cl.eh.client.gui.model.LESString;
import cl.eh.common.Network;
import cl.eh.common.Network.Evento;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Usuario
 */
public class ModeloTablaLES implements TableModel {
    private List<LESCellObject> ectList;
    private final static int COLUMNAS = 2;
    
    public ModeloTablaLES(List<LESCellObject> ectList){
        this.ectList = ectList;
    }

    public int getRowCount() {
        return ectList.size();
    }

    public int getColumnCount() {
        return COLUMNAS;
    }

    public String getColumnName(int columnIndex) {
        String nomColumna = null;
        switch(columnIndex){
            case 0:
                nomColumna = "Cadena";
                break;
            case 1:
                nomColumna = "Usuario";
                break;
        }
        return nomColumna;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return LESString.class;
            case 1:
                return String.class;

        }
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        LESCellObject les = ectList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return les.getString();
            case 1:
                return les.getUser();
                        
        }
        return null;
        
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        switch (columnIndex) {
//            case 0:
//                if(aValue instanceof LESString){
//                        LESString string = (LESString)aValue;
//                        
//                }else{
//                    System.err.println("manzo error");
//                }
//                
//            case 1:
//                return les.getUser();
//       
//        }
    }

    public void addTableModelListener(TableModelListener l) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeTableModelListener(TableModelListener l) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
