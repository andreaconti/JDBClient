package jdbc.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class QueryError {
	
	private String errorCode;
	private String description;
	private LocalDateTime timeCreation;
	
	public QueryError(String errorCode, String description) {
		
		if ( errorCode == null || description == null )
			throw new NullPointerException("cod == null || description == null");
		
		this.errorCode = errorCode;
		this.description = description;
		this.timeCreation = LocalDateTime.now();
		
	}
	
	public QueryError(Number errorCode, String description) {
		this(errorCode.toString(), description);
	}
	
	public QueryError(String errorCode) {
		this(errorCode, "Description Not Found");
	}
	
	public QueryError(Number errorCode) {
		this(errorCode.toString());
	}
	
	public String getTimeCreation() {
		return timeCreation.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getDescription() {
		return description;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
