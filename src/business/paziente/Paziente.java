package business.paziente;

import java.util.ArrayList;

import messaggistica.MainException;
import integration.dao.AbstractDAO;
import integration.dao.DAOFactory;
import integration.dao.PazienteMySqlDAO;
import javax.swing.DefaultListModel;
import javax.swing.table.AbstractTableModel;


/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Entit&agrave degli pazienti, incapsula i dati relativi all'paziente
 * id, nome e cognome.<br>
 * Tramite il {@link DAOFactory} si collega al dao relativo all'paziente
 * {@link integration.dao.PazienteMySqlDAO}
*/
public class Paziente implements I_PazienteMod, I_PazienteGet{
	
	private DAOFactory daoFact;
	private AbstractDAO daoPaz;
	
	private String myName = "Paziente";
        
	/**
	 * Costruttore, recupera la {@link integration.dao.MySqlDAOFactory} dalla quale riceve 
	 * il dao appropriato
	 * @throws MainException se non trova la dao
	*/
	public Paziente() throws MainException{
		
		daoFact = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		
		//cattura la dao paziente
		daoPaz = daoFact.createDAO(myName);
		if(daoPaz==null){
			throw new MainException("DAO per "+myName+" non trovata.\n" +
					"Controllare le corrispondenze del file xml");
		}
	}
	
	
	
	
	/**
	 * Crea un nuovo paziente con i dati contenuti nel to
	 * @param to il transfer object del'pazienti con i dati
	 * @return true se l'inserimento dell'paziente ha successo, false altrimenti
	*/
        @Override
	public boolean createPaziente(PazienteTO to){
            /*asteriscata la parte dei controlli in quanto già implementata nell'interfaccia*/
		//if(checkDatiTO(to))
			return daoPaz.create(to);
		/*else{
			System.err.println("Paziente: Alcuni dati nel to passato sono nulli");
			return false;
		}*/
	}
	
	/**
	 * Restituisce un ADISysTabelModel con tutti i dati degli pazienti presenti nel database
	 * @return una struttura dati contenente i to di pazienti {@link PazienteTO}
	*/
        @Override
        public AbstractTableModel getTabella(){
		return daoPaz.getTabella();
	}
	
	
	/**
	 * Cancella l'paziente che ha quei dati passati dal tranfer object,
	 * @param pazTO il transfer object dell'paziente dal quale ottenere le pazormazioni,
	 * &egrave necessario che sia definito l'attributo id (gli altri non sono obbligatori)
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deletePaziente(PazienteTO pazTO){
            return daoPaz.delete(pazTO.getID());
	}
	
	
	public boolean modificaPaziente(PazienteTO pazTO){
            return daoPaz.update(pazTO);
        }
	
	/**
	 * Svuota l'intera tabella degli pazienti, usata per la sostituzione
	 * @return true se lo svuotamento ha successo, false altrimenti
	*/
        @Override
	public boolean reset(){
		return daoPaz.delete(-1);
	}
        
        @Override
        public DefaultListModel<String> getCellulari(int idPatient) {
                return ((PazienteMySqlDAO) daoPaz).getCellulari(idPatient);
        }
	
        @Override
        public String getPaziente(int idPatient) {
            return ((PazienteMySqlDAO) daoPaz).getSpecifiedWithPat(idPatient);
        }

    @Override
    public boolean alterTable(ArrayList<String> fieldsList) {
        return ((PazienteMySqlDAO) daoPaz).alterTable(fieldsList);
    }
    
    @Override
    public boolean alterTableCellulari(ArrayList<String> fieldsList) {
        return ((PazienteMySqlDAO) daoPaz).alterTableCellulari(fieldsList);
    }

    @Override
    public boolean exists(String name, String surname, Object[] cellulars) {
        return ((PazienteMySqlDAO) daoPaz).idEsistente(name, surname, cellulars);
    }

}
