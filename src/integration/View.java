/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import messaggistica.MainException;
import integration.dao.InfermiereMySqlDAO;
import integration.dao.InterventoMySqlDAO;
import integration.dao.PazienteMySqlDAO;

/**
 *
 * @author Luca
 */
public class View {
    
    protected static I_LinkingDb linkDb;
    
    public View() throws MainException{
        System.out.println("Costrutture di View");
		
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
    }
    public void createView(){
         linkDb.esegui("CREATE VIEW CONTOINTERVENTI (ID,NUM_INTERV) "
                + "AS (SELECT " + InfermiereMySqlDAO.NOME_TABELLA + "." + InfermiereMySqlDAO.NOME_COLONNA_ID
                + ", COUNT(" + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "."
                + InterventoMySqlDAO.NOME_COLONNA_ID_INFERMIERE + ") AS NUM_INTERV "
                + "FROM " + InfermiereMySqlDAO.NOME_TABELLA 
                + " LEFT JOIN " + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + " ON "
                + InfermiereMySqlDAO.NOME_TABELLA + "." + InfermiereMySqlDAO.NOME_COLONNA_ID 
                + " = " + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI 
                + "." + InterventoMySqlDAO.NOME_COLONNA_ID_INFERMIERE + " WHERE "
                + InterventoMySqlDAO.NOME_COLONNA_DATA + " >= CURDATE() AND "  
                + InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO + " >= CURTIME() GROUP BY "
                + InfermiereMySqlDAO.NOME_TABELLA + "." + InfermiereMySqlDAO.NOME_COLONNA_ID + " ORDER BY "
                + InfermiereMySqlDAO.NOME_TABELLA + "." + InfermiereMySqlDAO.NOME_COLONNA_ID + ")"); 
                
        
         linkDb.esegui("CREATE VIEW INTERV_INFERM (ID,NOME,COGNOME,NUMINTERVENTI) "
                + "AS (SELECT " + InfermiereMySqlDAO.NOME_COLONNA_ID + ","
                + InfermiereMySqlDAO.NOME_COLONNA_NOME + ","
                + InfermiereMySqlDAO.NOME_COLONNA_COGNOME + ","
                + " NUM_INTERV AS NUMINTERVENTI FROM "
                + InfermiereMySqlDAO.NOME_TABELLA + " LEFT JOIN CONTOINTERVENTI "
                + "ON " + InfermiereMySqlDAO.NOME_TABELLA + "." 
                + InfermiereMySqlDAO.NOME_COLONNA_ID + " = " + " CONTOINTERVENTI.ID);");
                
         linkDb.esegui("CREATE VIEW VISUALIZZAZIONEINTERVENTI "
                + "(ID,INFERMIERE,NOME,COGNOME,DATAINT,ORAINIZIO,ORAFINE,CITTA,CIVICO) AS "
                + "(SELECT " + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_ID + ","
                + InfermiereMySqlDAO.NOME_TABELLA + "." 
                + InfermiereMySqlDAO.NOME_COLONNA_COGNOME + " AS "
                + "INFERMIERE, " + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_NOME + ", "
                + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_COGNOME + ", "
                + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_DATA
                + ", " +  InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO
                + ", " + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_ORA_FINE + ", "
                + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_CITTA + ", "
                + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_CIVICO + " FROM "
                + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + ", " 
                + InfermiereMySqlDAO.NOME_TABELLA + ", " + PazienteMySqlDAO.NOME_TABELLA + " WHERE "
                + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "."
                + InterventoMySqlDAO.NOME_COLONNA_ID_INFERMIERE + " = "
                + InfermiereMySqlDAO.NOME_TABELLA + "."
                + InfermiereMySqlDAO.NOME_COLONNA_ID 
                + " AND " + InterventoMySqlDAO.NOME_TABELLA_INTERVENTI + "." + InterventoMySqlDAO.NOME_COLONNA_ID_PAZIENTE
                + " = " + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_ID + ");");
    }
    
    public void destroyView(){
        linkDb.esegui("DROP VIEW IF EXISTS INTERV_INFERM");
        linkDb.esegui("DROP VIEW IF EXISTS CONTOINTERVENTI");
        linkDb.esegui("DROP VIEW IF EXISTS VISUALIZZAZIONEINTERVENTI");
    }
}
