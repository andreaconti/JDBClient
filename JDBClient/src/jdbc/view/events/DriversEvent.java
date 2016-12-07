package jdbc.view.events;

import java.nio.file.Path;
import java.util.Optional;

import javafx.event.Event;
import javafx.event.EventType;

public class DriversEvent extends ClientEvent {
	
	private static final long serialVersionUID = 1L;
	
	public static final EventType<DriversEvent> ALL_DRIVER_EVENTS = new EventType<>("Remove Database Request");
	public static final EventType<DriversEvent> SHOW_DRIVER_REQUEST = new EventType<>(ALL_DRIVER_EVENTS,"Remove Database Request");
	public static final EventType<DriversEvent> ADD_DRIVER_REQUEST = new EventType<>(ALL_DRIVER_EVENTS, "Add Driver Request");
	public static final EventType<DriversEvent> REMOVE_DRIVER_REQUEST = new EventType<>(ALL_DRIVER_EVENTS, "Remove Driver Request");
	
	private Optional<Path> driverPath;
	
	public DriversEvent(EventType<? extends Event> eventType, Path driverPath ) {
		super(eventType);
		this.driverPath = Optional.ofNullable(driverPath);
	}
	
	public DriversEvent(EventType<? extends Event> eventType) {
		super(eventType);
		this.driverPath = Optional.empty();
	}
	
	public Optional<Path> getDriverPath() {
		return this.driverPath;
	}

}
