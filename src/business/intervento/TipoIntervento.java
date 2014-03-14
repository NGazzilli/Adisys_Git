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
    private static ResourceBundle tipoIntervento = ResourceBundle.getBundle("adisys/server/property/TipoIntervento");
       
    private int ID;	
    private String nome;
    private String valoreRilevato;
    private String tempoIntervento;
    private String note;
    private ArrayList<PatologiaTO> listaPatologie;
	
        public static void setResourceBundle(String path, Locale locale){
            tipoIntervento = ResourceBundle.getBundle(path, locale);
        }
            
	public TipoIntervento()
	{
		nome="";
		valoreRilevato="";
		tempoIntervento="";
		note="";
                listaPatologie = new ArrayList<PatologiaTO>();
	}
	
        public void setID(int id){
            ID = id;
        }
        
        public int getID(){
            return ID;
        }
        
	public TipoIntervento(String newNome, String newNote, ArrayList<PatologiaTO> arrayPatologie)
	{
		nome=newNome;
		valoreRilevato="";
		tempoIntervento="";
		note=newNote;
                listaPatologie = arrayPatologie;
	}
	
                
	public String getNome() {
		return nome;
	}
	
	public String getValoreRilevato() {
		return valoreRilevato;
	}
	
	public String getTempoIntervento() {
		return tempoIntervento;
	}
	
	public String getNote() {
		return note;
	}
        
    public ArrayList<PatologiaTO> getListaPatologie(){
    	return listaPatologie;
    }
    
    public void setListaPatologie(ArrayList<PatologiaTO> listaPatologie){
        this.listaPatologie = listaPatologie;
    }
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setValoreRilevato(String valoreRilevato) {
		this.valoreRilevato = valoreRilevato;
	}
	
	public void setTempoIntervento(String tempoIntervento) {
		this.tempoIntervento = tempoIntervento;
	}
	
	public void setNote(String note) {
		this.note = note;
	}    
        
	@Override
	public String toString()
	{
		String stringaTipo = tipoIntervento.getString("TIPO INTERVENTO: ")+ getNome() + "\n";
                for(PatologiaTO e : listaPatologie){
                    stringaTipo += java.text.MessageFormat.format(tipoIntervento.getString("PATOLOGIA N. {0} -> {1}"), new Object[] {e.getCodice(), e.getNome()});
                    stringaTipo += java.text.MessageFormat.format(tipoIntervento.getString("GRAVITA DELLA PATOLOGIA: {0}"), new Object[] {e.getGravita()});
                }
                if(getValoreRilevato() != null)
                    stringaTipo += tipoIntervento.getString("VALORE RILEVATO: ")+ getValoreRilevato(); 
                if(getTempoIntervento() != null)
                    stringaTipo += tipoIntervento.getString("TEMPO INTERVENTO: ")+ getTempoIntervento();
                stringaTipo += tipoIntervento.getString("NOTE: ")+ getNote() + "\n";
		
		return stringaTipo;
	}
}
