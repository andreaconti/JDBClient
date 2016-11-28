package jdbc.model.controller;

import java.sql.SQLException;
import java.sql.SQLWarning;

public class CommandLineErrorHandler extends ErrorHandler {
	public void handleSQLException(SQLException ex) {
		System.err.println(super.buildErrorString(ex));
	}
	public void handleSQLWarning(SQLWarning warning) {
		System.err.println(super.buildWarningString(warning));
	}	
}

	