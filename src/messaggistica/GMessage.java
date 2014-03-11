package messaggistica;

//import java.util.HashMap;
import javax.swing.JOptionPane;

/**
* Messaggistica di alto livello (GUI), con i suoi metodi mostra all'utente un messaggio 
* riassuntivo standard a seconda dell'evento generato (errore, informazioni, conferme).
*/
public class GMessage {
	
	/*private static final String GENERALE = "E' stato lanciato un errore generale.\n" +
						"Eseguire il programma da terminale per maggiori informazioni";
	
	private static final String FC = "Il front controller ha lanciato un eccezione.\n" +
						"Eseguire il programma da terminale per maggiori informazioni";
	
	
	private static final String CREAZ_DB = "Impossibile creare il database";
	private static final String DB = "La creazione del database ha generato qualche errore.\n" +
						"Eseguire il programma da terminale per maggiori informazioni";
	
	
	private static final String DATI_CONFIG = "Dati incompleti o inesatti, assicurarsi che:\n" +
							" - password ADI Sys e ripeti password ADI Sys siano uguali,\n" +
							" - distanza e inattivita' siano numeri\n" +
							" - percorso db, username db e password db non siano vuoti\ne reinviare.";
	private static final String CONFIG = "La configurazione ha avuto esito negativo. Riprovare";
	
	
	private static final String IMPORTA = "Il sistema non e' in grado di importare la lista.\n" +
								"Controllare che il file non sia corrotto e\n" +
								" sia stato scritto secondo la sintassi " +
								"comprensibile dal sistema.";
	
	
	private static final String POPOLA_TABELLA = "La popolazione della tabella e' fallita.";
	
	
	private static final String CANC_INTERVENTO = "La cancellazione dell'intervento e' fallita\n" +
									"Eseguire il programma da terminale per maggiori informazioni";
      
      private static final String CANC_INFERMIERE = "La cancellazione dell'infermiere e' fallita\n" +
									"Eseguire il programma da terminale per maggiori informazioni";
      
      private static final String CANC_PAZIENTE = "La cancellazione del paziente e' fallita\n" +
									"Eseguire il programma da terminale per maggiori informazioni";
      
      private static final String CANC_PATOLOGIA = "La cancellazione della patologia e' fallita\n" +
									"Eseguire il programma da terminale per maggiori informazioni";
          
      private static final String CANC_TUTTI = "La cancellazione non è andata a buon fine a casa di un errore nel database";
      
	private static final String CONVERSIONE = "E' stato lanciato un errore di conversione di data\n" +
							"Eseguire il programma da terminale per maggiori informazioni";
	
	
	private static final String DATI_INCORRETTI = "I dati inseriti sono incorretti, controllare" +
							"la correttezza di ciascun campo";
	
	
	private static final String SINCRO_DB = 
		"Errore durante la sincronizzazione, il sistema non e' stato in grado\n" +
		"di copiare i dati del journaling nel database.\nProbabilmente il file " +
		"di journaling e' corrotto o ha una sintassi non valida.\n" +
		"Ricordarsi che il file di journaling deve riferirsi a degli interventi\n" +
		"che sono stati precedentemente pianificati all'infermiere proprietario del file.\n" +
		"Controllare inoltre la connessione ad internet per poter accedere al servizio\n" +
		"Geocoding di Google.";
	
	
	private static final String CREAZIONE_DIRECTORY = "La creazione di una directory e' fallita, \n" +
			"eseguire il sistema da terminale e controllare le cartelle del sistema.";
	

	/**Mostra un messaggio di errore collegato alla parola chiave in input ({@code reason})*/
	/*public static void error(String reason){
		initializeMap();
		String message = map.get(reason.toLowerCase());
		
		adisys.server.strumenti.Sound.riproduciErrorSound();
		
		JOptionPane.showMessageDialog(null, message, "ERRORE", JOptionPane.ERROR_MESSAGE);
	}*/
      
	public static int confirm(String message){	
          return JOptionPane.showConfirmDialog(null, message, "ATTENZIONE", JOptionPane.YES_NO_OPTION);
	}
      
	public static void message_error(String message){
         
          JOptionPane.showMessageDialog(null, message, "ERRORE", JOptionPane.ERROR_MESSAGE);
	}
      
      public static void information(String message){
          JOptionPane.showMessageDialog(null, message, "INFORMAZIONI", JOptionPane.INFORMATION_MESSAGE);
      }
      
      public static int yes_no_cancel_message(String message){
          return JOptionPane.showConfirmDialog(null, message, "ATTENZIONE", JOptionPane.YES_NO_CANCEL_OPTION);
      }
      
	/**Mappa che associa il nome dell'errore con il messaggio da visualizzare*/
	/*protected static HashMap<String, String> map = new HashMap<String, String>();
	private static void initializeMap(){
		map.put("creazionedb", CREAZ_DB);
		map.put("db", DB);
		map.put("frontcontroller", FC);
		map.put("generale", GENERALE);
		map.put("daticonfig", DATI_CONFIG);
		map.put("config", CONFIG);
		map.put("importa", IMPORTA);
		map.put("popolazione", POPOLA_TABELLA);
		map.put("cancintervento", CANC_INTERVENTO);
              map.put("cancpatologia", CANC_PATOLOGIA);
              map.put("cancinfermiere", CANC_INFERMIERE);
              map.put("cancpaziente", CANC_PAZIENTE);
              map.put("canctutti", CANC_TUTTI);
		map.put("conversione", CONVERSIONE);
		map.put("datiincorretti", DATI_INCORRETTI);
		map.put("sincrojourdb", SINCRO_DB);
		map.put("creazionedir", CREAZIONE_DIRECTORY);
	}*/
	
	private static final String NOT_FOUND = "La finestra <win> non e' stata trovata.";
	/**
	 * Da chiamare quando una finestra non viene trovata
	*/
	public static void winNotFound(String window){
		String message = NOT_FOUND.replaceAll("<win>", window);
		
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
              
	}
	
}
