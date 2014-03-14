package adisys.server.strumenti;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Classe di utility che associa una chiave {@code T1} al valore {@code T2}
*/
public class Record <T1, T2> {
	public T1 key;
	public T2 value;
	
	public Record(T1 key, T2 value){
		this.key = key;
		this.value = value;
	}
	
	public String toString(){
		return "{"+key+", "+value+"}";
	}
	
}
