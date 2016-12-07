package jdbc.view;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javafx.stage.Window;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;

public interface UserDialog {

	void showInformation(String info);

	void showError(String error);

	Optional<String> askForString(String title, String promptText);

	boolean showChoice(String request);

	//TODO versione in cui si specifica anche il controller della UI
	Optional<File> chooseDirectory(String title, File initialDirectory);

	Optional<File> chooseDirectory(File initialDirectory);

	Optional<File> chooseDirectory();

	Optional<File> chooseDirectory(String title);

	Optional<File> showSaveFileDialog(String title, File initialDirectory);

	Optional<File> showSaveFileDialog(File initialDirectory);

	Optional<File> showSaveFileDialog(String title);

	Optional<File> showSaveFileDialog();

	ClientDirectoryChooser chooseExportingDirectoryWithOptionsAndFormat(Path oldDirectory,
			List<ExportingOptions> exportingOptions, List<ExportingFormat> exportingFormats);

	ClientDirectoryChooser chooseExportingDirectoryWithOptionsAndFormat(List<ExportingOptions> exportingOptions,
			List<ExportingFormat> exportingFormats);

	void setCSSStyle(List<String> cssPath);

	void addCSSStyle(String cssPath);

	void resetCSSStyle();

	void removeCSSStyle(String cssPath);

	Optional<File> showOpenFile(String title, File initialDirectory, Window owner);

	Optional<File> showOpenFile(String title, File initialDirectory);

	Optional<File> showOpenFile(String title);

	Optional<File> showOpenFile(File initialDirectory);

	Optional<File> showOpenFile();

}