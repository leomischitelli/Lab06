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
	private List<Citta> listaCitta;
	
	
	public Model() {
		this.meteoDAO = new MeteoDAO();
		this.listaCitta = new ArrayList<Citta>();
		
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		return this.meteoDAO.getAllRilevaementiMese(mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		
		return null;
		
	}
	

	private void sequenza_ricorsiva(List<Rilevamento> sequenzaAttuale, int livello) {
			
		
	}
	

}
