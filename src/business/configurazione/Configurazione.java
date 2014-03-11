package business.configurazione;

import integration.dao.CreateDBFromScript;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import messaggistica.GMessage;

import messaggistica.MainException;

/**
 * Astrazione sul file di configurazione. Permette la memorizzazione
 * e la lettura sul file di configurazione.
 * Implementa l'interfaccia per la configurazione completa del sistema (getDati e setAllDati) e
 * l'interfaccia per l'accesso al database (getDBDates)
*/
public class Configurazione implements I_Configurazione, I_ConfigDB {
	
	private static File file;
	private static final String FILENAME = "database/configurazione.txt";
        private static ResourceBundle configurazione = ResourceBundle.getBundle("adisys/server/property/Configurazione");
	private FileReader fRead;
	private BufferedReader fInput;
	private FileWriter fWrite;
	private PrintWriter fOutput;
	
	/**Sa' se le variabili d'istanza sono sincronizzate con i valori del file*/
	private boolean isSynchronized;
	
	//configurazione database
        private String dbPath; //= "jdbc:hsqldb:file:database/";
	private String dbName; //= "ADISysData";
	private String dbUsername; //= "asl";
	private String dbPassword; //= "";

	
	/**
	 * Metodo statico che predica se il file di configurazione esiste, usato dal main
	 * per decidere quale finestra aprire per prima.
	 * @return true se esiste una configurazione, false altrimenti
	*/
	public static boolean checkConfigurazioneExists(){
		file = new File(FILENAME);
		boolean exists = file.exists();
		file = null;
		return exists;
	}
	
        public static void setResourceBundle(String path, Locale locale){
            configurazione = ResourceBundle.getBundle(path, locale);
        }
	/**
	 * Costruttore, controlla l'esistenza del file e la possibilità di lettura e scrittura.
	 * Se il file non esiste lo crea e poi controlla se la creazione ha avuto successo.
	 * 
	 * @throws MainException la creazione del file di configurazione fallisce,
	 * il file esiste ma non può essere letto oppure non può essere modificato
	*/
	public Configurazione() throws MainException{
                this.isSynchronized = false;
		if(file==null){
			file = new File(FILENAME);
		}
		
		try{
			this.readDatiFromFile();
		}
		catch(Exception e){
			//se giunge qui vuol dire che il file non esiste, quindi lo crea
			this.isSynchronized = false;
//			e.printStackTrace();
			
//			System.out.println("Configurazione : "+e.exception);
			try {
				fWrite = new FileWriter(file);
				fOutput = new PrintWriter(fWrite);
				fWrite.close();
				fOutput.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				
			
		}
		
		
		if(!file.exists()){ 
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canRead()){
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE LETTO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canWrite()){
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE MODIFICATO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
	}

	/**
	 * Costruttore, setta il nuovo percorso dove trovare il file di configurazione.<br>
	 * Usa il metodo private {@link #readDatiFromFile()} per effettuare la sincronizzazione delle
	 * variabili d'istanza con i valori nel file
	 * @param path il percorso del file di configurazione
	 * @throws MainException se il file non esiste, non puo' essere letto o non puo' essere modificato
	*/
	/*public Configurazione(String path) throws MainException{
		file = new File(path);
		this.isSynchronized = false;
		
		try{
			this.readDatiFromFile();
		}
		catch(MainException e){
			if(e.exception.equals(FileNotFoundException.class)){
				System.out.println(configurazione.getString("CONFIGURAZIONE : ")+e.exception);
				try {
					fWrite = new FileWriter(file);
					fOutput = new PrintWriter(fWrite);
					fWrite.close();
					fOutput.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		
                if(!file.exists()){ 
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canRead()){
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE LETTO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canWrite()){
                    String message = configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE MODIFICATO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
	}*/	
	
	/**
	 * Effettua la sincronizzazione tra i dati contenuti nel file e le variabili
	 * di istanza di questa classe.
	 * @throws MainException errore di formato nel file o non esiste
	*/
	private void readDatiFromFile() throws MainException{
		try {
			fRead = new FileReader(file);
			fInput = new BufferedReader(fRead);
			
			String row = fInput.readLine();
			String[] rowArray;
			//deve controllare anche che ci siano tutti i campi
			boolean dbPath=false, dbName=false, dbUsername=false, dbPassword=false;
			while (row != null){
				//System.out.println("Stringa letta: "+row);
				rowArray = row.split("=");
                                String campo = rowArray[0];
                                String valore = "";
                                if(!campo.equals("dbPassword"))
                                    valore = rowArray[1];
                                else if(rowArray.length == 2)
                                    valore = rowArray[1];
				if(rowArray.length != 2 && !campo.equals("dbPassword")){//formato campo=valore non rispettato
                                    String messaggio = configurazione.getString("ECCEZIONE CAMPO VALORE");
                                    GMessage.message_error(messaggio);
                                    throw new MainException(messaggio);
				} else if(rowArray.length > 2 && campo.equals("dbPassword")){
                                    String messaggio = configurazione.getString("ECCEZIONE CAMPO VALORE");
                                    GMessage.message_error(messaggio);
                                    throw new MainException(messaggio);
                                }
				
				//deve controllare anche che ci siano tutti i campi
				if(campo.equals("dbPath")){
					this.dbPath = valore;
					dbPath=true;
				}
				else if(campo.equals("dbName")){
					this.dbName = valore;
					dbName=true;
				}
				else if(campo.equals("dbUsername")){
					this.dbUsername = valore;
					dbUsername=true;
				}
				else if(campo.equals("dbPassword")){
					this.dbPassword = valore;
					dbPassword=true;
				}
				/*else if(campo.equals("distanza")){
					this.distanza = Integer.parseInt(valore);
					distanza=true;
				}
				else if(campo.equals("inattivita")){
					this.inattivita = Integer.parseInt(valore);
					inattivita=true;
				}
				else if(campo.equals("sysPassword")){
					this.sysPassword = valore;
					sysPassword=true;
				}*/
				else{//se c'e' un valore sconosciuto allora il formato e' sbagliato
                                    String messaggio = configurazione.getString("ECCEZIONE, RISCONTRATO UN ERRORE DI FORMATO ") +
							configurazione.getString("NEL FILE DI CONFIGURAZIONE");
                                    GMessage.message_error(messaggio);
                                    throw new MainException(messaggio);
				}
				
				row = fInput.readLine();
			}
			fInput.close();
			fRead.close();
			if(dbPath && dbName && dbUsername && dbPassword){
				//tutto ok, tutti i valori sono stati inseriti
				this.isSynchronized = true;//sincronizzazione effettuata con successo
			}
			else{//non sono presenti tutti i campi nel file di configurazione
				 String messaggio = configurazione.getString("ECCEZIONE CAMPI MANCANTI");
                                 GMessage.message_error(messaggio);
                                 throw new MainException(messaggio);
			}
			
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			throw new MainException(configurazione.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.")+
					e.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Restituisce le informazioni per accedere al database.<br>
	 * Usa le variabili gi&agrave sincronizzate con i valori del database
	 * @return l'array contenente nel'ordine dbPath, dbName, dbUsername, dbPassword
	*/
	public String[] getDbDates(){
		if(this.isSynchronized){
                        
			String[] dbDates = {dbPath, dbName, dbUsername, dbPassword};
			//JOptionPane.showMessageDialog(null, dbDates[0]);
                        return dbDates;
		}
		else{
			return null;
		}
		
	}

	
	/**
	 * Restituisce il Transfer Object di Configurazione che incapsula tutti i dati.
	 * Il metodo sovrascrive le variabili d'istanza relative ai campi.
	 * Potrebbe essere usata quando si vuole cambiare la configurazione
	 * @return il transfer object con tutti i dati contenuti nel file di configurazione 
	 * o null se c'&egrave un errore di formato nel file o non esiste
	*/
	public ConfigurazioneTO getDati(){
		if(this.isSynchronized){
			return new ConfigurazioneTO(dbPath, dbName, dbUsername, dbPassword);
		}
		else{
			return null;
		}
		
	}
	
	/**
	 * Controlla se i dati di configurazione memorizzati nel transfer object siano tutti e che non ci siano
	 * valori nulli.
	 * @param to il transfer object con i dati da controllare
	 * @return true se non ci sono campi nulli, false altrimenti 
	*/
	private boolean checkDatiTO(ConfigurazioneTO to){
		if(to.getDbPath()==null || to.getDbName()==null || to.getDbUsername()==null || 
				to.getDbPassword()==null /*|| to.getDistanza()==0 || to.getInattivita()==0
				|| to.getSysPassword()==null*/){
			return false;
		}
		else{
			return true;
		}
	}
	

	/**
	 * Prendendo in input il transfer object di Configurazione (ConfigurazioneTO) copia tutti i valori
	 * nelle variabili d'istanza
	 * @param to il tranfer object con tutti i dati
	*/
	private void copyFromTO(ConfigurazioneTO to){
		this.dbPath = to.getDbPath();
		this.dbName = to.getDbName();
		this.dbUsername = to.getDbUsername();
		this.dbPassword = to.getDbPassword();
/*		this.distanza = to.getDistanza();
		this.inattivita = to.getInattivita();
		
		//preleva la password gia' criptata dal transfer object
		this.sysPassword = to.getSysPassword();*/
		this.isSynchronized = true;
	}
	
	/*
	 * La funzione di creazione dl database era prima fatta nel metodo di qua sotto,
	 * quando si settavano i dati di configurazione. Ora quella classe sta in integration.dao
	 * ed e' MySqlDAOFactory che puo' invocarla se gli giunge la richiesta dall'alto
	 * di reset del db
	 * 
	 * Se reset=true allora si suppone vengano cambiati anche i valori del database
	 * {@link#dbPath} {@link#dbName} {@link#dbUsername} {@link#dbPassword}
	 * quindi rieffettua la costruzione del database (i dati precendentemente inseriti
	 * verranno cancellati) un reset.
	*/
	
	
	
	/**
	 * Setta i valori contenuti nel transfer object nelle variabili d'istanza e li memorizza
	 * nel file di configurazione (se non esiste, lo crea).
	 * 
	 * @param to il transfer object dove prelevare i valori
	 * @return true se la memorizzazione ha successo, false se fallisce
	 * @throws MainException Valori incompleti nel to
	*/
	public boolean setAllDati(ConfigurazioneTO to) throws MainException{
		if(!checkDatiTO(to)){
			throw new MainException(configurazione.getString("IMPOSSIBILE ESEGUIRE LA MEMORIZZAZIONE DEI VALORI ") +
					configurazione.getString("NEL FILE DI CONFIGURAZIONE. VALORI INCOMPLETI"));
		}
		
		//copio i valori del transfer object nelle variabili d'istanza(sovrascrivendo quelle
		//del file sincronizzate)
		copyFromTO(to);
		
		try {
			fWrite = new FileWriter(file);
			fOutput = new PrintWriter(fWrite);
			fOutput.println("dbPath=" + dbPath);
			fOutput.println("dbName=" + dbName);
			fOutput.println("dbUsername=" + dbUsername);
			fOutput.println("dbPassword=" + dbPassword);
/*			fOutput.println("distanza=" + distanza);
			fOutput.println("inattivita=" + inattivita);
			
			//scrive su file la password gia' criptata dal tranfer object
			fOutput.println("sysPassword=" + sysPassword);*/
			
			fOutput.close();
			fWrite.close();
			
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Crea il database da zero tramite lo script sql
	 * @return true se la creazione ha successo, false altrimenti
	*/
	/*@Override
	public boolean createDatabase(ConfigurazioneTO to){
		try {
			CreateDBFromScript db = new CreateDBFromScript(to);
			return db.createDBMS();
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
	}*/

    @Override
    public void createDatabaseEsistente(ConfigurazioneTO to) {
        try {
            CreateDBFromScript db = new CreateDBFromScript(to);	
        } catch (MainException e) {
            e.printStackTrace();
        }
    }
	
}
