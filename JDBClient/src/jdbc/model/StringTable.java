package jdbc.model;


import java.util.ArrayList;
import java.util.List;

public class StringTable implements Table{
	

	private List<Attribute> attributes;
	private List<Tuple> tuples;
	
	
	public StringTable()   {
		attributes = new ArrayList<>();
		tuples = new ArrayList<>();
	}
	public int columnCount() {
		return attributes.size();
	}
	public int tupleCount() {
		return tuples.size();
	}
	public String getValueAt(int row , int column) {
		 return tuples.get(row).getValueAt(column).toString();
	}
	@Override
	public void addTuple(Tuple tuple) {
		this.tuples.add(tuple);
	}
	@Override
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	
	@Override
	public String toString() {
		if(!tuples.isEmpty()) {
		StringBuilder sb = new StringBuilder();
		for(Attribute a: attributes) {
			sb.append(a.toString() + "\t");
		}
		sb.append(System.getProperty("line.separator"));
		for(Attribute a: attributes) {
			for(int i = 0; i< a.getCharLength(); i++) {
				sb.append("-");
			}
			sb.append(" ");
		}
		sb.append(System.getProperty("line.separator"));
		
		for(Tuple t: tuples) {
			for(int i = 0; i < t.size(); i++) {
				sb.append(String.format("%" + attributes.get(i).getCharLength() + "s",t.getValueAt(i).toString()));
				sb.append("\t");
			}
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(this.tuples.size()).append(" records.");
		return sb.toString();
		}
		else
			return "0 records.";
	}
	
	
	
}
