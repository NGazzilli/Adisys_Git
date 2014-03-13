package business.configurazione;

import business.TO;
import messaggistica.MainException;

/**
 * Transfer Object of the configuration entity. It wraps all the configuration data.
 * Same functionalities as for the Transfer Object pattern
*/
public class ConfigurazioneTO implements java.io.Serializable, TO{
	private static final long serialVersionUID = 1L;
	

	//configurazione database
	private String dbPath;
	private String dbName; 
	private String dbUsername;
	private String dbPassword;
	
	//configurazione sistema (parametri inseriti dall'utente)
	/*
        private int distanza;
	private int inattivita;
	private String sysPassword;*/
	
	/**
	 * Acquires values to create a pushing transfer object
	 * @param sysPassword clear system password
	*/
	public ConfigurazioneTO(String dbPath, String dbName, String dbUsername, String dbPassword/*, 
			int distanza, int inattivita, String sysPassword*/){
		this.dbName = dbName;
                this.dbPath = dbPath;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
/*		this.distanza = distanza;
		this.inattivita = inattivita;
		
		//la password criptata
		this.sysPassword = Encrypt.encrypt(sysPassword);*/
			
	}
        
        public ConfigurazioneTO() throws MainException {
                try {
			I_ConfigDB config = new Configurazione();
			String[] dbDates = config.getDbDates();
			
				dbPath = dbDates[0];
				dbName = dbDates[1];
				dbUsername = dbDates[2];
				dbPassword = dbDates[3];
			
			
		} catch (MainException e) {
			System.err.println(e.getMessage());
			throw new MainException("Collegamento al database non riuscito.");
		}
        }

	public String getDbPath() {
		return dbPath;
	}
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}	
	
	
}
