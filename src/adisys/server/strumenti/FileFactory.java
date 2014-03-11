/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.strumenti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

/**
 *
 * @author Luca
 */
/**
 * Factory per la creazione/modifica/lettura 
 * e verifica di risorse su filesystem.

 */
public class FileFactory { 
  
  /**
   * Testa l'esistenza di una risorsa su filesystem.
   * 
   * @param path L'indirizzo fisico della risorsa.
   * @return     true se esiste 
   *             altrimenti false.
   */
  public static boolean fileExists(String path){ 
    return new File(path).exists();  
  } 
  
  /**
   * Restituisce il contenuto del file 
   * all'indirizzo path
   * 
   * @param  path  Indirizzo fisico del file da leggere.
   * @return       Il contenuto del file in una stringa.
   * 
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static String getFileContent(String path)  
      throws FileNotFoundException, IOException{ 
    String content = ""; 
    BufferedReader br =  
        new BufferedReader(new FileReader(path)); 
 
    while (br.ready()) 
      content += "\n" + br.readLine(); 
      
    br.close(); 
    return content; 
  } 
 
  /**
   * Restituisce il contenuto del file f 
   * 
   * @param  f  Il file da leggere.
   * @return    Il contenuto del file in una stringa.
   * 
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static String getFileContent(File f)  
      throws FileNotFoundException, IOException{ 
    String content = "";   
    BufferedReader br =  
        new BufferedReader(new FileReader(f)); 
   
    while (br.ready()) 
      content += "\n" + br.readLine(); 
      
    br.close(); 
    return content; 
  } 
  
  /**
   * Inserisce del contenuto in un file.
   * 
   * @param f        File in cui scrivere
   * @param content  Contenuto da scrivere nel file
   * 
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void setFileContent 
        (File f, String content)  
      throws FileNotFoundException, IOException{ 
    FileOutputStream fos;       
    fos = new FileOutputStream(f); 
    fos.write(content.getBytes()); 
    fos.close();  
  } 
  
  /**
   * Rimuove un file.
   * 
   * @param path  Indirizzo fisico del file da cancellare.
   * @return      true se il file è stato 
   *              eliminato, altrimenti false. 
   */
  public static boolean removeFile(String path){ 
    return new File(path).delete(); 
  } 
  
  /**
   * Crea un file.
   * 
   * @param path        Indirizzo fisico del file da creare.
   * @param isdirectory Indica se bisogna creare una directory
   * @return            Il file creato oppure null 
   */ 
  public static File createFile 
        (String path, boolean isdirectory)  
      throws IOException{ 
    File f = new File(path); 
    boolean created = (isdirectory)  
        ? f.mkdir() : f.createNewFile(); 
    return (created) ? f : null; 
  } 
  
  /**
   * Crea un nuovo file e restituisce il suo riferimento.
   * 
   * @param path  Indirizzo del file da creare.
   * @return      Il file appena creato.
   * 
   * @throws IOException
   */
  public static File createFile(String path)  
      throws IOException{ 
    File f = new File(path); 
    if(f.exists()) 
      f.delete(); 
 
    if(!f.createNewFile())  
      throw new IOException("Non è stato possibile creare il file '"  
          + path + "'"); 
   
    return f; 
  } 
  
  /**
   * Crea un nuovo file solo se non esiste 
   * già all'indirizzo indicato.
   * 
   * @param path        Indirizzo del file.
   * @param isdirectory Indica se si tratta di una directory.
   * @return            Il file esistente o creato;
   *
   * @throws IOException  Se non è possibile operare sul file system.
   */
  public static File createFileIfNotExists 
       (String path, boolean isdirectory) throws IOException{ 
    File f = new File(path); 
    if(!f.exists()){ 
      boolean created = (isdirectory)  
          ? f.mkdir() : f.createNewFile(); 
      return (created) ? f : null; 
    } 
    return f; 
  }  
  
  /**
   * Utilizza il template dei menu 
   * per creare il menù del Lunedì.
   * 
   * TEMPLATE:
   *  PRIMI:
   *      *PASTA1*
   *      *RISOTTO1*
   *      *MINESTRA1*
   *
   *  SECONDI:
   *      *CARNE1*
   *      *CARNE2*
   *      *PESCE1*
   *      
   *  CONTORNI:
   *      *VERDURE1*
   *      *VERDURE2*
   *      
   *  DOLCI:
   *      *DOLCI1*
   *      *DOLCI2*
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{ 
    //Path del file di template in input
    String menu_path = "C://temp//menu_template.txt"; 
   
    //Path del file che verrà creato per il menù del Lunedì
    String lunedi_path = "C://temp//menu_Lunedi.txt"; 
   
    //Path del file che verrà creato per tracciare 
    //l'andamento delle operazioni svolte.
    String logs_path = "C://temp//menu_logs.txt"; 
   
    //Inizio creazione dei los applicativi
    String logs = "Inizio attività."; 
   
    //Referenzio il template dei menù.
    File f = createFileIfNotExists(menu_path, false); 
   
    //Traccio nel log il contenuto originale 
    //del file di template
    logs += "\nAperto Template dei Menù: " + menu_path; 
    logs += FileFactory.getFileContent(f); 
   
    //Preparo il menù del Lunedì: 
    //ogni coppia chiave/valore rappresenta 
    //il token da sostituire/il nuovo contenuto.
    Hashtable<String, String> menuLunedi =  
        new Hashtable<String, String>(); 
    menuLunedi.put("*PASTA1*", "Spaghetti all'amatriciana"); 
    menuLunedi.put("*RISOTTO1*", "Risotto ai funghi porcini"); 
    menuLunedi.put("*MINESTRA1*", "Minestrone di stagione"); 
    menuLunedi.put("*CARNE1*", "Carne di cavallo ai ferri"); 
    menuLunedi.put("*CARNE2*", "Fesa di vitello"); 
    menuLunedi.put("*PESCE1*", "Trota al cartoccio"); 
    menuLunedi.put("*VERDURE1*", "Insalata mista"); 
    menuLunedi.put("*VERDURE2*", "Patatine fritte"); 
    menuLunedi.put("*DOLCI1*", "Tiramisù"); 
    menuLunedi.put("*DOLCI2*", "Torta Margherita"); 
   
    logs += "\nCaricato il dizionario dei token " 
          + "da sostituire il Lunedì."; 
   
    //Traccio la corretta esecuzione della generazione 
    //del menù del Lunedì.
    logs += "\nSostituzione eseguita." 
        + "\nMenù del Lunedì creato: " + lunedi_path; 
  logs += "\nAttività terminata correttamente"; 
   
    //Scrivo il file di logs.
    setFileContent( 
        createFileIfNotExists(logs_path, false), logs); 
  } 
} 