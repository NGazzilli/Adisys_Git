/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.strumenti;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class DateFormatConverterTest {
    
    public DateFormatConverterTest() {
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
     * Test of cambiaFormato method, of class DateFormatConverter.
     */
    @Test
    public void testCambiaFormato() {
        System.out.println("cambiaFormato");
        String dataOra = "2013-07-22 15.30";
        String formatoIngresso = "yyyy-MM-dd HH.mm";
        String formatoUscita = "dd/MM/yyyy HH:mm";
        String expResult = "22/07/2013 15:30";
        String result = DateFormatConverter.cambiaFormato(dataOra, formatoIngresso, formatoUscita);
        assertEquals(expResult, result);
        formatoIngresso = "HH.mm yyyy-MM-dd";
        result = DateFormatConverter.cambiaFormato(dataOra, formatoIngresso, formatoUscita);
        assertEquals(null, result);
        String data = "2013-07-18";
        String expData = DateFormatConverter.cambiaFormato(data, "yyyy-MM-dd", "dd/MM/yyyy");
        assertEquals(expData, "18/07/2013");
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of dateString2long method, of class DateFormatConverter.
     */
    @Test
    public void testDateString2long() {
        System.out.println("dateString2long");
        String dataOra = "22/07/2013";
        String formatoIngresso = "dd/MM/yyyy";
        long result = DateFormatConverter.dateString2long(dataOra, formatoIngresso);
        assertNotNull(result);
        String convert = DateFormatConverter.long2dateString(result, formatoIngresso);
        assertEquals(convert, dataOra);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of long2dateString method, of class DateFormatConverter.
     */
    @Test
    public void testLong2dateString() {
        System.out.println("long2dateString");
        long dataOra = 1374400000;
        String formatoUscita = "dd/MM/yyyy";
        String result = DateFormatConverter.long2dateString(dataOra, formatoUscita);
        long expResult = DateFormatConverter.dateString2long(result, formatoUscita);
        String ris1 = DateFormatConverter.long2dateString(dataOra, formatoUscita);
        String ris2 = DateFormatConverter.long2dateString(expResult, formatoUscita);
        assertEquals(ris1, ris2);
        
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of parseable method, of class DateFormatConverter.
     */
    @Test
    public void testParseable() {
        System.out.println("parseable");
        String dataOra = "01/12/1990";
        String formato = "dd/MM/yyyy";
        boolean expResult = true;
        boolean result = DateFormatConverter.parseable(dataOra, formato);
        assertEquals(expResult, result);
        String formatoErr = "yyyy-MM-dd";
        expResult = false;
        result = DateFormatConverter.parseable(dataOra, formatoErr);
        assertEquals(expResult, result);
    }

    /**
     * Test of oggi method, of class DateFormatConverter.
     */
    @Test
    public void testOggi() {
        System.out.println("oggi");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataCorrente = dateFormat.format(c.getTime());
        long result = DateFormatConverter.oggi();
        String expResult = DateFormatConverter.long2dateString(result, "dd/MM/yyyy");
        assertEquals(expResult, dataCorrente);
    }

}