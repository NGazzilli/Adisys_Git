/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.journaling;

import business.intervento.InterventoCompletoTO;
import messaggistica.MainException;
import java.io.File;
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
public class JournalingTest {
    
    public JournalingTest() {
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
     * Test of getListaJournaling method, of class Journaling.
     */
    @Test
    public void testGetListaJournaling() throws MainException {
        System.out.println("getListaJournaling");
        Journaling instance = new Journaling();
        File dir = new File ("Importazione");
        int conteggio = dir.listFiles().length;
        String[] expResult = dir.list();
        String[] result = instance.getListaJournaling();
        assertEquals(conteggio, result.length);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getIntervento method, of class Journaling.
     */
    /*@Test
    public void testGetIntervento() {
        System.out.println("getIntervento");
        int idIntervento = 0;
        Journaling instance = new Journaling();
        InterventoCompletoTO expResult = null;
        InterventoCompletoTO result = instance.getIntervento(idIntervento);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getListaInterventi method, of class Journaling.
     */
    /*@Test
    public void testGetListaInterventi() {
        System.out.println("getListaInterventi");
        Journaling instance = new Journaling();
        ArrayList expResult = null;
        ArrayList result = instance.getListaInterventi();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of caricaFile method, of class Journaling.
     */
    @Test
    public void testCaricaFile() throws MainException {
        System.out.println("caricaFile");
        Journaling instance = new Journaling();
        File dir = new File ("Importazione");
        String[] listaFile = dir.list();
        String result = instance.caricaFile(listaFile[0]);
        assertTrue(result.contains("con successo"));
        assertTrue(result.contains("è valido"));
    }
}