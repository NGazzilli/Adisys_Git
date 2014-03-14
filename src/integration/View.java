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
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
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
         linkDb.execute("CREATE VIEW CONTOINTERVENTI (ID,NUM_INTERV) "
                + "AS (SELECT " + InfermiereMySqlDAO.TABLE_NAME + "." + InfermiereMySqlDAO.COLUMN_ID_NAME
                + ", COUNT(" + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "."
                + InterventoMySqlDAO.NURSE_COLUMN_ID_NAME + ") AS NUM_INTERV "
                + "FROM " + InfermiereMySqlDAO.TABLE_NAME 
                + " LEFT JOIN " + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + " ON "
                + InfermiereMySqlDAO.TABLE_NAME + "." + InfermiereMySqlDAO.COLUMN_ID_NAME 
                + " = " + InterventoMySqlDAO.INTERVENTION_TABLE_NAME 
                + "." + InterventoMySqlDAO.NURSE_COLUMN_ID_NAME + " WHERE "
                + InterventoMySqlDAO.NOME_COLONNA_DATA + " >= CURDATE() AND "  
                + InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO + " >= CURTIME() GROUP BY "
                + InfermiereMySqlDAO.TABLE_NAME + "." + InfermiereMySqlDAO.COLUMN_ID_NAME + " ORDER BY "
                + InfermiereMySqlDAO.TABLE_NAME + "." + InfermiereMySqlDAO.COLUMN_ID_NAME + ")"); 
                
        
         linkDb.execute("CREATE VIEW INTERV_INFERM (ID,NOME,COGNOME,NUMINTERVENTI) "
                + "AS (SELECT " + InfermiereMySqlDAO.COLUMN_ID_NAME + ","
                + InfermiereMySqlDAO.COLUMN_NAME_NAME + ","
                + InfermiereMySqlDAO.COLUMN_SURNAME_NAME + ","
                + " NUM_INTERV AS NUMINTERVENTI FROM "
                + InfermiereMySqlDAO.TABLE_NAME + " LEFT JOIN CONTOINTERVENTI "
                + "ON " + InfermiereMySqlDAO.TABLE_NAME + "." 
                + InfermiereMySqlDAO.COLUMN_ID_NAME + " = " + " CONTOINTERVENTI.ID);");
                
         linkDb.execute("CREATE VIEW VISUALIZZAZIONEINTERVENTI "
                + "(ID,INFERMIERE,NOME,COGNOME,DATAINT,ORAINIZIO,ORAFINE,CITTA,CIVICO) AS "
                + "(SELECT " + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.COLUMN_ID_NAME + ","
                + InfermiereMySqlDAO.TABLE_NAME + "." 
                + InfermiereMySqlDAO.COLUMN_SURNAME_NAME + " AS "
                + "INFERMIERE, " + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_NOME + ", "
                + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_COGNOME + ", "
                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.NOME_COLONNA_DATA
                + ", " +  InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO
                + ", " + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.NOME_COLONNA_ORA_FINE + ", "
                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.COLUMN_CITY_NAME + ", "
                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.NOME_COLONNA_CIVICO + " FROM "
                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + ", " 
                + InfermiereMySqlDAO.TABLE_NAME + ", " + PazienteMySqlDAO.NOME_TABELLA + " WHERE "
                + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "."
                + InterventoMySqlDAO.NURSE_COLUMN_ID_NAME + " = "
                + InfermiereMySqlDAO.TABLE_NAME + "."
                + InfermiereMySqlDAO.COLUMN_ID_NAME 
                + " AND " + InterventoMySqlDAO.INTERVENTION_TABLE_NAME + "." + InterventoMySqlDAO.PATIENT_COLUMN_ID_NAME
                + " = " + PazienteMySqlDAO.NOME_TABELLA + "." + PazienteMySqlDAO.NOME_COLONNA_ID + ");");
    }
    
    public void destroyView(){
        linkDb.execute("DROP VIEW IF EXISTS INTERV_INFERM");
        linkDb.execute("DROP VIEW IF EXISTS CONTOINTERVENTI");
        linkDb.execute("DROP VIEW IF EXISTS VISUALIZZAZIONEINTERVENTI");
    }
}
