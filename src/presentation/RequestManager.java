package presentation;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import messaggistica.MainException;

import business.ApplicationController;
import adisys.server.strumenti.Record;
import adisys.server.boundary.*;
import java.util.Locale;
import java.util.ResourceBundle;
import messaggistica.GMessage;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Gestore di richiesta, si occupa di gestire tutte le richieste provenienti direttamente
 * dall'utente (attraverso le diverse interfacce grafiche).<br>
 * Gestisce anche la navigabilità tra le interfacce grafiche.
 * E' singleton, ovvero è possibile avere una sola istanza di RequestManager in tutto il sistema.<br>
 * Coincide con il pattern FrontController.
*/
public class RequestManager implements FrontController{
	/**Istanza di se stesso*/
	private static RequestManager obj = null;
        
        public static final String OPEN_LANGUAGE_EDITOR = "EditorLanguage";
        public static final String OPEN_PLANNER = "Pianificazione";
        public static final String OPEN_INTERVENTIONS_EDITOR = "EditorInterventi";
        public static final String OPEN_NURSES_EDITOR = "EditorInfemieri";
        public static final String OPEN_PATHOLOGIES_EDITOR = "EditorPatologie";
        public static final String OPEN_PATIENTS_EDITOR = "EditorPazienti";
        public static final String OPEN_REPEAT_PATHOLOGIES_EDITOR = "EditorRipetiPatologie";
        public static final String OPEN_P_INTERVENTION_TYPE_EDITOR = "EditorPatologieTipoIntervento";
        public static final String OPEN_SEVERITY_EDITOR = "EditorGravita";
        public static final String OPEN_CHECK_DIALOG = "DialogoVerifica";
        public static final String OPEN_EXPORT_DIALOG = "DialogoEsportazione";
        public static final String OPEN_REGISTER_DIALOG = "DialogoStorico";
        
        
        private static ResourceBundle requestManager = ResourceBundle.getBundle("adisys/server/property/RequestManager");
        
        private EditorLanguage editorLanguage = new EditorLanguage();
        
	/**
	 * Mappa che associa la stringa che identifica una boundary al nome
	 * effettivo della boundary
	*/
            private HashMap<String, Boundary> map = new HashMap<String, Boundary>();
	
	/**
	 * Costruttore inizializza la mappa per le boundary
	*/
	private RequestManager(){
		map.put(OPEN_LANGUAGE_EDITOR, editorLanguage);
	}
	
        public static void setResourceBundle(String path, Locale local){
            requestManager = ResourceBundle.getBundle(path, local);
        }
                
        public void addService() throws MainException {
            map.put(OPEN_PLANNER, new Pianificatore());
            map.put(OPEN_INTERVENTIONS_EDITOR, new EditorInterventi());
            map.put(OPEN_NURSES_EDITOR, new EditorInfermieri());
            map.put(OPEN_PATHOLOGIES_EDITOR, new EditorPatologie());
            map.put(OPEN_PATIENTS_EDITOR, new EditorPazienti());
            map.put(OPEN_REPEAT_PATHOLOGIES_EDITOR, new EditorRipetiPatologie());
            map.put(OPEN_P_INTERVENTION_TYPE_EDITOR, new EditorPatologieTipoIntervento());
            map.put(OPEN_SEVERITY_EDITOR, new EditorGravita());
            map.put(OPEN_CHECK_DIALOG, new DialogoVerifica());
            map.put(OPEN_EXPORT_DIALOG, new DialogoEsportazione());
            map.put(OPEN_REGISTER_DIALOG, new DialogoStorico());
        }
        
	/**
	 * Implementazione del pattern Singleton. Non avendo un costruttore public, si pu&ograve ottenere
	 * una istanza di {@link RequestManager} solo attraverso questo metodo che restituisce
	 * una nuova istanza se e solo se non ne esiste un'altra.
	*/
	public static RequestManager getFCInstance(){
		if(obj == null){
			obj = new RequestManager();
		}
		
		return obj;
	}

	
	
	@Override
	public Object processRequest(String requestMethod, ArrayList< Record<String, Object> > params)
		throws MainException{
		System.out.println("RequestManager : processRequest("+requestMethod+")");
		
		//ottiene l'application controller al quale gli passiamo il nome della
		//funzionalita' voluta. L'AC processa quel nome e capisce quale classe
		//deve istanziare per quella funzionalita'
		ApplicationController applicationController = new ApplicationController(requestMethod);
		
		
		//una volta che lo ha capito deve evocare il metodo che svolge la funzionalita' voluta
		//con i parametri e lo restituisce, cosi' che la boundary chiamante ottiene
		//i dati che vuole
		try {
			return applicationController.handleRequest(requestMethod, params);
		}
		catch (SecurityException e){
			e.printStackTrace();
                        String message = requestManager.getString("NON SI POSSIEDONO I PERMESSI PER EFFETTUARE L'OPERAZIONE");
                        GMessage.message_error(message);
			throw new MainException (message);
		}
		catch (NoSuchMethodException e){
			e.printStackTrace();
                        String message = requestManager.getString("IL METODO RICHIESTO NON È STATO TROVATO!");
                        GMessage.message_error(message);
			throw new MainException (message);
		}
		catch (InvocationTargetException ite){
			ite.printStackTrace();
			String message = requestManager.getString("IL METODO INVOCATO HA SOLLEVATO UN'ECCEZIONE!");
                        GMessage.message_error(message);
			throw new MainException (message);
		}
		catch (Exception e) {
			e.printStackTrace();
			String message = requestManager.getString("ECCEZIONE GENERALE");
                        GMessage.message_error(message);
			throw new MainException (message);
		}
	}
	
	
	
	
	
	@Override
	public boolean processRequest(String request) {
		System.out.println("RequestManager, WindowNavigator : processRequest("+request+")");
		try{
                        Boundary b = map.get(request);
                        b.open();
			return true;
		}catch(NullPointerException e){
                    String message = requestManager.getString("OPS... IL REQUESTMANAGER (FRONT CONTROLLER) NON HA TROVATO LA CLASSE") +
					requestManager.getString("CHE GESTISCE LA FUNZIONALITA' RICHIESTA ")+request;
			GMessage.message_error(message);
			e.printStackTrace();
			return false;
		}
	}	
}
