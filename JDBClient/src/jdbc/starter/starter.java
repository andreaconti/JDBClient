package jdbc.starter;

import java.util.Arrays;

import javafx.application.Application;
import javafx.stage.Stage;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.ClientUIController;

public class starter extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientUIController c = new ClientUIController(Arrays.asList(ExportingOptions.values()), Arrays.asList(ExportingFormat.values()));
		c.show();
	}

}