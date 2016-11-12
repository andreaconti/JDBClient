package jdbc.view.clientInput;

import javafx.event.EventHandler;

public interface ClientInputEventHandler extends EventHandler<ClientInputEvent> {
	
	@Override
	public void handle(ClientInputEvent ev);

}
