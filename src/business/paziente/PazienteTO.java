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
    private String name;
    private String surname;
    private Date birthDay;
    private ArrayList<String> cellulars;    //Modifica Ver 2: sostituisce "cellulare"
    
    private static ResourceBundle patient = ResourceBundle.getBundle("adisys/server/property/Paziente");
	
    public static void setResourceBundle(String path, Locale local){
    	patient = ResourceBundle.getBundle(path, local);
    }

    public PazienteTO()
    {
    	cellulars=new ArrayList<String>();
    }
    
    public PazienteTO(String name, String surname, String birthDay, Object[] cellulars)
    {
    	this.name = name;
        this.surname = surname;
        this.birthDay = new Date(DateFormatConverter.dateString2long(birthDay, DateFormatConverter.getFormatData()));
        this.cellulars=new ArrayList<String>();
        for(int i = 0; i < cellulars.length; i++){
            addCellulare(cellulars[i].toString());
        }
    }
    
    public PazienteTO(int id, String name, String surname, String birthDay, Object[] cellulars)
    {
        this.ID = id;
    	this.name = name;
        this.surname = surname;
        this.birthDay = new Date(DateFormatConverter.dateString2long(birthDay, DateFormatConverter.getFormatData()));
        this.cellulars=new ArrayList<String>();
        for(int i = 0; i < cellulars.length; i++){
            addCellulare(cellulars[i].toString());
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
        return name;
    }

    /**
     * @return il cognome del paziente
     */
    public String getCognome() {
        return surname;
    }

    public String getDataNascita(String format) {
		if (birthDay!=null)
			return DateFormatConverter.long2dateString(birthDay.getTime(), format);
		else return "";
	}

	/**Modificato in versione 2 da getCellulare a getCellulari
	 * Metodo che restituisce un array di numeri di telefono (Qbject[] che contiene stringhe)
     * @return il numero di cellulare del paziente
     */
    public Object[] getCellulari() {
        return (cellulars.toArray());
    }

    /**Aggiunta in versione 2: Cancellazione numeri di telefono del paziente
     * 
     */
    public void clearCellulari()
    {
    	cellulars.clear();
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
    public void setNome(String name) {
        this.name = name;
    }

    /**
     * @param cognome the cognome to set
     */
    public void setCognome(String surname) {
        this.surname = surname;
    }

    /**Aggiunta numero cellulare (sostituisce setCellulare nella vers.2)
     * @param cellulare the cellulare to set
     */
    public void addCellulare(String cellular) {
        this.cellulars.add(cellular);
    }

    public void setDataNascita(String newBirthDay , String format) {

    	birthDay = new Date(DateFormatConverter.dateString2long(newBirthDay, format));
    }

    public String calcolaAnniMesi()
    {
    	GregorianCalendar today = new GregorianCalendar();
    	GregorianCalendar birth = new GregorianCalendar();
    	birth.setTimeInMillis(birthDay.getTime());
    	
    	int yearDiff  = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR); 
		int monthDiff = (yearDiff * 12 + today.get(Calendar.MONTH) - birth.get(Calendar.MONTH)) %12;
    	
    	String formatoprovvisorio = "%02D/%02D";
    	Formatter formatter = new Formatter();
    	formatter.format(formatoprovvisorio, yearDiff,monthDiff);
    	
    	System.out.println(java.text.MessageFormat.format(patient.getString("PAZINTE -> ELABORAZIONE ANNI/MESI: {0}"), new Object[] {formatter.toString()}));
    	return formatter.toString();

    }
    @Override
    public String toString()
    {
    	String patientString= new String();
    	patientString += java.text.MessageFormat.format(patient.getString("ID PAZIENTE: {0}"), new Object[] {getID()});
    	patientString += java.text.MessageFormat.format(patient.getString("- NOME: {0}"), new Object[] {getNome()});
    	patientString += java.text.MessageFormat.format(patient.getString("- COGNOME: {0}"), new Object[] {getCognome()});
    	patientString += java.text.MessageFormat.format(patient.getString("- DATA DI NASCITA: {0}"), new Object[] {getDataNascita("dd/MM/yyyy")});
    	for(String c:cellulars)
    		patientString += java.text.MessageFormat.format(patient.getString("- NUMERO DI CELLULARE: {0}"), new Object[] {c});
    	return patientString;
    }
    
}
