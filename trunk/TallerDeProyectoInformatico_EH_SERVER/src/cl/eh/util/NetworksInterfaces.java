/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

import java.lang.String;
import java.util.Enumeration;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.out;

/**
 *
 * @author Usuario
 */
public class NetworksInterfaces {

    private Enumeration<NetworkInterface> nets;
    private List<Interface> interfaces;

    public NetworksInterfaces() {
        interfaces = new ArrayList<Interface>();
        try {
            nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface net : Collections.list(nets)) {
                Interface in_face_new = new Interface(net.getDisplayName(),
                        net.getName().replaceAll("interface:", ""),
                        net.getInterfaceAddresses());
                interfaces.add(in_face_new);
            }
        } catch (SocketException ex) {
            Logger.getLogger(NetworksInterfaces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }
}
