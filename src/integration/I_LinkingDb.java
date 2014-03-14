package integration;

import adisys.server.strumenti.ADISysTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia della classe di collegamento al database {@link LinkingDb}
*/
public interface I_LinkingDb {
	
	
	/**
	 * Avvia una connessione al database
	 * @return la sessione {@link Connection} con il database dalla quale preparare gli statement,
	 * null se la connessione ha esito negativo
	*/
	public Connection connect();
	
	
	
	/**
	 * Disconnette dal database
	 * @return true se la disconnessione &egrave andata a buon fine, false altrimenti 
	*/
	public boolean disconnect();
	
	
	
	/**
	 * Predica se si � attualmente connessi al database.
	 * @return true se la connessione &egrave attiva, false altrimenti
	*/
	public boolean isConnected();
	
	
	
	/**
	 * Esegue una query di aggiornamento (insert, update, delete) sul database
	 * @param stmt il {@link PreparedStatement} gi&agrave pronto
	 * @return true se l'aggiornamento va a buon fine, false altrimenti
	*/
	public boolean update(PreparedStatement stmt);
	
	
	/**
	 * Esegue una query select sul database, ritorna i valori in formato stringa
	 * @param stmt il {@link PreparedStatement} gi� pronto
	 * @return la struttura dati contenente i record stringa risultanti
	*/
	public ArrayList<String[]> select(PreparedStatement stmt);
       
   
	public ADISysTableModel getTable(PreparedStatement stmt);
        
        public ADISysTableModel getTable(String queryText);
	
        public String string2sqlstring(String stringaInput);
        
        public ResultSet getResultSet(String queryText);
        
        public String getError();
	
	public boolean execute(String query);
	
	/**
	 * Incapsula il comportamento del metodo Connection.prepareStatement(String).
	 * @param sqlString un SQL statement che pu� contenere una o pi� '?' 
	 * al posto dei parametri
	 * @return un nuovo SQL statement con i '?' al posto dei valori pronti per essere
	 *  sostituiti con il metodo PreparedStatement.setString
	 * @throws SQLException se viene lanciata un eccezione di accesso al database 
	 * o non &egrave stata aperta una connesione al database
	*/
	public PreparedStatement prepareStatement(String sqlString) throws SQLException;
	
}
