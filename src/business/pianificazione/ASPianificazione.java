/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.pianificazione;

import business.infermiere.InfermiereTO;
import business.intervento.InterventoTO;
import messaggistica.MainException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 */
public class ASPianificazione extends business.ServiceHandler{
	
	/**
	 * Costruttore, come ogni costruttore delle altre classi di servizio, inizializza
	 * la hashmap con le corrispondenze nome funzionalit&agrave -> metodo
	*/
	public ASPianificazione(){		
		map.put("esportaPianificazione", "esporta");
	}
	
        private Pianificazione pian;
        
        /**
	 * Esportazione della pianificazione in un file. Usa {@link Pianificazione}
	 * @return il nome del file appena creato, null se non riesce a creare nulla
	*/
	public String esporta(InfermiereTO infTO, ArrayList<InterventoTO> intTOs) {
            try {
                pian = new Pianificazione();
                return pian.esportaPianificazione(infTO, intTOs);
            } catch (MainException ex) {
                Logger.getLogger(ASPianificazione.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
	}
	
    
}
