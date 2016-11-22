package jdbc.view.events;

import java.nio.file.Path;

import javafx.event.Event;
import javafx.event.EventType;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.QueryResult;

public class ExportQueryResultEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<? extends ClientEvent> ANY = new EventType<>(ClientEvent.ANY, "All ExportQueryResultEvents");
	
	private QueryResult queryResult;
	private Path directoryPath;
	private String fileName;
	private ExportingFormat exportingFormat;
	private ExportingOptions exportingOptions;
	
	public ExportQueryResultEvent(EventType<? extends Event> eventType, QueryResult queryResult, Path filePath, String fileName, ExportingFormat exportingFormat, ExportingOptions exportingOptions) {
		super(eventType);
		this.directoryPath = filePath;
		this.fileName = fileName;
		this.queryResult = queryResult;
		this.exportingFormat = exportingFormat;
		this.exportingOptions = exportingOptions;
	}

	public QueryResult getQueryResult() {
		return queryResult;
	}

	public ExportingFormat getExportingFormat() {
		return exportingFormat;
	}

	public ExportingOptions getExportingOptions() {
		return exportingOptions;
	}

	public Path getDirectoryPath() {
		return directoryPath;
	}
	
	public String getFileName() {
		return this.fileName;
	}

}
