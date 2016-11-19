package jdbc.view.css;

@FunctionalInterface
public interface StyleProducer {
	
	public void setStyleOf(CSSStyleable toSetStyle);

}
