package integration.dao;

import business.intervento.TipoIntervento;
import integration.View;
import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.DateFormatConverter;
import adisys.server.strumenti.FileFactory;
import integration.LinkingDb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import messaggistica.MainException;

import business.TO;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoTO;
import business.patologia.PatologiaTO;
import static integration.dao.AbstractDAO.linkDb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Formatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Il DAO per mysql che si occupa delle operazioni sulla tabella infermiere presente nel database.
*/
public class InterventoMySqlDAO extends AbstractDAO{
	
        
        public static String NOME_TABELLA_INTERVENTI="";

	public static String NOME_COLONNA_ID="";
	public static String NOME_COLONNA_ID_PAZIENTE="";
	public static String NOME_COLONNA_ID_INFERMIERE="";

	public static String NOME_COLONNA_CITTA="";
	public static String NOME_COLONNA_CIVICO="";
	public static String NOME_COLONNA_CAP="";

	public static String NOME_COLONNA_DATA="";
	public static String NOME_COLONNA_ORA_INIZIO="";
	public static String NOME_COLONNA_ORA_FINE="";
        public static String NOME_COLONNA_STORICO="";

	public static String NOME_TABELLA_TIPI_INTERVENTI="";
        
	public static String NOME_COLONNA_ID_TIPO_INTERVENTO ="";
	public static String NOME_COLONNA_IDINT_TIPO="";
	public static String NOME_COLONNA_NOME_TIPO="";
	public static String NOME_COLONNA_VALORE_RILEVATO = "";
	public static String NOME_COLONNA_TEMPO_INTERVENTO = "";
	public static String NOME_COLONNA_NOTE_TIPO="";
         
	public static String NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI="";
	public static String NOME_COLONNA_ID_PAT_INTERVENTO = "";
        public static String NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI = "";
	public static String NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI = "";
        public static String NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI = "";
        
        //COSTANTI DI FORMATO
	private static final String FORMATO_DATA_SQL = "yyyy-MM-dd";
	private static final String FORMATO_ORA_SQL = "HH:mm:ss";
        public final static int diffMax = 120;
	public static final int intTrascorso = -1;
	public static final int intNonModificabile = 0;
	public static final int intModificabile = 1;
        
        private static String filePath = "file/interventi.txt";
        private static String filePathTipi = "file/tipiInterventi.txt";
        private static String filePathPatologie = "file/patTipiInterventi.txt";
        
	/**Costante protetta per i campi della query insert*/
	protected final static String[] FIELDS_INSERT = {
		NOME_COLONNA_ID_PAZIENTE,
                NOME_COLONNA_ID_INFERMIERE,
                NOME_COLONNA_CITTA,
                NOME_COLONNA_CIVICO,
                NOME_COLONNA_CAP,
                NOME_COLONNA_DATA,
                NOME_COLONNA_ORA_INIZIO
	};
	//Se cambio il numero di FIELDS_INSERT, devo cambiare anche FIELDS_QUERY
	/**Costante protetta per i campi della query select, include i campi della costante
	 * {@link FIELDS_INSERT}*/
	protected final static String[] FIELDS_QUERY = {
		NOME_COLONNA_ID,
		FIELDS_INSERT[0],
		FIELDS_INSERT[1],
                FIELDS_INSERT[2],
                FIELDS_INSERT[3],
                FIELDS_INSERT[4],
                FIELDS_INSERT[5],
                FIELDS_INSERT[6]
	};
	/**Costante per il nome della tabella degli infermieri*/
	protected final static String TABLE = leggiNomeTabella();
        
               
	/**Grazie a questo oggetto ottiene le query già ben formate pronte per essere
	 * date in pasto al database*/
	private MySqlDAOConfig mysqlConfig = new MySqlDAOConfig(FIELDS_INSERT, FIELDS_QUERY, TABLE);
	
        private static String leggiNomeTabella(){
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));
                return scanner.next();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
       
        private void caricaVariables(String[] nomeCampi){
            
            NOME_TABELLA_INTERVENTI = nomeCampi[0];
            NOME_COLONNA_ID = nomeCampi[1];
            NOME_COLONNA_ID_PAZIENTE = nomeCampi[2];
            NOME_COLONNA_ID_INFERMIERE = nomeCampi[3];
            NOME_COLONNA_CITTA = nomeCampi[4];
            NOME_COLONNA_CIVICO = nomeCampi[5];
            NOME_COLONNA_CAP = nomeCampi[6];
            NOME_COLONNA_DATA = nomeCampi[7];
            NOME_COLONNA_ORA_INIZIO = nomeCampi[8];
            NOME_COLONNA_ORA_FINE = nomeCampi[9];
            NOME_COLONNA_STORICO = nomeCampi[10];
            
            FIELDS_INSERT[0] = NOME_COLONNA_ID_PAZIENTE + ", ";
            FIELDS_INSERT[1] = NOME_COLONNA_ID_INFERMIERE + ", ";
            FIELDS_INSERT[2] = NOME_COLONNA_CITTA + ", ";
            FIELDS_INSERT[3] = NOME_COLONNA_CIVICO + ", ";
            FIELDS_INSERT[4] = NOME_COLONNA_CAP + ", ";
            FIELDS_INSERT[5] = NOME_COLONNA_DATA + ", ";
            FIELDS_INSERT[6] = NOME_COLONNA_ORA_INIZIO;
            FIELDS_QUERY[0] = NOME_COLONNA_ID + ", ";
            FIELDS_QUERY[1] = FIELDS_INSERT[0];
            FIELDS_QUERY[2] = FIELDS_INSERT[1];
            FIELDS_QUERY[3] = FIELDS_INSERT[2];
            FIELDS_QUERY[4] = FIELDS_INSERT[3];
            FIELDS_QUERY[5] = FIELDS_INSERT[4];
            FIELDS_QUERY[6] = FIELDS_INSERT[5];
            FIELDS_QUERY[7] = FIELDS_INSERT[6];
        }
        
        public void leggiFiles() {
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));

                int i = 0;
                String[] nomeCampi = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[nomeCampi.length + 1];
                    for (i = 0; i < nomeCampi.length; i++) 
                        temp[i] = nomeCampi[i];
                    nomeCampi = temp;
                    nomeCampi[nomeCampi.length - 1] = newCampo;
                }
                caricaVariables(nomeCampi);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFilesTipiInterventi();
            leggiFilesPatTipiInterventi();
        }
        
        private void caricaVariablesTipiInterventi(String[] nomeCampi){
            NOME_TABELLA_TIPI_INTERVENTI = nomeCampi[0];
            NOME_COLONNA_ID_TIPO_INTERVENTO = nomeCampi[1];
            NOME_COLONNA_IDINT_TIPO = nomeCampi[2];
            NOME_COLONNA_NOME_TIPO = nomeCampi[3];
            NOME_COLONNA_VALORE_RILEVATO = nomeCampi[4];
            NOME_COLONNA_TEMPO_INTERVENTO = nomeCampi[5];
            NOME_COLONNA_NOTE_TIPO = nomeCampi[6];
        }
        
        public void leggiFilesTipiInterventi() {
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathTipi));

                int i = 0;
                String[] nomeCampi = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[nomeCampi.length + 1];
                    for (i = 0; i < nomeCampi.length; i++) 
                        temp[i] = nomeCampi[i];
                    nomeCampi = temp;
                    nomeCampi[nomeCampi.length - 1] = newCampo;
                }
                caricaVariablesTipiInterventi(nomeCampi);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        private void caricaVariablesPatTipiInterventi(String[] nomeCampi){
            NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI = nomeCampi[0];
            NOME_COLONNA_ID_PAT_INTERVENTO = nomeCampi[1];
            NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI = nomeCampi[2];
            NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI = nomeCampi[3];
            NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI = nomeCampi[4];
        }
        
        public void leggiFilesPatTipiInterventi() {
            try {
                                    //JOptionPane.showMessageDialog(null, FileFactory.getFileContent(name));
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathPatologie));

                int i = 0;
                String[] nomeCampi = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[nomeCampi.length + 1];
                    for (i = 0; i < nomeCampi.length; i++) 
                        temp[i] = nomeCampi[i];
                    nomeCampi = temp;
                    nomeCampi[nomeCampi.length - 1] = newCampo;
                }
                caricaVariablesPatTipiInterventi(nomeCampi);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
 
	//--------------------------------
 
	/**
	 * Costruttore, nel caso in cui non esiste una connessione al db (oggetto {@link LinkingDb})
	 * ne crea una, altrimenti usa quella statica della classe AbstractDAO da cui questa eredita
	 * @throws MainException il file di configurazione non esiste, non può essere letto oppure 
	 * non può essere modificato
	*/
	public InterventoMySqlDAO() throws MainException{
		System.out.println("Costrutture di InfermiereMySqlDAO");
		
		if(linkDb==null){
			linkDb = new LinkingDb(null);
			linkDb.connect();
		}
		else{
			if(linkDb.isConnected()){
				System.out.println("linkDb gia' connessa");
			}
			else{
				System.out.println("linkDb esistente ma non connessa. Ritento la connessione");
				linkDb = new LinkingDb(null);
				linkDb.connect();
			}
		}
                leggiFiles();
	}
        
	/**Numero massimo di caratteri consentiti dal database
	private static final int MAX_CHARS = 20;*/


	/**
	 * Crea un nuovo infermiere, inserisce nel database il nuovo infermiere con i dati prelevati dal TO
	 * 
	 * @param interfaceTo il transfer object con tutti i dati da inserire
	 * @return true se la creazione ha successo, false altrimenti
	*/
	@Override
	public boolean create(TO interfaceTo) {
		System.out.println("create()");
		InterventoTO to = (InterventoTO) interfaceTo;
            
		//Preparazione dei formati SQL
		Formatter f= new Formatter();
		Formatter f2= new Formatter();
		String formatoElencoColonne="%s,%s,%s,%s,%s,%s,%s";
		String formatoElencoValori="%s,%s,'%s','%s','%s','%s','%s'";


		String elencoColonne= f.format(formatoElencoColonne, 
				NOME_COLONNA_ID_INFERMIERE,
				NOME_COLONNA_ID_PAZIENTE,

				NOME_COLONNA_CITTA,
				NOME_COLONNA_CIVICO,
				NOME_COLONNA_CAP,

				NOME_COLONNA_DATA,
				NOME_COLONNA_ORA_INIZIO
				).toString();


		String elencoValori= f2.format(formatoElencoValori,
				String.valueOf(to.getIDInfermiere()) ,
				String.valueOf(to.getIDPaziente()),

				linkDb.string2sqlstring(to.getCitta()),
				linkDb.string2sqlstring(to.getCivico()),
				linkDb.string2sqlstring(to.getCap()),

				to.getDataDaFormato(FORMATO_DATA_SQL),
				to.getOraInizioDaFormato(FORMATO_ORA_SQL)

				).toString();

		//Composizione istruzione 
		String istruzione = "INSERT INTO "+ NOME_TABELLA_INTERVENTI+" (" + elencoColonne + ") VALUES ("+ elencoValori+ ");";

		//Trace
		System.out.println("SQL: " + istruzione.toUpperCase());

		//Esecuzione
		if(linkDb.esegui(istruzione))
		{
			//Caso inserimento dati di base eseguito con successo -> Inserimento dei tipi di intervento

			//Trace
			System.out.println("InterventoMySqlDAO -> Intervento creato con successo, inserimento tipi in corso...");

			//TODO Inserire valori tipi intervento nel caso di inserimento andato a buon fine.
			//Recupero id dell'intervento:
			ResultSet idAssegnato = linkDb.getResultSet("SELECT MAX(" + NOME_COLONNA_ID + ") FROM " 
                                + NOME_TABELLA_INTERVENTI);

			int newID; //ID del nuovo intervento

			try {
				//Posizionamento nel resultset generato
				idAssegnato.first();
				//Recupero dell'id
				newID = idAssegnato.getInt(1);

			}
			catch (SQLException e) 
			{
				//Auto-generated catch block
				e.printStackTrace();
				//Trace
				System.out.println("InterventoMySqlDAO -> Inserimento dei tipi di intervento fallito.");

				return false; 

			}


			//TODO Inserimento dei tipi di intervento:
			//Contatore dei tipi di intervento inseriti
			int numeroTipiInseriti=0;

			//Ciclo di update
			for(int i=0; i < to.countTipInterventi(); i++ )
			{
				//Esecuzione predicato con incremento indice in caso di successo.
				if(inserisciTipoIntervento(newID, to.getTipoIntervento(i).getNome(), to.getTipoIntervento(i).getNote()))
				{
					
					ResultSet idTipoIntervento = linkDb.getResultSet("SELECT MAX(" 
                                                + NOME_COLONNA_ID_TIPO_INTERVENTO + ") FROM " + NOME_TABELLA_TIPI_INTERVENTI);

					int lastInsertId; //ID del nuovo intervento

					try {
						//Posizionamento nel resultset generato
						idTipoIntervento.first();
						//Recupero dell'id
						lastInsertId = idTipoIntervento.getInt(1);

					}
					catch (SQLException e) 
					{
						//Auto-generated catch block
						e.printStackTrace();
						//Trace
						System.out.println("InterventoMySqlDAO -> Inserimento del tipo di intervento fallito.");

						return false; 

					}

                                    //Inserimento patologie per ogni tipo intervento
                                    ArrayList<PatologiaTO> patologieTipoIntervento = to.getTipoIntervento(i).getListaPatologie();
                                    
                                    
                                    for(int k = 0; k < patologieTipoIntervento.size(); k++)
                                        if(inserisciPatologieTipoIntervento(lastInsertId, patologieTipoIntervento.get(k).getCodice(), 
                                                patologieTipoIntervento.get(k).getGravita())){
                                            //Trace
					System.out.println("InterventoMySqlDAO -> Patologia tipo intervento <<"+ to.getTipoIntervento(i).getNome() +">> inserite con successo");
                                        }
                                        else  {
                                            //Trace
                                            System.out.println("InterventoMySqlDAO -> Patologia tipo intervento <<"+ to.getTipoIntervento(i).getNome() +">> non inserita. Contattare l'assistenza");
                                        }

                                    
					//Trace
					System.out.println("InterventoMySqlDAO -> Tipo intervento <<"+ to.getTipoIntervento(i).getNome() +">> inserito con successo");
					numeroTipiInseriti++;

				}
				else 
				{
					//Trace
					System.out.println("InterventoMySqlDAO -> Tipo intervento <<"+ to.getTipoIntervento(i).getNome() +">> non inserito. Contattare l'assistenza");
				}
			}

			//Trace
			System.out.println("InterventoMySqlDAO -> Inseriti " + numeroTipiInseriti +" tipi di intervento su " + to.countTipInterventi() +".");

			//Se sono stati inseriti tutti i tipi di intervento da eseguire restituisce true, altrimenti false
			if(to.countTipInterventi() == numeroTipiInseriti)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else 
		{
			//Trace
			System.out.println("InterventoMySqlDAO -> Inserimento intervento fallito!!");

			return false;
		}
	}
        
        private boolean inserisciTipoIntervento(int idIntervento, String nomeTipo, String noteTipo) 
	{
		//TODO Inserimento del tipo di intervento:

		//Composizione colonne
		String colonna1 = NOME_COLONNA_IDINT_TIPO;
		String colonna2 = NOME_COLONNA_NOME_TIPO;
		String colonna3 = NOME_COLONNA_NOTE_TIPO;
		String colonna4 = NOME_COLONNA_TEMPO_INTERVENTO;
		String colonna5 = NOME_COLONNA_VALORE_RILEVATO;


		String elencoColonneTipi = "("+ colonna1 +"," + colonna2 +"," + colonna3 + "," + colonna4 +"," + colonna5 + ")";

		//Composizione valori
		String valore1 = String.valueOf(idIntervento);
		String valore2 = "'" + linkDb.string2sqlstring(nomeTipo) + "'";
		String valore3 = "'" + linkDb.string2sqlstring(noteTipo) + "'";
		String valore4 = "null";
		String valore5 = "null";

		String elencoValoriTipi = "("+ valore1 +"," + valore2 +"," + valore3 +"," + valore4 +"," + valore5 +")";

		//Composizione predicato SQL
		String predicatoInserimentoTipo= "INSERT INTO " + NOME_TABELLA_TIPI_INTERVENTI + elencoColonneTipi +" VALUES " + elencoValoriTipi + ";";

		//Trace
		System.out.println("SQL: " + predicatoInserimentoTipo);

		//Esecuzione predicato con incremento indice in caso di successo.
		if(linkDb.esegui(predicatoInserimentoTipo))
		{
			//Trace
			System.out.println("InterventoMySqlDAO -> Tipo intervento <<"+ nomeTipo +">> inserito con successo");
			return true;

		}
		else 
		{
			//Trace
			System.out.println("InterventoMySqlDAO -> Tipo intervento <<"+ nomeTipo +">> non inserito.");
			return false;
		}
	}
        
        private boolean inserisciPatologieTipoIntervento(int idIntervento, String codicePatologia, int gravita) 
	{
		//TODO Inserimento del tipo di intervento:

		//Composizione colonne
		String colonna1 = NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI;
		String colonna2 = NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI;
                String colonna3 = NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI;

		String elencoColonneTipi = "(" + colonna1 +"," + colonna2 +"," + colonna3+ ")";

		//Composizione valori
		String valore1 = String.valueOf(idIntervento);
		String codice = "'" + codicePatologia + "'";
                String valore2 = "";
                String valore3 = "'" + gravita + "'";
                        
                //Composizione predicato SQL
		String queryIdPatologia = "SELECT " + PatologiaMySqlDAO.NOME_COLONNA_ID +" FROM " 
                        + PatologiaMySqlDAO.NOME_TABELLA + " WHERE " + PatologiaMySqlDAO.NOME_COLONNA_CODICE + " = " + codice;

                //Esecuzione query
		ResultSet tabella= linkDb.getResultSet(queryIdPatologia);
		//Trace
		System.out.println("SQL: " + queryIdPatologia);

		//Trasfermento dati (se la tabella non è vuota)
		try {
			if(tabella.first())
                            valore2 = tabella.getString(PatologiaMySqlDAO.NOME_COLONNA_ID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//trace
			System.out.println("InterventoMySqlDAO -> ERRORE: Impossibile leggere l'id della patologia con codice = " + codice);

		}

		String elencoValoriTipi = "("+ valore1 +"," + valore2 + "," + valore3 + ")";

		//Composizione predicato SQL
		String predicatoInserimentoTipo= "INSERT INTO " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI 
                        + elencoColonneTipi +" VALUES " + elencoValoriTipi + ";";

		//Trace
		System.out.println("SQL: " + predicatoInserimentoTipo);


		if(linkDb.esegui(predicatoInserimentoTipo))
		{
			//Trace
			System.out.println("InterventoMySqlDAO -> Patologie tipo intervento <<"+ valore1 +">> inserite con successo");
			return true;

		}
		else 
		{
			//Trace
			System.out.println("InterventoMySqlDAO -> Patologie tipo intervento <<"+ valore1 +">> non inserite");
			return false;
		}
	}
	
        public ADISysTableModel getTabellaInterventiInfermiere(int id, String dataDal, String dataAl)
	{
		int yesId = 0;
                String formatoElencoColonne="%s,%s,%s,%s,%s,%s,%s,%s";
		Formatter f= new Formatter();

		String elencoColonne= f.format(formatoElencoColonne, 
				NOME_COLONNA_ID,
                                NOME_COLONNA_ID_INFERMIERE,
				NOME_COLONNA_ID_PAZIENTE,

				NOME_COLONNA_CITTA,
				NOME_COLONNA_CIVICO,
				NOME_COLONNA_CAP,

				NOME_COLONNA_DATA,
				NOME_COLONNA_ORA_INIZIO
				).toString();
		//Recupero dati sugli interventi dell'infermiere
		String queryText= "SELECT " + elencoColonne + " FROM " + NOME_TABELLA_INTERVENTI; 
		if(id != -1) {
			queryText = queryText + " WHERE " + NOME_COLONNA_ID_INFERMIERE + "=" + id;
			yesId = 1;
		}
		if(!dataDal.equals("") || !dataAl.equals("")){
			if(yesId == 0)
				queryText = queryText + " WHERE ";
			else
				queryText = queryText + " AND ";
			if(!dataDal.equals("") && dataAl.equals("")){
				String testoData = DateFormatConverter.cambiaFormato(dataDal.toString(), 
                                        DateFormatConverter.getFormatData(),  FORMATO_DATA_SQL);
				queryText = queryText + NOME_COLONNA_DATA + " >= '" + testoData + "'";
			}
			else if(dataDal.equals("") && !dataAl.equals("")){
				String testoData = DateFormatConverter.cambiaFormato(dataAl.toString(), 
                                        DateFormatConverter.getFormatData(),  FORMATO_DATA_SQL);
				queryText = queryText + NOME_COLONNA_DATA + " <= '" + testoData + "'";
			}
			else {
				String testoDataDal = DateFormatConverter.cambiaFormato(dataDal.toString(), 
                                        DateFormatConverter.getFormatData(),  FORMATO_DATA_SQL);
				String testoDataAl = DateFormatConverter.cambiaFormato(dataAl.toString(), 
                                        DateFormatConverter.getFormatData(),  FORMATO_DATA_SQL);
				
				long mllsDataDal = DateFormatConverter.dateString2long(dataDal, DateFormatConverter.getFormatData());
				long mllsDataAl = DateFormatConverter.dateString2long(dataAl, DateFormatConverter.getFormatData());
				
				long diff = mllsDataAl - mllsDataDal;
				boolean maggDataAl = true;
				if(diff < 0)
					maggDataAl = false;
				queryText = queryText + NOME_COLONNA_DATA + 
						" BETWEEN '";
				if(maggDataAl == true){
					queryText = queryText + testoDataDal + "'" +
							" AND '" + testoDataAl + "'";
				} else {
					queryText = queryText + testoDataAl + "'" +
							" AND '" + testoDataDal + "'";
                                        queryText = queryText + " ORDER BY " + NOME_COLONNA_DATA + " DESC";
                                        return linkDb.getTabella(queryText);
				}
				
			}
		}	
		queryText = queryText + " ORDER BY " + NOME_COLONNA_DATA;	
		return linkDb.getTabella(queryText);

	}        
       
	/**
	 * Legge l'intera tabella degli infermieri, ogni infermiere e' incapsulato nel transfer object.
	 * Il risultato equivale alla query SELECT * FROM infermiere;
	 * 
	 * @return la struttura dati dei transfer object {@link InfermiereTO} con i dati di ogni record
	 *  della tabella del db, null se c'e' qualche errore
	*/
	@Override
	public ArrayList<TO> read() {
		/*System.out.println("read()");
		ArrayList<TO> interventi = new ArrayList<TO>();//arraylist dei to da riempire;
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getSelectSQL("true"));
			ArrayList<String[]> dates = linkDb.select(stmt);//arraylist sui dati del database
			Iterator<String[]> iterDates = dates.iterator();
			while(iterDates.hasNext()){
				String[] record = iterDates.next();
				
				
				InterventoTO infTO = new InterventoTO();//crea il nuovo TO con i dati prelevati
				
				id = Integer.parseInt(record[0]);
				nome = record[1];
				cognome = record[2];
				
				//setta i valori del database --------------
				infTO.setID(id);
				infTO.setNome(nome);
				infTO.setCognome(cognome);
				//------------------------------------------
				
				//aggiunge il nuovo TO all'arraylist
				infermieri.add(infTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e){
			System.err.println("Ops... Il database e la DAO non sono compatibili");
			e.printStackTrace();
			return null;
		}
		return infermieri;*/
            return null;
	}

        @Override
	public ADISysTableModel getTabella() {
		System.out.println("read()");
                //leggiFiles();
                try {
                    String query = mysqlConfig.getSelectSQL("true");
                    return linkDb.getTabella(query);
		} catch (NumberFormatException e){
			System.err.println("Ops... Il database e la DAO non sono compatibili");
			e.printStackTrace();
			return null;
		}
                    
	}
        
	/*
	 * Aggiorna il record di un infermiere con i dati incapsulati nel transfer object {@link InfermiereTO}
	 * quando la condizione <code>condition</code> &egrave <i>true</i>.<br>
	 * 
	 * @param interfaceTO l'interfaccia del transfer object dove sono incapsulati i dati da aggiornare
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return true se l'aggiornamento ha successo, false altrimenti
	 */
        @Override
	public boolean update(TO interfaceTO) {
		System.out.println("update()");
		//Preparazione dei formati SQL
		Formatter f= new Formatter();
                InterventoTO to = (InterventoTO) interfaceTO;
		String formatoUpdate="%s = %s, %s = %s, %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' ";


		String elencoUpdate= f.format(formatoUpdate, 
				NOME_COLONNA_ID_INFERMIERE, String.valueOf(to.getIDInfermiere()) ,
				NOME_COLONNA_ID_PAZIENTE, String.valueOf(to.getIDPaziente()),

				NOME_COLONNA_CITTA, linkDb.string2sqlstring(to.getCitta()),
				NOME_COLONNA_CIVICO, linkDb.string2sqlstring(to.getCivico()),
				NOME_COLONNA_CAP, to.getCap(),

				NOME_COLONNA_DATA, to.getDataDaFormato(FORMATO_DATA_SQL),
				NOME_COLONNA_ORA_INIZIO, to.getOraInizioDaFormato(FORMATO_ORA_SQL)
				).toString();


		//Composizione istruzione 
		String istruzione = "UPDATE " + NOME_TABELLA_INTERVENTI +" SET " + elencoUpdate +" WHERE " 
                        + NOME_COLONNA_ID + " = " + to.getID()+ ";";

		//Trace
		System.out.println("SQL: " + istruzione.toUpperCase());

		//Esecuzione
		if(linkDb.esegui(istruzione))
		{
			//TODO Esecuzione andata a buon fine, reinserimento tipi intervento
			//Trace
			System.out.println("Interventi -> Dati intervento modificati. Reinserimento dei tipi di intervento in corso...");
                         
                        for(int i = 0; i < to.countTipInterventi(); i++){
                            cancellaPatologieTipoIntervento(to.getTipoIntervento(i).getID());
                        }

			//TODO VERIFICARE - Cancellazione vecchi tipi di intervento
			if(cancellaTipiIntervento(to.getID())) 
			{
				//Trace
				System.out.println("Interventi -> Tipi intervento " + to.getID() + "cancellati con successo!");
			}
			else
			{
				//Trace
				System.out.println("Interventi -> ERRORE:Tipi intervento " 
                                        + to.getID() + " non cancellati, modifica fallita.");
				return false;
			}

			//TODO VERIFICARE - Ciclo di inserimento
			//TODO Inserimento dei tipi di intervento:

			//Contatore dei tipi di intervento inseriti
			int numeroTipiInseriti=0;

			//Ciclo di update
			for(int i=0; i < to.countTipInterventi(); i++ )
			{
				//Esecuzione predicato con incremento indice in caso di successo.
				if( inserisciTipoIntervento(to.getID(), to.getTipoIntervento(i).getNome(), 
                                        to.getTipoIntervento(i).getNote()))
				{
					
					ResultSet idTipoIntervento = linkDb.getResultSet("SELECT MAX("
                                                + NOME_COLONNA_ID_TIPO_INTERVENTO + ") FROM " + NOME_TABELLA_TIPI_INTERVENTI);

					int lastInsertId; //ID del nuovo intervento

					try {
						//Posizionamento nel resultset generato
						idTipoIntervento.first();
						//Recupero dell'id
						lastInsertId = idTipoIntervento.getInt(1);

					}
					catch (SQLException e) 
					{
						//Auto-generated catch block
						e.printStackTrace();
						//Trace
						System.out.println("Interventi -> Inserimento del tipo di intervento fallito.");

						return false; 

					}

                                    //Inserimento patologie per ogni tipo intervento
                                    ArrayList<PatologiaTO> patologieTipoIntervento = to.getTipoIntervento(i).getListaPatologie();
                                    
                                    
                                    for(int k = 0; k < patologieTipoIntervento.size(); k++)
                                        if(inserisciPatologieTipoIntervento(lastInsertId, patologieTipoIntervento.get(k).getCodice(),
                                                patologieTipoIntervento.get(k).getGravita())){
                                                
                                            //Trace
					System.out.println("Interventi -> Patologia tipo intervento <<"+ 
                                                to.getTipoIntervento(i).getNome() +">> inserite con successo");
                                        }
                                        else  {
                                            //Trace
                                            System.out.println("Interventi -> Patologia tipo intervento <<"+ 
                                                    to.getTipoIntervento(i).getNome() +">> non inserita. Contattare l'assistenza");
                                        }

                                    
					//Trace
					System.out.println("Interventi -> Tipo intervento <<"+ 
                                                to.getTipoIntervento(i).getNome() +">> inserito con successo");
					numeroTipiInseriti++;

				}
				else 
				{
					//Trace
					System.out.println("Interventi -> Tipo intervento <<"+ 
                                                to.getTipoIntervento(i).getNome() +">> non inserito. Contattare l'assistenza");
				}
			}

			//Trace
			System.out.println("Interventi -> Inseriti " + numeroTipiInseriti +" tipi di intervento su " 
                                + to.countTipInterventi() +".");

			//Se sono stati inseriti tutti i tipi di intervento da eseguire restituisce true, altrimenti false
			if(to.countTipInterventi() == numeroTipiInseriti)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else 
		{
			//Trace
			System.out.println("Interventi -> Inserimento intervento fallito!!");

			return false;
		}
	}
	
	private boolean cancellaTipiIntervento(int IDIntervento)
	{
		//Preparazione predicato
		String predicato= "DELETE FROM " 
                        + NOME_TABELLA_TIPI_INTERVENTI + " WHERE " 
                        + NOME_COLONNA_IDINT_TIPO + "=" + String.valueOf(IDIntervento)+";";

		//Esecuzione
		return linkDb.esegui(predicato); 
	}
	
        private boolean cancellaPatologieTipoIntervento(int IDTipoIntervento)
	{
		//Preparazione predicato
		String predicato= "DELETE FROM " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI + 
                        " WHERE " + NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI + "=" 
                        + String.valueOf(IDTipoIntervento)+";";

		//Esecuzione
		return linkDb.esegui(predicato); 
	}
	/**
	 * Cancella l'infermiere con l'id in input. Se l'id in input &egrave -1, allora
	 * cancella tutti gli infermieri in tabella (coincide con un reset) 
	 * Usa {@link #delete(String)}
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	@Override
	public boolean delete(int id){
		if(id==-1){
			return delete("true");
		}
		else{
			return delete(NOME_COLONNA_ID + " = " + id);
		}
	}

	/**
	 * <u>GENERALIZZAZIONE DEL PROBLEMA</u>.<br>
	 * Il metodo di cancellazione intervento di ADISys si basa sull'id,
	 * basta conoscere l'id del infermiere da cancellare (senza sapere altro). Questo
	 * metodo generalizza il problema offrendo la possibilit&agrave di cancellare un infermiere
	 * dando una stringa di condizione ben formata. La metodologia di cancellazione &egrave comunque
	 * rispettata dal metodo {@link #delete(int)}<br>
	 * 
	 * Cancella un infermiere se si verifica la condizione condition
	 * @param condition la condizione affinch&egrave la tupla venga cancellata.
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	private boolean delete(String condition) {
		System.out.println("delete()");
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getDeleteSQL(condition));
			//System.out.println(stmt);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
        public ArrayList<TipoIntervento> leggiTipiIntervento(int IDIntervento)
	{
		//Preparazione query
		String query= "SELECT * FROM " + NOME_TABELLA_TIPI_INTERVENTI + " WHERE " + NOME_COLONNA_IDINT_TIPO +"=" + String.valueOf(IDIntervento) + ";";

		//Esecuzione query
		ResultSet tabella= linkDb.getResultSet(query);

		//Creazione lista dei tipi di intervento da restituire
		ArrayList<TipoIntervento> array= new ArrayList<TipoIntervento>();

		//Trasfermento dati (se la tabella non è vuota)
		try {
			if(tabella.first())
			{
				for(;!(tabella.isAfterLast()); tabella.next())
				{
					//Creazione e popolamento nuovo tipo
					TipoIntervento nuovoTipo= new TipoIntervento();
					int idTipoIntervento = tabella.getInt(NOME_COLONNA_ID_TIPO_INTERVENTO);
                                        nuovoTipo.setID(idTipoIntervento);
					nuovoTipo.setNome(tabella.getString(NOME_COLONNA_NOME_TIPO));
					nuovoTipo.setValoreRilevato(tabella.getString(NOME_COLONNA_VALORE_RILEVATO));
					nuovoTipo.setTempoIntervento(tabella.getString(NOME_COLONNA_TEMPO_INTERVENTO));
					nuovoTipo.setNote(tabella.getString(NOME_COLONNA_NOTE_TIPO));
					
					//Preparazione query
					query = "SELECT * FROM " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI +
							" JOIN " + PatologiaMySqlDAO.NOME_TABELLA + " ON " +
							NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI + "." 
							+ NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI + " = " +
							PatologiaMySqlDAO.NOME_TABELLA + "." + PatologiaMySqlDAO.NOME_COLONNA_ID +
							" WHERE " + NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI 
							+"=" + idTipoIntervento + ";";

					//Esecuzione query
					ResultSet tabellaPatologie = linkDb.getResultSet(query);
					
					//Creazione lista delle patologie associate al tipo intervento
					ArrayList<PatologiaTO> arrayPatologie = new ArrayList<PatologiaTO>();
					
					try {
						if(tabellaPatologie.first())
						{
							for(;!(tabellaPatologie.isAfterLast()); tabellaPatologie.next())
                                                        {
                                                            arrayPatologie.add(new PatologiaTO(tabellaPatologie.getString(PatologiaMySqlDAO.NOME_COLONNA_CODICE),
                                                                    tabellaPatologie.getString(PatologiaMySqlDAO.NOME_COLONNA_NOME),
                                                                    tabellaPatologie.getInt(NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI)));
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//trace
						System.out.println("Interventi -> ERRORE: Impossibile leggere le patologie del tipo intervento con ID=" + String.valueOf(IDIntervento));

						}

					nuovoTipo.setListaPatologie(arrayPatologie);
					//Aggiunta del tipo di intervento alla lista
					array.add(nuovoTipo);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//trace
			System.out.println("Interventi -> ERRORE: Impossibile leggere i tipi dell'intervento con ID=" + String.valueOf(IDIntervento));

		}

		return array;
	}
	
      /**Verifica che l'ora corrente in cui si vuole modificare un intervento sia almeno a distanza di 
	 * due ore dalla prima attività dell'infermiere inerente all'intervento selezionato
	 * @param idInfermiere id dell'infermiere
	 * @return -1 se l'intervento è già passato (data e ora corrente > data e ora dell'intervento
	 * 		    0 se il primo intervento dell'infermiere è a meno di 2 ore dalla data corrente (pianificazione
	 * 			  non modificaabile)	
	 * 		    1 per pianificazione modificabile (ora del primo intervento dell'infermiere a più di
	 * 			2 ore dall'ora corrente
	 */
	public int verificaOraIntervento(String idInfermiere, String data, String ora) {
		
		//System.out.println(idInfermiere);
		long today = DateFormatConverter.oggi();
		String dataInt = data + " " + ora;
		long mllsInt = DateFormatConverter.dateString2long(dataInt, DateFormatConverter.Dateformat());
		
		
		if(mllsInt - today < 0){
			System.out.println("Impossibile creare/modificare un intervento passato");
			return intTrascorso;
		} else {
		
		String testoToday = DateFormatConverter.long2dateString(today, DateFormatConverter.getFormatData());
		System.out.println(testoToday);
		
		if(data.equals(testoToday)) {
		//Preparazione ed esecuzione query
			try {
			/* String query = "SELECT q1.diff AS differenza FROM (" +
			 					"SELECT TIMEDIFF(DATAORA , NOW( )) AS diff" +
			 					" from " + NOME_TABELLA_INTERVENTI + 
			 					" WHERE IDINFERMIERE = " + idInfermiere + 
			 					" ORDER BY DATAORA " +
			 					" LIMIT 0 , 1" +
			 					" ) AS q1" +
			 				" WHERE q1.diff >= '02:00'"; */
			 
			 String query = 
			 		"SELECT MIN( " + NOME_COLONNA_ORA_INIZIO + 
			 		" ) AS ORARIO FROM " + NOME_TABELLA_INTERVENTI + " " +
			 		" WHERE " + NOME_COLONNA_ID_INFERMIERE + " = " + idInfermiere +
			 		" AND " + NOME_COLONNA_DATA + " = CURDATE() ";
			 
			 ResultSet risultato = linkDb.getResultSet(query);
			 
			 risultato.next();
			  
			 String orarioMin = risultato.getString("ORARIO");
			 
			 if(orarioMin != null){
				 
				 /*String currentHour = DateFormatConverter.long2dateString(DateFormatConverter.oggi(),
						 DateFormatConverter.getFormatOra());*/
				 
				 long longOra = DateFormatConverter.dateString2long(data + " " + orarioMin, 
						 DateFormatConverter.getFormatData() + " " + FORMATO_ORA_SQL);
					
					//differenza tra ora dell'intervento e ora corrente in millisecondi
					long longDiff = (longOra - today);
				
					int minuti = (int) ((longDiff / 3600000) * 60) + (int) (longDiff / 60000 % 60);
					if(minuti < diffMax) {
						System.out.println("Intervento non modificabile a meno di due ore");
						return intNonModificabile;
					} else {
						String testoDiff = (int) (longDiff / 3600000) + " ora/e e " +
								(int) (longDiff / 60000 % 60) + " minuto/i";
						System.out.println("Ora inizio primo intervento odierno: " + orarioMin);
						System.out.println("Mancano " + testoDiff + " dall'inizio del primo intervento odierno " +
								"dell'infermiere" + idInfermiere + ": intervento modificabile");
						return intModificabile;
					} //else su orario inferiore alle 2 ore
				}//if su risultato.first()
	        } catch (SQLException ex) {
	            Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
	            //trace
		    System.out.println("Interventi -> ERRORE: Intervento n." + String.valueOf(idInfermiere) + " non trovato.");
	        }
		}
		}//else su intervento già passato
		return intModificabile;
	}
        
      public ArrayList<PatologiaTO> leggiPatologieIntervento(int IDIntervento)
	{
		//Preparazione query
		String query = "SELECT DISTINCT P. " + PatologiaMySqlDAO.NOME_COLONNA_CODICE + ", P."
                       + PatologiaMySqlDAO.NOME_COLONNA_NOME + ", PT." + NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI
                       + " FROM " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI + " PT "
                       + "JOIN " + NOME_TABELLA_TIPI_INTERVENTI + " T ON PT." 
                       + NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI
                       + " = T." + NOME_COLONNA_ID_TIPO_INTERVENTO  
                       + " JOIN " + PatologiaMySqlDAO.NOME_TABELLA + " P "
                       + " ON PT." + NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI + " = P."
                       + PatologiaMySqlDAO.NOME_COLONNA_ID
                       + " WHERE " + NOME_COLONNA_IDINT_TIPO +"=" + String.valueOf(IDIntervento) + ";";

		//Esecuzione query
		ResultSet tabella= linkDb.getResultSet(query);

		//Creazione lista dei tipi di intervento da restituire
		ArrayList<PatologiaTO> listaPatologie= new ArrayList<PatologiaTO>();

		//Trasfermento dati (se la tabella non è vuota)
		try {
			if(tabella.first())
			{
				for(;!(tabella.isAfterLast()); tabella.next())
				{
					//Popolamento lista patologie
					listaPatologie.add(new PatologiaTO(
                                                tabella.getString(PatologiaMySqlDAO.NOME_COLONNA_CODICE), 
                                                tabella.getString(PatologiaMySqlDAO.NOME_COLONNA_NOME), 
                                                tabella.getInt(NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI)));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//trace
			System.out.println("Interventi -> ERRORE: Impossibile leggere i tipi dell'intervento con ID=" + String.valueOf(IDIntervento));

		}

		return listaPatologie;
	}
      
    	public ArrayList<InterventoTO> getListaInterventiInfermiere(int id)
	{
		//Recupero dati sugli interventi dell'infermiere
		String queryText= "SELECT * FROM " + NOME_TABELLA_INTERVENTI; 
		if(id != -1)
			queryText = queryText + " WHERE " + NOME_COLONNA_ID_INFERMIERE + "=" + id
                                + " AND " + NOME_COLONNA_DATA + " >= CURDATE()";
		ResultSet tabella = linkDb.getResultSet(queryText);

		//**Scaricamento dei dati in una lista di interventi**
		//Creazione della lista
		ArrayList<InterventoTO> lista = new ArrayList<InterventoTO>();

		//Ciclo di lettura dalla tabella e scrittura in un nuovo oggetto
		try 
		{
			//Posizionamento del cursore ad inizio lista
			if(tabella.first())
			{
				do
				{
					//Creazione oggetto Intervento
					InterventoTO i = new InterventoTO();

					//Popolamento dell'oggetto

					//Dati intervento
					i.setID(tabella.getString(NOME_COLONNA_ID));
					i.setIDInfermiere(tabella.getString(NOME_COLONNA_ID_INFERMIERE));
					i.setIDPaziente(tabella.getString(NOME_COLONNA_ID_PAZIENTE));

					i.setCitta(tabella.getString(NOME_COLONNA_CITTA));
					i.setCivico(tabella.getString(NOME_COLONNA_CIVICO));
					i.setCap(tabella.getString(NOME_COLONNA_CAP));

					//TODO Controllare FORMATTAZIONE - Saltato, filtro impostato nella gui.
					i.setDataFmt(tabella.getString(NOME_COLONNA_DATA), FORMATO_DATA_SQL);
					i.setOraInizioFmt(tabella.getString(NOME_COLONNA_ORA_INIZIO), FORMATO_ORA_SQL);

					//Creazione array dei tipi dell'intervento corrente dal database
					ArrayList<TipoIntervento> listaInterventi = leggiTipiIntervento(i.getID());
					
					//Aggiunta dei tipi di intervento all'intervento corrente
					for(TipoIntervento t:listaInterventi) i.addTipoIntervento(t);

					//Aggiunta dell'oggetto intervento alla lista
					lista.add(i);

				} 
				while (tabella.next());
			}
			else
			{
				//Caso tabella vuota
				String errore="Interventi.getListaInterventiInfermiere(...) -> Nessun intervento relativo all'infermiere selezionato";
				System.out.println(errore);
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Interventi.getListaInterventiInfermiere(...) -> Errore nel trasfermienti tabella/lista, la lista restituita potrebbe essere incompleta");
		}

		//Restituzione lista
		return lista;
	}

    @Override
    public TO getSpecified(int id) {
        try {
            String query = "SELECT * FROM " + NOME_TABELLA_INTERVENTI + " WHERE " + NOME_COLONNA_ID + "=" + String.valueOf(id)+";";
            ResultSet risultato = linkDb.getResultSet(query);
            if(risultato.first())
            {
                InterventoTO i = new InterventoTO();
                i.setID(risultato.getInt(NOME_COLONNA_ID));
                i.setIDInfermiere(risultato.getInt(NOME_COLONNA_ID_INFERMIERE));
                i.setIDPaziente(risultato.getInt(NOME_COLONNA_ID_PAZIENTE));
                i.setDataFmt(risultato.getString(NOME_COLONNA_DATA), FORMATO_DATA_SQL);
                i.setOraInizioFmt(risultato.getString(NOME_COLONNA_ORA_INIZIO),FORMATO_ORA_SQL);
                //i.setOraFineFmt(risultato.getString(NOME_COLONNA_ORA_FINE),FORMATO_ORA_SQL);
                i.setCitta(risultato.getString(NOME_COLONNA_CITTA));
                i.setCivico(risultato.getString(NOME_COLONNA_CIVICO));
                i.setCap(risultato.getString(NOME_COLONNA_CAP));
                
                for(TipoIntervento t:leggiTipiIntervento(id))
                    i.addTipoIntervento(t);
                
                return i;
            }


        } catch (SQLException ex) {
            Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            //trace
	    System.out.println("Interventi -> ERRORE: Intervento n." + String.valueOf(id) + " non trovato.");

        }
        return null;
    }
    
     public boolean alterTableTipiIntervento(ArrayList<String> nomeCampi) {
        boolean change = false;   
        boolean ok = true;
        String nomeTabella = nomeCampi.get(0);
        String campo1 = nomeCampi.get(1);
        String campo2 = nomeCampi.get(2);
        String campo3 = nomeCampi.get(3);
        String campo4 = nomeCampi.get(4);
        String campo5 = nomeCampi.get(5);
        String campo6 = nomeCampi.get(6);
        if(!campo1.equals(NOME_COLONNA_ID_TIPO_INTERVENTO)
                || !campo2.equals(NOME_COLONNA_IDINT_TIPO) || !campo3.equals(NOME_COLONNA_NOME_TIPO)
                || !campo4.equals(NOME_COLONNA_VALORE_RILEVATO) 
                || !campo5.equals(NOME_COLONNA_TEMPO_INTERVENTO)
                || !campo6.equals(NOME_COLONNA_NOTE_TIPO)) {
            change = true;
            if(!campo1.equals(NOME_COLONNA_ID_TIPO_INTERVENTO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ID_TIPO_INTERVENTO + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_IDINT_TIPO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_IDINT_TIPO + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NOME_COLONNA_NOME_TIPO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_NOME_TIPO + " RENAME TO " + campo3);
            }
            if(!campo4.equals(NOME_COLONNA_VALORE_RILEVATO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_VALORE_RILEVATO + " RENAME TO " + campo4);
            }
            if(!campo5.equals(NOME_COLONNA_TEMPO_INTERVENTO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_TEMPO_INTERVENTO + " RENAME TO " + campo5);
            }
            if(!campo6.equals(NOME_COLONNA_NOTE_TIPO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_NOTE_TIPO + " RENAME TO " + campo6);
            }
        }
        if(!nomeTabella.equals(NOME_TABELLA_TIPI_INTERVENTI)){
            change = true;
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_TIPI_INTERVENTI
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePathTipi), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2 + "\n"
                        + campo3 + "\n"
                        + campo4 + "\n"
                        + campo5 + "\n"
                        + campo6);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFilesTipiInterventi();
        }
        return ok;
    }
    
    public boolean alterTableInterventi(ArrayList<String> nomeCampi) {
        boolean change = false; 
        boolean ok = true;
        String nomeTabella = nomeCampi.get(0);
        String campo1 = nomeCampi.get(1);
        String campo2 = nomeCampi.get(2);
        String campo3 = nomeCampi.get(3);
        String campo4 = nomeCampi.get(4);
        String campo5 = nomeCampi.get(5);
        String campo6 = nomeCampi.get(6);
        String campo7 = nomeCampi.get(7);
        String campo8 = nomeCampi.get(8);
        String campo9 = nomeCampi.get(9);
        String campo10 = nomeCampi.get(10);
        View viste = null;
            try {
                viste = new View();
            } catch (MainException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            if(!campo1.equals(NOME_COLONNA_ID)
                || !campo2.equals(NOME_COLONNA_ID_PAZIENTE) || !campo3.equals(NOME_COLONNA_ID_INFERMIERE)
                || !campo4.equals(NOME_COLONNA_CITTA) || !campo5.equals(NOME_COLONNA_CIVICO)
                || !campo6.equals(NOME_COLONNA_CAP) || !campo7.equals(NOME_COLONNA_DATA)
                || !campo8.equals(NOME_COLONNA_ORA_INIZIO) || !campo9.equals(NOME_COLONNA_ORA_FINE)
                || !campo10.equals(NOME_COLONNA_STORICO)) {
            change = true;
            viste.destroyView();
            if(!campo1.equals(NOME_COLONNA_ID)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ID + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_ID_PAZIENTE)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ID_PAZIENTE + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NOME_COLONNA_ID_INFERMIERE)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ID_INFERMIERE + " RENAME TO " + campo3);
            }
            if(!campo4.equals(NOME_COLONNA_CITTA)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_CITTA + " RENAME TO " + campo4);
            }
            if(!campo5.equals(NOME_COLONNA_CIVICO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_CIVICO + " RENAME TO " + campo5);
            }
            if(!campo6.equals(NOME_COLONNA_CAP)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_CAP + " RENAME TO " + campo6);
            }
            if(!campo7.equals(NOME_COLONNA_DATA)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_DATA + " RENAME TO " + campo7);
            }
            if(!campo8.equals(NOME_COLONNA_ORA_INIZIO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ORA_INIZIO + " RENAME TO " + campo8);
            }
            if(!campo9.equals(NOME_COLONNA_ORA_FINE)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_ORA_FINE + " RENAME TO " + campo9);
            }
            if(!campo10.equals(NOME_COLONNA_STORICO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI 
                        + " ALTER COLUMN " + NOME_COLONNA_STORICO + " RENAME TO " + campo10);
            }
        }
        if(!nomeTabella.equals(NOME_TABELLA_INTERVENTI)){
            change = true;
             viste.destroyView();
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_INTERVENTI
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){
            
            try {
                FileFactory.setFileContent(new File(filePath), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2 + "\n"
                        + campo3 + "\n"
                        + campo4 + "\n"
                        + campo5 + "\n"
                        + campo6 + "\n"
                        + campo7 + "\n"
                        + campo8 + "\n"
                        + campo9 + "\n"
                        + campo10);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFiles();
             
        }
        if(change != false)
            viste.createView();
        return ok;
    }
    
    public boolean alterTablePatTipiInterventi(ArrayList<String> nomeCampi) {
        boolean change = false;   
        boolean ok = true;
        String nomeTabella = nomeCampi.get(0);
        String campo1 = nomeCampi.get(1);
        String campo2 = nomeCampi.get(2);
        String campo3 = nomeCampi.get(3);
        String campo4 = nomeCampi.get(4);
        if(!campo1.equals(NOME_COLONNA_ID_PAT_INTERVENTO)
                || !campo2.equals(NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI) 
                || !campo3.equals(NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI)
                || !campo4.equals(NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI) ) {
            change = true;
            if(!campo1.equals(NOME_COLONNA_ID_PAT_INTERVENTO)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " ALTER COLUMN " + NOME_COLONNA_ID_PAT_INTERVENTO + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " ALTER COLUMN " + NOME_COLONNA_ID_TIPO_INTERVENTO_PATOLOGIE_TIPI_INTERVENTI 
                        + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " ALTER COLUMN " + NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI 
                        + " RENAME TO " + campo3);
            }
            if(!campo4.equals(NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " ALTER COLUMN " + NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI 
                        + " RENAME TO " + campo4);
            }

        }
        if(!nomeTabella.equals(NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI)){
            change = true;
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){

            try {
                FileFactory.setFileContent(new File(filePathPatologie), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2 + "\n"
                        + campo3 + "\n"
                        + campo4);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFilesPatTipiInterventi();
        }
        return ok;
    }

    public void addIntStorico(int id) {
        System.out.println("update()");
	//------------------------------------------------
	String[] fieldsValue = {
			NOME_COLONNA_STORICO + " = 1"
		};	
	try {
            PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getUpdateSQL(fieldsValue, 
            NOME_COLONNA_ID + " = " + id));
            stmt.executeUpdate();

	} catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public AbstractTableModel getStorico(int id) {
        System.out.println("read()");
        try {
            String query = mysqlConfig.getSelectSQL(NOME_COLONNA_ID_INFERMIERE + " = " + id,
                    NOME_COLONNA_STORICO + " = 1");
            return linkDb.getTabella(query);
        } catch (NumberFormatException e){
            System.err.println("Ops... Il database e la DAO non sono compatibili");
            e.printStackTrace();
            return null;
	}
    }

        public int contaInterventi(int id) {
            try {
                ResultSet risultato = linkDb.getResultSet("SELECT *"
                        + " FROM " + NOME_TABELLA_INTERVENTI +
                        " WHERE " + NOME_COLONNA_DATA + " >= CURDATE() AND "
                        + NOME_COLONNA_ID_INFERMIERE + " = " + id); 
               int cont = 0;
                if(risultato.first()){
                   for(;!(risultato.isAfterLast()); risultato.next()){
                       String oraInizio = risultato.getString(NOME_COLONNA_ORA_INIZIO);
                       oraInizio = DateFormatConverter.cambiaFormato(oraInizio, 
                               "HH:mm:ss", DateFormatConverter.getFormatOra());
                       String data = risultato.getString(NOME_COLONNA_DATA);
                       data = DateFormatConverter.cambiaFormato(data, 
                               "yyyy-MM-dd", DateFormatConverter.getFormatData());
                       String dataOra = data + " " + oraInizio;
                       long longDate = DateFormatConverter.dateString2long(dataOra, 
                               DateFormatConverter.Dateformat());
                       long oggi = DateFormatConverter.oggi();
                       if(longDate - oggi >= 0)
                           cont++;
                   }
               }
                /*risultato.first();
                return risultato.getInt(1);*/
                return cont;
            } catch (SQLException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
    }

}
