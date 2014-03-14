package business.patologia;

import business.TO;
import business.infermiere.*;
import integration.dao.PatologiaMySqlDAO;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Interfaccia per la entity {@link Infermiere}, offre il metodo di restituzione
 * di tutti gli infermieri
*/
public interface I_PatologieGet {
	
        public ArrayList<TO> getArrayPatologie(); 
    
        public AbstractTableModel getTabella();
        
        public ResultSet getTabellaPatologieAssociate(int idPatologia);
        	
}
