package jdbc.view.events;

import javafx.event.EventType;
import jdbc.view.ClientUIController;

public class ConnectionEvent extends ClientEvent {
	
	
	private static final long serialVersionUID = 1L;
	
	public static final EventType<? extends ClientEvent> ANY_CONNECTION_REQUEST = new EventType<>(ClientEvent.ANY, "Any Connection Request");
	public static final EventType<? extends ClientEvent> CONNECTION_REQUEST = new EventType<>(ConnectionEvent.ANY_CONNECTION_REQUEST, "Connection Request");
	public static final EventType<? extends ClientEvent> DISCONNECTION_REQUEST = new EventType<>(ConnectionEvent.ANY_CONNECTION_REQUEST, "Disconnection Request");
	
	private ClientUIController source;
	private String username;
	private String password;
	private String databasePath;

	public ConnectionEvent(ClientUIController source, String username, String password, String databasePath, EventType<? extends ClientEvent> eventType) {
		super(eventType);
		this.username = username;
		this.databasePath = databasePath;
		this.password = password;
	}

	public ClientUIController getSource() {
		return source;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDatabasePath() {
		return databasePath;
	}
	
	

}