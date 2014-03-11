package adisys.server.strumenti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Classe di utility che fornisce metodi per la lettura di un file XML.
 * @version 1.0
*/
//I commenti nel codice costituiscono una breve documentazione sul funzionamento della visita di un file XML
public class ReadXML {
	
	
	/*
	 * Piccolo remind sulla sintassi XML:
	 * La struttura XML e' gerarchica, l'XML deve contenere un solo elemento radice (root) che racchiude tutti 
	 * gli altri elementi secondo una struttura di relazione padre-figlio. La radice e' il primo elemento.
	 * La prima riga e' una dichiarazione che definisce la versione XML del documento, puo' essere anche
	 * specificato il tipo di codifica usata (esempio UTF-8 oppure ISO-8859-1).
	 * Es. <?xml version="1.0" encoding="ISO-8859-1"?>
	 * Un documento XML e' well formed se ha un corrispondente DTD (oppure XSD, quello che usiamo noi) e ne rispetta
	 * la sintassi.
	 * 
	 * 
	 * 
	 * ATTENZIONE : 
	 * Nella visita dell'albero fare attenzione quando si leggono i valori dei primi figli.
	 * nodo.getFirstChild();
	 * restituisce il primo figlio, che puo' essere il testo immediatamente seguente (anche spazi e tabulazioni)
	 * quindi occhio quando si fa
	 * nodo.getFirstChild().getNextSibling()
	 * Questo restituisce il nodo corretto ma non il valore che esso contiene, infatti se fai
	 * nodo.getFirstChild().getNextSibling().getNodeValue()
	 * Il risultato e' null, anche se il nodo contiene qualcosa.
	 * Per prelevare effettivamente i dati contenuti nel primo figlio devi fare
	 * nodo.getFirstChild().getNextSibling().getFirstChild().getNodeValue()
	*/
	
	
	private static final boolean DEBUG = false;
	
	
	
	private Document doc = null;

	
	
	
	/**
	 * Costruttore che inizializza il {@link Document} {@code doc} che rappresenta l'intero file xml
	 * 
	 * @param percorsoFile percorso del file XML da cui leggere i dati
	 * @throws IOException errore di I/O durante il parse del documento
	 * @throws SAXException errore di parse
	 * @throws IllegalArgumentException se percorsoFile non corrisponde ad un file
	 */	
	public ReadXML (String percorsoFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		doc = docBuilder.parse (new File(percorsoFile));
	}

	
	
	
	/**
	 * Legge dati dal file XML sotto il tag {@code tag}.<br>
	 * Usarlo per elementi che non hanno figli
	 * 
	 * @param tag il tag del file XML da cui recuperare il valore 
	 * @return il valore del tag {@code tag}
	 */
	public ArrayList<String> readValue (String tag){ 
		if(DEBUG) System.out.println("readValue("+tag+")");
		ArrayList<String> valueTag = new ArrayList<String>();
		NodeList listOfParameters = doc.getElementsByTagName(tag);

		for(int s=0; s<listOfParameters.getLength(); s++){

			Node ParameterNode = listOfParameters.item(s);
			String value = ParameterNode.getFirstChild().getNodeValue();
			valueTag.add(value);
		}
		return valueTag;
	}

	
	
	
	/*
	 * <u>NON USATO IN ADISYS</u>.<br>
	 * Crea una mappa con chiave uguale ad ogni elemento con nome keyNodeName e per ogni elemento trovato
	 * associa il valore dei suoi elementi figli
	 * @param keyNodeName tag del file XML dal quale recuperare i valori
	 * @return hashmap contenente come chiave il nodo corrispondente a keyNodeName e i valori associati sono
	 * i valori dei figli di tale nodo
	
	public HashMap<Node, ArrayList<String>> getMapChildNode(String keyNodeName){
		System.out.println("getMapChildNode("+keyNodeName+")");
		if(DEBUG) System.out.println("Sto processando l'elemento : "+keyNodeName);
		
		HashMap<Node, ArrayList<String>> map = new HashMap<Node, ArrayList<String>>();
		
		//ottiene il document element grazie al quale posso scansionare gli elementi dell'xml
		Element rootElement = doc.getDocumentElement();
		
		//ottengo una lista di nodi del tag == keyNodeName (stringa arrivata per parametro) 
		NodeList keyList = rootElement.getElementsByTagName(keyNodeName);
		
		for(int s=0; s<keyList.getLength(); s++){//ciclo per ogni elemento della lista keyList
			if(DEBUG) System.out.println("Processo il "+keyNodeName+" numero "+s+" ------------------------------------");
			
			ArrayList<String> mapElements = new ArrayList<String>();//array di string per contenere i valori
			Node keyNode = keyList.item(s);//ritorna il nodo attuale della lista
			
			
//			 * keyNode.getAttributes() : restituisce una mappa di tutti gli attributi relativi a keyNode
//			 * keyNode.getAttributes().item(0) : restituisce il primo attributo della mappa
//			 * keyNode.getAttributes().item(0).getNodeName() : restituisce il nome dell'attributo
//			 * keyNode.getAttributes().item(0).getNodeValue() : restituisce il valore dell'attributo
			
			
			//ottengo il nome del primo attributo del nodo keyNode attuale
			String nomePrimoAttr = keyNode.getAttributes().item(0).getNodeName();
			
			//ottengo il valore del primo attributo del nodo keyNode attuale
			String valPrimoAttr = keyNode.getAttributes().item(0).getNodeValue();
			
			
			if(DEBUG) System.out.println(nomePrimoAttr+"  =  "+valPrimoAttr);
			
			
			
//			 * keyNode.getFirstChild() : restituisce qualsiasi cosa subito dentro keyNode, se e' del testo e' #text e 
//			 * lo prende tutto fino al prossimo elemento (#text vuol dire anche le tabulazioni e spazi!!)
			 
//			 * keyNode.getFirstChild().getNextSibling() : restituisce il nodo immediatamente successivo all'attuale,
//			 * con il quale non e' imparentato
			 
//			 * Quindi l'evocazione: keyNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue()
//			 * restituisce l'effettivo primo nodo figlio (elemento dell'xml) del nodo keyNode 
			
			
			String nomeFirstChild = keyNode.getFirstChild().getNodeName();
			
			if(DEBUG)
				System.out.println("Subito dentro "+keyNode.getNodeName()+" sta "+
					nomeFirstChild+"    (se e' #text vuol dire che c'e' del testo)");
			
			//ottiene il valore del primo elemento figlio di keyNode
//			String son1Value = keyNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue();
			
			//if(DEBUG) System.out.println(node1Value);
			
			
			
//			 * keyNode.getChildNodes() : restituisce una NodeList di tutti i nodi figli di keyNode, tutti i nodi vuol dire
//			 * anche i #text cioe' il testo dentro keyNode
			 
//			 * keyNode.getChildNodes().item(i) : restituisce il nodo figlio in posizione i (intero), compreso 
//			 * l'eventuale testo
			 
			 
			
			
			if(DEBUG){
				System.out.println("Tutti i nomi dei nodi figli");
				for(int i=0; i<keyNode.getChildNodes().getLength(); i++){
					String nodeChildName  = keyNode.getChildNodes().item(i).getNodeName();
					System.out.print(nodeChildName+"     ");
				}
				System.out.println();
			}
			
			
			if(DEBUG) System.out.println("Provo a stampare solo gli elementi figli, scartando il testo");
			//provo a prelevare solo gli elementi contenuti in keyNode, scartando i #text
			for(int i=0; i<keyNode.getChildNodes().getLength(); i++){
				
				if(!keyNode.getChildNodes().item(i).getNodeName().equals("#text")){
					String elemChildName = keyNode.getChildNodes().item(i).getNodeName();
					String elemChildValue = keyNode.getChildNodes().item(i).getFirstChild().getNodeValue();
					
					if(DEBUG) System.out.print(elemChildName+"="+elemChildValue+"    ");
					mapElements.add(elemChildValue);
				}
				
			}
			
			System.out.println();
			
			
			//se il terzo figlio 
//			if (keyNode.getChildNodes().item(2).getNextSibling()!= null){
//				String node2Value = keyNode.getChildNodes().item(2).getNextSibling().getFirstChild().getNodeValue();
//				mapElements.add(node2Value);
//				if (keyNode.getLastChild().getPreviousSibling().getFirstChild() != null){
//					String node3Value = keyNode.getLastChild().getPreviousSibling().getFirstChild().getNodeValue();
//					mapElements.add(node3Value);
//				}
//			}
			
			//inserisco la coppia (K,V) nella map
//			map.put(valPrimoAttr, mapElements);
			
			
			map.put(keyNode, mapElements);
			
			if(DEBUG) System.out.println("------------------------------------------------------------------\n");
		}
		
		
		return map;
	}
	*/
	
	
	
	
	
	
	
	
	/**
	 * Restituisce il nodo nodeName che ha come valore del suo attributo primo {@code firstAttrValue}<br>
	 * &lt;nodeName attr="unValore"&gt;&lt;/nodeName&gt;<br>
	 * <b>&lt;nodeName attr="firstAttrValue"&gt;&lt;/nodeName&gt;<br></b>
	 * &lt;nodeName attr="unaltrovalore"&gt;&lt;/nodeName&gt;<br>
	 * @param nodeName il nome del nodo
	 * @param firstAttrValue il valore del primo attributo del nodo
	 * @return il nodo con il valore del primo attributo specificato, null se non e' presente un nodo
	 * nodeName che ha come valore del suo primo attributo firstAttrValue
	*/
	private Node getNodeByFirstAttrValue(String nodeName, String firstAttrValue){
		if(DEBUG) System.out.println("getNodeByFirstAttrValue("+nodeName+", "+firstAttrValue+")");
		//ottiene il document element grazie al quale posso scansionare gli elementi dell'xml
		Element rootElement = doc.getDocumentElement();
		
		//ottengo una lista di nodi del tag == nodeName (stringa arrivata per parametro) 
		NodeList keyList = rootElement.getElementsByTagName(nodeName);
		
		//cicla per ogni nodo nella lista
		for(int i=0; i<keyList.getLength(); i++){
			Node keyNode = keyList.item(i);//ritorna il nodo attuale della lista
			
			//ottengo il valore del primo attributo del nodo keyNode attuale
			String nomePrimoAttr = keyNode.getAttributes().item(0).getNodeValue();
			if(DEBUG) System.out.println("getNodeByFirstAttrValue : nomePrimoAttr="+nomePrimoAttr);
			
			
			if(nomePrimoAttr.equals(firstAttrValue)){//nodo trovato!
				//questo nodo ha come attributo primo quello specificato per parametro
				return keyNode;
				
			}
			
		}
		
		return null;
	}
	
	
	
	
	/**
	 * Restituisce il valore del primo figlio (non testuale) del nodo {@code nodeName} 
	 * che ha valore del primo attributo = {@code firtsAttrValue}.
	 * Esempio, è utile nel caso in cui abbiamo vari nodi con lo stesso nome che vengono
	 * identificati dal valore del loro primo attributo.<br>
	 * &lt;nodeName attr="firstAttrValue"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son1&gt;<b>Valore</b>&lt;/son1&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son2&gt;&lt;/son2&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son3&gt;&lt;/son3&gt;<br>
	 * &lt;/nodeName&gt;
	 * 
	 * @param nodeName il nome del nodo papà del nodo da restituire
	 * @param firstAttrValue il valore del primo attributo del papa' del nodo restituito
	 * @return il valore del nodo primogenito del nodo che ha attrValue come valore del suo primo attributo
	*/
	public String getFirstChildValue(String nodeName, String firstAttrValue){
		if(DEBUG) System.out.println("ReadXML.getFirstChildValue("+nodeName+", "+firstAttrValue+")");
		
		//trova il nodo che ha quell'attributo
		Node dad = this.getNodeByFirstAttrValue(nodeName, firstAttrValue);
		
		if(dad == null){
			return null;
		}
		
		//recupera il primo figlio del nodo trovato
		String firstChildValue;
		if( ( dad.getFirstChild().getNodeName() ).equals("#text") ){
			if(DEBUG) System.out.println("getFirstChildValue : firstChildName="
					+dad.getFirstChild().getNextSibling().getNodeName());
			
			firstChildValue = dad.getFirstChild().getNextSibling().getFirstChild().getNodeValue();
			if(DEBUG)System.out.println("ramo if: "+firstChildValue);
		}
		else{
			firstChildValue = dad.getFirstChild().getNodeValue();
			if(DEBUG) System.out.println(firstChildValue);
		}
		
		return firstChildValue;
	}
	
	
	
	
	
	/**
	 * Restituisce i valori dei figli del nodo {@code nodeName} che ha come valore
	 * del suo primo attributo {@code firstAttrValue}<br>
	 * &lt;nodeName attr="firstAttrValue"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son1&gt;<b>Valore figlio 1</b>&lt;/son1&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son2&gt;<b>Valore figlio 2</b>&lt;/son2&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;son3&gt;<b>Valore figlio 3</b>&lt;/son3&gt;<br>
	 * &lt;/nodeName&gt;
	 * @param nodeName il nome del nodo da cui ottenere i valori dei figli
	 * @param firstAttrValue il valore del primo attributo del nodo da cui ottenere i figli
	 * @return un array di stringhe con i valori dei figli del nodo
	*/
	public ArrayList<String> getChildrenValue(String nodeName, String firstAttrValue){
		if(DEBUG) System.out.println("ReadXML.getChildrenValue("+nodeName+", "+firstAttrValue+")");
		
		//trova il nodo che ha quell'attributo
		Node dad = this.getNodeByFirstAttrValue(nodeName, firstAttrValue);
		
		if(dad == null){
			return null;
		}
		
		ArrayList<String> childrenValues = new ArrayList<String>();
		
		NodeList children = dad.getChildNodes();
		
		
		if(DEBUG) System.out.println("children.getLength() = "+children.getLength());
		for(int i=0; i<children.getLength(); i++){
			//se e' un testo, non ha un figlio, quindi getFirstChild() lancia NullPointerException
			if( ( children.item(i).getNodeName() ).equals("#text") ){
				continue;
			}
			else{
				childrenValues.add(children.item(i).getFirstChild().getNodeValue());
				if(DEBUG) System.out.println(children.item(i).getFirstChild().getNodeValue());
			}
			
		}
		
		return childrenValues;
	}
	
	
	
	
	
	
	
	
	
	/**Restituisce il numero di istanze del nodo {@code nodeName}*/
	public int getNodeIstanceNumber(String nodeName){
		if(DEBUG) System.out.println("getNodeIstanceNumber("+nodeName+")");
		ArrayList<String> nodeNameFirstAttrValues = this.getFirstAttributeValue(nodeName);
		
		return nodeNameFirstAttrValues.size();
	}
	
	
	
	
	
	/**
	 * Prende in input il nome di un nodo e per ogni istanza di quel nodo, restituisce il valore
	 * del primo attributo.
	 * @param nodeName il nome del nodo
	 * @return l'array di stringhe che corrisponde al valore del primo attributo di ogni {@code nodeName}
	*/
	public ArrayList<String> getFirstAttributeValue(String nodeName){
		if(DEBUG) System.out.println("getFirstAttributeValue("+nodeName+")");
		//ottiene il document element grazie al quale posso scansionare gli elementi dell'xml
		Element rootElement = doc.getDocumentElement();
		
		//ottengo una lista di nodi del tag == nodeName (stringa arrivata per parametro) 
		NodeList keyList = rootElement.getElementsByTagName(nodeName);
		
		ArrayList<String> values = new ArrayList<String>();
		
		for(int i=0; i<keyList.getLength(); i++){
			Node keyNode = keyList.item(i);//ritorna il nodo attuale della lista
			
			//ottengo il nome del primo attributo del nodo keyNode attuale
//			@SuppressWarnings("unused")
//			String nomePrimoAttr = keyNode.getAttributes().item(0).getNodeName();
			
			//ottengo il valore del primo attributo del nodo keyNode attuale
			String valPrimoAttr = keyNode.getAttributes().item(0).getNodeValue();
			
			//aggiunge il valore del primo attributo appena trovato all'arrayList
			values.add(valPrimoAttr);
		}
		
		return values;
	}
	
	


}
