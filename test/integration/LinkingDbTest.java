/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import adisys.server.strumenti.ADISysTableModel;
import business.configurazione.ConfigurazioneTO;
import messaggistica.MainException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
public class LinkingDbTest {

    public LinkingDbTest() {
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
     * Test of connect method, of class LinkingDb.
     */
    @Test
    public void testConnect() throws MainException {
        System.out.println("connect");
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        Connection result = instance.connect();
        assertNotNull(result);
        boolean expResult = true;
        assertEquals(instance.isConnected(), expResult);
    }

    /**
     * Test of esegui method, of class LinkingDb.
     */
    @Test
    public void testEsegui() throws MainException {
        System.out.println("esegui");
        String SQLString = "insert into infermieri (nome, cognome) values ('Luca', 'Massa')";
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        instance.connect();
        boolean expResult = true;
        boolean result = instance.esegui(SQLString);
        assertEquals(expResult, result);
    }

    /**
     * Test of update method, of class LinkingDb.
     */
    @Test
    public void testUpdate() throws MainException, SQLException {
        System.out.println("update");
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        instance.connect();
        PreparedStatement stmt = instance.prepareStatement(
                "insert into infermieri (nome, cognome) values ('luca', 'massa')");
        boolean expResult = true;
        boolean result = instance.update(stmt);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTabella method, of class LinkingDb.
     */
    @Test
     public void testGetTabella_PreparedStatement() throws MainException, SQLException {
        System.out.println("getTabella");
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        instance.connect();
        PreparedStatement stmt = instance.prepareStatement(
                "select * from infermieri");
        ADISysTableModel result = instance.getTabella(stmt);
        assertNotNull(result);
     }

    
    /**
     * Test of getTabella method, of class LinkingDb.
     */
    @Test
     public void testGetTabella_String() throws MainException, SQLException {
        System.out.println("getTabella");
        String queryText = "select * from infermieri";
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        instance.connect();
        ADISysTableModel result = instance.getTabella(queryText);
        assertNotNull(result);
        queryText = "select * from infermieri where id = -1";
        result = instance.getTabella(queryText);
        assertTrue(result.getRowCount() == 0);
     }
    /**
     * Test of getResultSet method, of class LinkingDb.
     */
    @Test
    public void testGetResultSet() throws MainException {
        System.out.println("getResultSet");
        String queryText = "select * from infermieri";
        LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
        instance.connect();
        ResultSet result = instance.getResultSet(queryText);
        assertNotNull(result);
     // TODO review the generated test code and remove the default call to fail.
     }

    /**
     * Test of disconnect method, of class LinkingDb.
     */
    @Test
     public void testDisconnect() throws MainException {
     System.out.println("disconnect");
     LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
     instance.connect();
     boolean expResult = true;
     boolean result = instance.disconnect();
     assertEquals(expResult, result);
     result = instance.isConnected();
     assertEquals(result, false);
     // TODO review the generated test code and remove the default call to fail.
     }
    /**
     * Test of isConnected method, of class LinkingDb.
     */
    @Test
     public void testIsConnected() throws MainException {
     System.out.println("isConnected");
     LinkingDb instance = new LinkingDb(new ConfigurazioneTO("jdbc:hsqldb:file:database/",
                "ADISysData", "asl", ""));
     instance.connect();
     boolean expResult = true;
     boolean result = instance.isConnected();
     assertEquals(expResult, result);
     instance.disconnect();
     result = instance.isConnected();
     assertEquals(false, result);

     }

}