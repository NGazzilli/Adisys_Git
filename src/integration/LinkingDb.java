/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 *
 * Classe dedicata alla gestione del Database.
 * Gestisce l'apertura e la chiusura della connessione col Database
 * Fornisce i metodi per l'esecuzione delle query sul Database
 */
import adisys.server.strumenti.ADISysTableModel;
import business.configurazione.Configurazione;
import business.configurazione.ConfigurazioneTO;
import business.configurazione.I_ConfigDB;
import messaggistica.MainException;
import java.awt.HeadlessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import messaggistica.GMessage;

public class LinkingDb implements I_LinkingDb {
   private static String nomeDB; // = "ADISysData";       // Nome del Database a cui connettersi
   private static String nomeUtente; //= "asl";   // Nome utente utilizzato per la connessione al
   private static String dbPath; // = "jdbc:hsqldb:file:database/";
   private static String pwdUtente; // = "";    // Password usata per la connessione al

   private static String errore;       // Raccoglie informazioni riguardo l'ultima

   private static Connection connessione;       // La connessione col Database
   private static boolean connesso;    // Flag che indica se la connessione è attiva o meno
   private static ResourceBundle linkingDb = ResourceBundle.getBundle("adisys/server/property/LinkingDb");

   public static void setResourceBundle(String path, Locale locale){
       linkingDb = ResourceBundle.getBundle(path, locale);
   }
           
   public LinkingDb(ConfigurazioneTO to) throws MainException{
		
		try {
			I_ConfigDB config = new Configurazione();
			String[] dbDates = config.getDbDates();
			/*if(!first){
				dbPath = dbDates[0];
				nomeDB = dbDates[1];
				nomeUtente = dbDates[2];
                                pwdUtente = dbDates[3];
			}
                        
			/*else{primo avvio del sistema, il db non esiste: non previsto da adisys
                               serve per configurazione dal database da parte dell'utente
				dbPath = dbDates[0];
				nomeDB = "mysql";
				nomeUtente = dbDates[2];
				pwdUtente = dbDates[3];
			}*/
				dbPath = dbDates[0];
				nomeDB = dbDates[1];
				nomeUtente = dbDates[2];
                                pwdUtente = dbDates[3];
		} catch (MainException e) {
			System.err.println(e.getMessage());
                        String message = linkingDb.getString("COLLEGAMENTO AL DATABASE NON RIUSCITO.");
                        GMessage.message_error(message);
			throw new MainException(linkingDb.getString("COLLEGAMENTO AL DATABASE NON RIUSCITO."));
		}
		
	}
   
   /*public Database(String nomeDB) { this(nomeDB, "", ""); }

   public Database(String nomeDB, String nomeUtente, String pwdUtente) {
      this.nomeDB = nomeDB;
      this.nomeUtente = nomeUtente;
      this.pwdUtente = pwdUtente;
      connesso = false;
      errore = "";
   }*/

   // Apre la connessione con il Database
   @Override
   public Connection connect() {
      
       connesso = false;
       try {
          System.out.println(linkingDb.getString("CARICAMENTO DATABASE. ATTENDERE..."));
         
         //Carico il driver JDBC per la connessione con il database MySQL
         //try {
            //Class.forName("com.mysql.jdbc.Driver");
         
            // Controllo che il nome del Database non sia nulla
            if (!nomeDB.equals("")) {

                // Controllo se il nome utente va usato o meno per la connessione
                if (nomeUtente.equals("")) {

                    // La connessione non richiede nome utente e password
                    connessione = DriverManager.getConnection(dbPath + nomeDB);
                } else {

                    // La connessione richiede nome utente, controllo se necessita anche della password
                    if (pwdUtente.equals("")) {

                    // La connessione non necessita di password
                    connessione = DriverManager.getConnection(dbPath + nomeDB + "?user=" + nomeUtente);
                    } else {

                        // La connessione necessita della password
                        connessione = DriverManager.getConnection(dbPath + nomeDB + "?user=" + nomeUtente + "&password=" + pwdUtente);
                    }
                }

                // La connessione è avvenuta con successo
                connesso = true;
                //System.out.println("CONNESSIONE OK");
            } else {
                GMessage.message_error(linkingDb.getString("MANCA IL NOME DEL DATABASE"));
                System.exit(0);
            }
         /*} catch (ClassNotFoundException e) {
             System.err.println("Ops... La classe non e' stata trovata, assicurarsi di possedere JDBC");
             e.printStackTrace();
             return null;
         }*/
      } catch (SQLException | HeadlessException e) { 
          String messaggio = linkingDb.getString("ERRORE NEL TENTATIVO DI CONNESSIONE AL DATABASE CON I PARAMETRI SPECIFICATI");
          GMessage.message_error(messaggio);
           try {
               throw new MainException(messaggio);
           } catch (MainException ex) {
               Logger.getLogger(LinkingDb.class.getName()).log(Level.SEVERE, null, ex);
           }
      }
      return connessione;
      	
   }

   @Override
   public boolean esegui(String SQLString)
	{
		//Crea un oggetto per le operazioni sul database
		try {
			//Connessione
			Statement st = connessione.createStatement( );
			System.out.println("Database -> Esecuzione predicato SQL: "+ SQLString);		
			st.executeUpdate(SQLString);
			return true;
		} catch (SQLException e) {
			//ERRORE: restituisce falso
			e.printStackTrace();
                        String messaggio = linkingDb.getString("ERRORE: IMPOSSIBILE AGGIORNARE IL DATABASE.");
                        GMessage.message_error(messaggio);
			return false;
		}
	}
	
	 /**
	  * Esegue una query di aggiornamento (insert, update, delete) sul database
	  * @param stmt il {@link PreparedStatement} già pronto
	  * @return true se l'aggiornamento va a buon fine, false altrimenti
	 */
   @Override
	public boolean update(PreparedStatement stmt){
		try{
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
        
        /**
	 * Esegue una query select sul database, ritorna i valori in formato stringa
	 * @param stmt il {@link PreparedStatement} gi&agrave pronto
	 * @return la struttura dati contenente i record stringa risultanti, una struttura
	 * dati vuota se non trova niente
	*/
   @Override
	public ArrayList<String[]> select(PreparedStatement stmt){
//		System.out.println("Query: "+stmt.toString());
		
		ArrayList<String[]> dates;
		String[] aRecord;
		int numColumn = 0;
		try{
			ResultSet result = stmt.executeQuery();//esegue la query
			dates = new ArrayList<>();
			//prende il metadata per ottenere il numero di colonne
			ResultSetMetaData resultMeta = result.getMetaData();
			numColumn = resultMeta.getColumnCount();
			while(result.next()){//per ogni record
				aRecord = new String[numColumn];//record attuale
				for(int i=0; i<numColumn; i++){//per ogni valore del record
					aRecord[i] = result.getString(i+1);
				}
				
				dates.add(aRecord);
			}
			result.close();
			return dates;
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
        
        /**
	 * Esegue una query select sul database, ritorna i valori in formato stringa
	 * @param stmt il {@link PreparedStatement} gi&agrave pronto
	 * @return la struttura dati contenente i record stringa risultanti, una struttura
	 * dati vuota se non trova niente
	*/
        @Override
	public ADISysTableModel getTabella(PreparedStatement stmt){
            System.out.println("Query: "+stmt.toString());
		try {
                    ResultSet result = stmt.executeQuery();
                    return new ADISysTableModel(result);
                } catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        }
         
	/**
	 * Incapsula il comportamento del metodo Connection.prepareStatement(String).
	 * @param sqlString un SQL statement che può contenere una o più '?' 
	 * al posto dei parametri
	 * @return un nuovo SQL statement con i '?' al posto dei valori pronti per essere
	 *  sostituiti con il metodo PreparedStatement.setString
	 * @throws SQLException se viene lanciata un eccezione di accesso al database 
	 * o non è stata aperta una connesione al database
	*/
	@Override
	public PreparedStatement prepareStatement(String sqlString) throws SQLException{
		return connessione.prepareStatement(sqlString);
	}
	
	/**
	 * Esegue una query sul database e restituisce il relativo tableModel da utilizzare in una JTable
	 * @param queryTXT
	 * @return ADISysTableModel
	 * 
	 */
   @Override
	public ADISysTableModel getTabella(String queryText)
	{
	
		Statement enunciato;
		try {
			
			//Crea uno statement per l'interrogazione del database
                   
			enunciato = connessione.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//Trace
			System.out.println("Database -> Interrogazione SQL: " + queryText);		
			
			//Crea un ResultSet eseguendo l'interrogazione desiderata
			ResultSet tabellina = enunciato.executeQuery(queryText);
			
			return new ADISysTableModel(tabellina);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
        
        /**
	 * Esegue una query sul database e restituisce il relativo ResultSet
	 * @param queryTXT
	 * @return ADISysTableModel
	 * 
	 */
   @Override
	public ResultSet getResultSet(String queryText)
	{
	
		Statement enunciato;
		try {
			
			//Crea uno statement per l'interrogazione del database
			enunciato = connessione.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	
			//Trace
			System.out.println("Database -> Interrogazione SQL: " + queryText);		
			
			//Crea un ResultSet eseguendo l'interrogazione desiderata
			ResultSet tabellina = enunciato.executeQuery(queryText);
			
			return tabellina;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**Metodo da applicare ai valori di tipo stringa prima dell'inserimento nel database
	 * 
	 * @param stringaInput
	 */
   @Override
	public String string2sqlstring(String stringaInput)
	{
		String stringaOutput;
		
		//Rimedio bug apostrofo
		stringaOutput=stringaInput.replaceAll("'", "''");
		
		
		return stringaOutput;
	}
        
   // Chiude la connessione con il Database
   @Override
   public boolean disconnect() {
      try {
         connessione.close();
         connesso = false;
         return true;
      } catch (Exception e) { e.printStackTrace(); }
      return false;
   }

   @Override
   public boolean isConnected() { return connesso; }   // Ritorna TRUE se la connessione con il Database è attiva
   @Override
   public String getErrore() { return errore; }       // Ritorna il messaggio d'errore dell'ultima eccezione sollevata

}
