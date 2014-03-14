package presentation;

import java.util.ArrayList;

import messaggistica.MainException;

import adisys.server.strumenti.Record;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia di {@link RequestManager}
*/
public interface FrontController {
	
	/**
	 * Gestisce le richieste proveniente da una finestra (classe boundary).<br>
	 * Il tipo di richiesta gestibile da questo metodo è
	 * <ul><li>
	 * 		<i>Richiesta di un servizio di business</i>.
	 * 		Una finestra chiede al FrontController un servizio di business, il FrontController
	 * 		esegue il corrispondente "manipolatore di richiesta" (<i>RequestHandler</i>)
	 * 		cio&egrave la classe in grado di fornire l'effettivo servizio.<br>
	 * 		Successivamente sar&agrave l'{@link business.ApplicationController} con l'ausilio
	 * 		del {@link business.ServiceHandler} a capire quale metodo della RequestHandler evocare
	 * </li></ul>
	 * 
	 * @param requestMethod la stringa che identifica la funzionalità richiesta
	 * @param params una ArrayList di {@link Record} con chiave=funzionalit&agrave(metodo) e 
	 * valore=parametri(del metodo)
	*/
	public Object processRequest(String requestMethod, ArrayList< Record<String, Object> > params) throws MainException;
	
	
	
	
	
	
	/**
	 * Richiesta senza parametri, <u>usarla solo</u> per la richiesta specificata qui sotto.<br>
	 * Per invocare funzionalità che non richiedono parametri, usare il metodo
	 * {@link #processRequest(String, ArrayList)} passando null invece di un ArrayList.<br>
	 * Il tipo di richiesta gestibile da questo metodo &egrave
	 * <ul><li>
	 * 		<i><b>Navigazione tra finestre</b></i>. Ovvero una finestra pu&ograve chiamare
	 * 		il FrontController per passare ad un altra finestra. Nella stringa &egrave
	 * 		specificata una funzionalit&agrave
	 * </li></ul>
	 * Overloading di {@link #processRequest(String, ArrayList)}
	 * @param request la richiesta che identifica una interfaccia grafica secondo il protocollo
	 * @return true se l'apertura della finestra &egrave avvenuta con successo, false altrimenti
	*/
	public boolean processRequest(String request);
	
}
