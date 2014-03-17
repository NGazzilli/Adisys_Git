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
        
        private static final double MAX_DISTANCE= 30; //metri
        private static final long MAX_NULL_ACCESS_TIME = 1800000; //millisecondi (30 min x 60 sec/min * 1000 ms/sec)
	
        //Parametri di importazione del file
        private static final String JOURNALING_FOLDER="Importazione";
        private static final String JOURNALING_EXTENSION = ".xml";
        private static final String XSD_IMP_SCHEME = "SchemaXSD/XSDImp.xsd";
        
        private static ResourceBundle journaling = ResourceBundle.getBundle("adisys/server/property/Journaling");
	
        public static void setResourceBundle(String path, Locale local){
            journaling = ResourceBundle.getBundle(path, local);
        }
        
        private FrontController frontController;
	
        //Lista degli interventi per l'analisi dei dati
        private static ArrayList<InterventoCompletoTO> listaInterventi;
	
	public Journaling() throws MainException{

	}
        
        public String[] getListaJournaling(){
            	File journalingFolder = new File(JOURNALING_FOLDER);
		
		//Controllo esistenza percorso
		if(journalingFolder.exists())
		{
			//Cartella esistente. 
			//Creazione di un filtro
			FilenameFilter filter= new FilenameFilter(){
				@Override
				public boolean accept(File cartella, String fileName) {
					if (fileName.endsWith(JOURNALING_EXTENSION)) return true;
					else return false;
				}
				
			};

			//Estrazione lista file
			return journalingFolder.list(filter);
		}
		else
		{
			//Cartlla inesistente
			System.out.println("Verifica -> Impossibile individuare la cartella");
			return null;
		}
        }
        
        public InterventoCompletoTO getIntervento(int idIntervention){
            return listaInterventi.get(idIntervention);
        }
        
        public ArrayList<InterventoCompletoTO> getListaInterventi(){
            return listaInterventi;
        }
        
        public String caricaFile(String fileName){
            String journalingPath= JOURNALING_FOLDER + "/" + fileName;
        
            String errLog= journaling.getString("CARICAMENTO JOURNALING:");
        
            //CARICAMENTO FILE
            StrutturaInterscambio s = new StrutturaInterscambio();
            errLog += "\n" + s.caricaDaFileXML(journalingPath, XSD_IMP_SCHEME);
        
            //Aggiunta interventi (completi) alla lista
            listaInterventi = s.getInterventiCompleti();
            if (listaInterventi.isEmpty())
        	errLog += journaling.getString("- ERRORE LETTURA FILE DI JOURNALING");
            else {
            	frontController = RequestManager.getFCInstance();
                int idNurse = listaInterventi.get(0).getIDInfermiere();
                InfermiereTO infTO = new InfermiereTO();
                infTO.setID(idNurse);
                boolean found = false;
                ArrayList<Record<String, Object>> params = new ArrayList<Record<String, Object>>();
                params.add(new Record<String, Object>("business.infermiere.InfermiereTO", infTO));      
                try {
                	found = (boolean) frontController.processRequest("infermiereEsistente", params);
                } catch (MainException ex) {
                    Logger.getLogger(Journaling.class.getName()).log(Level.SEVERE, null, ex);
                }
                String names[] = new String[listaInterventi.size()];
                if(found == true){
                    boolean foundPatient = false;
                    for(int i = 0; i < listaInterventi.size(); i++){
                    	frontController = RequestManager.getFCInstance();
                        //int idPaziente = listaInterventi.get(i).getIDPaziente();
                        PazienteTO pazTO = listaInterventi.get(i).getPaziente();
                        params.clear();
                        params.add(new Record<String, Object>("business.paziente.PazienteTO", pazTO));      
                        try {
                        	foundPatient = (boolean) frontController.processRequest("pazienteEsistente", params);
                            if(foundPatient == false){
                            	names[i] = listaInterventi.get(i).getPaziente().getNome();
                            }
                                
                        } catch (MainException ex) {
                            Logger.getLogger(Journaling.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    }
                    
                    if(foundPatient == false){ //if su paziente assente
                        String message = journaling.getString("PAZIENTE ASSENTE");
                        for(int i = 0; i < names.length; i++){
                            if(names[i] != null)
                                message = message + "\n - " + names[i];
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
				double distance;
				distance=Math.sqrt( Math.pow((LatN-Lat0), 2) * Math.pow((LonN-Lon0), 2) * Math.pow((AltN-Alt0), 2) );

				//Verifica 
				if(distance>MAX_DISTANCE) 
                                {
                                    i.setStatoVerificaGPS(InterventoCompletoTO.StatoVerifica.anomaly);
                                    //Trace
                                    String msg="Verifica -> Rilevata anomalia GPS:";
                                    msg += " distanza di " + distance;
                                    msg += " metri (max consentito " + MAX_DISTANCE + " metri)";
                                    
                                    System.out.println(msg);
                                                         
                                }
			}
						
			//Se al termine del ciclo la verifica non ha dato anomalie si imposta OK
			if(i.getStatoVerificaGPS()==InterventoCompletoTO.StatoVerifica.notVerified)
				i.setStatoVerificaGPS(InterventoCompletoTO.StatoVerifica.checkOK);
			
		}
	}
	
	private void verificaAccelerometro()
	{
		for (InterventoCompletoTO i:listaInterventi)
		{
			/*Ciclo che legge tutti i log */
			
			//Dichiarazione cursori per timestamp
			Long first=null;
			Long last=null;
		
			for (int logIndex = 0; logIndex < i.contaLog(); logIndex++)
			{
				//log attuale
				Rilevazione currentLog = i.getLog(logIndex);
				
				//Verifica accelerometro fermo
				boolean standingAccelerometer = (currentLog.getAccX() == 0) && (currentLog.getAccY()==0) && (currentLog.getAccZ()==0);
				
				if(standingAccelerometer)
				{
					
					if (first==null) 
					{
						/*Se l'accelerometro e' fermo per la prima volta
						 * viene settato il primo cursore per il calcolo 
						 * del tempo trascorso*/
						first = currentLog.getTimestamp().getTime();
					}
					else
					{
						/*Se l'accelerometro era gia' fermo durante l'ultima rilevazione
						 *viene settata la rilevazione attuale come secondo valore per
						 *il calcolo del tempo trascorso e se questo e' superiore alla
						 *soglia viene impostato l'alert*/
						last=currentLog.getTimestamp().getTime();
						
						if (last-first > MAX_NULL_ACCESS_TIME )
                                                {
							i.setStatoVerificaAccelerometro(InterventoCompletoTO.StatoVerifica.anomaly);
                                                        //Trace
                                                        String msg="Verifica -> Rilevata anomalia accelerometro:";
                                                        msg += " nullo dalle " + DateFormatConverter.long2dateString(first,
                                                                DateFormatConverter.getFormatOra());
                                                        msg += " alle " + DateFormatConverter.long2dateString(last,
                                                                DateFormatConverter.getFormatOra());
                                                        
                                                        System.out.println(msg);
                                                }
					}
				}
				else
				{
					//L'accelerometro non e' fermo, vengono resettati i cursori
					first=null;
					last=null;
				}


			}
			//Se al termine del ciclo la verifica non ha dato anomalie si imposta OK
			if(i.getStatoVerificaAccelerometro()==InterventoCompletoTO.StatoVerifica.notVerified)
				i.setStatoVerificaAccelerometro(InterventoCompletoTO.StatoVerifica.checkOK);
		}
	}
}
