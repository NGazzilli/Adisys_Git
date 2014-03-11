package business.patologia;

import business.TO;
import messaggistica.MainException;
import integration.dao.AbstractDAO;
import integration.dao.DAOFactory;
import integration.dao.PatologiaMySqlDAO;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * Entit&agrave degli patologie, incapsula i dati relativi all'patologia
 * id, codice e nome.<br>
 * Tramite il {@link DAOFactory} si collega al dao relativo all'patologia
 * {@link integration.dao.PatologiaMySqlDAO}
*/
public class Patologia implements I_PatologiaMod, I_PatologieGet{
	
	private DAOFactory daoFact;
	private AbstractDAO daoPat;
	
	private String myName = "Patologia";
        
	/**
	 * Costruttore, recupera la {@link integration.dao.MySqlDAOFactory} dalla quale riceve 
	 * il dao appropriato
	 * @throws MainException se non trova la dao
	*/
	public Patologia() throws MainException{
		
		daoFact = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		
		//cattura la dao patologia
		daoPat = daoFact.createDAO(myName);                
		if(daoPat==null){
			throw new MainException("DAO per "+myName+" non trovata.\n" +
					"Controllare le corrispondenze del file xml");
		}
	}

	/**
	 * Crea una nuova patologia con i dati contenuti nel to
	 * @param to il transfer object del'patologie con i dati
	 * @return true se l'inserimento dell'patologia ha successo, false altrimenti
	*/
        @Override
	public boolean createPatologia(PatologiaTO to){
            /*asteriscata la parte dei controlli in quanto già implementata nell'interfaccia*/
		//if(checkDatiTO(to))
			return daoPat.create(to);
		/*else{
			System.err.println("Patologia: Alcuni dati nel to passato sono nulli");
			return false;
		}*/
	}
		
	/**
	 * Restituisce un ADISysTabelModel con tutti i dati delle patologie presenti nel database
	 * @return una struttura dati contenente i to di patologie {@link PatologiaTO}
	*/
        @Override
        public AbstractTableModel getTabella(){
		return daoPat.getTabella();
	}
	
	
	/**
	 * Cancella l'patologia che ha quei dati passati dal tranfer object,
	 * @param patTO il transfer object dell'patologia dal quale ottenere le patormazioni,
	 * &egrave necessario che sia definito l'attributo id (gli altri non sono obbligatori)
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	public boolean deletePatologia(PatologiaTO patTO){
            return daoPat.delete(patTO.getID());
	}
	
	
	public boolean modificaPatologia(PatologiaTO patTO){
            return daoPat.update(patTO);
        }
	
	/**
	 * Svuota l'intera tabella degli patologie, usata per la sostituzione
	 * @return true se lo svuotamento ha successo, false altrimenti
	*/
        @Override
	public boolean reset(){
		return daoPat.delete(-1);
	}

    @Override
    public ResultSet getTabellaPatologieAssociate(int idPatologia) {
        return ((PatologiaMySqlDAO) daoPat).getTabellaPatologieAssociate(idPatologia);
    }

    @Override
    public ArrayList<TO> getArrayPatologie() {
        return daoPat.read();
    }

   @Override
    public boolean alterTable(ArrayList<String> campi) {
        return ((PatologiaMySqlDAO) daoPat).alterTable(campi);
    }

}
