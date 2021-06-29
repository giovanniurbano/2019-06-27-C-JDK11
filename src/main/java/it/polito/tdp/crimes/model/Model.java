package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> vertici;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getCategorie() {
		return this.dao.getCategorie();
	}
	
	public List<LocalDate> listAllDates() {
		return this.dao.listAllDates();
	}
	
	public String creaGrafo(String cat, LocalDate data) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.vertici = this.dao.getVertici(cat, data);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
}
