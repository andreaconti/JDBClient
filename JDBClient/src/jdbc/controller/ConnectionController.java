package jdbc.controller;


import jdbc.model.Table;
import java.sql.SQLException;

public interface ConnectionController {
	
	public abstract void connectTo(String url, String user, String password) throws SQLException;
	public abstract void disconnect() throws SQLException;
	public abstract Table executeQuery(String sql) throws SQLException;
	public void connectTo(String url, String port, String dbName, String user, String password) throws SQLException;

}
