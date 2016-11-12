package jdbc.view.clientInput;

import javafx.event.Event;
import javafx.event.EventType;

public class ClientInputEvent extends Event {
	
	private ClientInput sender;

	private static final long serialVersionUID = 1L;
	public static final EventType<ClientInputEvent> ROOT = new EventType<ClientInputEvent>("Root");
	public static final EventType<ClientInputEvent> CONNECTION_REQUEST = new EventType<ClientInputEvent>(ROOT, "Connection Request");
	public static final EventType<ClientInputEvent> DISCONNECTION_REQUEST = new EventType<ClientInputEvent>(ROOT, "Disconnection Request");
	public static final EventType<ClientInputEvent> SUBMIT_QUERY_REQUEST = new EventType<ClientInputEvent>(ROOT, "Submit Query");
	
	public static final EventType<ClientInputEvent> EXPORT_ON_FILE = new EventType<ClientInputEvent>(ROOT, "Save On File Request");
	public static final EventType<ClientInputEvent> EXPORT_ON_TXT_FILE_ONLY_RESULTS = new EventType<ClientInputEvent>(EXPORT_ON_FILE, "Save On .txt File Only results Request");
	public static final EventType<ClientInputEvent> EXPORT_ON_TXT_FILE_ALL = new EventType<ClientInputEvent>(EXPORT_ON_FILE, "Save On .txt File All Request");
	
	public static final EventType<ClientInputEvent> REMOVE_DATABASE_REQUEST = new EventType<ClientInputEvent>(ROOT, "Remove Database Request");	
	public static final EventType<ClientInputEvent> ADD_DATABASE_REQUEST = new EventType<ClientInputEvent>(ROOT, "Add Database Request");
	
	public ClientInputEvent(ClientInput sender, EventType<? extends ClientInputEvent> eventType) {
		super(eventType);
		this.sender = sender;
	}
	
	public ClientInput getSender() {
		return sender;
	}

}
