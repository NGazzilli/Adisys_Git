/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.intervento;

import business.patologia.PatologiaTO;
import java.util.ArrayList;


/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class PatologieTipoIntervento extends InterventoTO {
    
    private ArrayList<PatologiaTO>[] pathologiesList = new ArrayList[0];
    
    public ArrayList<PatologiaTO> getListaPatologieTipoIntervento(int pos){
    	return pathologiesList[pos];
        //return listaPatologie.get(riga);
    }
    
    public void rewriteListaPatologieTipoIntervento(int pos, ArrayList<PatologiaTO> pathologiesList){
        this.pathologiesList[pos] = pathologiesList;
    }
    
    public void setListaPatologieTipoIntervento(ArrayList<PatologiaTO> list){
    	int i;
    	ArrayList<PatologiaTO>[] temp = new ArrayList[pathologiesList.length + 1];
    	for (i = 0; i < pathologiesList.length; i++) 
         	temp[i] = pathologiesList[i];
    	pathologiesList = temp;
    	pathologiesList[pathologiesList.length - 1] = list;
    }
    
    public void removeListaPatologieTipoIntervento(int row){  
 		for (int i = row; i < pathologiesList.length - 1; i++){
 			pathologiesList[i] = pathologiesList[i + 1]; //shiftare a sinistra
 		}
 		
 		ArrayList<PatologiaTO>[] temp = new ArrayList[pathologiesList.length - 1];
 		for(int i = 0; i < temp.length; i++) temp[i] = pathologiesList[i];
 		pathologiesList = temp;
    }
    
    public ArrayList<PatologiaTO>[] getListaInterventiPatologie(){
        return pathologiesList;
    }
      
}
