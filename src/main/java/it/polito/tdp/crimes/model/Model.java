package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
	private List<Adiacenza> archi;
	
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
		archi = this.dao.getAdiacenze(cat, data, this.vertici);
		for(Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public List<Adiacenza> getInferioriMediano() {
		Double pesoMax = this.calcolaPesoMax();
		Double pesoMin = this.calcolaPesoMin();
		
		Double mediano = (pesoMax + pesoMin)/2;
		List<Adiacenza> inferiori = new ArrayList<Adiacenza>();
		for(Adiacenza a : archi)
			if(a.getPeso() < mediano)
				inferiori.add(a);
		
		return inferiori;
	}

	private Double calcolaPesoMin() {
		Double min = 1000.0;
		for(Adiacenza a : archi)
			if(a.getPeso() < min)
				min = a.getPeso();
		
		return min;
	}

	private Double calcolaPesoMax() {
		Double max = 0.0;
		for(Adiacenza a : archi)
			if(a.getPeso() > max)
				max = a.getPeso();
		
		return max;
	}
}
