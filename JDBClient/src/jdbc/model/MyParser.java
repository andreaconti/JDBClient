package jdbc.model;

import java.util.Arrays;
import java.util.List;


public class MyParser implements Parser {
	/**
	 * Parser di base per la gestione di query multiple
	 * eventualmente riadattare secondo le esigenze
	 */
	@Override
	public List<String> parseQueries(String text) {
		if(text == null)
			throw new IllegalArgumentException("Il testo da parsare è nullo. Impossibile procedere.");
		return Arrays.asList(text.split(";"));
	}

}
