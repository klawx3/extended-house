/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.arduino;

import cl.eh.common.ArduinoSignal;
import cl.eh.exceptions.RelayException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ReleeShield implements ArduinoSignal {

    public static final String DISP_NAME = "RL";
    public static final int MAX_RELES = 8;
    private int[] rele_s;
    private boolean[] estado_rele_s;
    private SerialArduino s_a;

    public ReleeShield(SerialArduino s_a) {
        this.s_a = s_a;
        rele_s = new int[MAX_RELES];
        estado_rele_s = new boolean[MAX_RELES];
        for (int i = 0; i < estado_rele_s.length; i++) {
            estado_rele_s[i] = false;
        }
    }

    public boolean isReleePowerOn(int rele) {
        return estado_rele_s[rele];
    }

    public void powerOnRelee(int rele) {
        if (rele >= 0 && rele <= 7) {
            if (estado_rele_s[rele] == false) {
                synchronized (s_a) {
                    s_a.enviarSeñal(RELEE_SIGNAL);
                    s_a.enviarSeñal((rele == 0) ? 48 : rele);
                }
                estado_rele_s[rele] = true;
                System.err.println("Rele int:" + rele + "||||Rele char:" + (char) rele);
            }
        } else {
            try {
                throw new RelayException("Numero (" + rele + ") fuera de rango");
            } catch (RelayException ex) {
                Logger.getLogger(ReleeShield.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void powerOffRelee(int rele) {
        if (rele >= 0 && rele <= 7) {
            if (estado_rele_s[rele] == true) {
                synchronized (s_a) {
                    s_a.enviarSeñal(RELEE_SIGNAL);
                    s_a.enviarSeñal((rele == 0) ? 48 : rele);
                }
                estado_rele_s[rele] = false;
                System.err.println("Rele int:" + rele + "||||Rele char:" + (char) rele);
            }
        } else {
            try {
                throw new RelayException("Numero (" + rele + ") fuera de rango");
            } catch (RelayException ex) {
                Logger.getLogger(ReleeShield.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
