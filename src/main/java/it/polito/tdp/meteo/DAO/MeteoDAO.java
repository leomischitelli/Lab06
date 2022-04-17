package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT localita, data, umidita "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA) = ? AND localita = ? ";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Citta> getAllLocalitaMese(int mese){
		
		final String sql = "SELECT DISTINCT localita "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA) = ? ";
		
		List<Citta> localita = new ArrayList<Citta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				String s = rs.getString("Localita");
				localita.add(new Citta(s));
				
			}
			
			conn.close();
			return localita;
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
				
		
	}
	
	public String getAllRilevaementiMese(int mese){
		
		final String sql = "SELECT localita, AVG (umidita) AS u "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA) = ? "
				+ "GROUP BY localita ";
		
		String output = "";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				if(output != "")
					output+="\n";
				
				output += rs.getString("Localita") + " " + Integer.toString(rs.getInt("u"));
				
			}
			
			conn.close();
			return output;
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}


}
