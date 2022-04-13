package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO meteoDAO;
	private List<Rilevamento> sequenzaMigliore;
	private int costoMinimo; 
	private List<Citta> listaCitta;
	
	public Model() {
		this.meteoDAO = new MeteoDAO();
		this.sequenzaMigliore = new ArrayList<Rilevamento>();
		costoMinimo = -1;
		this.listaCitta = new ArrayList<Citta>();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		return this.meteoDAO.getAllRilevaementiMese(mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		
		List<Rilevamento> sequenzaAttuale = new ArrayList<Rilevamento>();
		
		sequenzaAttuale.clear();
		
		for(Citta c : this.meteoDAO.getAllLocalitaMese(mese)) {
			//salvo i rilevamenti in ogni citta
			c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			listaCitta.add(c);
		}
		
		for(Citta c : listaCitta) {
			for(int i=0; i<3; i++) {
				Rilevamento r = c.getRilevamenti().get(i);
				sequenzaAttuale.add(r);
				c.increaseCounter();
			}
			
			sequenza_ricorsiva(sequenzaAttuale, 3); //livello da 0 a 14
			for(Citta cc : listaCitta) {
				cc.resetAll();
			}
			sequenzaAttuale.clear();
		}
		
		
		return sequenzaMigliore;
		
	}

	private void sequenza_ricorsiva(List<Rilevamento> sequenzaAttuale, int livello) {
		
		if(sequenzaAttuale.size() == 15) {
			//caso terminale, calcola costo, fai cose, verifica presenza di tutte le citta
			List<Citta> controllo = new ArrayList<Citta>();
			for(Rilevamento r : sequenzaAttuale) {
				if(!controllo.contains(r.getCitta()))
					controllo.add(r.getCitta());
			}
			if(listaCitta.size() != controllo.size())
				return;
			
			//ogni citta e' presente almeno una volta
			
			int costoAttuale = sequenzaAttuale.get(0).getUmidita();
			for(int i = 1; i < 15; i++) {
				costoAttuale += sequenzaAttuale.get(i).getUmidita();
				if(!sequenzaAttuale.get(i).getCitta().equals(sequenzaAttuale.get(i-1).getCitta()))
					costoAttuale += COST;
			}
			
			if(costoAttuale < costoMinimo) {
				costoMinimo = costoAttuale;
				sequenzaMigliore.clear();
				sequenzaMigliore.addAll(sequenzaAttuale);
			}
		}
		
		Rilevamento ultimoRilevamento = sequenzaAttuale.get(sequenzaAttuale.size() - 1);
		Citta ultimaCitta = ultimoRilevamento.getCitta();
		int ultimoCounter = ultimaCitta.getCounter();
		
		if(ultimoCounter > 0 && ultimoCounter < 3) {
			while(ultimoCounter < 3 && sequenzaAttuale.size() < 15) {
				
				Rilevamento r = ultimaCitta.getRilevamenti().get(livello);
				sequenzaAttuale.add(r);
				ultimaCitta.increaseCounter();
				livello++;
			}
			sequenza_ricorsiva(sequenzaAttuale, livello);
		}
	
		for(Citta c : listaCitta) {
			int counter = c.getCounter();
			if(counter < 6) { //se c = 6 salta la citta
				Rilevamento r = c.getRilevamenti().get(livello);
				sequenzaAttuale.add(r);
				c.increaseCounter();
				sequenza_ricorsiva(sequenzaAttuale, livello + 1);
				sequenzaAttuale.remove(sequenzaAttuale.size() - 1);
			}
		}
		
		
		
		
		
		
		
		
	}
	

}
