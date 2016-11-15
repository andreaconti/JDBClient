package jdbc.view.events;

import javafx.event.EventType;
import jdbc.view.ClientUIController;

public class SendQueryEvent extends ClientEvent {
	
	public static final EventType<? extends SendQueryEvent> ANY = new EventType<>(ClientEvent.ANY, "Send Query Request");

	private static final long serialVersionUID = 1L;
	
	private ClientUIController sender;
	private String query;
	
	public SendQueryEvent(EventType<? extends SendQueryEvent> eventType, ClientUIController sender, String query) {
		super(eventType);
		this.sender = sender;
		this.query = query;
	}

	public ClientUIController getSender() {
		return sender;
	}

	public String getQuery() {
		return query;
	}

}
