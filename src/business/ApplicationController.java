package business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import adisys.server.strumenti.Record;
import messaggistica.GMessage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Application Controller del sistema.
 * L'utente richiede un servizio inviando una stringa che identifica il servizio di business voluto,
 * la richiesta viene filtrata dal FrontController che la passa all'AC il quale trova la corrispondente
 *  classe che si occupa di quel servizio cercandola con l'ausilio di {@link ReadCrossing} in un file XML.
 * Una volta trovata evoca il metodo che si occupa della funzionalità richiesta usando 
 * {@link ServiceHandler}
 * 
 * @author Luca Massa
*/
public class ApplicationController implements Int_ApplicationController{
	
    /**
     * Astrazione sul file XML dove sono contenute le corrispondenze richiesta->classe di servizio
     */
    private static ReadCrossing fileXML;
    private static ResourceBundle applicationController = ResourceBundle.getBundle("adisys/server/property/ApplicationController");
	
    public static void setResourceBundle(String path, Locale locale){
        applicationController = ResourceBundle.getBundle(path, locale);
    }
	/**
	 * ServiceHandler che si occupa di capire il metodo da eseguire
	 * avendo in input il nome del metodo e i parametri
	*/
	private ServiceHandler ASAbstract;
	
	/**
	 * Costruttore, inizializza la {@link ReadCrossing} ed esegue il metodo 
	 * {@link #getInstance(String request)}
	*/
	public ApplicationController(String requestMethod){
		fileXML = new ReadCrossing();
		ASAbstract = (ServiceHandler) getInstance(requestMethod);
	}
	
	
	/**
	 * Ottiene l'istanza della classe di business associata alla richiesta {@code request}
	 * così inizializza {@link #ASAbstract}
	 * @param request la stringa che identifica 
	 * @throws ClassNotFoundException se non esiste una classe con quel nome
	*/
	private Object getInstance(String request){
		System.out.println("ApplicationController : getInstance("+request+")");
		
		Class<?> classBusiness = null;
		Object objBusiness = null;
		
		String rHandlerName = "<null>";
		
		try{
			//preleva il nome della classe che fornisce il servizio richiesto
			rHandlerName = getASName(request);
                        
			//ottiene il Class del nome della classe trovata
			classBusiness =  Class.forName(rHandlerName);
                        //ottiene l'istanza del Class ottenuto prima (evoca il costruttore)
			objBusiness = classBusiness.newInstance();
			
		} catch(ClassNotFoundException e){
			System.err.println(applicationController.getString("OPS.. APPLICATIONCONTROLLER:") +
					applicationController.getString("IL REQUESTMANAGER (FRONT CONTROLLER) RICEVERA' UN ERRORE,") +
				applicationController.getString("LA FUNZIONALITA' RICHIESTA '")+request+applicationController.getString("' PUO' NON ESSERE DISPONIBILE,") +
				applicationController.getString("SI E' TENTATO DI CHIAMARE IL REQUEST HANDLER ")+rHandlerName+applicationController.getString(" CHE RISULTA INESISTENTE") +
				applicationController.getString("CONTROLLARE LE CORRISPONDENZE DEL FILE XML OPPURE LA REQUEST INVIATA DALLA BOUNDARY"));
                        GMessage.message_error(applicationController.getString("ERRORE FUNZIONALITA"));
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objBusiness;
	}
	
	
	
	
	
	/**
	 * Attraverso la richiesta {@code key} proveniente dal Front Controller
	 * capisce il metodo da invocare (appartenente alle classi che la estendono)
	 * che è in grado di soddisfare la richiesta.<br>
	 * Ovviamente il metodo richiesto può avere dei parametri in input,
	 * questi sono specificati dall'ArrayList di {@link Record} con chiave = tipo di parametro;
	 *  valore = valori dei parametri di quel tipo
	 * 
	 * @see ServiceHandler#handleRequest(String, ArrayList)
	 * 
	 * @param key la richiesta di funzionalit&agrave proveniente dal {@link presentation.RequestManager}
	 * @param params &egrave un'ArrayList di {@link Record} che hanno una sola coppia <chiave, valore>
	 * @throws NoSuchMethodException se il metodo non viene trovato
	 * @throws SecurityException pu&ograve essere causato dal metodo getDeclaredMethod della classe Class
	 * @throws InvocationTargetException se il metodo trovato lancia un eccezione
	 * @throws IllegalAccessException se la classe o il suo costruttore non sono accessibili
	 * @throws IllegalArgumentException se il metodo trovato non ottiene i giusti parametri in input
	 * @throws InstantiationException se la classe non ha un costruttore, quindi non può essere 
	 * istanziata
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 * @return qualsiasi valore ritornato dal metodo invocato
	*/
	public Object handleRequest(String keyMethod, ArrayList<Record<String, Object>> params)
	throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		return ASAbstract.handleRequest(keyMethod, params);
	}
	
	
	
	
	
	
	
	/**
	 * Restituisce il nome della classe che fornisce il servizio voluto,
	 * usa il metodo {@link ReadCrossing#getNameHandler(String)}
	 * @throws ClassNotFoundException se non &egrave associata nessuna classe alla richiesta
	*/
	private String getASName(String request) throws ClassNotFoundException{
		
		String handlerName = fileXML.getNameHandler(request);
		if(handlerName==null){
			throw new ClassNotFoundException();
		}
		else{
			return handlerName;
		}
	}
	
	
	
}
