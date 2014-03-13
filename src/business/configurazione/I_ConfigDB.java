package business.configurazione;



/**
 * Interface of {@link Configurazione} for database's access data.
 * Used by the related database link class {@link integration.LinkingDb}
*/
public interface I_ConfigDB {

	
	/**
	 * Returns the information in order to gain access to the database. <br>
	 * Uses the already synchronized variables with values fetched directly from the database.
	 * @return the array containing dbPath, dbName, dbUsername, dbPassword (ordered list)
	*/
	public String[] getDbDates();
	
}
