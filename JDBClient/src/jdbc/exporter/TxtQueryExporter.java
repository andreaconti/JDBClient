package jdbc.exporter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class TxtQueryExporter implements QueryExporter {
	
	private Map<String, Integer> attributes;
	private Writer wrt;
	
	public TxtQueryExporter() {
		
	}
	
	@Override
	public void openStream(Path filePath, String fileName, OpenOption... options) throws IOException {
		wrt = Files.newBufferedWriter(filePath.resolve(fileName).resolve(".txt"), options);
	}
	
	@Override
	public void writeDescription(String string) throws IOException {
		wrt.write(string);
	}

	@Override
	public void writeQueryTitle(String title) throws IOException {
		wrt.write(title.toString());
	}

	@Override
	public void writeAttributes(List<String> attributes) throws IOException {
		
	}
	
	public void wirteAttributes(Map<String, Integer> attributes) throws IOException {
		
	}

	@Override
	public void writeTuple(List<String> tuple) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTuples(List<List<String>> tuples) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeStream() throws IOException {
		// TODO Auto-generated method stub
		
	}


}
