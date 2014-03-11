package adisys.server.strumenti;

import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


/**
 * @author Luca
 *
 */
public class ADISysTableModel extends AbstractTableModel implements TableModel {

	ResultSet dati;
      
        
	public ADISysTableModel(ResultSet nuoviDati)
	{
		dati=nuoviDati;
                
                
		try {
			//Trace
			System.out.println("- Creazione modello tabella: \""+ dati.getMetaData().getTableName(1) +"\"");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	
	@Override
	public int getColumnCount() {
	
		try {
			return dati.getMetaData().getColumnCount();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE: Calcolo del numero di colonne errato.");
			return 0;
		}
	}

	@Override
	public int getRowCount() {
				
		try {
			//Seleziona l'ultimo elemento
			dati.last();
			//Restituisce l'indice dell'elemento
			return (dati.getRow());
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERRORE: Calcolo del numero di righe errato. (metodo getRowCount() )");
			return 0;
		}
	}

	@Override
	public Object getValueAt(int riga, int colonna) {

		try {
			//Sposta il cursore alla riga desiderata (con sfasamento di 1)
			dati.absolute(riga+1);
			
			//Estrae il valore nella colonna specificata e lo restituisce (con sfasamento di 1)
			return dati.getObject(colonna+1);
			
		} catch (SQLException e) {
			// In caso di errore restituisce un oggetto vuoto
			e.printStackTrace();
			
			//Trace
			System.out.println("ERRORE: Valore dell'elemento della tabella non valido.");
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int rIndex, int cIndex)
	{
            /*if(flgTabellaEditorPatologie == 1 && cIndex == 3)
                return true;
            else*/
		return false;
	}
        
        
	@Override
    public String getColumnName(int col) {
        try {
			return dati.getMetaData().getColumnName(col+1);
		} catch (SQLException e) {
			// Eccezione
			e.printStackTrace();
			return "?";
		}
    }

	public Integer getID(int riga)
	{
		//Ricerca colonna ID
		for(int i=0; i<=getColumnCount(); i++)
			if(getColumnName(i).equals("ID"))
				return i;
		return null;
	}
	
	
	/**
	 * Restituisce l'indice della colonna a partire dal nome della colonna ricercata
	 * <b>N.B. L'indice della prima colonna è 0, l'ultimo è numeroColonne-1.</b>
	 * @param Nome - Stringa con il nome della colonna
	 * @return -1 se la colonna non e' stata trovata, altrimenti l'indice della colonna
	 */
	public int getColumnIndex(String nome)
	{
		for (int i=0; i<getColumnCount();i++)
			if( getColumnName(i)==nome) return i;
		return -1;
	}

}
