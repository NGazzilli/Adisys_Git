/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.intervento;

import business.patologia.PatologiaTO;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luca
 */
public class PatologieTipoInterventoTOTest {
    
    public PatologieTipoInterventoTOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getListaPatologieTipoIntervento method, of class PatologieTipoIntervento.
     */
    @Test
    public void testGetListaPatologieTipoIntervento() {
        System.out.println("getListaPatologieTipoIntervento");
        int pos = 0;
        PatologieTipoIntervento instance = new PatologieTipoIntervento();
        ArrayList<PatologiaTO> expResult = new ArrayList<>();
        expResult.add( new PatologiaTO("982315", "FARINGITE"));
        instance.setListaPatologieTipoIntervento(expResult);
        ArrayList result = instance.getListaPatologieTipoIntervento(pos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of rewriteListaPatologieTipoIntervento method, of class PatologieTipoIntervento.
     */
    @Test
    public void testRewriteListaPatologieTipoIntervento() {
        System.out.println("rewriteListaPatologieTipoIntervento");
        int pos = 0;
        PatologieTipoIntervento instance = new PatologieTipoIntervento();
        ArrayList<PatologiaTO> expResult = new ArrayList<>();
        expResult.add( new PatologiaTO("982315", "FARINGITE"));
        instance.setListaPatologieTipoIntervento(expResult);
        assertEquals(instance.getListaPatologieTipoIntervento(pos).get(0).getNome(), "FARINGITE");
        ArrayList<PatologiaTO> newList = new ArrayList<>();
        newList.add( new PatologiaTO("982315", "LARINGITE"));
        newList.add(new PatologiaTO("432675", "IPERTENSIONE"));
        instance.rewriteListaPatologieTipoIntervento(pos, newList);
        assertTrue(!instance.getListaPatologieTipoIntervento(pos).contains(new PatologiaTO("982315", "FARINGITE")));
        assertEquals(instance.getListaPatologieTipoIntervento(pos).get(0).getNome(), "LARINGITE");
        assertEquals(instance.getListaPatologieTipoIntervento(pos).get(1).getNome(), "IPERTENSIONE");
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setListaPatologieTipoIntervento method, of class PatologieTipoIntervento.
     */
    @Test
    public void testSetListaPatologieTipoIntervento() {
        System.out.println("setListaPatologieTipoIntervento");
        ArrayList<PatologiaTO> newList = new ArrayList<>();
        newList.add( new PatologiaTO("982315", "LARINGITE"));
        newList.add(new PatologiaTO("432675", "IPERTENSIONE"));
        PatologieTipoIntervento instance = new PatologieTipoIntervento();
        instance.setListaPatologieTipoIntervento(newList);
        assertEquals(instance.getListaPatologieTipoIntervento(0).get(0).getNome(), "LARINGITE");
        // TODO review the generated test code and remove the default call to fail.
     
    }

    /**
     * Test of removeListaPatologieTipoIntervento method, of class PatologieTipoIntervento.
     */
    @Test
    public void testRemoveListaPatologieTipoIntervento() {
        System.out.println("removeListaPatologieTipoIntervento");
        int riga = 0;
        PatologieTipoIntervento instance = new PatologieTipoIntervento();
        ArrayList<PatologiaTO> newList = new ArrayList<>();
        newList.add(new PatologiaTO("982315", "LARINGITE"));
        newList.add(new PatologiaTO("432675", "IPERTENSIONE"));
        instance.setListaPatologieTipoIntervento(newList);
        instance.removeListaPatologieTipoIntervento(riga);
        ArrayList<PatologiaTO> otherList = new ArrayList<>();
        otherList.add(new PatologiaTO("314321", "PARKINSON"));
        instance.setListaPatologieTipoIntervento(otherList);
        assertEquals(instance.getListaPatologieTipoIntervento(riga).get(riga).getNome(), "PARKINSON");
        assertTrue(instance.getListaInterventiPatologie()[0] == otherList);
        // TODO review the generated test code and remove the default call to fail.
       
    }

}