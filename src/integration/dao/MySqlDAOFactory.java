package integration.dao;

/**
 * Concrete Factory, si occupa dell'istanziazione e restituzione della dao richiesta
 * tramite reflection.
*/
public class MySqlDAOFactory extends DAOFactory{
	/**Istanza di se stesso*/
	private static MySqlDAOFactory obj = null;
	
	private static ReadEntityDAO fileXML;
	
	public MySqlDAOFactory(){
		fileXML = new ReadEntityDAO();
	}

	/**
	 * Implementazione del pattern Singleton. Non avendo un costruttore public, si può ottenere
	 * una istanza di {@link MySqlDAOFactory} solo attraverso questo metodo che restituisce
	 * una nuova istanza sse non ne esiste un'altra.
	*/
	public static MySqlDAOFactory getInstance(){
		if(obj == null){
			obj = new MySqlDAOFactory();
		}
		
		return obj;
	}
	
	
	
	/**
	 * Crea e restituisce una istanza della classe dao associata alla richiesta {@code request}
	*/
	@Override
	public AbstractDAO createDAO(String request){
		System.out.println("MySqlDAOFactory : getInstance("+request+")");
		
		String rHandlerName = getDAOName(request);
		Class<?> classObj = null;
		
		if(rHandlerName==null){
			return null;
		}
		else{
		
			try{
				classObj =  Class.forName(rHandlerName);
				return (AbstractDAO) classObj.newInstance();
			} catch(ClassNotFoundException e){
				System.err.println("Ops.. La DAO factory ha generato un errore, non e' stato trovato\n" +
					"alcun dao associato alla entity ricevuta "+request);
				e.printStackTrace();
				return null;
			} catch (InstantiationException e) {
				System.err.println("Errore al momento della istanziazione del dao "+rHandlerName);
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				System.err.println("Errore al momento della istanziazione del dao "+rHandlerName);
				e.printStackTrace();
				return null;
			}
			
		}
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Restituisce la dao appropriata, a seconda del nome dell'entity passata per parametro
	 * @param entityName il nome dell'entity chiamante
	 * @return il nome della DAO associata alla entity chiamante, null se non ha trovato
	 * nessuna DAO associata
	*/
	private String getDAOName(String entityName){
		return fileXML.getDAOName(entityName);
	}
		
	
	/*
	public static void main (String args[]){
		MySqlDAOFactory mysqldao = (MySqlDAOFactory) DAOFactory.getDAOFactory(0);
		AbstractDAO intDAO = (AbstractDAO) mysqldao.createDAO("Infermiere");
		
		ArrayList<TO> infermieri = intDAO.read();
		for(TO interfaceInf : infermieri){
			InfermiereTO inf = (InfermiereTO) interfaceInf;
			System.out.println(inf.getId()+"  "+inf.getNome()+"  "+inf.getCognome());
		}
		
		intDAO = mysqldao.createDAO("Paziente");
		
		ArrayList<TO> pazienti = intDAO.read();
		for(TO interfaceInf : pazienti){
			PazienteTO paz = (PazienteTO) interfaceInf;
			System.out.println(paz.getId()+"  "+paz.getNome()+"  "+paz.getCognome()+"  "+paz.getCitta());
		}
	}
	*/




}
