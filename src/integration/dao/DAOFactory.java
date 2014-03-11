package integration.dao;

/**
 * DAOFactory, funzionamento coincidente con il pattern <i>Abstract Factory</i>.<br>
 * Delega la creazione dei prodotti (le classi DAO) alla Concrete Factory.<br>
 * L'utente richiede la Concrete Factory al metodo static {@link #getDAOFactory(int)} specificando 
 * un parametro intero:
 * <ul>
 * 		<li>0 = {@link MySqlDAOFactory}</li>
 * </ul>
 * 
 * Può essere estesa da un altra Concrete Factory aggiungendo un'altra costante intera come
 * fatto per MYSQL.
 * 
 * @author Luca Massa
*/
public abstract class DAOFactory {
	public final static int MYSQL = 0;
	
	/**
	 * Restituisce l'istanza della classe dao associata alla entity {@code request}
	 * @param request la stringa che identifica la dao voluta
	 * @return l'oggetto dao gi&agrave istanziato, null se la dao non viene trovata
	*/
	public abstract AbstractDAO createDAO(String request);
	
	
	
	/**
	 * Restituisce la DAO factory richiesta tramite valore intero. Ogni DAO factory
	 * ha un corrispondente numero intero
	 * @param whichFactory un int per specificare il tipo di dao voluto
	 * @return l'istanza del dao corrispondente all'intero passato per parametro 
	*/
	public static DAOFactory getDAOFactory(int whichFactory){
		switch(whichFactory){
			case MYSQL:
				return new MySqlDAOFactory();
			default:
				return null;
		}
		
	}
	
}
