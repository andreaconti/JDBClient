package jdbc.view;

import java.nio.file.Path;
import java.util.Optional;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ClientInput extends Stage {
	
	private VBox rootNode;
	private ComboBox<String> databasePath;
	private TextField username;
	private PasswordField password;
	private Button connection;
	private boolean isConnected = false;
	
	private TextArea query;
	private Button sendQuery;
	private TextArea sqlError;
	
	private Optional<String> databaseRequested = Optional.empty();
	private Optional<Path> exportingPath = Optional.empty();
	private StringProperty obsQueryText = new SimpleStringProperty();
	
	private String cssPath;

	public ClientInput(String username, ObservableList<String> databases, double width, double height) {
		
		rootNode = new VBox(10);
		rootNode.setPadding(new Insets(10,10,10,10));
		Scene scene = new Scene(rootNode, width, height);
		this.setScene(scene);
		
		// create controlBar
		HBox bar = new HBox(10);
		{
			databasePath = new ComboBox<String>(databases);
			if (!databases.isEmpty()) { databasePath.getSelectionModel().select(0); }
			databasePath.setMinWidth(100);
			HBox.setHgrow(databasePath, Priority.ALWAYS);
			this.username = new TextField(username);
			this.username.setPromptText("username");
			HBox.setHgrow(this.username, Priority.ALWAYS);
			password = new PasswordField();
			password.setPromptText("password");
			HBox.setHgrow(this.password, Priority.ALWAYS);
			connection = new Button("connect");
			connection.setMinWidth(100);
			bar.getChildren().addAll(databasePath, this.username, password, connection);
		}
		
		query = new TextArea();
		query.setWrapText(true);
		obsQueryText = query.textProperty();
		VBox.setVgrow(query, Priority.ALWAYS);
		sqlError = new TextArea();
		sqlError.setWrapText(true);
		
		sendQuery = new Button("Send Query");
		sendQuery.setMaxWidth(Double.MAX_VALUE);
		
		rootNode.getChildren().addAll(setMainMenu(),bar, query, sqlError, sendQuery);
		
		this.setFiringEvents();
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
						this.databaseRequested = string;
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.ADD_DATABASE_REQUEST));
					}
				});
				
				list.setOnActionDeleteButton( ev2 -> {
					String toDelete = list.getListView().getSelectionModel().getSelectedItem();
					if ( toDelete != null ) {
						System.out.println(toDelete);
						this.databaseRequested = Optional.of(toDelete);
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.REMOVE_DATABASE_REQUEST));
					}
				});
				
				list.show();
			});
			
			Menu exporting = new Menu("Exporting Options");
			{
				MenuItem exportOnFile = new MenuItem("Export on a .txt file");
				exporting.getItems().add(exportOnFile);
				
				exportOnFile.setOnAction( ev -> {
					ExportingFileForm chooser = new ExportingFileForm( exportingPath );
					Optional<Pair<Path, ExportingFileOption>> pathWithOptions = chooser.showAndWait();
					if ( pathWithOptions.isPresent() ) {
						this.exportingPath = Optional.of(pathWithOptions.get().getKey());
						this.fireEvent(new ClientInputEvent(this, ClientInputEvent.EXPORT_ON_TXT_FILE_ALL));
					}
				});
			}
			
			Menu info = new Menu("Info");
			
			mainMenu.getMenus().addAll(databases, exporting, info);
		}
		
		return mainMenu;
		
	}
	
	
	// firing events
	
	private void setFiringEvents() {
		
		sendQuery.setOnAction( ev -> this.fireEvent(new ClientInputEvent(this, ClientInputEvent.SUBMIT_QUERY_REQUEST)));
		
		connection.setOnAction( ev -> {
			if (isConnected == false) {
				this.fireEvent(new ClientInputEvent(this, ClientInputEvent.CONNECTION_REQUEST));
			}
			else
				this.fireEvent(new ClientInputEvent(this, ClientInputEvent.DISCONNECTION_REQUEST));
		});
		
	}
	
	// getters && setters

	public String getUsername() {
		return username.getText();
	}
	
	public void setUsername(String username) {
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
	
	public String getdatabaseURL() {
		return databasePath.getSelectionModel().getSelectedItem();
	}

	public String getQuery() {
		return query.getText();
	}
	
	public void resetQuery() {
		this.query.setText("");
	}
	
	public void setSqlError(String text) {
		this.sqlError.setText(text);
	}
	
	public void addCssStyle(String style) {
		if (style == null)
			throw new IllegalArgumentException("style == null");
		rootNode.getStylesheets().add(cssPath);
	}
	
	public Optional<String> getDatabaseRequested() {
		if (this.databaseRequested.isPresent()) {
			String result = databaseRequested.get();
			databaseRequested = Optional.empty();
			return Optional.of(result);
		}
		else
			return Optional.empty();
	}
	
	public Optional<Path> getExportingPath() {
		
		if (this.exportingPath.isPresent()) {
			Path result = exportingPath.get();
			exportingPath = Optional.empty();
			return Optional.of(result);
		}
		else
			return Optional.empty();
		
	}
	
	public StringProperty getObservableQueryText() {
		return this.obsQueryText;
	}
	
	// altre cose
	
	public void setConnection(boolean conn) {
		this.isConnected = conn;
		
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
	
	public boolean isConnected() {
		return this.isConnected;
	}
	
}
