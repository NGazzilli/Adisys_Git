package business.infermiere;

import business.TO;
import java.util.Locale;
import java.util.ResourceBundle;



/**
 * Transfer Object dell'entita' infermiere. Incapsula tutti i dati dell'infermiere.
 * Funzionalit&agrave coincidenti con il pattern Transfer Object
*/
public class InfermiereTO implements TO{
    
        private static ResourceBundle infermiere = ResourceBundle.getBundle("adisys/server/property/Infermiere");
    
        public static void setResourceBundle(String path, Locale locale){
            infermiere = ResourceBundle.getBundle(path, locale);
        }
        
	private int id;
	private String nome;
	private String cognome;
	
	public InfermiereTO(){}
	
	/**Costruttore per riferirsi ad un infermiere gi&agrave esistente nel database*/
	public InfermiereTO(int id){
		this.id = id;
	}
	
	public InfermiereTO(int id, String nome, String cognome){
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	public InfermiereTO(String nome, String cognome){
		this.nome = nome;
		this.cognome = cognome;
	}
	
	@Override
        public String toString(){
            String stringaInfermiere= new String();
            stringaInfermiere += java.text.MessageFormat.format(infermiere.getString("ID INFERMIERE: {0}"), new Object[] {getID()});
            stringaInfermiere += java.text.MessageFormat.format(infermiere.getString("- NOME: {0}"), new Object[] {getNome()});
            stringaInfermiere += java.text.MessageFormat.format(infermiere.getString("- COGNOME: {0}"), new Object[] {getCognome()});

            return stringaInfermiere;
        }
	public int getID() {
		return id;
	}
	public void setID(int id_infermiere) {
		this.id = id_infermiere;
	}


	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome){
		this.nome = nome;
	}

	

	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome){
		this.cognome = cognome;
	}

}
