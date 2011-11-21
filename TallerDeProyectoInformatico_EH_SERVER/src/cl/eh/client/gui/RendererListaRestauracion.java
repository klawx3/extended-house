/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client.gui;

import cl.eh.client.status.StatusRespaldos;
import cl.eh.util.Fecha2;
import java.awt.Component;
import java.util.Hashtable;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Usuario
 */
public class RendererListaRestauracion implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    protected static TitledBorder focusBorder = new TitledBorder(LineBorder.createGrayLineBorder(),
            "seleccionado");

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        if (value instanceof StatusRespaldos) {
            StatusRespaldos sr = (StatusRespaldos) value;
            renderer.setFont(new java.awt.Font("Verdana", 0, 11));
            if (sr.isIsCreadoByUser()) {
                renderer.setIcon(
                        new javax.swing.ImageIcon(
                        getClass().getResource(
                        "/cl/eh/images/user.png")));
                renderer.setToolTipText("Creado por usuario");

            } else {
                renderer.setIcon(
                        new javax.swing.ImageIcon(
                        getClass().getResource(
                        "/cl/eh/images/user_silhouette.png")));
                renderer.setToolTipText("Generado automaticamente");
            }
            if (cellHasFocus) {
                focusBorder.setBorder(focusBorder);
            }
            renderer.setText(Fecha2.getFecha(sr.getFecha_res(), '/')
                    + " "
                    + Fecha2.getHora(sr.getFecha_res(), ':'));
        }

        return renderer;
    }
}
