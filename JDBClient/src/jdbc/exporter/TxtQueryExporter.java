package jdbc.exporter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class TxtQueryExporter implements QueryExporter {
	
	private List<String> attributes;
	private List<Integer> attributesLenght;
	private Writer wrt;
	private Path completePath;
	
	private boolean isWritingATable = false;
	
	@Override
	public void openStream(Path filePath, String fileName, OpenOption... options) throws IOException {
		
		if ( wrt != null )
			throw new ExportingException("stream just opened, in order to open a new stream the last one must be closed");
		
		completePath = filePath.resolve(fileName).resolve(".txt");
		wrt = Files.newBufferedWriter(completePath, options);
	}
	
	@Override
	public void writeDescription(String string) throws IOException {
		wrt.write("\n" + string + "\n");
		this.isWritingATable = false;
	}

	@Override
	public void writeQueryTitle(String title) throws IOException {
		wrt.write("\n" + title.toString() + "\n");
		this.isWritingATable = false;
	}

	@Override
	public void writeAttributes(List<String> attributes) throws IOException {
		this.attributes = attributes;
		this.attributesLenght = new ArrayList<>();
		attributes.forEach( string -> attributesLenght.add(string.length()));
		this.writeAttributes(attributes, attributesLenght);
	}
	
	@Override
	public void writeAttributes(List<String> attributes, List<Integer> lenghtAttributes) throws IOException {
		
		if (attributes == null || lenghtAttributes == null)
			throw new NullPointerException("attributes == null || lenghtAttributes == null");
		
		if ( attributes.size() != lenghtAttributes.size() )
			throw new IllegalArgumentException("attributes.size() != lenghtAttributes.size()");
		
		this.attributes = attributes;
		this.attributesLenght = lenghtAttributes;
		this.isWritingATable = true;
		
		// scrivo l'introduzione della tabella
		for ( int i = 0 ; i < attributes.size() ; i++ ) {
			writeString(attributes.get(i) + " ", lenghtAttributes.get(i));
		}
		wrt.write("\n");
	}

	@Override
	public void writeTuple(List<String> tuple) throws IOException {
		if (this.isWritingATable == false) throw new ExportingException("attributes for this tuple not setted");
		if (tuple == null) throw new NullPointerException("tuple == null");
		if (tuple.size() != this.attributes.size()) throw new IllegalArgumentException( tuple + "does not match the attributes size");
		
		for(int i = 0 ; i < tuple.size(); i++)
			this.writeString(tuple.get(i) + " ", attributesLenght.get(i));
		wrt.write("\n");
	}

	@Override
	public void writeTuples(List<List<String>> tuples) throws IOException {
		for ( List<String> tuple : tuples )
			this.writeTuple(tuple);
	}

	@Override
	public void closeStream() throws IOException {
		
		if ( wrt == null )
			throw new ExportingException("there is no stream to be closed");
		
		wrt.close();
		wrt = null;
	}
	
	/* METODI PRIVATI */
	
	private void writeString(String attribute, int lenght) throws IOException {
		
		int size = attribute.length();
		
		for (int i = 0 ; i < lenght ; i++ ) {
			if ( i < size )
				wrt.write(attribute.substring(i, i + 1));
			else
				wrt.write(" ");
		}
		
	}

	@Override
	public Path getCompletePath() {
		return completePath;
	}

}
