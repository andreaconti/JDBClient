package jdbc.view.events;

import javafx.event.EventType;

public class AddRemoveDatabaseEvent extends ClientEvent {

	private static final long serialVersionUID = 1L;
	public static final EventType<AddRemoveDatabaseEvent> ALL_DATABASE_EVENTS = new EventType<AddRemoveDatabaseEvent>("All AddRemoveDatabaseEvents");
	
	public static final EventType<AddRemoveDatabaseEvent> SHOW_DATABASES_REQUEST = new EventType<AddRemoveDatabaseEvent>(ALL_DATABASE_EVENTS,"Request to show all Databases");
	public static final EventType<AddRemoveDatabaseEvent> ADD_DATABASE_REQUEST = new EventType<AddRemoveDatabaseEvent>(ALL_DATABASE_EVENTS,"Add Database Request");
	public static final EventType<AddRemoveDatabaseEvent> REMOVE_DATABASE_REQUEST = new EventType<AddRemoveDatabaseEvent>(ALL_DATABASE_EVENTS,"Remove Database Request");
	
	private String databasePath;

	public AddRemoveDatabaseEvent(EventType<? extends ClientEvent> eventType, String databasePath) {
		super(eventType);
		this.databasePath = databasePath;
	}


	public String getDatabasePath() {
		return databasePath;
	}
	
}
