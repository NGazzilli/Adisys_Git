/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.patologia;

import adisys.server.strumenti.ADISysTableModel;
import business.TO;
import messaggistica.MainException;
import integration.dao.PatologiaMySqlDAO;
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
public class PatologiaTest {
    
    public PatologiaTest() {
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
     * Test of createPatologia method, of class Patologia.
     */
    @Test
    public void testCreatePatologia() throws MainException {
        System.out.println("createPatologia");
        Patologia instance = new Patologia();
        boolean expResult = true;
        PatologiaTO to = new PatologiaTO("054623", "TIROIDE");
        assertNotNull("Patologia creata nulla", to);
        boolean resultData = instance.createPatologia(to);
        assertEquals(expResult, resultData);
        AbstractTableModel result = instance.getTabella();
        int row = result.getRowCount() - 1;
        int colID = ((ADISysTableModel) result).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int colCodice = ((ADISysTableModel) result).findColumn(PatologiaMySqlDAO.NOME_COLONNA_CODICE);
        int colNome = ((ADISysTableModel) result).findColumn(PatologiaMySqlDAO.NOME_COLONNA_NOME);
        int id = Integer.parseInt(result.getValueAt(row, colID).toString());
        String codice = result.getValueAt(row, colCodice).toString();
        String nome = result.getValueAt(row, colNome).toString();
        PatologiaTO expPaz = new PatologiaTO(id, codice, nome);
        int idPaz = expPaz.getID();
        to.setID(idPaz);
        assertTrue(result != null);
        assertEquals(to.getID(), expPaz.getID());
        assertEquals(to.getCodice(), expPaz.getCodice());
        assertEquals(to.getNome(), expPaz.getNome());
    }

    /**
     * Test of deletePatologia method, of class Patologia.
     */
    @Test
    public void testDeletePatologia() throws MainException {
        System.out.println("deletePatologia");
        Patologia instance = new Patologia();
        boolean expResult = true;
        PatologiaTO to = new PatologiaTO("062314", "APPENDICITE");
        assertNotNull("Patologia creato nullo", to);
        boolean result = instance.createPatologia(to);
        assertEquals(expResult, result);
        ArrayList<TO> listaPatologie = instance.getArrayPatologie();
        PatologiaTO expPat = (PatologiaTO) listaPatologie.get(listaPatologie.size() - 1);
        int idPat = expPat.getID();
        PatologiaTO delPat = new PatologiaTO();
        delPat.setID(idPat);
        result = instance.deletePatologia(delPat);
        assertEquals(expResult, result);
    }

    /**
     * Test of modificaPatologia method, of class Patologia.
     */
    @Test
    public void testModificaPatologia() throws MainException {
        System.out.println("modificaPatologia");
        Patologia instance = new Patologia();
        boolean expResult = true;
        PatologiaTO to = new PatologiaTO("103925", "DIABETE");
        assertNotNull("Patologia creato nullo", to);
        boolean result = instance.createPatologia(to);
        assertEquals(expResult, result);
        ArrayList<TO> listaPatologie = instance.getArrayPatologie();
        PatologiaTO expPat = (PatologiaTO) listaPatologie.get(listaPatologie.size() - 1);
        int idPat = expPat.getID();
        PatologiaTO patTO = new PatologiaTO();
        patTO.setID(idPat);
        patTO.setCodice("093146");
        patTO.setNome("MIOPIA");
        expResult = true;
        result = instance.modificaPatologia(patTO);
        assertEquals(result, expResult);
    }

}