/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.journaling;

import business.intervento.InterventoCompletoTO;
import business.intervento.InterventoTO;
import messaggistica.MainException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 */
public class ASJournaling extends business.ServiceHandler {
    	
        public ASJournaling(){		
		map.put("visualizzaListaJournaling", "getListaJournaling");
                map.put("visualizzaInterventoCompleto", "getInterventoCompleto");
                map.put("visualizzaListaInterventi", "getListaInterventi");
                map.put("caricaFile", "caricaFile");
	}
        
        private I_Journaling journ;
        
                /**
	 * Esportazione della pianificazione in un file. Usa {@link Pianificazione}
	 * @return il nome del file appena creato, null se non riesce a creare nulla
	*/
	public String[] getListaJournaling() {
            try {
                journ = new Journaling();
                return journ.getListaJournaling();
            } catch (MainException ex) {
                Logger.getLogger(ASJournaling.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
	}
        
        public InterventoCompletoTO getInterventoCompleto(InterventoTO to){
            try {
                journ = new Journaling();
                return journ.getIntervento(to.getID());
            } catch (MainException ex) {
                Logger.getLogger(ASJournaling.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        public ArrayList<InterventoCompletoTO> getListaInterventi(){
            try {
                journ = new Journaling();
                return journ.getListaInterventi();
            } catch (MainException ex) {
                Logger.getLogger(ASJournaling.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } 
        }
        
        public String caricaFile(String fileName){
             try {
                journ = new Journaling();
                return journ.caricaFile(fileName);
            } catch (MainException ex) {
                Logger.getLogger(ASJournaling.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } 
        }
}
