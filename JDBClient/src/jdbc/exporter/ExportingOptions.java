package jdbc.exporter;

public enum ExportingOptions {
	
	ONLY_QUERY("Export Only Query"), ONLY_RESULT("Export only result"), ALL("Export all");
	
	private String description;
	
	private ExportingOptions(String description) {
		this.description = description;
	}

	public String getFormat() {
		return description;
	}

}
