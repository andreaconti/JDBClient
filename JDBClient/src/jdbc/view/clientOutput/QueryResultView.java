package jdbc.view.clientOutput;

import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class QueryResultView extends VBox {
	
	private QueryResult toShow;
	
	private TextArea description;
	private TextArea errors;
	
	public QueryResultView(QueryResult toShow) {
		super(10);
		this.setPadding(new Insets(10,0,10,0));
		
		if ( toShow == null)
			throw new NullPointerException("QueryResul == null not allowed");
		
		this.description = new TextArea(toShow.getDescriptionOfResult());
		this.errors = new TextArea(toShow.getErrorsToShow().stream().map(err -> err.getErrorCode() + " - " + err.getDescription()).collect(Collectors.joining("\n")));
		this.description.setEditable(false);
		this.errors.setEditable(false);
		
		this.getChildren().addAll(description, toShow.getTableResult(), errors);
		
	}

}
