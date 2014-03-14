package adisys.server.strumenti;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class ComboItem{
	   private String name;
	   private String value;

	   public ComboItem(String value, String name) {
	      this.name = name;
	      this.value = value;
	   }

	   public String getName() {
	      return name;
	   }

	   public String getValue() {
	      return value;
	   }

	   public String toString() {
	      return name; //Importante perche' la combobox stampa il toString degli oggetti
	   }
	}
