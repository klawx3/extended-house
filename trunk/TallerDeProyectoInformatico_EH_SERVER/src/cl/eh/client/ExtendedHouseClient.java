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
        params = new Parametros(
                super.getParameter(Parametros.PARAM_SERVERIP),
                super.getParameter(Parametros.PARAM_CLIENTIP),
                super.getParameter(Parametros.PARAM_USER));
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
                    jTextField5.setEnabled(false);
                    jComboBox4.setEnabled(false);
                    
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
                    vc.user = "weon";
                    vc.client_ip = "127.0.0.1";
                }

                client.sendTCP(vc); //validacion de connecion
                client.sendTCP(new ServerStatusRequest()); //peticion del estado del servidor

            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("El servidor se ha caido:");
                jTextField2.setText("DOWN");
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
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jButton10 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel31 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
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
        jTextField4 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel26 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

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

        cbo_actuador.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        cbo_actuador.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel14.add(cbo_actuador, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 170, -1));

        jLabel28.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel28.setText("Estado");
        jPanel14.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jComboBox3.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Encendido", "Apagado", "Cambio" }));
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

        jLabel32.setText("(");
        jDialog1.getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel33.setText(")");
        jDialog1.getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 10, -1));

        jLabel34.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jLabel34.setText("Ejecutar Cada:");
        jDialog1.getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 10));

        jTextField5.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jDialog1.getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 60, 20));

        jComboBox4.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "millisegundos", "horas", "minutos", "segundos" }));
        jDialog1.getContentPane().add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 100, 20));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/add.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 50, 50));

        jLabel35.setText("String resultante:");
        jDialog1.getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 130, -1));

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/alarm.png"))); // NOI18N
        jButton11.setText("Agregar Evento");
        jDialog1.getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 360, 30));

        jEditorPane1.setContentType("text/html");
        jScrollPane4.setViewportView(jEditorPane1);

        jDialog1.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 400, 80));

        jLabel31.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N
        jLabel31.setText("Fecha:");
        jDialog1.getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/dd/yyyy"))));
        jDialog1.getContentPane().add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 80, 20));

        jLabel30.setFont(new java.awt.Font("Verdana", 0, 9));
        jLabel30.setText("Hora:");
        jDialog1.getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance())));
        jDialog1.getContentPane().add(jFormattedTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 80, 20));

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
        jDialog1.getContentPane().add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, -1, -1));

        setForeground(new java.awt.Color(255, 255, 255));

        jToolBar1.setFloatable(false);

        jButton1.setFont(new java.awt.Font("Verdana", 1, 11));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cl/eh/images/lightning.png"))); // NOI18N
        jButton1.setText("Conectar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator1);

        jLabel1.setText("Server status:");
        jToolBar1.add(jLabel1);

        jTextField2.setEditable(false);
        jToolBar1.add(jTextField2);
        jToolBar1.add(jSeparator2);

        jLabel27.setText("Usuarios Conectados:");
        jToolBar1.add(jLabel27);

        txt_usuarios.setEditable(false);
        jToolBar1.add(txt_usuarios);

        jTabbedPane1.setFont(new java.awt.Font("Verdana", 0, 11));

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
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
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
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addContainerGap(185, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 210, 250));

        jTabbedPane1.addTab("Gestion de Respaldos", jPanel11);

        jTable1.setFont(new java.awt.Font("Verdana", 0, 11));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "LES", "User"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jButton3.setText("Obtener Información");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton7.setText("enviar LES");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Eliminar Evento");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Crear LES");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(160, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jButton9)
                .addContainerGap(449, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addGap(18, 18, 18)
                .addComponent(jButton9)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion de eventos", jPanel3);

        jToolBar2.setFloatable(false);

        jLabel26.setText("Server Info:");
        jToolBar2.add(jLabel26);

        jTextField3.setEditable(false);
        jToolBar2.add(jTextField3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

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
                    jTextField2.setForeground(new java.awt.Color(0, 255, 0));
                    jButton1.setEnabled(false);
                    /*VALIDO ALTOKE ^^*/
                } catch (IOException ex) {
                    jTextField2.setText(ex.getMessage());
                    jTextField2.setForeground(new java.awt.Color(255, 0, 0));
                }
            }
        }.start();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            System.out.println(Fecha2.getHora(sr.getFecha_res(), '-'));
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
        ce.LES = jTextField4.getText();
        client.sendTCP(ce);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if(selectedRow != -1){
                    System.err.println("row;"+selectedRow);
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
        jDialog1.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
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
            }
        });
        cbo_actuador.setEnabled(false);
        jComboBox3.setEnabled(false);

        
        System.out.println(ainfo.getNumero());
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if(les_client.isIsConfiguracionReady()){
            String tiempo1 = jFormattedTextField1.getText();
            if(!tiempo1.isEmpty()){
                
            }else{
                //error
            }
        }else{
            //error
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if(evt.getStateChange()==ItemEvent.SELECTED ){
            les_client.setIsExtendedFechaSet(true);
            jTextField5.setEnabled(true);
            jComboBox4.setEnabled(true);
        }else{
            les_client.setIsExtendedFechaSet(false);
            jTextField5.setEnabled(false);
            jComboBox4.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
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
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField txt_usuarios;
    // End of variables declaration//GEN-END:variables

    private void setButtonEnable(JButton boton, boolean estado) {
        boton.setEnabled(estado);
    }    
}
