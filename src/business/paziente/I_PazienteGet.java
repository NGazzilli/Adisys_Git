package business.paziente;

import javax.swing.DefaultListModel;
import javax.swing.table.AbstractTableModel;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia per la entity {@link Paziente}, offre il metodo di restituzione
 * di tutti gli infermieri
*/
public interface I_PazienteGet {
	        
        public AbstractTableModel getTabella();
	
        public DefaultListModel<String> getCellulari(int idPatient);

        public String getPaziente(int idPatient);
        
        public boolean exists(String name, String surname, Object[] cellulars);
        
}
