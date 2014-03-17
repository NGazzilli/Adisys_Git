package business.intervento;

import business.infermiere.InfermiereTO;
import business.paziente.PazienteTO;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class InterventoCompletoTO extends InterventoTO {
    private static ResourceBundle completeIntervention = ResourceBundle.getBundle("adisys/server/property/InterventoCompleto");
    
        public static void setResourceBundle(String path, Locale local){
        	completeIntervention = ResourceBundle.getBundle(path, local);
        }
	//Enumerazioni per verifiche
	public enum StatoVerifica{notVerified, checkOK, anomaly}
	
	//Oggetti membro
	private PazienteTO patient;
	private InfermiereTO nurse;

	private String observedMeasure;
	private ArrayList<Rilevazione> log;
	private StatoVerifica checkStatusGPS;
	private StatoVerifica checkStatusAccelerometer;
	
	private String notes;
	
	public InterventoCompletoTO() {

		log = new ArrayList<Rilevazione>();
		checkStatusGPS=StatoVerifica.notVerified;
		checkStatusAccelerometer=StatoVerifica.notVerified;
	}

	public PazienteTO getPaziente()
	{
		return patient;
	}
	
	public InfermiereTO getInfermiere()
	{
		return nurse;
	}

	public Rilevazione getLog(int index)
	{
		if ( index >= 0 && index < contaLog())
			return log.get(index);
		else
		{
			System.out.println(completeIntervention.getString("INTERVENTOCOMPLETO -> TENTATIVO DI PRELIEVO DI UNITALOG FUORI DAI LIMITI DELLA LISTA."));
			return null;
		}
	}

	public String getMisuraRilevata() {
		return observedMeasure;
	}

	public StatoVerifica getStatoVerificaGPS() {
		return checkStatusGPS;
	}

	public StatoVerifica getStatoVerificaAccelerometro() {
		return checkStatusAccelerometer;
	}

	public boolean addLog(Rilevazione u)
	{
		return log.add(u);
	}

	public int contaLog()
	{
		return log.size();
	}

	public boolean setPaziente(PazienteTO newPatient)
	{
		patient=newPatient;
		return true;
	}

	public boolean setInfermiere(InfermiereTO newNurse)
	{
		nurse=newNurse;
		return true;
	}

	public boolean setMisuraRilevata(String newObservedMeasure)
	{
		observedMeasure=newObservedMeasure;
		return true;
	}

	public void setStatoVerificaGPS(StatoVerifica checkStatusGPS) {
		this.checkStatusGPS = checkStatusGPS;
	}

	public void setStatoVerificaAccelerometro(StatoVerifica checkStatusAccelerometer) {
		this.checkStatusAccelerometer = checkStatusAccelerometer;
	}

	public String getNote() {
		return notes;
	}

	public void setNote(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString()
	{
		String interventionString= java.text.MessageFormat.format(completeIntervention.getString("INTERVENTO NUMERO {0}"), new Object[] {getID()});
		interventionString += java.text.MessageFormat.format(completeIntervention.getString("INFERMIERE N.{0}"), new Object[] {getIDInfermiere()});
		interventionString += java.text.MessageFormat.format(completeIntervention.getString("{0}"), new Object[] {getPaziente().toString()});
                
		interventionString += completeIntervention.getString("- DATA INTERVENTO: ") +getDataDaFormato(completeIntervention.getString("DD/MM/YYYY"));
		interventionString += java.text.MessageFormat.format(completeIntervention.getString("- ORA INIZIO INTERVENTO: {0}"), new Object[] {getOraInizioDaFormato("HH:mm")});
		//stringaIntervento += "\n- Ora fine intervento: " + getOraFineDaFormato("HH:mm");
		interventionString += java.text.MessageFormat.format(completeIntervention.getString("- INDIRIZZO : {0}"), new Object[] {getCivico()});
		interventionString += java.text.MessageFormat.format(completeIntervention.getString("- CITTÀ: {0} - CAP {1}"), new Object[] {getCitta(), getCap()});
		interventionString += "\n";
		interventionString += completeIntervention.getString("TIPI DI INTERVENTO: ") + "\n\n";
		for(TipoIntervento t: interventionTypes)
			interventionString += t.toString() + "\n";
                if(getNote() != null)
                	interventionString += java.text.MessageFormat.format(completeIntervention.getString("NOTE RILEVAZIONI: {0}"), new Object[] {getNote()});
		
		return interventionString;
	}
}
