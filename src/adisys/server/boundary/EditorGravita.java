/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.boundary;

import business.intervento.PatologieTipoIntervento;
import business.patologia.PatologiaTO;
import messaggistica.GMessage;
import integration.dao.InterventoMySqlDAO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/**
*
* @author Gianmarco Divittorio
* @author Nicola Gazzilli
*/
public class EditorGravita extends javax.swing.JDialog implements Boundary {
    
    private static EditorInterventi tmp = null;
    private static int rigaPatologia = -1;
    private static String codice = "";
    private static int rigaTipoIntervento = -1;
    private static int gravita = 0;
    private static ResourceBundle editorGravita = ResourceBundle.getBundle("adisys/server/property/EditorGravita");
    private PatologieTipoIntervento listaPatologieTipoIntervento;
    
    public static void setResourceBundle(String path, Locale locale){
             editorGravita = ResourceBundle.getBundle(path, locale);
    }
    
    public static void setRigaPatologia(int riga, String cod){
        rigaPatologia = riga;
        codice = cod;
    }   
    
    public static void setRigaTipoIntervento(int riga){
        rigaTipoIntervento = riga;
    }   
    
    public static void setGravita(int value){
        gravita = value;
    }   
    
    public static void setEditorInterventi(EditorInterventi editor){
        tmp = editor;
    }
	/**
	 * Creates new form dialogoInfermieri
	 */
	public EditorGravita(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
                groupButton();
                setRadioButton();
                listaPatologieTipoIntervento = tmp.getPatologieTipoIntervento();
	}

        public EditorGravita(){
            
        }
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        immagineTestataInfermieri = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        pulsanteOK = new javax.swing.JButton();
        pulsanteEsci = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(editorGravita.getString("TITLE")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 160));
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        immagineTestataInfermieri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/EditorGravita.png"))); // NOI18N

        jLabel25.setText(editorGravita.getString("SELEZIONA LA GRAVIT� DA ASSOCIARE ALLA PATOLOGIA SELEZIONATA")); // NOI18N

        jRadioButton1.setText(editorGravita.getString("1")); // NOI18N

        jRadioButton2.setText(editorGravita.getString("2")); // NOI18N

        jRadioButton3.setText(editorGravita.getString("3")); // NOI18N

        jRadioButton4.setText(editorGravita.getString("4")); // NOI18N

        jRadioButton5.setText(editorGravita.getString("5")); // NOI18N

        pulsanteOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/iconainseriscirossa.png"))); // NOI18N
        pulsanteOK.setText(editorGravita.getString("OK")); // NOI18N
        pulsanteOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteOKActionPerformed(evt);
            }
        });

        pulsanteEsci.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/indietro.png"))); // NOI18N
        pulsanteEsci.setText(editorGravita.getString("EditorGravita.pulsanteEsci.text")); // NOI18N
        pulsanteEsci.setToolTipText(editorGravita.getString("EditorGravita.pulsanteEsci.toolTipText")); // NOI18N
        pulsanteEsci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteEsciActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pulsanteEsci)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pulsanteOK, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(immagineTestataInfermieri)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addComponent(jLabel25))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(jRadioButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton5)))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(immagineTestataInfermieri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pulsanteEsci, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pulsanteOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   
         if(GMessage.confirm(editorGravita.getString("SEI SICURO DI VOLER USCIRE DALL")
                 + editorGravita.getString("EDITOR DELLA GRAVIT� DELLA PATOLOGIA? "))   
                 == JOptionPane.YES_OPTION) {
             //tmp.unsetRow(selectedRow);
             this.dispose();
         }
    }                                  

    private void pulsanteOKActionPerformed(java.awt.event.ActionEvent evt) {                                           

        //Aggiunta paziente
        String txGravita = "";
        if(jRadioButton1.isSelected()){
            txGravita = jRadioButton1.getText();
        }
        if(jRadioButton2.isSelected()){
            txGravita = jRadioButton2.getText();
        }
        if(jRadioButton3.isSelected()){
            txGravita = jRadioButton3.getText();
        }
        if(jRadioButton4.isSelected()){
            txGravita = jRadioButton4.getText();
        }
        if(jRadioButton5.isSelected()){
            txGravita = jRadioButton5.getText();
        }
        
        if(Integer.parseInt(txGravita) == gravita)
            this.dispose();
        else {
              if(GMessage.confirm(editorGravita.getString("SI VUOLE MODIFICARE")
                      + editorGravita.getString("LA GRAVIT� DELLA PATOLOGIA SELEZIONATA DA ")
                      + gravita + java.text.MessageFormat.format(editorGravita.getString(" A {0}. DESIDERI CONFERMARE?"), new Object[] {txGravita}))   
                      == JOptionPane.YES_OPTION) { 
                  String[] colonne = tmp.columnModelTabellaPatologieTipoIntervento();
                  int colonnaGravita = -1;
                  for(int i = 0; i < colonne.length; i++){
                      if(colonne[i].equals(InterventoMySqlDAO.NOME_COLONNA_GRAVITA_PATOLOGIE_TIPI_INTERVENTI))
                          colonnaGravita = i;
                  }
                  int numTipiIntervento = tmp.getTabellaTipiIntervento().getRowCount();
                  if(numTipiIntervento > 1){
                      int valoreScelto = Integer.parseInt(txGravita);
                      boolean trovato = false;
                      int precGravita = 0;
                      int tipoInterventoSelezionato = tmp.getTabellaTipiIntervento().getSelectedRow();
                      ArrayList<PatologiaTO>[] listaPatologie = listaPatologieTipoIntervento.getListaInterventiPatologie();
                      for(int i = 0; i < listaPatologie.length; i++){
                          for(PatologiaTO e : listaPatologie[i]){
                              if(e.getCodice().equals(this.codice) && e.getGravita() != valoreScelto 
                                      && i != tipoInterventoSelezionato){
                                  precGravita = e.getGravita();
                                  trovato = true;
                                  break;
                              }//if su patologia gi� trovata con valore diverso
                              if(trovato == true)
                                  break;
                          }//for-each su patologie
                      }//for su listaPatologie
                      if(trovato == true){
                          String msg =  editorGravita.getString("SI VUOLE IMPOSTARE ")
                                  + java.text.MessageFormat.format(editorGravita.getString("LA GRAVIT� {0} PER UN PATOLOGIA "), new Object[] {valoreScelto})
                                  + editorGravita.getString(" ASSOCIATA AD ALTRI TIPI DI INTERVENTO ")
                                  + java.text.MessageFormat.format(editorGravita.getString("CON UNA GRAVIT� DIVERSA ({0})."), new Object[] {precGravita})
                                  + editorGravita.getString("SE SI SCEGLIE DI PROSEGUIRE VERR� MODIFICATA ANCHE ")
                                  + editorGravita.getString("LA GRAVITA DELLA STESSA PATOLOGIA ASSOCIATA ")
                                  + editorGravita.getString("AGLI ALTRI TIPI DI INTERVENTO ")
                                  +  java.text.MessageFormat.format(editorGravita.getString("DA {0} A {1}."), new Object[] {precGravita, valoreScelto})
                                  + editorGravita.getString("ALTRIMENTI ALLA RIGA SELEZIONATA VERR� SETTATA LA ")
                                  + java.text.MessageFormat.format(editorGravita.getString("GRAVIT� {0} GI� PRECEDENTEMENTE "), new Object[] {precGravita})
                                  + editorGravita.getString("IMPOSTATA.");
                          if(GMessage.confirm(msg) == JOptionPane.YES_OPTION) { 
                              for(int i = 0; i < listaPatologie.length; i++){
                                  for(PatologiaTO e : listaPatologie[i]){
                                      if(e.getCodice().equals(codice)
                                              && e.getGravita() != valoreScelto){
                                          e.setGravita(valoreScelto);
                                          tmp.addValueTabellaPatologie(valoreScelto, rigaPatologia, 
                                                  colonnaGravita);
                                          break;
                                      }//if su codice e gravit� uguali
                                  }//for-each
                              }//for su lista patologie
                              this.dispose();
                          }//if su rispota si al secondo messaggio di avviso
                      } else {
                          tmp.addValueTabellaPatologie(precGravita, rigaPatologia, 
                                  colonnaGravita);
                      }//else su trovato = true
                  }//if su numero tipi intervento > 0          
                  tmp.addValueTabellaPatologie(txGravita, rigaPatologia, 
                                  colonnaGravita);
                   this.dispose();
              }//if su risposta si al primo messaggio di avviso   
             
        }//if su valore scelto != dal valore passato
          
    }                                          

    private void pulsanteEsciActionPerformed(java.awt.event.ActionEvent evt) {                                             

        String msg = editorGravita.getString("SEI SICURO DI VOLER USCIRE DAL PANNELLO?");
        if(GMessage.confirm(msg) == JOptionPane.YES_OPTION)
            this.dispose();
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
				if (editorGravita.getString("NIMBUS").equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(EditorGravita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(EditorGravita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(EditorGravita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(EditorGravita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				EditorGravita window = new EditorGravita(new javax.swing.JFrame(), true);
				Dimension risoluzioneSchermo = Toolkit.getDefaultToolkit().getScreenSize();
				window.setLocation(new Point( (risoluzioneSchermo.width - window.getWidth()) /2, (risoluzioneSchermo.height - window.getHeight()) /2)  );
				window.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify                     
    private javax.swing.JLabel immagineTestataInfermieri;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JButton pulsanteEsci;
    private javax.swing.JButton pulsanteOK;
    // End of variables declaration                   

    private void groupButton(){
         ButtonGroup bg1 = new ButtonGroup();
            
            bg1.add(jRadioButton1);
            bg1.add(jRadioButton2);
            bg1.add(jRadioButton3);
            bg1.add(jRadioButton4);
            bg1.add(jRadioButton5);
         
    }
    
    private void setRadioButton() {
        
           
        if(gravita == 1)
         jRadioButton1.setSelected(true);
        if(gravita == 2)
            jRadioButton2.setSelected(true);
        if(gravita == 3)
            jRadioButton3.setSelected(true);
        if(gravita == 4)
            jRadioButton4.setSelected(true);
        if(gravita == 5)
            jRadioButton5.setSelected(true);
        
    }

    @Override
    public void open() {
        start();
    }
    
}
