/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.strumenti;

import java.io.File;
import java.io.IOException;
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
public class FileFactoryTest {
    
    public FileFactoryTest() {
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
     * Test of fileExists method, of class FileFactory.
     */
    @Test
    public void testFileExists() {
        System.out.println("fileExists");
        String path = "file/infermieri.txt";
        boolean expResult = true;
        boolean result = FileFactory.fileExists(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getFileContent method, of class FileFactory.
     */
    @Test
    public void testGetFileContent_String() throws Exception {
        System.out.println("getFileContent");
        String path = "file/infermieri.txt";
        boolean expResult = true;
        String result = FileFactory.getFileContent(path);
        boolean contain = result.contains("INFERMIERI");
        assertEquals(expResult, contain);

    }

    /**
     * Test of getFileContent method, of class FileFactory.
     */
    @Test
    public void testGetFileContent_File() throws Exception {
        System.out.println("getFileContent");
        File f = new File("file/infermieri.txt");
        boolean expResult = true;
        String result = FileFactory.getFileContent(f);
        boolean contain = result.contains("INFERMIERI");
        assertEquals(expResult, contain);
    }

    /**
     * Test of setFileContent method, of class FileFactory.
     */
    @Test
    public void testSetFileContent() throws Exception {
        System.out.println("setFileContent");
        File f = FileFactory.createFile("file/test.txt");
        String content = "Questo è un file di prova utilizzato per i test";
        FileFactory.setFileContent(f, content);
        String result = FileFactory.getFileContent(f);
        boolean contains = result.contains("Questo è un file di prova utilizzato per i test");
        assertEquals(contains, true);
        boolean notContains = result.contains("Stringa non contenuta");
        assertEquals(notContains, false); 
    }

    /**
     * Test of removeFile method, of class FileFactory.
     */
    @Test
    public void testRemoveFile() throws IOException {
        System.out.println("removeFile");
        String path = "file/prova.txt";
        FileFactory.createFile(path);
        boolean expResult = true;
        boolean result = FileFactory.removeFile(path);
        assertEquals(expResult, result);
        expResult = false;
        result = FileFactory.fileExists(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of createFile method, of class FileFactory.
     */
    @Test
    public void testCreateFile_String_boolean() throws Exception {
        System.out.println("createFile");
        String path = "file/create.txt";
        boolean isdirectory = false;
        File result = FileFactory.createFile(path, isdirectory);
        assertNotNull(result);
        boolean expResult = true;
        boolean exists = FileFactory.fileExists(path);
        assertEquals(expResult, exists);
        FileFactory.removeFile(path);
    }

    /**
     * Test of createFile method, of class FileFactory.
     */
    @Test
    public void testCreateFile_String() throws Exception {
        System.out.println("createFile");
        String path = "file/create.txt";
        boolean isdirectory = false;
        File result = FileFactory.createFile(path);
        assertNotNull(result);
        boolean expResult = true;
        boolean exists = FileFactory.fileExists(path);
        assertEquals(expResult, exists);
        FileFactory.removeFile(path);
    }

    /**
     * Test of createFileIfNotExists method, of class FileFactory.
     */
    @Test
    public void testCreateFileIfNotExists() throws Exception {
        System.out.println("createFileIfNotExists");
        String path = "file/test.txt";
        boolean isdirectory = false;
        File result = FileFactory.createFileIfNotExists(path, isdirectory);
        assertNotNull(result);
        boolean expResult = true;
        boolean exists = FileFactory.fileExists(path);
        assertEquals(expResult, exists);
        FileFactory.removeFile(path);
    }

}