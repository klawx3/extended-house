/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

/**
 *
 * @author Usuario
 */
public class ReleeShield {
    public static final int MAX_RELES = 8;
    private int[] rele_s;
    private boolean[] estado_rele_s;
    private SerialArduino s_a;
    
    public ReleeShield(SerialArduino s_a){
        this.s_a = s_a;
        rele_s = new int[MAX_RELES];
        estado_rele_s = new boolean[MAX_RELES];
    }
    
    public boolean isReleePowerOn(int rele){
        assert (rele >= 0 || rele <=7);
        return estado_rele_s[rele];
    }
    
    public void powerOnRelee(int rele){
        assert (rele >= 0 || rele <=7);
        if(estado_rele_s[rele] == false){
            s_a.enviarSeñal((char)'r');
            s_a.enviarSeñal(rele);
        }
    }
    
    public void powerOfRelee(int rele){
        assert (rele >= 0 || rele <=7);
        if(estado_rele_s[rele] == true){
            s_a.enviarSeñal((char)'r');
            s_a.enviarSeñal(rele);
        }
    }
    
    
}
