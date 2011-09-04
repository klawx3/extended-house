/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteJApplet.java
 *
 * Created on 16-jul-2011, 19:41:04
 */
package cl.eh.client;

import cl.eh.common.Network;
import cl.eh.common.Network.ValidacionConnection;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.awt.Component;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author Usuario
 */
public class ClienteJApplet extends javax.swing.JApplet {

    private String ipserver;
    private Client client;
    private Thread hilo_conectar;

    @Override
    public void init() {/*
        client = new Client();
        client.start();
        Network.register(client);
        client.addListener(new Listener() {

            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void disconnected(Connection connection) {
                jTextField3.setText("El servidor se ha caido");
            }

            @Override
            public void received(Connection connection, Object object) {
                //---------------OBJECTOS A MANIPULAR-----------------------
                if (object instanceof Network.InvalidConnection) {
                    jTextField3.setText("Conexion rechasada");
                    return;
                }
                //---------------END OBJECTOS A MANIPULAR-------------------
            }
        });
        
        hilo_conectar = new Thread("Conectar a servidor") {
            @Override
            public void run() {
                try {
                    client.connect(5000, ipserver, Network.getNetworkPort());
                    jTextField2.setText("UP");
                    jTextField2.setForeground(new java.awt.Color(0, 255, 0));
                } catch (IOException ex) {
                    jTextField2.setText("DOWN");
                    jTextField2.setForeground(new java.awt.Color(255, 0, 0));
                }
            }
        };
        */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

    }


    @Override
    public void start() {
        System.out.println("EN START");
    }
    @Override
    public void stop(){
        System.out.println("EN STOP");
    }
    @Override
    public void destroy(){
        System.out.println("EN DESTROY");
    }


    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_server = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        btn_conectar = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();

        setStub(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Conectar a ExtendedHouse", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11))); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(640, 480));
        jPanel1.setMinimumSize(new java.awt.Dimension(640, 480));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Server:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 450, -1, -1));
        jPanel1.add(txt_server, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 120, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Estado servidor:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, -1, -1));

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(204, 204, 0));
        jTextField2.setText("UNKNOW");
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, 60, -1));

        btn_conectar.setText("Conectar");
        btn_conectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_conectarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_conectar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, 100, -1));

        jTextField3.setEditable(false);
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 450, 180, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_conectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_conectarActionPerformed
        ipserver = txt_server.getText().trim();
        hilo_conectar.start();
    }//GEN-LAST:event_btn_conectarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_conectar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField txt_server;
    // End of variables declaration//GEN-END:variables
}