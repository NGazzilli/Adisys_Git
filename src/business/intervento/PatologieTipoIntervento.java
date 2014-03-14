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
    
    private ArrayList<PatologiaTO>[] listaPatologie = new ArrayList[0];
    
    public ArrayList<PatologiaTO> getListaPatologieTipoIntervento(int pos){
    	return listaPatologie[pos];
        //return listaPatologie.get(riga);
    }
    
    public void rewriteListaPatologieTipoIntervento(int pos, ArrayList<PatologiaTO> listaPatologie){
        this.listaPatologie[pos] = listaPatologie;
    }
    
    public void setListaPatologieTipoIntervento(ArrayList<PatologiaTO> lista){
    	int i;
    	ArrayList<PatologiaTO>[] temp = new ArrayList[listaPatologie.length + 1];
    	for (i = 0; i < listaPatologie.length; i++) 
         	temp[i] = listaPatologie[i];
    	listaPatologie = temp;
    	listaPatologie[listaPatologie.length - 1] = lista;
    }
    
    public void removeListaPatologieTipoIntervento(int riga){  
 		for (int i = riga; i < listaPatologie.length - 1; i++){
 			listaPatologie[i] = listaPatologie[i + 1]; //shiftare a sinistra
 		}
 		
 		ArrayList<PatologiaTO>[] temp = new ArrayList[listaPatologie.length - 1];
 		for(int i = 0; i < temp.length; i++) temp[i] = listaPatologie[i];
 		listaPatologie = temp;
    }
    
    public ArrayList<PatologiaTO>[] getListaInterventiPatologie(){
        return listaPatologie;
    }
      
}
