package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.List;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getCategorie() {
		return this.dao.getCategorie();
	}
	
	public List<LocalDate> listAllDates() {
		return this.dao.listAllDates();
	}
}
