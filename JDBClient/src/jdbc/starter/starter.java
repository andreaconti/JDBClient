package jdbc.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.ClientUIController;
import jdbc.view.QueryError;
import jdbc.view.QueryResult;
import jdbc.view.Tuple;
import jdbc.view.events.DriversEvent;

public class starter extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientUIController c = new ClientUIController(Arrays.asList(ExportingOptions.values()), Arrays.asList(ExportingFormat.values()));
		c.show();
		c.appendOperationsDescription("Proviamo un po...");

		Tuple a1 = new Tuple("Andrea", "Conti", "3381382564");
		Tuple a2 = new Tuple("dfhjdfkd", "Ciao", "44546464");
		List<Tuple> tuples = new ArrayList<>();
		tuples.add(a1);
		tuples.add(a2);
		List<String> attributi = new ArrayList<>();
		attributi.add("Nome");
		attributi.add("Cognome");
		attributi.add("cellulare");
		
		List<QueryError> errors = new ArrayList<>();
		
		QueryResult result1 = new QueryResult(attributi, tuples, "prova prova", errors);
		List<QueryResult> results = new ArrayList<>();
		results.add(result1);
		c.showResults(results);
		
		c.addDriversEventHandler(DriversEvent.ALL_DRIVER_EVENTS, ev -> {
			System.out.println(ev.toString());
			
			if ( ev.getEventType().equals(DriversEvent.SHOW_DRIVER_REQUEST) )
				c.showDrivers(FXCollections.observableArrayList());
			
		});
	}

}