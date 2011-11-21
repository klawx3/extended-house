/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Usuario
 */
public class NewClass {
    public NewClass() throws IOException{
        BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder cadenaFinal = new StringBuilder();
        
        String fec = leer.readLine(); // tipo de ingreso fecha = ddmmYY 
        int dia = Integer.parseInt(fec.substring(0, 2));
        int mes = Integer.parseInt(fec.substring(2, 4));
        int año = Integer.parseInt(fec.substring(4, 6));
        
        cadenaFinal.append(dia);
        cadenaFinal.append(" ");
        switch(mes){
            case 1:
                cadenaFinal.append("Enero");
                break;
            case 2:
                cadenaFinal.append("Febrero");
                break;
            //case 3... etc....       
        }
        //luego faltaria adjuntar el año... y al final...
        
        
        String fecha = cadenaFinal.toString();
        // y muestro la fecha con out..
    
    }
}
