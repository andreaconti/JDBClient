package jdbc.view.events;

import java.nio.file.Path;

import javafx.event.EventType;
import jdbc.exporter.ExportingFormat;
import jdbc.exporter.ExportingOptions;

public class HistoryFileEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<? extends HistoryFileEvent> ANY = new EventType<>(ClientEvent.ANY, "Any HistoryFileEvent");
	
	private ExportingOptions exportingOptions;
	private ExportingFormat exportingFormats;
	private Path exportingPath;
	
	
	public HistoryFileEvent(EventType<? extends HistoryFileEvent> eventType, ExportingOptions exportingOptions,
			ExportingFormat exportingFormats, Path exportingPath) {
		super(eventType);
		this.exportingOptions = exportingOptions;
		this.exportingFormats = exportingFormats;
		this.exportingPath = exportingPath;
	}

	public ExportingOptions getExportingOptions() {
		return exportingOptions;
	}


	public ExportingFormat getExportingFormats() {
		return exportingFormats;
	}


	public Path getDatabasePath() {
		return exportingPath;
	}

}
