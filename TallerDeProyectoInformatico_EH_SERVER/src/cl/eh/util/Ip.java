/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

/**
 *
 * @author Usuario
 */
public class Ip {

    private short[] ip;
    private short mask;
    private short[] network;

    public Ip(byte[] ip, short mask) {
        setIp(getIp(ip), mask, getNetwork(ip, mask));
    }
    
    public static int compareNetwork(byte[] ip,short mask,byte[] other_ip,short other_mask){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setIp(short[] ip, short mask, short[] network) {
        this.ip = ip;
        this.mask = mask;
        this.network = network;
    }

    private static short[] getNetwork(byte[] ip, short mask) {
        short[] aux = new short[ip.length];
        short[] typeofnet = getTypeOfNetwork(mask);
        if (typeofnet != null) { // Significa que es clase A, B o C
            for (int i = 0; i < ip.length; i++) {
                int guarismo = ((ip[i] < 0) ? Math.abs(ip[i] + 256) : ip[i]);
                if (typeofnet[i] == 255) {
                    aux[i] = (short) guarismo;
                } else {
                    aux[i] = 0;
                }
            }
        } else {
            throw new UnsupportedOperationException("Clase de ip desconosida");
        }
        return aux;
    }

    private short[] getIp(byte[] ip) {
        short[] aux = new short[ip.length];
        for (int i = 0; i < ip.length; i++) {
            int guarismo = ((ip[i] < 0) ? Math.abs(ip[i] + 256) : ip[i]);
            aux[i] = (short) guarismo;
        }
        return aux;
    }

    private static short[] getTypeOfNetwork(short mask) {
        switch (mask) {
            case 8:  // Clase A
                return new short[]{255, 0, 0, 0};
            case 16: // Clase B
                return new short[]{255, 255, 0, 0};
            case 24: // Clase C
                return new short[]{255, 255, 255, 0};
            default:
                return null;
        }
    }
        
    public String getIpString(){
        String _ip = "";
        if (ip.length != 0) {
            for (int i = 0; i < this.ip.length; i++) {
                _ip = _ip + ip[i] + ((i == 3) ? "" : ".");
            }
        }
        return _ip;
    }
    
    public String getNetworkString(){
        String _ip = "";
        if (network.length != 0) {
            for (int i = 0; i < this.network.length; i++) {
                _ip = _ip + network[i] + ((i == 3) ? "" : ".");
            }
        }
        return _ip;
    }
    
    public short[] getIp() {
        return ip;
    }

    public short getMask() {
        return mask;
    }

    public short[] getNetwork() {
        return network;
    }
}
