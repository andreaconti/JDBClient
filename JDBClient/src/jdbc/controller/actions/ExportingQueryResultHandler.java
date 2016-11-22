package jdbc.controller.actions;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javafx.event.EventHandler;
import jdbc.exporter.ExportingException;
import jdbc.exporter.QueryExporter;
import jdbc.view.ClientUIController;
import jdbc.view.events.ExportQueryResultEvent;

public class ExportingQueryResultHandler implements EventHandler<ExportQueryResultEvent> {
	
	private ExportQueryResultEvent event;
	private QueryExporter exporter;
	private ClientUIController crtl;
	
	private OpenOption option = StandardOpenOption.CREATE_NEW;
	
	public ExportingQueryResultHandler(ClientUIController crtl) {
		this.crtl = crtl;
	}

	@Override
	public void handle(ExportQueryResultEvent event) {
		this.event = event;
		
		try {
			exporter = QueryExporter.getExporter(event.getExportingFormat());
		} catch (ExportingException e) { /*TODO */ }
		
		try {
			
			if ( verify() == false ) return;
			exporter.openStream(event.getDirectoryPath(), event.getFileName(), option);
			
			
		} catch (Exception e) {}
	}
	
	private boolean verify() {
		
		if ( Files.isDirectory(exporter.getCompletePath()) ) {
			this.crtl.getUserDialog().showError("The path given is a directory");
			return false;
		}
		
		if ( Files.exists(exporter.getCompletePath()) ) {
			boolean response = this.crtl.getUserDialog().showChoice("Files already exists, append the query ?");
			if (response == false) return false;
			else option = StandardOpenOption.APPEND;
		}
		
		return true;
	}
	
	/*
	private void writeTable() {
		try {
			
			
		} catch (IOException e) {}
	}
	*/

}
