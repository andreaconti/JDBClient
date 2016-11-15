package jdbc.exporter;

public enum ExportingFormat {

	TXT(".txt"), CSV(".csv");
	
	private String format;
	
	private ExportingFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

}
