package business.patologia;

import business.infermiere.*;
import integration.dao.PatologiaMySqlDAO;
import java.util.ArrayList;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Interfaccia per la entity {@link Patologia}, offre i metodi di creazione di un infermiere
 * e la cancellazione di tutti gli infermieri
*/
public interface I_PatologiaMod {
	
	
	/**
	 * Crea un nuovo infermiere con i dati contenuti nel to
	 * @param to il transfer object del'infermieri con i dati
	 * @return true se l'inserimento dell'infermiere ha successo, false altrimenti
	*/
	public boolean createPatologia(PatologiaTO to);
	
        public boolean deletePatologia(PatologiaTO to);
        
        public boolean modificaPatologia(PatologiaTO to);
	/**
	 * Svuota l'intera tabella degli infermieri, usata per la sostituzione
	*/
	public boolean reset();
        
        public boolean alterTable(ArrayList<String> fieldsList);
        
}
