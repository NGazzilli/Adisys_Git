package business;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import adisys.server.strumenti.Record;

/**
 * Abstract class of the service classes of the system.
 * It does the lookup on the request, catching the class that provides the requested service and
 * calls for the proper method.
 * All the service classes of the system in the business tier extend this specific class.
*/
public abstract class ServiceHandler {
	
	
	/**
	 * It obtains the name of the wanted method thanks to the data structure that wraps the
	 * correspondence between key and method name.
	 * @param key it identifies the requested method
	 * @return name of the requested method
	*/
	protected String getMethodName(String key){
		return map.get(key);
	}
	
	
	
	/**
	 * Map that associates the name of the functionality coming from the front controller
	 * with the name of the effective method of the class that implements it.
	 * */
	protected HashMap<String, String> map = new HashMap<String, String>();
	
	
	
	
	/**
	 * Through {@code key} request coming from Front Controlelr it understands the method to call
	 * (belonging to the classes that extend it), able to satisfy the request. <br>
	 * Requested method may have input params, that are specificied in the ArrayList of {@link Record} with
	 * key = parameter type; value = values of the parameters of that type.
	 * 
	 * @param keyMethod functionality request coming from {@link RequestManager}
	 * @param params ArrayList of {@link Record} that have one couple <key, value>
	 * @throws NoSuchMethodException if no method has been found
	 * @throws SecurityException may be caused by getDeclaredMethod of Class class
	 * @throws InvocationTargetException if found method throws an exception
	 * @throws IllegalAccessException if the class or its builder are not accessible
	 * @throws IllegalArgumentException if found method doesn't have right input parameters
	 * @throws InstantiationException if the class has no builder, and therefore cannot be instantiated
	 * @throws ClassNotFoundException if the requested class cannot be found
	 * @return whatever value returned bu the called method
	*/
	public Object handleRequest(String keyMethod, ArrayList<Record<String, Object>> params)
	throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		
		
		System.out.println("ServiceHandler.handleRequest("+keyMethod+");");
		Class<? extends ServiceHandler> myselfClass = this.getClass();
		Object myselfObj = myselfClass.newInstance();
		System.out.println("ServiceHandler : Nome classe AS catturato: "+myselfClass.getSimpleName()+" "
				+myselfClass);
		
		String methodName = getMethodName(keyMethod);
		
		System.out.println("Metodo da chiamare: "+methodName);
		
		Method m;
		Object result = null;
		
		if(params==null || params.isEmpty()){//no parameters
			m = myselfClass.getDeclaredMethod(methodName);
			result = m.invoke(myselfObj);
			
		}
		else{//no parameters
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
			
			//got method with that name and those parameter's types
			m = myselfClass.getDeclaredMethod(methodName, classArr);
			result = m.invoke(myselfObj, valueArr);
		}
		
		
		return result;
	}
	
	
}
