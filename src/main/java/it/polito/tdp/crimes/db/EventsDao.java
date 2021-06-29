package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<LocalDate> listAllDates() {
		String sql = "SELECT DISTINCT date(reported_date) AS d "
				+ "FROM events "
				+ "ORDER BY d" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<LocalDate> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getDate("d").toLocalDate());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getCategorie(){
		final String sql = "SELECT DISTINCT offense_category_id FROM events";
		List<String> result = new LinkedList<String>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("offense_category_id"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<String> getVertici(String cat, LocalDate data) {
		final String sql = "SELECT DISTINCT offense_type_id "
				+ "FROM events "
				+ "WHERE offense_category_id = ? "
				+ "AND DATE(reported_date) = ?";
		List<String> result = new LinkedList<String>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, cat);
			st.setDate(2, Date.valueOf(data));
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("offense_type_id"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List<Adiacenza> getAdiacenze(String cat, LocalDate data, List<String> vertici) {
		final String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(DISTINCT e1.precinct_id) AS peso "
				+ "FROM events e1, events e2 "
				+ "WHERE e1.incident_id <> e2.incident_id "
				+ "AND e1.offense_type_id < e2.offense_type_id "
				+ "AND e1.precinct_id = e2.precinct_id "
				+ "AND date(e1.reported_date) = date(e2.reported_date) AND date(e1.reported_date) = ? "
				+ "AND e1.offense_category_id = e2.offense_category_id AND e1.offense_category_id = ? "
				+ "GROUP BY id1, id2";
		List<Adiacenza> result = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(data));
			st.setString(2, cat);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(vertici.contains(res.getString("id1")) && vertici.contains(res.getString("id2")) && !res.getString("id1").equals(res.getString("id2"))) {
					Adiacenza a = new Adiacenza(res.getString("id1"), res.getString("id2"), res.getDouble("peso"));
					result.add(a);
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
