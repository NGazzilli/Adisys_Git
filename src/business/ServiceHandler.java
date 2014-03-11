package business;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import adisys.server.strumenti.Record;

/**
 * Classe astratta delle classi di servizio del sistema.
 * Effettua il lookup sulla richiesta, cattura la classe che offre il servizio
 * richiesto(che sar&agrave quella adesso in esecuzione che estende questa stessa)
 *  e invoca il metodo appropriato.<br>
 * Tutte le classi di servizio del sistema a livello di business estendono questa classe.
*/
public abstract class ServiceHandler {
	
	
	/**
	 * Ottiene il nome del metodo voluto grazie alla struttura dati che incapsula la corrispondeza
	 * tra key e il nome del metodo
	 * @param key chiave che identifica il metodo richiesto
	 * @return il nome del metodo richiesto
	*/
	protected String getMethodName(String key){
		return map.get(key);
	}
	
	
	
	/**Mappa che associa il nome della funzionalit&agrave giunta dal front controller
	 * col nome dell'effettivo metodo di questa classe che la implementa.*/
	protected HashMap<String, String> map = new HashMap<String, String>();
	
	
	
	
	/**
	 * Attraverso la richiesta {@code key} proveniente dal Front Controller
	 * capisce il metodo da invocare (appartenente alle classi che la estendono)
	 * che è in grado di soddisfare la richiesta.<br>
	 * Ovviamente il metodo richiesto pu&ograve avere dei parametri in input,
	 * questi sono specificati dall'ArrayList di {@link Record} con chiave = tipo di parametro;
	 *  valore = valori dei parametri di quel tipo
	 * 
	 * @param keyMethod la richiesta di funzionalit&agrave proveniente dal {@link RequestManager}
	 * @param params &egrave un'ArrayList di {@link Record} che hanno una sola coppia <chiave, valore>
	 * @throws NoSuchMethodException se il metodo non viene trovato
	 * @throws SecurityException pu&ograve essere causato dal metodo getDeclaredMethod della classe Class
	 * @throws InvocationTargetException se il metodo trovato lancia un eccezione
	 * @throws IllegalAccessException se la classe o il suo costruttore non sono accessibili
	 * @throws IllegalArgumentException se il metodo trovato non ottiene i giusti parametri in input
	 * @throws InstantiationException se la classe non ha un costruttore, quindi non pu&ograve essere 
	 * istanziata
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 * @return qualsiasi valore ritornato dal metodo invocato
	*/
	public Object handleRequest(String keyMethod, ArrayList<Record<String, Object>> params)
	throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		
		
		System.out.println("ServiceHandler.handleRequest("+keyMethod+");");
		//ottiene la classe in esecuzione che sta estendendo queswta stessa
		Class<? extends ServiceHandler> myselfClass = this.getClass();
		Object myselfObj = myselfClass.newInstance();
		System.out.println("ServiceHandler : Nome classe AS catturato: "+myselfClass.getSimpleName()+" "
				+myselfClass);
		
		String methodName = getMethodName(keyMethod);
		
		System.out.println("Metodo da chiamare: "+methodName);
		
		Method m;
		Object result = null;
		
		if(params==null || params.isEmpty()){//il metodo non ha parametri
			m = myselfClass.getDeclaredMethod(methodName);
			result = m.invoke(myselfObj);
			
		}
		else{//il metodo non ha parametri
			Iterator< Record<String, Object> > iterParam = params.iterator();
			
			Class<?>[] classArr = new Class<?>[params.size()];
			Object[] valueArr = new Object[params.size()]; 
			int index = 0;
			
			while(iterParam.hasNext()){
				Record<String, Object> currentCouple = iterParam.next();
				Class<?> currentClass = Class.forName(currentCouple.key);
				
				classArr[index] = currentClass;
				valueArr[index] = currentCouple.value;
				
				System.out.println("ServiceHandler : currentCouple = "+currentCouple);
				
				index++;
			}
			
			//prendo il metodo con quel nome e quei tipi di parametri
			m = myselfClass.getDeclaredMethod(methodName, classArr);
			//invoco il metodo definito e i valori dei parametri, acquisisco
			//il valore di ritorno (null se e' void)
			result = m.invoke(myselfObj, valueArr);
			
//			System.out.println(result);
		}
		
		
		return result;
	}
	
	
}
