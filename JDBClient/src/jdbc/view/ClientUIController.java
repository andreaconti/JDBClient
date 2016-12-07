package jdbc.view;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Screen;
import javafx.util.Pair;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.css.StyleProducer;
import jdbc.view.events.*;
import jdbc.view.events.ConnectionEvent;
import jdbc.view.events.HistoryFileEvent;

/**
 * User Interface Controller used in order to show to the user many informations as a main view where he can enter username, password, request a connection
 * to a database, add database Paths send query and request to save on a file the history of the queries done. Results are showed on a different view
 * where the user can request to save a query on a file in many formats and with different options. Formats and Options are encapsulated in 
 * <code>ExportingFormat</code> and <code>ExportingOptions</code> enums, the requests sended by the user are notified through many events to which you can
 * register throught the proper methods. 
 * 
 * @author Andrea Conti
 *
 */

public class ClientUIController {
	
	private ClientInput mainView;
	private OperationsDescriptorView operationsView;
	private final double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
	private final double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
	private List<ExportingOptions> exportingOptions;
	private List<ExportingFormat> exportingFormats;
	
	private StyleProducer styleProducer;
	private UserDialogImpl dialog;
	
	private List<Pair<EventHandler<? super SendQueryEvent>, EventType<? extends SendQueryEvent>>> sendQueryEventHandlers = new ArrayList<>();
	private List<Pair<EventHandler<DriversEvent>, EventType<DriversEvent>>> driversEventHandlers = new ArrayList<>();
	
	/**
	 * 
	 * Creates a instance of the ClientUIController
	 * 
	 * @param username a username that is showed to the user as default choice
	 * @param databases list of databases paths from witch the user can choose, it is observable so if the is added a database it will be showed to the user
	 * @param optionsList options from witch the user can choose when he wants export or save a history of his queries
	 * @param formatsList formats file from witch the user can choose when he wants export or save a history of his queries
	 */
	public ClientUIController(String username, ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		dialog = new UserDialogImpl();
		mainView = new ClientInput(username, databases, optionsList, formatsList, dialog, width, height);
		mainView.setOptionsList(Arrays.asList(ExportingOptions.values()));
		this.operationsView = new OperationsDescriptorView(height, width);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
		setMainMenuBar();
	}
	
	/**
	 * 
	 * Creates a instance of the ClientUIController, the main view created from this instance will set an empty default list of databases paths
	 * 
	 * @param username a username that is showed to the user as default choice
	 * @param optionsList options from witch the user can choose when he wants export or save a history of his queries
	 * @param formatsList formats file from witch the user can choose when he wants export or save a history of his queries
	 */
	public ClientUIController(String username, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		dialog = new UserDialogImpl();
		mainView = new ClientInput(username, optionsList, formatsList, dialog, width, height);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
		this.operationsView = new OperationsDescriptorView(height, width);
		dialog = new UserDialogImpl();
		setMainMenuBar();
	}
	
	/**
	 * Creates a instance of the ClientUIController
	 * the main view created from this instance will set an empty default list of databases paths
	 * 
	 * @param databases list of databases paths from witch the user can choose, it is observable so if the is added a database it will be showed to the user
	 * @param optionsList options from witch the user can choose when he wants export or save a history of his queries
	 * @param formatsList formats file from witch the user can choose when he wants export or save a history of his queries
	 */
	public ClientUIController(ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		dialog = new UserDialogImpl();
		mainView = new ClientInput(databases, optionsList, formatsList, dialog, width, height);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
		this.operationsView = new OperationsDescriptorView(height, width);
		dialog = new UserDialogImpl();
		setMainMenuBar();
	}
	
	/**
	 * Creates a instance of the ClientUIController
	 * 
	 * @param optionsList options from witch the user can choose when he wants export or save a history of his queries
	 * @param formatsList formats file from witch the user can choose when he wants export or save a history of his queries
	 */
	public ClientUIController(List<ExportingOptions> optionsList, List<ExportingFormat> formatsList) {
		dialog = new UserDialogImpl();
		mainView = new ClientInput(optionsList, formatsList, dialog, width, height);
		this.operationsView = new OperationsDescriptorView(height, width);
		this.exportingFormats = formatsList;
		this.exportingOptions = optionsList;
		dialog = new UserDialogImpl();
		setMainMenuBar();
	}
	
	/* TUTTI I GETTERS E SETTER */
	
	/**
	 * @return the string representing the guery requested by the user
	 */
	public String getQuery() {
		return mainView.getQuery();
	}
	
	/**
	 * reset the query showed to the user to an empty string
	 */
	public void resetQuery() {
		mainView.resetQuery();
	}
	
	/**
	 * adds an error incapsulated in a QueryError instance to the list showed to the user
	 * 
	 * @param toAdd the QueryError that will be added to the list showed to the user
	 */
	public void addError(QueryError toAdd) {
		mainView.addError(toAdd);
	}
	
	/**
	 * sets the list of errors showed to the user to an empty list
	 */
	public void resetErrors() {
		mainView.resetErrors();
	}
	
	/**
	 * @return the username showed / entered to/by the user
	 */
	public String getUsername() {
		return mainView.getUsername();
	}
	
	/**
	 * sets the username showed to the user
	 * 
	 * @param username the username showed to the user
	 */
	public void setUsername(String username) {
		mainView.setUsername(username);
	}
	
	/**
	 * sets the username showed to the user to an empty string
	 */
	public void resetUsername() {
		mainView.resetUsername();
	}

	/**
	 * returns the password entered by the user
	 * 
	 * @return the password entered by the user
	 */
	public String getPassword() {
		return mainView.getPassword();
	}
	
	/**
	 * resets the password entered by the user
	 */
	public void resetPassword() {
		mainView.resetPassword();
	}
	
	/**
	 * @return a UserDialog instance truly setted 
	 */
	public UserDialog getUserDialog() {
		return this.dialog;
	}
	
	public ObservableList<String> getDatabasesPaths() {
		return mainView.getDatabasesShowed();
	}
	
	/**
	 * Is used in order to inform the view of the succesfull connection to the database, this method is very important to due 
	 * it allows the view to sets itself in order to allow the user to send a disconnection request.
	 * It must be used every time there is a new connection or disconnection
	 * 
	 * @param connection informs the view of the succesfull connection to the database
	 */
	public void setConnection(boolean connection) {
		mainView.setConnection(connection);
	}
	
	/**
	 * 
	 * @return if the view is setted as connected or not
	 */
	public boolean isConnected() {
		return mainView.isConnected();
	}
	
	// cssStyle
	/**
	 * Sets the StyleProducer used in order to style the view
	 * @param producer the StyleProducer used in order to set the style of the view
	 */
	public void setStyleProducer(StyleProducer producer) {
		this.styleProducer = producer;
	}
	
	/**
	 * @return the StyleProducer used by the view for styling
	 */
	public StyleProducer getStyleProducer() {
		return this.styleProducer;
	}
	
	/* METODI PER MOSTRARE I RISULTATI E MAIN VIEW */
	
	/**
	 * shows the main view of the ClientUIController 
	 */
	public void show() {
		mainView.show();
	}
	
	/**
	 * Hide the main view of the ClientUIController
	 */
	public void hide() {
		mainView.hide();
	}
	
	/**
	 * This method is used in order to show to the user the results of a query
	 * 
	 * @param results a list of QueryResult that incapsulates the tables to show to the user
	 */
	public void showResults(List<QueryResult> results) {
		double width = Screen.getPrimary().getVisualBounds().getWidth() / 3;
		double height = Screen.getPrimary().getVisualBounds().getWidth() / 4;
		ClientOutput out = new ClientOutput(results, exportingOptions, exportingFormats, dialog, width, height);
		sendQueryEventHandlers.forEach( handler -> {
			out.addEventHandler( handler.getValue(), handler.getKey() );
		});
		
		if (styleProducer != null) styleProducer.setStyleOf(out);
		
		out.show();
	}
	
	/**
	 * this method shows to the user a window where he can see all drivers and manage them
	 * @param drivers list of all drivers to show to the user
	 */
	public void showDrivers(ObservableList<Path> drivers) {
		ValuesListView list = new ValuesListView(drivers);
		
		this.driversEventHandlers.forEach( handler  -> list.addEventHandler(handler.getValue(), handler.getKey()) );
		
		list.setOnActionAddButton( ev -> {
			Optional<File> result = dialog.showOpenFile("Chooser the driver");
			if ( result.isPresent() )
				list.fireEvent(new DriversEvent(DriversEvent.ADD_DRIVER_REQUEST, result.get().toPath()));
		});
		list.setOnActionDeleteButton( ev ->
			list.fireEvent( new DriversEvent(DriversEvent.REMOVE_DRIVER_REQUEST, (Path) list.getListView().getSelectionModel().getSelectedItem()) ) );
		
		list.show();
	}
	
	/**
	 * allows to show to the user a view containing a full upgradable ( with <code>appendOperationsDescription</code>) description 
	 */
	public void showOperationsDescription() {
		this.operationsView.show();
	}
	
	/**
	 * hides to the user a view containing a full upgradable ( with <code>appendOperationsDescription</code> ) description
	 */
	public void hideOperationsDescription() {
		this.operationsView.hide();
	}
	
	/**
	 * appends description informations to a view that the user can see in order to know what the system did
	 * @param strings
	 */
	public void appendOperationsDescription(String...strings) {
		this.operationsView.appendText(strings);
	}
	
	/**
	 * resets the description informations of a view that the user can see used in order to know what the system did
	 */
	public void resetOperationsDescription() {
		this.operationsView.resetText();
	}
	
	/* STYLE */
	/**
	 * sets the StyleProducer used by the ClientUIController in order to set the style off all views that it can shows to the user
	 * @param producer the StyleProducer used by the ClientUIController
	 */
	public void setStyle(StyleProducer producer) {
		this.styleProducer = producer;
		producer.setStyleOf(dialog);
		producer.setStyleOf(mainView);
		producer.setStyleOf(operationsView);
	}
	
	/* METODI PER GESTIRE GLI EVENTI DELLA UI */
	
	/**
	 * add an handler called when is fired a request by the user to add or remove a database path
	 * @param type the type of the Event : <code>ADD_DATABASE_REQUEST</code> or <code>REMOVE_DATABASE_REQUEST</code>
	 * @param handler the EventHandler that is called
	 */
	public void addAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type, EventHandler<AddRemoveDatabaseEvent> handler) {
		mainView.addAddRemoveDatabaseEventHandler(type, handler);
	}
	
	/**
	 * add an handler called when is fired a request by the user in order to connect to a database
	 * the event fired class is <code>ConnectionEvent</code> and it contains informations about the connection requested
	 * @param type the type of the Event : <code>CONNECTION_REQUEST</code> or <code>DISCONNECTION_REQUEST</code>
	 * @param handler the EventHandler that is called
	 */
	public void addConnectionEventHandler(EventType<ConnectionEvent> type, EventHandler<ConnectionEvent> handler) {
		mainView.addConnectionEventHandler(type, handler);
	}
	
	/**
	 * add an handler called when is fired when the user tries to send a query
	 * @param type the type of the SendQuery on witch to apply the EventHandler
	 * @param handler the EventHandler that is called
	 */
	public void addSendQueryEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		mainView.addSendQueryEventHandler(type, handler);
	}
	
	/**
	 * add an handler called when is fired the request to save the queries history to a file
	 * @param type the type of the HistoryFileEvent on witch to apply the EventHandler
	 * @param handler the EventHandler that is called
	 */
	public void addHistoryFileEventHandler(EventType<HistoryFileEvent> type, EventHandler<HistoryFileEvent> handler) {
		mainView.addHistoryFileEventHandler(type, handler);
	}
	
	/**
	 * add an handler called when is fired a request to export a table incapsulated in a QueryResult contained in the ExportQueryResult fired
	 * @param type the type of the ExportQueryResult on witch to apply the EventHandler
	 * @param handler the EventHandler that is called
	 */
	public void addExportQueryResultEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		this.sendQueryEventHandlers.add(new Pair<>(handler, type));
	}
	
	/**
	 * add an handler called when is fired an event about the application itself as a close request
	 * @param type
	 * @param handler
	 */
	public void addSystemEventHandler(EventType<SystemEvent> type, EventHandler<SystemEvent> handler) {
		this.mainView.addSystemEventHandler(type, handler);
	}
	
	public void addDriversEventHandler(EventType<DriversEvent> type, EventHandler<DriversEvent> handler) {
		if (type.equals(DriversEvent.ALL_DRIVER_EVENTS)) {
			this.mainView.addEventHandler(type, handler);
			this.driversEventHandlers.add(new Pair<>(handler, type));
		}
		else if ( type.equals(DriversEvent.SHOW_DRIVER_REQUEST) ) 
			this.mainView.addEventHandler(type, handler);
		else
			this.driversEventHandlers.add(new Pair<>(handler, type));
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type, EventHandler<AddRemoveDatabaseEvent> handler) {
		mainView.removeAddRemoveDatabaseEventHandler(type, handler);
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeConnectionEventHandler(EventType<ConnectionEvent> type, EventHandler<ConnectionEvent> handler) {
		mainView.removeConnectionEventHandler(type, handler);
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeSendQueryEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		mainView.removeSendQueryEventHandler(type, handler);
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeHistoryFileEventHandler(EventType<HistoryFileEvent> type, EventHandler<HistoryFileEvent> handler) {
		mainView.removeHistoryFileEventHandler(type, handler);
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeExportQueryResultEventHandler(EventType<SendQueryEvent> type, EventHandler<SendQueryEvent> handler) {
		this.sendQueryEventHandlers.remove(new Pair<>(handler, type));
	}
	
	/**
	 * remove the EventHandler for the EventType specified
	 * @param type the type of the Event
	 * @param handler the EventHandler that is called
	 */
	public void removeSystemEventHandler(EventType<SystemEvent> type, EventHandler<SystemEvent> handler) {
		this.mainView.removeEventHandler(type, handler);
	}
	
	/* METODI PRIVATI */
	
	/*
	 * adds menus to the menubar of the mainView
	 */
	private void setMainMenuBar() {
		Menu info = new Menu("Info");
		{
			MenuItem operationsDescription = new MenuItem("Show Operations Description");
			operationsDescription.setOnAction( ev -> this.operationsView.show());
			info.getItems().add(operationsDescription);
		}
		this.mainView.getMenuBar().getMenus().add(info);	
	}

}
