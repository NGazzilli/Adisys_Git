/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adisys.server.strumenti;


import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luca
 */
public class VariableTableModel extends DefaultTableModel

{
  
    
    @Override
  public boolean isCellEditable(int riga, int colonna)
  {
     
     /* if(colonna == EDIT_COLUMN)
          return true;*/
      return false;
  }
    
} 
