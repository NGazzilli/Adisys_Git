package business.infermiere;

import business.TO;
import java.util.Locale;
import java.util.ResourceBundle;



/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Transfer Object dell'entita' infermiere. Incapsula tutti i dati dell'infermiere.
 * Funzionalit&agrave coincidenti con il pattern Transfer Object
*/
public class InfermiereTO implements TO{
    
        private static ResourceBundle nurse = ResourceBundle.getBundle("adisys/server/property/Infermiere");
    
        public static void setResourceBundle(String path, Locale local){
        	nurse = ResourceBundle.getBundle(path, local);
        }
        
	private int id;
	private String name;
	private String surname;
	
	public InfermiereTO(){}
	
	/**Costruttore per riferirsi ad un infermiere gi&agrave esistente nel database*/
	public InfermiereTO(int id){
		this.id = id;
	}
	
	public InfermiereTO(int id, String name, String surname){
		this.id = id;
		this.name = name;
		this.surname = surname;
	}
	
	public InfermiereTO(String name, String surname){
		this.name = name;
		this.surname = surname;
	}
	
	@Override
        public String toString(){
            String nurseString= new String();
            nurseString += java.text.MessageFormat.format(nurse.getString("ID INFERMIERE: {0}"), new Object[] {getID()});
            nurseString += java.text.MessageFormat.format(nurse.getString("- NOME: {0}"), new Object[] {getNome()});
            nurseString += java.text.MessageFormat.format(nurse.getString("- COGNOME: {0}"), new Object[] {getCognome()});

            return nurseString;
        }
	public int getID() {
		return id;
	}
	public void setID(int id_nurse) {
		this.id = id_nurse;
	}


	
	public String getNome() {
		return name;
	}
	public void setNome(String name){
		this.name = name;
	}

	

	public String getCognome() {
		return surname;
	}
	
	public void setCognome(String surname){
		this.surname = surname;
	}

}
