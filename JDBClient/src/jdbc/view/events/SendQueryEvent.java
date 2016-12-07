package jdbc.view.events;

import javafx.event.EventType;

public class SendQueryEvent extends ClientEvent {
	
	public static final EventType<SendQueryEvent> ANY = new EventType<>("Send Query Request");

	private static final long serialVersionUID = 1L;
	
	private String query;
	
	public SendQueryEvent(EventType<? extends SendQueryEvent> eventType, String query) {
		super(eventType);
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

}
