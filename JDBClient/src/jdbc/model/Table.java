package jdbc.model;

public interface Table {
	public String getValueAt(int row, int column); //Il primo valore di riga e di colonna è identificato dal valore 0
	public void addTuple(Tuple tuple);
	public void addAttribute(Attribute attribute);
}
