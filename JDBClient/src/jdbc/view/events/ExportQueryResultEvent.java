package jdbc.view.events;

import javafx.event.Event;
import javafx.event.EventType;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;
import jdbc.view.ClientUIController;
import jdbc.view.QueryResult;

public class ExportQueryResultEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<? extends ClientEvent> ANY = new EventType<>(ClientEvent.ANY, "All ExportQueryResultEvents");
	
	private QueryResult queryResult;
	private ExportingFormat exportingFormat;
	private ExportingOptions exportingOptions;
	private boolean isAppendingRequested;
	
	public ExportQueryResultEvent(EventType<? extends Event> eventType, QueryResult queryResult, ExportingFormat exportingFormat, ExportingOptions exportingOptions, boolean isAppendingRequested) {
		super(eventType);
		this.queryResult = queryResult;
		this.exportingFormat = exportingFormat;
		this.exportingOptions = exportingOptions;
		this.isAppendingRequested = isAppendingRequested;
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
	
	public boolean isAppendingRequested() {
		return this.isAppendingRequested;
	}

}
