package jdbc.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.CheckBox;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;

public class ExportQueryFileChooser extends HistoryFileDirectoryChooser {
	
	private CheckBox append;

	public ExportQueryFileChooser(Path oldPathFile, List<ExportingOptions> exportingOptions, List<ExportingFormat> exportingFormats) {
		super(oldPathFile, exportingOptions, exportingFormats);
		
		if ( Files.isDirectory(oldPathFile) )
			throw new IllegalArgumentException(oldPathFile + "is a directory");
		
		CheckBox append = new CheckBox("Append to existent file");
		super.rootNode.getChildren().add(append);
		
		super.browse.setOnAction( ev -> {
			UserDialog d = new UserDialog();
			Optional<File> result = d.showSaveFileDialog(oldPathFile != null ? oldPathFile.toFile() : Paths.get(System.getProperty("user.home")).toFile());
			if ( result.isPresent()) {
				super.result = result.get().toPath();
				directoryChoosen.setText(result.get().toString()); 
			}
		});
	}
	
	public boolean isAppendRequested() {
		return append.isSelected();
	}

}
