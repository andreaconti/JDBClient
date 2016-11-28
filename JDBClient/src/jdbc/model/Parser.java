package jdbc.model;

import java.util.List;

public interface Parser {
	public List<String> parseQueries(String text);
}
