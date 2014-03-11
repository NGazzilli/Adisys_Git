package business.pianificazione;
import business.intervento.Rilevazione;
import business.intervento.TipoIntervento;
import business.intervento.InterventoCompletoTO;
import business.intervento.InterventoTO;
import business.patologia.PatologiaTO;
import business.paziente.PazienteTO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import java.util.Locale;
import java.util.ResourceBundle;

public class StrutturaInterscambio {
    private static ResourceBundle strutturaInterscambio = ResourceBundle.getBundle("adisys/server/property/StrutturaInterscambio");
	private final String FORMATO_DATA_XML = "yyyy-MM-dd";
	private final String FORMATO_ORA_XML = "HH:mm:ss";

    public static void setResourceBundle(String path, Locale locale){
            strutturaInterscambio = ResourceBundle.getBundle(path, locale);
    }
	Element interventi;

	public StrutturaInterscambio()
	{
		interventi= new Element("interventi");
	}

	public void addIntervento(InterventoTO datiIntervento, PazienteTO datiPaziente)
	{
		//crea il nuovo oggetto Element intervento
		Element nuovoIntervento = new Element("intervento");

		//trasferisce i dati dall'oggetto intervento all'element
		//Creazione e inserimento ID intervento
		Attribute id = new Attribute("id", int2string6(datiIntervento.getID()));

		//Attributo orafine non vengono inseriti (a carico di ADISysMobile)
		Attribute oraInizio = new Attribute("orainizio", datiIntervento.getOraInizioDaFormato(FORMATO_ORA_XML));
		//Attribute oraFine = new Attribute("orainizio", datiIntervento.getOraInizioDaFormato(FORMATO_ORA_XML));


		//Elemento Operatore intervento
		Element operatoreIntervento = new Element("operatoreIntervento");
		operatoreIntervento.setAttribute("id", int2string6(datiIntervento.getIDInfermiere()));


		//Elemento data
		Element data = new Element("data");
		data.setText(datiIntervento.getDataDaFormato(FORMATO_DATA_XML));

		//Elemento luogo
		Element luogo = new Element("luogo");
		luogo.setAttribute("citta", datiIntervento.getCitta());
		luogo.setAttribute("indirizzo", datiIntervento.getCivico());
		luogo.setAttribute("cap", datiIntervento.getCap());

		//Elemento paziente con ID
		Element paziente = new Element("paziente");
		paziente.setAttribute("id", int2string6(datiPaziente.getID()));

		{
			//Nome paziente
			Element paziente_nome = new Element("nome");
			paziente_nome.setText(datiPaziente.getNome());
			paziente.addContent(paziente_nome);

			//Cognome paziente
			Element paziente_cognome = new Element("cognome");
			paziente_cognome.setText(datiPaziente.getCognome());
			paziente.addContent(paziente_cognome);

			//Cellulari
			Element cellulari = new Element("cellulari");

			//Ciclo di aggiunta dei numeri di cellulare
			Object elencoCellulari[] = datiPaziente.getCellulari();
			for (Object numeroCellulare: elencoCellulari)
			{
				//Creazione elemento cellulare
				Element cellulare = new Element("cellulare");
				//Aggiunta numero
				cellulare.setAttribute("numero",String.valueOf(numeroCellulare));
				//Aggiunta elemento a cellulari
				cellulari.addContent(cellulare);
			}

			paziente.addContent(cellulari);
		}

		//TipiInterventi
		Element tipiInterventi = new Element("tipiInterventi");

		for(int cont=0; cont<datiIntervento.countTipInterventi(); cont++)
		{
			Element tipoIntervento = new Element("tipoIntervento");
			//Aggiunta attributo nome
			tipoIntervento.setAttribute("nome", datiIntervento.getTipoIntervento(cont).getNome());
                        
                        /*INSERIRE LE PATOLOGIE DA QUI*/
                        Element patologie = new Element("patologie");
                        for(int contPat = 0; contPat < 
                                datiIntervento.getTipoIntervento(cont).getListaPatologie().size(); contPat++){
                            Element patologia = new Element("patologia");
                            //Aggiunta attributo nome patologia
                            patologia.setAttribute("codice", 
                                    datiIntervento.getTipoIntervento(cont).getListaPatologie().get(contPat).getCodice());
                            
                            Element nome = new Element("nome");
                            nome.setText(datiIntervento.getTipoIntervento(cont).getListaPatologie().get(contPat).getNome());
                            
                            Element gravita = new Element("gravita"); 
                            gravita.setText(String.valueOf(datiIntervento.getTipoIntervento(cont).getListaPatologie().get(contPat).getGravita()));
                       
                            patologia.addContent(nome);
                            patologia.addContent(gravita);
                            
                            patologie.addContent(patologia);
                        }//for su patologie
                        tipoIntervento.addContent(patologie);
                        
                        
			//Creazione Elementi valore rilevato e note con attributi
			//Element valoreRilevato = new Element("valoreRilevato");
			Element note = new Element("note");
			note.setText(datiIntervento.getTipoIntervento(cont).getNote());

                        
			//Attributo tempointervento non obbligatorio, sarà aggiunto da ADISys Mobile.
			//Aggiunta elementi a tipoIntervento
			//tipoIntervento.addContent(valoreRilevato);
			tipoIntervento.addContent(note);
                       
			//Aggiunta TipoIntervento a lista
			tipiInterventi.addContent(tipoIntervento);
		}

		//Creazione elemento log (vuoto)
		Element log = new Element("log");
		Element log_note= new Element("note");
		Element rilevazioni= new Element("rilevazioni");
		log.addContent(rilevazioni);
		log.addContent(log_note);



		//TODO aggiunge l'element a "interventi
		//ID
		nuovoIntervento.setAttribute(oraInizio);
		nuovoIntervento.setAttribute(id);
		//Operatore intervento
		nuovoIntervento.addContent(operatoreIntervento);
		//Data
		nuovoIntervento.addContent(data);
		//Luogo
		nuovoIntervento.addContent(luogo);
		//Paziente
		nuovoIntervento.addContent(paziente);
		//TipiInterventi
		nuovoIntervento.addContent(tipiInterventi);
		//Log
		nuovoIntervento.addContent(log);

		interventi.addContent(nuovoIntervento);
	}

	public String salvaSuFileXML(String cartella, String nomeFile, String percorsoXSD)
	{
		//Creazione e preparazione Document XML
		Document fileXMLInterscambio = new Document();
		fileXMLInterscambio.setRootElement(interventi);

		//TODO Controllo esistenza file/percorso
		File fCartella= new File(cartella);
		File fileXML = new File(cartella + "/" +nomeFile);
		File fileXSD = new File(percorsoXSD);

		//Creazione cartella se non esistente
		if(!(fCartella.exists())) fCartella.mkdir();

		String errLog=strutturaInterscambio.getString("REPORT SALVATAGGIO XML:");

		//Controllo esistenza file
		org.jdom2.output.XMLOutputter estrattore = new XMLOutputter();
		try 
		{
			estrattore.output(fileXMLInterscambio, new FileWriter(fileXML));
			System.out.println(strutturaInterscambio.getString("XML: SCRITTURA OK!"));
			errLog += strutturaInterscambio.getString("-SCRITTURA FILE XML OK.");
			
			errLog += java.text.MessageFormat.format(strutturaInterscambio.getString("{0}"), new Object[] {validaXML(fileXML, fileXSD)});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(strutturaInterscambio.getString("XML: SCRITTURA FALLITA!"));
			errLog += strutturaInterscambio.getString("-SCRITTURA FILE XML FALLITA.");
		}

		
		return errLog;
	}

	public String caricaDaFileXML(String percorsoXML, String schemaXsdImp)
	{
		//Stringa per feedback
		String errLog = "";
		//Controllo esistenza file XML
		File XML= new File(percorsoXML);
		File XSD= new File(schemaXsdImp);
		
		//Controllo esistenza file XSD
		if(!XSD.exists()) errLog += java.text.MessageFormat.format(strutturaInterscambio.getString("-ERRORE: FILE {0}NON ESISTENTE."), new Object[] {XSD.getName()}); 
			
		//Validazione
		errLog += java.text.MessageFormat.format(strutturaInterscambio.getString("- {0}"), new Object[] {validaXML(XML, XSD)});
		
		//Tentativo caricamento
        try {
        	SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(XML);
            interventi = doc.getRootElement();
            errLog+=java.text.MessageFormat.format(strutturaInterscambio.getString("- FILE {0} CARICATO CON SUCCESSO."), new Object[] {XML.getName()});
		} catch (JDOMException e) {
			// Auto-generated catch block
			errLog+=java.text.MessageFormat.format(strutturaInterscambio.getString("- ERRORE: IMPOSSIBILE ESEGUIRE IL PARSING DEL FILE {0}"), new Object[] {XML.getName()});
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			errLog+=java.text.MessageFormat.format(strutturaInterscambio.getString("- ERRORE: IMPOSSIBILE LEGGERE IL FILE {0}"), new Object[] {XML.getName()});
			e.printStackTrace();
		}
		
		return errLog;
	}
        
	public ArrayList<InterventoCompletoTO> getInterventiCompleti()
	{
		//Creazione Lista
		ArrayList<InterventoCompletoTO> listaInterventi= new ArrayList<>();
		
		for (Element intervento:interventi.getChildren())
		{
			//Creazione oggetto intervento
			InterventoCompletoTO i = new InterventoCompletoTO();
			
			//Trasferimento dati
			i.setID(intervento.getAttributeValue("id"));
			
			i.setOraInizioFmt(intervento.getAttributeValue("orainizio"), FORMATO_ORA_XML );
			i.setOraFineFmt(intervento.getAttributeValue("orafine"), FORMATO_ORA_XML );
			
			i.setIDInfermiere(intervento.getChild("operatoreIntervento").getAttributeValue("id"));
			
			i.setDataFmt(intervento.getChildText("data") , FORMATO_DATA_XML);
			
			i.setCitta(intervento.getChild("luogo").getAttributeValue("citta"));
			i.setCivico(intervento.getChild("luogo").getAttributeValue("indirizzo"));
			i.setCap(intervento.getChild("luogo").getAttributeValue("cap"));
			
			//Creazione paziente
			PazienteTO p = new PazienteTO();
			//Aggiunta dati paziente
			p.setID(Integer.valueOf(intervento.getChild("paziente").getAttributeValue("id")));
			p.setNome(intervento.getChild("paziente").getChildText("nome"));
			p.setCognome(intervento.getChild("paziente").getChildText("cognome"));
			
			//Aggiunta numeri di cellulare paziente se ce ne sono
			if(intervento.getChild("paziente").getChild("cellulari").getChildren()!=null)
				for(Element cellulare:intervento.getChild("paziente").getChild("cellulari").getChildren())
				{
					p.addCellulare(cellulare.getAttributeValue("numero"));
				}
			//Aggiunta paziente all'intervento
			i.setPaziente(p);
			
			//Aggiunta tipi interventi
			for(Element tipoIntervento: intervento.getChild("tipiInterventi").getChildren() )
			{
				TipoIntervento t= new TipoIntervento();
				t.setNome(tipoIntervento.getAttributeValue("nome"));
				ArrayList<PatologiaTO> listaPatologie = new ArrayList<>();
                                for(Element patologia: tipoIntervento.getChild("patologie").getChildren() ) {
                                    PatologiaTO pat = new PatologiaTO();
                                    pat.setCodice(patologia.getAttributeValue("codice"));
                                    pat.setNome(patologia.getChildText("nome"));
                                    pat.setGravita(patologia.getChildText("gravita"));
                                   listaPatologie.add(pat);
                                }
                                t.setListaPatologie(listaPatologie);
                                t.setValoreRilevato(tipoIntervento.getChildText("valoreRilevato"));
				t.setTempoIntervento(tipoIntervento.getChild("valoreRilevato").getAttributeValue("tempoIntervento"));
				t.setNote(tipoIntervento.getChildText("note"));
                                
                                //devo aggiungere le patologie
 				i.addTipoIntervento(t);
                                
			}
			
			//Aggiunta Log/Rilevazioni
			for(Element rilevazione: intervento.getChild("log").getChild("rilevazioni").getChildren())
			{
				Rilevazione r = new Rilevazione();
				
				//Lettura e inserimento Timestamp
				String data = rilevazione.getChild("timestamp").getAttributeValue("data");
				String ora = rilevazione.getChild("timestamp").getAttributeValue("ora");
				String timestamp = data + java.text.MessageFormat.format(strutturaInterscambio.getString(" {0}"), new Object[] {ora});
				String formatoTimestamp = FORMATO_DATA_XML + java.text.MessageFormat.format(strutturaInterscambio.getString(" {0}"), new Object[] {FORMATO_ORA_XML});
				r.setTimestampFromString(timestamp, formatoTimestamp);
				
				//GPS
				r.setGpsLatitude(Double.valueOf(rilevazione.getChild("gps").getAttributeValue("latitudine")));
				r.setGpsLongitude(Double.valueOf(rilevazione.getChild("gps").getAttributeValue("longitudine")));
				r.setGpsAltitude(Double.valueOf(rilevazione.getChild("gps").getAttributeValue("altitudine")));
				r.setGpsAccuracy(Double.valueOf(rilevazione.getChild("gps").getAttributeValue("accuratezza")));
				
				//ACCELEROMETRO
				r.setAccX(Double.valueOf(rilevazione.getChild("accelerometro").getAttributeValue("valorex")));
				r.setAccY(Double.valueOf(rilevazione.getChild("accelerometro").getAttributeValue("valorey")));
				r.setAccZ(Double.valueOf(rilevazione.getChild("accelerometro").getAttributeValue("valorez")));
				
				//Aggiunta rilevazione all'intervento
				i.addLog(r);
			}
			
			i.setNote(intervento.getChild("log").getChildText("note"));
			
			//TODO:Aggiunta dell'intervento alla ista
			listaInterventi.add(i);
		}
		return listaInterventi;
	}

	public static String int2string6(int numero)
	{
		return String.format("%06d", numero);
	}

	public static String validaXML(File XML, File XSD)
	{
		Source xmlFile = new StreamSource(XML);
		Source schemaFile = new StreamSource(XSD);

		if(!XSD.exists()) return java.text.MessageFormat.format(strutturaInterscambio.getString("ERRORE: FILE {0} NON TROVATO."), new Object[] {XSD.getName()});
		if(!XML.exists()) return java.text.MessageFormat.format(strutturaInterscambio.getString("ERRORE: FILE {0} NON TROVATO."), new Object[] {XML.getName()});
		
		try 
		{
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);

			return (XML.getName() + strutturaInterscambio.getString(" È VALIDO"));
		} 
		catch (SAXException e) 
		{
			return (java.text.MessageFormat.format(strutturaInterscambio.getString("IL FILE {0} NON È VALIDO ({1})"), new Object[] {XML.getName(), e.getLocalizedMessage()}));
		} 
		catch (IOException e) 
		{
			return strutturaInterscambio.getString("ERRORE: FILE NON TROVATO");
		}

	}
}

