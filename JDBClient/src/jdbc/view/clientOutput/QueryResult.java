package jdbc.view.clientOutput;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jdbc.view.QueryError;

public class QueryResult {
	
	// tableResult
	private List<String> attributes;
	private List<Tuple> tuples;
	private TableView<Tuple> tableResult;
	
	private String descriptionOfResult;
	private List<QueryError> errorsToShow;

	/* CONSTRUCTORS */
	public QueryResult(List<String> attributes, List<Tuple> tuples, String descriptionOfResult, List<QueryError> errorsToShow) {
		
		// controlli null
		if ( attributes == null || tuples == null || descriptionOfResult == null || errorsToShow == null )
			throw new NullPointerException("Null values not ammitted");
		
		// controlli not empty
		if ( attributes.isEmpty() )
			throw new IllegalArgumentException("Attributes Empty non ammitted");
		
		// controlli omogeneità
		tuples.forEach( tuple -> {
			if (tuple.size() != attributes.size())
				throw new IllegalArgumentException("tuples lenght does not match the attributes size");
		});
		
		this.attributes = attributes;
		this.tuples = tuples;
		this.descriptionOfResult = descriptionOfResult;
		this.errorsToShow = errorsToShow;
		
		// creo tableView
		tableResult = new TableView<Tuple>(FXCollections.observableArrayList(tuples));
		tableResult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TuplePropertyValueFactory tuplePropertyValueFactory = new TuplePropertyValueFactory(0, attributes.size());
		for ( String attribute : attributes ) {
			TableColumn<Tuple, String> col = new TableColumn<>(attribute);
			col.setCellValueFactory( tuplePropertyValueFactory );
			tableResult.getColumns().add(col);
		}
		
	}
	
	public QueryResult(List<String> attributes, List<Tuple> tuples, String descriptionOfResult ) {
		this(attributes, tuples, descriptionOfResult, new ArrayList<QueryError>());
	}
	
	public QueryResult(List<String> attributes, List<Tuple> tuples, List<QueryError> errorsToShow ) {
		this(attributes, tuples, "", errorsToShow);
	}
	
	public QueryResult(List<String> attributes, List<Tuple> tuples ) {
		this(attributes, tuples, "");
	}
	
	public QueryResult(List<String> attributes) {
		this(attributes, new ArrayList<Tuple>());
	}
	
	/* GETTERS AND SETTERS */

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public List<Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(List<Tuple> tuples) {
		this.tuples = tuples;
	}

	public TableView<Tuple> getTableResult() {
		return tableResult;
	}

	public void setTableResult(TableView<Tuple> tableResult) {
		this.tableResult = tableResult;
	}

	public String getDescriptionOfResult() {
		return descriptionOfResult;
	}

	public void setDescriptionOfResult(String descriptionOfResult) {
		this.descriptionOfResult = descriptionOfResult;
	}

	public List<QueryError> getErrorsToShow() {
		return errorsToShow;
	}

	public void setErrorsToShow(List<QueryError> errorsToShow) {
		this.errorsToShow = errorsToShow;
	}

}

class TuplePropertyValueFactory extends PropertyValueFactory<Tuple, String> {
	
	private int index;
	private int tupleSize;
	
	public TuplePropertyValueFactory(int index, int tupleSize) {
		super("");
		this.index = index;
		this.tupleSize = tupleSize;
	}
	
	@Override
	public ObservableValue<String> call(TableColumn.CellDataFeatures<Tuple,String> param) {
		
		String value = param.getValue().getValuesData().get(index);
		index++;
		if ( index == tupleSize ) index = 0;
		return new SimpleStringProperty(value);
		
	}
	
}





