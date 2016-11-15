package jdbc.view.clientInput;

import java.nio.file.Path;
import java.time.LocalDateTime;
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
import javafx.stage.Stage;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.QueryError;
import jdbc.view.UserDialog;
import jdbc.view.events.AddRemoveDatabaseEvent;
import jdbc.view.events.ConnectionEvent;
import jdbc.view.events.HistoryFileEvent;

public class ClientInput extends Stage {
	
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
	
	// styling
	private String cssPath;

	/* COSTRUTTORI */
	public ClientInput(String username, ObservableList<String> databases, double width, double height) {
		
		validate("username or databases list is null", username, databases );
		if ( width < 0 || height < 0 )
			throw new IllegalArgumentException("width < 0  || height < 0");
		
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
		
		rootNode.getChildren().addAll(setMainMenu(),HighToolbar, query, errorsTable, sendQuery);
	}
	
	public ClientInput(ObservableList<String> databases, double width, double height) {
		this("", databases, width, height);
	}
	
	public ClientInput(String username, double width, double height) {
		this(username, FXCollections.observableArrayList(), width, height);
	}
	
	public ClientInput(double width, double height) {
		this("", FXCollections.observableArrayList(), width, height);
	}
	
	// listeners
	
	public void addListenerOnConnectionRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.CONNECTION_REQUEST, handler); }
	public void addListenerOnDisconnectionRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.DISCONNECTION_REQUEST, handler); }
	public void addListenerOnQueryRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.SUBMIT_QUERY_REQUEST, handler); }
	public void addListenerOnExportOnFileRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.EXPORT_ON_FILE, handler); }
	public void addListenerOnExportOnTxtFileOnlyResultsRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.EXPORT_ON_TXT_FILE_ONLY_RESULTS, handler); }
	public void addListenerOnExportOnTxtFileAllRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.EXPORT_ON_TXT_FILE_ALL, handler); }
	public void addListenerOnDeleteDatabaseRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.REMOVE_DATABASE_REQUEST, handler); }
	public void addListenerOnAddDatabaseRequest(ClientInputEventHandler handler) { this.addEventHandler(ClientInputEvent.ADD_DATABASE_REQUEST, handler); }
	
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
					UserDialog dialog;
					if (cssPath != null) dialog = new UserDialog(cssPath); else dialog = new UserDialog();
					Optional<String> string = dialog.askForString("Archive Database", "Write here the complete path of the database..");
					if (string.isPresent()) {
						this.databaseToAddOrDelete = string;
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.ADD_DATABASE_REQUEST));
					}
				});
				
				list.setOnActionDeleteButton( ev2 -> {
					String toDelete = list.getListView().getSelectionModel().getSelectedItem();
					if ( toDelete != null ) {
						System.out.println(toDelete);
						this.databaseToAddOrDelete = Optional.of(toDelete);
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.REMOVE_DATABASE_REQUEST));
					}
				});
				
				list.show();
			});
			
			Menu exporting = new Menu("Exporting Options");
			{
				MenuItem exportOnFile = new MenuItem("Save Query on a file");
				exporting.getItems().add(exportOnFile);
				
				exportOnFile.setOnAction( ev -> {
					ExportingFileForm chooser = new ExportingFileForm( exportingPath );
					Optional<Path> path = chooser.showAndWait();
					if ( path.isPresent() ) {
						this.exportingPath = Optional.of(path.get());
						this.exportingOptions = Optional.ofNullable(chooser.getExportingOptionsSelected());
						this.exportingFormat = Optional.ofNullable(chooser.getExportingFormatSelected());
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.EXPORT_ON_FILE));
					}
				});
			}
			
			Menu info = new Menu("Info");
			
			mainMenu.getMenus().addAll(databases, exporting, info);
		}
		
		return mainMenu;
		
	}
	
	/* GETTERS && SETTERS */

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
		sendQuery.setOnAction( ev -> this.fireEvent(new ClientInputEvent(this, ClientInputEvent.SUBMIT_QUERY_REQUEST)));
	}
	
	private void initConnectionButton() {
		connection = new Button("connect");
		isConnected = false;
		connection.setMinWidth(100);
		
		connection.setOnAction( ev -> {
			if (isConnected == false)
				this.fireEvent(new ClientInputEvent(this, ClientInputEvent.CONNECTION_REQUEST));
			else
				this.fireEvent(new ClientInputEvent(this, ClientInputEvent.DISCONNECTION_REQUEST)); });
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
	
	private void validate(String descError, Object...objects) {
		
		for ( Object ob : objects )
			if ( ob == null )
				throw new NullPointerException(descError);
		
	}
	
	
}
