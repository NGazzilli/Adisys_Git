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
    private static ResourceBundle interventoCompleto = ResourceBundle.getBundle("adisys/server/property/InterventoCompleto");
    
        public static void setResourceBundle(String path, Locale locale){
            interventoCompleto = ResourceBundle.getBundle(path, locale);
        }
	//Enumerazioni per verifiche
	public enum StatoVerifica{nonVerificato, verificaOK, anomalia}
	
	//Oggetti membro
	private PazienteTO paziente;
	private InfermiereTO infermiere;

	private String misuraRilevata;
	private ArrayList<Rilevazione> log;
	private StatoVerifica statoVerificaGPS;
	private StatoVerifica statoVerificaAccelerometro;
	
	private String note;
	
	public InterventoCompletoTO() {

		log = new ArrayList<Rilevazione>();
		statoVerificaGPS=StatoVerifica.nonVerificato;
		statoVerificaAccelerometro=StatoVerifica.nonVerificato;
	}

	public PazienteTO getPaziente()
	{
		return paziente;
	}
	
	public InfermiereTO getInfermiere()
	{
		return infermiere;
	}

	public Rilevazione getLog(int indice)
	{
		if ( indice >= 0 && indice < contaLog())
			return log.get(indice);
		else
		{
			System.out.println(interventoCompleto.getString("INTERVENTOCOMPLETO -> TENTATIVO DI PRELIEVO DI UNITALOG FUORI DAI LIMITI DELLA LISTA."));
			return null;
		}
	}

	public String getMisuraRilevata() {
		return misuraRilevata;
	}

	public StatoVerifica getStatoVerificaGPS() {
		return statoVerificaGPS;
	}

	public StatoVerifica getStatoVerificaAccelerometro() {
		return statoVerificaAccelerometro;
	}

	public boolean addLog(Rilevazione u)
	{
		return log.add(u);
	}

	public int contaLog()
	{
		return log.size();
	}

	public boolean setPaziente(PazienteTO newPaziente)
	{
		paziente=newPaziente;
		return true;
	}

	public boolean setInfermiere(InfermiereTO newInfermiere)
	{
		infermiere=newInfermiere;
		return true;
	}

	public boolean setMisuraRilevata(String newMisuraRilevata)
	{
		misuraRilevata=newMisuraRilevata;
		return true;
	}

	public void setStatoVerificaGPS(StatoVerifica statoVerificaGPS) {
		this.statoVerificaGPS = statoVerificaGPS;
	}

	public void setStatoVerificaAccelerometro(StatoVerifica statoVerificaAccelerometro) {
		this.statoVerificaAccelerometro = statoVerificaAccelerometro;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString()
	{
		String stringaIntervento= java.text.MessageFormat.format(interventoCompleto.getString("INTERVENTO NUMERO {0}"), new Object[] {getID()});
		stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("INFERMIERE N.{0}"), new Object[] {getIDInfermiere()});
		stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("{0}"), new Object[] {getPaziente().toString()});
                
		stringaIntervento += interventoCompleto.getString("- DATA INTERVENTO: ") +getDataDaFormato(interventoCompleto.getString("DD/MM/YYYY"));
		stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("- ORA INIZIO INTERVENTO: {0}"), new Object[] {getOraInizioDaFormato("HH:mm")});
		//stringaIntervento += "\n- Ora fine intervento: " + getOraFineDaFormato("HH:mm");
		stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("- INDIRIZZO : {0}"), new Object[] {getCivico()});
		stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("- CITTÀ: {0} - CAP {1}"), new Object[] {getCitta(), getCap()});
		stringaIntervento += "\n";
                stringaIntervento += interventoCompleto.getString("TIPI DI INTERVENTO: ") + "\n\n";
		for(TipoIntervento t: tipiIntervento)
			stringaIntervento += t.toString() + "\n";
                if(getNote() != null)
                stringaIntervento += java.text.MessageFormat.format(interventoCompleto.getString("NOTE RILEVAZIONI: {0}"), new Object[] {getNote()});
		
		return stringaIntervento;
	}
}
