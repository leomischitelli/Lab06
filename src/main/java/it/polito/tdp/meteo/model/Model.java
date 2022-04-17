package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO meteoDAO;
	private List<Citta> sequenzaMigliore;
	private int costoMinimo; 
	private Set<Citta> setCitta;
	
	public Model() {
		this.meteoDAO = new MeteoDAO();
		this.sequenzaMigliore = new ArrayList<Citta>();
		costoMinimo = -1;
		this.setCitta = new HashSet<Citta>();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		return this.meteoDAO.getAllRilevaementiMese(mese);
	}
	
	public void initialize() {
		
		setCitta.clear();
		sequenzaMigliore.clear();
		costoMinimo = -1;
		
	}
	
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		initialize();
		
		for(Citta c : this.meteoDAO.getAllLocalitaMese(mese)) {
			//salvo i rilevamenti in ogni citta
			c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			setCitta.add(c);
		}
	
		sequenza_ricorsiva(new ArrayList<Citta>(), 0); //livello da 0 a 14	
		
		return sequenzaMigliore;
		
	}

	private boolean sequenza_ricorsiva(List<Citta> sequenzaAttuale, int livello) {
		
		if(sequenzaAttuale.size() == NUMERO_GIORNI_TOTALI) {
			//controllare che ci sia ogni citta almeno una volta
	
			for(Citta c : sequenzaAttuale) {
				if(!setCitta.contains(c))
					return false; //false
			}
			
			//tutte le citta sono presenti almeno una volta
			int costoAttuale = sequenzaAttuale.get(0).aumentaCosto(0, 0); //umidita del primo rilevamento della prima citta
		
			for(int i=1; i<NUMERO_GIORNI_TOTALI; i++) {
//				costoAttuale += sequenzaAttuale.get(i).getRilevamenti().get(i).getUmidita();
				costoAttuale = sequenzaAttuale.get(i).aumentaCosto(costoAttuale, i);
				
				if(!sequenzaAttuale.get(i).equals(sequenzaAttuale.get(i-1))) //citta diversa dalla precedente
					costoAttuale += COST;
					
			}
			
			if(costoAttuale < costoMinimo || costoMinimo < 0) { //costoMinimo settato a -1 per iniziare
				costoMinimo = costoAttuale;
				sequenzaMigliore.clear();
				sequenzaMigliore.addAll(sequenzaAttuale);
				
				return true; //true
			}

		}
		
		
		//seleziono una citta e vedo se supera tutti i controlli
		for(Citta c : setCitta) {
			if(c.getCounter() < NUMERO_GIORNI_CITTA_MAX) { //posso inserirlo, altrimenti salta alla prossima citta
				do {
					sequenzaAttuale.add(c);
					c.increaseCounter();
					livello++;
				} while (livello < NUMERO_GIORNI_TOTALI && c.getCounter() < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN); //ripete automaticamente i passi finche il counter non arriva a 3
					
				sequenza_ricorsiva(sequenzaAttuale, livello);
				do {
					//backtracking
					c.decreaseCounter();
					sequenzaAttuale.remove(sequenzaAttuale.size() - 1);
					livello--;
				} while (c.getCounter() > 0 && c.getCounter() < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN);
					
				
				
			}
		}
		
		return false; //false, non ho inserito nulla, resetta tutto
		
		
				
		
		
		
		
	}
	

}
