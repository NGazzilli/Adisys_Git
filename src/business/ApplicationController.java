package business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import adisys.server.strumenti.Record;
import messaggistica.GMessage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * System's Application Controller.
 * The user requests a service by sending a string that identifies the desired business service,
 * this request is filtered by the Front Controller that dispatches it to the Application Controller.
 * This one finds the related class linked to the requested service, with the help of {@link ReadCrossing} in an XML file.
 * One it has been found, it calls for the method that handles the requested functionality using
 * {@link ServiceHandler}
 * 
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
*/

public class ApplicationController implements Int_ApplicationController{
	
    /**
     * Abstraction on the XML file that contains the matching request -> service class
     */
    private static ReadCrossing XMLfile;
    private static ResourceBundle applicationController = ResourceBundle.getBundle("adisys/server/property/ApplicationController");
	
    public static void setResourceBundle(String path, Locale local){
        applicationController = ResourceBundle.getBundle(path, local);
    }
	/**
	 * This Service Handler finds the method to execute using the name of the method as
	 * the input and its parameters.
	*/
	private ServiceHandler ASAbstract;
	
	/**
	 * Builder, it starts the {@link ReadCrossing} and processes the method {@link #getInstance(String request)}
	 * 
	*/
	public ApplicationController(String requestMethod){
		XMLfile = new ReadCrossing();
		ASAbstract = (ServiceHandler) getInstance(requestMethod);
	}
	
	
	/**
	 * It obtains the instance of the business class associated to the request {@code request}
	 * and then it starts {@link #ASAbstract}
	 * @param request the string it identifies 
	 * @throws ClassNotFoundException if there is no class with that name
	*/
	private Object getInstance(String request){
		System.out.println("ApplicationController : getInstance("+request+")");
		
		Class<?> classBusiness = null;
		Object objBusiness = null;
		
		String rHandlerName = "<null>";
		
		try{
			//fetches the class name that handles the requested service
			rHandlerName = getASName(request);
                        
			//name Class of found class
			classBusiness =  Class.forName(rHandlerName);
                        //instance of the Class obtained before
			objBusiness = classBusiness.newInstance();
			
		} catch(ClassNotFoundException e){
			System.err.println(applicationController.getString("OPS.. APPLICATIONCONTROLLER:") +
					applicationController.getString("IL REQUESTMANAGER (FRONT CONTROLLER) RICEVERA' UN ERRORE,") +
				applicationController.getString("LA FUNZIONALITA' RICHIESTA '")+request+applicationController.getString("' PUO' NON ESSERE DISPONIBILE,") +
				applicationController.getString("SI E' TENTATO DI CHIAMARE IL REQUEST HANDLER ")+rHandlerName+applicationController.getString(" CHE RISULTA INESISTENTE") +
				applicationController.getString("CONTROLLARE LE CORRISPONDENZE DEL FILE XML OPPURE LA REQUEST INVIATA DALLA BOUNDARY"));
                        GMessage.message_error(applicationController.getString("ERRORE FUNZIONALITA"));
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objBusiness;
	}
	
	
	
	
	
	/**
	 * Through {@code key} request coming from Front Controller, it
	 * understands the method to call (belonging to the classes that extend it) to
	 * satisfy the request.
	 * Requested method may have input parameters, these are specified in the ArrayList of {@link Record}
	 * with key = parameter type; value = values of the parameter of that type
	 * 
	 * @see ServiceHandler#handleRequest(String, ArrayList)
	 * 
	 * @param key functionality request coming from {@link presentation.RequestManager}
	 * @param params ArrayList of {@link Record} that have one couple <key, value>
	 * @throws NoSuchMethodException if no method has been found
	 * @throws SecurityException could be caused by getDeclaredMethod method of the class Class
	 * @throws InvocationTargetException if found method throws an exception
	 * @throws IllegalAccessException if the class or its builder are not accessible
	 * @throws IllegalArgumentException if found method doesn't have correct input parameters
	 * @throws InstantiationException if class has not a builder and therefore cannot be instantiated
	 * @throws ClassNotFoundException if requested class cannot be found
	 * @return whatever value returned from the called method
	*/
	public Object handleRequest(String keyMethod, ArrayList<Record<String, Object>> params)
	throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		return ASAbstract.handleRequest(keyMethod, params);
	}
	
	
	
	
	
	
	
	/**
	 * Returns the name of the class that provides the requested service, using 
	 * {@link ReadCrossing#getNameHandler(String)}
	 * @throws ClassNotFoundException if no class has been associated
	*/
	private String getASName(String request) throws ClassNotFoundException{
		
		String handlerName = XMLfile.getNameHandler(request);
		if(handlerName == null){
			throw new ClassNotFoundException();
		}
		else{
			return handlerName;
		}
	}
	
	
	
}
