package business.intervento;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Interfaccia della entity {@link Intervento} offre il metodo di restituzione
 * di un singolo intervento.
*/
public interface I_InterventoSpecifico {
	/**
	 * Avendo in input un id_intervento deduce l'intervento dalla tabella intervento
	 * del database e ritorna tutti i dati incapsulati in InterventoTO
	 * Ritorna null se non c'e' un intervento associato a quell'id
	 * @param id_intervento l'id dell'intervento da cui dedurre l'intervento richiesto
	 * @return il to dell'intervento con id = {@code id_intervento}, null se all'id 
	 * specificato non corrisponde alcun intervento
	*/
	public InterventoTO getSpecificIntervento(int id_intervention);
}
