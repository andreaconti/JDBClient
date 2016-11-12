package jdbc.view.clientOutput;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tuple {
	
	private List<?> valuesData;

	public Tuple(List<?> values) {
		this.valuesData = values;
	}
	
	public Tuple(Object... values) {
		this( Arrays.asList(values) );
	}

	public List<String> getValuesData() {
		return valuesData.stream().map(Object::toString).collect(Collectors.toList());
	}

	public void setValuesData(List<?> valuesData) {
		this.valuesData = valuesData;
	}
	
	public int size() {
		return valuesData.size();
	}
	
	

}
