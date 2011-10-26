/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.client;

/**
 *
 * @author Administrador
 */
public class Parametros {
    public static final String PARAM_SERVERIP = "server_ip";
    public static final String PARAM_CLIENTIP = "client_ip";
    public static final String PARAM_USER = "user";
    
    private String ip_server;
    private String ip_cliente;
    private String user;

    public Parametros(String ip_server, String ip_cliente, String user) {
        this.ip_server = ip_server;
        this.ip_cliente = ip_cliente;
        this.user = user;
    }
    
     public String getIp_cliente() {
        return ip_cliente;
    }

    public String getIp_server() {
        return ip_server;
    }

    public String getUser() {
        return user;
    }
    
}
