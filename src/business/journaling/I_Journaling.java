/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.journaling;

import business.intervento.InterventoCompletoTO;
import java.util.ArrayList;

/**
 *
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 */
public interface I_Journaling {
    
      public String[] getListaJournaling();
        
        public InterventoCompletoTO getIntervento(int idIntervento);
        
        public ArrayList<InterventoCompletoTO> getListaInterventi();
        
        public String caricaFile(String nomeFile);
           
        
}
