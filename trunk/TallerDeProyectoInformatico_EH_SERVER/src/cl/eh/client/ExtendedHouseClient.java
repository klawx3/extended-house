/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExtendedHouseClient.java
 *
 * Created on 04-sep-2011, 1:00:20
 */
package cl.eh.client;

import cl.eh.client.gui.ModeloComboBoxGenerico;
import cl.eh.client.gui.ModeloListaRestauracion;
import cl.eh.client.gui.ModeloTablaLES;
import cl.eh.client.gui.RendererComboBoxLES;
import cl.eh.client.gui.RendererListaRestauracion;
import cl.eh.client.gui.model.LESCellObject;
import cl.eh.client.gui.model.LESString;
import cl.eh.client.gui.model.RendererTablaLES;
import cl.eh.client.model.ActuadorInfo;
import cl.eh.client.model.LESClientAdmin;
import cl.eh.client.status.StatusExtendedHouse;
import cl.eh.client.status.StatusRespaldos;
import cl.eh.common.ArduinoSignal;
import cl.eh.common.ClientArduinoSignal;
import cl.eh.common.Network;
import cl.eh.common.Network.*;
import cl.eh.eventos.compilator_utils.LESCompUtils;
import cl.eh.util.Fecha2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.jtattoo.plaf.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableColumn;

/**
 *
 * @author Usuario
 */
public class ExtendedHouseClient extends javax.swing.JApplet {

    private final boolean pruebaLocal = true;
    private Client client;
    private StatusExtendedHouse status_eh;
    private Parametros params;
    private ImageIcon sen_pasivo, sen_activo;
    private ModeloListaRestauracion modelo_lista_rest;
    private List<ActuadorInfo> actuadorInfoList;
    LESClientAdmin les_client;
    /*
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/sensor_pasivo.png"))); // NOI18N
    jLabel2.setText("jLabel2");
    
    jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
     */

    /** Initializes the applet ExtendedHouseClient */
    @Override
    public void init() {
        les_client = new LESClientAdmin();
        actuadorInfoList = new ArrayList<ActuadorInfo>();
        status_eh = new StatusExtendedHouse();
        modelo_lista_rest = new ModeloListaRestauracion();
        if (pruebaLocal) {
            params = new Parametros("localhost", "localhost", "extended_house");
        } else {
            params = new Parametros(
                    super.getParameter(Parametros.PARAM_SERVERIP),
                    super.getParameter(Parametros.PARAM_CLIENTIP),
                    super.getParameter(Parametros.PARAM_USER));
        }

        loadCliente();
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
//                    try {
//                        UIManager.put("ClassLoader", getClass().getClassLoader());
//                        UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
//
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(ExtendedHouseClient.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (InstantiationException ex) {
//                        Logger.getLogger(ExtendedHouseClient.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IllegalAccessException ex) {
//                        Logger.getLogger(ExtendedHouseClient.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (UnsupportedLookAndFeelException ex) {
//                        Logger.getLogger(ExtendedHouseClient.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    initComponents();
                    sen_pasivo = new ImageIcon(getClass().getResource("/cl/eh/images/selection.png"));
                    sen_activo = new ImageIcon(getClass().getResource("/cl/eh/images/sensor_pasivo.png"));
                    jFormattedTextField3.setEnabled(false);
                    jComboBox4.setEnabled(false);
                    jMenuBar1.setVisible(true);
                    
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
//        jLabel3.setText("ip_cliente" + params.getIp_cliente());
//        jLabel28.setText("ipserver:" + params.getIp_server());
//        jLabel29.setText("user:" + params.getUser());

    }
    private void loadCliente() {
        Log.set(Log.LEVEL_DEBUG);
        client = new Client();
        client.start();
        Network.register(client);
        client.addListener(new Listener() {

            @Override
            public void connected(Connection connection) {
                ValidacionConnection vc = new ValidacionConnection();
                if (!pruebaLocal) {
                    vc.user = params.getUser();
                    vc.client_ip = params.getIp_cliente();
                } else {
                    vc.user = "extended_house";
                    vc.client_ip = "127.0.0.1";
                }

                client.sendTCP(vc); //validacion de connecion
                client.sendTCP(new ServerStatusRequest()); //peticion del estado del servidor

            }

            @Override
            public void disconnected(Connection connection) {
                jTextField2.setText("Desconectado");
                jTextField2.setForeground(new java.awt.Color(255, 0, 0));
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.InvalidConnection) {
                    jTextField2.setText("Rechazada");
                } else if (object instanceof ArduinoOutput) {
                    ArduinoOutput ao = (ArduinoOutput) object;
                    String dispostv = ao.dispositivo;
                    int numero = Integer.parseInt(ao.numero);
                    int valor = (int) ao.valor;

                    if (dispostv.equals(ClientArduinoSignal.TEMPERATURA_SIGNAL)) {
                        status_eh.updateSensorTemperatura(numero, valor);

                        jLabel7.setText(Integer.toString(status_eh.getValorSensorTemperatura(
                                Integer.parseInt(ao.numero))));

                    } else if (dispostv.equals(ClientArduinoSignal.LUZ_SIGNAL)) {
                        status_eh.updateSensorLuminico(numero, valor);

                        jLabel5.setText(Integer.toString(status_eh.getValorSensorLuminico(
                                Integer.parseInt(ao.numero))));

                    } else if (dispostv.equals(ClientArduinoSignal.RELEE_SIGNAL)) {
                        status_eh.updateActuadorRelay(numero, valor);
                        switch (numero) {
                            case 0: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele1_off, false);
                                    setButtonEnable(btn_rele1_on, true);
                                } else {
                                    setButtonEnable(btn_rele1_off, true);
                                    setButtonEnable(btn_rele1_on, false);
                                }
                                break;
                            }
                            case 1: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele2_off, false);
                                    setButtonEnable(btn_rele2_on, true);
                                } else {
                                    setButtonEnable(btn_rele2_off, true);
                                    setButtonEnable(btn_rele2_on, false);
                                }
                                break;
                            }
                            case 2: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele3_off, false);
                                    setButtonEnable(btn_rele3_on, true);
                                } else {
                                    setButtonEnable(btn_rele3_off, true);
                                    setButtonEnable(btn_rele3_on, false);
                                }
                                break;
                            }
                            case 3: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele4_off, false);
                                    setButtonEnable(btn_rele4_on, true);
                                } else {
                                    setButtonEnable(btn_rele4_off, true);
                                    setButtonEnable(btn_rele4_on, false);
                                }
                                break;
                            }
                            case 4: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele5_off, false);
                                    setButtonEnable(btn_rele5_on, true);
                                } else {
                                    setButtonEnable(btn_rele5_off, true);
                                    setButtonEnable(btn_rele5_on, false);
                                }
                                break;
                            }
                            case 5: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele6_off, false);
                                    setButtonEnable(btn_rele6_on, true);
                                } else {
                                    setButtonEnable(btn_rele6_off, true);
                                    setButtonEnable(btn_rele6_on, false);
                                }
                                break;
                            }
                            case 6: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele7_off, false);
                                    setButtonEnable(btn_rele7_on, true);
                                } else {
                                    setButtonEnable(btn_rele7_off, true);
                                    setButtonEnable(btn_rele7_on, false);
                                }
                                break;
                            }
                            case 7: {
                                if (valor == 0) {
                                    setButtonEnable(btn_rele8_off, false);
                                    setButtonEnable(btn_rele8_on, true);
                                } else {
                                    setButtonEnable(btn_rele8_off, true);
                                    setButtonEnable(btn_rele8_on, false);
                                }
                                break;
                            }
                        }

                    } else if (dispostv.equals(ClientArduinoSignal.MOVIMIENTO_SIGNAL)) {
                        status_eh.updateSensorMovimiento(numero, valor);
                        if(status_eh.getValorSensorMovimiento(numero) == 1){
                            setLabelSensorIcon(jLabel17, true);
                        }else{
                            setLabelSensorIcon(jLabel17, false);
                        }



                    } else if (dispostv.equals(ClientArduinoSignal.INTERRUPTOR_LENGUETA_SIGNAL)) {
                        status_eh.updateSensorMagnetico(numero, valor);

                        switch (numero) {
                            case 0: {
                                if (status_eh.getValorSensorMagnetico(numero) == 1) {
                                    setLabelSensorIcon(jLabel9, true);
                                } else {
                                    setLabelSensorIcon(jLabel9, false);
                                }
                                break;
                            }
                            case 1: {
                                if (status_eh.getValorSensorMagnetico(numero) == 1) {
                                    setLabelSensorIcon(jLabel13, true);
                                } else {
                                    setLabelSensorIcon(jLabel13, false);
                                }
                                break;
                            }
                            case 2: {
                                if (status_eh.getValorSensorMagnetico(numero) == 1) {
                                    setLabelSensorIcon(jLabel14, true);
                                } else {
                                    setLabelSensorIcon(jLabel14, false);
                                }
                                break;
                            }
                            case 3: {
                                if (status_eh.getValorSensorMagnetico(numero) == 1) {
                                    setLabelSensorIcon(jLabel15, true);
                                } else {
                                    setLabelSensorIcon(jLabel15, false);
                                }
                                break;
                            }

                        }
                    }
                } else if (object instanceof UsersOnline) {
                    UsersOnline uo = (UsersOnline) object;
                    txt_usuarios.setText(Integer.toString(uo.users));
                } else if (object instanceof ServerMesage) {
                    ServerMesage sm = (ServerMesage) object;
                    jTextField3.setText(sm.mensaje);
                } else if (object instanceof ListaRespaldos) {
                    ListaRespaldos lr = (ListaRespaldos) object;
                    Iterator ir = lr.respaldos.iterator();
                    status_eh.getListRespaldos().clear();
                    while (ir.hasNext()) {
                        Network.Respaldo rr = (Network.Respaldo) ir.next();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(rr.fecha);
                        StatusRespaldos sr = new StatusRespaldos(rr.isRespaldoByUsuario, cal);
                        status_eh.getListRespaldos().add(sr); 
                    }
                    modelo_lista_rest.actualizarLista(status_eh.getListRespaldos());
                    jList1.setModel(modelo_lista_rest);
                    jList1.setCellRenderer(new RendererListaRestauracion());

                } else if (object instanceof ServerErrorInfoToUser) {
                    final ServerErrorInfoToUser seitu = (ServerErrorInfoToUser) object;
                    new Thread(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(rootPane, seitu.mensaje, "Mensaje desde el servidor", seitu.tipo_error);
                        }
                    }).start();

                } else if(object instanceof Network.ListaEventos){
                    ListaEventos listEvents = (ListaEventos) object;
                    Iterator it = listEvents.eventos.iterator();
                    List<LESCellObject> asd = new ArrayList<LESCellObject>();
                    while(it.hasNext()){
                        Evento evt = (Evento) it.next();
                        LESString string = new LESString(evt.LesString,evt.LesHTMLString);
                        LESCellObject lesco = new LESCellObject(string,evt.user);
                        asd.add(lesco);
                    }// luego manipular la wea
                    ModeloTablaLES modelotabla = new ModeloTablaLES(asd);
                    jTable1.setModel(modelotabla);
                    int vColIndex = 0;
                    TableColumn col = jTable1.getColumnModel().getColumn(vColIndex);
                    col.setCellRenderer(new RendererTablaLES());
                    col = jTable1.getColumnModel().getColumn(vColIndex+1);
                    col.setCellRenderer(new RendererTablaLES());
                    
                } else if(object instanceof ActuadorInfoList){
                    ActuadorInfoList actInfoList = (ActuadorInfoList) object;
                    Iterator it = actInfoList.infoActuador.iterator();
                    cbo_actuador.removeAllItems();
                    while(it.hasNext()){
                        InfoActuador info = (InfoActuador) it.next();
                        ActuadorInfo clientInfo = new ActuadorInfo(info.id,info.nombre,info.numero);
                        actuadorInfoList.add(clientInfo);
                        cbo_actuador.addItem(clientInfo);
                    }
                    cbo_actuador.setRenderer(new RendererComboBoxLES());
                }

            }
            private void setLabelSensorIcon(JLabel a, boolean b) {
                if (b) {//activado
                    a.setIcon(sen_pasivo);
                } else {
                    a.setIcon(sen_activo);
                }
            }
        });
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPanel14 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbo_actuador = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jButton12 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jButton10 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel27 = new javax.swing.JLabel();
        txt_usuarios = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        btn_rele7_off = new javax.swing.JButton();
        btn_rele7_on = new javax.swing.JButton();
        btn_rele1_on = new javax.swing.JButton();
        btn_rele2_on = new javax.swing.JButton();
        btn_rele3_on = new javax.swing.JButton();
        btn_rele4_on = new javax.swing.JButton();
        btn_rele5_on = new javax.swing.JButton();
        btn_rele6_on = new javax.swing.JButton();
        btn_rele1_off = new javax.swing.JButton();
        btn_rele2_off = new javax.swing.JButton();
        btn_rele3_off = new javax.swing.JButton();
        btn_rele4_off = new javax.swing.JButton();
        btn_rele5_off = new javax.swing.JButton();
        btn_rele6_off = new javax.swing.JButton();
        btn_rele8_off = new javax.swing.JButton();
        btn_rele8_on = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton16 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel26 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jDialog1.setAlwaysOnTop(true);
        jDialog1.setBounds(new java.awt.Rectangle(0, 0, 350, 300));
        jDialog1.setMinimumSize(new java.awt.Dimension(350, 300));
        jDialog1.setModal(true);
        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuracion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 9))); // NOI18N
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel2.setText("Actuador");
        jPanel14.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        cbo_actuador.setFont(new java.awt.Font("Verdana", 0, 9));
        cbo_actuador.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel14.add(cbo_actuador, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 170, -1));

        jLabel28.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel28.setText("Estado");
        jPanel14.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jComboBox3.setFont(new java.awt.Font("Verdana", 0, 9));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Encendido", "Apagado", "Cambiar" }));
        jPanel14.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 80, -1));

        jButton12.setText("Guardar configuracion");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 43, 360, 30));

        jDialog1.getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 80));

        jLabel29.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel29.setText("Actuador Nº");
        jDialog1.getContentPane().add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/book_open.png"))); // NOI18N
        jButton13.setText("Limpiar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 100, -1));

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/book_add.png"))); // NOI18N
        jButton14.setText("Aceptar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, 110, -1));

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/book_previous.png"))); // NOI18N
        jButton15.setText("Salir");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 100, -1));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Deficion de fecha/tiempo del evento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 9))); // NOI18N

        jLabel32.setText("(");

        jLabel33.setText(")");

        jLabel34.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel34.setText("Ejecutar Cada:");

        jComboBox4.setFont(new java.awt.Font("Verdana", 0, 9));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "millisegundos", "horas", "minutos", "segundos" }));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/add.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel31.setText("Fecha:");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("d-MM-yyyy"))));

        jLabel30.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel30.setText("Hora:");

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.MEDIUM))));

        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel36.setText("(");

        jLabel37.setText(")");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel32)
                .addGap(6, 6, 6)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(6, 6, 6)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(5, 5, 5)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(12, 12, 12)
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox1))
                .addGap(9, 9, 9)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel32))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(16, 16, 16)
                        .addComponent(jCheckBox1))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel37))
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jDialog1.getContentPane().add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 380, 100));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadena Resultante", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 9))); // NOI18N

        jEditorPane1.setContentType("text/html");
        jEditorPane1.setFont(new java.awt.Font("Verdana", 0, 10));
        jScrollPane4.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog1.getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 380, 130));

        setForeground(new java.awt.Color(255, 255, 255));

        jToolBar1.setFloatable(false);

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jLabel1.setText("Server status:");
        jToolBar1.add(jLabel1);

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jToolBar1.add(jTextField2);
        jToolBar1.add(jSeparator2);

        jLabel27.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jLabel27.setText("Usuarios Conectados:");
        jToolBar1.add(jLabel27);

        txt_usuarios.setEditable(false);
        txt_usuarios.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jToolBar1.add(txt_usuarios);

        jTabbedPane1.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 11), new java.awt.Color(0, 0, 153))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel18.setText("Rele 7:");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel19.setText("Rele 1:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel20.setText("Rele 2:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel21.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel21.setText("Rele 3:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel22.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel22.setText("Rele 4:");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel23.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel23.setText("Rele 5:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel24.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel24.setText("Rele 6:");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        btn_rele7_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele7_off.setText("Off");
        btn_rele7_off.setEnabled(false);
        btn_rele7_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele7_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele7_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 110, 20));

        btn_rele7_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele7_on.setText("On");
        btn_rele7_on.setEnabled(false);
        btn_rele7_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele7_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele7_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 110, 20));

        btn_rele1_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele1_on.setText("On");
        btn_rele1_on.setEnabled(false);
        btn_rele1_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele1_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele1_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 110, 20));

        btn_rele2_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele2_on.setText("On");
        btn_rele2_on.setEnabled(false);
        btn_rele2_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele2_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele2_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 110, 20));

        btn_rele3_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele3_on.setText("On");
        btn_rele3_on.setEnabled(false);
        btn_rele3_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele3_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele3_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 110, 20));

        btn_rele4_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele4_on.setText("On");
        btn_rele4_on.setEnabled(false);
        btn_rele4_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele4_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele4_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 110, 20));

        btn_rele5_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele5_on.setText("On");
        btn_rele5_on.setEnabled(false);
        btn_rele5_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele5_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele5_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 110, 20));

        btn_rele6_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele6_on.setText("On");
        btn_rele6_on.setEnabled(false);
        btn_rele6_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele6_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele6_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 110, 20));

        btn_rele1_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele1_off.setText("Off");
        btn_rele1_off.setEnabled(false);
        btn_rele1_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele1_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele1_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 110, 20));

        btn_rele2_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele2_off.setText("Off");
        btn_rele2_off.setEnabled(false);
        btn_rele2_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele2_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele2_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 110, 20));

        btn_rele3_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele3_off.setText("Off");
        btn_rele3_off.setEnabled(false);
        btn_rele3_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele3_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele3_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 110, 20));

        btn_rele4_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele4_off.setText("Off");
        btn_rele4_off.setEnabled(false);
        btn_rele4_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele4_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele4_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 110, 20));

        btn_rele5_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele5_off.setText("Off");
        btn_rele5_off.setEnabled(false);
        btn_rele5_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele5_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele5_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 110, 20));

        btn_rele6_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele6_off.setText("Off");
        btn_rele6_off.setEnabled(false);
        btn_rele6_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele6_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele6_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 110, 20));

        btn_rele8_off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/apagado.png"))); // NOI18N
        btn_rele8_off.setText("Off");
        btn_rele8_off.setEnabled(false);
        btn_rele8_off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele8_offActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele8_off, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 110, 20));

        btn_rele8_on.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/encendido.png"))); // NOI18N
        btn_rele8_on.setText("On");
        btn_rele8_on.setEnabled(false);
        btn_rele8_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rele8_onActionPerformed(evt);
            }
        });
        jPanel2.add(btn_rele8_on, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 110, 20));

        jLabel25.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/switch.png"))); // NOI18N
        jLabel25.setText("Rele 8:");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 300, 210));

        jTabbedPane1.addTab("Actuadores", jPanel5);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 11))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/lightbulb.png"))); // NOI18N
        jLabel4.setText("Sensor Luz:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel5.setText("0");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 50, -1));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/timeline_marker.png"))); // NOI18N
        jLabel6.setText("Temperatura:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel7.setText("0");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 50, -1));

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 220, 220));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Puertas y Ventanas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 11))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Movimiento"));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 230, 90));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/magnet.png"))); // NOI18N
        jLabel10.setText("Magnetico 1:");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/magnet.png"))); // NOI18N
        jLabel11.setText("Magnetico 2:");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/magnet.png"))); // NOI18N
        jLabel12.setText("Magnetico 3:");
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/magnet.png"))); // NOI18N
        jLabel8.setText("Magnetico 4:");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 20, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 20, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
        jPanel7.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 20, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 20, -1));

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 230, 120));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movimiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 11))); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Movimiento"));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 230, 90));

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/transmit.png"))); // NOI18N
        jLabel16.setText("Detector Nº1:");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/selection.png"))); // NOI18N
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        jPanel4.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 230, 90));

        jTabbedPane1.addTab("Sensores", jPanel4);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 120));
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 380, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/script.png"))); // NOI18N
        jButton2.setText("Enviar Comando");
        jPanel6.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 140, -1));

        jTabbedPane1.addTab("Avanzado", jPanel6);

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Restauracion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11))); // NOI18N

        jList1.setFont(new java.awt.Font("Verdana", 0, 11));
        jScrollPane2.setViewportView(jList1);

        jButton4.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/find.png"))); // NOI18N
        jButton4.setText("Obtener Lista");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/database.png"))); // NOI18N
        jButton5.setText("Restaurar respaldo Seleccionado");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 250));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Respaldo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11))); // NOI18N

        jButton6.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/disk.png"))); // NOI18N
        jButton6.setText("Crear nuevo respaldo");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 210, 250));

        jTabbedPane1.addTab("Gestion de Respaldos", jPanel11);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setFont(new java.awt.Font("Verdana", 0, 11));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "LES", "User"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 586, 113));

        jButton3.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/find.png"))); // NOI18N
        jButton3.setText("Obtener Información");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 170, 30));

        jButton7.setText("enviar LES");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, -1, -1));

        jButton8.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/delete.png"))); // NOI18N
        jButton8.setText("Eliminar Evento");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 158, 30));

        jButton9.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/add.png"))); // NOI18N
        jButton9.setText("Crear LES");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, 150, 30));

        jButton11.setText("jButton11");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, 91, -1));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Creacion de Evento"));

        jTextPane1.setContentType("text/html");
        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Verdana", 0, 11));
        jScrollPane5.setViewportView(jTextPane1);

        jButton16.setFont(new java.awt.Font("Verdana", 0, 11));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/alarm.png"))); // NOI18N
        jButton16.setText("Comenzar Ejcucion de evento");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel3.setText("ES Actual:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(11, 11, 11)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(jButton16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, -1, 90));

        jTabbedPane1.addTab("Gestion de eventos", jPanel3);

        jToolBar2.setFloatable(false);

        jLabel26.setText("Server Info:");
        jToolBar2.add(jLabel26);

        jTextField3.setEditable(false);
        jToolBar2.add(jTextField3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/user.png"))); // NOI18N
        jMenu1.setText("Menu");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/connect.png"))); // NOI18N
        jMenuItem1.setText("Conectar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/disconnect.png"))); // NOI18N
        jMenuItem2.setText("Desconectar");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_rele1_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele1_onActionPerformed
        int señal = 0;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele1_onActionPerformed

    private void btn_rele1_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele1_offActionPerformed
        int señal = 0;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele1_offActionPerformed

    private void btn_rele2_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele2_onActionPerformed
        int señal = 1;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele2_onActionPerformed

    private void btn_rele2_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele2_offActionPerformed
        int señal = 1;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele2_offActionPerformed

    private void btn_rele3_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele3_onActionPerformed
        int señal = 2;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele3_onActionPerformed

    private void btn_rele3_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele3_offActionPerformed
        int señal = 2;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele3_offActionPerformed

    private void btn_rele4_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele4_onActionPerformed
        int señal = 3;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele4_onActionPerformed

    private void btn_rele4_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele4_offActionPerformed
        int señal = 3;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele4_offActionPerformed

    private void btn_rele5_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele5_onActionPerformed
        int señal = 4;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele5_onActionPerformed

    private void btn_rele5_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele5_offActionPerformed
        int señal = 4;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele5_offActionPerformed

    private void btn_rele6_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele6_onActionPerformed
        int señal = 5;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele6_onActionPerformed

    private void btn_rele6_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele6_offActionPerformed
        int señal = 5;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele6_offActionPerformed

    private void btn_rele7_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele7_onActionPerformed
        int señal = 6;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele7_onActionPerformed

    private void btn_rele7_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele7_offActionPerformed
        int señal = 6;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele7_offActionPerformed

    private void btn_rele8_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele8_onActionPerformed
        int señal = 7;
        int valor = 1;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele8_onActionPerformed

    private void btn_rele8_offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rele8_offActionPerformed
        int señal = 7;
        int valor = 0;
        ArduinoInput ai = new ArduinoInput();
        ai.dispositivo = señal;
        ai.señal = ArduinoSignal.RELEE_SIGNAL;
        ai.valor = valor;
        client.sendTCP(ai);
    }//GEN-LAST:event_btn_rele8_offActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         client.sendTCP(new RespaldoRequest());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        client.sendTCP(new Network.MakeDatabaseBackup());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        StatusRespaldos sr = (StatusRespaldos) jList1.getSelectedValue();
        if(sr != null){

            MakeDatabaseRestore mdr = new MakeDatabaseRestore();
            mdr.fecha = sr.getFecha_res().getTimeInMillis();
            mdr.restaurarYEliminarDatosHastaLaFecha = true;
            client.sendTCP(mdr);
        }else{
            JOptionPane.showMessageDialog(rootPane
                    , "Deve seleccionar un respaldo 1º"
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
        
       
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        client.sendTCP(new Network.EventoRequest());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Network.CreacionEvento ce = new CreacionEvento();
//        ce.LES = jTextField4.getText();
        client.sendTCP(ce);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            Object e = jTable1.getModel().getValueAt(selectedRow, 0);
            if (e instanceof LESString) {
                LESString lesString = (LESString) e;
                String lesOriginal = lesString.getLESString();
                EliminarEventoRequest eer = new Network.EliminarEventoRequest();
                eer.lesString = lesOriginal;
                client.sendTCP(eer);
            } else {
                System.err.println("error en la wea");
            }
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (!les_client.isIsConfiguracionReady()) {
            ActuadorInfo ainfo = (ActuadorInfo) cbo_actuador.getSelectedItem();
            String fijar = (String) jComboBox3.getSelectedItem();

            String les = "EN " + ainfo.getNombre()
                    + " NUMERO " + ainfo.getNumero()
                    + " FIJAR " + fijar;
            les_client.setLES(les);
            les_client.setIsConfiguracionReady(true);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    jEditorPane1.setText(LESCompUtils.getHtmlLESString(les_client.getLES()));
                    cbo_actuador.setEnabled(false);
                    jComboBox3.setEnabled(false);
                }
            });
            

        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        final String NBS = " ";
        if(les_client.isIsConfiguracionReady()){
            StringBuilder sb = new StringBuilder();
            sb.append(NBS);
            if (les_client.isIsExtendedFechaSetBeffore()) {
                sb.append("y");
            } else {
                les_client.setIsExtendedFechaSetBeffore(true);
            }
            sb.append(NBS);
            String fecha = jFormattedTextField1.getText().trim();
            String hora = jFormattedTextField2.getText().trim();
            if (!fecha.isEmpty()) {
                String lesAnterior = les_client.getLES();
                sb.append("PARA");
                sb.append(NBS);
                sb.append(fecha);
                if(!hora.isEmpty()){
                    sb.append("_");
                    sb.append(hora);
                }
                sb.append(NBS);
                String tiempo222 = "";
                if (les_client.isIsExtendedFechaSet()) {
                    int valor = Integer.parseInt(jFormattedTextField3.getText().trim());
                    String unidad = (String) jComboBox4.getSelectedItem();
                    tiempo222 += ("CADA " + valor + " " + unidad);
                }
                
                sb.append(tiempo222);
                sb.insert(0, lesAnterior);
                les_client.setLES(sb.toString());
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        jEditorPane1.setText(LESCompUtils.getHtmlLESString(les_client.getLES()));
                        jFormattedTextField1.setValue(null);
                        jFormattedTextField2.setValue(null);
                        jFormattedTextField3.setValue(null);
                        les_client.setIsExtendedFechaSet(false);
                        jFormattedTextField3.setEnabled(false);
                        jComboBox4.setEnabled(false);
                        jCheckBox1.getModel().setSelected(false);
                    }
                });
                les_client.setCorrectSentence(true);
            } else {
                sw(jDialog1.getRootPane(),"Fecha vacia",1);
            }
        } else {
            sw(jDialog1.getRootPane(),"Debe primero establecer configuracion",1);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if(evt.getStateChange()==ItemEvent.SELECTED ){
            les_client.setIsExtendedFechaSet(true);
            jFormattedTextField3.setEnabled(true);
            jComboBox4.setEnabled(true);
        }else{
            les_client.setIsExtendedFechaSet(false);
            jFormattedTextField3.setEnabled(false);
            jComboBox4.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        limpiarFormularioYRestablecerLES();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (!les_client.isCorrectSentence()) {
            sw(jDialog1.getRootPane(), "No ha formulado todabia un evento", 1);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    jDialog1.setVisible(false);

                }
            });
            jTextPane1.setText(LESCompUtils.getHtmlLESString(les_client.getLES()));
        }

    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        limpiarFormularioYRestablecerLES();
        jDialog1.setVisible(false);
        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if (les_client.isCorrectSentence()) {
            sw(rootPane, "wea correcta", 2);
            Network.CreacionEvento ce = new CreacionEvento();
            ce.LES = les_client.getLES();
            client.sendTCP(ce);
            jTextPane1.setText(null);

            les_client.resetAllValues();
            
        } else {
            sw(rootPane, "No ha creado un evento todabia", 1);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        limpiarFormularioYRestablecerLES();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jDialog1.setVisible(true);
                

            }
        });

    }//GEN-LAST:event_jButton9MouseClicked

private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

}//GEN-LAST:event_jMenu1ActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        new Thread("Conectar a servidor") {

            @Override
            public void run() {
                try {
                    if (pruebaLocal) {
                        client.connect(5000, "127.0.0.1", Network.getNetworkPort());
                    } else {
                        client.connect(5000, params.getIp_server(), Network.getNetworkPort());
                    }
                    jTextField2.setText("UP");
                    jTextField2.setForeground(new java.awt.Color(50, 255, 50));
                   // jButton1.setEnabled(false);
                    /*VALIDO ALTOKE ^^*/
                } catch (IOException ex) {
                    jTextField2.setText(ex.getMessage());
                    jTextField2.setForeground(new java.awt.Color(255, 0, 0));
                }
            }
        }.start();
}//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void sw(JComponent com,String mensaje,int tipo){
        String titulo;
        switch(tipo){
            case JOptionPane.ERROR_MESSAGE:
                titulo = "Error";
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                titulo = "Informacion";
                break;
            case JOptionPane.PLAIN_MESSAGE:
                titulo = "Debug";
                break;
            case JOptionPane.QUESTION_MESSAGE:
                titulo = "¿Pregunta?";
                break;
            default:
                titulo = "mensaje";

        }
        JOptionPane.showMessageDialog(com, mensaje, titulo, tipo);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_rele1_off;
    private javax.swing.JButton btn_rele1_on;
    private javax.swing.JButton btn_rele2_off;
    private javax.swing.JButton btn_rele2_on;
    private javax.swing.JButton btn_rele3_off;
    private javax.swing.JButton btn_rele3_on;
    private javax.swing.JButton btn_rele4_off;
    private javax.swing.JButton btn_rele4_on;
    private javax.swing.JButton btn_rele5_off;
    private javax.swing.JButton btn_rele5_on;
    private javax.swing.JButton btn_rele6_off;
    private javax.swing.JButton btn_rele6_on;
    private javax.swing.JButton btn_rele7_off;
    private javax.swing.JButton btn_rele7_on;
    private javax.swing.JButton btn_rele8_off;
    private javax.swing.JButton btn_rele8_on;
    private javax.swing.JComboBox cbo_actuador;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField txt_usuarios;
    // End of variables declaration//GEN-END:variables

    private void setButtonEnable(JButton boton, boolean estado) {
        boton.setEnabled(estado);
    }

    private void limpiarFormularioYRestablecerLES() {

        les_client.setIsConfiguracionReady(false);
        les_client.setIsExtendedFechaSet(false);
        les_client.setIsExtendedFechaSetBeffore(false);
        les_client.setCorrectSentence(false);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jEditorPane1.setText(null);
                setFormularioEnabled(cbo_actuador, true);
                setFormularioEnabled(jComboBox3, true);
                setFormularioEnabled(jFormattedTextField3, false);
                setFormularioEnabled(jComboBox4, false);
            }
        });

        jCheckBox1.getModel().setEnabled(true);


    }

    private void setFormularioEnabled(JComponent jcomp,boolean enabled){
        jcomp.setEnabled(enabled);
    }
    
}
