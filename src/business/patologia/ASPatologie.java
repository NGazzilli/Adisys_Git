package business.patologia;

import business.TO;
import java.util.ArrayList;

import messaggistica.MainException;
import java.sql.ResultSet;
import javax.swing.table.AbstractTableModel;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Gestisce le funzionalità sulle patologie: la visualizzazione(che ritorna una
 * struttura dati di {@link PatologiaTO})
*/
public class ASPatologie extends business.ServiceHandler{

	
	/**
	 * Costruttore, come ogni costruttore delle altre classi di servizio, inizializza
	 * la hashmap con le corrispondenze nome funzionalit&agrave -> metodo
	*/
	public ASPatologie(){
		
		map.put("visualizzaPatologie", "getTabellaPatologie");
                map.put("visualizzaListaPatologie", "getListaPatologie");
                map.put("creaPatologia", "creaPatologia");
                map.put("modificaPatologia", "modificaPatologia");
                map.put("cancellaPatologia", "deletePatologia");
                map.put("cancellaTuttePatologie", "deleteAll");
                map.put("visualizzaPatologieAssociate", "getPatologieAssociate");
                map.put("modificaTabellaPatologie", "alterTable");
	}
	
	/**
	 * Istanza della entity dell'patologia {@link Patologia} su cui ASPatologie lavora 
	*/
	private I_PatologieGet pat;
        private I_PatologiaMod patologia;
	
      	/**
	 * Restituisce una struttura dati dei to degli patologie presenti nel database
	 * @return un array dei dati degli patologie incapsulati nei transfer object
	*/
	public AbstractTableModel getTabellaPatologie(){
		try {
			pat = new Patologia();
			return pat.getTabella();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}

              	/**
	 * Restituisce una struttura dati dei to delle patologie presenti nel database
	 * @return un array dei dati degli patologie incapsulati nei transfer object
	*/
	public ArrayList<TO> getListaPatologie(){
		try {
			pat = new Patologia();
			return pat.getArrayPatologie();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}

        
        public ResultSet getPatologieAssociate(PatologiaTO to){
            try {
			pat = new Patologia();
			return pat.getTabellaPatologieAssociate(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}
        }
        
        public boolean alterTable(ArrayList<String> listaCampi){
            
		try {
			patologia = new Patologia();
			return patologia.alterTable(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
        public boolean creaPatologia(PatologiaTO to){
            	try {
			
			patologia = new Patologia();
			return patologia.createPatologia(to);
			
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
        }
	
        /**
	 * Cancella l'patologia che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deletePatologia(PatologiaTO to){
		
		try {
			patologia = new Patologia();
			return patologia.deletePatologia(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Modifica l'patologia che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean modificaPatologia(PatologiaTO to){
		
		try {
			patologia = new Patologia();
			return patologia.modificaPatologia(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Cancella l'patologia che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deleteAll(){
		
		try {
			patologia = new Patologia();
			return patologia.reset();
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
}
