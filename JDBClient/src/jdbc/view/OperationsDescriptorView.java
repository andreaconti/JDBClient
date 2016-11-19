package jdbc.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jdbc.view.css.CSSStyleable;

class OperationsDescriptorView extends Stage implements CSSStyleable {
	
	private StringProperty text = new SimpleStringProperty();
	private TextArea textArea;

	public OperationsDescriptorView(double width, double height) {
		this.textArea = new TextArea();
		textArea.textProperty().bind(text);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		BorderPane rootNode = new BorderPane();
		rootNode.setPadding(new Insets(10,10,10,10));
		this.setScene(new Scene(rootNode, width, height));
		rootNode.setCenter(textArea);
	}
	
	public void appendText(List<String> strings) {
		text.set(text.get() + "\n" + strings.stream().collect(Collectors.joining("\n")));
	}
	
	public void appendText(String...strings) {
		this.appendText(Arrays.asList(strings));
	}
	
	public void resetText() {
		text.set("");
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