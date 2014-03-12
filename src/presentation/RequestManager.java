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
 * Gestore di richiesta, si occupa di gestire tutte le richieste provenienti direttamente
 * dall'utente (attraverso le diverse interfacce grafiche).<br>
 * Gestisce anche la navigabilità tra le interfacce grafiche.
 * E' singleton, ovvero è possibile avere una sola istanza di RequestManager in tutto il sistema.<br>
 * Coincide con il pattern FrontController.
*/
public class RequestManager implements FrontController{
	/**Istanza di se stesso*/
	private static RequestManager obj = null;
        
        public static final String APRI_EDITOR_LANGUAGE = "EditorLanguage";
        public static final String APRI_EDITOR_SCELTA = "EditorScelta";
        public static final String APRI_EDITOR_DATABASE = "EditorDatabase";
        public static final String APRI_PIANIFICATORE = "Pianificazione";
        public static final String APRI_EDITOR_INTERVENTI = "EditorInterventi";
        public static final String APRI_EDITOR_INFERMIERI = "EditorInfemieri";
        public static final String APRI_EDITOR_PATOLOGIE = "EditorPatologie";
        public static final String APRI_EDITOR_PAZIENTI = "EditorPazienti";
        public static final String APRI_EDITOR_RIPETI_PATOLOGIE = "EditorRipetiPatologie";
        public static final String APRI_EDITOR_PATOLOGIE_TIPO_INTERVENTO = "EditorPatologieTipoIntervento";
        public static final String APRI_EDITOR_GRAVITA = "EditorGravita";
        public static final String APRI_DIALOGO_VERIFICA = "DialogoVerifica";
        public static final String APRI_DIALOGO_ESPORTAZIONE = "DialogoEsportazione";
        public static final String APRI_DIALOGO_STORICO = "DialogoStorico";
        
        
        private static ResourceBundle requestManager = ResourceBundle.getBundle("adisys/server/property/RequestManager");
        
        private EditorLanguage editorLanguage = new EditorLanguage();
        private EditorScelta editorScelta = new EditorScelta();
        private EditorDatabase editorDatabase = new EditorDatabase();
        /*private EditorInterventi editorInterventi = new EditorInterventi();
        private EditorPatologie editorPatologie = new EditorPatologie();
        private EditorInfermieri editorInfermieri = new EditorInfermieri();
        private EditorPazienti editorPazienti = new EditorPazienti();
        private EditorPatologieTipoIntervento editorPatologieTipoIntervento = new EditorPatologieTipoIntervento();
        private EditorRipetiPatologie editorRipetiPatologie = new EditorRipetiPatologie();
        private EditorGravita editorGravita = new EditorGravita();
        private DialogoVerifica dialogoVerifica = new DialogoVerifica();
        private DialogoEsportazione dialogoEsportazione = new DialogoEsportazione();*/
        
        
	/**
	 * Mappa che associa la stringa che identifica una boundary al nome
	 * effettivo della boundary
	*/
            private HashMap<String, Boundary> map = new HashMap<String, Boundary>();
	
	/**
	 * Costruttore inizializza la mappa per le boundary
	*/
	private RequestManager(){
		map.put(APRI_EDITOR_LANGUAGE, editorLanguage);
                map.put(APRI_EDITOR_SCELTA, editorScelta);
                map.put(APRI_EDITOR_DATABASE, editorDatabase);
                /*map.put(APRI_EDITOR_INTERVENTI, editorInterventi);
                map.put(APRI_EDITOR_INFERMIERI, editorInfermieri);
                map.put(APRI_EDITOR_PATOLOGIE, editorPatologie);
                map.put(APRI_EDITOR_PAZIENTI, editorPazienti);
                map.put(APRI_EDITOR_RIPETI_PATOLOGIE, editorRipetiPatologie);
                map.put(APRI_EDITOR_PATOLOGIE_TIPO_INTERVENTO, editorPatologieTipoIntervento);
                map.put(APRI_EDITOR_GRAVITA, editorGravita);
                map.put(APRI_DIALOGO_VERIFICA, dialogoVerifica);
                map.put(APRI_DIALOGO_ESPORTAZIONE, dialogoEsportazione);*/
	}
	
        public static void setResourceBundle(String path, Locale locale){
            requestManager = ResourceBundle.getBundle(path, locale);
        }
                
        public void addService() throws MainException {
            map.put(APRI_PIANIFICATORE, new Pianificatore());
            map.put(APRI_EDITOR_INTERVENTI, new EditorInterventi());
            map.put(APRI_EDITOR_INFERMIERI, new EditorInfermieri());
            map.put(APRI_EDITOR_PATOLOGIE, new EditorPatologie());
            map.put(APRI_EDITOR_PAZIENTI, new EditorPazienti());
            map.put(APRI_EDITOR_RIPETI_PATOLOGIE, new EditorRipetiPatologie());
            map.put(APRI_EDITOR_PATOLOGIE_TIPO_INTERVENTO, new EditorPatologieTipoIntervento());
            map.put(APRI_EDITOR_GRAVITA, new EditorGravita());
            map.put(APRI_DIALOGO_VERIFICA, new DialogoVerifica());
            map.put(APRI_DIALOGO_ESPORTAZIONE, new DialogoEsportazione());
            map.put(APRI_DIALOGO_STORICO, new DialogoStorico());
        }
        
	/**
	 * Implementazione del pattern Singleton. Non avendo un costruttore public, si pu&ograve ottenere
	 * una istanza di {@link RequestManager} solo attraverso questo metodo che restituisce
	 * una nuova istanza sse non ne esiste un'altra.
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
		ApplicationController ac = new ApplicationController(requestMethod);
		
		
		//una volta che lo ha capito deve evocare il metodo che svolge la funzionalita' voluta
		//con i parametri e lo restituisce, cosi' che la boundary chiamante ottiene
		//i dati che vuole
		try {
			return ac.handleRequest(requestMethod, params);
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
	
	/*
	public static void main(String args[]) throws MainException{
		FrontController frontController = RequestManager.getFCInstance();
		
		//struttura dati per contenere i parametri (TO per i parametri)
		ArrayList< Record<String, Object> > paramsArr = new ArrayList< Record<String, Object> >();
		
		//richiede la funzionalita' getDatiConfigurazione
		ConfigurazioneTO toOut = 
			(ConfigurazioneTO)frontController.processRequest("getDatiConfigurazione", null);
		

		
		if(toOut==null){
			System.out.println("to nullo. Eccezione lanciata sul file");
		}
		else{
			System.out.println(
				"dbName="+toOut.getDbName()+"\n"+
				"dbPassword="+toOut.getDbPassword()+"\n"+
				"dbPath="+toOut.getDbPath()+"\n"+
				"dbUsername="+toOut.getDbUsername()+"\n"+
				"distanza="+toOut.getDistanza()+"\n"+
				"inattivita="+toOut.getInattivita()+"\n"+
				"sysPassword="+toOut.getSysPassword()
			);
		}
		
		
		ConfigurazioneTO toIn = new ConfigurazioneTO("localhost","root", "qwerty777",
				123, 321, "password2");
		
		
		paramsArr.clear();
		//aggiunge i parametri per evocare la funzionalita' setDatiConfigurazione
		paramsArr.add(new Record<String, Object>("business.entity.ConfigurazioneTO", toIn));
		
		//richiede la funzionalita' setDatiConfigurazione
		frontController.processRequest("setDatiConfigurazione", paramsArr);
		
		
		paramsArr.clear();
		paramsArr.add(new Record<String, Object>("java.lang.String", "password"));
		
		System.out.println("checkPassword restituisce "+
				frontController.processRequest("checkPassword", paramsArr));
		
		
		paramsArr.clear();
		paramsArr.add(new Record<String, Object>("java.lang.String", "system_files/infermieri.xml"));
		
		
//		frontController.processRequest("accoda", null);
		frontController.processRequest("accoda", paramsArr);
		
		
		
		
		
		//richiede la funzionalita' di aprire la finestra di autenticazione
//		frontController.processRequest("apriAutenticazione");
		
	}
	*/
	
	
	
}
