package jdbc.view;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jdbc.view.css.CSSStyleable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.events.ExportQueryResultEvent;

class ClientOutput extends Stage implements CSSStyleable {
	
	// core values
	private List<QueryResultView> results;
	
	// elementi grafici
	Spinner<Integer> tableChooser;
	
	// altri valori
	Path exportingPath;
	List<ExportingOptions> exportingOptions;
	List<ExportingFormat> exportingFormats;
	private UserDialog dialog;

	public ClientOutput(List<QueryResult> results, List<ExportingOptions> exportingOptions,
				List<ExportingFormat> exportingFormats, UserDialog dialog, double width, double height) {
		
		// checks
		if ( results == null || results.isEmpty())
			throw new IllegalArgumentException("Invalid QueryResult");
		
		if ( exportingFormats == null || exportingOptions == null )
			throw new NullPointerException("(exportingFormats || exportingOptions) == null");
		
		if (dialog == null)
			throw new NullPointerException("UserDialog == null");
		
		this.exportingOptions = exportingOptions;
		this.exportingFormats = exportingFormats;
		this.results = results.stream().map(queryResult -> new QueryResultView(queryResult)).collect(Collectors.toList());
		
		this.dialog = dialog;
		
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
		rootNode.setTop(initMainMenu());
	}
	

	@Override
	public void setCSSStyle(List<String> cssPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCSSStyle(String cssPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetCSSStyle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCSSStyle(String cssPath) {
		// TODO Auto-generated method stub
		
	}
	
	private MenuBar initMainMenu() {
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.setUseSystemMenuBar(true);
		
		Menu exportOption = new Menu("Exporting Options");
		MenuItem exportQueryResult = new MenuItem("Export Query Results");
		exportOption.getItems().add(exportQueryResult);
		
		exportQueryResult.setOnAction( ev -> {
			ClientDirectoryChooser chooser = dialog.chooseExportingDirectoryWithOptionsAndFormat(exportingPath, exportingOptions, exportingFormats);
			Optional<Path> directory = chooser.getDirectorySelected();
			if ( directory.isPresent() ) {
				ExportQueryResultEvent toFire = new ExportQueryResultEvent(ExportQueryResultEvent.ANY,
														results.get(tableChooser.getValue() - 1).getQueryResult(),
														directory.get(),
														chooser.getFileName(),
														chooser.getExportingFormatSelected(),
														chooser.getExportingOptionSelected());
				this.fireEvent(toFire);
			}	
		});
		
		mainMenu.getMenus().add(exportOption);
		return mainMenu;
		
	}

}
