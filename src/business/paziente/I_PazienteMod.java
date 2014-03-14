package business.paziente;

import java.util.ArrayList;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia per la entity {@link Paziente}, offre i metodi di creazione di un paziente
 * e la cancellazione di tutti i pazienti
*/
public interface I_PazienteMod {
	
	
	/**
	 * Crea un nuovo paziente con i dati contenuti nel to
	 * @param to il transfer object del'infermieri con i dati
	 * @return true se l'inserimento dell'infermiere ha successo, false altrimenti
	*/
	public boolean createPaziente(PazienteTO to);
	
        /**
	 * Elimina il paziente con i dati contenuti nel to
	 * @param to il transfer object del'infermieri con i dati
	 * @return true se l'inserimento dell'infermiere ha successo, false altrimenti
	*/
        public boolean deletePaziente(PazienteTO to);
        
        /**
	 * Modifica il paziente con i dati contenuti nel to
	 * @param to il transfer object del'infermieri con i dati
	 * @return true se l'inserimento dell'infermiere ha successo, false altrimenti
	*/
        public boolean modificaPaziente(PazienteTO to);
	/**
	 * Svuota l'intera tabella dei pazienti
	*/
	public boolean reset();
        
        public boolean alterTable(ArrayList<String> listaCampi);
        
        public boolean alterTableCellulari(ArrayList<String> listaCampi);
}
