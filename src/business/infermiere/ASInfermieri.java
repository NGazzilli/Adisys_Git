package business.infermiere;

import adisys.server.strumenti.ComboItem;
import java.util.ArrayList;

import messaggistica.MainException;
import javax.swing.table.AbstractTableModel;


/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Gestisce le funzionalità sugli infermieri: la visualizzazione(che ritorna una
 * struttura dati di {@link InfermiereTO})
*/
public class ASInfermieri extends business.ServiceHandler{
	
	/**
	 * Costruttore, come ogni costruttore delle altre classi di servizio, inizializza
	 * la hashmap con le corrispondenze nome funzionalit&agrave -> metodo
	*/
	public ASInfermieri(){
		
		map.put("visualizzaInfermieri", "getTabellaInfermieri");
                map.put("creaInfermiere", "creaInfermiere");
                map.put("modificaInfermiere", "modificaInfermiere");
                map.put("cancellaInfermiere", "deleteInfermiere");
                map.put("cancellaTuttiInfermieri", "deleteAll");
                map.put("visualizzaElenco", "getElencoInfermieri");
                map.put("visualizzaInfermiere", "getInfermiere");
                map.put("visualizzaInfermieriConInterventi", "getInfermieriConInterventi");
                map.put("infermiereEsistente", "isExists");
                map.put("modificaTabellaInfermieri", "alterTable");
                map.put("contaInterventi", "contaInterventi");
	}
	
	/**
	 * Istanza della entity dell'infermiere {@link Infermiere} su cui ASInfermieri lavora 
	*/
	private I_InfermieriGet inf;
    private I_InfermiereMod nurse;
	
	/**
	 * Restituisce una struttura dati dei to degli infermieri presenti nel database
	 * @return un array dei dati degli infermieri incapsulati nei transfer object
	*/
	public ArrayList<InfermiereTO> getInfermieri(){
		try {
			inf = new Infermiere();
			return inf.getDati();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
       /**
	 * Restituisce una struttura dati dei to degli infermieri presenti nel database
	 * @return un array dei dati degli infermieri incapsulati nei transfer object
	*/
	public AbstractTableModel getTabellaInfermieri(){
		try {
			inf = new Infermiere();
			return inf.getTabella();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
        
        public boolean alterTable(ArrayList<String> fieldList){
            
		try {
			nurse = new Infermiere();
			return nurse.alterTable(fieldList);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
        
        /**
	 * Restituisce una struttura dati dei to degli infermieri presenti nel database
	 * @return un array dei dati degli infermieri incapsulati nei transfer object
	*/
	public ArrayList<ComboItem> getElencoInfermieri(){
		try {
			inf = new Infermiere();
			return inf.getArrayInfermieri();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
        
	public InfermiereTO getInfermiere(InfermiereTO to){
		try {
			inf = new Infermiere();
			return inf.getInfermiere(to.getID());
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
        
        public boolean creaInfermiere(InfermiereTO to){
            	try {
			
            		nurse = new Infermiere();
			return nurse.createInfermiere(to);
			
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
        }
	
        /**
	 * Cancella l'infermiere che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deleteInfermiere(InfermiereTO to){
		
		try {
			nurse = new Infermiere();
			return nurse.deleteInfermiere(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Modifica l'infermiere che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean modificaInfermiere(InfermiereTO to){
		
		try {
			nurse = new Infermiere();
			return nurse.modificaInfermiere(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        /**
	 * Cancella l'infermiere che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deleteAll(){
		
		try {
			nurse = new Infermiere();
			return nurse.reset();
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}

        public AbstractTableModel getInfermieriConInterventi(){
            	try {
			inf = new Infermiere();
                        return inf.getInfermieriConInterventi();
		} catch (MainException e) {
			e.printStackTrace();
                        return null;
		}
		
        }
        
        public boolean isExists(InfermiereTO to){
            try {
			inf = new Infermiere();
                        return inf.exists(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}
        }
        
}
