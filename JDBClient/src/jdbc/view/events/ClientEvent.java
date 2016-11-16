package jdbc.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class ClientEvent extends Event {

	private static final long serialVersionUID = 1L;
	public static final EventType<? extends ClientEvent> ANY = new EventType<>(Event.ANY, "Any Event");

	public ClientEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
