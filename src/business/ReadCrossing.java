package business;

import adisys.server.strumenti.ReadXML;


/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Legge dal file xml le associazioni funzionalità -> classe di servizio associata. Utilizza
 * la classe {@link ReadXML} per la lettura dal file XML<br>
 * Permette quindi il crossing (da qui il nome) tra richiesta e handler.
*/
public class ReadCrossing {
	
	/**Nome e path del file XML*/
	private static final String XML_CROSSING_PATH = "system_files/functions_to_managers.xml";
	
	/**Nome degli elementi del file XML che identificano una funzionalit&agrave*/
	private static final String NAME_ELEMENT_FUNCTIONS = "action";
	
	/**Istanza di classe che si occupa della lettura sul file XML*/
	private ReadXML fileXML;
	
	
	public ReadCrossing(){
		try{
			fileXML = new ReadXML(XML_CROSSING_PATH);
		} catch(Exception e){
			System.err.println("Errore nella lettura del file xml assicurarsi che esista e possa essere letto");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * Restituisce il request handler appropriato secondo la request function in input
	 * @param requestFunction un stringa che identifica il nome della funzionalita' richiesta
	 * @return il nome della request handler associata alla request in input, null se non
	 * trova nessuna corrispondenza
	*/
	public String getNameHandler(String requestFunction){
		System.out.println("ReadCrossing.getNameHandler("+requestFunction+")");
		
		//preleva il nome della classe associata a requestFunction
		String controllerName = fileXML.getFirstChildValue(NAME_ELEMENT_FUNCTIONS, requestFunction);
		
		return controllerName;
	}
	
}
