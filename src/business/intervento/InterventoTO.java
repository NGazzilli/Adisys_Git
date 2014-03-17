package business.intervento;

import adisys.server.strumenti.DateFormatConverter;
import adisys.server.strumenti.Record;
import business.TO;
import business.infermiere.InfermiereTO;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import messaggistica.MainException;
import presentation.FrontController;
import presentation.RequestManager;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Transfer object dei dati singoli di intervento, non comprende i dati del paziente
 * ad esso associato.
*/
public class InterventoTO implements TO {
    
        private static final String formatInputDate = "dd/MM/yyyy";
	private static final String formatInputTime = "HH.mm";
        
        private static ResourceBundle intervention = ResourceBundle.getBundle("adisys/server/property/Intervento");
    
        public static void setResourceBundle(String path, Locale local){
        	intervention = ResourceBundle.getBundle(path, local);
        }
        
        //Campi
	private int ID;
	private int patientID;
	private int nurseID;

	private Date date;
	private Time startTime;
	private Time endTime;

	private String city;
	private String address;
	private String cap;

	protected ArrayList<TipoIntervento> interventionTypes;

	public InterventoTO()
	{
		date= new Date(0);
		startTime = new Time(0);
		endTime = new Time(0); 
		interventionTypes = new ArrayList<>();
	}

	public int getID() { 
		return ID; 
	}

	public int getIDPaziente() {
		return patientID;
	}

	public int getIDInfermiere() {
		return nurseID;
	}

	public String getData()
	{
		return date.toString();
	}

	public String getOraInizio()
	{
		return startTime.toString();
	}
	public String getOraFine()
	{
		return endTime.toString();
	}

	public String getCitta() {
		return city;
	}

	public String getCivico() {
		return address;
	}

	/**
	 * @return the cap
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * @param cap the cap to set
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	public String setID(String ID) {

		String error="";

		//Caso stringa vuota
		if (ID.isEmpty()) error+= intervention.getString("ID INTERVENTO NON SPECIFICATO");
		else
		{
			this.ID = Integer.valueOf(ID);
		}

		//Restituzione stringa
		return error;
	}

	public void setID(int newID)
	{
		this.ID = newID;
	}

	public void setIDPaziente(int patientID) {
		this.patientID = patientID;
	}

	public String setIDPaziente(String newpatientID) {

		String error="";

		//Caso stringa vuota
		if (newpatientID.isEmpty()) error+= intervention.getString("ID PAZIENTE NON SPECIFICATO");
		else
		{
			this.patientID = Integer.valueOf(newpatientID);
		}

		//Restituzione stringa
		return error;
	}

	public void setIDInfermiere(int nurseID) {
		this.nurseID = nurseID;
	}

	public String setIDInfermiere(String newnurseID) {

		String error="";

		//Caso stringa vuota
		if (newnurseID.isEmpty()) error+= intervention.getString("ID INFERMIERE NON SPECIFICATO");
		else
		{
			this.nurseID = Integer.valueOf(newnurseID);
		}

		//Restituzione stringa
		return error;
	}

	public boolean setData(String newDate) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatInputDate);
			date.setTime(sdf.parse(newDate).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervention.getString("PARSING DATA FALLITO"));
			return false;

		}
	}

	public boolean setOraInizio(String newStartTime) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatInputTime);
			startTime.setTime(sdf.parse(newStartTime).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervention.getString("PARSING ORA INIZIO INTERVENTO FALLITO"));
			return false;

		}
	}
	public boolean setOraFine(String newEndTime) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatInputTime);
			startTime.setTime(sdf.parse(newEndTime).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervention.getString("PARSING ORA INIZIO INTERVENTO FALLITO"));
			return false;

		}
	}
	public void setDataFmt(String strData, String inputFormat)
	{
		date.setTime(DateFormatConverter.dateString2long(strData, inputFormat) );
	}

	public void setOraInizioFmt(String strOra, String inputFormat)
	{
		startTime.setTime(DateFormatConverter.dateString2long(strOra, inputFormat) );
	}

	public void setOraFineFmt(String strOra, String inputFormat)
	{
		endTime.setTime(DateFormatConverter.dateString2long(strOra, inputFormat) );
	}



	public void setCitta(String city) {
		this.city = city;
	}

	public void setCivico(String address) {
		this.address = address;
	}
	
	/**Restituisce data dell'intervento in un formato specifico (es. "dd/MM/yyyy")
	 *
	 */
	public String getDataDaFormato(String dateFormat) {
		return DateFormatConverter.long2dateString(date.getTime(), dateFormat);
	}

	/**Restituisce l'ora di inizio dell'intervento in un formato specifico (es. "hh:mm:ss")
	 *
	 */
	public String getOraInizioDaFormato(String timeFormat) {
		return DateFormatConverter.long2dateString(startTime.getTime(), timeFormat);
	}

	/**Restituisce l'ora di fine dell'intervento in un formato specifico (es. "hh:mm:ss")
	 *
	 */
	public String getOraFineDaFormato(String timeFormat) {
		return DateFormatConverter.long2dateString(endTime.getTime(), timeFormat);
	}


	public void addTipoIntervento (TipoIntervento newinterventionTypes)
	{
		interventionTypes.add(newinterventionTypes);
	}

	public void removeTipoIntervento(int index)
	{
		interventionTypes.remove(index);
	}

	public void cancellaTipiIntervento()
	{
		interventionTypes.clear();
	}

	public int countTipInterventi()
	{
		return interventionTypes.size();
	}

	public TipoIntervento getTipoIntervento(int n)
	{
		try{
			return interventionTypes.get(n);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		String interventionString= java.text.MessageFormat.format(intervention.getString("INTERVENTO NUMERO {0}"), new Object[] {getID()});
		interventionString += java.text.MessageFormat.format(intervention.getString("- INFERMIERE ASSEGNATO N.{0}"), new Object[] {getIDInfermiere()});
		interventionString += java.text.MessageFormat.format(intervention.getString("- PAZIENTE N.{0}"), new Object[] {getIDPaziente()});
                interventionString += intervention.getString("- DATA INTERVENTO: ") +getDataDaFormato(intervention.getString("DD/MM/YYYY"));
		interventionString += java.text.MessageFormat.format(intervention.getString("- ORA INIZIO INTERVENTO: {0}"), new Object[] {getOraInizioDaFormato("HH:mm")});
		//stringaIntervento += "\n- Ora fine intervento: " + getOraFineDaFormato("HH:mm");
		interventionString += java.text.MessageFormat.format(intervention.getString("- INDIRIZZO : {0}"), new Object[] {getCivico()});
		interventionString += java.text.MessageFormat.format(intervention.getString("- CITTÀ: {0} - CAP {1}"), new Object[] {getCitta(), getCap()});
		interventionString += intervention.getString("TIPI DI INTERVENTO: ");
		interventionString += intervention.getString("");
		for(TipoIntervento t: interventionTypes)
			interventionString += java.text.MessageFormat.format(intervention.getString("{0}"), new Object[] {t.toString()});
		return interventionString;
	}
}
