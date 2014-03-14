package business.infermiere;

import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.ComboItem;
import java.util.ArrayList;

import business.TO;

import messaggistica.MainException;
import integration.dao.AbstractDAO;
import integration.dao.DAOFactory;
import integration.dao.InfermiereMySqlDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import presentation.FrontController;
import presentation.RequestManager;


/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Entit� degli infermieri, incapsula i dati relativi all'infermiere
 * id, nome e cognome.<br>
 * Tramite il {@link DAOFactory} si collega al dao relativo all'infermiere
 * {@link integration.dao.InfermiereMySqlDAO}
*/
public class Infermiere implements I_InfermiereMod, I_InfermieriGet{

	private DAOFactory daoFact;
	private AbstractDAO daoInf;
	
	private String myName = "Infermiere";
        
	/**
	 * Costruttore, recupera la {@link integration.dao.MySqlDAOFactory} dalla quale riceve 
	 * il dao appropriato
	 * @throws MainException se non trova la dao
	*/
	public Infermiere() throws MainException{
		
		daoFact = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		
		//cattura la dao infermiere
		daoInf = daoFact.createDAO(myName);
               
		if(daoInf==null){
			throw new MainException("DAO per "+myName+" non trovata.\n" +
					"Controllare le corrispondenze del file xml");
		}
		
	}
	
	
	
	
	/**
	 * Crea un nuovo infermiere con i dati contenuti nel to
	 * @param to il transfer object del'infermieri con i dati
	 * @return true se l'inserimento dell'infermiere ha successo, false altrimenti
	*/
        @Override
	public boolean createInfermiere(InfermiereTO to){
            /*asteriscata la parte dei controlli in quanto gi� implementata nell'interfaccia*/
		//if(checkDatiTO(to))
			return daoInf.create(to);
		/*else{
			System.err.println("Infermiere: Alcuni dati nel to passato sono nulli");
			return false;
		}*/
	}
		
	/**
	 * Restituisce tutti i dati degli infermieri presenti nel database
	 * @return una struttura dati contenente i to di infermieri {@link InfermiereTO}
	*/
        @Override
	public ArrayList<InfermiereTO> getDati(){
		ArrayList<TO> tos = daoInf.read();
		ArrayList<InfermiereTO> infTOs = new ArrayList<InfermiereTO>();
		for(TO to : tos){
			InfermiereTO infTO = (InfermiereTO) to;
			System.out.println(infTO.getID()+" ## "+infTO.getCognome()+" ## "+infTO.getNome());
			infTOs.add(infTO);
		}
		
		return infTOs;
	}
	
	/**
	 * Restituisce un ADISysTabelModel con tutti i dati degli infermieri presenti nel database
	 * @return una struttura dati contenente i to di infermieri {@link InfermiereTO}
	*/
        @Override
        public AbstractTableModel getTabella(){
		return daoInf.getTabella();
	}
	
	
	/**
	 * Cancella l'infermiere che ha quei dati passati dal tranfer object,
	 * @param infTO il transfer object dell'infermiere dal quale ottenere le informazioni,
	 * &egrave necessario che sia definito l'attributo id (gli altri non sono obbligatori)
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
        @Override
	public boolean deleteInfermiere(InfermiereTO infTO){
            return daoInf.delete(infTO.getID());
	}
	
	
        @Override
	public boolean modificaInfermiere(InfermiereTO infTO){
            return daoInf.update(infTO);
        }
	
	/**
	 * Svuota l'intera tabella degli infermieri, usata per la sostituzione
	 * @return true se lo svuotamento ha successo, false altrimenti
	*/
        @Override
	public boolean reset(){
		return daoInf.delete(-1);
	}

    @Override
    public ArrayList<ComboItem> getArrayInfermieri() {
         FrontController FC = RequestManager.getFCInstance();
         ADISysTableModel v = null;
            try {
                v = (ADISysTableModel) FC.processRequest("visualizzaInfermieri", null);
            } catch (MainException ex) {
                Logger.getLogger(Infermiere.class.getName()).log(Level.SEVERE, null, ex);
            }
	int colonnaID = v.findColumn(InfermiereMySqlDAO.COLUMN_ID_NAME);
        int numRows = v.getRowCount();
        int columnRows = v.getColumnCount();
        String id = "";
        String labelInfermiere = "";
        ArrayList<ComboItem> listInfermieri = new ArrayList<>();
        
        for(int i = 0; i < numRows; i++){
        	for(int k = 0; k < columnRows; k++){
        		if(k != colonnaID)
        			labelInfermiere = labelInfermiere + v.getValueAt(i, k).toString() + " ";
        		else 
        			id = v.getValueAt(i, k).toString();
        	}
        	listInfermieri.add(new ComboItem(id, labelInfermiere));
        	labelInfermiere = "";
        }
		return listInfermieri;
    }

    @Override
    public InfermiereTO getInfermiere(int idInfermiere) {
        return (InfermiereTO) daoInf.getSpecified(idInfermiere);
    }

    @Override
    public AbstractTableModel getInfermieriConInterventi() {
        return ((InfermiereMySqlDAO) daoInf).getTabellaInfoInfermieriConInterventi();
    }

    @Override
    public boolean exists(int idInfermiere) {
        return ((InfermiereMySqlDAO) daoInf).existingID(idInfermiere);
    }

    @Override
    public boolean alterTable(ArrayList<String> campi) {
        return ((InfermiereMySqlDAO) daoInf).alterTable(campi);
    }



}
