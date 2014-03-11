package business.configurazione;



/**
 * Interfaccia di {@link Configurazione} per i dati d'accesso al database.
 * Usata dalla classe di collegamento al database {@link integration.LinkingDb}
*/
public interface I_ConfigDB {

	
	/**
	 * Restituisce le informazioni per accedere al database.<br>
	 * Usa le variabili gi� sincronizzate con i valori del database
	 * @return l'array contenente nel'ordine dbPath, dbName, dbUsername, dbPassword
	*/
	public String[] getDbDates();
	
}
