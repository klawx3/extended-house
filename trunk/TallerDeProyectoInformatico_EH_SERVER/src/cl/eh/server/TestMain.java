/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.server;

import cl.eh.db.QueryGenerator;
import cl.eh.db.model.Actuador;
import cl.eh.db.model.Historial;
import java.util.Calendar;

/**
 *
 * @author Usuario
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Actuador ac = new Actuador();
        ac.setId(0);
        ac.setNombre("RL");
        ac.setNumero(0);
        Historial his =
                new Historial(0,ac,null,Calendar.getInstance(),"200.23","hola");
        System.out.println(QueryGenerator.getQuery(his));
    }
}
