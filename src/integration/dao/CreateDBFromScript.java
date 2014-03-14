package integration.dao;

import business.configurazione.ConfigurazioneTO;
import integration.I_LinkingDb;
import integration.LinkingDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import messaggistica.GMessage;

import messaggistica.MainException;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Legge il file che contiene lo script SQL da eseguire sul database mysql
 * per creare il database stesso e le tabelle.<br>
 * Si connette al database usando {@link LinkingDb}.<br>
 * E' quindi riusabile insieme alla classe LinkingDb
*/
public class CreateDBFromScript {
	/**Percorso interno ad ADISys dove trovare il file dello script sql*/
	private static final String SQL_SCRIPT_PATH = "system_files/ADISysDB.sql";
	private static File file;
        private static ResourceBundle createDBFromScript = ResourceBundle.getBundle("adisys/server/property/CreateDBFromScript");
	private FileReader fRead;
	private BufferedReader fInput;
	/**Variabile boolean che predica se si &egrave conclusa la lettura del file*/
	private boolean isFinish;
	
	private I_LinkingDb linkDB;
	
        public static void setResourceBundle(String path, Locale locale){
            createDBFromScript = ResourceBundle.getBundle(path, locale);
        }
	
	/**
	 * Costruttore, apre gli streaming in lettura sul file {@link #SQL_SCRIPT_PATH}
	 * @throws MainException se il file non esiste o non &egrave apribile
	*/
	public CreateDBFromScript(ConfigurazioneTO to) throws MainException{
		file = new File(SQL_SCRIPT_PATH);
		try{
			//apertura file dello script in lettura
			fRead = new FileReader(file);
			fInput = new BufferedReader(fRead);
			
			linkDB = new LinkingDb(to);
			linkDB.connect();
			
			isFinish = false;
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
                        String messaggio = createDBFromScript.getString("IMPOSSIBILE CREARE IL DATABASE, FILE DELLO SCRIPT NON DISPONIBILE");
                        GMessage.message_error(messaggio);
			throw new MainException(messaggio);
		}
	}
	
	
	
	/**
	 * Preleva dal file {@link #SQL_SCRIPT_PATH} tutti i comandi sql da eseguire per creare
	 * la struttura del database
	 * @return true se la creazione ha successo con nessun errore, false altrimenti (successo
	 * con errore)
	*/
	public boolean createDBMS(){
		System.out.println("CreateDBFromScript.createDBMS()");
		try {
			boolean allFine = true;
			while(!isFinish){
				String stmtUntilSemicolon = readUntilSemicolon();
				//se e' arrivato allo \n vuol dire che il file e' finito
				if(!stmtUntilSemicolon.equals("\n")){
					if(linkDB.update(linkDB.prepareStatement(stmtUntilSemicolon))){
						System.out.println(createDBFromScript.getString("AGGIORNAMENTO ESEGUITO CON SUCCESSO: ")+
							stmtUntilSemicolon.replace('\n', ' '));
					}
					else{
                                                String messaggio = createDBFromScript.getString("AGGIORNAMENTO FALLITO, RICONTROLLARE LA SINTASSI SQL: ") +
                                                        stmtUntilSemicolon.replace('\n', ' ');
                                                GMessage.message_error(messaggio);
						System.err.println(messaggio);
						allFine = false;
					}
//					System.out.println(stmtUntilSemicolon);
				}
			}
			System.out.println(createDBFromScript.getString("SCRIPT DI CREAZIONE DATABASE ESEGUITO."));
			fInput.close();
			fRead.close();
			
			
			return allFine;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
			
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Legge una stringa dal file fino al carattere semicolon (; punto e virgola),
	 * se dal punto di lettura fino alla fine non c'è un carattere semicolon
	 * restituisce tutta la stringa fino alla fine.
	 * @return una stringa del file fino al semicolon
	 * @throws IOException eccezione lanciata dallo streaming in lettura sul file
	*/
	private String readUntilSemicolon() throws IOException{
		String stat = "";
		int next;
		
		while( ((next = fInput.read()) != -1) ){
			char nextChar = (char) next;
			stat += nextChar;
//			System.out.print(nextChar);
			if(nextChar==';')
				return stat;
		}
		isFinish = true;
		
		return stat;
		
	}

	
	
}
