package jdbc.view;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.sun.glass.ui.Screen;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.*;

public class ExportingFileForm extends Dialog<Path> {
	
	private TextField directoryChoosen;
	private Button browse;
	private ComboBox<ExportingOptions> options;
	private ComboBox<ExportingFormat> format;
	
	private ButtonType close;
	private ButtonType ok;
	private Path result;

	public ExportingFileForm(Path oldDirectory, List<ExportingOptions> exportingOptions, List<ExportingFormat> exportingFormats ) {
		
		VBox rootNode = new VBox(10);
		
		// imposto directoryChoosed
		directoryChoosen = new TextField();
		directoryChoosen.setMinWidth(Screen.getMainScreen().getWidth() / 3);
		directoryChoosen.setEditable(false);
		directoryChoosen.setPromptText("Here There is the path choosen");
		if (oldDirectory != null)
			directoryChoosen.setText(oldDirectory.toString());
		HBox.setHgrow(directoryChoosen, Priority.ALWAYS);
		
		// imposto tasto browse
		browse = new Button("Browse");
		browse.setOnAction( ev -> {

			UserDialog d = new UserDialog();
			Optional<File> result = d.chooseDirectory(oldDirectory != null ? oldDirectory.toFile() : Paths.get(System.getProperty("user.home")).toFile());
			if ( result.isPresent()) {
				this.result = result.get().toPath();
				directoryChoosen.setText(result.get().toString());
			}
		});
		
		HBox browsingBar = new HBox(10);
		browsingBar.getChildren().addAll(directoryChoosen, browse);
		
		// imposto options e format
		options = new ComboBox<>(FXCollections.observableArrayList(exportingOptions));
		options.setMinWidth(Screen.getMainScreen().getWidth() / 3);
		options.getSelectionModel().select(0);
		
		format = new ComboBox<ExportingFormat>(FXCollections.observableArrayList(exportingFormats));
		format.setMinWidth(Screen.getMainScreen().getWidth() / 3);
		format.getSelectionModel().select(0);
		
		close = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ok = new ButtonType("Done", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(ok, close);
		rootNode.getChildren().addAll(browsingBar, options, format);
		
		this.getDialogPane().setContent(rootNode);
		
		this.setResultConverter( button -> {
			if ( button == ok && result != null) 
				return result;
			else 
				return null;
		});
		
		Node enablerOk = this.getDialogPane().lookupButton(ok);
		enablerOk.setDisable(true);
		
		directoryChoosen.textProperty().addListener( (obs, oldV, newV) -> {
			enablerOk.setDisable(directoryChoosen.getText().trim().isEmpty());
		});
		
	}
	
	public ExportingFileForm( List<ExportingOptions> exportingOptions, List<ExportingFormat> exportingFormats) {
		this( Paths.get(""), exportingOptions, exportingFormats );
	}
	
	public ExportingOptions getExportingOptionSelected() {
		return options.getSelectionModel().getSelectedItem();
	}
	
	public ExportingFormat getExportingFormatSelected() {
		return format.getSelectionModel().getSelectedItem();
	}
	
	public Optional<Path> getDirectorySelected() {
		return Optional.ofNullable(result);
	}


}
