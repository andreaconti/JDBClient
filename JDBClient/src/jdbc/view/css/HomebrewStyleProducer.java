package jdbc.view.css;

public class HomebrewStyleProducer implements StyleProducer {

	@Override
	public void setStyleOf(CSSStyleable toSetStyle) {
		toSetStyle.resetCSSStyle();
		// TODO toSetStyle.setCSSStyle(cssPath);
	}

}
