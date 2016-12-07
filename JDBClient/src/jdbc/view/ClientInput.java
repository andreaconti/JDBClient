package jdbc.view;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.css.CSSStyleable;
import jdbc.view.events.AddRemoveDatabaseEvent;
import jdbc.view.events.ConnectionEvent;
import jdbc.view.events.HistoryFileEvent;
import jdbc.view.events.SendQueryEvent;
import jdbc.view.events.SystemEvent;

class ClientInput extends ResizableStage implements CSSStyleable {
	
	// root node
	private VBox rootNode;
	
	// toolbar nella parte alta
	private ObservableList<String> databases;
	private ComboBox<String> databasePath;
	private TextField username;
	private PasswordField password;
	private Button connection;
	
	// corpo centrale view
	private TextArea query;
	private ObservableList<QueryError> errors;
	private TableView<QueryError> errorsTable;
	
	// toolbar in basso
	private Button sendQuery;
	
	// elementi per informazioni in seguito ad eventi e settabili..
	private Optional<String> databaseToAddOrDelete = Optional.empty();
	private Optional<Path> exportingPath = Optional.empty();
	private Optional<ExportingOptions> exportingOptions = Optional.empty();
	private Optional<ExportingFormat> exportingFormat = Optional.empty();
	private StringProperty obsQueryText = new SimpleStringProperty();
	private boolean isConnected = false;
	private List<ExportingOptions> optionsList;
	private List<ExportingFormat> formatsList;
	
	// dialogs
	private UserDialog dialog;
	
	// styling
	private String cssPath;
	
	// estensibile
	private MenuBar menuBar;

	/* COSTRUTTORI */
	
	public ClientInput(String username, ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList, UserDialog dialog, double width, double height) {
		super();
		validate("username or databases list is null", username, databases, dialog);
		if ( width < 0 || height < 0 )
			throw new IllegalArgumentException("width < 0  || height < 0");
		
		this.dialog = dialog;
		
		// imposto root..
		rootNode = new VBox(10);
		rootNode.setPadding(new Insets(10,10,10,10));
		Scene scene = new Scene(rootNode, width, height);
		this.setScene(scene);
		
		// creo toolbar alta
		HBox HighToolbar = new HBox(10);
		{
			initDatabasePaths(databases);
			this.initPasswordAndUsername(username);
			initConnectionButton();
			HighToolbar.getChildren().addAll(databasePath, this.username, password, connection);
		}
		
		initQueryArea();
		initErrorsTable();
		initSendQuery();
		initCloseButton();
		
		this.optionsList = optionsList;
		this.formatsList = formatsList;
		
		menuBar = setMainMenu();
		rootNode.getChildren().addAll(menuBar,HighToolbar, query, errorsTable, sendQuery);
	}
	
	public ClientInput(ObservableList<String> databases, List<ExportingOptions> optionsList, List<ExportingFormat> formatsList, UserDialog dialog, double width, double height) {
		this("", databases, optionsList, formatsList, dialog, width, height);
	}
	
	public ClientInput(String username,List<ExportingOptions> optionsList, List<ExportingFormat> formatsList, UserDialog dialog, double width, double height) {
		this(username, FXCollections.observableArrayList(), optionsList, formatsList, dialog, width, height);
	}
	
	public ClientInput(List<ExportingOptions> optionsList, List<ExportingFormat> formatsList, UserDialog dialog, double width, double height) {
		this("", FXCollections.observableArrayList(), optionsList, formatsList, dialog, width, height);
	}
	
	// listeners
	
	public void addAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type , EventHandler<AddRemoveDatabaseEvent> handler) { this.addEventHandler(type, handler); }
	public void addConnectionEventHandler(EventType<ConnectionEvent> type , EventHandler<ConnectionEvent> handler) { this.addEventHandler(type, handler); }
	public void addHistoryFileEventHandler(EventType<HistoryFileEvent> type , EventHandler<HistoryFileEvent> handler) { this.addEventHandler(type, handler); }
	public void addSendQueryEventHandler(EventType<SendQueryEvent> type , EventHandler<SendQueryEvent> handler) { this.addEventHandler(type, handler); }
	
	public void removeAddRemoveDatabaseEventHandler(EventType<AddRemoveDatabaseEvent> type , EventHandler<AddRemoveDatabaseEvent> handler) { this.removeEventHandler(type, handler); }
	public void removeConnectionEventHandler(EventType<ConnectionEvent> type , EventHandler<ConnectionEvent> handler) { this.removeEventHandler(type, handler); }
	public void removeHistoryFileEventHandler(EventType<HistoryFileEvent> type , EventHandler<HistoryFileEvent> handler) { this.removeEventHandler(type, handler); }
	public void removeSendQueryEventHandler(EventType<SendQueryEvent> type , EventHandler<SendQueryEvent> handler) { this.removeEventHandler(type, handler); }
	
	public void addSystemEventHandler(EventType<SystemEvent> type, EventHandler<SystemEvent> handler) { this.addEventHandler(type, handler); }
	
	// set mainMenu
	
	private MenuBar setMainMenu() {
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.setUseSystemMenuBar(true);
		
		{
			Menu databases = new Menu("Database Management");
			MenuItem removeAdd = new MenuItem("Remove / Add Databases");
			databases.getItems().add(removeAdd);
			
			removeAdd.setOnAction(ev -> {
				
				DatabasesListView list = new DatabasesListView(this.databasePath.getItems());
				list.setOnActionAddButton( ev1 -> {
					Optional<String> string = dialog.askForString("Archive Database", "Write here the complete path of the database..");
					if (string.isPresent()) {
						this.databaseToAddOrDelete = string;
						AddRemoveDatabaseEvent toFire = new AddRemoveDatabaseEvent(AddRemoveDatabaseEvent.ADD_DATABASE_REQUEST, this.getDatabaseToAddOrDelete().get());
						this.fireEvent(toFire);
					}
				});
				
				list.setOnActionDeleteButton( ev2 -> {
					String toDelete = list.getListView().getSelectionModel().getSelectedItem();
					if ( toDelete != null ) {
						System.out.println(toDelete);
						this.databaseToAddOrDelete = Optional.of(toDelete);
						AddRemoveDatabaseEvent toFire = new AddRemoveDatabaseEvent(AddRemoveDatabaseEvent.REMOVE_DATABASE_REQUEST, this.getDatabaseToAddOrDelete().get());
						this.fireEvent(toFire);
					}
				});
				
				list.show();
			});
			
			/* TODO */
			
			Menu exporting = new Menu("Exporting Options");
			{
				MenuItem exportOnFile = new MenuItem("Save Query on a file");
				exporting.getItems().add(exportOnFile);
				
				exportOnFile.setOnAction( ev -> {
					ClientDirectoryChooser chooser;
					if ( exportingPath.isPresent() )
						chooser = dialog.chooseExportingDirectoryWithOptionsAndFormat(exportingPath.get(), optionsList, formatsList);
					else
						chooser = dialog.chooseExportingDirectoryWithOptionsAndFormat(optionsList, formatsList);
					
					Optional<Path> path = chooser.getDirectorySelected();
					if ( path.isPresent() ) {
						this.exportingPath = Optional.of(path.get());
						this.exportingOptions = Optional.ofNullable(chooser.getExportingOptionSelected());
						this.exportingFormat = Optional.ofNullable(chooser.getExportingFormatSelected());
						HistoryFileEvent toFire = new HistoryFileEvent(HistoryFileEvent.ANY, 
																		exportingOptions.get(), 
																		exportingFormat.get(), 
																		exportingPath.get(),
																		chooser.getFileName());
						this.fireEvent(toFire);
					}
				});
			}
			
			mainMenu.getMenus().addAll(databases, exporting);
		}
		
		return mainMenu;
		
	}
	
	/* GETTERS && SETTERS */

	public void setOptionsList(List<ExportingOptions> optionsList) {
		this.optionsList = optionsList;
	}

	public void setFormatsList(List<ExportingFormat> formatsList) {
		this.formatsList = formatsList;
	}

	// username and password
	public String getUsername() {
		return username.getText();
	}
	
	public void setUsername(String username) {
		validate("username is null", username);
		this.username.setText(username);
	}
	
	public void resetUsername() {
		this.username.setText("");
	}

	public String getPassword() {
		return password.getText();
	}
	
	public void resetPassword() {
		this.password.setText("");
	}
	
	// databases paths
	public String getdatabaseURLSelected() {
		return databasePath.getSelectionModel().getSelectedItem();
	}
	
	public ObservableList<String> getDatabasesShowed() {
		return this.databases;
	}

	// queryArea && errors
	public String getQuery() {
		return query.getText();
	}
	
	public void resetQuery() {
		query.setText("");
	}
	
	public StringProperty getObservableQueryText() {
		return obsQueryText;
	}
	
	public void addError(QueryError toAdd) {
		errors.add(toAdd);
	}
	
	public void resetErrors() {
		errors.removeAll(errors);
	}
	
	// isConnected
	public boolean isConnected() {
		return isConnected;
	}
	
	// getMenuBar
	public MenuBar getMenuBar() {
		return this.menuBar;
	}
	
	/* ALTRE COSE */
	
	// styling css
	public void addCssStyle(String style) {
		validate("style is null", style);
		rootNode.getStylesheets().add(cssPath);
	}
	
	// per settare il salvataggio della cronologia delle query..
	public Optional<Path> getExportingPath() {
		
		if (this.exportingPath.isPresent()) {
			Path result = exportingPath.get();
			exportingPath = Optional.empty();
			return Optional.of(result);
		}
		else
			return Optional.empty();
	}
	
	public Optional<ExportingFormat> getExportingFormatSelected() {
		
		if (this.exportingFormat.isPresent()) {
			ExportingFormat result = exportingFormat.get();
			exportingFormat = Optional.empty();
			return Optional.of(result);
		}
		else
			return Optional.empty();
	}
	
	public Optional<ExportingOptions> getExportingOptionsSelected() {
		
		if (this.exportingOptions.isPresent()) {
			ExportingOptions result = exportingOptions.get();
			exportingOptions = Optional.empty();
			return Optional.of(result);
		}
		else
			return Optional.empty();
	}
	
	// per dire alla view se siamo connessi o no..
	public void setConnection(boolean conn) {
		isConnected = conn;
		
		if (isConnected == false) {
			this.connection.setText("connect");
			this.resetPassword();
			this.password.setEditable(true);
			this.username.setEditable(true);
		}
		else {
			this.connection.setText("disconnect");
			this.resetPassword();
			this.password.setEditable(false);
			this.username.setEditable(false);
		}
	}
	
	// restituisce il path del database da aggiungere o eliminare..
	public Optional<String> getDatabaseToAddOrDelete() {
		if ( databaseToAddOrDelete.isPresent() ) {
			String value = databaseToAddOrDelete.get();
			databaseToAddOrDelete = Optional.empty();
			return Optional.of(value);
		}
		else
			return databaseToAddOrDelete;
	}
	
	/* METODI PRIVATI PER LEGGIBILITA' E UTILITA' */
	
	private void initQueryArea() {
		query = new TextArea();
		query.setWrapText(true);
		VBox.setVgrow(query, Priority.ALWAYS);
		obsQueryText = query.textProperty();
	}
	
	private void initErrorsTable() {
		errors = FXCollections.observableArrayList();
		errorsTable = new TableView<QueryError>(errors);
		errorsTable.setMaxHeight(150);
		errorsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		TableColumn<QueryError, LocalDateTime> dataCol = new TableColumn<>("Date");
		dataCol.setCellValueFactory(new PropertyValueFactory<>("timeCreation"));
		TableColumn<QueryError, String> errorCode = new TableColumn<>("Error Code");
		errorCode.setCellValueFactory(new PropertyValueFactory<>("errorCode"));
		TableColumn<QueryError, String> description = new TableColumn<>("Description");
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		dataCol.setPrefWidth(110);
		dataCol.setResizable(false);
		errorCode.setPrefWidth(110);
		dataCol.setResizable(false);
		
		description.prefWidthProperty().bind(errorsTable.widthProperty().subtract(dataCol.getWidth() + errorCode.getWidth() + 2));
		
		errorsTable.getColumns().add(dataCol);
		errorsTable.getColumns().add(errorCode);
		errorsTable.getColumns().add(description);
		
	}
	
	private void initSendQuery() {
		sendQuery = new Button("Send Query");
		sendQuery.setMaxWidth(Double.MAX_VALUE);
		sendQuery.setOnAction( ev -> this.fireEvent(new SendQueryEvent(SendQueryEvent.ANY, this.getQuery())) );
	}
	
	private void initConnectionButton() {
		connection = new Button("connect");
		isConnected = false;
		connection.setMinWidth(100);
		
		connection.setOnAction( ev -> {
			if (isConnected == false)
				this.fireEvent(new ConnectionEvent(ConnectionEvent.CONNECTION_REQUEST, username.getText(), password.getText(), getdatabaseURLSelected()) );
			else
				this.fireEvent(new ConnectionEvent(ConnectionEvent.DISCONNECTION_REQUEST, username.getText(), password.getText(), getdatabaseURLSelected()) );
		});
	}
	
	private void initDatabasePaths(ObservableList<String> databases) {
		this.databases = databases;
		this.databasePath = new ComboBox<>(this.databases);
		if (!databases.isEmpty()) { databasePath.getSelectionModel().select(0); }
		databasePath.setMinWidth(100);
		HBox.setHgrow(databasePath, Priority.ALWAYS);
	}
	
	private void initPasswordAndUsername(String username) {
		this.username = new TextField(username);
		this.username.setPromptText("username");
		HBox.setHgrow(this.username, Priority.ALWAYS);
		password = new PasswordField();
		password.setPromptText("password");
		HBox.setHgrow(this.password, Priority.ALWAYS);
	}
	
	private void initCloseButton() {
		super.close.setOnAction( ev -> this.fireEvent(new SystemEvent(SystemEvent.CLOSE)) );
	}
	
	private void validate(String descError, Object...objects) {
		
		for ( Object ob : objects )
			if ( ob == null )
				throw new NullPointerException(descError);	
	}
	
}
