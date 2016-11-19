package jdbc.model;

import java.util.ArrayList;
import java.util.List;

public class StringTuple implements Tuple {
	private List<String> values;
	public StringTuple() {
		values = new ArrayList<>();
	}
	public List<String> getValues() {
		return values;
	}
	public String getValueAt(int index) {
		return values.get(index);
	}
	public void addValue(String value){
		values.add(value);
	}
	public int size() {
		return values.size();
	}
}
