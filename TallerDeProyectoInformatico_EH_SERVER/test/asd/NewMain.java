/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asd;

import cl.eh.eventos.HiloDeEventoLES;
import cl.eh.eventos.model.EventoEvent;
import cl.eh.eventos.model.EventoListener;
import cl.eh.eventos.LESAdministador;
import cl.eh.db.ConexionExtendedHouse;
import cl.eh.exceptions.LESException;
import com.sun.org.apache.xpath.internal.operations.Equals;
import java.util.*;
import static java.lang.System.*;
/**
 *
 * @author Usuario
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LESException {
        //tring ip_db, String nom_db, String user_db, String pass_db
        ConexionExtendedHouse con = new ConexionExtendedHouse("localhost","ih_db","root","piramid");
        LESAdministador lesAdm = new LESAdministador(con);
        lesAdm.addEventoListener(new EventoListener() {

            public void eventoPerformed(EventoEvent e) {
                out.println(e.getAccion());
                out.println(e.getActuador());
                out.println(e.getNumero_actuador());
            }
        });
        String lesString = "EN rl NUMERO 6 FIJAR encendido "
                + "PARA 12-12-2012";
//        lesAdm.addEventoSimpleString(lesString);
        Scanner leer = new Scanner(System.in);
        String entrada;
        int i = 200;
        while (!(entrada = leer.next()).equals("exit")) {
            out.println(entrada);
            i++;
            if (entrada.equals("eliminar")) {
                lesAdm.removeEventoSiemple("EN rl NUMERO 6 FIJAR encendido "
                        + "PARA 12-12-2012");
            } else if (entrada.equals("listar")) {
                synchronized (lesAdm.getEventInExecutionList()) {
                    for (HiloDeEventoLES asd : lesAdm.getEventInExecutionList()) {
                        out.println(asd.getEventoString());

                    }
                }
            }else if(entrada.equals("insert")){
                lesAdm.addEventoSimpleString("EN rl NUMERO "+i+" FIJAR encendido "
                        + "PARA 12-12-2012");
            }
        }

        

    }
}
