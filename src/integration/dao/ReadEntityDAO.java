package integration.dao;

import adisys.server.strumenti.ReadXML;

/**
 * Classe che astrae sul file xml di sistema dove sono presenti le corrispondenze tra 
 * classi entity (solo nome) e classi dao (nome comprendente i package)
*/
public class ReadEntityDAO {
	private static final String xmlEntityDAOPath = "system_files/entity_dao.xml";
	private static final String nameElementFunctions = "entity";
	
	
	/**
	 * Costruttore, inizializza la classe di utility {@link ReadXML}, fondamentale per la lettura
	 * del file xml
	*/
	public ReadEntityDAO(){
		try{
			fileXML = new ReadXML(xmlEntityDAOPath);
		} catch(Exception e){
			System.err.println("Errore nella lettura del file xml assicurarsi che esista e possa essere letto");
			e.printStackTrace();
		}
	}
	
	

	
	
	/**
	 * Restituisce la dao appropriata, a seconda del nome dell'entity passata per parametro
	 * @param entityName il nome dell'entity chiamante
	 * @return il nome della DAO associata alla entity chiamante, null se non
	 * trova nessuna corrispondenza
	*/
	public String getDAOName(String entityName){
		System.out.println("ReadEntityDAO.getNameDAO("+entityName+")");
		
		//preleva il nome della classe associata a requestFunction
		String daoName = fileXML.getFirstChildValue(nameElementFunctions, entityName);
		
		return daoName;
	}
	
	
	
	
	private ReadXML fileXML;
	
}
