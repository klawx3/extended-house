/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Usuario
 */
public class RendererTablaLES extends JLabel implements TableCellRenderer{


    public Component getTableCellRendererComponent
            (JTable table, Object value, boolean isSelected
            , boolean hasFocus, int row, int column) {
        
        
        if(isSelected){
            setBorder(BorderFactory.createEtchedBorder());
        }else{
            setBorder(null);
        }
        
        switch(column){
            case 0:
                if(value instanceof LESString){
                    LESString aux = (LESString) value;
                    setText(aux.getLESString());
                    setIcon(new ImageIcon(getClass().getResource("/cl/eh/images/bullet_black.png")));
                    setForeground(Color.BLUE);
                    this.setToolTipText("Cadena de programacion de evento");
                    setFont(new Font("Verdana", Font.ITALIC, 9));
                }else{
                    System.err.println("Manzo Error");
                }
                break;
            case 1:
                if(value instanceof String){
                    String aux = (String)value;
                    setIcon(new ImageIcon(getClass().getResource("/cl/eh/images/user.png")));
                    setText(aux);
                    setFont(new Font("Verdana",Font.BOLD, 9));
                    setHorizontalAlignment(SwingConstants.CENTER);

                }else{
                    System.err.println("Manzo Error");
                }
                break;
        }
        return this;
    }
    
    @Override
    public void validate() {}
    @Override
    public void revalidate() {}
    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    
}
