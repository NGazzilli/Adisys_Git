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
    
   	private static final String FILE_NAME_FORMAT = "codoper_data_device.xml"; //Modificato vers. 2
	private static final String XSD_ESP_FILE_SCHEME = "SchemaXSD/XSDEsp.xsd";
	private static final String OPERATOR_CODE_LABEL = "codoper";
	private static final String DATE_LABEL = "data";
	private static final String DEVICE_LABEL = "device";
	
	private static final String DEVICE = "s"; //Modificato vers. 2
	private static final String ESP_FILE_FOLDER = "Esportazione";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
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
			ArrayList<InterventoTO> interventionList) throws MainException{
            		
				//ESPORTAZIONE DELLA PIANIFICAZIONE DEGLI INTERVENTI
            //Creazione struttura di interscambio
            StrutturaInterscambio s = new StrutturaInterscambio();
			
            //Per ogni intervento aggiunge i dati del paziente alla lista
            for(InterventoTO i:interventionList) {
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
            String fileName = FILE_NAME_FORMAT;
            fileName = fileName.replace(OPERATOR_CODE_LABEL, StrutturaInterscambio.int2string6(infTO.getID()));
            fileName = fileName.replace(DATE_LABEL, DateFormatConverter.long2dateString(DateFormatConverter.oggi(), DATE_FORMAT));
            fileName = fileName.replace(DEVICE_LABEL, DEVICE);
			
            //Trace
            System.out.println("\nPianificazione -> Preparato nome del file : " + fileName);
			
            //Esportazione del file e restituzione di una stringa con il risultato di scrittura e validazione 
            return s.salvaSuFileXML(ESP_FILE_FOLDER, fileName, XSD_ESP_FILE_SCHEME);
	}
	
}
