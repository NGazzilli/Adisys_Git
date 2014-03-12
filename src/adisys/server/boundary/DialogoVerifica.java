
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.boundary;

import adisys.server.strumenti.ADISysTableRendererVerifica;
import adisys.server.strumenti.DateFormatConverter;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoCompletoTO;
import business.intervento.InterventoTO;
import business.intervento.Rilevazione;
import messaggistica.MainException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import presentation.FrontController;
import presentation.RequestManager;
import adisys.server.strumenti.Record;
import messaggistica.GMessage;

/**
*
* @author Gianmarco Divittorio
* @author Nicola Gazzilli
*/
public class DialogoVerifica extends javax.swing.JDialog implements Boundary {

    private static DialogoVerifica window;
    private static ResourceBundle dialogoVerifica = ResourceBundle.getBundle("adisys/server/property/DialogoVerifica");
    private FrontController FC;
    private static final String FORMATO_DATA_GUI = DateFormatConverter.getFormatData();
    private static final String FORMATO_ORA_GUI ="HH:mm:ss";
    
    
     public static void setResourceBundle(String path, Locale locale){
        dialogoVerifica = ResourceBundle.getBundle(path, locale);
    }
	/**
	 * Creates new form DialogoVerifica
	 */
	public DialogoVerifica(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
                setListaJournaling();
	}

        public DialogoVerifica(){
            
        }
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        immagineTestataVerifica = new javax.swing.JLabel();
        pannelloTabellaAttivita = new javax.swing.JScrollPane();
        tabellaAttivita = new javax.swing.JTable();
        pannelloTabellaLog = new javax.swing.JScrollPane();
        tabellaLog = new javax.swing.JTable();
        txInfermiere = new javax.swing.JLabel();
        txRilevazioni = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        pulsanteAggiornaLista = new javax.swing.JButton();
        cbFileJournaling = new javax.swing.JComboBox();
        pulsanteCarica = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pulsanteEsci = new javax.swing.JButton();
        pulsanteHome = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(dialogoVerifica.getString("TITLE VERIFICA")); // NOI18N
        setIconImage(null);
        setMinimumSize(null);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        immagineTestataVerifica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/Verifica.png"))); // NOI18N

        tabellaAttivita.setRowHeight(25);
        tabellaAttivita.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaAttivita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaAttivitaMouseClicked(evt);
            }
        });
        tabellaAttivita.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabellaAttivitaKeyPressed(evt);
            }
        });
        pannelloTabellaAttivita.setViewportView(tabellaAttivita);

        tabellaLog.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabellaLog.getTableHeader().setReorderingAllowed(false);
        pannelloTabellaLog.setViewportView(tabellaLog);

        txInfermiere.setFont(txInfermiere.getFont().deriveFont(txInfermiere.getFont().getStyle() | java.awt.Font.BOLD, 14));
        txInfermiere.setForeground(new java.awt.Color(255, 255, 255));
        txInfermiere.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txInfermiere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Viola.png"))); // NOI18N
        txInfermiere.setText(dialogoVerifica.getString("INFERMERE: -")); // NOI18N
        txInfermiere.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txRilevazioni.setFont(txRilevazioni.getFont().deriveFont(txRilevazioni.getFont().getStyle() | java.awt.Font.BOLD, 14));
        txRilevazioni.setForeground(new java.awt.Color(255, 255, 255));
        txRilevazioni.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txRilevazioni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/barre/Grigia.png"))); // NOI18N
        txRilevazioni.setText(dialogoVerifica.getString("LISTA RILEVAZIONI")); // NOI18N
        txRilevazioni.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        pulsanteAggiornaLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaAggiorna.png"))); // NOI18N
        pulsanteAggiornaLista.setText(dialogoVerifica.getString("AGGIORNA LISTA")); // NOI18N
        pulsanteAggiornaLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteAggiornaListaActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteAggiornaLista);

        jToolBar1.add(cbFileJournaling);

        pulsanteCarica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/IconaImportazione.png"))); // NOI18N
        pulsanteCarica.setText(dialogoVerifica.getString("CARICA E VERIFICA")); // NOI18N
        pulsanteCarica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteCaricaActionPerformed(evt);
            }
        });
        jToolBar1.add(pulsanteCarica);

        jLabel1.setText(dialogoVerifica.getString("CLICCARE SU UNA SINGOLA ATTIVITA' PER LEGGERE LE RILEVAZIONI, PREMERE 'I' PER INFORMAZIONI SULL'ATTIVIT�.")); // NOI18N

        pulsanteEsci.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/arresta.png"))); // NOI18N
        pulsanteEsci.setText(dialogoVerifica.getString("ABBANDONA ADISYS")); // NOI18N
        pulsanteEsci.setToolTipText(dialogoVerifica.getString("ESCI DA ADISYS")); // NOI18N
        pulsanteEsci.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteEsci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteEsciActionPerformed(evt);
            }
        });

        pulsanteHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/home.png"))); // NOI18N
        pulsanteHome.setText(dialogoVerifica.getString("HOME")); // NOI18N
        pulsanteHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pulsanteHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pannelloTabellaLog)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pulsanteEsci)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pulsanteHome)
                        .addContainerGap())
                    .addComponent(pannelloTabellaAttivita)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(immagineTestataVerifica)
                            .addComponent(txRilevazioni)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txInfermiere)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)))
                        .addGap(0, 135, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(immagineTestataVerifica)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txInfermiere)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pannelloTabellaAttivita, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txRilevazioni)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pannelloTabellaLog, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pulsanteEsci)
                    .addComponent(pulsanteHome))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void pulsanteAggiornaListaActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        setListaJournaling();
    }                                                     

    private void pulsanteCaricaActionPerformed(java.awt.event.ActionEvent evt) {                                               
        if(cbFileJournaling.getModel().getSelectedItem() != 
                cbFileJournaling.getModel().getElementAt(0)){
            FC = RequestManager.getFCInstance();
            boolean infTrovato = true;
            ArrayList<Record<String, Object>> params = new ArrayList<Record<String, Object>>();
            params.add(new Record<String, Object>("java.lang.String", 
                String.valueOf(cbFileJournaling.getSelectedItem())));      
            try {
                String messaggio = (String) FC.processRequest("caricaFile", params);
                if(messaggio.contains(dialogoVerifica.getString("INFERMIERE"))){
                    infTrovato = false;
                    GMessage.message_error(messaggio);
                } else if(messaggio.contains(dialogoVerifica.getString("ANNULLATO"))){
                    infTrovato = false;
                    GMessage.information(messaggio);
                } else {
                    GMessage.information(messaggio);
                }
            } catch (MainException ex) {
                Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(infTrovato == true){
                String stringaInfermiere ="Infermiere: ";
                InfermiereTO toInf = new InfermiereTO();
                FC = RequestManager.getFCInstance();
                ArrayList<InterventoCompletoTO> listaInterventi = null;
                try {
                    listaInterventi = (ArrayList<InterventoCompletoTO>) FC.processRequest("visualizzaListaInterventi", null);
                } catch (MainException ex) {
                    Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
                }
                toInf.setID(listaInterventi.get(0).getIDInfermiere());
                params.clear();
                params.add(new Record<String, Object>("business.infermiere.InfermiereTO", toInf));   
     
                try {
                    toInf = (InfermiereTO) FC.processRequest("visualizzaInfermiere", params);
                } catch (MainException ex) {
                    Logger.getLogger(EditorInterventi.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                stringaInfermiere += toInf.getNome();
                stringaInfermiere += " " + toInf.getCognome();
        
                txInfermiere.setText(stringaInfermiere);
        
                //TableModel per popolamento tabella
                DefaultTableModel modello = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column)
                    {return false;}
                };
        
                //Settaggio colonne
                String titoli[] = {"IL", "DALLE","ALLE", "LUOGO","PAZIENTE","GPS","ACCEL."};
        
                modello.setColumnCount(titoli.length);
                modello.setColumnIdentifiers(titoli);
        
                if(!listaInterventi.isEmpty()) {
                    for (InterventoCompletoTO i:listaInterventi){
                        params.clear();
                        params.add(new Record<String, Object>("business.intervento.InterventoCompletoTO", i));
                        try {
                            FC.processRequest("aggiornaStorico", params);
                        } catch (MainException ex) {
                            Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String riga[]={
                            i.getDataDaFormato(FORMATO_DATA_GUI),
                            i.getOraInizioDaFormato(FORMATO_ORA_GUI),
                            i.getOraFineDaFormato(FORMATO_ORA_GUI),
                            i.getCivico() + "- " +i.getCitta(),
                            i.getPaziente().getNome() + " " + i.getPaziente().getCognome(),
                            i.getStatoVerificaGPS().toString(),
                            i.getStatoVerificaAccelerometro().toString()
        		};
        		
        		modello.addRow(riga);
                    }
                }
        
                tabellaAttivita.setModel(modello);
        
                int colonnaGraficaGPS =5;
                int colonnaGraficaACC =6;
        
        
                ADISysTableRendererVerifica renderer = new ADISysTableRendererVerifica(colonnaGraficaGPS,colonnaGraficaACC);
                tabellaAttivita.setDefaultRenderer(tabellaAttivita.getColumnClass(0), renderer);
        
        
                ///Svuota la tabella dei log
                tabellaLog.setModel(new DefaultTableModel());
            }
        }
    }                                              

    private void setListaJournaling(){
        FC = RequestManager.getFCInstance();
        String[] listaFile = null;
        try {
            listaFile = (String[]) FC.processRequest("visualizzaListaJournaling", null);
        } catch (MainException ex) {
            Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel<String> modello= new DefaultComboBoxModel<>();
        modello.addElement(dialogoVerifica.getString("SCEGLI FILE"));
        cbFileJournaling.setModel(modello);
        if (listaFile==null) 
            GMessage.message_error(dialogoVerifica.getString("ERRORE CARICA LISTA"));
        else{
            for(String s:listaFile) modello.addElement(s);
            cbFileJournaling.setModel(modello);
        }
    }
    private void pulsanteEsciActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(GMessage.confirm(dialogoVerifica.getString("SEI SICURO DI VOLER ABBANDONARE")
            + dialogoVerifica.getString(" ADISYS?")) == JOptionPane.YES_OPTION) {
        System.exit(0);
        }
    }                                            

    private void tabellaAttivitaKeyPressed(java.awt.event.KeyEvent evt) {                                           
        // TODO add your handling code here:
        if(evt.getKeyChar()== 'i' || evt.getKeyChar()== 'I'){
            FC = RequestManager.getFCInstance();
            int valoreSelezionato = tabellaAttivita.getSelectedRow();
            if (valoreSelezionato !=-1){
                System.out.println("ControllerVerifica - Prelievo elemento " + valoreSelezionato);
                InterventoTO to = new InterventoTO();
                to.setID(valoreSelezionato);
                ArrayList<Record<String, Object>> params = new ArrayList<Record<String, Object>>();
                params.add(new Record<String, Object>("business.intervento.InterventoTO", to));
                InterventoCompletoTO toCompleto = null;
                try {
                    toCompleto = (InterventoCompletoTO) 
                    FC.processRequest("visualizzaInterventoCompleto", params);
                } catch (MainException ex) {
                    Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
                }
            GMessage.information(toCompleto.toString());
            }
        }

    }                                          

    private void tabellaAttivitaMouseClicked(java.awt.event.MouseEvent evt) {                                             
        int valoreSelezionato = tabellaAttivita.getSelectedRow();
        if (valoreSelezionato !=-1)
            popolaTabellaLog(valoreSelezionato);

    }                                            

    public void popolaTabellaLog(int indiceIntervento)
    {
        FC = RequestManager.getFCInstance();
        ArrayList<InterventoCompletoTO> listaInterventi = null;
        try {
            listaInterventi = (ArrayList<InterventoCompletoTO>) FC.processRequest("visualizzaListaInterventi", null);
        } catch (MainException ex) {
            Logger.getLogger(DialogoVerifica.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel modelloLog = new DefaultTableModel()
        {
        
        @Override
        public boolean isCellEditable(int r, int c)
            {
                return false;
            }
        };

        modelloLog.addColumn("Timestamp");
        modelloLog.addColumn("Latitude");
        modelloLog.addColumn("Longitude");
        modelloLog.addColumn("Altitude");
        modelloLog.addColumn("Accuracy");
        modelloLog.addColumn("Accel X");
        modelloLog.addColumn("Accel Y");
        modelloLog.addColumn("Accel Z");
        
        int i=0;
        while(listaInterventi.get(indiceIntervento).getLog(i)!=null)
        {
            Rilevazione u =  listaInterventi.get(indiceIntervento).getLog(i);
            Object[] nuovaRiga=
            {
                DateFormatConverter.long2dateString(u.getTimestamp().getTime(), 
                    FORMATO_DATA_GUI + "-" + FORMATO_ORA_GUI),
                u.getGpsLatitude(),
                u.getGpsLongitude(),
                u.getGpsAltitude(),
                u.getGpsAccuracy(),
                u.getAccX(),
                u.getAccY(),
                u.getAccZ() 
            };
            modelloLog.addRow(nuovaRiga);
            i++;
        }    
        tabellaLog.setModel(modelloLog);
    }
    
    private void pulsanteHomeActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if(GMessage.confirm(dialogoVerifica.getString("SEI SICURO DI VOLER USCIRE DAL ")
            + dialogoVerifica.getString("DIALOGO DELLA VERIFICA E TORNARE ALLA PIANIFICAZIONE?"))
        == JOptionPane.YES_OPTION) {
        this.dispose();
        }
    }                                            

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   
         if(GMessage.confirm(dialogoVerifica.getString("SEI SICURO DI VOLER USCIRE DAL DIALOGO DELLA VERIFICA?")
                 ) == JOptionPane.YES_OPTION) {
             this.dispose();
         }
    }                                  

	/**
	 * @param args the command line arguments
	 */
	public static void start() {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if (dialogoVerifica.getString("NIMBUS").equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DialogoVerifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DialogoVerifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DialogoVerifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DialogoVerifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				window = new DialogoVerifica(new javax.swing.JFrame(), true);
				Dimension risoluzioneSchermo = Toolkit.getDefaultToolkit().getScreenSize();
				window.setLocation(new Point( (risoluzioneSchermo.width - window.getWidth()) /2, (risoluzioneSchermo.height - window.getHeight()) /2)  );
				window.setVisible(true);
				
			}
		});
	}

	public void stop()
	{
		if (window!=null) window.dispose();
	}
    // Variables declaration - do not modify                     
    private javax.swing.JComboBox cbFileJournaling;
    private javax.swing.JLabel immagineTestataVerifica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane pannelloTabellaAttivita;
    private javax.swing.JScrollPane pannelloTabellaLog;
    private javax.swing.JButton pulsanteAggiornaLista;
    private javax.swing.JButton pulsanteCarica;
    private javax.swing.JButton pulsanteEsci;
    private javax.swing.JButton pulsanteHome;
    private javax.swing.JTable tabellaAttivita;
    private javax.swing.JTable tabellaLog;
    private javax.swing.JLabel txInfermiere;
    private javax.swing.JLabel txRilevazioni;
    // End of variables declaration                   

    @Override
    public void open() {
        start();
    }

}
