package jdbc.starter;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import jdbc.view.clientOutput.ClientOutput;
import jdbc.view.clientOutput.QueryResult;
import jdbc.view.clientOutput.Tuple;

public class starter extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Tuple t1 = new Tuple("1", "bianco", "Chianti");
		Tuple t2 = new Tuple("2", "Rosso", "Chiellini");
		List<Tuple> lista = new ArrayList<>();
		lista.add(t1);
		lista.add(t2);
		
		List<String> attributi = new ArrayList<>();
		attributi.add("Codice");
		attributi.add("Tipo");
		attributi.add("Vigneto");
		
		Tuple t3 = new Tuple("3", "verde", "macchenesoio");
		List<Tuple> lista1 = new ArrayList<>();
		lista1.add(t1);
		lista1.add(t2);
		lista1.add(t3);
		
		List<QueryResult> results = new ArrayList<>();
		results.add(new QueryResult(attributi, lista, "Wooowwwww"));
		results.add(new QueryResult(attributi, lista1));
		
		ClientOutput out = new ClientOutput(results, 500, 300);
		out.show();
		
	}

}