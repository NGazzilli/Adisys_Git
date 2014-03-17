package business.intervento;

import java.util.ArrayList;

import business.patologia.PatologiaTO;

import messaggistica.MainException;

import integration.dao.DAOFactory;
import integration.dao.AbstractDAO;
import integration.dao.InterventoMySqlDAO;
import javax.swing.table.AbstractTableModel;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Classe entity che incapsula i dati relativi al singolo intervento, compresi i dati
 * del paziente che riceve l'intervento.
 * Tramite il {@link DAOFactory} si collega a {@link integration.dao.InterventoMySqlDAO}.
 * Riceve e invia i dati attraverso il transfer object {@link InterventoTO}
*/
public class Intervento implements I_Intervento, I_InterventoSpecifico {
	
	private DAOFactory daoFact;
	private AbstractDAO daoFullInt;
	
        private String myName = "Intervento";
	
	/**
	 * Costruttore, recupera la {@link integration.dao.MySqlDAOFactory} dalla quale riceve 
	 * il dao appropriato
	 * @throws MainException se non trova la dao
	*/
	public Intervento() throws MainException{
		
		daoFact = DAOFactory.getDAOFactory(DAOFactory.MYSQL);//cattura del mysql factory
		
		//-------------- cattura del dao interventopaziente ----------------
		daoFullInt = daoFact.createDAO(myName);
		if(daoFullInt==null){
			throw new MainException("DAO " + myName + " non trovata");
		}
	}
	
	
	
	
	/**
	 * Crea un nuovo intervento con i dati dell'intervento e del relativo paziente.
	 * Usa InterventoMySqlDAO per aggiungere intervento paziente e la corrispondenza
	 * @param to il transfer object InterventoTO che contiene i to del paziente e 
	 * dell'intervento
	 * @return true se la creazione ha successo, false altrimenti
	*/
	public boolean createIntervento(InterventoTO to){
		return daoFullInt.create(to);
	}
	
	
	/**
	 * Cancella l'intervento corrispondente al to passato per parametro
	 * @param to il tranfer object dell'intervento InterventoTO
	 * @return true se la cancellazione ha succeso, false altrimenti
	*/
	public boolean deleteIntervento(InterventoTO to){
		
		return daoFullInt.delete(to.getID());
		
	}
	
	
	/*
	 * Query per associare intervento e paziente facendo venire tutti i dati di intervento e
	 * paziente.
	 * select interventopaziente.id_intervento, dataOra, tipo, noteInt, interventopaziente.id_paziente,
	   nome, cognome, eta, citta, civico, cellulare
	   from intervento join interventopaziente join paziente
	   where intervento.id_intervento=interventopaziente.id_intervento and 
	   paziente.id_paziente=interventopaziente.id_paziente;
	*/
	
    @Override
    public InterventoTO getSpecificIntervento(int id_intervento){
        return (InterventoTO) daoFullInt.getSpecified(id_intervento);
    }

    @Override
    public boolean modificaIntervento(InterventoTO to) {
        return daoFullInt.update(to);
    }


    @Override
    public AbstractTableModel getTabella() {
        return daoFullInt.getTabella();
    }

    @Override
    public boolean reset() {
        return daoFullInt.delete(-1);
    }

    @Override
    public AbstractTableModel getTabellaInterventiInfermiere(int id, String dataDal, String dataAl) {
        return ((InterventoMySqlDAO) daoFullInt).getTabellaInterventiInfermiere(id, dataDal, dataAl);
    }

    @Override
    public int verificaIntervento(int id, String dataInit, String ora) {
        return ((InterventoMySqlDAO) daoFullInt).verificaOraIntervento(String.valueOf(id), dataInit, ora);
    }

    @Override
    public ArrayList<TipoIntervento> leggiTipiIntervento(int idIntervento) {
        return ((InterventoMySqlDAO) daoFullInt).readInterventionsTypes(idIntervento);
    }

    @Override
    public ArrayList<PatologiaTO> leggiPatologieIntervento(int idIntervento) {
        return ((InterventoMySqlDAO) daoFullInt).leggiPatologieIntervento(idIntervento);
    }

    @Override
    public ArrayList<InterventoTO> getListaInterventiInfermiere(int idInfermiere) {
        return ((InterventoMySqlDAO) daoFullInt).getListaInterventiInfermiere(idInfermiere);
    }

    @Override
    public boolean alterTable(ArrayList<String> listaCampi) {
        return ((InterventoMySqlDAO) daoFullInt).alterTableInterventi(listaCampi);
    }

    @Override
    public boolean alterTablePatologieIntervento(ArrayList<String> listaCampi) {
        return ((InterventoMySqlDAO) daoFullInt).alterTablePatTipiInterventi(listaCampi);
    }

    @Override
    public boolean alterTableTipiIntervento(ArrayList<String> listaCampi) {
        return ((InterventoMySqlDAO) daoFullInt).alterTableTipiIntervento(listaCampi);
    }

    @Override
    public void addIntStorico(int idIntervento) {
        ((InterventoMySqlDAO) daoFullInt).addIntStorico(idIntervento);
    }

    @Override
    public AbstractTableModel getStorico(int id) {
        return ((InterventoMySqlDAO) daoFullInt).getStorico(id);
    }

   @Override
    public int contaInterventi(int id) {
        return ((InterventoMySqlDAO) daoFullInt).contaInterventi(id);
    }
}
