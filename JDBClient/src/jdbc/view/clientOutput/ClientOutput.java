package jdbc.view.clientOutput;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientOutput extends Stage {
	
	// core values
	private List<QueryResultView> results;
	
	// elementi grafici
	Spinner<Integer> tableChooser;

	public ClientOutput(List<QueryResult> results, int width, int height) {
		
		// checks
		if ( results == null || results.isEmpty() )
			throw new IllegalArgumentException("Invalid QueryResult");
		
		this.results = results.stream().map(queryResult -> new QueryResultView(queryResult)).collect(Collectors.toList());
		
		BorderPane rootNode = new BorderPane();
		rootNode.setPadding(new Insets(10,10,10,10));
		Scene scene = new Scene(rootNode, width, height);
		this.setScene(scene);
		
		rootNode.setCenter(this.results.get(0));
		
		tableChooser = new Spinner<>();
		tableChooser.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, this.results.size()));
		tableChooser.getEditor().setStyle("-fx-alignment: CENTER");
		tableChooser.getEditor().setText("1 of " + results.size());
		tableChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		tableChooser.valueProperty().addListener((obs, oldV, newV) -> {
			tableChooser.getEditor().setText(newV.intValue() + " of " + results.size());
		});
		tableChooser.valueProperty().addListener( (obs, oldV, newV) -> {
			rootNode.setCenter(this.results.get(newV - 1));
		});
		BorderPane.setAlignment(tableChooser, Pos.CENTER);
		
		rootNode.setBottom(tableChooser);
		
		
	}

}
