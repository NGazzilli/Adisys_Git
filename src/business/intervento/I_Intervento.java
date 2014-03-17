package business.intervento;

import business.patologia.PatologiaTO;
import integration.dao.InterventoMySqlDAO;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Interfaccia della entity {@link Intervento} offre i metodi di creazione e cancellazione
 * di un singolo intervento e restituzione di tutti gli interventi memorizzati.
*/
public interface I_Intervento {
	
	/**
	 * Crea un nuovo intervento con i dati dell'intervento e del relativo paziente.
	 * Usa InterventoMySqlDAO per aggiungere intervento paziente e la corrispondenza
	 * @param to il transfer object InterventoPazienteTO che contiene i to del paziente e 
	 * dell'intervento
	 * @return true se la creazione ha successo, false altrimenti
	*/
	public boolean createIntervento(InterventoTO to);
	
	
	
	/**
	 * Cancella l'intervento corrispondente al to passato per parametro
	 * @param to il tranfer object dell'intervento InterventoPazienteTO
	 * @return true se la cancellazione ha succeso, false altrimenti
	*/
	public boolean deleteIntervento(InterventoTO to);

	public boolean modificaIntervento(InterventoTO to);
  
        public AbstractTableModel getTabella();
        
        public AbstractTableModel getTabellaInterventiInfermiere(int id, String dataDal, String dataAl);
        
        public ArrayList<InterventoTO> getListaInterventiInfermiere(int idNurse);
        
        public int verificaIntervento(int id, String startDate, String time);
        
        public ArrayList<TipoIntervento> leggiTipiIntervento(int idIntervention);
        
        public ArrayList<PatologiaTO> leggiPatologieIntervento(int idIntervention);
        
        /**
	 * Svuota l'intera tabella degli infermieri, usata per la sostituzione
	*/
	public boolean reset();
        
        public boolean alterTable(ArrayList<String> fieldsList);
        
        public boolean alterTablePatologieIntervento(ArrayList<String> fieldsList);
        
        public boolean alterTableTipiIntervento(ArrayList<String> fieldsList);
        
        public void addIntStorico(int idIntervention);

        public AbstractTableModel getStorico(int id);
    
        public int contaInterventi(int id);
        
}
