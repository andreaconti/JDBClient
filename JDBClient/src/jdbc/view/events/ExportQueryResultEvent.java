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
	
	private ClientUIController source;
	private QueryResult queryResult;
	private ExportingFormat exportingFormat;
	private ExportingOptions exportingOptions;
	
	public ExportQueryResultEvent(EventType<? extends Event> eventType, ClientUIController source, QueryResult queryResult, ExportingFormat exportingFormat, ExportingOptions exportingOptions) {
		super(eventType);
		this.source = source;
		this.queryResult = queryResult;
		this.exportingFormat = exportingFormat;
		this.exportingOptions = exportingOptions;
	}

	public ClientUIController getSource() {
		return source;
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

}
