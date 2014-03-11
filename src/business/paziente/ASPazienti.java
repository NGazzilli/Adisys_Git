package business.paziente;

import messaggistica.MainException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.table.AbstractTableModel;


/**
 * Gestisce le funzionalit&agrave sugli pazienti: la visualizzazione(che ritorna una
 * struttura dati di {@link PazienteTO})
*/
public class ASPazienti extends business.ServiceHandler{

	
	
	
	/**
	 * Costruttore, come ogni costruttore delle altre classi di servizio, inizializza
	 * la hashmap con le corrispondenze nome funzionalit&agrave -> metodo
	*/
	public ASPazienti(){
		
		map.put("visualizzaPazienti", "getTabellaPazienti");
                map.put("creaPaziente", "creaPaziente");
                map.put("modificaPaziente", "modificaPaziente");
                map.put("cancellaPaziente", "deletePaziente");
                map.put("cancellaTuttiPazienti", "deleteAll");
                map.put("visualizzaNumeriTelefono", "getCellulariPaziente");
                map.put("visualizzaPaziente", "getPaziente");
                map.put("modificaTabellaPazienti", "alterTable");
                map.put("modificaTabellaCellulari", "alterTableCellulari");
                map.put("pazienteEsistente", "isExists");
	}
	
	/**
	 * Istanza della entity dell'paziente {@link Paziente} su cui ASPazienti lavora 
	*/
	private I_PazienteGet paz;
        private I_PazienteMod paziente;
	
        /**
	 * Restituisce una struttura dati dei to degli pazienti presenti nel database
	 * @return un array dei dati degli pazienti incapsulati nei transfer object
	*/
	public AbstractTableModel getTabellaPazienti(){
		try {
			paz = new Paziente();
			return paz.getTabella();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
        
        public boolean alterTable(ArrayList<String> listaCampi){
            
		try {
			paziente = new Paziente();
			return paziente.alterTable(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
        
        public boolean alterTableCellulari(ArrayList<String> listaCampi){
            
		try {
			paziente = new Paziente();
			return paziente.alterTableCellulari(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }

        
        public boolean creaPaziente(PazienteTO to){
            	try {
			
			paziente = new Paziente();
			return paziente.createPaziente(to);
			
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
        }
	
        /**
	 * Cancella l'paziente che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deletePaziente(PazienteTO to){
		
		try {
			paziente = new Paziente();
			return paziente.deletePaziente(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Modifica l'paziente che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean modificaPaziente(PazienteTO to){
		
		try {
			paziente = new Paziente();
			return paziente.modificaPaziente(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Cancella l'paziente che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deleteAll(){
		
		try {
			paziente = new Paziente();
			return paziente.reset();
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        public DefaultListModel<String> getCellulariPaziente(PazienteTO to){
           try {
			paz = new Paziente();
			return paz.getCellulari(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		} 
        }
        
        public String getPaziente(PazienteTO to){
           try {
			paz = new Paziente();
			return paz.getPaziente(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		} 
        }
        
        public boolean isExists(PazienteTO to){
            try {
			paz = new Paziente();
                        return paz.exists(to.getNome(), to.getCognome(), to.getCellulari());
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}
        }
        
}
