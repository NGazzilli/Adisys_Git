package business;

import integration.dao.AbstractDAO;

/**
 * Interface of Transfer Object.<br>
 * It is need for DAO structure. {@link AbstractDAO} It cannot know which
 * of the transfer objects associated to DAO is being processed, there is need for
 * an interface to allow declaration of CRUD methods.
*/
public interface TO {

}
