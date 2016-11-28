package jdbc.model.controller;

import java.sql.SQLException;
import java.sql.SQLWarning;

public abstract class ErrorHandler {
	
	public abstract void handleSQLException(SQLException ex);
	public abstract void handleSQLWarning(SQLWarning warning);
	
	public String buildErrorString(SQLException ex) {
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");

		sb.append("SQLState: ") 
		.append(ex.getSQLState())
		.append(lineSeparator)
		.append("Error code: ")
		.append(ex.getErrorCode())
		.append(lineSeparator)
		.append("Message: ")
		.append(ex.getMessage());
		return sb.toString();
	}
	public String buildWarningString(SQLWarning warning) {
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
			sb.append("SQLState: ") 
			.append(warning.getSQLState())
			.append(lineSeparator)
			.append("Warning: ")
			.append(": ")
			.append(warning.getErrorCode())
			.append(lineSeparator)
			.append("Message: ")
			.append(warning.getMessage());
			return sb.toString();
	}
}
