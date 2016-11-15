package jdbc.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.stage.Screen;
import jdbc.view.clientInput.ClientInput;
import jdbc.view.clientOutput.ClientOutput;
import jdbc.view.events.*;
import jdbc.view.events.ConnectionEvent;
import jdbc.view.events.HistoryFileEvent;

public class ClientUIController {
	
	private ClientInput mainView;
	private final double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
	private final double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
	
	private Map<EventHandler<? extends ClientEvent>, EventType<? extends ClientEvent>> handlersForClientOuput = new HashMap<>();
	
	/* COSTRUTTORI */
	public ClientUIController(String username, ObservableList<String> databases) {
		mainView = new ClientInput(username, databases, width, height);
		initFiringEvents();
	}
	
	public ClientUIController(String username) {
		mainView = new ClientInput(username, width, height);
		initFiringEvents();
	}
	
	public ClientUIController(ObservableList<String> databases) {
		mainView = new ClientInput(databases, width, height);
		initFiringEvents();
	}
	
	public ClientUIController() {
		mainView = new ClientInput(width, height);
		initFiringEvents();
	}
	
	/* TUTTI I GETTERS E SETTER */
	public String getQuery() {
		return mainView.getQuery();
	}
	
	public void resetQuery() {
		mainView.resetQuery();
	}
	
	public void addError(QueryError toAdd) {
		mainView.addError(toAdd);
	}
	
	public void resetErrors() {
		mainView.resetErrors();
	}
	
	public String getUsername() {
		return mainView.getUsername();
	}
	
	public void setUsername(String username) {
		mainView.setUsername(username);
	}
	
	public void resetUsername() {
		mainView.resetUsername();
	}

	public String getPassword() {
		return mainView.getPassword();
	}
	
	public void resetPassword() {
		mainView.resetPassword();
	}
	
	// isConnected
	public void setConnection(boolean connection) {
		mainView.setConnection(connection);
	}
	
	public boolean isConnected() {
		return mainView.isConnected();
	}
	
	/* METODI PER MOSTRARE I RISULTATI E MAIN VIEW */
	
	public void show() {
		mainView.show();
	}
	
	public void showResults(List<QueryResult> results) {
		double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
		double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
		ClientOutput out = new ClientOutput(results, width, height);
		out.show();
	}
	
	/* METODI PER GESTIRE GLI EVENTI DELLA UI */
	
	public void addAddRemoveDatabaseEventHandler( EventHandler<AddRemoveDatabaseEvent> handler, EventType<? extends AddRemoveDatabaseEvent> type) {
		mainView.addEventHandler(type, handler);
	}
	public void addConnectionEventHandler( EventHandler<ConnectionEvent> handler, EventType<? extends ConnectionEvent> type) {
		mainView.addEventHandler(type, handler);
	}
	public void addExportQueryResultEventHandler( EventHandler<ExportQueryResultEvent> handler, EventType<? extends ExportQueryResultEvent> type) {
		handlersForClientOuput.put(handler, type);
	}
	public void addHistoryFileEventHandler( EventHandler<HistoryFileEvent> handler, EventType<? extends HistoryFileEvent> type) {
		mainView.addEventHandler(type, handler);
	}

	/* METODI PRIVATI PER SPARARE EVENTI */

	private void initFiringEvents() {

		mainView.addListenerOnAddDatabaseRequest( ev -> {
			Optional<String> db = mainView.getDatabaseToAddOrDelete();
			if (db.isPresent()) {
				AddRemoveDatabaseEvent toFire = new AddRemoveDatabaseEvent(AddRemoveDatabaseEvent.ADD_DATABASE_REQUEST, db.get());
				mainView.fireEvent(toFire);
			}
		});
		
		mainView.addListenerOnDeleteDatabaseRequest( ev -> {
			Optional<String> db = mainView.getDatabaseToAddOrDelete();
			if (db.isPresent()) {
				AddRemoveDatabaseEvent toFire = new AddRemoveDatabaseEvent(AddRemoveDatabaseEvent.REMOVE_DATABASE_REQUEST, db.get());
				mainView.fireEvent(toFire);
			}
		});
		
		mainView.addListenerOnConnectionRequest( ev -> {
			ConnectionEvent toFire = new ConnectionEvent(this, this.getUsername(), this.getPassword(), mainView.getdatabaseURLSelected(), ConnectionEvent.CONNECTION_REQUEST);
			mainView.fireEvent(toFire);
		});
		
		mainView.addListenerOnDisconnectionRequest( ev -> {
			ConnectionEvent toFire = new ConnectionEvent(this, this.getUsername(), this.getPassword(), mainView.getdatabaseURLSelected(), ConnectionEvent.DISCONNECTION_REQUEST);
			mainView.fireEvent(toFire);
		});
		
		mainView.addListenerOnExportOnFileRequest( ev -> {
			HistoryFileEvent toFire = new HistoryFileEvent(HistoryFileEvent.ANY, 
					mainView.getExportingOptionsSelected().get(), 
					mainView.getExportingFormatSelected().get(), 
					mainView.getdatabaseURLSelected());
			mainView.fireEvent(toFire);
		});
		
		mainView.addListenerOnQueryRequest( ev -> {
			SendQueryEvent toFire = new SendQueryEvent(SendQueryEvent.ANY, this, mainView.getQuery());
			mainView.fireEvent(toFire);
		});
	}
}
