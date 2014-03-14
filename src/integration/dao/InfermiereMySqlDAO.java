package integration.dao;

import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.FileFactory;
import integration.LinkingDb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import messaggistica.MainException;

import business.TO;
import business.infermiere.InfermiereTO;
import integration.View;
import static integration.dao.AbstractDAO.linkDb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Il DAO per mysql che si occupa delle operazioni sulla tabella infermiere presente nel database.
*/
public class InfermiereMySqlDAO extends AbstractDAO{
	
        public static String TABLE_NAME = "";
	public static String COLUMN_ID_NAME = "";
	public static String COLUMN_NAME_NAME = "";
	public static String COLUMN_SURNAME_NAME = "";
        private static String filePath = "file/infermieri.txt";
                
	/**Costante protetta per i campi della query insert*/
	protected final static String[] FIELDS_INSERT = {
		COLUMN_NAME_NAME,
		COLUMN_SURNAME_NAME
	};
	//Se cambio il numero di FIELDS_INSERT, devo cambiare anche FIELDS_QUERY
	/**Costante protetta per i campi della query select, include i campi della costante
	 * {@link FIELDS_INSERT}*/
	protected final static String[] FIELDS_QUERY = {
		COLUMN_ID_NAME,
		FIELDS_INSERT[0],
		FIELDS_INSERT[1]
	};
	/**Costante per il nome della tabella degli infermieri*/
	protected final static String TABLE = readTableName();

	//--------------------------------
	private int id;
	private String name;
	private String surname;
	//--------------------------------
        
        /**Grazie a questo oggetto ottiene le query già ben formate pronte per essere
	 * date in pasto al database*/
	private MySqlDAOConfig mysqlConfig = new MySqlDAOConfig(FIELDS_INSERT, FIELDS_QUERY, TABLE);
        
        private void loadVariables(String[] nomeCampi){
        	TABLE_NAME = nomeCampi[0];
        	COLUMN_ID_NAME = nomeCampi[1];
        	COLUMN_NAME_NAME = nomeCampi[2];
        	COLUMN_SURNAME_NAME = nomeCampi[3];
            FIELDS_INSERT[0] = COLUMN_NAME_NAME + ", ";
            FIELDS_INSERT[1] = COLUMN_SURNAME_NAME;
            FIELDS_QUERY[0] = COLUMN_ID_NAME + ", ";
            FIELDS_QUERY[1] = FIELDS_INSERT[0];
            FIELDS_QUERY[2] = FIELDS_INSERT[1];
        }
        	
        private static String readTableName(){
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));
                return scanner.next();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
        
         public void readFiles() {
            try {
   
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));

                int i;
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
                loadVariables(nomeCampi);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        

	/**
	 * Costruttore, nel caso in cui non esiste una connessione al db (oggetto {@link LinkingDb})
	 * ne crea una, altrimenti usa quella statica della classe AbstractDAO da cui questa eredita
	 * @throws MainException il file di configurazione non esiste, non può essere letto oppure 
	 * non può essere modificato
	*/
	public InfermiereMySqlDAO() throws MainException{
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
		InfermiereTO to = (InfermiereTO) interfaceTo;
		
		//---------------------------
		name = to.getNome();
		surname = to.getCognome();
		
		/*if(nome.length() > MAX_CHARS){
			nome = to.getNome().substring(0, MAX_CHARS);
		}
		if(cognome.length() > MAX_CHARS){
			cognome = to.getCognome().substring(0, MAX_CHARS);
		}*/
		
		//---------------------------
		
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getInsertSQL());
			
			//----------------------------
			stmt.setString(1, name);
			stmt.setString(2, surname);
			//---------------------------
			
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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
		System.out.println("read()");
		ArrayList<TO> nurses = new ArrayList<TO>();//arraylist dei to da riempire;
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getSelectSQL("true"));
			ArrayList<String[]> dates = linkDb.select(stmt);//arraylist sui dati del database
			Iterator<String[]> iterDates = dates.iterator();
			while(iterDates.hasNext()){
				String[] record = iterDates.next();
				
				
				InfermiereTO infTO = new InfermiereTO();//crea il nuovo TO con i dati prelevati
				
				id = Integer.parseInt(record[0]);
				name = record[1];
				surname = record[2];
				
				//setta i valori del database --------------
				infTO.setID(id);
				infTO.setNome(name);
				infTO.setCognome(surname);
				//------------------------------------------
				
				//aggiunge il nuovo TO all'arraylist
				nurses.add(infTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e){
			System.err.println("Ops... Il database e la DAO non sono compatibili");
			e.printStackTrace();
			return null;
		}
		return nurses;
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
        
	/**
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
		InfermiereTO to = (InfermiereTO) interfaceTO;
		
		//------------------------------------------------
		name = to.getNome();
		surname = to.getCognome();
		String[] fieldsValue = {
				COLUMN_NAME_NAME + " = '" + name + "', ",
				COLUMN_SURNAME_NAME + " = '" + surname + "'"
		};
		//------------------------------------------------
		
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getUpdateSQL(fieldsValue, 
					COLUMN_ID_NAME + " = " + to.getID()));
			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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
	

        public boolean existingID(int ID){
		//Preparazione query
		String stat= "SELECT COUNT(" + COLUMN_ID_NAME + ") FROM " + TABLE_NAME +" WHERE "
                        + COLUMN_ID_NAME + " = " + ID;

		//Esecuzione query
		ResultSet countTab = linkDb.getResultSet(stat);

		try {
			//Estrazione valore
			countTab.first();
			if (countTab.getInt(1)==1) return true;
			else return false;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE RICERCA ID!!!");

			//TRACE
			System.out.println("Infermieri -> ERRORE NELLA RICERCA DELL'ID NELLA TABELLA " + TABLE_NAME);

			return false;
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
	
     public boolean alterTable(ArrayList<String> fieldsName) {
     boolean change = false;
     boolean ok = true;
     String tableName = fieldsName.get(0);
     String field1 = fieldsName.get(1);
     String field2 = fieldsName.get(2);
     String field3 = fieldsName.get(3);
     View views = null;
            try {
            	views = new View();
            } catch (MainException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
     if(!field1.equals(COLUMN_ID_NAME)
                || !field2.equals(COLUMN_NAME_NAME) || !field3.equals(COLUMN_SURNAME_NAME)) {
            change = true;
            views.destroyView();
            if(!field1.equals(COLUMN_ID_NAME)){
                ok = linkDb.execute("ALTER TABLE " + TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_ID_NAME + " RENAME TO " + field1);
            }
            if(!field2.equals(COLUMN_NAME_NAME)){
                ok = linkDb.execute("ALTER TABLE " + TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_NAME_NAME + " RENAME TO " + field2);
            }
            if(!field3.equals(COLUMN_SURNAME_NAME)){
                ok = linkDb.execute("ALTER TABLE " + TABLE_NAME 
                        + " ALTER COLUMN " + COLUMN_SURNAME_NAME + " RENAME TO " + field3);
            }
        }
        if(!tableName.equals(TABLE_NAME)){
            change = true;
            views.destroyView();
                ok = linkDb.execute("ALTER TABLE " + TABLE_NAME
                        + " RENAME TO " + tableName + ";");
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePath), 
                		tableName + "\n"
                        + field1 + "\n"
                        + field2 + "\n"
                        + field3);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InfermiereMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            readFiles();
        }
        if(change != false)
        	views.createView();
      return ok;
    }//fine metodo

    @Override
    public TO getSpecified(int id) {
        //Preparazione ed esecuzione query
		String queryInt = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_NAME + 
                        "=" + id + ";";
		ResultSet nurseTable = linkDb.getResultSet(queryInt);
		try {		
			//Posizionamento del cursore sull'unica riga
			nurseTable.first();

			//Popolamento di un oggetto di tipo Infermiere;
			InfermiereTO i = new InfermiereTO();

			i.setID(id);
			i.setNome(nurseTable.getString(COLUMN_NAME_NAME));
			i.setCognome(nurseTable.getString(COLUMN_SURNAME_NAME));

			//Restituzione dell'oggetto
			return i;
		} catch (SQLException e) {
			//Notifica errore
			String errorMessage ="Infermieri -> Errore: impossibile estrarre l'infermiere dal DB";
			System.out.println(errorMessage);
			e.printStackTrace();
			return null;
		}
    }
    
    public ADISysTableModel getTabellaInfoInfermieriConInterventi() {
        return linkDb.getTable("SELECT * FROM INTERV_INFERM");
    }
    
}
