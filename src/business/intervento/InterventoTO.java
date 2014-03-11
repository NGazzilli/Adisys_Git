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
 * Transfer object dei dati singoli di intervento, non comprende i dati del paziente
 * ad esso associato.
*/
public class InterventoTO implements TO {
    
        private static final String formatoDataInput = "dd/MM/yyyy";
	private static final String formatoOraInput = "HH.mm";
        
        private static ResourceBundle intervento = ResourceBundle.getBundle("adisys/server/property/Intervento");
    
        public static void setResourceBundle(String path, Locale locale){
            intervento = ResourceBundle.getBundle(path, locale);
        }
        
        //Campi
	private int ID;
	private int IDPaziente;
	private int IDInfermiere;

	private Date data;
	private Time oraInizio;
	private Time oraFine;

	private String citta;
	private String civico;
	private String cap;

	protected ArrayList<TipoIntervento> tipiIntervento;

	public InterventoTO()
	{
		//TODO Verifica aggiornamento costruttore
		data= new Date(0);
		oraInizio = new Time(0);
		oraFine = new Time(0); 
		tipiIntervento = new ArrayList<>();
	}

	public int getID() { 
		return ID; 
	}

	public int getIDPaziente() {
		return IDPaziente;
	}

	public int getIDInfermiere() {
		return IDInfermiere;
	}

	public String getData()
	{
		return data.toString();
	}

	public String getOraInizio()
	{
		return oraInizio.toString();
	}
	public String getOraFine()
	{
		return oraFine.toString();
	}

	public String getCitta() {
		return citta;
	}

	public String getCivico() {
		return civico;
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

		String errore="";

		//Caso stringa vuota
		if (ID.isEmpty()) errore+= intervento.getString("ID INTERVENTO NON SPECIFICATO");
		else
		{
			this.ID = Integer.valueOf(ID);
		}

		//Restituzione stringa
		return errore;
	}

	public void setID(int newID)
	{
		this.ID = newID;
	}

	public void setIDPaziente(int IDPaziente) {
		this.IDPaziente = IDPaziente;
	}

	public String setIDPaziente(String newIDPaziente) {

		String errore="";

		//Caso stringa vuota
		if (newIDPaziente.isEmpty()) errore+= intervento.getString("ID PAZIENTE NON SPECIFICATO");
		else
		{
			this.IDPaziente = Integer.valueOf(newIDPaziente);
		}

		//Restituzione stringa
		return errore;
	}

	public void setIDInfermiere(int IDInfermiere) {
		this.IDInfermiere = IDInfermiere;
	}

	public String setIDInfermiere(String newIDInfermiere) {

		String errore="";

		//Caso stringa vuota
		if (newIDInfermiere.isEmpty()) errore+= intervento.getString("ID INFERMIERE NON SPECIFICATO");
		else
		{
			this.IDInfermiere = Integer.valueOf(newIDInfermiere);
		}

		//Restituzione stringa
		return errore;
	}

	public boolean setData(String nuovaData) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatoDataInput);
			data.setTime(sdf.parse(nuovaData).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervento.getString("PARSING DATA FALLITO"));
			return false;

		}
	}

	public boolean setOraInizio(String nuovaOraInizio) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatoOraInput);
			oraInizio.setTime(sdf.parse(nuovaOraInizio).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervento.getString("PARSING ORA INIZIO INTERVENTO FALLITO"));
			return false;

		}
	}
	public boolean setOraFine(String nuovaOraFine) {
		try {
			//Tentativo di parsing
			SimpleDateFormat sdf = new SimpleDateFormat(formatoOraInput);
			oraInizio.setTime(sdf.parse(nuovaOraFine).getTime());
			return true;
		} catch (ParseException e) {

			//Caso fallimento parsing
			e.printStackTrace();
			System.out.println(intervento.getString("PARSING ORA INIZIO INTERVENTO FALLITO"));
			return false;

		}
	}
	public void setDataFmt(String strData, String formatoIngresso)
	{
		data.setTime(DateFormatConverter.dateString2long(strData, formatoIngresso) );
	}

	public void setOraInizioFmt(String strOra, String formatoIngresso)
	{
		oraInizio.setTime(DateFormatConverter.dateString2long(strOra, formatoIngresso) );
	}

	public void setOraFineFmt(String strOra, String formatoIngresso)
	{
		oraFine.setTime(DateFormatConverter.dateString2long(strOra, formatoIngresso) );
	}



	public void setCitta(String citta) {
		this.citta = citta;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	/*
	public String setTimestampDaStringhe(String data, String ora)
	{
		String errore="";
		//Controllo stringhe vuote
		if (data.isEmpty()) errore+="Data vuota";
		if (ora.isEmpty()) errore+=",Ora vuota";

		//Se non ci sono stati errori tenta il parsing e la scrittura del timestamp
		if (errore.isEmpty())
		{

			try {
				//Tentativo di parsing
				String stringaTimeStamp = data + " " + ora;
				SimpleDateFormat sdf = new SimpleDateFormat(formatoDataOraInput);
				dataOra.setTime(sdf.parse(stringaTimeStamp).getTime() );

			} catch (ParseException e) {

				//Caso fallimento parsing
				e.printStackTrace();

				//Aggiunge informazioni alla stringa di errore
				errore+="Parsing data fallito. Formato data non valido.";
				return errore;
			}

		}
		return null;
	}
	 */
	
	/**Restituisce data dell'intervento in un formato specifico (es. "dd/MM/yyyy")
	 *
	 */
	public String getDataDaFormato(String formatoData) {
		return DateFormatConverter.long2dateString(data.getTime(), formatoData);
	}

	/**Restituisce l'ora di inizio dell'intervento in un formato specifico (es. "hh:mm:ss")
	 *
	 */
	public String getOraInizioDaFormato(String formatoOra) {
		return DateFormatConverter.long2dateString(oraInizio.getTime(), formatoOra);
	}

	/**Restituisce l'ora di fine dell'intervento in un formato specifico (es. "hh:mm:ss")
	 *
	 */
	public String getOraFineDaFormato(String formatoOra) {
		return DateFormatConverter.long2dateString(oraFine.getTime(), formatoOra);
	}


	public void addTipoIntervento (TipoIntervento nuovoTipoIntervento)
	{
		tipiIntervento.add(nuovoTipoIntervento);
	}

	public void removeTipoIntervento(int indice)
	{
		tipiIntervento.remove(indice);
	}

	public void cancellaTipiIntervento()
	{
		tipiIntervento.clear();
	}

	public int countTipInterventi()
	{
		return tipiIntervento.size();
	}

	public TipoIntervento getTipoIntervento(int n)
	{
		try{
			return tipiIntervento.get(n);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		String stringaIntervento= java.text.MessageFormat.format(intervento.getString("INTERVENTO NUMERO {0}"), new Object[] {getID()});
                stringaIntervento += java.text.MessageFormat.format(intervento.getString("- INFERMIERE ASSEGNATO N.{0}"), new Object[] {getIDInfermiere()});
                stringaIntervento += java.text.MessageFormat.format(intervento.getString("- PAZIENTE N.{0}"), new Object[] {getIDPaziente()});
		stringaIntervento += intervento.getString("- DATA INTERVENTO: ") +getDataDaFormato(intervento.getString("DD/MM/YYYY"));
		stringaIntervento += java.text.MessageFormat.format(intervento.getString("- ORA INIZIO INTERVENTO: {0}"), new Object[] {getOraInizioDaFormato("HH:mm")});
		//stringaIntervento += "\n- Ora fine intervento: " + getOraFineDaFormato("HH:mm");
		stringaIntervento += java.text.MessageFormat.format(intervento.getString("- INDIRIZZO : {0}"), new Object[] {getCivico()});
		stringaIntervento += java.text.MessageFormat.format(intervento.getString("- CITTÀ: {0} - CAP {1}"), new Object[] {getCitta(), getCap()});
		stringaIntervento += intervento.getString("TIPI DI INTERVENTO: ");
		stringaIntervento += intervento.getString("");
		for(TipoIntervento t: tipiIntervento)
			stringaIntervento += java.text.MessageFormat.format(intervento.getString("{0}"), new Object[] {t.toString()});
		return stringaIntervento;
	}
}
