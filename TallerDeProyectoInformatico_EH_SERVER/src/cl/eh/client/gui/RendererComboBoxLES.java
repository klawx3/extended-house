/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui;

import cl.eh.client.model.ActuadorInfo;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Usuario
 */
public class RendererComboBoxLES
        extends JLabel
        implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof ActuadorInfo) {
            ActuadorInfo info = (ActuadorInfo) value;
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText(info.getNombre() + " " + info.getNumero());
            setIcon(new ImageIcon(getClass().getResource("/cl/eh/images/bullet_black.png")));
            setForeground(Color.BLUE);

        } else {
            System.err.print("manzo error");
        }

        return this;
    }
}
