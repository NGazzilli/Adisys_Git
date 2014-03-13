package business.configurazione;

import messaggistica.MainException;


/**
 * Setting manager. It allows reading and insertion of settings-related data.
*/
public class ASConfigurazione extends business.ServiceHandler{
	
	
	
	/**Initializes the {@code #map} with correspondences functionalityName -> method <br>
	 * @throws MainException configuration file doesn't exist, cannot be read or cannot be edited	*/
	public ASConfigurazione() throws MainException{
		System.out.println("Costruttore di ASConfigurazione");
		config = new Configurazione();
		
		map.put("getDatiConfigurazione", "getDati");
		map.put("setDatiConfigurazione", "setDati");
	}
	
	
	
	
	/**
	 * Instance of entity class {@link Configurazione} on which ASConfigurazione works
	*/
	private I_Configurazione config;
	
	
	
	
	
	
	/**
	 * Returns all configuration data in the Transfer Object {@link ConfigurazioneTO}.
	 * Useful when there is need to edit configuration and to show vales stored in the configuration file.
	 * @return the transfer object containing all the configuration data, <i>null</i> if it
	 * has been thrown an entity-level exception. (format error in file or it doesn't exist)*/
	public ConfigurazioneTO getDati(){
		System.out.println("ASConfigurazione.getDati()");
		ConfigurazioneTO to = config.getDati();
		if(to==null){
			System.err.println("errore di formato nel file o non esiste");
		}
		return to;
	}
	
	
	
	
	
	/**
	 * Sets the new data fetched from the Transfer Object passed by parameter
	 * @param to the TO with data to transfer
	 * @return true if insertion has been successful, false otherwise
	*/
	public boolean setDati(ConfigurazioneTO to){
		System.out.println("ASConfigurazione.setDati("+to+")");
		try {
			if(config.setAllDati(to)){
					config.createDatabaseEsistente(to);
                                        return true;
			}
			else{
				return false;
			}
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

}
