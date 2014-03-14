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
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Astrazione sul file di configurazione. Permette la memorizzazione
 * e la lettura sul file di configurazione.
 * Implementa l'interfaccia per la configurazione completa del sistema (getDati e setAllDati) e
 * l'interfaccia per l'accesso al database (getDBDates)
*/
public class Configurazione implements I_Configurazione, I_ConfigDB {
	
	private static File file;
	private static final String FILENAME = "database/configurazione.txt";
        private static ResourceBundle configuration = ResourceBundle.getBundle("adisys/server/property/Configurazione");
	private FileReader fRead;
	private BufferedReader fInput;
	private FileWriter fWrite;
	private PrintWriter fOutput;
	
	/**Sa' se le variabili d'istanza sono sincronizzate con i valori del file*/
	private boolean isSynchronized;
	
	//database configuration
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
        	configuration = ResourceBundle.getBundle(path, locale);
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
                    String message = configuration.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canRead()){
                    String message = configuration.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE LETTO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
		if(!file.canWrite()){
                    String message = configuration.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON PUO' ESSERE MODIFICATO.");
                    GMessage.message_error(message);
                    throw new MainException(message);
		}
	}
	
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
                                String field = rowArray[0];
                                String value = "";
                                if(!field.equals("dbPassword"))
                                	value = rowArray[1];
                                else if(rowArray.length == 2)
                                	value = rowArray[1];
				if(rowArray.length != 2 && !field.equals("dbPassword")){//formato campo=valore non rispettato
                                    String message = configuration.getString("ECCEZIONE CAMPO VALORE");
                                    GMessage.message_error(message);
                                    throw new MainException(message);
				} else if(rowArray.length > 2 && field.equals("dbPassword")){
                                    String message2 = configuration.getString("ECCEZIONE CAMPO VALORE");
                                    GMessage.message_error(message2);
                                    throw new MainException(message2);
                                }
				
				//deve controllare anche che ci siano tutti i campi
				if(field.equals("dbPath")){
					this.dbPath = value;
					dbPath=true;
				}
				else if(field.equals("dbName")){
					this.dbName = value;
					dbName=true;
				}
				else if(field.equals("dbUsername")){
					this.dbUsername = value;
					dbUsername=true;
				}
				else if(field.equals("dbPassword")){
					this.dbPassword = value;
					dbPassword=true;
				}
				
				else{//se c'e' un valore sconosciuto allora il formato e' sbagliato
                                    String message3 = configuration.getString("ECCEZIONE, RISCONTRATO UN ERRORE DI FORMATO ") +
                                    		configuration.getString("NEL FILE DI CONFIGURAZIONE");
                                    GMessage.message_error(message3);
                                    throw new MainException(message3);
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
				 String message4 = configuration.getString("ECCEZIONE CAMPI MANCANTI");
                                 GMessage.message_error(message4);
                                 throw new MainException(message4);
			}
			
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			throw new MainException(configuration.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.")+
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
	
	
	/**
	 * Imposta i valori contenuti nel transfer object nelle variabili d'istanza e li memorizza
	 * nel file di configurazione (se non esiste, lo crea).
	 * 
	 * @param to il transfer object dove prelevare i valori
	 * @return true se la memorizzazione ha successo, false se fallisce
	 * @throws MainException Valori incompleti nel transfer object
	*/
	public boolean setAllDati(ConfigurazioneTO to) throws MainException{
		if(!checkDatiTO(to)){
			throw new MainException(configuration.getString("IMPOSSIBILE ESEGUIRE LA MEMORIZZAZIONE DEI VALORI ") +
					configuration.getString("NEL FILE DI CONFIGURAZIONE. VALORI INCOMPLETI"));
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

    @Override
    public void createDatabaseEsistente(ConfigurazioneTO to) {
        try {
            CreateDBFromScript db = new CreateDBFromScript(to);	
        } catch (MainException e) {
            e.printStackTrace();
        }
    }
	
}
