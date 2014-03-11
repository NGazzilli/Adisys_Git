/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.strumenti;
import business.intervento.InterventoCompletoTO;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Luca
 */
public class ADISysTableRendererVerifica extends DefaultTableCellRenderer  {
	private static int IND_COLONNA_GPS;
	private static int IND_COLONNA_ACC;

	private static final String VALORE_OK = InterventoCompletoTO.StatoVerifica.verificaOK.toString();
	private static final String VALORE_KO = InterventoCompletoTO.StatoVerifica.anomalia.toString();;
	private static final String VALORE_NULL = InterventoCompletoTO.StatoVerifica.nonVerificato.toString();;

    public ADISysTableRendererVerifica(int colonnaGPS, int colonnaACC) {
        IND_COLONNA_ACC =colonnaACC;
        IND_COLONNA_GPS= colonnaGPS;
    }

	

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) 
	{

		JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String status = (String)value;
		Icon icon=null;
		

		//Icone per la colonna GPS
		if (column==IND_COLONNA_GPS )
		{
			if(value.equals(VALORE_OK))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/GPSOK.png"));
			}
			else if(value.equals(VALORE_KO))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/GPSKO.png"));
			} 
			else if(value.equals(VALORE_NULL))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/GPSnull.png"));
			}
			else
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/GPSerr.png"));
			}
		} 
		else if (column==IND_COLONNA_ACC )
		{
			//Icone per la colonna Accelerometro
			if(value.equals(VALORE_OK))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/ACCOK.png"));
			}
			else if(value.equals(VALORE_KO))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/ACCKO.png"));
			} 
			else if(value.equals(VALORE_NULL))
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/ACCnull.png"));
			}
			else
			{
				icon = new ImageIcon(getClass().getResource("/adisys/server/img/spie/ACCerr.png"));
			}
		}
		
		if(icon == null){
			label.setText(status);
			label.setIcon(null);
		}
		else
		{  
			label.setText("");
			label.setIcon(icon);
		}
		return label;
	}



}
