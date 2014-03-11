package business.configurazione;

import messaggistica.MainException;


/**
 * Gestore per la configurazione. Permette la lettura e l'inserimento dei 
 * dati relativi alla configurazione.
*/
public class ASConfigurazione extends business.ServiceHandler{
	
	
	
	/**
	 * Inizializza la {@code #map} con le corrispondenze nome funzionalit&agrave -> metodo.<br>
	 * 
	 * @throws MainException il file di configurazione non esiste, non può essere
	 * letto oppure non può essere modificato
	*/
	public ASConfigurazione() throws MainException{
		System.out.println("Costruttore di ASConfigurazione");
		config = new Configurazione();
		
		map.put("getDatiConfigurazione", "getDati");
		map.put("setDatiConfigurazione", "setDati");
	}
	
	
	
	
	/**
	 * Istanza della classe entity {@link Configurazione} su cui ASConfigurazione lavora
	*/
	private I_Configurazione config;
	
	
	
	
	
	
	/**
	 * Restituisce tutti i dati di configurazione nel Transfer Object {@link ConfigurazioneTO}.
	 * Utile quando si vuole modificare la configurazione e si vogliono mostrare i valori
	 * attualmente memorizzati sul file di configurazione
	 * 
	 * @return il transfer object contenente tutti i dati di configurazione, <i>null</i> se &egrave
	 * stata lanciata un eccezione a livello di entity (errore di formato nel file o non esiste)
	*/
	public ConfigurazioneTO getDati(){
		System.out.println("ASConfigurazione.getDati()");
		ConfigurazioneTO to = config.getDati();
		if(to==null){
			System.err.println("errore di formato nel file o non esiste");
		}
		return to;
	}
	
	
	
	
	
	/**
	 * Setta i nuovi dati prelevati dal to passato per parametro
	 * @param to il transfer object con i dati da inserire
	 * @return true se l'inserimento ha avuto successo, false altrimenti
	 * (lanciata un eccezione a livello di entity: dati nel to incompleti)
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
