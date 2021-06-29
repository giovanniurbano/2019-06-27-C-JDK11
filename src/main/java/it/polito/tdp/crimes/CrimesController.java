/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.boxArco.getItems().clear();
    	
    	txtResult.appendText("Crea grafo...\n");
    	String cat = this.boxCategoria.getValue();
    	LocalDate d = this.boxGiorno.getValue();
    	
    	if(d == null || cat == null) {
    		this.txtResult.appendText("Inserire categoria e giorno!");
    		return;
    	}
    	String msg = this.model.creaGrafo(cat, d);
    	this.txtResult.appendText(msg);
    	
    	List<Adiacenza> inferioriAMediano = this.model.getInferioriMediano();
    	this.txtResult.appendText("\nArchi con peso inferiore al peso mediano:\n");
    	for(Adiacenza a : inferioriAMediano)
    		this.txtResult.appendText(a + "\n");
    	
    	this.boxArco.getItems().addAll(inferioriAMediano);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare grafo!");
    		return;
    	}
    	
    	Adiacenza arco = this.boxArco.getValue();
    	if(arco == null) {
    		this.txtResult.appendText("Scegliere un arco!");
    		return;
    	}
    	List<String> percorso = this.model.calcolaPercorso(arco);
    	this.txtResult.appendText("Percorso:\n");
    	for(String s : percorso) {
    		this.txtResult.appendText(s + "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxCategoria.getItems().addAll(this.model.getCategorie());
    	this.boxGiorno.getItems().addAll(this.model.listAllDates());
    }
}
