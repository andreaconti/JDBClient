package jdbc.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public class AddRemoveDatabaseEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<AddRemoveDatabaseEvent> ALL = new EventType<AddRemoveDatabaseEvent>("All AddRemoveDatabaseEvents");
	public static final EventType<AddRemoveDatabaseEvent> ADD_DATABASE_REQUEST = new EventType<AddRemoveDatabaseEvent>(ALL,"Add Database Request");
	public static final EventType<AddRemoveDatabaseEvent> REMOVE_DATABASE_REQUEST = new EventType<AddRemoveDatabaseEvent>(ALL,"Remove Database Request");
	
	private String databasePath;

	public AddRemoveDatabaseEvent(EventType<? extends ClientEvent> eventType, String databasePath) {
		super(eventType);
		this.databasePath = databasePath;
	}


	public String getDatabasePath() {
		return databasePath;
	}
	
}
