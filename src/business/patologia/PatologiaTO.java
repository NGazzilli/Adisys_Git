package business.patologia;

import adisys.server.strumenti.ADISysTableModel;
import business.TO;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Transfer Object dell'entita' infermiere. Incapsula tutti i dati dell'infermiere.
 * Funzionalit&agrave coincidenti con il pattern Transfer Object
*/
public class PatologiaTO implements TO{
    
    private int id;
    private String code;
    private String name;
    private int severity = 0;

    private static ResourceBundle pathology = ResourceBundle.getBundle("adisys/server/property/Patologia");
    
    public static void setResourceBundle(String path, Locale local){
    	pathology = ResourceBundle.getBundle(path, local);
    }
        
    public PatologiaTO(String code, String name){
    	this.code = code;
    	this.name = name;
    }
    
    public PatologiaTO(int id, String code, String name){
    	this.id = id;
        this.code = code;
    	this.name = name;
    }
    
      public PatologiaTO(String code, String name, int severity){
    	this.code = code;
    	this.name = name;
    	this.severity = severity;
    }
    
    public PatologiaTO(){
    	
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    /**
     * @return the Codice
     */
    public String getCodice() {
        return code;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return name;
    }

    /**
     * @return the gravita
     */
    public int getGravita() {
        return severity;
    }

    public String setCodice(String code) {

		String error="";

		//Caso stringa vuota
		if (code.isEmpty()) error+= pathology.getString("CODICE PATOLOGIA NON SPECIFICATO");
		else
		{
			this.code = code;
		}

		//Restituzione stringa
		return error;
	}
    
    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.name = nome;
    }

    /**
     * @param  the gravita to gravita
     */
    public void setGravita(int severity) {
        this.severity = severity;
    }
    
	public String setGravita(String newSeverity) {

		String error="";

		//Caso stringa vuota
		if (newSeverity.isEmpty()) error+= pathology.getString("GRAVITÀ DELLA PATOLOGIA NON SPECIFICATA");
		else
		{
			this.severity = Integer.valueOf(newSeverity);
		}

		//Restituzione stringa
		return error;
	}
	
 
    @Override
    public String toString()
    {
    	String stringaPatologia = new String();
    	stringaPatologia += java.text.MessageFormat.format(pathology.getString("CODICE PATOLOGIA: {0}"), new Object[] {getCodice()});
    	stringaPatologia += java.text.MessageFormat.format(pathology.getString("- NOME: {0}"), new Object[] {getNome()});
    	stringaPatologia += java.text.MessageFormat.format(pathology.getString("- GRAVITÀ: {0}"), new Object[] {getGravita()});

    	return stringaPatologia;
    }
}
