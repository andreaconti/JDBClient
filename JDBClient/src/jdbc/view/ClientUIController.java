package jdbc.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.stage.Screen;
import javafx.util.Pair;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.clientInput.ClientInput;
import jdbc.view.clientOutput.ClientOutput;
import jdbc.view.events.*;
import jdbc.view.events.ConnectionEvent;
import jdbc.view.events.HistoryFileEvent;

public class ClientUIController {
	
	private ClientInput mainView;
	private final double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
	private final double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
	private List<ExportingOptions> exportingOptions;
	private List<ExportingFormat> exportingFormats;
	
	private List<Pair<EventHandler<? super SendQueryEvent>, EventType<? extends SendQueryEvent>>> eventHandlers = new ArrayList<>();
	
	/* COSTRUTTORI */
	public ClientUIController(String username, ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		mainView = new ClientInput(username, databases, optionsList, formatsList, width, height);
		mainView.setOptionsList(Arrays.asList(ExportingOptions.values()));
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
	}
	
	public ClientUIController(String username, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		mainView = new ClientInput(username, optionsList, formatsList,  width, height);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
	}
	
	public ClientUIController(ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		mainView = new ClientInput(databases, optionsList, formatsList, width, height);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
	}
	
	public ClientUIController(List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		mainView = new ClientInput(optionsList, formatsList, width, height);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
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
	
	public void hide() {
		mainView.hide();
	}
	
	public void showResults(List<QueryResult> results) {
		double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
		double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
		ClientOutput out = new ClientOutput(results, exportingOptions, exportingFormats, width, height);
		eventHandlers.forEach( handler -> {
			out.addEventHandler( handler.getValue(), handler.getKey() );
		});
		out.show();
	}
	
	/* METODI PER GESTIRE GLI EVENTI DELLA UI */
	
	public void addAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type, EventHandler<AddRemoveDatabaseEvent> handler) {
		mainView.addAddRemoveDatabaseEventHandler(type, handler);
	}
	public void addConnectionEventHandler(EventType<ConnectionEvent> type, EventHandler<ConnectionEvent> handler) {
		mainView.addConnectionEventHandler(type, handler);
	}
	public void addSendQueryEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		mainView.addSendQueryEventHandler(type, handler);
	}
	public void addHistoryFileEventHandler(EventType<HistoryFileEvent> type, EventHandler<HistoryFileEvent> handler) {
		mainView.addHistoryFileEventHandler(type, handler);
	}
	public void addExportQueryResultEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		this.eventHandlers.add(new Pair<>(handler, type));
	}
	
	public void removeAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type, EventHandler<AddRemoveDatabaseEvent> handler) {
		mainView.removeAddRemoveDatabaseEventHandler(type, handler);
	}
	public void removeConnectionEventHandler(EventType<ConnectionEvent> type, EventHandler<ConnectionEvent> handler) {
		mainView.removeConnectionEventHandler(type, handler);
	}
	public void removeSendQueryEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		mainView.removeSendQueryEventHandler(type, handler);
	}
	public void removeHistoryFileEventHandler(EventType<HistoryFileEvent> type, EventHandler<HistoryFileEvent> handler) {
		mainView.removeHistoryFileEventHandler(type, handler);
	}
	public void removeExportQueryResultEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		this.eventHandlers.remove(new Pair<>(handler, type));
	}

}
