/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

import cl.eh.eventos.model.EventoListener;
import cl.eh.properties.Guardable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lenguaje de Evento Simple
 * @author Usuario
 */
public class LESAdministador implements Guardable{
    private EventoListener show;
    private ExecutorService exe;
    private List<HiloDeEventoLES> eventos;
    private int sizeThreadPool;
    private int currentsThreads;
    
    public LESAdministador(int sizeThreadPool){
        show = null;
        exe = Executors.newFixedThreadPool(sizeThreadPool);
        currentsThreads = 0;
        eventos = new ArrayList<HiloDeEventoLES>();
    }
    
    public void addEventoSimpleString(String eventoString) throws Exception{
        if(eventos.size() < sizeThreadPool){
            HiloDeEventoLES aux = new HiloDeEventoLES(eventoString,show);
            eventos.add(aux);
            exe.execute(aux);
        }else{
            throw new Exception("Maximo de eventos permitidos exedido");
        }
        
    }
    
    public void addEventoListener(EventoListener l){
        show = l;
    }
    
    public void save() {
        exe.shutdown();
    }

    
    

//    public static ScheduleInfo getScheduleInfo(String ssl) throws Exception{
//        TreeMap<Integer,String> tm = new TreeMap<Integer,String>();
//        StringTokenizer token = new StringTokenizer(ssl);
//        int cont = 0;
//        while(token.hasMoreTokens()){
//            String palabla = token.nextToken();
//            tm.put(cont, palabla);
//            cont++;
//        }
//        analisis(tm);
//        
//        return null;
//    }
//
//    private static void analisis(TreeMap<Integer, String> tm) {
//        for (Map.Entry<Integer, String> entry : tm.entrySet()) {
//            String value = entry.getValue();
//            Integer key = entry.getKey();
//            System.out.printf("%d\t\t%s\n",key,value);
//        }
//    }

        
        
        //        Set<Integer> claves = tm.keySet();
        //        TreeSet<Integer> clavesOrdenadas = new TreeSet<Integer>(claves);
        //        for(Integer clave:clavesOrdenadas){
        //            String valor = tm.get(clave);
        //            System.out.printf("%d\t\t%s\n",clave,valor);
        //        }




    }

