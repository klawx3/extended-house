package cl.eh.util;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Interface {
    
    private String nombreDispositivo;
    private String nombreInterface;
    private boolean isActive;
    private List<Ip> ips;
    

    public Interface(String nombreDispositivo, String nombreInterface, List<InterfaceAddress> interface_addr_list) {
        this.isActive = false;
        this.nombreDispositivo = nombreDispositivo;
        this.nombreInterface = nombreInterface;
        if(!interface_addr_list.isEmpty()){
            this.isActive = true;
            ips = new ArrayList<Ip>();
            for (InterfaceAddress i_addr : interface_addr_list) {
                ips.add(new Ip(i_addr.getAddress().getAddress(),
                        i_addr.getNetworkPrefixLength()));
            }
        }
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public String getNombreInterface() {
        return nombreInterface;
    }
    
    public List<Ip> getIps(){
        return ips;
    }

    public boolean isActive(){
        return isActive;
    }
    
}
