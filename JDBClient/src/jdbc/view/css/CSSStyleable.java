package jdbc.view.css;

import java.util.List;

/**
 * A styleable class that implements this interface must 
 * 
 * @author andreaconti
 */

public interface CSSStyleable {
	
	public void setCSSStyle(List<String> cssPath);
	public void addCSSStyle(String cssPath);
	public void resetCSSStyle();
	public void removeCSSStyle(String cssPath);

}
