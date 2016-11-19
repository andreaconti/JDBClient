package jdbc.view;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.sun.glass.ui.Screen;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.css.CSSStyleable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class UserDialog implements CSSStyleable {
	
	private String cssPath;
	
	
	
	public void showInformation(String info) {
		
		validateObjects(info);
		
		Alert infoAlert = new Alert(AlertType.INFORMATION);

		applyStyle(infoAlert);
		
		infoAlert.setTitle("Information");
		infoAlert.setHeaderText(info);
		infoAlert.showAndWait();
	}
	
	public void showError(String error) {
		
		validateObjects(error);
		
		Alert infoAlert = new Alert(AlertType.ERROR);
		
		applyStyle(infoAlert);
		
		infoAlert.setTitle("Error");
		infoAlert.setHeaderText(error);
		infoAlert.showAndWait();
	}
	
	public Optional<String> askForString(String title, String promptText) {
		
		validateObjects(title);
		
		Dialog<String> d = new Dialog<>();
		d.setTitle(title);
		
		applyStyle(d);
		
		{
			ButtonType ok = new ButtonType("ok", ButtonData.OK_DONE);
			d.getDialogPane().getButtonTypes().add(ok);
			
			VBox root = new VBox();
			TextField t = new TextField();
			t.setMinWidth(Screen.getMainScreen().getWidth() / 3);
			t.requestFocus();
			t.setPromptText(promptText);
			VBox.setMargin(t, new Insets(10));
			root.getChildren().addAll( t);
			d.getDialogPane().setContent(root);
			d.setResultConverter( button -> {
				if ( button == ok )
					return t.getText();
				else 
					return null;
			});
		}
		
		Optional<String> result = d.showAndWait();
		if ( result.isPresent() )
			return result;
		else
			return Optional.empty();
		
	}
	
	public boolean showChoice(String request) {
		
		validateObjects(request);
		
		Dialog<Boolean> d = new Dialog<>();
		d.setTitle("Enter The Project Title");
		
		applyStyle(d);
		
		{
			ButtonType ok = new ButtonType("Yes", ButtonData.OK_DONE);
			ButtonType undone = new ButtonType("No", ButtonData.CANCEL_CLOSE);
			d.getDialogPane().getButtonTypes().addAll(ok, undone);
			
			FlowPane root = new FlowPane();
			Label t = new Label(request);
			t.setMinWidth(Screen.getMainScreen().getWidth() / 3);
			FlowPane.setMargin(t, new Insets(10));
			root.getChildren().add(t);
			d.getDialogPane().setContent(root);
			d.setResultConverter( button -> {
				if ( button == ok )
					return true;
				else 
					return false;
			});
		}
		
		return d.showAndWait().get();

	}
	
	//TODO versione in cui si specifica anche il controller della UI
	public Optional<File> chooseDirectory(String title, File initialDirectory) {
		
		validateObjects(title, initialDirectory);
		
		DirectoryChooser f = new DirectoryChooser();
		f.setTitle(title);
		f.setInitialDirectory(initialDirectory);
		File result = f.showDialog(null);
		
		if ( result == null )
			return Optional.empty();
		else
			return Optional.of(result);
		
	}
	
	public Optional<File> chooseDirectory(File initialDirectory) {
		return chooseDirectory("", initialDirectory);
	}
	
	public Optional<File> chooseDirectory() {
		return chooseDirectory("", Paths.get(System.getProperty("user.home")).toFile());
	}
	
	public Optional<File> chooseDirectory(String title) {
		return chooseDirectory(title, Paths.get(System.getProperty("user.home")).toFile());
	}
	
	public Optional<File> showSaveFileDialog(String title, File initialDirectory) {
		
		validateObjects(title, initialDirectory);
		
		FileChooser f = new FileChooser();
		f.setTitle(title);
		f.setInitialDirectory(initialDirectory);
		File result = f.showSaveDialog(null);
		
		if ( result == null )
			return Optional.empty();
		else
			return Optional.of(result);
		
	}
	
	public Optional<File> showSaveFileDialog(File initialDirectory) {
		return this.showSaveFileDialog("", initialDirectory);
	}
	
	public Optional<File> showSaveFileDialog(String title) {
		return this.showSaveFileDialog(title, Paths.get(System.getProperty("user.home")).toFile());
	}
	
	public Optional<File> showSaveFileDialog() {
		return this.showSaveFileDialog("", Paths.get(System.getProperty("user.home")).toFile());
	}
	
	
	public ClientDirectoryChooser chooseExportingDirectoryWithOptionsAndFormat(Path oldDirectory, 
											List<ExportingOptions> exportingOptions,
											List<ExportingFormat> exportingFormats) {
		ClientDirectoryChooser chooser = new ClientDirectoryChooser(oldDirectory, exportingOptions, exportingFormats);
		applyStyle(chooser);
		chooser.showAndWait();
		return chooser;
		
	}
	
	public ClientDirectoryChooser chooseExportingDirectoryWithOptionsAndFormat( List<ExportingOptions> exportingOptions,
											List<ExportingFormat> exportingFormats) {
		return this.chooseExportingDirectoryWithOptionsAndFormat(Paths.get(System.getProperty("user.home")), exportingOptions, exportingFormats);
	}
	
	
	private void validateObjects(Object...objects) {
		
		for ( Object ob : objects )
			if ( ob == null )
				throw new NullPointerException();
		
	}
	
	private void applyStyle(Dialog<?> d) {
		if (this.cssPath != null) {
			d.getDialogPane().getStylesheets().add(this.cssPath);
		}
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

}










