

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.boundary;

import adisys.server.strumenti.DateFormatConverter;
import business.ApplicationController;
import business.configurazione.Configurazione;
import business.infermiere.InfermiereTO;
import business.intervento.InterventoCompletoTO;
import business.intervento.InterventoTO;
import business.intervento.TipoIntervento;
import business.journaling.Journaling;
import business.patologia.PatologiaTO;
import business.paziente.PazienteTO;
import business.pianificazione.StrutturaInterscambio;
import integration.LinkingDb;
import integration.dao.CreateDBFromScript;
import integration.dao.PazienteMySqlDAO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.Main;
import messaggistica.MainException;
import presentation.FrontController;
import presentation.RequestManager;

/**
*
* @author Gianmarco Divittorio
* @author Nicola Gazzilli
*/
public class EditorLanguage extends javax.swing.JFrame implements Boundary {

    @Override
    public void open() {
        start();
    }

        private static enum Language{italiano, inglese};
        private static Language linguaggio = Language.italiano;
        private FrontController FC;
        
	/**
	 * Creates new form ADISysGUI
	 */
	public EditorLanguage() {
		initComponents();
                fieldCombo();
	}

        private void fieldCombo(){
            if(linguaggio ==  Language.italiano){
                jLocaleChooser1.setSelectedItem("italiano (Italia)");
            }
                
            else if(linguaggio ==  Language.inglese){
                jLocaleChooser1.setSelectedItem("inglese (Regno Unito)");
            }
               
        }
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pulsanteAvanti = new javax.swing.JButton();
        imgLanguage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLocaleChooser1 = new com.toedter.components.JLocaleChooser();
        barraDeiMenu = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Opzioni lingua");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setMinimumSize(null);

        pulsanteAvanti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/pulsanti/freccia_verde_256x256.png"))); // NOI18N
        pulsanteAvanti.setText("Avanti");
        pulsanteAvanti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteAvantiActionPerformed(evt);
            }
        });

        imgLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/italiano.jpg"))); // NOI18N

        jLabel1.setText("Seleziona la lingua:");

        jLocaleChooser1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "italiano (Italia)", "inglese (Regno Unito)" }));
        jLocaleChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLocaleChooser1ActionPerformed(evt);
            }
        });

        barraDeiMenu.setRequestFocusEnabled(false);
        setJMenuBar(barraDeiMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pulsanteAvanti, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(imgLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLocaleChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgLanguage)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLocaleChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pulsanteAvanti)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    
    private void pulsanteAvantiActionPerformed(java.awt.event.ActionEvent evt) {                                               
    	 this.dispose();
         try {
             request(RequestManager.OPEN_PLANNER);
         } catch (MainException ex) {
             
         }
    }           
    
    
    
    private void request(String request) throws MainException{
        FC = RequestManager.getFCInstance();
        RequestManager.getFCInstance().addService();
        if(FC.processRequest(request)){
            System.out.println("Finestra " + request + " aperta con successo.");
        }
        else{
            messaggistica.GMessage.winNotFound(request);
        }
    }
       
    private void jLocaleChooser1ActionPerformed(java.awt.event.ActionEvent evt) {                                                
        RequestManager.setResourceBundle("adisys/server/property/RequestManager", jLocaleChooser1.getLocale());
        PazienteMySqlDAO.setResourceBundle("adisys/server/property/PazienteDAO", jLocaleChooser1.getLocale());
        Main.setResourceBundle("adisys/server/property/Main", jLocaleChooser1.getLocale());
        LinkingDb.setResourceBundle("adisys/server/property/LinkingDb", jLocaleChooser1.getLocale());
        Journaling.setResourceBundle("adisys/server/property/Journaling", jLocaleChooser1.getLocale());
        Configurazione.setResourceBundle("adisys/server/property/Configurazione", jLocaleChooser1.getLocale());
        ApplicationController.setResourceBundle("adisys/server/property/ApplicationController", jLocaleChooser1.getLocale());
        InterventoCompletoTO.setResourceBundle("adisys/server/property/InterventoCompleto", jLocaleChooser1.getLocale());
        TipoIntervento.setResourceBundle("adisys/server/property/TipoIntervento", jLocaleChooser1.getLocale());
        InfermiereTO.setResourceBundle("adisys/server/property/Infermiere", jLocaleChooser1.getLocale());
        PazienteTO.setResourceBundle("adisys/server/property/Paziente", jLocaleChooser1.getLocale());
        PatologiaTO.setResourceBundle("adisys/server/property/Patologia", jLocaleChooser1.getLocale());
        InterventoTO.setResourceBundle("adisys/server/property/Intervento", jLocaleChooser1.getLocale());
        StrutturaInterscambio.setResourceBundle("adisys/server/property/StrutturaInterscambio", jLocaleChooser1.getLocale());
        DateFormatConverter.setResourceBundle("adisys/server/property/DateFormatConverter", jLocaleChooser1.getLocale());
        DialogoStorico.setResourceBundle("adisys/server/property/DialogoStorico", jLocaleChooser1.getLocale());
        EditorRipetiPatologie.setResourceBundle("adisys/server/property/EditorRipetiPatologie", jLocaleChooser1.getLocale());
        DialogoEsportazione.setResourceBundle("adisys/server/property/DialogoEsportazione", jLocaleChooser1.getLocale());
        DialogoVerifica.setResourceBundle("adisys/server/property/DialogoVerifica", jLocaleChooser1.getLocale());
       
        Pianificatore.setResourceBundle("adisys/server/property/Pianificatore", jLocaleChooser1.getLocale());
        EditorPazienti.setResourceBundle("adisys/server/property/EditorPazienti", jLocaleChooser1.getLocale());
        EditorPatologieTipoIntervento.setResourceBundle("adisys/server/property/EditorPatologieTipoIntervento", 
                jLocaleChooser1.getLocale());
        EditorPatologie.setResourceBundle("adisys/server/property/EditorPatologie", jLocaleChooser1.getLocale());
        EditorInterventi.setResourceBundle("adisys/server/property/EditorInterventi", jLocaleChooser1.getLocale());
        EditorInfermieri.setResourceBundle("adisys/server/property/EditorInfermieri", jLocaleChooser1.getLocale());
        EditorGravita.setResourceBundle("adisys/server/property/EditorGravita",  jLocaleChooser1.getLocale());
        String language = jLocaleChooser1.getLocale().getLanguage() + "_" + jLocaleChooser1.getLocale().getCountry();
        if(language.equals("it_IT")){
            imgLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/italiano.jpg"))); // NOI18N
            jLabel1.setText("Selezionare la lingua:");
            pulsanteAvanti.setText("Avanti");
            this.setTitle("ADISys - Opzioni lingua");
            linguaggio = Language.italiano;
        } else {
            imgLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adisys/server/img/decorazioniFinestre/inglese.jpg"))); // NOI18N
            jLabel1.setText("Select the language:");
            pulsanteAvanti.setText("Next");
            this.setTitle("ADISys - Language options");
            linguaggio = Language.inglese;
        }
            
    }                                               

	/**
	 *
	 */
	public static void start() {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(EditorLanguage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(EditorLanguage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(EditorLanguage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(EditorLanguage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
                                EditorLanguage window = new EditorLanguage();
                                Dimension risoluzioneSchermo = Toolkit.getDefaultToolkit().getScreenSize();
                                window.setLocation(new Point( (risoluzioneSchermo.width - window.getWidth()) /2, (risoluzioneSchermo.height - window.getHeight()) /2)  );
                                window.setVisible(true);
			}
		});

	}
        
    // Variables declaration - do not modify                     
    private javax.swing.JMenuBar barraDeiMenu;
    private javax.swing.JLabel imgLanguage;
    private javax.swing.JLabel jLabel1;
    private com.toedter.components.JLocaleChooser jLocaleChooser1;
    private javax.swing.JButton pulsanteAvanti;
    // End of variables declaration                   

}

