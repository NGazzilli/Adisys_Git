package business.infermiere;

import adisys.server.strumenti.ComboItem;
import integration.dao.InfermiereMySqlDAO;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia per la entity {@link Infermiere}, offre il metodo di restituzione
 * di tutti gli infermieri
*/
public interface I_InfermieriGet {
	
	/**
	 * Restituisce tutti i dati degli infermieri presenti nel database
	 * @return una struttura dati contenente i to di infermieri {@link InfermiereTO}
	*/
	public ArrayList<InfermiereTO> getDati();
        
        public ArrayList<ComboItem> getArrayInfermieri();
        
        public AbstractTableModel getTabella();
        
        public AbstractTableModel getInfermieriConInterventi();
        
        public InfermiereTO getInfermiere(int idNurse);

        public boolean exists(int idNurse);
        
}
