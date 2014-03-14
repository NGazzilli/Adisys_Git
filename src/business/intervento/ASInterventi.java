package business.intervento;

import adisys.server.strumenti.DateFormatConverter;
import business.infermiere.InfermiereTO;
import business.patologia.PatologiaTO;
import java.util.ArrayList;

import messaggistica.MainException;
import javax.swing.table.AbstractTableModel;



/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Gestisce le funzionalità sugli interventi
 *  
 * Tutte le funzionalità avvengono mediante trasferimento dei tranfer object 
 * {@link InterventoTO}
*/
public class ASInterventi extends business.ServiceHandler{
	
	
	/**
	 * Costruttore, come ogni costruttore delle altre classi di servizio, inizializza
	 * la hashmap con le corrispondenze nome funzionalit&agrave -> metodo
	*/
	public ASInterventi(){
		
		map.put("visualizzaInterventi", "getTabellaInterventi");
		map.put("creaIntervento", "createIntervento");
		map.put("cancellaIntervento", "deleteIntervento");
                map.put("cancellaTuttiInterventi", "deleteAll");
                map.put("modificaIntervento", "modificaIntervento");
                map.put("visualizzaTabellaInterventiInfermiere", "getTabellaInterventiInfermiere");
                map.put("visualizzaTipiIntervento", "getListaTipiIntervento");
                map.put("visualizzaPatologieIntervento", "getListaPatologieIntervento");
                map.put("verificaValiditaIntervento", "verificaValidita");
                map.put("visualizzaIntervento", "getIntervento");
                map.put("visualizzaListaInterventiInfermiere", "getListaInterventiInfermiere");
                map.put("modificaTabellaPatologieIntervento", "alterTablePatologieIntervento");
                map.put("modificaTabellaTipiIntervento", "alterTableTipiIntervento");
                map.put("modificaTabellaInterventi", "alterTable");
                map.put("aggiornaStorico", "aggiornaStorico");
                map.put("visualizzaStorico", "getStorico");
                map.put("contaInterventi", "contaInterventi");
	}
	
	
	/**
	 * Istanza della entity dell'intervento {@link Intervento} su cui ASInterventi lavora 
	*/
	private I_Intervento interv;
        private I_InterventoSpecifico intSpec;
        
        /**
	 * Restituisce una struttura dati dei to degli interventi presenti nel database
	 * @return un array dei dati degli interventi incapsulati nei transfer object
	*/
	public AbstractTableModel getTabellaInterventi(){
		try {
			interv = new Intervento();
			return interv.getTabella();
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
        
        public AbstractTableModel getTabellaInterventiInfermiere(Integer id, String dataDal, String dataAl){
		try {
			interv = new Intervento();
			return interv.getTabellaInterventiInfermiere(id, dataDal, dataAl);
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
        
        public ArrayList<TipoIntervento> getListaTipiIntervento(InterventoTO to){
            try {
			interv = new Intervento();
			return interv.leggiTipiIntervento(to.getID());
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
        }
        
        public ArrayList<PatologiaTO> getListaPatologieIntervento(InterventoTO to){
            try {
			interv = new Intervento();
			return interv.leggiPatologieIntervento(to.getID());
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
        }
        
        public InterventoTO getIntervento(InterventoTO to){
            try {
			intSpec = new Intervento();
			return intSpec.getSpecificIntervento(to.getID());
			
		} catch (MainException e) {
			e.printStackTrace();
			return null;
		}	
        }
        
         public boolean alterTable(ArrayList<String> listaCampi){
            
		try {
			interv = new Intervento();
			return interv.alterTable(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
         
        public boolean alterTableTipiIntervento(ArrayList<String> listaCampi){
            
		try {
			interv = new Intervento();
			return interv.alterTableTipiIntervento(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
        
        public boolean alterTablePatologieIntervento(ArrayList<String> listaCampi){
            
		try {
			interv = new Intervento();
			return interv.alterTablePatologieIntervento(listaCampi);
		} catch (MainException e) {
			e.printStackTrace();
                        return false;
		}	
        }
        

	/**
	 * Crea un nuovo intervento con i dati passati attraverso il to
	 * @return true se la creazione ha successo, false altrimenti
	*/
	public boolean createIntervento(InterventoTO to){
		try {
			
			interv = new Intervento();
			return interv.createIntervento(to);
			
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
		
	/**
	 * Cancella l'intevento che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deleteIntervento(InterventoTO to){
		
		try {
			interv = new Intervento();
			return interv.deleteIntervento(to);
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	 /**
	 * Modifica l'infermiere che ha i dati passati tramite il to
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean modificaIntervento(InterventoTO to){
		
		try {
			interv = new Intervento();
			return interv.modificaIntervento(to);
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
			interv = new Intervento();
			return interv.reset();
		} catch (MainException e) {
			e.printStackTrace();
			return false;
		}
		
	}
        
        public int verificaValidita(InterventoTO to){
           	try {
			interv = new Intervento();
			return interv.verificaIntervento(to.getIDInfermiere(), to.getDataDaFormato(DateFormatConverter.getFormatData()), 
                                to.getOraInizioDaFormato(DateFormatConverter.getFormatOra()));
		} catch (MainException e) {
			e.printStackTrace();
                        //se non ha avuto successo ritorna un qualsiasi valore non compreso fra -1 e 1
                        return 2;
		} 
        }
               
        public ArrayList<InterventoTO> getListaInterventiInfermiere(InfermiereTO to){
            	try {
			interv = new Intervento();
                        return interv.getListaInterventiInfermiere(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
                        return null;
		}
        }
        
        public void aggiornaStorico(InterventoCompletoTO to){
                try {
			interv = new Intervento();
                        interv.addIntStorico(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
		}
        }
        
        public AbstractTableModel getStorico(InfermiereTO to){
                try {
			interv = new Intervento();
                        return interv.getStorico(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
                        return null;
		}
        }
        
        public int contaInterventi(InfermiereTO to){
                try {
			interv = new Intervento();
                        return interv.contaInterventi(to.getID());
		} catch (MainException e) {
			e.printStackTrace();
                        return -1;
		}
        }
        
}
