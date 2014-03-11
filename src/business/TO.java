package business;

import integration.dao.AbstractDAO;

/**
 * Interfaccia del Transfer Object.<br>
 * E' necessaria per la struttura DAO. {@link AbstractDAO} non pu� sapere quale �
 * il transfer object associato alla DAO in esecuzione, � necessaria quindi un'interfaccia
 * anche vuota per permettere la dichiarazione dei metodi CRUD.
*/
public interface TO {

}
