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
import business.patologia.PatologiaTO;
import static integration.dao.AbstractDAO.linkDb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Il DAO per mysql che si occupa delle operazioni sulla tabella patologia presente nel database.
*/
public class PatologiaMySqlDAO extends AbstractDAO{
	
        public static String NOME_TABELLA = "";
	public static String NOME_COLONNA_ID = "";
	public static String NOME_COLONNA_CODICE = "";
	public static String NOME_COLONNA_NOME = "";
        private static String filePath = "file/patologie.txt";
        
	/**Costante protetta per i campi della query insert*/
	protected final static String[] FIELDS_INSERT = {
		NOME_COLONNA_CODICE,
                NOME_COLONNA_NOME
	};
	//Se cambio il numero di FIELDS_INSERT, devo cambiare anche FIELDS_QUERY
	/**Costante protetta per i campi della query select, include i campi della costante
	 * {@link FIELDS_INSERT}*/
	protected final static String[] FIELDS_QUERY = {
		NOME_COLONNA_ID,
		FIELDS_INSERT[0],
		FIELDS_INSERT[1]
	};
	/**Costante per il nome della tabella degli patologie*/
	protected final static String TABLE = leggiNomeTabella();
	
        private static String leggiNomeTabella(){
            try {
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));
                return scanner.next();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
	//--------------------------------
	private int id;
	private String codice;
	private String nome;
	//--------------------------------
	
	/**Grazie a questo oggetto ottiene le query già ben formate pronte per essere
	 * date in pasto al database*/
	private MySqlDAOConfig mysqlConfig = new MySqlDAOConfig(
			FIELDS_INSERT, FIELDS_QUERY, TABLE);


        private void caricaVariables(String[] nomeCampi){
            NOME_TABELLA = nomeCampi[0];
            NOME_COLONNA_ID = nomeCampi[1];
            NOME_COLONNA_CODICE = nomeCampi[2];
            NOME_COLONNA_NOME = nomeCampi[3];
            
            FIELDS_INSERT[0] = NOME_COLONNA_CODICE + ", ";
            FIELDS_INSERT[1] = NOME_COLONNA_NOME;
            FIELDS_QUERY[0] = NOME_COLONNA_ID + ", ";
            FIELDS_QUERY[1] = FIELDS_INSERT[0];
            FIELDS_QUERY[2] = FIELDS_INSERT[1];
        }
        
        public void leggiFiles() {
            try {
                
                Scanner scanner = new Scanner(FileFactory.getFileContent(filePath));
                //scanner.useDelimiter(",");
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
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        


	/**
	 * Costruttore, nel caso in cui non esiste una connessione al db (oggetto {@link LinkingDb})
	 * ne crea una, altrimenti usa quella statica della classe AbstractDAO da cui questa eredita
	 * @throws MainException il file di configurazione non esiste, non può essere letto oppure 
	 * non può essere modificato
	*/
	public PatologiaMySqlDAO() throws MainException{
		System.out.println("Costrutture di PatologiaMySqlDAO");
		
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
	 * Crea un nuovo patologia, inserisce nel database il nuovo patologia con i dati prelevati dal TO
	 * 
	 * @param interfaceTo il transfer object con tutti i dati da inserire
	 * @return true se la creazione ha successo, false altrimenti
	*/
	@Override
	public boolean create(TO interfaceTo) {
		System.out.println("create()");
		PatologiaTO to = (PatologiaTO) interfaceTo;
		
		//---------------------------
		codice = to.getCodice();
                nome = to.getNome();
		
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
			stmt.setString(1, codice);
			stmt.setString(2, nome);
			//---------------------------
			
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
        
	/**
	 * Legge l'intera tabella degli patologie, ogni patologia e' incapsulato nel transfer object.
	 * Il risultato equivale alla query SELECT * FROM patologia;
	 * 
	 * @return la struttura dati dei transfer object {@link PatologiaTO} con i dati di ogni record
	 *  della tabella del db, null se c'e' qualche errore
	*/
	public ArrayList<TO> read() {
		System.out.println("read()");
		ArrayList<TO> patologie = new ArrayList<TO>();//arraylist dei to da riempire;
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getSelectSQL("true"));
			ArrayList<String[]> dates = linkDb.select(stmt);//arraylist sui dati del database
			Iterator<String[]> iterDates = dates.iterator();
			while(iterDates.hasNext()){
				String[] record = iterDates.next();
				
				
				PatologiaTO patTO = new PatologiaTO();//crea il nuovo TO con i dati prelevati
				
				id = Integer.parseInt(record[0]);
				codice = record[1];
				nome = record[2];
				
				//setta i valori del database --------------
				patTO.setCodice(codice);
				patTO.setNome(nome);
				//------------------------------------------
				
				//aggiunge il nuovo TO all'arraylist
				patologie.add(patTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e){
			System.err.println("Ops... Il database e la DAO non sono compatibili");
			e.printStackTrace();
			return null;
		}
		return patologie;
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
	 * Aggiorna il record di un patologia con i dati incapsulati nel transfer object {@link PatologiaTO}
	 * quando la condizione <code>condition</code> &egrave <i>true</i>.<br>
	 * 
	 * @param interfaceTO l'interfaccia del transfer object dove sono incapsulati i dati da aggiornare
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return true se l'aggiornamento ha successo, false altrimenti
	 */
        @Override
	public boolean update(TO interfaceTO) {
		System.out.println("update()");
		PatologiaTO to = (PatologiaTO) interfaceTO;
		
		//------------------------------------------------
		codice = to.getCodice();
		nome = to.getNome();
		String[] fieldsValue = {
			NOME_COLONNA_CODICE + " = '" +  codice + "', ",
			NOME_COLONNA_NOME + " = '" + nome + "'"
		};
		//------------------------------------------------
		
		try {
			PreparedStatement stmt = linkDb.prepareStatement(mysqlConfig.getUpdateSQL(fieldsValue, 
                                NOME_COLONNA_ID + " = " + to.getID()));
			stmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * Cancella l'patologia con l'id in input. Se l'id in input &egrave -1, allora
	 * cancella tutti gli patologie in tabella (coincide con un reset) 
	 * Usa {@link #delete(String)}
	 * @return true se la cancellazione ha successo, false altrimenti
	*/
	@Override
	public boolean delete(int id){
		if(id==-1){
                    
                    ADISysTableModel tabella = getTabella();
			delete(NOME_COLONNA_ID + " NOT IN "
                        + " (SELECT " + InterventoMySqlDAO.NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI
                        + " FROM " + InterventoMySqlDAO.NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI + " )");
                    if(tabella.getRowCount() != getTabella().getRowCount())
                        return true;
                    else
                        return false;                        
		}
		else{
			return delete(NOME_COLONNA_ID + " = " + id);
		}
	}
	



	/**
	 * <u>GENERALIZZAZIONE DEL PROBLEMA</u>.<br>
	 * Il metodo di cancellazione intervento di ADISys si basa sull'id,
	 * basta conoscere l'id del patologia da cancellare (senza sapere altro). Questo
	 * metodo generalizza il problema offrendo la possibilit&agrave di cancellare un patologia
	 * dando una stringa di condizione ben formata. La metodologia di cancellazione &egrave comunque
	 * rispettata dal metodo {@link #delete(int)}<br>
	 * 
	 * Cancella un patologia se si verifica la condizione condition
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
     if(!campo1.equals(NOME_COLONNA_ID)
                || !campo2.equals(NOME_COLONNA_CODICE) || !campo3.equals(NOME_COLONNA_NOME)) {
            change = true;
            if(!campo1.equals(NOME_COLONNA_ID)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_ID + " RENAME TO " + campo1);
            }
            if(!campo2.equals(NOME_COLONNA_CODICE)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_CODICE + " RENAME TO " + campo2);
            }
            if(!campo3.equals(NOME_COLONNA_NOME)){
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA 
                        + " ALTER COLUMN " + NOME_COLONNA_NOME + " RENAME TO " + campo3);
            }
        }
        if(!nomeTabella.equals(NOME_TABELLA)){
            change = true;
                ok = linkDb.esegui("ALTER TABLE " + NOME_TABELLA
                        + " RENAME TO " + nomeTabella + ";");
        }
        if(change != false && ok == true){
            try {
                FileFactory.setFileContent(new File(filePath), 
                        nomeTabella + "\n"
                        + campo1 + "\n"
                        + campo2 + "\n"
                        + campo3);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PatologiaMySqlDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            leggiFiles();
        }
      return ok;
    }//fine metodo

    @Override
    public TO getSpecified(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ResultSet getTabellaPatologieAssociate(int idPatologia) {
        return linkDb.getResultSet("SELECT * FROM " + InterventoMySqlDAO.NOME_TABELLA_PATOLOGIE_TIPI_INTERVENTI
                        + " WHERE " + InterventoMySqlDAO.NOME_COLONNA_ID_PATOLOGIA_PATOLOGIE_TIPI_INTERVENTI + " = "
                        + idPatologia);
    }

}
