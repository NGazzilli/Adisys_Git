/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.configurazione;

import messaggistica.MainException;
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
public class ConfigurazioneTest {
    
    public ConfigurazioneTest() {
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
     * Test of checkConfigurazioneExists method, of class Configurazione.
     */
    @Test
    public void testCheckConfigurazioneExists() {
        System.out.println("checkConfigurazioneExists");
        boolean expResult = true;
        boolean result = Configurazione.checkConfigurazioneExists();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getDbDates method, of class Configurazione.
     */
    @Test
    public void testGetDbDates() throws MainException {
        System.out.println("getDbDates");
        Configurazione instance = new Configurazione();
        String[] expResult = {"jdbc:hsqldb:file:database/", "ADISysData", "asl", ""};
        String[] result = instance.getDbDates();
        assertNotNull(result);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getDati method, of class Configurazione.
     */
    @Test
    public void testGetDati() throws MainException {
        System.out.println("getDati");
        Configurazione instance = new Configurazione();
        ConfigurazioneTO result = instance.getDati();
        assertNotNull(result);
        String[] information = instance.getDbDates();
        assertEquals(result.getDbPath(), information[0]);
        assertEquals(result.getDbName(), information[1]);
        assertEquals(result.getDbUsername(), information[2]);
    }

    /**
     * Test of setAllDati method, of class Configurazione.
     */
    @Test
    public void testSetAllDati() throws Exception {
        System.out.println("setAllDati");
        ConfigurazioneTO to = new ConfigurazioneTO();
        Configurazione instance = new Configurazione();
        boolean expResult = true;
        boolean result = instance.setAllDati(to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail
    }

    /**
     * Test of createDatabaseEsistente method, of class Configurazione.
     */
    @Test
    public void testCreateDatabaseEsistente() throws MainException {
        System.out.println("createDatabaseEsistente");
        ConfigurazioneTO to = null;
        Configurazione instance = new Configurazione();
        to = instance.getDati();
        instance.createDatabaseEsistente(to);
    }
}