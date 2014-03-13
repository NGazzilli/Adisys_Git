package business.configurazione;

import messaggistica.MainException;


/**
 * Interface of {@link Configurazione} for complete configuration, used by the configuration manager.
*/
public interface I_Configurazione {
	
	/**
	 * Returns the COnfiguration Transfer Object that wraps all the data.
	 * The method overrides the instance variables related to the fields.
	 * It could be used when there is need for a configuration change.
	 * @return the transfer object with all the data contained in the configuration file, or <i>null</i>
	 * if there is a format error in the file or it doesn't exist. 
	*/
	public ConfigurazioneTO getDati();
	
	/**
	 * Sets values contained in the transfer object in the instance variables and stores them in the configuration file, 
	 * creating a new one if there is no existing configuration file yet.
	 * 
	 * @param to the transfer object from which fetching the values
	 * @return true if the storing process is successful, false otherwise
	 * @throws MainException Incomplete values in the transfer object
	*/
	public boolean setAllDati(ConfigurazioneTO to) throws MainException;

	
        /**Creates the environment used to connect to the already existing database */
        public void createDatabaseEsistente(ConfigurazioneTO to);
}
