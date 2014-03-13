package business;

import adisys.server.strumenti.ReadXML;


/**
 * It reads from the XML file the matchings functionality -> associated service class.
 * It uses the {@link ReadXML} class for reading from XML file.
 * It allows crossing between request and handler.
*/
public class ReadCrossing {
	
	/**Name and path of file XML*/
	private static final String XML_CROSSING_PATH = "system_files/functions_to_managers.xml";
	
	/**Name of the items of the XML file that identify a functionality*/
	private static final String NAME_ELEMENT_FUNCTIONS = "action";
	
	/**
	 * Class instance that reads XML file*/
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
	 * Returns the proper request handler according to the request function passed as input.
	 * @param requestFunction a string that identifies the name of the requested functionality
	 * @return the name of the request handler associated to the request passed as input, null if there is no correspondence
	*/
	public String getNameHandler(String requestFunction){
		System.out.println("ReadCrossing.getNameHandler("+requestFunction+")");
		
		//preleva il nome della classe associata a requestFunction
		String controllerName = fileXML.getFirstChildValue(NAME_ELEMENT_FUNCTIONS, requestFunction);
		
		return controllerName;
	}
	
}
