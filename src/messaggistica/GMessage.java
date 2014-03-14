package messaggistica;

//import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
* Messaggistica di alto livello (GUI), con i suoi metodi mostra all'utente un messaggio 
* riassuntivo standard a seconda dell'evento generato (errore, informazioni, conferme).
*/
public class GMessage {
      
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
	
	private static final String NOT_FOUND = "La finestra <win> non e' stata trovata.";
	/**
	 * Da chiamare quando una finestra non viene trovata
	*/
	public static void winNotFound(String window){
		String message = NOT_FOUND.replaceAll("<win>", window);
		
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
              
	}
	
}
