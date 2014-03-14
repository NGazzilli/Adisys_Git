package messaggistica;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Eccezione di basso livello, può essere lanciata a qualsiasi livello, verrà
 * catturata al livello di graphic user interface
*/
public class MainException extends Exception{
	private static final long serialVersionUID = 1L;

	public Class<?> exception = MainException.class;
	
	public MainException(String msg){
		super(msg);
	}
	
	
	/**
	 * Costruttore nel caso si voglia specificare il tipo di eccezione lanciata.
	*/
	public MainException(String msg, Class<?> typeOfExc){
		super(msg);
		exception = typeOfExc;
	}
}
