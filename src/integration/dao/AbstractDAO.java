package integration.dao;

import adisys.server.strumenti.ADISysTableModel;
import integration.I_LinkingDb;

import java.util.ArrayList;

import business.TO;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Il CRUD di tutte le DAO.<br>
 * Ogni dao estende questa classe. Il cliente della struttura usa questa interfaccia
 * per "dialogare" con le dao, deve comunque conoscere il Tranfer Object associato
 * al dao.
*/
public abstract class AbstractDAO {
	
	/**
	 * Per ogni dao crea un record diverso della tabella
	*/
	public abstract boolean create(TO to);
	
	/**
	 * Per ogni dao legge i record della tabella a cui si riferiscono
	*/
	public abstract ArrayList<TO> read();
        
        /**
	 * Per ogni dao modifica il relativo record
	*/
        public abstract boolean update(TO to);
        
        /**
	 * Per ogni dao legge i record della tabella a cui si riferiscono
	*/
	public abstract ADISysTableModel getTabella();
        
        public abstract TO getSpecified(int id);
 
	/**
	 * Per ogni dao cancella i record della tabella con l'id in input
	*/
	public abstract boolean delete(int id);
	
	/**
	 * Variabile statica definita in {@link AbstractDAO}
	*/
	protected static I_LinkingDb linkDb;
	
	/**
	 * Avvia una nuova connessione la database
	*/
	public static I_LinkingDb connectToDB(){
		
		linkDb.connect();
		
		return linkDb;
		
	}
	
	
	/**
	 * Metodo che viene chiamato nel caso di nuova configurazione.
	 * Le DAO perdono la loro connessione per crearne un'altra.
	 * Altrimenti si continua ad essere collegati al database della vecchia configurazione
	*/
	public static void disconnectToDB(){
		
		if(linkDb!=null){
			linkDb.disconnect();
		}
		
		linkDb = null;
	}
	
}
