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
	
        public DefaultListModel<String> getCellulari(int idPaziente);

        public String getPaziente(int idPaziente);
        
        public boolean exists(String nome, String cognome, Object[] cellulari);
        
}
