package business;

import integration.dao.AbstractDAO;

/**
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 * 
 * Interfaccia del Transfer Object.<br>
 * E' necessaria per la struttura DAO. {@link AbstractDAO} non può sapere quale è
 * il transfer object associato alla DAO in esecuzione, è necessaria quindi un'interfaccia
 * anche vuota per permettere la dichiarazione dei metodi CRUD.
*/
public interface TO {

}
