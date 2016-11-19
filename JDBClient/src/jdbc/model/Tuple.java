package jdbc.model;

import java.util.List;

public interface Tuple {
	
	public int size();
	public Object getValueAt(int index);
	public List<?> getValues();
	
	
}
