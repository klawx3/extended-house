/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.persistence;

import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Usuario
 */
public class PercistenceTest {
    public static EntityManagerFactory emf;
    public static EntityManager em;
    public PercistenceTest(){

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        emf = Persistence.createEntityManagerFactory("TallerDeProyectoInformatico_EH_SERVERPU");
//        em = emf.createEntityManager();
////        
////        em.getTransaction().begin();
//        
////        Rol rol = new Rol();
////        rol.setId(null);
////        rol.setNombre("SUPERADMIN");
//        List abc = em.createNamedQuery("Rol.findAll").getResultList();
//        Iterator it = abc.iterator();
//        while(it.hasNext()){
//            Rol asd = (Rol)it.next();
//            System.out.println(asd.getId());
//            System.out.println(asd.getNombre());
//        }
////        em.persist(rol);
////        em.getTransaction().commit();
//        
//        em.close();
//        emf.close();
//        

        
                
    }
}
