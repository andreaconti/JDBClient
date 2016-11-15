package jdbc.view.events;

import javafx.event.Event;
import javafx.event.EventType;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;

public class HistoryFileEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<? extends ClientEvent> ANY = new EventType<>(ClientEvent.ANY, "Any HistoryFileEvent");
	
	private ExportingOptions exportingOptions;
	private ExportingFormat exportingFormats;
	private String databasePath;
	
	
	public HistoryFileEvent(EventType<? extends Event> eventType, ExportingOptions exportingOptions,
			ExportingFormat exportingFormats, String databasePath) {
		super(eventType);
		this.exportingOptions = exportingOptions;
		this.exportingFormats = exportingFormats;
		this.databasePath = databasePath;
	}

	public ExportingOptions getExportingOptions() {
		return exportingOptions;
	}


	public ExportingFormat getExportingFormats() {
		return exportingFormats;
	}


	public String getDatabasePath() {
		return databasePath;
	}

}
