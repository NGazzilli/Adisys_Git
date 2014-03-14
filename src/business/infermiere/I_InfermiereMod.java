package business.infermiere;

import java.util.ArrayList;


/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia per la entity {@link Paziente}, offre i metodi di creazione di un paziente
 * e la cancellazione di tutti i pazienti
*/
public interface I_InfermiereMod {

	public boolean createInfermiere(InfermiereTO to);
	
        public boolean deleteInfermiere(InfermiereTO to);
        
        public boolean modificaInfermiere(InfermiereTO to);

        public boolean reset();
        
        public boolean alterTable(ArrayList<String> campi);
        
}
