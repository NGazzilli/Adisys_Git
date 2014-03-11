/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.intervento;

import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.DateFormatConverter;
import business.infermiere.Infermiere;
import business.infermiere.InfermiereTO;
import business.patologia.Patologia;
import business.patologia.PatologiaTO;
import business.paziente.Paziente;
import business.paziente.PazienteTO;
import messaggistica.MainException;
import integration.dao.InterventoMySqlDAO;
import integration.dao.PatologiaMySqlDAO;
import integration.dao.PazienteMySqlDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
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
public class InterventoTest {
    
    private String FORMATO_DATA_INSERT = "dd/MM/yyyy";
    private String FORMATO_DATA_TABELLA = "yyyy-MM-dd";
    private String FORMATO_ORA_INSERT = "HH.mm";
    private String FORMATO_ORA_TABELLA = "HH:mm:ss";
    
    public InterventoTest() {
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
     * Test of createIntervento method, of class Intervento.
     */
    @Test
    public void testCreateIntervento() throws MainException {
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("GIORGIO", "CLONE");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("DOMENICO", "PANZETTA", "1/12/1948", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        
        System.out.println("createIntervento");
        Intervento instance = new Intervento();
        boolean expResult = true;
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Bari");
        to.setCivico("Via Lorenzo Perosi 24");
        to.setData("08/07/2013");
        to.setOraInizio("18.00");
        to.setCap("70126");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Insulina");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("054625", "ASTIGMATISMO");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(3);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        boolean result = instance.createIntervento(to);
        assertEquals(expResult, result);
     
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        int colCitta = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_CITTA);
        int colCivico = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_CIVICO);
        
        String citta = resultTab.getValueAt(row, colCitta).toString();
        String civico = resultTab.getValueAt(row, colCivico).toString();
        
        assertEquals(citta, "Bari");
        assertEquals(civico, "Via Lorenzo Perosi 24");
    }

    /**
     * Test of deleteIntervento method, of class Intervento.
     */
    @Test
    public void testDeleteIntervento() throws MainException {
        
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("GIUSEPPE", "STRISCIULLO");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("LIA", "MISSONI", "1/12/1986", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
   
        
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Margherita di Savoia - FG");
        to.setCivico("Via Ruggero Leoncavallo 5");
        to.setData("09/07/2013");
        to.setOraInizio("10.00");
        to.setCap("00246");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Prelievo del sangue");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("054628", "OBESITA");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(4);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);
        
        System.out.println("deleteIntervento");
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        
        colID = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID);
        int colCitta = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_CITTA);
        int idInt = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        String citta = resultTab.getValueAt(row, colCitta).toString();
        InterventoTO delInt = new InterventoTO();
        delInt.setID(idInt);
        boolean expResult = true;
        boolean result = instance.deleteIntervento(delInt);
        assertEquals(expResult, result);
        assertTrue("Margherita di Savoia - FG" != citta);
    }

    /**
     * Test of modificaIntervento method, of class Intervento.
     */
    @Test
    public void testModificaIntervento() throws MainException {
        
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("GIUSEPPE", "STRISCIULLO");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("LIA", "MISSONI", "1/12/1986", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
       
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Margherita di Savoia - FG");
        to.setCivico("Via Ruggero Leoncavallo 5");
        to.setData("09/07/2013");
        to.setOraInizio("10.00");
        to.setCap("00246");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Prelievo del sangue");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("054628", "OBESITA");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(4);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);

        System.out.println("modificaIntervento");
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        
        colID = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID);
        int idInt = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        to.setID(idInt);
        to.setCitta("Roma");
        to.setCivico("Via Condotti 15");
        to.setCap("00673");
        tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Iniezione farmaco");
        
        listaPatologie = new ArrayList<>();
        instancePat = new Patologia();
        toPat = new PatologiaTO("031628", "MICOSI");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(5);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        to.addTipoIntervento(tipoIntervento1);
        boolean expResult = true;
        boolean result = instance.modificaIntervento(to);
        InterventoTO getInt = instance.getSpecificIntervento(idInt);
        assertEquals(expResult, result);
        assertEquals(getInt.getCitta(), "Roma");
        assertEquals(getInt.getTipoIntervento(1).getNome(), tipoIntervento1.getNome());
    }


    /**
     * Test of reset method, of class Intervento.
     */
    /*@Test
    public void testReset() {
        System.out.println("reset");
        Intervento instance = new Intervento();
        boolean expResult = false;
        boolean result = instance.reset();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getTabellaInterventiInfermiere method, of class Intervento.
     */
    @Test
    public void testGetTabellaInterventiInfermiere() throws MainException {
         
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("NICOLA", "FORNAERELLI");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("DOMENICO", "DE TOMA", "16/11/1975", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Bari");
        to.setCivico("Via Crispi 12");
        //date dall'intervento con 2 giorni in più rispetto alla data odierna
        long currentDateAfter = DateFormatConverter.oggi() + 172800000;
        String dateCurrentAfter = DateFormatConverter.long2dateString(currentDateAfter, FORMATO_DATA_INSERT);
        to.setData(dateCurrentAfter);
        to.setOraInizio("10.00");
        to.setCap("02456");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Insulina");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("034175", "Parkinson");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(3);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);

        
        System.out.println("getTabellaInterventi");
        AbstractTableModel result = instance.getTabellaInterventiInfermiere(idInf, "", "");
        assertEquals(result.getRowCount(), 1);
        row = result.getRowCount() - 1;
        int colCivico = ((ADISysTableModel) result).findColumn(InterventoMySqlDAO.NOME_COLONNA_CIVICO);
        assertEquals(result.getValueAt(row, colCivico), "Via Crispi 12");
        
        long currentDate = DateFormatConverter.oggi();
        String dateCurrent = DateFormatConverter.long2dateString(currentDate, FORMATO_DATA_INSERT);
        to.setData(dateCurrent);
        to.setCivico("Via Napoli, 23");
        instance.createIntervento(to);
        result = instance.getTabellaInterventiInfermiere(idInf, "", "");
        assertEquals(result.getRowCount(), 2);

        result = instance.getTabellaInterventiInfermiere(idInf, dateCurrent, dateCurrent);
        assertEquals(result.getRowCount(), 1);
        row = result.getRowCount() - 1;
        assertEquals(result.getValueAt(row, colCivico), "Via Napoli, 23");
        
        result = instance.getTabellaInterventiInfermiere(idInf, dateCurrentAfter, dateCurrentAfter);
        assertEquals(result.getRowCount(), 1);
        row = result.getRowCount() - 1;
        assertEquals(result.getValueAt(row, colCivico), "Via Crispi 12");
        
        result = instance.getTabellaInterventiInfermiere(idInf, dateCurrent, dateCurrentAfter);
        assertEquals(result.getRowCount(), 2);
        row = result.getRowCount() - 1;
        assertEquals(result.getValueAt(row, colCivico), "Via Crispi 12");
        
        result = instance.getTabellaInterventiInfermiere(idInf, dateCurrentAfter, dateCurrent);
        assertEquals(result.getRowCount(), 2);
        row = result.getRowCount() - 1;
        assertEquals(result.getValueAt(row, colCivico), "Via Napoli, 23");
    }

    /**
     * Test of verificaIntervento method, of class Intervento.
     */
    @Test
    public void testVerificaIntervento() throws MainException {
        
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("SIMONE", "CURIONE");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("ANTONELLA", "MONDINO", "01/06/1990", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Bari - BA");
        to.setCivico("Via Lorenzo D'avanzo 13");
        to.setData("04/07/2013");
        to.setOraInizio("21.00");
        to.setCap("70125");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Iniezione");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("081234", "Febbre alta");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(5);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);
        
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        
        colID = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID);
        int colIDInf = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID_INFERMIERE);
        int colData = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_DATA);
        int colOra = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO);
        int idInt =  Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        idInf = Integer.parseInt(resultTab.getValueAt(row, colIDInf).toString());
        String dataInit = resultTab.getValueAt(row, colData).toString();
        String ora = resultTab.getValueAt(row, colOra).toString();
        dataInit = DateFormatConverter.cambiaFormato(dataInit, FORMATO_DATA_TABELLA, FORMATO_DATA_INSERT);
        ora = DateFormatConverter.cambiaFormato(ora, FORMATO_ORA_TABELLA, FORMATO_ORA_INSERT);
        
        System.out.println("verificaIntervento");
        
        int expResult = -1;
        int result = instance.verificaIntervento(idInf, dataInit, ora);
        assertEquals(expResult, result);

        /*to.setID(idInt);
        instance.deleteIntervento(to);*/
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA_INSERT);
        String dataCorrente = dateFormat.format(c.getTime());
        
        long currentLongDate = DateFormatConverter.dateString2long(dataCorrente, FORMATO_DATA_INSERT);
        dataInit = DateFormatConverter.long2dateString(currentLongDate, FORMATO_DATA_INSERT);
        expResult = 1;
        result = instance.verificaIntervento(idInf, dataInit, ora);
        assertEquals(expResult, result);
        
         //inserimento data ad un ora e mezza dalla corrente in modo da non poter essere modificabile
        long hourNotUpdate = c.getTimeInMillis() + 5400000;
        ora = DateFormatConverter.long2dateString(hourNotUpdate, FORMATO_ORA_INSERT);
        to.setData(dataInit);
        to.setOraInizio(ora);
        to.setID(idInt);
        instance.modificaIntervento(to);
       
        expResult = 0;
        result = instance.verificaIntervento(idInf, dataInit, ora);
        assertEquals(expResult, result);
            
    }

    /**
     * Test of leggiTipiIntervento method, of class Intervento.
     */
    @Test
    public void testLeggiTipiIntervento() throws MainException {
        
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("GIUSEPPE", "STRISCIULLO");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("LIA", "MISSONI", "1/12/1986", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
   
        
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Margherita di Savoia - FG");
        to.setCivico("Via Ruggero Leoncavallo 5");
        to.setData("09/07/2013");
        to.setOraInizio("10.00");
        to.setCap("00246");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Prelievo del sangue");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("054628", "OBESITA");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(4);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);
        
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID);
        int idInt = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        InterventoTO getInt = new InterventoTO();
        getInt.setID(idInt);
        getInt = instance.getSpecificIntervento(idInt);
        
        System.out.println("leggiTipiIntervento");
        ArrayList<TipoIntervento> result = instance.leggiTipiIntervento(idInt);
        String nomeTipoInt = getInt.getTipoIntervento(0).getNome();
        assertEquals(nomeTipoInt, result.get(0).getNome(), "Prelievo del sangue");
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of leggiPatologieIntervento method, of class Intervento.
     */
    @Test
    public void testLeggiPatologieIntervento() throws MainException {
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("FRANCESCO", "PICCININNI");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("LUISA", "MASSA", "07/07/1964", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
   
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Sannicandro - BA");
        to.setCivico("Via del Mare - 28");
        to.setData("10/07/2013");
        to.setOraInizio("11.00");
        to.setCap("10256");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Trattamento cutaneo");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("054630", "ERITEMA");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(3);
        
        listaPatologie.add(toPat);
        
        toPat = new PatologiaTO("054631", "FOLLICOLITE");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(2);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);
        
        resultTab = instance.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(InterventoMySqlDAO.NOME_COLONNA_ID);
        int idInt = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        InterventoTO getInt = new InterventoTO();
        getInt.setID(idInt);
        getInt = instance.getSpecificIntervento(idInt);
  
        ArrayList<PatologiaTO> expList = getInt.getTipoIntervento(0).getListaPatologie();
        System.out.println("leggiPatologieIntervento");
        ArrayList<PatologiaTO> result = instance.leggiPatologieIntervento(idInt);
        assertEquals(expList.get(0).getNome(), result.get(0).getNome(), "ERITEMA");
        assertEquals(expList.get(1).getNome(), result.get(1).getNome(), "FOLLICOLITE");
    }

    @Test
    public void testGetListaInterventiInfermiere() throws MainException {
        
        Infermiere instanceInf = new Infermiere();
        InfermiereTO toInf = new InfermiereTO("ANTONIO", "CACCIAPAGLIA");
        assertNotNull("Infermiere creato nullo", toInf);
        instanceInf.createInfermiere(toInf);
        ArrayList<InfermiereTO> listaInfermieri = instanceInf.getDati();
        InfermiereTO expInf = listaInfermieri.get(listaInfermieri.size() - 1);
        int idInf = expInf.getID();
        
        Paziente instancePaz = new Paziente();
        PazienteTO toPaz = new PazienteTO("TIZIANA", "MASSA", "12/01/1982", new Object[0]);
        assertNotNull("Paziente creato nullo", toPaz);
        instancePaz.createPaziente(toPaz);
        AbstractTableModel resultTab = instancePaz.getTabella();
        int row = resultTab.getRowCount() - 1;
        int colID = ((ADISysTableModel) resultTab).findColumn(PazienteMySqlDAO.NOME_COLONNA_ID);
        int idPaz = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        
        Intervento instance = new Intervento();
        InterventoTO to = new InterventoTO();
        to.setIDInfermiere(idInf);
        to.setIDPaziente(idPaz);
        to.setCitta("Bitritto - BA");
        to.setCivico("Via Piave 64");
        to.setData("10/07/2013");
        to.setOraInizio("10.00");
        to.setCap("02456");
        TipoIntervento tipoIntervento1 = new TipoIntervento();
        tipoIntervento1.setNome("Metadone");
        
        ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
        Patologia instancePat = new Patologia();
        PatologiaTO toPat = new PatologiaTO("081234", "Tossicodipendenza");
        assertNotNull("Patologia creata nulla", to);
        instancePat.createPatologia(toPat);
        resultTab = instancePat.getTabella();
        row = resultTab.getRowCount() - 1;
        colID = ((ADISysTableModel) resultTab).findColumn(PatologiaMySqlDAO.NOME_COLONNA_ID);
        int id = Integer.parseInt(resultTab.getValueAt(row, colID).toString());
        toPat.setID(id);
        toPat.setGravita(4);
        
        listaPatologie.add(toPat);
        
        tipoIntervento1.setListaPatologie(listaPatologie);
        
        to.addTipoIntervento(tipoIntervento1);
        assertNotNull("Intervento creato nullo", to);
        instance.createIntervento(to);
        
        System.out.println("getListaInterventiInfermiere");
        ArrayList<InterventoTO> result = instance.getListaInterventiInfermiere(idInf);
        
        /*è 0 perchè il metodo getlistainterventi infermiere restituisce la lista degli
        interventi degli infermiere odierni o in data futura*/
        assertEquals(result.size(), 0);
        to.setData("10/07/2014");
        instance.createIntervento(to);
        assertEquals(result.size(), 0);
        result = instance.getListaInterventiInfermiere(idInf);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getTipoIntervento(0).getNome(), "Metadone");
    }

    /**
     * Test of alterTable method, of class Intervento.
     */
    /*@Test
    public void testAlterTable() {
        System.out.println("alterTable");
        ArrayList<String> listaCampi = null;
        Intervento instance = new Intervento();
        instance.alterTable(listaCampi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of alterTablePatologieIntervento method, of class Intervento.
     */
    /*@Test
    public void testAlterTablePatologieIntervento() {
        System.out.println("alterTablePatologieIntervento");
        ArrayList<String> listaCampi = null;
        Intervento instance = new Intervento();
        instance.alterTablePatologieIntervento(listaCampi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of alterTableTipiIntervento method, of class Intervento.
     */
    /*@Test
    public void testAlterTableTipiIntervento() {
        System.out.println("alterTableTipiIntervento");
        ArrayList<String> listaCampi = null;
        Intervento instance = new Intervento();
        instance.alterTableTipiIntervento(listaCampi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}