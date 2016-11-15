package jdbc.view.clientOutput;

import javafx.event.Event;
import javafx.event.EventType;

public class ClientOutputEvent extends Event {
	
	private static final long serialVersionUID = 1L;
	private ClientOutput sender;
	
	public static final EventType<ClientOutputEvent> EXPORT_QUERY_RESULT = new EventType<>("Save on file query result");

	public ClientOutputEvent(ClientOutput sender, EventType<? extends Event> eventType) {
		super(eventType);
		this.sender = sender;
	}

	public ClientOutput getSender() {
		return sender;
	}

}
