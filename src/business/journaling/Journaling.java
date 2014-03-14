/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.journaling;

import adisys.server.strumenti.DateFormatConverter;
import adisys.server.strumenti.Record;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoCompletoTO;
import business.intervento.Rilevazione;
import business.paziente.PazienteTO;
import business.pianificazione.StrutturaInterscambio;
import messaggistica.MainException;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import messaggistica.GMessage;
import presentation.FrontController;
import presentation.RequestManager;

/**
 *
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 */
public class Journaling implements I_Journaling {
        
        private static final double DISTANZA_MAX_DOMICILIO= 30; //metri
        private static final long TEMPO_MAX_ACCEL_NULLO = 1800000; //millisecondi (30 min x 60 sec/min * 1000 ms/sec)
	
        //Parametri di importazione del file
        private static final String CARTELLA_JOURNALING="Importazione";
        private static final String ESTENSIONE_JOURNALING = ".xml";
        private static final String SCHEMA_XSD_IMP = "SchemaXSD/XSDImp.xsd";
        
        private static ResourceBundle journaling = ResourceBundle.getBundle("adisys/server/property/Journaling");
	
        public static void setResourceBundle(String path, Locale locale){
            journaling = ResourceBundle.getBundle(path, locale);
        }
        
        private FrontController FC;
	
        //Lista degli interventi per l'analisi dei dati
        private static ArrayList<InterventoCompletoTO> listaInterventi;
	
	public Journaling() throws MainException{

	}
        
        public String[] getListaJournaling(){
            	File cartellaJournaling = new File(CARTELLA_JOURNALING);
		
		//Controllo esistenza percorso
		if(cartellaJournaling.exists())
		{
			//Cartella esistente. 
			//Creazione di un filtro
			FilenameFilter filtro= new FilenameFilter(){
				@Override
				public boolean accept(File cartella, String nomeFile) {
					if (nomeFile.endsWith(ESTENSIONE_JOURNALING)) return true;
					else return false;
				}
				
			};

			//Estrazione lista file
			return cartellaJournaling.list(filtro);
		}
		else
		{
			//Cartlla inesistente
			System.out.println("Verifica -> Impossibile individuare la cartella");
			return null;
		}
        }
        
        public InterventoCompletoTO getIntervento(int idIntervento){
            return listaInterventi.get(idIntervento);
        }
        
        public ArrayList<InterventoCompletoTO> getListaInterventi(){
            return listaInterventi;
        }
        
        public String caricaFile(String nomeFile){
            String percorsoJournaling= CARTELLA_JOURNALING + "/" + nomeFile;
        
            String errLog= journaling.getString("CARICAMENTO JOURNALING:");
        
            //CARICAMENTO FILE
            StrutturaInterscambio s = new StrutturaInterscambio();
            errLog += "\n" + s.caricaDaFileXML(percorsoJournaling, SCHEMA_XSD_IMP);
        
            //Aggiunta interventi (completi) alla lista
            listaInterventi = s.getInterventiCompleti();
            if (listaInterventi.isEmpty())
        	errLog += journaling.getString("- ERRORE LETTURA FILE DI JOURNALING");
            else {
                FC = RequestManager.getFCInstance();
                int idInfermiere = listaInterventi.get(0).getIDInfermiere();
                InfermiereTO infTO = new InfermiereTO();
                infTO.setID(idInfermiere);
                boolean trovato = false;
                ArrayList<Record<String, Object>> params = new ArrayList<Record<String, Object>>();
                params.add(new Record<String, Object>("business.infermiere.InfermiereTO", infTO));      
                try {
                    trovato = (boolean) FC.processRequest("infermiereEsistente", params);
                } catch (MainException ex) {
                    Logger.getLogger(Journaling.class.getName()).log(Level.SEVERE, null, ex);
                }
                String nomi[] = new String[listaInterventi.size()];
                if(trovato == true){
                    boolean pazTrovato = false;
                    for(int i = 0; i < listaInterventi.size(); i++){
                        FC = RequestManager.getFCInstance();
                        //int idPaziente = listaInterventi.get(i).getIDPaziente();
                        PazienteTO pazTO = listaInterventi.get(i).getPaziente();
                        params.clear();
                        params.add(new Record<String, Object>("business.paziente.PazienteTO", pazTO));      
                        try {
                            pazTrovato = (boolean) FC.processRequest("pazienteEsistente", params);
                            if(pazTrovato == false){
                                nomi[i] = listaInterventi.get(i).getPaziente().getNome();
                            }
                                
                        } catch (MainException ex) {
                            Logger.getLogger(Journaling.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    }
                    
                    if(pazTrovato == false){ //if su paziente assente
                        String message = journaling.getString("PAZIENTE ASSENTE");
                        for(int i = 0; i < nomi.length; i++){
                            if(nomi[i] != null)
                                message = message + "\n - " + nomi[i];
                        }
                        message = message + "\n" + journaling.getString("NON RISULTA REGISTRATO");
                            if(GMessage.confirm(message) == JOptionPane.NO_OPTION)
                               return journaling.getString("CARICAMENTO ANNULLATO");
                            else {   
                                //Verifica GPS
                                verificaGPS();
        
                                //Verifica Accelerometro
                                verificaAccelerometro();
                            }
                    }    
                } else { //else su infermiere assente
                    errLog = journaling.getString("INFERMIERE ASSENTE");
                }
            }
            return errLog;
        }
        private void verificaGPS() 
	{
		for (InterventoCompletoTO i:listaInterventi)
		{
			/*Ciclo che legge tutti i log dopo il primo e li
			 *confronta con il primo per la verifica del GPS
			 *E' inutile considerare il primo log (indiceLog=0) perche' al fine della verifica
			 *non contiene dati rilevanti.*/
			for (int indiceLog = 1; indiceLog < i.contaLog(); indiceLog++)
			{
				//Allarme se:
				//GPS oltre 30m dal domicilio del paziente

				//Lettura prima rilevazione
				Rilevazione primoLog=i.getLog(0);

				//Lettura rilevazione attuale
				Rilevazione unitaLog=i.getLog(indiceLog);

				//Calcolo della distanza
				//Punto 0
				double Lat0 = primoLog.getGpsLatitude();
				double Lon0 = primoLog.getGpsLongitude();
				double Alt0 = primoLog.getGpsAltitude();
				//TODO Dato disponibile per il calcolo dell'errore (decommentare)
				//double Acc0 = primoLog.getGpsAccuracy(); 
				
				//Punto n
				double LatN = unitaLog.getGpsLatitude();
				double LonN = unitaLog.getGpsLongitude();
				double AltN = unitaLog.getGpsAltitude();
				//TODO Dato disponibile per il calcolo dell'errore (decommentare)
				//double AccN = unitaLog.getGpsAccuracy();

				//Calcolo distanza
				double distanza;
				distanza=Math.sqrt( Math.pow((LatN-Lat0), 2) * Math.pow((LonN-Lon0), 2) * Math.pow((AltN-Alt0), 2) );

				//Verifica 
				if(distanza>DISTANZA_MAX_DOMICILIO) 
                                {
                                    i.setStatoVerificaGPS(InterventoCompletoTO.StatoVerifica.anomalia);
                                    //Trace
                                    String msg="Verifica -> Rilevata anomalia GPS:";
                                    msg += " distanza di " + distanza;
                                    msg += " metri (max consentito " + DISTANZA_MAX_DOMICILIO + " metri)";
                                    
                                    System.out.println(msg);
                                                         
                                }
			}
						
			//Se al termine del ciclo la verifica non ha dato anomalie si imposta OK
			if(i.getStatoVerificaGPS()==InterventoCompletoTO.StatoVerifica.nonVerificato)
				i.setStatoVerificaGPS(InterventoCompletoTO.StatoVerifica.verificaOK);
			
		}
	}
	
	private void verificaAccelerometro()
	{
		for (InterventoCompletoTO i:listaInterventi)
		{
			/*Ciclo che legge tutti i log */
			
			//Dichiarazione cursori per timestamp
			Long primo=null;
			Long ultimo=null;
		
			for (int indiceLog = 0; indiceLog < i.contaLog(); indiceLog++)
			{
				//log attuale
				Rilevazione logCorrente = i.getLog(indiceLog);
				
				//Verifica accelerometro fermo
				boolean accelerometroFermo = (logCorrente.getAccX() == 0) && (logCorrente.getAccY()==0) && (logCorrente.getAccZ()==0);
				
				if(accelerometroFermo)
				{
					
					if (primo==null) 
					{
						/*Se l'accelerometro e' fermo per la prima volta
						 * viene settato il primo cursore per il calcolo 
						 * del tempo trascorso*/
						primo = logCorrente.getTimestamp().getTime();
					}
					else
					{
						/*Se l'accelerometro era gia' fermo durante l'ultima rilevazione
						 *viene settata la rilevazione attuale come secondo valore per
						 *il calcolo del tempo trascorso e se questo e' superiore alla
						 *soglia viene impostato l'alert*/
						ultimo=logCorrente.getTimestamp().getTime();
						
						if (ultimo-primo > TEMPO_MAX_ACCEL_NULLO )
                                                {
							i.setStatoVerificaAccelerometro(InterventoCompletoTO.StatoVerifica.anomalia);
                                                        //Trace
                                                        String msg="Verifica -> Rilevata anomalia accelerometro:";
                                                        msg += " nullo dalle " + DateFormatConverter.long2dateString(primo,
                                                                DateFormatConverter.getFormatOra());
                                                        msg += " alle " + DateFormatConverter.long2dateString(ultimo,
                                                                DateFormatConverter.getFormatOra());
                                                        
                                                        System.out.println(msg);
                                                }
					}
				}
				else
				{
					//L'accelerometro non e' fermo, vengono resettati i cursori
					primo=null;
					ultimo=null;
				}


			}
			//Se al termine del ciclo la verifica non ha dato anomalie si imposta OK
			if(i.getStatoVerificaAccelerometro()==InterventoCompletoTO.StatoVerifica.nonVerificato)
				i.setStatoVerificaAccelerometro(InterventoCompletoTO.StatoVerifica.verificaOK);
		}
	}
}
