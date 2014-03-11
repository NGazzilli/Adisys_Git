package business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import adisys.server.strumenti.Record;


/**
 * Interfaccia dell'{@link ApplicationController}
*/
public interface Int_ApplicationController {
	
	public Object handleRequest(String keyMethod, ArrayList<Record<String, Object>> params)
	throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException;
	
}
