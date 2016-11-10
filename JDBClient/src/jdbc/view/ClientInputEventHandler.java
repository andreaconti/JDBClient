package jdbc.view;

import javafx.event.EventHandler;

public interface ClientInputEventHandler extends EventHandler<ClientInputEvent> {
	
	@Override
	public void handle(ClientInputEvent ev);

}
