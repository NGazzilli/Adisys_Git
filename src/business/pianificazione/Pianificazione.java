/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.pianificazione;

import adisys.server.strumenti.DateFormatConverter;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoTO;
import business.paziente.PazienteTO;
import messaggistica.MainException;
import integration.dao.PazienteMySqlDAO;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 */
public class Pianificazione {
    
   	private static final String FORMATO_NOME_FILE = "codoper_data_device.xml"; //Modificato vers. 2
	private static final String FILE_SCHEMA_XSD_ESP = "SchemaXSD/XSDEsp.xsd";
	private static final String SEGNAPOSTO_CODICE_OPERATORE = "codoper";
	private static final String SEGNAPOSTO_DATA = "data";
	private static final String SEGNAPOSTO_DEVICE = "device";
        
        /*public static String NOME_TABELLA_ATTIVITA = "ATTIVITA";
        public static String NOME_COLONNA_ID_INTERVENTO_ATTIVITA = "IDINTERVENTO";
        public static String NOME_COLONNA_DATA_ORA_INIZIO_ATTIVITA = "DATAORAINIZIO";
        public static String NOME_COLONNA_DATA_ORA_FINE_ATTIVITA = "DATAORAFINE";
        public static String NOME_COLONNA_MISURA_RILEVATA_ATTIVITA = "MISURARILEVATA";
        public static String NOME_COLONNA_LOG_FILE_ATTIVITA = "LOGFILE";*/
	
	private static final String DEVICE = "s"; //Modificato vers. 2
	private static final String CARTELLA_ESP_FILE = "Esportazione";
	private static final String FORMATO_DATA = "yyyy-MM-dd";
	/**
	 * Costruttore, recupera la {@link integration.dao.MySqlDAOFactory}
	/**
	 * Imposta l'id_infermiere ed esegue la createDAO sulla DAOFactory per 
	 * ottenere il dao appropriato. Il dao ottenuto dedurr&agrave da solo attraverso
	 * {@link GetSetIDInfermiereToProcess} quale infermiere processare
	 * @throws MainException se non trova la dao
	*/
	public Pianificazione() throws MainException{

        }
	
    /**
	 * Esportazione della pianificazione in un file. Usa {@link PianificazioneFile}
	 * @param infTO il to dell'infermiere a cui appartiene la pianificazione
	 * @param intTOs una struttura dati degli interventi pianificati
	 * @return il nome del file appena creato, null se non riesce a creare nulla
	*/
	public String esportaPianificazione(InfermiereTO infTO, 
			ArrayList<InterventoTO> listaInterventi) throws MainException{
            		
				//ESPORTAZIONE DELLA PIANIFICAZIONE DEGLI INTERVENTI
            //Creazione struttura di interscambio
            StrutturaInterscambio s = new StrutturaInterscambio();
			
            //Per ogni intervento aggiunge i dati del paziente alla lista
            for(InterventoTO i:listaInterventi) {
                //Recupero dati paziente
                PazienteTO p = null;
                try {
                    p = (PazienteTO) new PazienteMySqlDAO().getSpecified(i.getIDPaziente());
                } catch (MainException ex) {
                    Logger.getLogger(Pianificazione.class.getName()).log(Level.SEVERE, null, ex);
                }
                s.addIntervento(i, p);
            }
		
            //Preparazione percorso e nome file
            String nomeFile = FORMATO_NOME_FILE;
            nomeFile = nomeFile.replace(SEGNAPOSTO_CODICE_OPERATORE, StrutturaInterscambio.int2string6(infTO.getID()));
            nomeFile = nomeFile.replace(SEGNAPOSTO_DATA, DateFormatConverter.long2dateString(DateFormatConverter.oggi(), FORMATO_DATA));
            nomeFile = nomeFile.replace(SEGNAPOSTO_DEVICE, DEVICE);
			
            //Trace
            System.out.println("\nPianificazione -> Preparato nome del file : " + nomeFile);
			
            //Esportazione del file e restituzione di una stringa con il risultato di scrittura e validazione 
            return s.salvaSuFileXML(CARTELLA_ESP_FILE, nomeFile, FILE_SCHEMA_XSD_ESP);
	}
	
}
