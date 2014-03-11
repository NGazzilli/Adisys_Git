/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.infermiere;

import adisys.server.strumenti.ADISysTableModel;
import messaggistica.MainException;
import integration.dao.InfermiereMySqlDAO;
import java.util.ArrayList;
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
public class InfermiereTest {
    
    public InfermiereTest() {
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
     * Test of createInfermiere method, of class Infermiere.
     */
    @Test
    public void testCreateInfermiere() throws MainException {
        System.out.println("createInfermiere");
        Infermiere instance = new Infermiere();
        boolean expResult = true;
        InfermiereTO to = new InfermiereTO("NINO", "D'ANGELO");
        assertNotNull("Infermiere creato nullo", to);
        boolean result = instance.createInfermiere(to);
        assertEquals(expResult, result);
        ArrayList<InfermiereTO> listaInfermieri = instance.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        to.setID(idInf);
        result = instance.exists(idInf);
        assertEquals(result, true);
        InfermiereTO insertInf = instance.getInfermiere(idInf);
        assertEquals(to.getID(), insertInf.getID());
        assertEquals(to.getNome(), insertInf.getNome());
        assertEquals(to.getCognome(), insertInf.getCognome());
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTabella method, of class Infermiere.
     */
    @Test
    public void testGetTabella() throws MainException {
        System.out.println("getTabella");
        Infermiere instance = new Infermiere();
        AbstractTableModel result = instance.getTabella();
        assertNotNull(result);
        int row = result.getRowCount() - 1;
        int colID = ((ADISysTableModel) result).findColumn(InfermiereMySqlDAO.NOME_COLONNA_ID);
        int colNome = ((ADISysTableModel) result).findColumn(InfermiereMySqlDAO.NOME_COLONNA_NOME);
        int colCognome = ((ADISysTableModel) result).findColumn(InfermiereMySqlDAO.NOME_COLONNA_COGNOME);
        int id = Integer.parseInt(result.getValueAt(row, colID).toString());
        String nome = result.getValueAt(row, colNome).toString();
        String cognome = result.getValueAt(row, colCognome).toString();
        InfermiereTO lastInf = new InfermiereTO(id, nome, cognome);
        InfermiereTO getInf = instance.getInfermiere(id);
        assertEquals(lastInf.getID(), getInf.getID());
        assertEquals(lastInf.getNome(), getInf.getNome());
        assertEquals(lastInf.getCognome(), getInf.getCognome());
        // TODO review the generated test code and remove the default call to fail.
    }
  
    /**
     * Test of deleteInfermiere method, of class Infermiere.
     */
    @Test
    public void testDeleteInfermiere() throws MainException {
        System.out.println("deleteInfermiere");
        Infermiere instance = new Infermiere();
        boolean expResult = true;
        InfermiereTO to = new InfermiereTO("VASCO", "ROSSI");
        assertNotNull("Infermiere creato nullo", to);
        boolean result = instance.createInfermiere(to);
        assertEquals(expResult, result);
        ArrayList<InfermiereTO> listaInfermieri = instance.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        InfermiereTO infTO = instance.getInfermiere(idInf);
        infTO.setID(idInf);
        result = instance.deleteInfermiere(infTO);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

      /**
     * Test of modificaInfermiere method, of class Infermiere.
     */
    @Test
    public void testModificaInfermiere() throws MainException {
        System.out.println("modificaInfermiere");
        Infermiere instance = new Infermiere();
        boolean expResult = true;
        InfermiereTO to = new InfermiereTO("VASCO", "ROSSI");
        assertNotNull("Infermiere creato nullo", to);
        boolean result = instance.createInfermiere(to);
        assertEquals(expResult, result);
        ArrayList<InfermiereTO> listaInfermieri = instance.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        InfermiereTO infTO = instance.getInfermiere(idInf);
        infTO.setID(idInf);
        infTO.setNome("LUCIANO");
        infTO.setCognome("LIGABUE");
        expResult = true;
        result = instance.modificaInfermiere(infTO);
        assertEquals(expResult, result);    
        InfermiereTO updateInf = instance.getInfermiere(idInf);
        assertEquals(infTO.getNome(), updateInf.getNome());
        assertEquals(infTO.getCognome(), updateInf.getCognome());
    }
    
    /**
     * Test of reset method, of class Infermiere.
     */
    @Test
    public void testReset() throws MainException {
        System.out.println("reset");
        Infermiere instance = new Infermiere();
        boolean expResult = true;
        boolean result = instance.reset();
        assertEquals(expResult, result);
        assertEquals(instance.getTabella().getRowCount(), 0);
        // TODO review the generated test code and remove the default call to fail.*/
    }
    
   /**
     * Test of exists method, of class Infermiere.
     */
    @Test
    public void testExists() throws MainException {
        System.out.println("exists");
        Infermiere instance = new Infermiere();
        boolean expResult = true;
        InfermiereTO to = new InfermiereTO("DIEGO", "MILITO");
        assertNotNull("Infermiere creato nullo", to);
        boolean result = instance.createInfermiere(to);
        assertEquals(expResult, result);
        ArrayList<InfermiereTO> listaInfermieri = instance.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        result = instance.exists(idInf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

}