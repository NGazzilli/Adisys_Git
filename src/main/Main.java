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
 * Main del sistema. Qui parte tutto, utilizzando {@link JustOneLock} si assicura che solo una
 * istanza di adisys parta
*/
public class Main {

        private static FrontController FC;
        private static ResourceBundle main = ResourceBundle.getBundle("adisys/server/property/Main");
	
        public static void setResourceBundle(String path, Locale locale){
            main = ResourceBundle.getBundle(path, locale);
        }
                
        public static void main(String[] args) throws MainException {
		
		JustOneLock ua = new JustOneLock("ADISys");
		if (ua.isAppActive()) {
			//c'e' gia' un'altra istanza di questa stessa applicazione, esco
			System.out.println(main.getString("APPLICAZIONE GIÀ IN ESECUZIONE."));
			
			GMessage.information(main.getString("ADISYS E' GIA' IN ESECUZIONE"));
			
			System.exit(0);
		}else {
                    //prima istanza dell'applicazione
                    System.out.println(main.getString("APPLICAZIONE ESEGUITA"));
		  
                    if(business.configurazione.Configurazione.checkConfigurazioneExists()){
                        FC = RequestManager.getFCInstance();
                        SplashScreen.start();
                        if(FC.processRequest(RequestManager.APRI_EDITOR_LANGUAGE)){
                            System.out.println("Finestra EditorLanguage aperta con successo");
                            //util.Sound.riproduciAvvio();
                             adisys.server.strumenti.Sound.riproduciAvvio();
                            ConfigurazioneTO configTO = null;
                            try {
                                configTO = new ConfigurazioneTO();
                            } catch (MainException ex) {
                                Logger.getLogger(EditorLanguage.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ArrayList<Record<String,Object>> params= new ArrayList<Record<String,Object>>();				
                            params.add(new Record<String,Object> ("business.configurazione.ConfigurazioneTO", configTO));
                            try {
                                setConfigurazione(params);
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
        }       
        
    /**
     * Setta la configurazione chiamando il front controller. Se il settaggio ha esito positivo, 
     * allora il database è connesso, ed è possibile interagire con esso e con le interfacce 
     * che lo utilizzano
    */
    private static void setConfigurazione(ArrayList<Record<String,Object>> params) throws MainException{
        if(!(Boolean) FC.processRequest("setDatiConfigurazione", params)){
            GMessage.message_error(main.getString("CONFIG"));
        }
        else{
            System.out.println(main.getString("CONNESSIONE AVVENUTA CON SUCCESSO"));
        }
    }
}
