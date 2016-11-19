package jdbc.controller;

import java.sql.SQLWarning;
import java.util.EventObject;

public class WarningDetectedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private SQLWarning warning;
	
	public WarningDetectedEvent(Object source,SQLWarning warning) {
		super(source);
		this.warning = warning;
	}
	public SQLWarning getWarning(){
		return warning;
	}

}
