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
	
        
        public static String INTERVENTION_TABLE_NAME = "";
        
	public static String COLUMN_ID_NAME = "";
	public static String PATIENT_COLUMN_ID_NAME = "";
	public static String NURSE_COLUMN_ID_NAME = "";

	public static String COLUMN_CITY_NAME = "";
	public static String COLUMN_HOUSE_NUMBER_NAME = "";
	public static String COLUMN_POSTCODE_NAME = "";

	public static String COLUMN_DATE_NAME = "";
	public static String COLUMN_START_TIME_NAME = "";
	public static String COLUMN_END_TIME_NAME = "";
        public static String COLUMN_REGISTER_NAME = "";

	public static String INTERVENTIONS_TYPES_TABLE_NAME = "";
        
	public static String COLUMN_INTERVENTION_TYPE_ID_NAME ="";
	public static String COLUMN_IDINT_TYPE_NAME = "";
	public static String TYPE_NAME_COLUMN_NAME = "";
	public static String COLUMN_OBSERVED_VALUE_NAME = "";
	public static String COLUMN_INTERVENTION_DURATION_NAME = "";
	public static String COLUMN_TYPE_NOTES_NAME = "";
         
	public static String PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME = "";
	public static String COLUMN_ID_INTERVENTION_PATHOLOGY_NAME = "";
        public static String COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME = "";
	public static String COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME = "";
        public static String COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME = "";
        
        //COSTANTI DI FORMATO
	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	private static final String SQL_TIME_FORMAT = "HH:mm:ss";
        public final static int diffMax = 120;
	public static final int intBygone = -1;
	public static final int intNotChangeable = 0;
	public static final int intChangeable = 1;
        
        private static String filePath = "file/interventi.txt";
        private static String filePathTypes = "file/tipiInterventi.txt";
        private static String filePathPathologies = "file/patTipiInterventi.txt";
        
	/**Costante protetta per i campi della query insert*/
	protected final static String[] FIELDS_INSERT = {
		PATIENT_COLUMN_ID_NAME,
		NURSE_COLUMN_ID_NAME,
		COLUMN_CITY_NAME,
		COLUMN_HOUSE_NUMBER_NAME,
		COLUMN_POSTCODE_NAME,
		COLUMN_DATE_NAME,
		COLUMN_START_TIME_NAME
	};
	//Se cambio il numero di FIELDS_INSERT, devo cambiare anche FIELDS_QUERY
	/**Costante protetta per i campi della query select, include i campi della costante
	 * {@link FIELDS_INSERT}*/
	protected final static String[] FIELDS_QUERY = {
		COLUMN_ID_NAME,
		FIELDS_INSERT[0],
		FIELDS_INSERT[1],
                FIELDS_INSERT[2],
                FIELDS_INSERT[3],
                FIELDS_INSERT[4],
                FIELDS_INSERT[5],
                FIELDS_INSERT[6]
	};
	/**Costante per il nome della tabella degli infermieri*/
	protected final static String TABLE = readTableName();
        
               
	/**Grazie a questo oggetto ottiene le query già ben formate pronte per essere
	 * date in pasto al database*/
	private MySqlDAOConfig mysqlConfig = new MySqlDAOConfig(FIELDS_INSERT, FIELDS_QUERY, TABLE);
	
        private static String readTableName(){
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
       
        private void loadVariables(String[] fieldsName){
            
        	INTERVENTION_TABLE_NAME = fieldsName[0];
        	COLUMN_ID_NAME = fieldsName[1];
        	PATIENT_COLUMN_ID_NAME = fieldsName[2];
        	NURSE_COLUMN_ID_NAME = fieldsName[3];
        	COLUMN_CITY_NAME = fieldsName[4];
        	COLUMN_HOUSE_NUMBER_NAME = fieldsName[5];
        	COLUMN_POSTCODE_NAME = fieldsName[6];
        	COLUMN_DATE_NAME = fieldsName[7];
        	COLUMN_START_TIME_NAME = fieldsName[8];
        	COLUMN_END_TIME_NAME = fieldsName[9];
        	COLUMN_REGISTER_NAME = fieldsName[10];
            
            FIELDS_INSERT[0] = PATIENT_COLUMN_ID_NAME + ", ";
            FIELDS_INSERT[1] = NURSE_COLUMN_ID_NAME + ", ";
            FIELDS_INSERT[2] = COLUMN_CITY_NAME + ", ";
            FIELDS_INSERT[3] = COLUMN_HOUSE_NUMBER_NAME + ", ";
            FIELDS_INSERT[4] = COLUMN_POSTCODE_NAME + ", ";
            FIELDS_INSERT[5] = COLUMN_DATE_NAME + ", ";
            FIELDS_INSERT[6] = COLUMN_START_TIME_NAME;
            FIELDS_QUERY[0] = COLUMN_ID_NAME + ", ";
            FIELDS_QUERY[1] = FIELDS_INSERT[0];
            FIELDS_QUERY[2] = FIELDS_INSERT[1];
            FIELDS_QUERY[3] = FIELDS_INSERT[2];
            FIELDS_QUERY[4] = FIELDS_INSERT[3];
            FIELDS_QUERY[5] = FIELDS_INSERT[4];
            FIELDS_QUERY[6] = FIELDS_INSERT[5];
            FIELDS_QUERY[7] = FIELDS_INSERT[6];
        }
        
        public void readFiles() {
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));

                int i = 0;
                String[] fieldsName = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[fieldsName.length + 1];
                    for (i = 0; i < fieldsName.length; i++) 
                        temp[i] = fieldsName[i];
                    fieldsName = temp;
                    fieldsName[fieldsName.length - 1] = newCampo;
                }
                loadVariables(fieldsName);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            readFilesInterventionsTypes();
            readFilesPatInterventionsTypes();
        }
        
        private void loadVariablesInterventionsTypes(String[] fieldsName){
        	INTERVENTIONS_TYPES_TABLE_NAME = fieldsName[0];
        	COLUMN_INTERVENTION_TYPE_ID_NAME = fieldsName[1];
        	COLUMN_IDINT_TYPE_NAME = fieldsName[2];
        	TYPE_NAME_COLUMN_NAME = fieldsName[3];
        	COLUMN_OBSERVED_VALUE_NAME = fieldsName[4];
        	COLUMN_INTERVENTION_DURATION_NAME = fieldsName[5];
        	COLUMN_TYPE_NOTES_NAME = fieldsName[6];
        }
        
        public void readFilesInterventionsTypes() {
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathTypes));

                int i = 0;
                String[] fieldsName = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[fieldsName.length + 1];
                    for (i = 0; i < fieldsName.length; i++) 
                        temp[i] = fieldsName[i];
                    fieldsName = temp;
                    fieldsName[fieldsName.length - 1] = newCampo;
                }
                loadVariablesInterventionsTypes(fieldsName);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        private void loadVariablesPatInterventionsTypes(String[] fieldsName){
        	PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME = fieldsName[0];
        	COLUMN_ID_INTERVENTION_PATHOLOGY_NAME = fieldsName[1];
        	COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME = fieldsName[2];
        	COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME = fieldsName[3];
        	COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME = fieldsName[4];
        }
        
        public void readFilesPatInterventionsTypes() {
            try {
                                    //JOptionPane.showMessageDialog(null, FileFactory.getFileContent(name));
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathPathologies));

                int i = 0;
                String[] fieldsName = new String[0];
                while(scanner.hasNext()){
                    String newCampo = scanner.next();
                    //System.out.println(newCampo);
                   
                    String[] temp = new String[fieldsName.length + 1];
                    for (i = 0; i < fieldsName.length; i++) 
                        temp[i] = fieldsName[i];
                    fieldsName = temp;
                    fieldsName[fieldsName.length - 1] = newCampo;
                }
                loadVariablesPatInterventionsTypes(fieldsName);
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
		readFiles();
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
				NURSE_COLUMN_ID_NAME,
				PATIENT_COLUMN_ID_NAME,

				COLUMN_CITY_NAME,
				COLUMN_HOUSE_NUMBER_NAME,
				COLUMN_POSTCODE_NAME,

				COLUMN_DATE_NAME,
				COLUMN_START_TIME_NAME
				).toString();


		String elencoValori= f2.format(formatoElencoValori,
				String.valueOf(to.getIDInfermiere()) ,
				String.valueOf(to.getIDPaziente()),

				linkDb.string2sqlstring(to.getCitta()),
				linkDb.string2sqlstring(to.getCivico()),
				linkDb.string2sqlstring(to.getCap()),

				to.getDataDaFormato(SQL_DATE_FORMAT),
				to.getOraInizioDaFormato(SQL_TIME_FORMAT)

				).toString();

		//Composizione istruzione 
		String istruzione = "INSERT INTO "+ INTERVENTION_TABLE_NAME+" (" + elencoColonne + ") VALUES ("+ elencoValori+ ");";

		//Trace
		System.out.println("SQL: " + istruzione.toUpperCase());

		//Esecuzione
		if(linkDb.execute(istruzione))
		{
			//Caso inserimento dati di base eseguito con successo -> Inserimento dei tipi di intervento

			//Trace
			System.out.println("InterventoMySqlDAO -> Intervento creato con successo, inserimento tipi in corso...");

			//TODO Inserire valori tipi intervento nel caso di inserimento andato a buon fine.
			//Recupero id dell'intervento:
			ResultSet idAssegnato = linkDb.getResultSet("SELECT MAX(" + COLUMN_ID_NAME + ") FROM " 
                                + INTERVENTION_TABLE_NAME);

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
				if(insertInterventionType(newID, to.getTipoIntervento(i).getNome(), to.getTipoIntervento(i).getNote()))
				{
					
					ResultSet idTipoIntervento = linkDb.getResultSet("SELECT MAX(" 
                                                + COLUMN_INTERVENTION_TYPE_ID_NAME + ") FROM " + INTERVENTIONS_TYPES_TABLE_NAME);

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
                                        if(insertPathologiesInterventionType(lastInsertId, patologieTipoIntervento.get(k).getCodice(), 
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
        
        private boolean insertInterventionType(int idIntervento, String nomeTipo, String noteTipo) 
	{
		//TODO Inserimento del tipo di intervento:

		//Composizione colonne
		String colonna1 = COLUMN_IDINT_TYPE_NAME;
		String colonna2 = TYPE_NAME_COLUMN_NAME;
		String colonna3 = COLUMN_TYPE_NOTES_NAME;
		String colonna4 = COLUMN_INTERVENTION_DURATION_NAME;
		String colonna5 = COLUMN_OBSERVED_VALUE_NAME;


		String elencoColonneTipi = "("+ colonna1 +"," + colonna2 +"," + colonna3 + "," + colonna4 +"," + colonna5 + ")";

		//Composizione valori
		String valore1 = String.valueOf(idIntervento);
		String valore2 = "'" + linkDb.string2sqlstring(nomeTipo) + "'";
		String valore3 = "'" + linkDb.string2sqlstring(noteTipo) + "'";
		String valore4 = "null";
		String valore5 = "null";

		String elencoValoriTipi = "("+ valore1 +"," + valore2 +"," + valore3 +"," + valore4 +"," + valore5 +")";

		//Composizione predicato SQL
		String predicatoInserimentoTipo= "INSERT INTO " + INTERVENTIONS_TYPES_TABLE_NAME + elencoColonneTipi +" VALUES " + elencoValoriTipi + ";";

		//Trace
		System.out.println("SQL: " + predicatoInserimentoTipo);

		//Esecuzione predicato con incremento indice in caso di successo.
		if(linkDb.execute(predicatoInserimentoTipo))
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
        
        private boolean insertPathologiesInterventionType(int idIntervento, String codicePatologia, int gravita) 
	{
		//TODO Inserimento del tipo di intervento:

		//Composizione colonne
		String colonna1 = COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME;
		String colonna2 = COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME;
                String colonna3 = COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME;

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
		String predicatoInserimentoTipo= "INSERT INTO " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME 
                        + elencoColonneTipi +" VALUES " + elencoValoriTipi + ";";

		//Trace
		System.out.println("SQL: " + predicatoInserimentoTipo);


		if(linkDb.execute(predicatoInserimentoTipo))
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
				COLUMN_ID_NAME,
				NURSE_COLUMN_ID_NAME,
                                PATIENT_COLUMN_ID_NAME,

                                COLUMN_CITY_NAME,
                                COLUMN_HOUSE_NUMBER_NAME,
                                COLUMN_POSTCODE_NAME,

                                COLUMN_DATE_NAME,
                                COLUMN_START_TIME_NAME
				).toString();
		//Recupero dati sugli interventi dell'infermiere
		String queryText= "SELECT " + elencoColonne + " FROM " + INTERVENTION_TABLE_NAME; 
		if(id != -1) {
			queryText = queryText + " WHERE " + NURSE_COLUMN_ID_NAME + "=" + id;
			yesId = 1;
		}
		if(!dataDal.equals("") || !dataAl.equals("")){
			if(yesId == 0)
				queryText = queryText + " WHERE ";
			else
				queryText = queryText + " AND ";
			if(!dataDal.equals("") && dataAl.equals("")){
				String testoData = DateFormatConverter.cambiaFormato(dataDal.toString(), 
                                        DateFormatConverter.getFormatData(),  SQL_DATE_FORMAT);
				queryText = queryText + COLUMN_DATE_NAME + " >= '" + testoData + "'";
			}
			else if(dataDal.equals("") && !dataAl.equals("")){
				String testoData = DateFormatConverter.cambiaFormato(dataAl.toString(), 
                                        DateFormatConverter.getFormatData(),  SQL_DATE_FORMAT);
				queryText = queryText + COLUMN_DATE_NAME + " <= '" + testoData + "'";
			}
			else {
				String testoDataDal = DateFormatConverter.cambiaFormato(dataDal.toString(), 
                                        DateFormatConverter.getFormatData(),  SQL_DATE_FORMAT);
				String testoDataAl = DateFormatConverter.cambiaFormato(dataAl.toString(), 
                                        DateFormatConverter.getFormatData(),  SQL_DATE_FORMAT);
				
				long mllsDataDal = DateFormatConverter.dateString2long(dataDal, DateFormatConverter.getFormatData());
				long mllsDataAl = DateFormatConverter.dateString2long(dataAl, DateFormatConverter.getFormatData());
				
				long diff = mllsDataAl - mllsDataDal;
				boolean maggDataAl = true;
				if(diff < 0)
					maggDataAl = false;
				queryText = queryText + COLUMN_DATE_NAME + 
						" BETWEEN '";
				if(maggDataAl == true){
					queryText = queryText + testoDataDal + "'" +
							" AND '" + testoDataAl + "'";
				} else {
					queryText = queryText + testoDataAl + "'" +
							" AND '" + testoDataDal + "'";
                                        queryText = queryText + " ORDER BY " + COLUMN_DATE_NAME + " DESC";
                                        return linkDb.getTable(queryText);
				}
				
			}
		}	
		queryText = queryText + " ORDER BY " + COLUMN_DATE_NAME;	
		return linkDb.getTable(queryText);

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
                    return linkDb.getTable(query);
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
				NURSE_COLUMN_ID_NAME, String.valueOf(to.getIDInfermiere()) ,
				PATIENT_COLUMN_ID_NAME, String.valueOf(to.getIDPaziente()),

				COLUMN_CITY_NAME, linkDb.string2sqlstring(to.getCitta()),
				COLUMN_HOUSE_NUMBER_NAME, linkDb.string2sqlstring(to.getCivico()),
				COLUMN_POSTCODE_NAME, to.getCap(),

				COLUMN_DATE_NAME, to.getDataDaFormato(SQL_DATE_FORMAT),
				COLUMN_START_TIME_NAME, to.getOraInizioDaFormato(SQL_TIME_FORMAT)
				).toString();


		//Composizione istruzione 
		String istruzione = "UPDATE " + INTERVENTION_TABLE_NAME +" SET " + elencoUpdate +" WHERE " 
                        + COLUMN_ID_NAME + " = " + to.getID()+ ";";

		//Trace
		System.out.println("SQL: " + istruzione.toUpperCase());

		//Esecuzione
		if(linkDb.execute(istruzione))
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
				if( insertInterventionType(to.getID(), to.getTipoIntervento(i).getNome(), 
                                        to.getTipoIntervento(i).getNote()))
				{
					
					ResultSet idTipoIntervento = linkDb.getResultSet("SELECT MAX("
                                                + COLUMN_INTERVENTION_TYPE_ID_NAME + ") FROM " + INTERVENTIONS_TYPES_TABLE_NAME);

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
                                        if(insertPathologiesInterventionType(lastInsertId, patologieTipoIntervento.get(k).getCodice(),
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
                        + INTERVENTIONS_TYPES_TABLE_NAME + " WHERE " 
                        + COLUMN_IDINT_TYPE_NAME + "=" + String.valueOf(IDIntervento)+";";

		//Esecuzione
		return linkDb.execute(predicato); 
	}
	
        private boolean cancellaPatologieTipoIntervento(int IDTipoIntervento)
	{
		//Preparazione predicato
		String predicato= "DELETE FROM " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME + 
                        " WHERE " + COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME + "=" 
                        + String.valueOf(IDTipoIntervento)+";";

		//Esecuzione
		return linkDb.execute(predicato); 
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
			return delete(COLUMN_ID_NAME + " = " + id);
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
	
        public ArrayList<TipoIntervento> readInterventionsTypes(int IDIntervento)
	{
		//Preparazione query
		String query= "SELECT * FROM " + INTERVENTIONS_TYPES_TABLE_NAME + " WHERE " + COLUMN_IDINT_TYPE_NAME +"=" + String.valueOf(IDIntervento) + ";";

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
					int idTipoIntervento = tabella.getInt(COLUMN_INTERVENTION_TYPE_ID_NAME);
                                        nuovoTipo.setID(idTipoIntervento);
					nuovoTipo.setNome(tabella.getString(TYPE_NAME_COLUMN_NAME));
					nuovoTipo.setValoreRilevato(tabella.getString(COLUMN_OBSERVED_VALUE_NAME));
					nuovoTipo.setTempoIntervento(tabella.getString(COLUMN_INTERVENTION_DURATION_NAME));
					nuovoTipo.setNote(tabella.getString(COLUMN_TYPE_NOTES_NAME));
					
					//Preparazione query
					query = "SELECT * FROM " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME +
							" JOIN " + PatologiaMySqlDAO.NOME_TABELLA + " ON " +
							PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME + "." 
							+ COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME + " = " +
							PatologiaMySqlDAO.NOME_TABELLA + "." + PatologiaMySqlDAO.NOME_COLONNA_ID +
							" WHERE " + COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME 
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
                                                                    tabellaPatologie.getInt(COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME)));
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
			return intBygone;
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
			 		"SELECT MIN( " + COLUMN_START_TIME_NAME + 
			 		" ) AS ORARIO FROM " + INTERVENTION_TABLE_NAME + " " +
			 		" WHERE " + NURSE_COLUMN_ID_NAME + " = " + idInfermiere +
			 		" AND " + COLUMN_DATE_NAME + " = CURDATE() ";
			 
			 ResultSet risultato = linkDb.getResultSet(query);
			 
			 risultato.next();
			  
			 String orarioMin = risultato.getString("ORARIO");
			 
			 if(orarioMin != null){
				 
				 /*String currentHour = DateFormatConverter.long2dateString(DateFormatConverter.oggi(),
						 DateFormatConverter.getFormatOra());*/
				 
				 long longOra = DateFormatConverter.dateString2long(data + " " + orarioMin, 
						 DateFormatConverter.getFormatData() + " " + SQL_TIME_FORMAT);
					
					//differenza tra ora dell'intervento e ora corrente in millisecondi
					long longDiff = (longOra - today);
				
					int minuti = (int) ((longDiff / 3600000) * 60) + (int) (longDiff / 60000 % 60);
					if(minuti < diffMax) {
						System.out.println("Intervento non modificabile a meno di due ore");
						return intNotChangeable;
					} else {
						String testoDiff = (int) (longDiff / 3600000) + " ora/e e " +
								(int) (longDiff / 60000 % 60) + " minuto/i";
						System.out.println("Ora inizio primo intervento odierno: " + orarioMin);
						System.out.println("Mancano " + testoDiff + " dall'inizio del primo intervento odierno " +
								"dell'infermiere" + idInfermiere + ": intervento modificabile");
						return intChangeable;
					} //else su orario inferiore alle 2 ore
				}//if su risultato.first()
	        } catch (SQLException ex) {
	            Logger.getLogger(InterventoMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
	            //trace
		    System.out.println("Interventi -> ERRORE: Intervento n." + String.valueOf(idInfermiere) + " non trovato.");
	        }
		}
		}//else su intervento già passato
		return intChangeable;
	}
        
      public ArrayList<PatologiaTO> leggiPatologieIntervento(int IDIntervento)
	{
		//Preparazione query
		String query = "SELECT DISTINCT P. " + PatologiaMySqlDAO.NOME_COLONNA_CODICE + ", P."
                       + PatologiaMySqlDAO.NOME_COLONNA_NOME + ", PT." + COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME
                       + " FROM " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME + " PT "
                       + "JOIN " + INTERVENTIONS_TYPES_TABLE_NAME + " T ON PT." 
                       + COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME
                       + " = T." + COLUMN_INTERVENTION_TYPE_ID_NAME  
                       + " JOIN " + PatologiaMySqlDAO.NOME_TABELLA + " P "
                       + " ON PT." + COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME + " = P."
                       + PatologiaMySqlDAO.NOME_COLONNA_ID
                       + " WHERE " + COLUMN_IDINT_TYPE_NAME +"=" + String.valueOf(IDIntervento) + ";";

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
                                                tabella.getInt(COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME)));
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
		String queryText= "SELECT * FROM " + INTERVENTION_TABLE_NAME; 
		if(id != -1)
			queryText = queryText + " WHERE " + NURSE_COLUMN_ID_NAME + "=" + id
                                + " AND " + COLUMN_DATE_NAME + " >= CURDATE()";
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
					i.setID(tabella.getString(COLUMN_ID_NAME));
					i.setIDInfermiere(tabella.getString(NURSE_COLUMN_ID_NAME));
					i.setIDPaziente(tabella.getString(PATIENT_COLUMN_ID_NAME));

					i.setCitta(tabella.getString(COLUMN_CITY_NAME));
					i.setCivico(tabella.getString(COLUMN_HOUSE_NUMBER_NAME));
					i.setCap(tabella.getString(COLUMN_POSTCODE_NAME));

					//TODO Controllare FORMATTAZIONE - Saltato, filtro impostato nella gui.
					i.setDataFmt(tabella.getString(COLUMN_DATE_NAME), SQL_DATE_FORMAT);
					i.setOraInizioFmt(tabella.getString(COLUMN_START_TIME_NAME), SQL_TIME_FORMAT);

					//Creazione array dei tipi dell'intervento corrente dal database
					ArrayList<TipoIntervento> listaInterventi = readInterventionsTypes(i.getID());
					
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
            String query = "SELECT * FROM " + INTERVENTION_TABLE_NAME + " WHERE " + COLUMN_ID_NAME + "=" + String.valueOf(id)+";";
            ResultSet risultato = linkDb.getResultSet(query);
            if(risultato.first())
            {
                InterventoTO i = new InterventoTO();
                i.setID(risultato.getInt(COLUMN_ID_NAME));
                i.setIDInfermiere(risultato.getInt(NURSE_COLUMN_ID_NAME));
                i.setIDPaziente(risultato.getInt(PATIENT_COLUMN_ID_NAME));
                i.setDataFmt(risultato.getString(COLUMN_DATE_NAME), SQL_DATE_FORMAT);
                i.setOraInizioFmt(risultato.getString(COLUMN_START_TIME_NAME),SQL_TIME_FORMAT);
                //i.setOraFineFmt(risultato.getString(NOME_COLONNA_ORA_FINE),FORMATO_ORA_SQL);
                i.setCitta(risultato.getString(COLUMN_CITY_NAME));
                i.setCivico(risultato.getString(COLUMN_HOUSE_NUMBER_NAME));
                i.setCap(risultato.getString(COLUMN_POSTCODE_NAME));
                
                for(TipoIntervento t:readInterventionsTypes(id))
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
        if(!campo1.equals(COLUMN_INTERVENTION_TYPE_ID_NAME)
                || !campo2.equals(COLUMN_IDINT_TYPE_NAME) || !campo3.equals(TYPE_NAME_COLUMN_NAME)
                || !campo4.equals(COLUMN_OBSERVED_VALUE_NAME) 
                || !campo5.equals(COLUMN_INTERVENTION_DURATION_NAME)
                || !campo6.equals(COLUMN_TYPE_NOTES_NAME)) {
            change = true;
            if(!campo1.equals(COLUMN_INTERVENTION_TYPE_ID_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_INTERVENTION_TYPE_ID_NAME + " RENAME TO " + campo1);
            }
            if(!campo2.equals(COLUMN_IDINT_TYPE_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_IDINT_TYPE_NAME + " RENAME TO " + campo2);
            }
            if(!campo3.equals(TYPE_NAME_COLUMN_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + TYPE_NAME_COLUMN_NAME + " RENAME TO " + campo3);
            }
            if(!campo4.equals(COLUMN_OBSERVED_VALUE_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_OBSERVED_VALUE_NAME + " RENAME TO " + campo4);
            }
            if(!campo5.equals(COLUMN_INTERVENTION_DURATION_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_INTERVENTION_DURATION_NAME + " RENAME TO " + campo5);
            }
            if(!campo6.equals(COLUMN_TYPE_NOTES_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_TYPE_NOTES_NAME + " RENAME TO " + campo6);
            }
        }
        if(!nomeTabella.equals(INTERVENTIONS_TYPES_TABLE_NAME)){
            change = true;
                ok = linkDb.execute("ALTER TABLE " + INTERVENTIONS_TYPES_TABLE_NAME
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePathTypes), 
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
            readFilesInterventionsTypes();
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
           
            if(!campo1.equals(COLUMN_ID_NAME)
                || !campo2.equals(PATIENT_COLUMN_ID_NAME) || !campo3.equals(NURSE_COLUMN_ID_NAME)
                || !campo4.equals(COLUMN_CITY_NAME) || !campo5.equals(COLUMN_HOUSE_NUMBER_NAME)
                || !campo6.equals(COLUMN_POSTCODE_NAME) || !campo7.equals(COLUMN_DATE_NAME)
                || !campo8.equals(COLUMN_START_TIME_NAME) || !campo9.equals(COLUMN_END_TIME_NAME)
                || !campo10.equals(COLUMN_REGISTER_NAME)) {
            change = true;
            viste.destroyView();
            if(!campo1.equals(COLUMN_ID_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_ID_NAME + " RENAME TO " + campo1);
            }
            if(!campo2.equals(PATIENT_COLUMN_ID_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + PATIENT_COLUMN_ID_NAME + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NURSE_COLUMN_ID_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + NURSE_COLUMN_ID_NAME + " RENAME TO " + campo3);
            }
            if(!campo4.equals(COLUMN_CITY_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_CITY_NAME + " RENAME TO " + campo4);
            }
            if(!campo5.equals(COLUMN_HOUSE_NUMBER_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_HOUSE_NUMBER_NAME + " RENAME TO " + campo5);
            }
            if(!campo6.equals(COLUMN_POSTCODE_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_POSTCODE_NAME + " RENAME TO " + campo6);
            }
            if(!campo7.equals(COLUMN_DATE_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_DATE_NAME + " RENAME TO " + campo7);
            }
            if(!campo8.equals(COLUMN_START_TIME_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_START_TIME_NAME + " RENAME TO " + campo8);
            }
            if(!campo9.equals(COLUMN_END_TIME_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_END_TIME_NAME + " RENAME TO " + campo9);
            }
            if(!campo10.equals(COLUMN_REGISTER_NAME)){
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_REGISTER_NAME + " RENAME TO " + campo10);
            }
        }
        if(!nomeTabella.equals(INTERVENTION_TABLE_NAME)){
            change = true;
             viste.destroyView();
                ok = linkDb.execute("ALTER TABLE " + INTERVENTION_TABLE_NAME
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
            readFiles();
             
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
        if(!campo1.equals(COLUMN_ID_INTERVENTION_PATHOLOGY_NAME)
                || !campo2.equals(COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME) 
                || !campo3.equals(COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME)
                || !campo4.equals(COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME) ) {
            change = true;
            if(!campo1.equals(COLUMN_ID_INTERVENTION_PATHOLOGY_NAME)){
                ok = linkDb.execute("ALTER TABLE " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME
                        + " ALTER COLUMN " + COLUMN_ID_INTERVENTION_PATHOLOGY_NAME + " RENAME TO " + campo1);
            }
            if(!campo2.equals(COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME)){
                ok = linkDb.execute("ALTER TABLE " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME
                        + " ALTER COLUMN " + COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME 
                        + " RENAME TO " + campo2);
            }
            if(!campo3.equals(COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME)){
                ok = linkDb.execute("ALTER TABLE " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME
                        + " ALTER COLUMN " + COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME 
                        + " RENAME TO " + campo3);
            }
            if(!campo4.equals(COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME)){
                ok = linkDb.execute("ALTER TABLE " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME
                        + " ALTER COLUMN " + COLUMN_PATHOLOGIES_SEVERITY_INTERVENTIONS_TYPES_NAME 
                        + " RENAME TO " + campo4);
            }

        }
        if(!nomeTabella.equals(PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME)){
            change = true;
                ok = linkDb.execute("ALTER TABLE " + PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){

            try {
                FileFactory.setFileContent(new File(filePathPathologies), 
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
            readFilesPatInterventionsTypes();
        }
        return ok;
    }

    public void addIntStorico(int id) {
        System.out.println("update()");
	//------------------------------------------------
	String[] fieldsValue = {
			COLUMN_REGISTER_NAME + " = 1"
		};	
	try {
            PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getUpdateSQL(fieldsValue, 
            		COLUMN_ID_NAME + " = " + id));
            stmt.executeUpdate();

	} catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public AbstractTableModel getStorico(int id) {
        System.out.println("read()");
        try {
            String query = mysqlConfig.getSelectSQL(NURSE_COLUMN_ID_NAME + " = " + id,
            		COLUMN_REGISTER_NAME + " = 1");
            return linkDb.getTable(query);
        } catch (NumberFormatException e){
            System.err.println("Ops... Il database e la DAO non sono compatibili");
            e.printStackTrace();
            return null;
	}
    }

        public int contaInterventi(int id) {
            try {
                ResultSet risultato = linkDb.getResultSet("SELECT *"
                        + " FROM " + INTERVENTION_TABLE_NAME +
                        " WHERE " + COLUMN_DATE_NAME + " >= CURDATE() AND "
                        + NURSE_COLUMN_ID_NAME + " = " + id); 
               int cont = 0;
                if(risultato.first()){
                   for(;!(risultato.isAfterLast()); risultato.next()){
                       String oraInizio = risultato.getString(COLUMN_START_TIME_NAME);
                       oraInizio = DateFormatConverter.cambiaFormato(oraInizio, 
                               "HH:mm:ss", DateFormatConverter.getFormatOra());
                       String data = risultato.getString(COLUMN_DATE_NAME);
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
