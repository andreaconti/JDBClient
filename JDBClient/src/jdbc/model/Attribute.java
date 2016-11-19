package jdbc.model;


public class Attribute {
	
	private String name;
	private String label;
	private int charLength;
	private int type;


	public Attribute(String name, String label, int charLength, int type) {
		super();
		if(name == null || name.isEmpty() || label == null || label.isEmpty())
			throw new IllegalArgumentException("Parametri nulli");
		this.name = name;
		this.label = label;
		this.charLength = charLength;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public int getCharLength() {
		return charLength;
	}

	public String getLabel() {
		return label;
	}

	public int getType() {
		return type;
	}
	@Override
	public String toString() {
		return String.format("%"+ charLength + "s", label);
	}

}
