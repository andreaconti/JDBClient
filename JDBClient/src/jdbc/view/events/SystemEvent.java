package jdbc.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SystemEvent extends Event {

	private static final long serialVersionUID = 1L;
	public static final EventType<SystemEvent> ANY = new EventType<>("Any System Event");
	public static final EventType<SystemEvent> CLOSE = new EventType<>(SystemEvent.ANY, "Close Application");

	public SystemEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
