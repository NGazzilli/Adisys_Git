package main;

import adisys.server.boundary.EditorLanguage;
import adisys.server.boundary.SplashScreen;
import business.configurazione.ConfigurazioneTO;
import messaggistica.MainException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import presentation.FrontController;
import presentation.RequestManager;
import adisys.server.strumenti.Record;
import java.util.Locale;
import messaggistica.GMessage;
import java.util.ResourceBundle;

/**
 * 
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 *
 */
public class Main {

	private static FrontController frontController;
	private static ResourceBundle main = ResourceBundle.getBundle("adisys/server/property/Main");

	public static void setResourceBundle(String path, Locale local){
		main = ResourceBundle.getBundle(path, local);
	}

	public static void main(String[] args) throws MainException {

		//prima istanza dell'applicazione
		System.out.println(main.getString("APPLICAZIONE ESEGUITA"));

		if(business.configurazione.Configurazione.checkConfigurazioneExists()){
			frontController = RequestManager.getFCInstance();
			SplashScreen.start();
			if(frontController.processRequest(RequestManager.OPEN_LANGUAGE_EDITOR)){
				System.out.println("Finestra EditorLanguage aperta con successo");
				ConfigurazioneTO configTO = null;
				try {
					configTO = new ConfigurazioneTO();
				} catch (MainException ex) {
					Logger.getLogger(EditorLanguage.class.getName()).log(Level.SEVERE, null, ex);
				}
				ArrayList<Record<String,Object>> params= new ArrayList<Record<String,Object>>();				
				params.add(new Record<String,Object> ("business.configurazione.ConfigurazioneTO", configTO));
				try {
					setConfiguration(params);
				} catch (MainException e1) {
					GMessage.message_error(main.getString("GENERALE"));
					e1.printStackTrace();
				}
				SplashScreen.stop();
			}
		} else {
			String message = main.getString("ECCEZIONE, RISULTA CHE IL FILE DI CONFIGURAZIONE NON ESISTE.");
			GMessage.message_error(message);
		}


	}       

	/**
	 * Imposta la configurazione chiamando il front controller. Se la configurazione ha esito positivo, 
	 * allora il database è connesso, ed è possibile interagire con esso e con le interfacce 
	 * che lo utilizzano
	 */
	private static void setConfiguration(ArrayList<Record<String,Object>> params) throws MainException{
		if(!(Boolean) frontController.processRequest("setDatiConfigurazione", params)){
			GMessage.message_error(main.getString("CONFIG"));
		}
		else{
			System.out.println(main.getString("CONNESSIONE AVVENUTA CON SUCCESSO"));
		}
	}
}
