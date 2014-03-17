package integration.dao;
/*
import adisys.server.database.Database;
import adisys.server.database.View;*/
import adisys.server.strumenti.ADISysTableModel;
import adisys.server.strumenti.FileFactory;
import integration.LinkingDb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import messaggistica.MainException;

import business.TO;
import business.patologia.PatologiaTO;
import business.paziente.PazienteTO;
import integration.View;
import static integration.dao.AbstractDAO.linkDb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Il DAO per mysql che si occupa delle operazioni sulla tabella paziente presente nel database.
*/
public class PazienteMySqlDAO extends AbstractDAO{
	
        private static ResourceBundle pazienteDao = ResourceBundle.getBundle("adisys/server/property/PazienteDAO");
        
        public static String NOME_TABELLA="";
	public static String NOME_COLONNA_ID = "";
	public static String NOME_COLONNA_NOME = "";
	public static String NOME_COLONNA_COGNOME = "";
	public static String NOME_COLONNA_DATA_NASCITA = "";
        
	//Modifica vers 2 -> Rimossa costante NOME_COLONNA_CELLULARE
	private static final String FORMATO_DATA_NASCITA = "yyyy-MM-dd";
        
        //AGGIUNTE VERS 2
	public static String NOME_TABELLA_CELLULARI = "";
	public static String NOME_COLONNA_PAZIENTE_CELLULARI = "";
	public static String NOME_COLONNA_NUMERO_CELLULARI = "";
        
        private static String filePathPazienti = "file/pazienti.txt";
        private static String filePathCellulari = "file/cellulari.txt";
        
        public static void setResourceBundle(String path, Locale locale){
            pazienteDao = ResourceBundle.getBundle(path, locale);
        }
                
	/**Costante protetta per i campi della query insert*/
	protected final static String[] FIELDS_INSERT = {
		NOME_COLONNA_NOME,
		NOME_COLONNA_COGNOME,
                NOME_COLONNA_DATA_NASCITA
	};
	//Se cambio il numero di FIELDS_INSERT, devo cambiare anche FIELDS_QUERY
	/**Costante protetta per i campi della query select, include i campi della costante
	 * {@link FIELDS_INSERT}*/
	protected final static String[] FIELDS_QUERY = {
		NOME_COLONNA_ID,
		FIELDS_INSERT[0],
		FIELDS_INSERT[1],
                FIELDS_INSERT[2],
                /*NOME_COLONNA_PAZIENTE_CELLULARI,
                NOME_COLONNA_NUMERO_CELLULARI*/
	};
	/**Costante per il nome della tabella degli pazienti*/
	protected final static String TABLE = leggiNomeTabella();
	
        private static String leggiNomeTabella(){
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathPazienti));
                return scanner.next();
                /*return nome_tab + " JOIN " + NOME_TABELLA_CELLULARI + " ON " +
                        nome_tab + "." + NOME_COLONNA_ID + " = " + NOME_TABELLA_CELLULARI + "." +
                                NOME_COLONNA_PAZIENTE_CELLULARI;*/
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
        
        private static void caricaVariables(String[] nomeCampi){
            NOME_TABELLA = nomeCampi[0];
            NOME_COLONNA_ID = nomeCampi[1];
            NOME_COLONNA_NOME = nomeCampi[2];
            NOME_COLONNA_COGNOME = nomeCampi[3];
            NOME_COLONNA_DATA_NASCITA = nomeCampi[4];
        }
        
        public void leggiFiles() {
            try {
                                    //JOptionPane.showMessageDialog(null, FileFactory.getFileContent(name));
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathPazienti));

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
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFilesCellulari();
        }
        
        private static void caricaVariablesCellulari(String[] nomeCampi){
            NOME_TABELLA_CELLULARI = nomeCampi[0];
            NOME_COLONNA_PAZIENTE_CELLULARI = nomeCampi[1];
            NOME_COLONNA_NUMERO_CELLULARI = nomeCampi[2];
        }
        
        public void leggiFilesCellulari() {
            try {
            
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePathCellulari));

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
                caricaVariablesCellulari(nomeCampi);
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            caricaFields();
        }
        
        private void caricaFields(){
            FIELDS_INSERT[0] = NOME_COLONNA_NOME + ", ";
            FIELDS_INSERT[1] = NOME_COLONNA_COGNOME + ", ";
            FIELDS_INSERT[2] = NOME_COLONNA_DATA_NASCITA;
            FIELDS_QUERY[0] = NOME_COLONNA_ID + ", ";
            FIELDS_QUERY[1] = FIELDS_INSERT[0];
            FIELDS_QUERY[2] = FIELDS_INSERT[1];
            FIELDS_QUERY[3] = FIELDS_INSERT[2];
           /* FIELDS_QUERY[4] = NOME_COLONNA_PAZIENTE_CELLULARI + ", ";
            FIELDS_QUERY[5] = NOME_COLONNA_NUMERO_CELLULARI;*/
        }

	//--------------------------------
	private int id;
	private String nome;
	private String cognome;
        private String dataNascita;
        private Object[] cellulari;
	//--------------------------------
	
	/**Grazie a questo oggetto ottiene le query già ben formate pronte per essere
	 * date in pasto al database*/
	private MySqlDAOConfig mysqlConfig = new MySqlDAOConfig(
			PazienteMySqlDAO.FIELDS_INSERT, PazienteMySqlDAO.FIELDS_QUERY, PazienteMySqlDAO.TABLE);



	/**
	 * Costruttore, nel caso in cui non esiste una connessione al db (oggetto {@link LinkingDb})
	 * ne crea una, altrimenti usa quella statica della classe AbstractDAO da cui questa eredita
	 * @throws MainException il file di configurazione non esiste, non può essere letto oppure 
	 * non può essere modificato
	*/
	public PazienteMySqlDAO() throws MainException{
		System.out.println("Costrutture di PazienteMySqlDAO");
		
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
	 * Crea un nuovo paziente, inserisce nel database il nuovo paziente con i dati prelevati dal TO
	 * 
	 * @param interfaceTo il transfer object con tutti i dati da inserire
	 * @return true se la creazione ha successo, false altrimenti
	*/
	@Override
	public boolean create(TO interfaceTo) {
		System.out.println("create()");
		PazienteTO to = (PazienteTO) interfaceTo;
		
		//---------------------------
		nome = to.getNome();
		cognome = to.getCognome();
                dataNascita = to.getDataNascita(FORMATO_DATA_NASCITA);
                cellulari = to.getCellulari();
		
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
			stmt.setString(1, nome);
			stmt.setString(2, cognome);
                        stmt.setString(3, dataNascita);
                        stmt.executeUpdate();
                        //Inserimento elemento
			//---------------------------
			
                        //Inserimento paziente effettuato con successo
			//Recupero ID assegnato -> ATTENZIONE AGLI OMONIMI!!!
			try{
				to.setID(recuperaID(to));

				for(Object cellulare:cellulari)
				{
					//Preparazione stringa
					String stringaUpdate = "INSERT INTO "+ NOME_TABELLA_CELLULARI + "("+ NOME_COLONNA_PAZIENTE_CELLULARI + ", " + NOME_COLONNA_NUMERO_CELLULARI +") " ;
					stringaUpdate += "VALUES(" + String.valueOf(to.getID()) + ",'" + String.valueOf(cellulare) + "');";
                                        PreparedStatement stmtCellulari = linkDb.prepareStatement(stringaUpdate);
                                        stmtCellulari.executeUpdate();
					//Inserimento dei numeri di cellulare (messaggio in caso di fallimento)
					
				}
			}
			catch (SQLException e)
			{
				//Inserimento dei numeri di cellulare fallito
				//JOptionPane.showMessageDialog(null, "ERRORE: Inserimento numeri di cellulare fallito");
                                e.printStackTrace();
				return false;
                        }
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
        
	/**
	 * Legge l'intera tabella degli pazienti, ogni paziente e' incapsulato nel transfer object.
	 * Il risultato equivale alla query SELECT * FROM paziente;
	 * 
	 * @return la struttura dati dei transfer object {@link PazienteTO} con i dati di ogni record
	 *  della tabella del db, null se c'e' qualche errore
	*/
	@Override
	public ArrayList<TO> read() {
		System.out.println("read()");
		ArrayList<TO> pazienti = new ArrayList<TO>();//arraylist dei to da riempire;
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getSelectSQL("true"));
			ArrayList<String[]> dates = linkDb.select(stmt);//arraylist sui dati del database
			Iterator<String[]> iterDates = dates.iterator();
			while(iterDates.hasNext()){
				String[] record = iterDates.next();
				
				
				PazienteTO infTO = new PazienteTO();//crea il nuovo TO con i dati prelevati
				
				id = Integer.parseInt(record[0]);
				nome = record[1];
				cognome = record[2];
                                dataNascita = record[3];
				
				//setta i valori del database --------------
				infTO.setID(id);
				infTO.setNome(nome);
				infTO.setCognome(cognome);
                                infTO.setDataNascita(dataNascita, FORMATO_DATA_NASCITA);
				//------------------------------------------
				
				//aggiunge il nuovo TO all'arraylist
				pazienti.add(infTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e){
			System.err.println("Ops... Il database e la DAO non sono compatibili");
			e.printStackTrace();
			return null;
		}
		return pazienti;
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
        
        public DefaultListModel<String> getCellulari(int idPaziente){
            //Caricamento paziente
            PazienteTO p = (PazienteTO) getSpecified(idPaziente);
            
            //Recupero array cellulari
            Object c[] = p.getCellulari();
            
            //Creazione modello lista
            DefaultListModel<String> modelloListaCellulari = new DefaultListModel<String>();
                    
            //Riempimento modello
            for(int cont=0; cont < c.length; cont++)
            {
                modelloListaCellulari.addElement(String.valueOf(c[cont]));
            }
            
            //Restituzione Modello
            return modelloListaCellulari;
        }

	/*
	 * Aggiorna il record di un paziente con i dati incapsulati nel transfer object {@link PazienteTO}
	 * quando la condizione <code>condition</code> &egrave <i>true</i>.<br>
	 * 
	 * @param interfaceTO l'interfaccia del transfer object dove sono incapsulati i dati da aggiornare
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return true se l'aggiornamento ha successo, false altrimenti
	 */
        @Override
	public boolean update(TO interfaceTO) {
		System.out.println("update()");
		PazienteTO to = (PazienteTO) interfaceTO;
		
		//------------------------------------------------
		id = to.getID();
                nome = to.getNome();
		cognome = to.getCognome();
                dataNascita = to.getDataNascita(FORMATO_DATA_NASCITA);
                cellulari = to.getCellulari();
		String[] fieldsValue = {
			NOME_COLONNA_NOME + " = '" + nome + "', ",
			NOME_COLONNA_COGNOME + " = '" + cognome + "', ",
                        NOME_COLONNA_DATA_NASCITA + " = '" + dataNascita + "'"
		};
		//------------------------------------------------
		
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getUpdateSQL(fieldsValue, 
                                NOME_COLONNA_ID + " = " + id));
                        //JOptionPane.showMessageDialog(null, query);
			stmt.executeUpdate();
			
                 //Aggiunta istruzioni x inserimento cellulari
		//Cancellazione numeri di cellulare esistenti
		//Praparazone query
		String queryCancellazioneCellulari= "DELETE FROM " + NOME_TABELLA_CELLULARI + " WHERE " + NOME_COLONNA_PAZIENTE_CELLULARI + "=" + String.valueOf(id +";");
		//Esecuzione
		linkDb.prepareStatement(queryCancellazioneCellulari).executeUpdate();
                
		//TODO Verificarenserimento dei numeri di cellulare
		Object[] cellulari= to.getCellulari();
		for(Object cellulare:cellulari)
		{
			//Preparazione stringa
			String stringaUpdate = "\nINSERT INTO "+ NOME_TABELLA_CELLULARI + "("+ NOME_COLONNA_PAZIENTE_CELLULARI + ", " + NOME_COLONNA_NUMERO_CELLULARI +") " ;
			stringaUpdate += "VALUES(" + String.valueOf(id) + ",'" + String.valueOf(cellulare) + "');";
                        PreparedStatement stmtCellulari = linkDb.prepareStatement(stringaUpdate);
                        stmtCellulari.executeUpdate();
                        
		}		
		
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * Cancella l'paziente con l'id in input. Se l'id in input &egrave -1, allora
	 * cancella tutti gli pazienti in tabella (coincide con un reset) 
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
	 * basta conoscere l'id del paziente da cancellare (senza sapere altro). Questo
	 * metodo generalizza il problema offrendo la possibilit&agrave di cancellare un paziente
	 * dando una stringa di condizione ben formata. La metodologia di cancellazione &egrave comunque
	 * rispettata dal metodo {@link #delete(int)}<br>
	 * 
	 * Cancella un paziente se si verifica la condizione condition
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
		
    public boolean alterTable(ArrayList<String> nomeCampi) {
        boolean change = false;   
        boolean ok = true;
        String nomeTabella = nomeCampi.get(0);
        String campo1 = nomeCampi.get(1);
        String campo2 = nomeCampi.get(2);
        String campo3 = nomeCampi.get(3);
        String campo4 = nomeCampi.get(4);
        View viste = null;
            try {
                viste = new View();
            } catch (MainException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        if(!campo1.equals(NOME_COLONNA_ID)
                || !campo2.equals(NOME_COLONNA_NOME) || !campo3.equals(NOME_COLONNA_COGNOME)) {
            change = true;
            viste.destroyView();
            if(!campo1.equals(NOME_COLONNA_ID)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_ID + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_NOME)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_NOME + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NOME_COLONNA_COGNOME)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_COGNOME + " RENAME TO " + campo3);
            }
            if(!campo4.equals(NOME_COLONNA_DATA_NASCITA)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_DATA_NASCITA + " RENAME TO " + campo4);
            }
        }
        if(!nomeTabella.equals(NOME_TABELLA)){
            change = true;
            viste.destroyView();
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA
                        + " RENAME TO " + nomeTabella);
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePathPazienti), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2 + "\n"
                        + campo3 + "\n"
                        + campo4);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFiles();
         
        }
        if(change != false)
            viste.createView();
        return ok;
    }//fine metodo
     
             
     public boolean alterTableCellulari(ArrayList<String> nomeCampi) {
        boolean change = false;   
        boolean ok = true;
        String nomeTabella = nomeCampi.get(0);
        String campo1 = nomeCampi.get(1);
        String campo2 = nomeCampi.get(2);
        if(!campo1.equals(NOME_COLONNA_PAZIENTE_CELLULARI)
                || !campo2.equals(NOME_COLONNA_NUMERO_CELLULARI)) {
            change = true;
            if(!campo1.equals(NOME_COLONNA_PAZIENTE_CELLULARI)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA_CELLULARI 
                        + " ALTER COLUMN " + NOME_COLONNA_PAZIENTE_CELLULARI + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_NUMERO_CELLULARI)){
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA_CELLULARI 
                        + " ALTER COLUMN " + NOME_COLONNA_NUMERO_CELLULARI + " RENAME TO " + campo2);
            }
        }
        if(!nomeTabella.equals(NOME_TABELLA_CELLULARI)){
            change = true;
                ok = linkDb.execute("ALTER TABLE " + NOME_TABELLA_CELLULARI
                        + " RENAME TO " + nomeTabella);
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePathCellulari), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PazienteMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFilesCellulari();
        }
      return ok;
    }//fine metodo */
    
     private Integer recuperaID(PazienteTO p)
	{
		//Recupero informazioni dal  database
		String queryText = "SELECT "+ NOME_COLONNA_ID +  " FROM " + NOME_TABELLA;
		queryText+= " WHERE "; 
		queryText+= NOME_COLONNA_NOME +" = '" + p.getNome() +"' ";
		queryText+="and " +NOME_COLONNA_COGNOME +" = '" + p.getCognome() +"' ";
		queryText+= "and " + NOME_COLONNA_DATA_NASCITA +" = '" + p.getDataNascita(FORMATO_DATA_NASCITA) +"' ";
		queryText+=";";
		ResultSet tabellaPaziente = linkDb.getResultSet(queryText.toUpperCase());

		//Copia delle informazioni in un oggetto di tipo Paziente
		try {
			//Posizionamento del cursore sull'unica riga
			tabellaPaziente.first();
			//Restituzione oggetto
			return tabellaPaziente.getInt(NOME_COLONNA_ID);

		} catch (SQLException e) {
			e.printStackTrace();
			String msgErrore ="Pazienti -> Errore metodo recuperaID()";
			System.out.println(msgErrore);
			return null;
		}

	}

    public String getSpecifiedWithPat(int id) {
        //Recupero informazioni dal  database
		String queryText = "SELECT * FROM " + NOME_TABELLA +" WHERE " + NOME_COLONNA_ID +" = " + id +";";
		ResultSet tabellaPaziente = linkDb.getResultSet(queryText);

		//Copia delle informazioni in un oggetto di tipo Paziente
		try {
			//Posizionamento del cursore sull'unica riga
			tabellaPaziente.first();

			//Creazione oggetto
			PazienteTO p = new PazienteTO();

			//Popolamento
			p.setID(id);
			p.setNome(tabellaPaziente.getString(NOME_COLONNA_NOME));
			p.setCognome(tabellaPaziente.getString(NOME_COLONNA_COGNOME));
			p.setDataNascita(tabellaPaziente.getString(NOME_COLONNA_DATA_NASCITA),FORMATO_DATA_NASCITA );

			//Aggiunta dei numeri di cellulare
			//TODO verificare cosa succede in caso di nessun numero di cellulare..
			String queryCellulari = "SELECT * FROM " + NOME_TABELLA_CELLULARI + " WHERE " + NOME_COLONNA_PAZIENTE_CELLULARI + "=" + String.valueOf(id);
			ResultSet rs = linkDb.getResultSet(queryCellulari);
			if(rs.first())
				for(;!(rs.isAfterLast()); rs.next()) p.addCellulare(rs.getString(NOME_COLONNA_NUMERO_CELLULARI));


                        String queryPat = "SELECT DISTINCT " 
                                + PatologiaMySqlDAO.NOME_TABELLA + "." + PatologiaMySqlDAO.NOME_COLONNA_CODICE + ","
                                + PatologiaMySqlDAO.NOME_TABELLA + "." + PatologiaMySqlDAO.NOME_COLONNA_NOME
                                + " FROM " 
                                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + " JOIN " 
                                + InterventoMySqlDAO.INTERVENTIONS_TYPES_TABLE_NAME + " ON " 
                                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + NOME_COLONNA_ID 
                                + " = " +  InterventoMySqlDAO.INTERVENTIONS_TYPES_TABLE_NAME + "."
                                + InterventoMySqlDAO.COLUMN_IDINT_TYPE_NAME + " JOIN " 
                                + InterventoMySqlDAO.PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME 
                                + " ON " + InterventoMySqlDAO.INTERVENTIONS_TYPES_TABLE_NAME + "."
                                + InterventoMySqlDAO.COLUMN_INTERVENTION_TYPE_ID_NAME + " = " 
                                + InterventoMySqlDAO.PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME 
                                + "." + InterventoMySqlDAO.COLUMN_ID_INTERVENTION_TYPE_PATHOLOGIES_INTERVENTIONS_TYPES_NAME 
                                + " JOIN " + PatologiaMySqlDAO.NOME_TABELLA + " ON " 
                                + InterventoMySqlDAO.PATHOLOGIES_INTERVENTIONS_TYPES_TABLE_NAME 
                                + "." + InterventoMySqlDAO.COLUMN_PATHOLOGY_ID_PATHOLOGIES_INTERVENTIONS_TYPES_NAME + " = " + 
                                PatologiaMySqlDAO.NOME_TABELLA + "." + PatologiaMySqlDAO.NOME_COLONNA_ID + " WHERE " +
                                InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.PATIENT_COLUMN_ID_NAME + " = " + id;
			//Restituzione oggetto
                        
                        ResultSet risultato = linkDb.getResultSet(queryPat);
                        ArrayList<PatologiaTO> listaPatologie = new ArrayList<PatologiaTO>();
                        if(risultato.first()){
                        for(;!(risultato.isAfterLast()); risultato.next()){
                            listaPatologie.add(new PatologiaTO(
                                    risultato.getString(PatologiaMySqlDAO.NOME_COLONNA_CODICE),
                                    risultato.getString(PatologiaMySqlDAO.NOME_COLONNA_NOME)));
                        }
               }
                        String paziente = p.toString();
                        paziente = paziente + pazienteDao.getString("PATOLOGIE");
                        int i = 0;
                        for(PatologiaTO e : listaPatologie){
                            i++;
                            paziente = paziente + "\n" + "- " + e.getCodice() + " " + e.getNome();
                        }
                        if(i == 0)
                            paziente = paziente + pazienteDao.getString("PAZIENTE NON ASSOCIATO");
                        return paziente;
		} catch (SQLException e) {
			e.printStackTrace();
			String msgErrore ="Pazienti -> Errore metodo getPaziente()";
			System.out.println(msgErrore);
			return null;
		}

    }

    @Override
    public TO getSpecified(int id) {
         //Recupero informazioni dal  database
		String queryText = "SELECT * FROM " + NOME_TABELLA +" WHERE " + NOME_COLONNA_ID +" = " + id +";";
		ResultSet tabellaPaziente = linkDb.getResultSet(queryText);

		//Copia delle informazioni in un oggetto di tipo Paziente
		try {
			//Posizionamento del cursore sull'unica riga
			tabellaPaziente.first();

			//Creazione oggetto
			PazienteTO p = new PazienteTO();

			//Popolamento
			p.setID(id);
			p.setNome(tabellaPaziente.getString(NOME_COLONNA_NOME));
			p.setCognome(tabellaPaziente.getString(NOME_COLONNA_COGNOME));
			p.setDataNascita(tabellaPaziente.getString(NOME_COLONNA_DATA_NASCITA),FORMATO_DATA_NASCITA );

			//Aggiunta dei numeri di cellulare
			//TODO verificare cosa succede in caso di nessun numero di cellulare..
			String queryCellulari = "SELECT * FROM " + NOME_TABELLA_CELLULARI + " WHERE " + NOME_COLONNA_PAZIENTE_CELLULARI + "=" + String.valueOf(id);
			ResultSet rs = linkDb.getResultSet(queryCellulari);
			if(rs.first())
				for(;!(rs.isAfterLast()); rs.next()) p.addCellulare(rs.getString(NOME_COLONNA_NUMERO_CELLULARI));
                        
                        return p;
		} catch (SQLException e) {
			e.printStackTrace();
			String msgErrore ="Pazienti -> Errore metodo getPaziente()";
			System.out.println(msgErrore);
			return null;
		}
    }

    public boolean idEsistente(String nome, String cognome, Object[] cellulari) {
        	//Preparazione query
		String stat= "SELECT * FROM " + NOME_TABELLA + " WHERE " 
                            + NOME_COLONNA_NOME + " = '" + nome + "' AND " + NOME_COLONNA_COGNOME + " = '"
                            + cognome + "'";

		//Esecuzione query
		ResultSet tabConto = linkDb.getResultSet(stat);

		try {
			//Estrazione valore
			if (tabConto.first()){
                            for(int i = 0; i < cellulari.length; i++){
                                stat = "SELECT * FROM " + NOME_TABELLA_CELLULARI + " WHERE "
                                        + NOME_COLONNA_NUMERO_CELLULARI + " = '" + cellulari[i] + "'";
                                tabConto = linkDb.getResultSet(stat);
                                if(!tabConto.first())
                                    return false;
                            }
                            return true;
                        } 
                            
			else return false;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE RICERCA ID!!!");

			//TRACE
			System.out.println("Infermieri -> ERRORE NELLA RICERCA DELL'ID NELLA TABELLA " + NOME_TABELLA);

			return false;
		}
    }

}
