package business.patologia;

import adisys.server.strumenti.ADISysTableModel;
import business.TO;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Transfer Object dell'entita' infermiere. Incapsula tutti i dati dell'infermiere.
 * Funzionalit&agrave coincidenti con il pattern Transfer Object
*/
public class PatologiaTO implements TO{
    
    private int id;
    private String codice;
    private String nome;
    private int gravita = 0;

    private static ResourceBundle patologia = ResourceBundle.getBundle("adisys/server/property/Patologia");
    
    public static void setResourceBundle(String path, Locale locale){
        patologia = ResourceBundle.getBundle(path, locale);
    }
        
    public PatologiaTO(String codice, String nome){
    	this.codice = codice;
    	this.nome = nome;
    }
    
    public PatologiaTO(int id, String codice, String nome){
    	this.id = id;
        this.codice = codice;
    	this.nome = nome;
    }
    
      public PatologiaTO(String codice, String nome, int gravita){
    	this.codice = codice;
    	this.nome = nome;
    	this.gravita = gravita;
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
        return codice;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the gravita
     */
    public int getGravita() {
        return gravita;
    }

    public String setCodice(String codice) {

		String errore="";

		//Caso stringa vuota
		if (codice.isEmpty()) errore+= patologia.getString("CODICE PATOLOGIA NON SPECIFICATO");
		else
		{
			this.codice = codice;
		}

		//Restituzione stringa
		return errore;
	}
    
    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param  the gravita to gravita
     */
    public void setGravita(int gravita) {
        this.gravita = gravita;
    }
    
	public String setGravita(String newGravita) {

		String errore="";

		//Caso stringa vuota
		if (newGravita.isEmpty()) errore+= patologia.getString("GRAVITÀ DELLA PATOLOGIA NON SPECIFICATA");
		else
		{
			this.gravita = Integer.valueOf(newGravita);
		}

		//Restituzione stringa
		return errore;
	}
	
 
    @Override
    public String toString()
    {
    	String stringaPatologia = new String();
    	stringaPatologia += java.text.MessageFormat.format(patologia.getString("CODICE PATOLOGIA: {0}"), new Object[] {getCodice()});
    	stringaPatologia += java.text.MessageFormat.format(patologia.getString("- NOME: {0}"), new Object[] {getNome()});
    	stringaPatologia += java.text.MessageFormat.format(patologia.getString("- GRAVITÀ: {0}"), new Object[] {getGravita()});

    	return stringaPatologia;
    }
}
