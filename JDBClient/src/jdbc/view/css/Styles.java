package jdbc.view.css;

public enum Styles {
	
	HOMEBREW("Home Brew", new HomebrewStyleProducer());
	
	private String name;
	private StyleProducer styleProducer;
	
	private Styles(String name, StyleProducer styleProducer) {
		this.name = name;
		this.styleProducer = styleProducer;
	}
	
	public String getName() {
		return name;
	}
	public StyleProducer getStyleProducer() {
		return styleProducer;
	}
	
	

}
