/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.paziente;

import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.DateFormatConverter;
import messaggistica.MainException;
import integration.dao.PazienteMySqlDAO;
import javax.swing.DefaultListModel;
import javax.swing.table.AbstractTableModel;
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
public class PazienteTest {
    
    private String FORMATO_DATA_NASCITA = "dd/MM/yyyy";
    private String FORMATO_DATA_TABELLA = "yyyy-MM-dd";
    
    public PazienteTest() {
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
     * Test of createPaziente method, of class Paziente.
     */
    @Test
    public void testCreatePaziente() throws MainException {
        System.out.println("createPaziente");
        Paziente instance = new Paziente();
        boolean expResult = true;
        PazienteTO to = new PazienteTO("CARMELO", "ZAPPULLA", "1/12/1990", new Object[0]);
        assertNotNull("Paziente creato nullo", to);
        to.addCellulare("3496802773");
        to.addCellulare("0805615099");
        boolean resultCreate = instance.createPaziente(to);
        assertEquals(expResult, resultCreate);
        AbstractTableModel result = instance.getTabella();
        int row = result.getRowCount() - 1;
        int colID = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int colNome = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_NOME);
        int colCognome = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_COGNOME);
        int colData = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_DATA_NASCITA);
        int id = Integer.parseInt(result.getValueAt(row, colID).toString());
        String nome = result.getValueAt(row, colNome).toString();
        String cognome = result.getValueAt(row, colCognome).toString();
        String data = result.getValueAt(row, colData).toString();
        String formatData = DateFormatConverter.cambiaFormato(data ,FORMATO_DATA_TABELLA, 
                FORMATO_DATA_NASCITA);
        
        PazienteTO expPaz = new PazienteTO(id, nome, cognome, formatData, new Object[0]);
        int idPaz = expPaz.getID();
        to.setID(idPaz);
        assertTrue(result != null);
        assertEquals(to.getID(), expPaz.getID());
        assertEquals(to.getNome(), expPaz.getNome());
        assertEquals(to.getCognome(), expPaz.getCognome());
        assertEquals(to.getDataNascita(FORMATO_DATA_TABELLA), expPaz.getDataNascita(FORMATO_DATA_TABELLA));
        
        DefaultListModel resultData = instance.getCellulari(idPaz);
        assertTrue(resultData.contains("3496802773"));
        assertTrue(resultData.contains("0805615099"));
        assertTrue(!resultData.contains("333333"));
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deletePaziente method, of class Paziente.
     */
    @Test
    public void testDeletePaziente() throws MainException {
        
        System.out.println("deletePaziente");
        Paziente instance = new Paziente();
        PazienteTO to = new PazienteTO("GIANNI", "CIARDO", "1/12/1990", new Object[0]);
        assertNotNull("Paziente creato nullo", to);
        AbstractTableModel result = instance.getTabella();
        assertNotNull(result);
        int row = result.getRowCount() - 1;
        int colID = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(result.getValueAt(row, colID).toString());
        PazienteTO lastInf = new PazienteTO();
        lastInf.setID(id);
        boolean expResult = true;
        boolean resultTest = instance.deletePaziente(lastInf);
        assertEquals(expResult, resultTest);
    }

    /**
     * Test of modificaPaziente method, of class Paziente.
     */
    @Test
    public void testModificaPaziente() throws MainException {
        System.out.println("modificaPaziente");
        Paziente instance = new Paziente();
        PazienteTO to = new PazienteTO("JAVIER", "ZANETTI", "1/12/1990", new Object[0]);
        assertNotNull("Paziente creato nullo", to);
        instance.createPaziente(to);
        AbstractTableModel result = instance.getTabella();
        assertNotNull(result);
        int row = result.getRowCount() - 1;
        int colID = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int colData = ((ADISysTableModel) result).findColumn(PazienteMySqlDAO.NOME_COLONNA_DATA_NASCITA);
        int id = Integer.parseInt(result.getValueAt(row, colID).toString());
        String data =  result.getValueAt(row, colData).toString();
        PazienteTO lastPaz = new PazienteTO();
        lastPaz.setID(id);
        lastPaz.setNome("ANTONIO");
        lastPaz.setCognome("CASSANO");
        lastPaz.setDataNascita(data, FORMATO_DATA_TABELLA);
        boolean expResult = true;
        boolean resultData = instance.modificaPaziente(lastPaz);
        assertEquals(expResult, resultData);    
        boolean updateInf = instance.exists(lastPaz.getNome(), lastPaz.getCognome(), lastPaz.getCellulari());
        assertEquals(expResult, updateInf);
    }
    

    /**
     * Test of reset method, of class Paziente.
     */
    @Test
    public void testReset() throws MainException {
        System.out.println("reset");
        Paziente instance = new Paziente();
        boolean expResult = true;
        boolean result = instance.reset();
        assertEquals(expResult, result);
        assertEquals(instance.getTabella().getRowCount(), 0);
        // TODO review the generated test code and remove the default call to fail.*/
    }



    /**
     * Test of alterTable method, of class Paziente.
     */
    /*@Test
    public void testAlterTable() {
        System.out.println("alterTable");
        ArrayList<String> listaCampi = null;
        Paziente instance = new Paziente();
        instance.alterTable(listaCampi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of alterTableCellulari method, of class Paziente.
     */
    /*@Test
    public void testAlterTableCellulari() {
        System.out.println("alterTableCellulari");
        ArrayList<String> listaCampi = null;
        Paziente instance = new Paziente();
        instance.alterTableCellulari(listaCampi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}