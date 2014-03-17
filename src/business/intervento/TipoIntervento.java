package business.intervento;

import business.patologia.PatologiaTO;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class TipoIntervento{
    private static ResourceBundle interventionType = ResourceBundle.getBundle("adisys/server/property/TipoIntervento");
       
    private int ID;	
    private String name;
    private String observedValue;
    private String interventionDuration;
    private String notes;
    private ArrayList<PatologiaTO> pathologiesList;
	
        public static void setResourceBundle(String path, Locale local){
        	interventionType = ResourceBundle.getBundle(path, local);
        }
            
	public TipoIntervento()
	{
		name="";
		observedValue="";
		interventionDuration="";
		notes="";
		pathologiesList = new ArrayList<PatologiaTO>();
	}
	
        public void setID(int id){
            ID = id;
        }
        
        public int getID(){
            return ID;
        }
        
	public TipoIntervento(String newName, String newNotes, ArrayList<PatologiaTO> pathologiesArray)
	{
		name=newName;
		observedValue="";
		interventionDuration="";
		notes=newNotes;
		pathologiesList = pathologiesArray;
	}
	
                
	public String getNome() {
		return name;
	}
	
	public String getValoreRilevato() {
		return observedValue;
	}
	
	public String getTempoIntervento() {
		return interventionDuration;
	}
	
	public String getNote() {
		return notes;
	}
        
    public ArrayList<PatologiaTO> getListaPatologie(){
    	return pathologiesList;
    }
    
    public void setListaPatologie(ArrayList<PatologiaTO> pathologiesList){
        this.pathologiesList = pathologiesList;
    }
	
	public void setNome(String name) {
		this.name = name;
	}
	
	public void setValoreRilevato(String observedValue) {
		this.observedValue = observedValue;
	}
	
	public void setTempoIntervento(String interventionDuration) {
		this.interventionDuration = interventionDuration;
	}
	
	public void setNote(String note) {
		this.notes = note;
	}    
        
	@Override
	public String toString()
	{
		String typeString = interventionType.getString("TIPO INTERVENTO: ")+ getNome() + "\n";
                for(PatologiaTO e : pathologiesList){
                	typeString += java.text.MessageFormat.format(interventionType.getString("PATOLOGIA N. {0} -> {1}"), new Object[] {e.getCodice(), e.getNome()});
                    typeString += java.text.MessageFormat.format(interventionType.getString("GRAVITA DELLA PATOLOGIA: {0}"), new Object[] {e.getGravita()});
                }
                if(getValoreRilevato() != null)
                	typeString += interventionType.getString("VALORE RILEVATO: ")+ getValoreRilevato(); 
                if(getTempoIntervento() != null)
                	typeString += interventionType.getString("TEMPO INTERVENTO: ")+ getTempoIntervento();
                typeString += interventionType.getString("NOTE: ")+ getNote() + "\n";
		
		return typeString;
	}
}
