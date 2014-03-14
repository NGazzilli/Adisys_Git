package business.paziente;

import adisys.server.strumenti.DateFormatConverter;
import business.TO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Transfer Object dell'entita' infermiere. Incapsula tutti i dati del paziente
 * Funzionalità coincidenti con il pattern Transfer Object
*/
public class PazienteTO implements TO{
    	
    private int ID;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private ArrayList<String> cellulari;    //Modifica Ver 2: sostituisce "cellulare"
    
    private static ResourceBundle paziente = ResourceBundle.getBundle("adisys/server/property/Paziente");
	
    public static void setResourceBundle(String path, Locale locale){
            paziente = ResourceBundle.getBundle(path, locale);
    }

    public PazienteTO()
    {
    	cellulari=new ArrayList<String>();
    }
    
    public PazienteTO(String nome, String cognome, String dataNascita, Object[] cellulari)
    {
    	this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = new Date(DateFormatConverter.dateString2long(dataNascita, DateFormatConverter.getFormatData()));
        this.cellulari=new ArrayList<String>();
        for(int i = 0; i < cellulari.length; i++){
            addCellulare(cellulari[i].toString());
        }
    }
    
    public PazienteTO(int id, String nome, String cognome, String dataNascita, Object[] cellulari)
    {
        this.ID = id;
    	this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = new Date(DateFormatConverter.dateString2long(dataNascita, DateFormatConverter.getFormatData()));
        this.cellulari=new ArrayList<String>();
        for(int i = 0; i < cellulari.length; i++){
            addCellulare(cellulari[i].toString());
        }
    }
    
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return Il nome del paziente
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return il cognome del paziente
     */
    public String getCognome() {
        return cognome;
    }

    public String getDataNascita(String formato) {
		if (dataNascita!=null)
			return DateFormatConverter.long2dateString(dataNascita.getTime(), formato);
		else return "";
	}

	/**Modificato in versione 2 da getCellulare a getCellulari
	 * Metodo che restituisce un array di numeri di telefono (Qbject[] che contiene stringhe)
     * @return il numero di cellulare del paziente
     */
    public Object[] getCellulari() {
        return (cellulari.toArray());
    }

    /**Aggiunta in versione 2: Cancellazione numeri di telefono del paziente
     * 
     */
    public void clearCellulari()
    {
    	cellulari.clear();
    }
    
    /**
     * @param ID il nuovo ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param cognome the cognome to set
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**Aggiunta numero cellulare (sostituisce setCellulare nella vers.2)
     * @param cellulare the cellulare to set
     */
    public void addCellulare(String cellulare) {
        this.cellulari.add(cellulare);
    }

    public void setDataNascita(String newDataNascita , String formato) {

        dataNascita = new Date(DateFormatConverter.dateString2long(newDataNascita, formato));
    }

    public String calcolaAnniMesi()
    {
    	GregorianCalendar today = new GregorianCalendar();
    	GregorianCalendar nascita = new GregorianCalendar();
    	nascita.setTimeInMillis(dataNascita.getTime());
    	
    	int yearDiff  = today.get(Calendar.YEAR) - nascita.get(Calendar.YEAR); 
		int monthDiff = (yearDiff * 12 + today.get(Calendar.MONTH) - nascita.get(Calendar.MONTH)) %12;
    	
    	String formatoprovvisorio = "%02D/%02D";
    	Formatter formatter = new Formatter();
    	formatter.format(formatoprovvisorio, yearDiff,monthDiff);
    	
    	System.out.println(java.text.MessageFormat.format(paziente.getString("PAZINTE -> ELABORAZIONE ANNI/MESI: {0}"), new Object[] {formatter.toString()}));
    	return formatter.toString();

    }
    @Override
    public String toString()
    {
    	String stringaPaziente= new String();
    	stringaPaziente += java.text.MessageFormat.format(paziente.getString("ID PAZIENTE: {0}"), new Object[] {getID()});
    	stringaPaziente += java.text.MessageFormat.format(paziente.getString("- NOME: {0}"), new Object[] {getNome()});
    	stringaPaziente += java.text.MessageFormat.format(paziente.getString("- COGNOME: {0}"), new Object[] {getCognome()});
    	stringaPaziente += java.text.MessageFormat.format(paziente.getString("- DATA DI NASCITA: {0}"), new Object[] {getDataNascita("dd/MM/yyyy")});
    	for(String c:cellulari)
    		stringaPaziente += java.text.MessageFormat.format(paziente.getString("- NUMERO DI CELLULARE: {0}"), new Object[] {c});
    	return stringaPaziente;
    }
    
}
