package jdbc.view.clientInput;

public enum ExportingFileOption {
	
	TXT_FILE_ALL("File .txt con query e risultato"), 
	TXT_FILE_ONLY_QUERY("File .txt con solo query"), 
	TXT_FILE_ONLY_RESULTS("File .txt con solo i risultati");
	
	private String desc;
	
	private ExportingFileOption(String description) {
		this.desc = description;
	}
	
	@Override
	public String toString() {
		return this.desc;
	}

}
