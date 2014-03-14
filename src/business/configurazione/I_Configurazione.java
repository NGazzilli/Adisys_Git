package business.configurazione;

import messaggistica.MainException;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Interfaccia di {@link Configurazione} per la configurazione completa,
 * usata dal gestore di configurazione
*/
public interface I_Configurazione {
	
	/**
	 * Restituisce il Transfer Object di Configurazione che incapsula tutti i dati.
	 * Il metodo sovrascrive le variabili d'istanza relative ai campi.
	 * Potrebbe essere usata quando si vuole cambiare la configurazione
	 * @return il transfer object con tutti i dati contenuti nel file di configurazione 
	 * o null se c'&egrave un errore di formato nel file o non esiste
	*/
	public ConfigurazioneTO getDati();
	
	/**
	 * Imposta i valori contenuti nel tranfer object nelle variabili d'istanza e li memorizza
	 * nel file di configurazione (se non esiste, lo crea).
	 * 
	 * @param to il transfer object dove prelevare i valori
	 * @return true se la memorizzazione ha successo, false se fallisce
	 * @throws MainException Valori incompleti nel transfer object
	*/
	public boolean setAllDati(ConfigurazioneTO to) throws MainException;

	/**Crea l'ambiente necessario per la connessione al database se già esitente*/
        public void createDatabaseEsistente(ConfigurazioneTO to);
}
