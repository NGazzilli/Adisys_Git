package integration.dao;



/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * 
 * Classe per la configurazione della sintassi sql.<br>
 * Incapsula il formato delle 4 query possibili sul database: insert, select, update, delete.<br>
 * Con i suoi metodi restituisce la sintassi completa delle interrogazioni.
*/
public class MySqlDAOConfig {
	
	/**Formato della query INSERT su un database mysql*/
	protected final static String INSERT_SQL = "insert into *TABLE ( *INSERT ) values ( *VALUES )";
	/**Formato della query SELECT su un database mysql*/
	protected final static String SELECT_SQL = "select *QUERY from *TABLE where ";
	/**Formato della query UPDATE su un database mysql*/
	protected final static String UPDATE_SQL = "update *TABLE set *FIELDSVALUES where ";
	/**Formato della query DELETE su un database mysql*/
	protected final static String DELETE_SQL = "delete from *TABLE where ";
	
	private String[] fieldsInsert;
	private String[] fieldsQuery;
	private String table;
	
	/**
	 * Costruttore, acquisisce le variabili in input
	 * @param fieldsInsert i campi che si vogliono specificare nella query insert
	 * @param fieldsQuery i campi che si vogliono specificare nella query select
	 * @param table il nome della tabella dove effettuare le query
	*/
	public MySqlDAOConfig(String fieldsInsert[], String fieldsQuery[], String table){
		this.fieldsInsert = fieldsInsert;
		this.fieldsQuery = fieldsQuery;
		this.table = table;
	}
	
	
	
	
	
	/**
	 * Restituisce la stringa per la query insert sul database, il formato lo prende dalla
	 * variabile {@link #INSERT_SQL}.
	 * @return la stringa nel formato mysql della query insert da dare in pasto al database mysql
	*/
	protected String getInsertSQL(){

		String str_fields_insert = "";
		//trasforma l'array in una string, le virgole stanno gia' negli elementi dell'array
		for(String field : fieldsInsert){//per ogni campo in FIELDS_INSERT
			str_fields_insert += field;
		}
		
		String insert = INSERT_SQL.replace("*INSERT", str_fields_insert);
		insert = insert.replace("*TABLE", table);
		
		//per ogni valore
		String values = "";
		for(int i=0; i<fieldsInsert.length; i++){
			if(i==fieldsInsert.length-1)//se e' l'ultimo non va la virgola
				values += "?";
			else
				values += "?, ";
		}
		insert = insert.replace("*VALUES", values);
		return insert;
	}
	
	
	
	
	
	
	
	
	/**
	 * Restituisce la stringa per la query select sul database, il formato lo prende dalla
	 * variabile {@link #SELECT_SQL}.
	 * @param condition la condizione per la query sql.
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return la stringa in formato sql della query select da dare in pasto al database mysql
	*/
	protected String getSelectSQL(String condition){
		String str_fields_select = "";
		//trasforma l'array in una string, le virgole stanno gia' negli elementi dell'array
		for(String field : fieldsQuery){//per ogni campo in FIELDS_INSERT
			str_fields_select += field;
		}
		
		String select = SELECT_SQL.replace("*QUERY", str_fields_select);
		select = select.replace("*TABLE", table);
		
		return select+condition;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo nel caso si voglia dare un ordine al risultato della query.<br>
	 * Aggiunge la clausola ORDER BY alla query select, il formato lo prende dalla
	 * variabile {@link #SELECT_SQL}
	 * @param condition la condizione affinche' i campi vadano cambiati.
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @param orderBy il campo secondo il quale ordinare il risultato della query
	 * @return la stringa in formato sql della query select compresa la clausola ORDER BY 
	 * da dare in pasto al database mysql
	*/
	protected String getSelectSQL(String condition, String orderBy){
		
		String str_fields_select = "";
		//trasforma l'array in una string, le virgole stanno gia' negli elementi dell'array
		for(String field : fieldsQuery){//per ogni campo in FIELDS_INSERT
			str_fields_select += field;
		}
		
		String select = SELECT_SQL.replace("*QUERY", str_fields_select);
		select = select.replace("*TABLE", table);
		
		orderBy = " order by "+orderBy;
		
		return select+condition+orderBy;
	}
	
	
	
	
	
	
	
	
	/**
	 * Restituisce la stringa per la query update sul database, il formato lo prende dalla
	 * variabile {@link #UPDATE_SQL}.
	 * @param fieldsValues l'array di stringhe che associa i campi da cambiare con i nuovi valori.
	 * Formato di ogni elemento <b>{@code FIELD = VALUE,}</b> . Attenzione: ricordarsi le virgolette "" 
	 * sul valore se il campo che si sta inserendo sia diverso da un numero
	 * @param condition la condizione affinche' i campi vadano cambiati.
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return la stringa in formato sql della query update da dare in pasto al database mysql
	*/
	protected String getUpdateSQL(String[] fieldsValues, String condition) {
		String str_fieldsValues = "";
		//trasforma l'array in una string, le virgole stanno gia' negli elementi dell'array
		for(String field : fieldsValues){//per ogni campo in fieldsValues
			str_fieldsValues += field;
		}
		String update = UPDATE_SQL.replace("*FIELDSVALUES", str_fieldsValues);
		update = update.replace("*TABLE", table);
		
		return update+condition;
	}
	
	
	
	
	
	
	
	
	/**
	 * Restituisce la stringa per la query delete sul database, il formato lo prende dalla
	 * variabile {@link #DELETE_SQL}.
	 * @param condition la condizione affinche' i campi vadano cambiati.
	 * Formato FIELD=VALUE ricordarsi le virgolette "" nel caso VALUE sia una stringa
	 * @return la stringa in formato sql della query delete da dare in pasto al database mysql
	*/
	protected String getDeleteSQL(String condition){
		String delete = DELETE_SQL.replace("*TABLE", table);
		return delete+condition;
	}
}
