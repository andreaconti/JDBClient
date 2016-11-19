package jdbc.exporter;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface that describes how works the exporter used in order to save on a file with different formats and options the QueryResults.
 * There is a different implementation for any different <code>ExportingFormat</code> type but the <code>ExportingOptions</code> options
 * choosen by the user must be implemented by the system that uses the methods below.
 * 
 * @author andreaconti
 */

public interface QueryExporter {
	
	/**
	 * 
	 * @param string
	 * @throws IOException
	 */
	public void writeDescription(String string) throws IOException;
	
	/**
	 * 
	 * @param title
	 * @throws IOException
	 */
	public void writeQueryTitle(String title) throws IOException;
	
	/**
	 * 
	 * @param attributes
	 * @throws IOException
	 */
	public void writeAttributes(List<String> attributes) throws IOException;
	
	/**
	 * 
	 * @param tuple
	 * @throws IOException
	 */
	public void writeTuple(List<String> tuple) throws IOException;
	
	/**
	 * 
	 * @param tuples
	 * @throws IOException
	 */
	public void writeTuples(List<List<String>> tuples) throws IOException;
	
	/**
	 * 
	 * @param filePath
	 * @param fileName
	 * @param options
	 * @throws IOException
	 */
	public void openStream(Path filePath, String fileName, OpenOption... options) throws IOException;
	
	/**
	 * 
	 * @throws IOException
	 */
	public void closeStream() throws IOException;
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	public static QueryExporter getExporter(ExportingFormat format) {
		//TODO
		return null;
	}

}
