/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.boundary;

import business.intervento.PatologieTipoIntervento;
import business.intervento.TipoIntervento;
import adisys.server.strumenti.ComboItem;
import adisys.server.strumenti.DateFormatConverter;
import adisys.server.strumenti.VariableTableModel;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoTO;
import business.patologia.PatologiaTO;
import messaggistica.MainException;
import integration.dao.InfermiereMySqlDAO;
import integration.dao.InterventoMySqlDAO;
import integration.dao.PatologiaMySqlDAO;
import integration.dao.PazienteMySqlDAO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.text.ParseException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import presentation.FrontController;
import presentation.RequestManager;
import adisys.server.strumenti.Record;
import messaggistica.GMessage;

/**
*
* @author Gianmarco Divittorio
* @author Nicola Gazzilli
*/
public class EditorInterventi extends javax.swing.JDialog implements Boundary {
    private int MODIFICA_DATA = 0;
    
    private static String NOME_COLONNA_CODICE;
    private static String NOME_COLONNA_NOME;
    private static String NOME_COLONNA_GRAVITA;
    private PatologieTipoIntervento listaPatologieTipoIntervento = null;
    private static ResourceBundle editorInterventi = ResourceBundle.getBundle("adisys/server/property/EditorInterventi");
    private static final String FORMATO_DATA_TABELLA = "yyyy-MM-dd";
    private static final String FORMATO_DATA_INPUT = DateFormatConverter.getFormatData();
    private static final String FORMATO_ORA_TABELLA = "HH:mm:ss";
    private static final String formatoDataInput = "dd/MM/yyyy";
    private static final String formatoOraInput = "HH.mm";
    private static final String FORMATO_ORA_INPUT = DateFormatConverter.getFormatOra();
    private static final int PRIMA_RIGA_DTM = 0; //Prma riga del defaulttablemodel
    private static final int COLONNA_NOME_TIPO_DTM = 0; //Prma riga del defaulttablemodel
    private static final int COLONNA_NOTE_TIPO_DTM = 1; //Prma riga del defaulttablemodel
    
    private final String modAggiungi = editorInterventi.getString("AGGIUNGI");
    private final String modModifica = editorInterventi.getString("MODIFICA N.");
    private final Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA_INPUT);
    private final String dataCorrente = dateFormat.format(c.getTime());
    private long currentLongDate = DateFormatConverter.dateString2long(dataCorrente, 
            FORMATO_DATA_INPUT);

    private static Pianificatore pianificatore;
    
    public static void setPianificatore(Pianificatore pian){
        pianificatore = pian;
    }
    
   //Costanti di vincolo
    private static final int lunghezzaMassimaCitta = 30;
    private static final int lunghezzaMassimaCivico = 30;
    private static final int lunghezzaMassimaCap = 5; //Nuova vers 2
    private static final int lunghezzaMassimaTipo = 300;
    private static final int lunghezzaMassimaNote = 300;	

    private FrontController FC;
    
    private String idIntervento;
    private String idInfermiere;
    private String idPaziente;
    private String dataInt;
    private String oraInt;
    private String civico;
    private String citta;
    private String cap;
    private DefaultTableModel modelloTabellaTipi;
    private ArrayList<TipoIntervento> listaTipi;
    private int errori = 0;
    
     public static void setResourceBundle(String path, Locale locale){
             editorInterventi = ResourceBundle.getBundle(path, locale);
     }
    
	/**
	 * Creates new form EditorInterventi
	 */
	public EditorInterventi(java.awt.Frame parent, boolean modal) throws MainException {
		super(parent, modal);
		initComponents();
		aggiornaTabelle();
		setModalitaAggiungi();
                fieldCombo();
                setDate();
                listaPatologieTipoIntervento = new PatologieTipoIntervento();
        }
        
        public EditorInterventi(){
            
        }
        
        public void setModelloTipiIntervento(DefaultTableModel modello){
        	modelloTipiIntervento = modello;
                tabellaTipiIntervento.setModel(modelloTipiIntervento);
        }
        
        public void setColumnModelPatologieTipoIntervento(){
            
            TableColumn col = tabellaPatologieTipoIntervento.getColumnModel().getColumn(
                    tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(PatologiaMySqlDAO.NOME_COLONNA_ID));
            col.setMinWidth(0);
            col.setMaxWidth(0);
            col.setPreferredWidth(0);
        }
        
        public void setPatologieTipoIntervento(PatologieTipoIntervento newLista){
            this.listaPatologieTipoIntervento = newLista;
        }
        
        public DefaultTableModel modelTabellaTipiIntervento(){
            String[] column = new String[tabellaTipiIntervento.getColumnCount()];
            for(int i = 0; i < tabellaTipiIntervento.getColumnCount(); i++){
                column[i] = tabellaTipiIntervento.getColumnName(i);
            }
            int numRows = tabellaTipiIntervento.getRowCount();
            return new DefaultTableModel(column, numRows - 1);
        }
        
        public String[] columnModelTabellaPatologieTipoIntervento(){
            String[] column = new String[tabellaPatologieTipoIntervento.getColumnCount()];
            for(int i = 0; i < tabellaPatologieTipoIntervento.getColumnCount(); i++){
                column[i] = tabellaPatologieTipoIntervento.getColumnName(i);
            }
            return column;
        }
        
         public PatologieTipoIntervento getPatologieTipoIntervento(){
            return listaPatologieTipoIntervento;
        }
        
        public void addValueTabellaPatologie(Object o, int riga, int colonna){
        	tabellaPatologieTipoIntervento.setValueAt(o, riga, colonna);
        }
        
        public void setModelTabellaPatologie(DefaultTableModel modello){
        	tabellaPatologieTipoIntervento.setModel(modello);
        }
        
        public TableModel getModelTabellaPatologie(){
        	return tabellaPatologieTipoIntervento.getModel();
        }
        
        public JTable getTabellaTipiIntervento(){
            return tabellaTipiIntervento;
        }
        
	/**
	 * Metodo che inizializza il form insieme al costruttore
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        immagineTestataInterventi = new javax.swing.JLabel();
        pannelloDati = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pulsanteConferma = new javax.swing.JButton();
        txCitta = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txCivico = new javax.swing.JTextField();
        txCAP = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        txPazienteSelezionato = new javax.swing.JFormattedTextField();
        txInfermiereSelezionato = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        labelIntervento = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabellaTipiIntervento = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txOra = new javax.swing.JFormattedTextField();
        pulsanteRimuoviTipo = new javax.swing.JButton();
        txNomePaziente = new javax.swing.JTextField();
        txNomeInfermiere = new javax.swing.JTextField();
        pulsanteAggiungiTipo = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabellaPatologieTipoIntervento = new javax.swing.JTable();
        pulsanteModificaPatologie = new javax.swing.JButton();
        pulsanteModificaGravita = new javax.swing.JButton();
        pulsanteRimuoviPatologia = new javax.swing.JButton();
        txData = new com.toedter.calendar.JDateChooser();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabellaInterventi = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaPazienti = new javax.swing.JTable();
        labelPazienti = new javax.swing.JLabel();
        pulsanteImpostaPaziente = new javax.swing.JButton();
        pulsanteEditorPazienti = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        pulsanteAggiungi = new javax.swing.JButton();
        pulsanteModifica = new javax.swing.JButton();
        pulsanteCancella = new javax.swing.JButton();
        pulsanteCancellaTutti = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaInfermieri = new javax.swing.JTable();
        labelInfermieri = new javax.swing.JLabel();
        pulsanteImpostaInfermiere = new javax.swing.JButton();
        pulsanteEditorInfermieri = new javax.swing.JButton();
        txInterventoSelezionato = new javax.swing.JFormattedTextField();
        txModalita = new javax.swing.JTextField();
        labelInterventi = new javax.swing.JLabel();
        comboInfermieri = new javax.swing.JComboBox();
        txRicDataDal = new com.toedter.calendar.JDateChooser();
        txRicDataAl = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pulsanteResettaRicerca = new javax.swing.JButton();
        pulsanteEsci = new javax.swing.JButton();
        pulsanteHome = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(editorInterventi.getString("TITLE")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        immagineTestataInterventi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/Interventi.png"))); // NOI18N
        immagineTestataInterventi.setName(""); // NOI18N

        pannelloDati.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText(editorInterventi.getString("DATA(GG/MM/AAAA)")); // NOI18N
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        pulsanteConferma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/iconainserisciazzurra.png"))); // NOI18N
        pulsanteConferma.setText(editorInterventi.getString("CONFERMA INSERIMENTO/MODIFICA")); // NOI18N
        pulsanteConferma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteConfermaActionPerformed(evt);
            }
        });

        txCitta.setToolTipText(editorInterventi.getString("SCRIVI QUI LA CITTÀ IN CUI SI SVOLGERÀ L'INTERVENTO")); // NOI18N

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(editorInterventi.getString("CITTA'")); // NOI18N
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(editorInterventi.getString("CIVICO")); // NOI18N
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txCivico.setToolTipText(editorInterventi.getString("SCRIVI QUI L'INDIRIZZO IN CUI SI SVOLGERÀ L'INTERVENTO")); // NOI18N

        txCAP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("00000"))));
        txCAP.setToolTipText(editorInterventi.getString("SCRIVI QUI IL CAP DELLA CITTÀ IN CUI SI SVOLGERÀ L'INTERVENTO")); // NOI18N

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText(editorInterventi.getString("ORA (HH.MM)")); // NOI18N
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txPazienteSelezionato.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txPazienteSelezionato.setEnabled(false);

        txInfermiereSelezionato.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txInfermiereSelezionato.setEnabled(false);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText(editorInterventi.getString("PAZIENTE")); // NOI18N
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText(editorInterventi.getString("INFERMIERE")); // NOI18N
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        labelIntervento.setFont(labelIntervento.getFont().deriveFont(labelIntervento.getFont().getStyle() | java.awt.Font.BOLD, 14));
        labelIntervento.setForeground(new java.awt.Color(255, 255, 255));
        labelIntervento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIntervento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Azzurra.png"))); // NOI18N
        labelIntervento.setText(editorInterventi.getString("DATI INTERVENTO")); // NOI18N
        labelIntervento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tabellaTipiIntervento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Intervento", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabellaTipiIntervento.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaTipiIntervento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaTipiInterventoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabellaTipiIntervento);
        tabellaTipiIntervento.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tabellaTipiIntervento.getColumnModel().getColumn(0).setResizable(false);
        tabellaTipiIntervento.getColumnModel().getColumn(1).setResizable(false);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText(editorInterventi.getString("CAP")); // NOI18N
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel2.setText(editorInterventi.getString("EDITOR DEI TIPI DI INTERVENTO (SPECIFICARNE ALMENO UNO)")); // NOI18N

        txOra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH.mm"))));

        pulsanteRimuoviTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/remove.png"))); // NOI18N
        pulsanteRimuoviTipo.setToolTipText(editorInterventi.getString("ELIMINA IL TIPO DI INTERVENTO SELEZIONATO")); // NOI18N
        pulsanteRimuoviTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteRimuoviTipoActionPerformed(evt);
            }
        });

        txNomePaziente.setEditable(false);

        txNomeInfermiere.setEditable(false);

        pulsanteAggiungiTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/add.png"))); // NOI18N
        pulsanteAggiungiTipo.setToolTipText(editorInterventi.getString("AGGIUNGI UN NUOVO TIPO DI INTERVENTO (INSULINA, PRESSIONE...)")); // NOI18N
        pulsanteAggiungiTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteAggiungiTipoActionPerformed(evt);
            }
        });

        tabellaPatologieTipoIntervento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane4.setViewportView(tabellaPatologieTipoIntervento);
        tabellaPatologieTipoIntervento.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        pulsanteModificaPatologie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/modifica.png"))); // NOI18N
        pulsanteModificaPatologie.setToolTipText(editorInterventi.getString("MODIFICA LE PATOLOGIE ASSOCIATE AL TIPO DI INTERVENTO SELEZIONATO")); // NOI18N
        pulsanteModificaPatologie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteModificaPatologieActionPerformed(evt);
            }
        });

        pulsanteModificaGravita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/modifica.png"))); // NOI18N
        pulsanteModificaGravita.setToolTipText(editorInterventi.getString("MODIFICA LA GRAVITÀ DELLA PATOLOGIA SELEZIONATA DEL TIPO DI INTERVENTO SELEZIONATO")); // NOI18N
        pulsanteModificaGravita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteModificaGravitaActionPerformed(evt);
            }
        });

        pulsanteRimuoviPatologia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/remove.png"))); // NOI18N
        pulsanteRimuoviPatologia.setToolTipText(editorInterventi.getString("ELIMINA LA PATOLOGIA SELEZIONATA DEL TIPO DI INTERVENTO SELEZIONATO")); // NOI18N
        pulsanteRimuoviPatologia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteRimuoviPatologiaActionPerformed(evt);
            }
        });

        txData.setDateFormatString(editorInterventi.getString("DD/MM/YYYY")); // NOI18N

        javax.swing.GroupLayout pannelloDatiLayout = new javax.swing.GroupLayout(pannelloDati);
        pannelloDati.setLayout(pannelloDatiLayout);
        pannelloDatiLayout.setHorizontalGroup(
            pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannelloDatiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pannelloDatiLayout.createSequentialGroup()
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pannelloDatiLayout.createSequentialGroup()
                                .addComponent(txCitta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txCAP, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txCivico)))
                    .addGroup(pannelloDatiLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pannelloDatiLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txData, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txOra, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pannelloDatiLayout.createSequentialGroup()
                                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txPazienteSelezionato)
                                    .addComponent(txInfermiereSelezionato, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txNomePaziente)
                                    .addComponent(txNomeInfermiere, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(pannelloDatiLayout.createSequentialGroup()
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pulsanteAggiungiTipo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pulsanteRimuoviTipo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pulsanteModificaPatologie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pulsanteModificaGravita, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pulsanteRimuoviPatologia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pannelloDatiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelIntervento, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pulsanteConferma, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        pannelloDatiLayout.setVerticalGroup(
            pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pannelloDatiLayout.createSequentialGroup()
                .addComponent(labelIntervento)
                .addGap(5, 5, 5)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txPazienteSelezionato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNomePaziente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txInfermiereSelezionato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNomeInfermiere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txOra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txCitta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txCAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txCivico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pannelloDatiLayout.createSequentialGroup()
                        .addComponent(pulsanteAggiungiTipo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pulsanteRimuoviTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pulsanteModificaPatologie, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pannelloDatiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pannelloDatiLayout.createSequentialGroup()
                        .addComponent(pulsanteRimuoviPatologia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pulsanteModificaGravita, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pulsanteConferma)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabellaInterventi.setAutoCreateRowSorter(true);
        tabellaInterventi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabellaInterventi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaInterventi.getTableHeader().setReorderingAllowed(false);
        tabellaInterventi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabellaInterventiKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tabellaInterventi);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabellaPazienti.setAutoCreateRowSorter(true);
        tabellaPazienti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabellaPazienti.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaPazienti.getTableHeader().setReorderingAllowed(false);
        tabellaPazienti.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(tabellaPazienti);

        labelPazienti.setFont(labelPazienti.getFont().deriveFont(labelPazienti.getFont().getStyle() | java.awt.Font.BOLD, 14));
        labelPazienti.setForeground(new java.awt.Color(255, 255, 255));
        labelPazienti.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPazienti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Verde.png"))); // NOI18N
        labelPazienti.setText(editorInterventi.getString("PAZIENTI")); // NOI18N
        labelPazienti.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        pulsanteImpostaPaziente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/miniinserisciverde.png"))); // NOI18N
        pulsanteImpostaPaziente.setText(editorInterventi.getString("IMPOSTA PAZIENTE SELEZIONATO")); // NOI18N
        pulsanteImpostaPaziente.setToolTipText(editorInterventi.getString("IMPOSTA IL PAZIENTE SELEZIONATO NEL FORM DELL'INTERVENTO")); // NOI18N
        pulsanteImpostaPaziente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteImpostaPaziente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteImpostaPazienteActionPerformed(evt);
            }
        });

        pulsanteEditorPazienti.setFont(pulsanteEditorPazienti.getFont().deriveFont(pulsanteEditorPazienti.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteEditorPazienti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaPazienti.png"))); // NOI18N
        pulsanteEditorPazienti.setText(editorInterventi.getString("MODIFICA PAZIENTI")); // NOI18N
        pulsanteEditorPazienti.setToolTipText(editorInterventi.getString("MODIFICA L'ELENCO DEI PAZIENTI")); // NOI18N
        pulsanteEditorPazienti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteEditorPazientiActionPerformed(evt);
            }
        });

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, 12));
        jLabel1.setText(editorInterventi.getString("INTERVENTI:")); // NOI18N
        jToolBar1.add(jLabel1);

        pulsanteAggiungi.setFont(pulsanteAggiungi.getFont().deriveFont(pulsanteAggiungi.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteAggiungi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaAggiungi.png"))); // NOI18N
        pulsanteAggiungi.setText(editorInterventi.getString("AGGIUNGI")); // NOI18N
        pulsanteAggiungi.setFocusable(false);
        pulsanteAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteAggiungiActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteAggiungi);

        pulsanteModifica.setFont(pulsanteModifica.getFont().deriveFont(pulsanteModifica.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteModifica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaModifica.png"))); // NOI18N
        pulsanteModifica.setText(editorInterventi.getString("MODIFICA")); // NOI18N
        pulsanteModifica.setFocusable(false);
        pulsanteModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteModificaActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteModifica);

        pulsanteCancella.setFont(pulsanteCancella.getFont().deriveFont(pulsanteCancella.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteCancella.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaCancella.png"))); // NOI18N
        pulsanteCancella.setText(editorInterventi.getString("CANCELLA")); // NOI18N
        pulsanteCancella.setFocusable(false);
        pulsanteCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteCancellaActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteCancella);

        pulsanteCancellaTutti.setFont(pulsanteCancellaTutti.getFont().deriveFont(pulsanteCancellaTutti.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteCancellaTutti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaCancellaTutti.png"))); // NOI18N
        pulsanteCancellaTutti.setText(editorInterventi.getString("CANCELLA TUTTI")); // NOI18N
        pulsanteCancellaTutti.setFocusable(false);
        pulsanteCancellaTutti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteCancellaTuttiActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteCancellaTutti);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labelPazienti)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pulsanteImpostaPaziente, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pulsanteEditorPazienti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPazienti)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pulsanteImpostaPaziente)
                    .addComponent(pulsanteEditorPazienti, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabellaInfermieri.setAutoCreateRowSorter(true);
        tabellaInfermieri.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabellaInfermieri.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaInfermieri.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tabellaInfermieri);

        labelInfermieri.setFont(labelInfermieri.getFont().deriveFont(labelInfermieri.getFont().getStyle() | java.awt.Font.BOLD, 14));
        labelInfermieri.setForeground(new java.awt.Color(255, 255, 255));
        labelInfermieri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInfermieri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Rossa.png"))); // NOI18N
        labelInfermieri.setText(editorInterventi.getString("INFERMIERI")); // NOI18N
        labelInfermieri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        pulsanteImpostaInfermiere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/miniinseriscirosso.png"))); // NOI18N
        pulsanteImpostaInfermiere.setText(editorInterventi.getString("IMPOSTA INFERMIERE SELEZIONATO")); // NOI18N
        pulsanteImpostaInfermiere.setToolTipText(editorInterventi.getString("IMPOSTA L'INFERMIERE SELEZIONATO NEL FORM DEGLI INTERVENTI")); // NOI18N
        pulsanteImpostaInfermiere.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteImpostaInfermiere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteImpostaInfermiereActionPerformed(evt);
            }
        });

        pulsanteEditorInfermieri.setFont(pulsanteEditorInfermieri.getFont().deriveFont(pulsanteEditorInfermieri.getFont().getStyle() | java.awt.Font.BOLD, 12));
        pulsanteEditorInfermieri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaInfermieri.png"))); // NOI18N
        pulsanteEditorInfermieri.setText(editorInterventi.getString("MODIFICA INFERMIERI")); // NOI18N
        pulsanteEditorInfermieri.setToolTipText(editorInterventi.getString("MODIFICA L'ELENCO DEGLI INFERMIERI")); // NOI18N
        pulsanteEditorInfermieri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteEditorInfermieriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labelInfermieri)
                .addGap(0, 156, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pulsanteImpostaInfermiere, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pulsanteEditorInfermieri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labelInfermieri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pulsanteImpostaInfermiere)
                    .addComponent(pulsanteEditorInfermieri, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        txInterventoSelezionato.setEditable(false);
        txInterventoSelezionato.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        txModalita.setEditable(false);
        txModalita.setText(editorInterventi.getString("OPERAZIONE")); // NOI18N

        labelInterventi.setFont(labelInterventi.getFont().deriveFont(labelInterventi.getFont().getStyle() | java.awt.Font.BOLD, 14));
        labelInterventi.setForeground(new java.awt.Color(255, 255, 255));
        labelInterventi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInterventi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Azzurra.png"))); // NOI18N
        labelInterventi.setText(editorInterventi.getString("INTERVENTI")); // NOI18N
        labelInterventi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        comboInfermieri.setToolTipText(editorInterventi.getString("SELEZIONA L'INFERMIERE PER CUI FILTRARE GLI INTERVENTI")); // NOI18N
        comboInfermieri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboInfermieriActionPerformed(evt);
            }
        });

        txRicDataDal.setToolTipText(editorInterventi.getString("SELEZIONA UNA DATA PER FILTRARE GLI INTERVENTI")); // NOI18N
        txRicDataDal.setDateFormatString(editorInterventi.getString("DD/MM/YYYY")); // NOI18N
        txRicDataDal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txRicDataDalPropertyChange(evt);
            }
        });

        txRicDataAl.setToolTipText(editorInterventi.getString("SELEZIONA UNA DATA PER FILTRARE GLI INTERVENTI")); // NOI18N
        txRicDataAl.setDateFormatString(editorInterventi.getString("DD/MM/YYYY")); // NOI18N
        txRicDataAl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txRicDataAlPropertyChange(evt);
            }
        });

        jLabel3.setText(editorInterventi.getString("FILTRA INTERVENTI DAL:")); // NOI18N

        jLabel4.setText(editorInterventi.getString("AL:")); // NOI18N

        pulsanteResettaRicerca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/reset.png"))); // NOI18N
        pulsanteResettaRicerca.setToolTipText(editorInterventi.getString("RESETTA I FILTRI DI RICERCA")); // NOI18N
        pulsanteResettaRicerca.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteResettaRicerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteResettaRicercaActionPerformed(evt);
            }
        });

        pulsanteEsci.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/arresta.png"))); // NOI18N
        pulsanteEsci.setText(editorInterventi.getString("ABBANDONA ADISYS")); // NOI18N
        pulsanteEsci.setToolTipText(editorInterventi.getString("ESCI DA ADISYS")); // NOI18N
        pulsanteEsci.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteEsci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteEsciActionPerformed(evt);
            }
        });

        pulsanteHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/home.png"))); // NOI18N
        pulsanteHome.setText(editorInterventi.getString("HOME")); // NOI18N
        pulsanteHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteHomeActionPerformed(evt);
            }
        });

        jLabel6.setText(editorInterventi.getString("INFO")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(immagineTestataInterventi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(104, 104, 104)
                .addComponent(txModalita, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txInterventoSelezionato, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelInterventi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboInfermieri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txRicDataDal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txRicDataAl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pulsanteResettaRicerca))
                    .addComponent(jScrollPane7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pulsanteEsci)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pulsanteHome))
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pannelloDati, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(immagineTestataInterventi)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboInfermieri, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelInterventi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txInterventoSelezionato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txModalita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txRicDataDal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pulsanteResettaRicerca)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txRicDataAl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 31, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pulsanteEsci)
                            .addComponent(pulsanteHome)))
                    .addComponent(pannelloDati, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

      private void fieldCombo() throws MainException{
          
        FC = RequestManager.getFCInstance();
	ArrayList<ComboItem> lista = (ArrayList<ComboItem>) FC.processRequest("visualizzaElenco", null);
        comboInfermieri.addItem(new ComboItem("", editorInterventi.getString("FILTRA INTERVENTI PER:")));
        for (ComboItem objA : lista) {
            comboInfermieri.addItem(objA);
        }
      }
      
      private void setDate(){
          txRicDataDal.setDate(new Date(currentLongDate));
          txRicDataAl.setDate(new Date(currentLongDate));
      }
      
      private void setDate(String date){
          long dateInsert = DateFormatConverter.dateString2long(date, formatoDataInput);
          txRicDataAl.setDate(new Date(dateInsert));
      }
        
        private void ricercaFiltrata() throws MainException {                                                      
		int id = -1;
                String dataDal = "";
                String dataAl = "";
                if(txRicDataDal.getDate() != null){
                    SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_INPUT);
                    dataDal = formatoData.format(txRicDataDal.getDate());
                }
                
                if(txRicDataAl.getDate() != null){
                    SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_INPUT);
                    dataAl = formatoData.format(txRicDataAl.getDate());
                }
			String strId = ((ComboItem) comboInfermieri.getSelectedItem()).getValue();
			
			if(!strId.equals(""))
				id = Integer.parseInt(strId);
                        
                          FC = RequestManager.getFCInstance();
                          ArrayList<Record<String, Object>> params = new ArrayList<>();
                          /*InterventoFiltratoTO to = new InterventoFiltratoTO(id, dataDal, dataAl);
                          params.add(new Record<String, Object>("business.intervento.InterventoFiltratoTO", to));*/
                          params.add(new Record<String, Object>("java.lang.Integer", id));
                          params.add(new Record<String, Object>("java.lang.String", dataDal));
                          params.add(new Record<String, Object>("java.lang.String", dataAl));
                          AbstractTableModel modelAdisys = (AbstractTableModel) 
                                  FC.processRequest("visualizzaTabellaInterventiInfermiere", params);
                          tabellaInterventi.setModel(modelAdisys);
                          tabellaInterventi = verificaTabellaPiena(tabellaInterventi, editorInterventi.getString("INTERVENTO"));
                        
	} 
        
	private void pulsanteImpostaPazienteActionPerformed(java.awt.event.ActionEvent evt) {                                                        
		
		int riga = tabellaPazienti.getSelectedRow();

		//Verifica selezione paziente
		if (riga==-1)
		{
			//Caso selezione nulla
			GMessage.message_error(editorInterventi.getString("ERRORE:NESSUN PAZIENTE SELEZIONATO."));
		}
		else
		{
			//Caso selezione valida

			//recupero dei numeri di colonna
			int colonnaID=tabellaPazienti.getColumn(PazienteMySqlDAO.NOME_COLONNA_ID).getModelIndex();
			int colonnaNome=tabellaPazienti.getColumn(PazienteMySqlDAO.NOME_COLONNA_NOME).getModelIndex();
			int colonnaCognome=tabellaPazienti.getColumn(PazienteMySqlDAO.NOME_COLONNA_COGNOME).getModelIndex();

			//Set della variabile relativa al paziente selezionato
			txPazienteSelezionato.setValue(tabellaPazienti.getValueAt(riga, colonnaID));

                        //Set campo nomepaziente
                        String tNome = String.valueOf(tabellaPazienti.getValueAt(riga, colonnaNome));
                        String tCognome = String.valueOf(tabellaPazienti.getValueAt(riga, colonnaCognome));
                        txNomePaziente.setText(tNome + java.text.MessageFormat.format(
                                editorInterventi.getString(" {0}"), new Object[] {tCognome}));
                        
			//Notifica
			String notifica = editorInterventi.getString("E' STATO SELEZIONATO IL PAZIENTE N.");
			
                                
                        notifica += txPazienteSelezionato.getText() + editorInterventi.getString(".");
			notifica += java.text.MessageFormat.format(editorInterventi.getString("NOME: {0}"), new Object[] {tNome});
			notifica += java.text.MessageFormat.format(editorInterventi.getString("COGNOME: {0}"), new Object[] {tCognome});

			GMessage.information(notifica);
                }
		
	}                                                       
/*//ME1: jcombobox
	private void comboInfermieriActionPerformed(java.awt.event.ActionEvent evt) {                                                        
	
	}                                                       
	*/
   
    private void pulsanteConfermaActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        errori = 0;
        if (txModalita.getText().equals(modAggiungi)) {
            //Caso nuovo intervento   
            if(datiValidi()){
                 if(GMessage.confirm(editorInterventi.getString("CREARE IL NUOVO INTERVENTO")) == 
                       JOptionPane.YES_OPTION){
                     InterventoTO to;
                    try {
                        to = getValueJustInsert();
                        ArrayList<Record<String, Object>> params = new ArrayList<>();
                        params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                        try {
                            boolean ok = (boolean) FC.processRequest("creaIntervento", params);
                            if(ok){
                            //Aggiunta al database
				String messaggio = 
                                        editorInterventi.getString("INTERVENTO CREATO CON SUCCESSO");
                                pianificatore.setDate(dataInt);
                                setDate(dataInt);
				ricercaFiltrata();
                                GMessage.information(messaggio);
                                setModalitaAggiungi();
                            } else {
				//Caso infermiere non aggiunto
				String messaggio = 
                                        editorInterventi.getString(
                                        "SI È VERIFICATO UN ERRORE, IMPOSSIBILE CREARE L'INTERVENTO");
				GMessage.message_error(messaggio);
                            }
                        //setAggiungiInfermiere();
                        } catch (MainException ex) {
                            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                    }				
                }
            }
        } else if (txModalita.getText().equals(modModifica)) {
            //Caso modifica intervento
            idIntervento = txInterventoSelezionato.getText();

            //chiamata controller
            //Caso nuovo intervento   
            if(datiValidi()){
                  if(GMessage.confirm(editorInterventi.getString("SEI SICURO DI MODIFICARE")) == 
                       JOptionPane.YES_OPTION){
                        InterventoTO to;
                    try {
                        to = getValueJustUpdate();
                        ArrayList<Record<String, Object>> params = new ArrayList<>();
                        params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                        try {
                            boolean ok = (boolean) FC.processRequest("modificaIntervento", params);
                            if(ok){
                            //Aggiunta al database
				String messaggio = 
                                        editorInterventi.getString("INTERVENTO MODIFICATO CON SUCCESSO");
                                pianificatore.setDate(dataInt);
                                setDate(dataInt);
				ricercaFiltrata();
                                GMessage.information(messaggio);
                                setModalitaAggiungi();
                            } else {
				//Caso infermiere non aggiunto
				String messaggio= editorInterventi.getString(
                                        "SI È VERIFICATO UN ERRORE, IMPOSSIBILE MODIFICARE L'INTERVENTO");
				GMessage.message_error(messaggio);
                            }
                        //setAggiungiInfermiere();
                        } catch (MainException ex) {
                            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }   catch (ParseException ex) {
                        Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                    }
						
                }//if su dati validi 
            }//IF SU JOPTION
        }
    }                                                

     /**
	 * Restituisce i valori dei campi appena inseriti
	 * @return una struttura dati di String con i valori appena inseriti
	 * @throws ParseException 
	*/
	private InterventoTO getValueJustInsert() throws ParseException{
		
		InterventoTO to = setDatiIntervento();
                return to;
		
	}
        
        /**
	 * Restituisce i valori dei campi appena inseriti
	 * @return una struttura dati di String con i valori appena inseriti
	 * @throws ParseException 
	*/
	private InterventoTO getValueJustUpdate() throws ParseException{
            InterventoTO to = setDatiIntervento();
            to.setID(idIntervento);
            return to;	
	}
        
        private InterventoTO setDatiIntervento(){
            InterventoTO to = new InterventoTO();
            to.setIDPaziente(idPaziente);
            to.setIDInfermiere(idInfermiere);
            to.setCitta(citta);
            to.setOraInizio(oraInt);
                
            //TODO Aggiunta tipi intervento
            to.setCivico(civico);
            to.setCap(cap);
            to.setData(dataInt);
            for(TipoIntervento t:listaTipi) to.addTipoIntervento(t);
            System.out.println(to);
            return to;
        }
        
        public boolean datiValidi()
	{
                //RECUPERO DATI DAL MODULO
                idPaziente = txPazienteSelezionato.getText();
                idInfermiere = txInfermiereSelezionato.getText();

                citta = txCitta.getText();
                civico = txCivico.getText();
                cap = txCAP.getText();

                dataInt = "";
                if(txData.getDate() != null){
                    SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_INPUT);
                    dataInt = formatoData.format(txData.getDate());
                }
        
                oraInt = txOra.getText();

                modelloTabellaTipi = (DefaultTableModel) tabellaTipiIntervento.getModel();

		String messaggio = editorInterventi.getString("ATTENZIONE");
		if(idPaziente.isEmpty()){
                    errori++;
			messaggio += editorInterventi.getString("NESSUN PAZIENTE SELEZIONATO");
                }
                
                if(idInfermiere.isEmpty()){
                    errori++;
			messaggio += editorInterventi.getString("NESSUN INFERMIERE SELEZIONATO");
                }
                if (citta.isEmpty())
		{
			errori++;
			messaggio += editorInterventi.getString("NOME CITTA VUOTO");
		} else if(citta.length() > lunghezzaMassimaCitta){
                        errori++;
                        messaggio += editorInterventi.getString("NOME CITTA TROPPO LUNGO (MAX");
                        messaggio += lunghezzaMassimaCitta + " ";
                        messaggio += editorInterventi.getString("CAR");
                }
                
		if(civico.isEmpty())
		{
			errori++;
			messaggio += editorInterventi.getString("INDIRIZZO PAZIENTE VUOTO");
		} else if(civico.length() > lunghezzaMassimaCivico){
                        errori++;
			messaggio += editorInterventi.getString("INDIRIZZO PAZIENTE TROPPO LUNGO (MAX");
                        messaggio += lunghezzaMassimaCivico + " ";
                        messaggio += editorInterventi.getString("CAR");
                }
                if(cap.isEmpty())
		{
			errori++;
			messaggio += editorInterventi.getString("CAP INDIRIZZO PAZIENTE VUOTO");
		} else if(cap.length() > lunghezzaMassimaCivico){
                        errori++;
			messaggio += editorInterventi.getString("CAP INDIRIZZO PAZIENTE TROPPO LUNGO (MAX");
                        messaggio += lunghezzaMassimaCap + " ";
                        messaggio += editorInterventi.getString("CAR");
                }
                final int postUltimaRiga = modelloTabellaTipi.getRowCount();
			
		//Array per il controllo di coerenza e l'inserimento della lista interventi
                listaTipi = new ArrayList<>();
		
                //Inserimento dati nell'array
                for(int i=PRIMA_RIGA_DTM;i<postUltimaRiga;i++) {
                    ArrayList<PatologiaTO> listaPatologieIntervento = listaPatologieTipoIntervento.getListaInterventiPatologie()[i];
                    listaTipi.add(new TipoIntervento( String.valueOf(modelloTabellaTipi.getValueAt(i, COLONNA_NOME_TIPO_DTM)),  
                            String.valueOf(modelloTabellaTipi.getValueAt(i, COLONNA_NOTE_TIPO_DTM)), listaPatologieIntervento));
                }
                //verifica tipi intervento
                messaggio += verificaCoerenzaTipi(listaTipi); 
                
                messaggio += verificaValiditaDataPianificazione(idInfermiere, dataInt, oraInt);
                //Set costante per esplorazione del modello dei tipi di intervento
		
                        
		if (errori == 0) return true;
		else 
		{
			messaggio+= "\n" + String.valueOf(errori)+ " " + editorInterventi.getString("ERRORI RILEVATI");
			GMessage.message_error(messaggio);
			return false;
		}
    }
        
        private String verificaCoerenzaTipi(ArrayList<TipoIntervento> array)
	{
		String errLog="";

		//Controllo array non vuoto
		if(array.size()<1) {
                    errori++;
                    errLog += editorInterventi.getString("NESSUN TIPO DI INTERVENTO SPECIFICATO");
                }

		for(int i=0; i<array.size(); i++ )
		{
			//Lettura elemento corrente
			TipoIntervento t= array.get(i);
			//TODO Controllo nome SQL non nullo
			if (t.getNome().equals("null")){
                            errori++;
                            errLog += editorInterventi.getString(
                                    "TIPO DI INTERVENTO") + " NO." + (i + 1) + " " +
                                    editorInterventi.getString("NON VALIDO, NOME VUOTO O CON VALORE NULLO.");
                        } 
                            
			//TODO Controllo assenza duplicati
			for(int j=i+1;j<array.size();j++){
                           
                            if(t.getNome().equals(array.get(j).getNome()) && !t.getNome().equals("null")){
                                errori++;
                                errLog += editorInterventi.getString("TIPO DI INTERVENTO")+ t.getNome() + " " +
                                        editorInterventi.getString("DUPLICATO, CORREGGERE.");
                            }
                                
                        } 
			
			//Controllo nome e tipo validi
			errLog += verificaValiditaTipo(t.getNome());
			errLog += verificaValiditaNote(t.getNote());
		}
		return errLog;
	}

	public String verificaValiditaTipo(String newTipo){
		String errLog="";

		//NON VUOTO
		if (newTipo.isEmpty()) {
                    errori++;
                    errLog += editorInterventi.getString("TIPO INTERVENTO NON SPECIFICATO");
                }

		//Controllo lunghezza massima
		if (newTipo.length()>lunghezzaMassimaTipo) {
                    errori++;
                    errLog += editorInterventi.getString("TIPO INTERVENTO TROPPO LUNGO (MAX")+ 
                        lunghezzaMassimaTipo + " " + editorInterventi.getString("CAR");
                }
		//TODO controllo caratteri speciali

		return errLog;

	}
	public String verificaValiditaNote(String newNote){

		String errLog="";

		//Controllo lunghezza massima
		if (newNote.length()>lunghezzaMassimaNote) {
                    errori++;
                    errLog += 
                        editorInterventi.getString("NOTE TROPPO LUNGHE (MAX")+ lunghezzaMassimaNote + " " +
                        editorInterventi.getString("CAR");

                }
                    
		//TODO controllo caratteri speciali
		return errLog;
	}
        
        public String verificaValiditaDataPianificazione
	(String idInfermiere, String dataInit, String ora) {
		
		String errLog;	
		
		errLog = verificaValiditaData(dataInit);
		errLog = errLog + verificaValiditaOra(ora);
		
		if(errLog.isEmpty()) {
                    //differenza tra ora dell'intervento e ora corrente in millisecondi
		    long longOra = DateFormatConverter.dateString2long(dataInt + " " + ora, 
                            DateFormatConverter.Dateformat());
                    long today = DateFormatConverter.oggi();
                    long longDiff = (longOra - today);
				
                    int minuti = (int) ((longDiff / 3600000) * 60) + (int) (longDiff / 60000 % 60);
                    if(minuti < InterventoMySqlDAO.diffMax) {
                        System.out.println("Intervento non modificabile a meno di due ore");
                        errLog += editorInterventi.getString(
                                "IMPOSSIBILE CREARE UN INTERVENTO A MENO DI DUE ORE DALL'ORA CORRENTE") + " " + 
                                    DateFormatConverter.long2dateString(DateFormatConverter.oggi(), 
                                DateFormatConverter.Dateformat());
                            errori++;
                            return errLog;
                    } 
                                        
                    InterventoTO to = new InterventoTO();
                    to.setIDInfermiere(idInfermiere);
                    to.setData(dataInit);
                    to.setOraInizio(ora);
                    ArrayList<Record<String, Object>> params = new ArrayList<>();
                    params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                    int permesso = 2;
                    try {
                        permesso = (int) FC.processRequest("verificaValiditaIntervento", params);
                    } catch (MainException ex) {
                        Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                    }	
                    switch(permesso) {
			
                        case InterventoMySqlDAO.intTrascorso: 
                            errLog += editorInterventi.getString("IMPOSSIBILE CREARE UN INTERVENTO IN DATA E ORA") + 
                                dataInit + java.text.MessageFormat.format(editorInterventi.getString("{0} INFERIORE ALLA DATA E ORA CORRENTE {1}"), 
                                    new Object[] {ora, DateFormatConverter.long2dateString(DateFormatConverter.oggi(), DateFormatConverter.Dateformat())});
                            errori++;
                            break;
                        case InterventoMySqlDAO.intNonModificabile: 
                            int id = Integer.parseInt(idInfermiere);
                            InfermiereTO toInf = new InfermiereTO();
                            toInf.setID(id);
                            params.clear();
                            params.add(new Record<String, Object>("business.infermiere.InfermiereTO", toInf));
                            try {
                                toInf = (InfermiereTO) FC.processRequest("visualizzaInfermiere", params);
                            } catch (MainException ex) {
                                Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            errLog += editorInterventi.getString("IMPOSSIBILE MODIFICARE LA PIANIFICAZIONE ODIERNA ") +
                               editorInterventi.getString("DEGLI INTERVENTI PER L'INFERMIERE") +
                                toInf.getNome() + " " + toInf.getCognome() + "."
                                + editorInterventi.getString("IL SUO PRIMO INTERVENTO ODIERNO RISULTA TRASCORSO O A ") +
                                editorInterventi.getString("MENO DI DUE ORE DALL'ORA CORRENTE");
                            errori++;
                            break;	
                    }

                }
                return errLog;		
	}

        public String verificaValiditaData(String data)
	{
            String errLog="";
            if(data.equals("")){
                errori++;
                errLog = editorInterventi.getString("DATA DELL'INTERVENTO NON INSERITA");
                return errLog;
            }
                
		SimpleDateFormat formato = new SimpleDateFormat(formatoDataInput);
		
		try {
			formato.parse(data);
		} 
		catch (ParseException e) 
		{
                        errori++;
			errLog += java.text.MessageFormat.format(editorInterventi.getString(
                                "IMPOSSIBILE EFFETTUARE IL PARSING DELLA DATA <<{0}>> (NON È NEL FORMATO {1})"), 
                                new Object[] {data, formatoDataInput});
		}
		
		return errLog;		
	}

	public String verificaValiditaOra(String ora)
	{
            String errLog="";
            if(ora.isEmpty()){
                errori++;
                errLog = editorInterventi.getString("ORA DELL'INTERVENTO NON INSERITA");
                return errLog;
            }
            SimpleDateFormat formato= new SimpleDateFormat(formatoOraInput);
	
		try {
			formato.parse(ora);
		}
		catch (ParseException e) 
		{
                        errori++;
			errLog += java.text.MessageFormat.format(editorInterventi.getString(""
                                + "IMPOSSIBILE EFFETTUARE IL PARSING DELL'ORA <<{0}>> (NON È NEL FORMATO {1})"),
                                new Object[] {ora, formatoOraInput});
		}
		
		return errLog;
	}
        
    private void pulsanteAggiungiTipoActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        //TODO TEST Chiamata finestra di dialogo
        modelloTipiIntervento.setRowCount(modelloTipiIntervento.getRowCount() + 1);
        tabellaTipiIntervento.setRowSelectionInterval(modelloTipiIntervento.getRowCount() - 1, 
                modelloTipiIntervento.getRowCount() - 1);
        
        if(modelloTipiIntervento.getRowCount() != 1){
            EditorRipetiPatologie.setEditorInterventi(this);
            request(RequestManager.APRI_EDITOR_RIPETI_PATOLOGIE);
        }
        
        else {  
            tabellaTipiIntervento.setRowSelectionInterval(tabellaTipiIntervento.getRowCount() - 1,
                tabellaTipiIntervento.getRowCount() - 1);
            EditorPatologieTipoIntervento.setEditorInterventi(this);
            EditorPatologieTipoIntervento.setModifica(0);
        
            request(RequestManager.APRI_EDITOR_PATOLOGIE_TIPO_INTERVENTO);
        }
       
    }                                                    

    private void request(String request){
        FC = RequestManager.getFCInstance();
        if(FC.processRequest(request)){
            System.out.println("Finestra " + request + " aperta con successo.");
        }
        else{
            messaggistica.GMessage.winNotFound(request);
        }
    }
    
    private void pulsanteRimuoviTipoActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        if(tabellaTipiIntervento.getSelectedRow() == -1){
            GMessage.message_error(editorInterventi.getString("NESSUN TIPO INTERVENTO SELEZIONATO"));
            return;
        }
        String msg = editorInterventi.getString("SEI SICURO DI VOLER ELIMINARE L'INTERVENTO");
        msg = msg + editorInterventi.getString(" COSI FACENDO CANCELLERAI ANCHE TUTTE LE PATOLOGIE ASSOCIATE");
        int rowRemoved = tabellaTipiIntervento.getSelectedRow();
        if(GMessage.confirm(msg) == JOptionPane.YES_OPTION) { 
           
            if (tabellaTipiIntervento.getSelectedRowCount() > 0) {
                 
                  
                  if(rowRemoved == 0 && tabellaTipiIntervento.getRowCount() == 1){
                      modelloTipiIntervento.removeRow(tabellaTipiIntervento.getSelectedRow());
                      modelloPatologieTipoIntervento = getModelloPatologieVuoto();
                  }
                  else {
                      if(rowRemoved == 0 && tabellaTipiIntervento.getRowCount() != 1){
                        modelloPatologieTipoIntervento = getModelloPatologiePrec(
                              listaPatologieTipoIntervento, rowRemoved + 1);
                         modelloTipiIntervento.removeRow(tabellaTipiIntervento.getSelectedRow());
                        tabellaTipiIntervento.setRowSelectionInterval(rowRemoved, rowRemoved);
                        
                        
                      } else if(rowRemoved != 0){
                          modelloPatologieTipoIntervento = getModelloPatologiePrec(
                              listaPatologieTipoIntervento, rowRemoved - 1);
                           modelloTipiIntervento.removeRow(tabellaTipiIntervento.getSelectedRow());
                        tabellaTipiIntervento.setRowSelectionInterval(rowRemoved - 1, rowRemoved - 1);
                      }
                  }
                 
                  tabellaPatologieTipoIntervento.setModel(modelloPatologieTipoIntervento);
            }
            
            listaPatologieTipoIntervento.removeListaPatologieTipoIntervento(rowRemoved);
            EditorPatologieTipoIntervento.setEditorInterventi(this);
            
            
        } 

    }                                                   

    public VariableTableModel getModelloPatologiePrec(PatologieTipoIntervento lista, int riga) {
        VariableTableModel modelloPatologie = getModelloPatologieVuoto();
        //Recupero dati sui tipi di intervento      
        ArrayList<PatologiaTO> patologie = lista.getListaPatologieTipoIntervento(riga);
        for(PatologiaTO t: patologie) {
            String[] valori = { 
                t.getCodice(),
                t.getNome(), 
                String.valueOf(new Integer(t.getGravita())) 
            };
            modelloPatologie.addRow(valori);
        }
        return modelloPatologie;
    }
 
    public DefaultTableModel getPatologieIntervento(int IDIntervento) {
        DefaultTableModel modelloPatologie = getModelloPatologieVuoto();
        //Recupero dati sui tipi di intervento
        InterventoTO to = new InterventoTO();
        to.setID(IDIntervento);
        ArrayList<Record<String, Object>> params = new ArrayList<>();
        params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
        ArrayList<PatologiaTO> patologie = null;
        try {
            patologie = (ArrayList<PatologiaTO>) FC.processRequest("visualizzaPatologieIntervento", params);
        } catch (MainException ex) {
            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(PatologiaTO t: patologie){
            String[] valori = {t.getCodice(),t.getNome(), String.valueOf(new Integer(t.getGravita()))};
            modelloPatologie.addRow(valori);
        }
        return modelloPatologie;            
     }
         
     private VariableTableModel getModelloPatologieVuoto() {
            //Intestazioni
            VariableTableModel tabellaPatologieVuota = new VariableTableModel();
            tabellaPatologieVuota.addColumn(PatologiaMySqlDAO.NOME_COLONNA_CODICE);
            tabellaPatologieVuota.addColumn(PatologiaMySqlDAO.NOME_COLONNA_NOME);
            tabellaPatologieVuota.addColumn(InterventoMySqlDAO.NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI);
           
            //Creazione modello
            return tabellaPatologieVuota;
       }
     
    private void pulsanteImpostaInfermiereActionPerformed(java.awt.event.ActionEvent evt) {                                                          
        //Recupero riga selezione
        Integer riga = new Integer(tabellaInfermieri.getSelectedRow());
       
        //Verifica selezione infermiere
        if (riga == -1) {
            //Caso selezione nulla
            GMessage.message_error(editorInterventi.getString("ERRORE:NESSUN INFERMIERE SELEZIONATO."));
        } else {
            //Caso selezione valida

            //recupero dei numeri di colonna
            int colonnaID = tabellaInfermieri.getColumn(InfermiereMySqlDAO.NOME_COLONNA_ID).getModelIndex();
            int colonnaNome = tabellaInfermieri.getColumn(InfermiereMySqlDAO.NOME_COLONNA_NOME).getModelIndex();
            int colonnaCognome = tabellaInfermieri.getColumn(InfermiereMySqlDAO.NOME_COLONNA_COGNOME).getModelIndex();

            //Set della variabile relativa all'infermiere selezionato
            txInfermiereSelezionato.setValue(tabellaInfermieri.getValueAt(riga, colonnaID));

            //Set campo nomeinfermiere
            String tNome = String.valueOf(tabellaInfermieri.getValueAt(riga, colonnaNome));
            String tCognome = String.valueOf(tabellaInfermieri.getValueAt(riga, colonnaCognome));
            txNomeInfermiere.setText(tNome + java.text.MessageFormat.format(editorInterventi.getString(" {0}"), new Object[] {tCognome}));

            //Notifica
            String notifica = editorInterventi.getString("E' STATO SELEZIONATO L'INFERMIERE N.");
            notifica += txInfermiereSelezionato.getText() + editorInterventi.getString(".");
            notifica += java.text.MessageFormat.format(editorInterventi.getString("NOME: {0}"), new Object[] {tNome});
            notifica += java.text.MessageFormat.format(editorInterventi.getString("COGNOME: {0}"), new Object[] {tCognome});

            GMessage.information(notifica);
        }
    }                                                         

    private void tabellaTipiInterventoMouseClicked(java.awt.event.MouseEvent evt) {                                                   
        ArrayList<PatologiaTO> listaCorrente = new ArrayList<>();
         int rigaCorrente = tabellaTipiIntervento.getSelectedRow();
         if (txModalita.getText().equals(modAggiungi)  || MODIFICA_DATA == 1) {
            listaCorrente = listaPatologieTipoIntervento.getListaPatologieTipoIntervento(rigaCorrente);
         } else if (txModalita.getText().equals(modModifica)) {
             int colonnaID = tabellaInterventi.getColumn(InterventoMySqlDAO.NOME_COLONNA_ID).getModelIndex();
             int idInt = Integer.parseInt(tabellaInterventi.getValueAt(tabellaInterventi.getSelectedRow(), colonnaID).toString());
             
             InterventoTO to = new InterventoTO();
             to.setID(idInt);
             ArrayList<Record<String, Object>> params = new ArrayList<>();
             params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
             try {
                 to = (InterventoTO) FC.processRequest("visualizzaIntervento", params);
             } catch (MainException ex) {
                 Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
             }
             TipoIntervento tipoInterventoSelezionato = to.getTipoIntervento(rigaCorrente);
             listaCorrente = tipoInterventoSelezionato.getListaPatologie();
         }
        
         int colonnaCodice = tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(NOME_COLONNA_CODICE);
         int colonnaNome = tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(NOME_COLONNA_NOME);
         int colonnaGravita = tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(NOME_COLONNA_GRAVITA);
         
         DefaultTableModel modello = (DefaultTableModel) tabellaPatologieTipoIntervento.getModel();
         modello.setRowCount(listaCorrente.size());
          
         setModelTabellaPatologie(modello);
            
         for(int i = 0; i < listaCorrente.size(); i++){
             tabellaPatologieTipoIntervento.setValueAt(listaCorrente.get(i).getCodice(), i, 
                     colonnaCodice);
                 tabellaPatologieTipoIntervento.setValueAt(listaCorrente.get(i).getNome(), i, 
                         colonnaNome);
                 tabellaPatologieTipoIntervento.setValueAt(listaCorrente.get(i).getGravita(), i, colonnaGravita);
         }
    }                                                  

    private void pulsanteModificaPatologieActionPerformed(java.awt.event.ActionEvent evt) {                                                          
         if(tabellaTipiIntervento.getSelectedRow() == -1){
            GMessage.message_error(editorInterventi.getString("NESSUN TIPO INTERVENTO SELEZIONATO"));
            return;
        }
      
            EditorPatologieTipoIntervento.setModifica(1);
            EditorPatologieTipoIntervento.setEditorInterventi(this);
     
        request(RequestManager.APRI_EDITOR_PATOLOGIE_TIPO_INTERVENTO);
        
    }                                                         

    private void txRicDataDalPropertyChange(java.beans.PropertyChangeEvent evt) {                                            
         if(txRicDataDal.getDate() != null)
             try {
             ricercaFiltrata();
         } catch (MainException ex) {
             Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
         }
    }                                           

    private void txRicDataAlPropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        if(txRicDataAl.getDate() != null)
             try {
            ricercaFiltrata();
        } catch (MainException ex) {
            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                          

    private void pulsanteResettaRicercaActionPerformed(java.awt.event.ActionEvent evt) {                                                       
       comboInfermieri.setSelectedIndex(0);
       txRicDataDal.setDate(null);
       txRicDataAl.setDate(null);
        try {
            ricercaFiltrata();
        } catch (MainException ex) {
            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                      

    private void pulsanteModificaGravitaActionPerformed(java.awt.event.ActionEvent evt) {                                                        
       if(tabellaPatologieTipoIntervento.getSelectedRow() == -1){
            GMessage.message_error(editorInterventi.getString("SELEZIONARE UNA PATOLOGIA"));
            return;
       }
       EditorGravita.setRigaPatologia(tabellaPatologieTipoIntervento.getSelectedRow(), 
               tabellaPatologieTipoIntervento.getValueAt(
               tabellaPatologieTipoIntervento.getSelectedRow(),
               tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(NOME_COLONNA_CODICE)).toString());
       EditorGravita.setRigaTipoIntervento(tabellaTipiIntervento.getSelectedRow());
       EditorGravita.setGravita(Integer.parseInt(tabellaPatologieTipoIntervento.getValueAt(
               tabellaPatologieTipoIntervento.getSelectedRow(), 
               tabellaPatologieTipoIntervento.getColumnModel().getColumnIndex(NOME_COLONNA_GRAVITA)).toString()));
       EditorGravita.setEditorInterventi(this);
       request(RequestManager.APRI_EDITOR_GRAVITA);
       EditorPatologieTipoIntervento.setEditorInterventi(this);
    }                                                       

    private void pulsanteRimuoviPatologiaActionPerformed(java.awt.event.ActionEvent evt) {                                                         
        if(tabellaPatologieTipoIntervento.getSelectedRow() == -1){
            GMessage.information(editorInterventi.getString("SELEZIONARE UNA PATOLOGIA"));
        } else {
            String msg = editorInterventi.getString("SEI SICURO DI VOLER CANCELLARE")
                + editorInterventi.getString("QUESTA PATOLOGIA PER IL TIPO INTERVENTO SELEZIONATO?");
            int patoRowCount = tabellaPatologieTipoIntervento.getRowCount();
           
            if(patoRowCount == 1)
                msg = msg + editorInterventi.getString("VERRÀ CANCELLATO ANCHE IL RELATIVO TIPO DI INTERVENTO ")
                        + editorInterventi.getString("ESSENDO L'UNICA PATOLOGIA ASSOCIATA");
            if(GMessage.confirm(msg) == JOptionPane.YES_OPTION) { 
           
            
            /*DefaultTableModel newTablePato = (DefaultTableModel) tabellaPatologieTipoIntervento.getModel();
            newTablePato.removeRow(tabellaPatologieTipoIntervento.getSelectedRow());
            tabellaPatologieTipoIntervento.setModel(newTablePato);*/
                }//else
                int rowRemoved = tabellaPatologieTipoIntervento.getSelectedRow();
                if (tabellaPatologieTipoIntervento.getRowCount() == 1) {
                	listaPatologieTipoIntervento.removeListaPatologieTipoIntervento(tabellaTipiIntervento.getSelectedRow());
                	if(tabellaTipiIntervento.getRowCount() == 1){
                		modelloPatologieTipoIntervento = getModelloPatologieVuoto();
                		modelloTipiIntervento = getModelloTipiVuoto();
                		tabellaTipiIntervento.setModel(modelloTipiIntervento);
                	} else {
                		modelloPatologieTipoIntervento = getModelloPatologiePrec(
                                listaPatologieTipoIntervento, tabellaTipiIntervento.getSelectedRow() - 1);
                		DefaultTableModel newTipi= (DefaultTableModel) tabellaTipiIntervento.getModel();
                		int selectTipo = tabellaTipiIntervento.getSelectedRow();
                		newTipi.removeRow(selectTipo);
                		tabellaTipiIntervento.setModel(newTipi);
                        tabellaTipiIntervento.setRowSelectionInterval(selectTipo - 1, selectTipo - 1);
                        
                  	}	
                	 tabellaPatologieTipoIntervento.setModel(modelloPatologieTipoIntervento);
                	
                } else {
                	 ArrayList<PatologiaTO> listaPatologie = listaPatologieTipoIntervento.getListaPatologieTipoIntervento(tabellaTipiIntervento.getSelectedRow());
                     ArrayList<PatologiaTO> tmp = new ArrayList<>();
                     
                     int i = 0;
                     for(PatologiaTO e : listaPatologie){
                         if(i != rowRemoved)
                             tmp.add(e);
                         i++;
                     }
                     
                     listaPatologieTipoIntervento.rewriteListaPatologieTipoIntervento(tabellaTipiIntervento.getSelectedRow(), tmp);
                	
                     DefaultTableModel newTablePato = (DefaultTableModel) tabellaPatologieTipoIntervento.getModel();
                     newTablePato.removeRow(tabellaPatologieTipoIntervento.getSelectedRow());
                     tabellaPatologieTipoIntervento.setModel(newTablePato);
                }//if su rigaselezionata             
                    
                     this.setPatologieTipoIntervento(listaPatologieTipoIntervento);
                     EditorPatologieTipoIntervento.setEditorInterventi(this);
                }//else su risposta si

    }                                                        

    private void pulsanteEditorInfermieriActionPerformed(java.awt.event.ActionEvent evt) {                                                         
        // PRESSIONE DEL PULSANTE "AGGIONGI INFERMIERE" nella finestra di dialogo Intervento
        EditorInfermieri.setEditorInterventi(this);
        request(RequestManager.APRI_EDITOR_INFERMIERI);
    }                                                        

    private void pulsanteEditorPazientiActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        EditorPazienti.setEditorInterventi(this);
        request(RequestManager.APRI_EDITOR_PAZIENTI);
    }                                                      

    private void pulsanteCancellaTuttiActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        String messaggio= editorInterventi.getString("CANCELLARE TUTTI GLI INTERVENTI PIANIFICATI?");
        messaggio += editorInterventi.getString("I DATI NON SARANNO PIU' RECUPERABILI");

	//Visualizzazione conferma
        if(GMessage.confirm(messaggio) == JOptionPane.YES_OPTION){        
            System.out.println("Evento pulsante CancellaTuttiInterventi");
            try {
                if((Boolean) FC.processRequest("cancellaTuttiInterventi", null)) {
                    System.out.println("Cancellazione di tutti gli interventi avvenuta con successo");
                    messaggio = editorInterventi.getString("TUTTI GLI INTERVENTI SONO STATI CANCELLATI CON SUCCESSO!");
                    pianificatore.ricercaFiltrata();
                    ricercaFiltrata();
                    GMessage.information(messaggio);
                } else {
                    messaggistica.GMessage.message_error(editorInterventi.getString("CANCTUTTI"));
                }
            } catch (MainException ex) {
                Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                                     

    private void pulsanteCancellaActionPerformed(java.awt.event.ActionEvent evt) {                                                 

        int rigaSelezionata = tabellaInterventi.getSelectedRow();
        if (rigaSelezionata == -1) {

            //NESSUNA RIGA SELEZIONATA
            GMessage.message_error(editorInterventi.getString("ERRORE: IMPOSSIBILE CANCELLARE, NESSUN INTERVENTO SELEZIONATO"));
        } else {
            boolean passato = false;
            String data = tabellaInterventi.getValueAt(tabellaInterventi.getSelectedRow(), 
                    tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_DATA)).toString();
            String dateFormat = DateFormatConverter.cambiaFormato(data, 
                    FORMATO_DATA_TABELLA, FORMATO_DATA_INPUT);
            String ora = tabellaInterventi.getValueAt(tabellaInterventi.getSelectedRow(), 
                    tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO)).toString();
            String oraFormat = DateFormatConverter.cambiaFormato(ora, 
                    FORMATO_ORA_TABELLA, FORMATO_ORA_INPUT);
            String dataOra = dateFormat + java.text.MessageFormat.format(
                    editorInterventi.getString(" {0}"), new Object[] {oraFormat});
            long now = DateFormatConverter.oggi();
            long dataIntervento = DateFormatConverter.dateString2long(dataOra, 
                    DateFormatConverter.Dateformat());
            if((dataIntervento - now) < 0)
                passato = true;
            String messaggio = "";
            if(passato == true)
                messaggio = messaggio + editorInterventi.getString("SI STA CERCANDO DI ELIMINARE UN INTERVENTO PASSATO.")
                        + editorInterventi.getString("SE SI SCEGLIE DI PROSEGUIRE NON SARÀ PIÙ POSSIBILE VISUALIZZARE")
                        + editorInterventi.getString("L'INTERVENTO NELLO STORICO CON TUTTE LE RELATIVE")
                        + editorInterventi.getString("INFORMAZIONI.");
            messaggio = messaggio + editorInterventi.getString("CANCELLARE L'INTERVENTO SELEZIONATO?");
            if(GMessage.confirm(messaggio) == JOptionPane.YES_OPTION) {
                InterventoTO to = new InterventoTO();
                int idInt = Integer.parseInt(String.valueOf
                    (tabellaInterventi.getValueAt(tabellaInterventi.getSelectedRow(), 
                    tabellaInterventi.getColumn(InterventoMySqlDAO.NOME_COLONNA_ID).getModelIndex())));
                to.setID(idInt);
                System.out.println("Evento pulsante CancellaIntervento : "+ to);
                ArrayList<Record<String, Object>> params = new ArrayList<>();
                params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                try {
                    if((Boolean) FC.processRequest("cancellaIntervento", params)){
                        System.out.println("Cancellazione dell'intervento eseguita con successo");
                        messaggio = editorInterventi.getString("INTERVENTO CANCELLATO CON SUCCESSO");
                        pianificatore.setDate(dateFormat);
                        setDate(dateFormat);
                        ricercaFiltrata();
                        GMessage.information(messaggio);
                    }
                    else{
                        GMessage.message_error(editorInterventi.getString("CANCINTERVENTO"));
                    }
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
            /*
            ADISysController.cancellaIntervento(Integer.parseInt(String.valueOf
                    (tabellaInterventi.getValueAt(tabellaInterventi.getSelectedRow(), 
                    tabellaInterventi.getColumn(InterventoMySqlDAO.NOME_COLONNA_ID).getModelIndex()))), this, passato);

        }     */
    }                                                

    private void pulsanteModificaActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        MODIFICA_DATA = 1;
        setModalitaModifica();
    }                                                

    private void pulsanteAggiungiActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        MODIFICA_DATA = 0;
        setModalitaAggiungi();
    }                                                

    private void pulsanteEsciActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(GMessage.confirm(editorInterventi.getString("SEI SICURO DI VOLER ABBANDONARE")
            + editorInterventi.getString(" ADISYS?")) == JOptionPane.YES_OPTION) {
        System.exit(0);
        }
    }                                            

    private void pulsanteHomeActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(GMessage.confirm(editorInterventi.getString("SEI SICURO DI VOLER USCIRE DALL'")
            + editorInterventi.getString("EDITOR DEGLI INTERVENTI E TORNARE ALLA HOME?"))
        == JOptionPane.YES_OPTION) {
        this.dispose();
        }
    }                                            

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   
         if(GMessage.confirm(editorInterventi.getString("SEI SICURO DI VOLER USCIRE DALL'")
                 + editorInterventi.getString("EDITOR DEGLI INTERVENTI?PERDERAI TUTTE LE INFORMAZIONI INSERITE")
                 + editorInterventi.getString(" SUL FORM SE NON SONO STATE CONFERMATE"))   
                 == JOptionPane.YES_OPTION) {
             this.dispose();
        }
    }                                  

    private void comboInfermieriActionPerformed(java.awt.event.ActionEvent evt) {                                                
        try {
            ricercaFiltrata();
        } catch (MainException ex) {
            Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                               

    private void tabellaInterventiKeyPressed(java.awt.event.KeyEvent evt) {                                             
              if((evt.getKeyChar()== 'i' || evt.getKeyChar()== 'I')
                        && tabellaInterventi.getColumnCount() != 1)
                {
                    FC = RequestManager.getFCInstance();
                    InterventoTO to = new InterventoTO();
                    to.setID(Integer.parseInt(tabellaInterventi.getValueAt(
                            tabellaInterventi.getSelectedRow(), 
                            tabellaInterventi.getColumnModel().getColumnIndex(
                            InterventoMySqlDAO.NOME_COLONNA_ID)).toString()));
                    ArrayList<Record<String, Object>> params = new ArrayList<>();
                    params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                    try {
                        String intervento = FC.processRequest("visualizzaIntervento", params).toString();
                        GMessage.information(intervento);
                    } catch (MainException ex) {
                        Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
    }                                            

    private JTable verificaTabellaPiena(JTable tabella, String label){
        if(tabella.getRowCount() == 0){
                    VariableTableModel modello = new VariableTableModel();
                    modello.addColumn("");
                    String[] msg = new String[1];
                    if(!label.equals(editorInterventi.getString("INTERVENTO"))){
                        msg[0] = java.text.MessageFormat.format(
                                editorInterventi.getString("NON È STATO TROVATO ALCUN {0} REGISTRATO NEL"), new Object[] {label});
                    }
                    else {
                        msg[0] = java.text.MessageFormat.format(
                                editorInterventi.getString("NON È STATO TROVATO ALCUN {0}"), new Object[] {label});
                        String strId = "";
                        String strName = "";
                        int noInfo = 0;
                        if(tabellaInfermieri.getColumnCount() != 1 && tabella.getRowCount() != 0){
                            strId = ((ComboItem) comboInfermieri.getSelectedItem()).getValue();
                            strName = ((ComboItem) comboInfermieri.getSelectedItem()).getName();
                            noInfo = 1;
                        }
			if(comboInfermieri.getSelectedIndex() != 0 && comboInfermieri.getSelectedIndex() != -1)
                            msg[0] = msg[0] + editorInterventi.getString("PER L'INFERMIERE") + 
                                    ((ComboItem) comboInfermieri.getSelectedItem()).getName();
                        if(txRicDataDal.getDate() != null){
                            SimpleDateFormat formatoData = new SimpleDateFormat(formatoDataInput);
                            String strDataDal = formatoData.format(txRicDataDal.getDate());
                            msg[0] = msg[0] + java.text.MessageFormat.format(editorInterventi.getString(" DAL {0}"), new Object[] {strDataDal});
                            noInfo = 1;
                        }
                        if(txRicDataAl.getDate() != null){
                            SimpleDateFormat formatoData = new SimpleDateFormat(formatoDataInput);
                            String strDataAl = formatoData.format(txRicDataAl.getDate());
                            msg[0] = msg[0] + java.text.MessageFormat.format(editorInterventi.getString(" AL {0}"), new Object[] {strDataAl});
                        } else {
                            if(noInfo == 1)
                                msg[0] = msg[0] + editorInterventi.getString("AD OGGI");
                        }
                    
                    }            
                    msg[0] = msg[0] + editorInterventi.getString("REGISTRATO");
                     modello.addRow(msg);
                     tabella.setModel(modello);
                     if(label.equals(editorInterventi.getString("INFERMIERE"))){
                         pulsanteImpostaInfermiere.setVisible(false);
                     } else if(label.equals(editorInterventi.getString("PAZIENTE"))){
                         pulsanteImpostaPaziente.setVisible(false);
                     } else {
                         pulsanteModifica.setVisible(false);
                         pulsanteCancella.setVisible(false);
                         pulsanteCancellaTutti.setVisible(false);
                     }
                } else {
            if(label.equals(editorInterventi.getString("PAZIENTE"))){
                if(pulsanteImpostaPaziente.isVisible() == false)
                    pulsanteImpostaPaziente.setVisible(true);
            }
                
            else if(label.equals(editorInterventi.getString("INFERMIERE"))){
                if(pulsanteImpostaInfermiere.isVisible() == false)
                    pulsanteImpostaInfermiere.setVisible(true);
            }
                
            else {
                if(pulsanteModifica.isVisible() == false){
                    pulsanteModifica.setVisible(true);
                    pulsanteCancella.setVisible(true);
                    pulsanteCancellaTutti.setVisible(true); 
                }
               
            }
        }
        return tabella;
    }
    private void aggiornaTabelle() throws MainException {
        FC = RequestManager.getFCInstance();
        NOME_COLONNA_CODICE = PatologiaMySqlDAO.NOME_COLONNA_CODICE;
        NOME_COLONNA_NOME = PatologiaMySqlDAO.NOME_COLONNA_NOME;
        NOME_COLONNA_GRAVITA = InterventoMySqlDAO.NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI;
        aggiornaInterventi();
        aggiornaInfermieri();
        aggiornaPazienti();
        //Intestazioni
        tabellaPatologieTipoIntervento.setModel(getModelloPatologieVuoto());
    }

    public void aggiornaInterventi() throws MainException {
	AbstractTableModel modelAdisys = (AbstractTableModel) FC.processRequest("visualizzaInterventi", null);
	tabellaInterventi.setModel(modelAdisys);
        tabellaInterventi = verificaTabellaPiena(tabellaInterventi, editorInterventi.getString("INTERVENTO"));
    }

    public void aggiornaInfermieri() throws MainException {
	AbstractTableModel modelAdisys = (AbstractTableModel) FC.processRequest("visualizzaInfermieri", null);
	tabellaInfermieri.setModel(modelAdisys);
        tabellaInfermieri = verificaTabellaPiena(tabellaInfermieri, editorInterventi.getString("INFERMIERE"));
    }

    public void aggiornaPazienti() throws MainException {
	AbstractTableModel modelAdisys = (AbstractTableModel) FC.processRequest("visualizzaPazienti", null);
	tabellaPazienti.setModel(modelAdisys);
        tabellaPazienti = verificaTabellaPiena(tabellaPazienti, editorInterventi.getString("PAZIENTE"));
    }

    /**
     * @param args the command line arguments
     */
    public static void start() {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (editorInterventi.getString("NIMBUS").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditorInterventi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditorInterventi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditorInterventi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditorInterventi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditorInterventi window = null;
                try {
                    window = new EditorInterventi(new javax.swing.JFrame(), true);
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
                Dimension risoluzioneSchermo = Toolkit.getDefaultToolkit().getScreenSize();
                window.setLocation(new Point((risoluzioneSchermo.width - window.getWidth()) / 2, (risoluzioneSchermo.height - window.getHeight()) / 2 - 20));
                window.setVisible(true);
                try {
                    window.aggiornaTabelle();
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JComboBox comboInfermieri;
    private javax.swing.JLabel immagineTestataInterventi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelInfermieri;
    private javax.swing.JLabel labelInterventi;
    private javax.swing.JLabel labelIntervento;
    private javax.swing.JLabel labelPazienti;
    private javax.swing.JPanel pannelloDati;
    private javax.swing.JButton pulsanteAggiungi;
    private javax.swing.JButton pulsanteAggiungiTipo;
    private javax.swing.JButton pulsanteCancella;
    private javax.swing.JButton pulsanteCancellaTutti;
    private javax.swing.JButton pulsanteConferma;
    private javax.swing.JButton pulsanteEditorInfermieri;
    private javax.swing.JButton pulsanteEditorPazienti;
    private javax.swing.JButton pulsanteEsci;
    private javax.swing.JButton pulsanteHome;
    private javax.swing.JButton pulsanteImpostaInfermiere;
    private javax.swing.JButton pulsanteImpostaPaziente;
    private javax.swing.JButton pulsanteModifica;
    private javax.swing.JButton pulsanteModificaGravita;
    private javax.swing.JButton pulsanteModificaPatologie;
    private javax.swing.JButton pulsanteResettaRicerca;
    private javax.swing.JButton pulsanteRimuoviPatologia;
    private javax.swing.JButton pulsanteRimuoviTipo;
    private javax.swing.JTable tabellaInfermieri;
    private javax.swing.JTable tabellaInterventi;
    private javax.swing.JTable tabellaPatologieTipoIntervento;
    private javax.swing.JTable tabellaPazienti;
    private javax.swing.JTable tabellaTipiIntervento;
    private javax.swing.JFormattedTextField txCAP;
    private javax.swing.JTextField txCitta;
    private javax.swing.JTextField txCivico;
    private com.toedter.calendar.JDateChooser txData;
    private javax.swing.JFormattedTextField txInfermiereSelezionato;
    private javax.swing.JFormattedTextField txInterventoSelezionato;
    private javax.swing.JTextField txModalita;
    private javax.swing.JTextField txNomeInfermiere;
    private javax.swing.JTextField txNomePaziente;
    private javax.swing.JFormattedTextField txOra;
    private javax.swing.JFormattedTextField txPazienteSelezionato;
    private com.toedter.calendar.JDateChooser txRicDataAl;
    private com.toedter.calendar.JDateChooser txRicDataDal;
    // End of variables declaration                   
    private DefaultTableModel modelloTipiIntervento;
    private DefaultTableModel modelloPatologieTipoIntervento;

    /**
     * Prepara il modulo per l'inserimento di un nuovo intervento
     *
     */
    private void setModalitaAggiungi() {

        //RESET DEGLI INDICI DI PAZIENTE ED INFERMIERE
        txModalita.setText(modAggiungi);

        txInterventoSelezionato.setText(null);
        txInfermiereSelezionato.setText(null);
        txPazienteSelezionato.setText(null);

        //RESET SELEZIONI TABELLA
        tabellaPazienti.clearSelection();
        tabellaInfermieri.clearSelection();
        tabellaInterventi.clearSelection();

        //RESET DEI CAMPI DI INSERIMENTO
        txData.setDate(null);
        txOra.setText("");

        txCAP.setText("");
        txCitta.setText("");
        txCivico.setText("");

        //RESET ETICHETTE
        txNomePaziente.setText(editorInterventi.getString("(PAZIENTE NON SELEZIONATO)"));
        txNomeInfermiere.setText(editorInterventi.getString("(INFERMIERE NON SELEZIONATO)"));
        labelIntervento.setText(editorInterventi.getString("AGGIUNGI DATI NUOVO INTERVENTO"));
        pulsanteConferma.setText(editorInterventi.getString("INSERISCI NUOVO INTERVENTO"));

        //Reset tipi Intervento
        modelloTipiIntervento = getModelloTipiVuoto();
        tabellaTipiIntervento.setModel(modelloTipiIntervento);
        
       //Reset patologie Intervento
        modelloPatologieTipoIntervento = getModelloPatologieVuoto();
        tabellaPatologieTipoIntervento.setModel(modelloPatologieTipoIntervento);
        
        listaPatologieTipoIntervento = new PatologieTipoIntervento();
    }

    public static DefaultTableModel getModelloTipiVuoto(){
            //Intestazioni
            String NOME_COLONNA_NOME_TIPO_INTERVENTO = "Tipo";
            String NOME_COLONNA_NOTE_TIPO_INTERVENTO = "Note";
            String[] colonne= { NOME_COLONNA_NOME_TIPO_INTERVENTO, NOME_COLONNA_NOTE_TIPO_INTERVENTO};
            
            //Creazione modello
            return new DefaultTableModel(colonne,0);
        }
    
     public DefaultTableModel getTipiIntervento(int IDIntervento)
        {
            
            DefaultTableModel modelloTipi = getModelloTipiVuoto();
            //Recupero dati sui tipi di intervento
             //TODO Caricamento numeri di cellulare
             InterventoTO to = new InterventoTO();
             to.setID(IDIntervento);
             ArrayList<Record<String, Object>> params = new ArrayList<>();
             params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
             ArrayList<TipoIntervento> tipi = null;
             try {
                 tipi = (ArrayList<TipoIntervento>) FC.processRequest("visualizzaTipiIntervento", params);
             } catch (MainException ex) {
                 Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
             }
           
            for(TipoIntervento t: tipi)
            {
                String[] valori = {t.getNome(),t.getNote()};
                modelloTipi.addRow(valori);
            }
            return modelloTipi;
                    
    }
    
    /**
     * Se c'e' un intervento selezionato prepara il modulo per la modifica,
     * altrimenti visualizza un messaggio di errore.
     */
    private void setModalitaModifica() {
        //Se e' selezionato un valore nella tabella interventi prepara il modulo
        //per la modifica. Altrimenti visualizza un messaggio di errore

        //VERIFICA INTERVENTO SELEZIONATO
        int rigaSelezionata = tabellaInterventi.getSelectedRow();

        if (rigaSelezionata == -1) {

            //NESSUNA RIGA SELEZIONATA
            GMessage.message_error(editorInterventi.getString("ERRORE: IMPOSSIBILE MODIFICARE, NESSUN INTERVENTO SELEZIONATO"));
        } else {

            //RECUPERO INFO COLONNE
            int colonnaID = tabellaInterventi.getColumn(InterventoMySqlDAO.NOME_COLONNA_ID).getModelIndex();
            int colonnaIDPaziente = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_ID_PAZIENTE);
            int colonnaIDInfermiere = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_ID_INFERMIERE);

            int colonnaData = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_DATA);
            int colonnaOraInizio = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_ORA_INIZIO);

            int colonnaCitta = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_CITTA);
            int colonnaCivico = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_CIVICO);
            int colonnaCAP = tabellaInterventi.getColumnModel().getColumnIndex(InterventoMySqlDAO.NOME_COLONNA_CAP);

            //Elaborazione data-ora 
            //Cambiamento formato data e ora
            String testoData = DateFormatConverter.cambiaFormato(tabellaInterventi.getValueAt(rigaSelezionata, colonnaData).toString(), FORMATO_DATA_TABELLA, formatoDataInput);
            String testoOra = DateFormatConverter.cambiaFormato(tabellaInterventi.getValueAt(rigaSelezionata, colonnaOraInizio).toString(), FORMATO_ORA_TABELLA, formatoOraInput);

            //Trace
            System.out.println();
            System.out.println(java.text.MessageFormat.format(
                    editorInterventi.getString("DATA RECUPERATA: {0}"), new Object[] {testoData}));
            System.out.println(java.text.MessageFormat.format(
                    editorInterventi.getString("ORA RECUPERATA: {0}"), new Object[] {testoOra}));
            InterventoTO to = new InterventoTO();
            int idInf = Integer.parseInt(tabellaInterventi.getValueAt(rigaSelezionata, colonnaIDInfermiere).toString());
            to.setIDInfermiere(idInf);
            to.setData(testoData);
            to.setOraInizio(testoOra);
                    ArrayList<Record<String, Object>> params = new ArrayList<>();
                    params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                    int flgMod = 2;
                    try {
                        flgMod = (int) FC.processRequest("verificaValiditaIntervento", params);
                    } catch (MainException ex) {
                        Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
             }
            //VERIFICA COERENZA ORARIO DI MODIFICA CON ORARIO DELLA PRIMA ATTIVITA DELL'INFERMIERE (ME1)
            if (flgMod == InterventoMySqlDAO.intTrascorso) {
                GMessage.message_error(editorInterventi.getString("ERRORE: IMPOSSIBILE MODIFICARE UN INTERVENTO PASSATO"));
            } else if (flgMod == InterventoMySqlDAO.intNonModificabile) {
                InfermiereTO toInf = new InfermiereTO();
                toInf.setID(idInf);
                params.clear();
                params.add(new Record<String, Object>("business.infermiere.InfermiereTO", toInf));
                try {
                    toInf = (InfermiereTO) FC.processRequest("visualizzaInfermiere", params);
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
                GMessage.message_error(editorInterventi.getString("ERRORE: IMPOSSIBILE MODIFICARE LA PIANIFICAZIONE ODIERNA ")
                        + editorInterventi.getString("DEGLI INTERVENTI PER L'INFERMIERE") +
                        toInf.getNome() + " " + toInf.getCognome() + "."
                        + editorInterventi.getString("IL SUO PRIMO INTERVENTO ODIERNO RISULTA TRASCORSO O A ")
                        + editorInterventi.getString("MENO DI DUE ORE DALL'ORA CORRENTE"));
            } else {
                //RECUPERO INFORMAZIONI ATTUALI E POPOLAMENTO CAMPI
                txInterventoSelezionato.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaID).toString());
                txPazienteSelezionato.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaIDPaziente).toString());
                txInfermiereSelezionato.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaIDInfermiere).toString());

                txCitta.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaCitta).toString());
                txCivico.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaCivico).toString());
                txCAP.setText(tabellaInterventi.getValueAt(rigaSelezionata, colonnaCAP).toString());

                //Assegnazione dataOra
                txData.setDate(new Date(DateFormatConverter.dateString2long(testoData, 
                        DateFormatConverter.getFormatData())));
                txOra.setText(testoOra);

                //TODO caricamento tipi intervento
                modelloTipiIntervento = getTipiIntervento(Integer.valueOf(tabellaInterventi.getValueAt(rigaSelezionata, colonnaID).toString()));
                tabellaTipiIntervento.setModel(modelloTipiIntervento);
                
                modelloPatologieTipoIntervento = getPatologieIntervento(Integer.valueOf(tabellaInterventi.getValueAt(rigaSelezionata, colonnaID).toString()));
                tabellaPatologieTipoIntervento.setModel(modelloPatologieTipoIntervento);
                
                listaPatologieTipoIntervento = new PatologieTipoIntervento();
                to = new InterventoTO();
                to.setID(Integer.parseInt(tabellaInterventi.getValueAt(rigaSelezionata, 
                        colonnaID).toString()));
                params.clear();
                params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                ArrayList<TipoIntervento> listaTipiIntervento = null;
                try {
                    listaTipiIntervento = (ArrayList<TipoIntervento>) FC.processRequest("visualizzaTipiIntervento", params);
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<PatologiaTO>[] listaPatologie = new ArrayList[listaTipiIntervento.size()];
                for(int i = 0; i < listaPatologie.length; i++){
                    listaPatologie[i] = listaTipiIntervento.get(i).getListaPatologie();
                    listaPatologieTipoIntervento.setListaPatologieTipoIntervento(listaPatologie[i]);
                }
                EditorPatologieTipoIntervento.setEditorInterventi(this);
               
                //RESET ETICHETTE
                txNomePaziente.setText(editorInterventi.getString("(PAZIENTE NON MODIFICATO)"));
                txNomeInfermiere.setText(editorInterventi.getString("(INFERMIERE NON MODIFICATO)"));
                labelIntervento.setText(java.text.MessageFormat.format(
                        editorInterventi.getString
                        ("MODIFICA DATI INTERVENTO N.{0}"), 
                        new Object[] {txInterventoSelezionato.getText()}));
                pulsanteConferma.setText(editorInterventi.getString("CONFERMA MODIFICHE"));

                //Settaggio variabile di stato
                txModalita.setText(modModifica);

            }//ME1: else su modifica intervento a meno di due ore dal primo intervento dell'infermiere selezionato
        }
    }

    @Override
    public void open() {
        start();
    }
   
}
