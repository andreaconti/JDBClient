package jdbc.model.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbc.model.Attribute;
import jdbc.model.StringTable;
import jdbc.model.StringTuple;
import jdbc.model.Table;

public class DB2ConnectionController implements ConnectionController {
	
	private static final String URL_PREFIX = "jdbc:db2://";
	private Connection connection;
	private List<WarningDetectedListener> listeners;

	/*COSTRUTTORE*/
	public DB2ConnectionController() {
		listeners = new ArrayList<>();
	}
	
	/*APERTURA CONNESSIONE*/
	public void connectTo(String url, String user, String password) throws SQLException {
		/**
		 * Attempts to connect to the Db2 database located at the  specified url
		 * @exception db2client.connection.DB2Exception
		 */
		
			if (url == null || url.isEmpty() || user == null || user.isEmpty() || password == null
					|| password.isEmpty())
				throw new IllegalArgumentException("Argomenti non validi. Inserire url, user e password.");
			if (connection != null) {
				connection.close();
			}
			connection = DriverManager.getConnection(URL_PREFIX+url.trim(), user, password);
			//checkConnectionWarnings(connection);
	}
	/*APERTURA CONNESSIONE COMPLETA*/
	
	public void connectTo(String url, String port, String dbName, String user, String password) throws SQLException {
		this.connectTo(url + ":" + port.trim() + "/" + dbName.trim(), user, password);
	}
	
	/*DISCONNESSIONE*/
	public void disconnect() throws SQLException {
		
	/**
	 * Disconnects from the database
	 * @exception db2client.connection.DB2Exception
	 * DB2Exception
	 */ 
			if(!(connection == null)) {	
				checkConnectionWarnings(connection);
				connection.close();
			}
	
	}
	
	
	/*ESECUZIONE QUERY*/
	public Table executeQuery(String sql) throws SQLException {
		
		/**
		 * @param sql					The sql query to be executed
		 * @return jdbc.model.Table		
		 */
		Table result = new StringTable();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			result = insertValues(rs);
			
			checkConnectionWarnings(connection);
			checkStatementWarnings(stmt);
			
			
			rs.close();
			stmt.close();
		
		return result;
	}
	
	/*FUNZIONE DI APPOGGIO PER L'INSERIMENTO DATI IN UNA TABLE*/
	private Table insertValues(ResultSet rs) throws SQLException {
		Table result = new StringTable();
		if (rs == null)
			throw new IllegalArgumentException("ResultSet nullo");
		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();			
			for(int i = 1; i <= columnCount; i++) {
				result.addAttribute(new Attribute(rsmd.getColumnName(i),
						rsmd.getColumnLabel(i),
						rsmd.getColumnDisplaySize(i),
						rsmd.getColumnType(i)));
			}
			while(rs.next()) {
				checkResultSetWarnings(rs);
				StringTuple tuple = new StringTuple();
				for(int i = 1; i <= columnCount; i++) {
					tuple.addValue(rs.getString(i) ==  null ? "-" : rs.getString(i));
				}
				result.addTuple(tuple);
			}
		return result;
	}
	
	/*CHECK WARNING CONNESSIONE*/
	private void checkConnectionWarnings(Connection connection) throws SQLException {
		SQLWarning warning;
		warning = connection.getWarnings();
		while(warning != null) {
			fireWarningDetectedEvent(warning);
			warning = warning.getNextWarning();
		}
	}
	/*CHECK WARNING STATEMENT*/
	private void checkStatementWarnings(Statement stmt) throws SQLException {
		SQLWarning warning;
		warning = stmt.getWarnings();
		while(warning != null) {
			fireWarningDetectedEvent(warning);
			warning = warning.getNextWarning();
		}
	}
	/*CHECK WARNING RESULT SET*/
	private void checkResultSetWarnings(ResultSet rs) throws SQLException{
		SQLWarning warning;
		warning = rs.getWarnings();
		while(warning != null) {
			fireWarningDetectedEvent(warning);
			warning = warning.getNextWarning();
		}
	}
	
	/*SPARA EVENTO OGNIQUALVOLTA UN WARNING VIENE RILEVATO*/
	private void fireWarningDetectedEvent(SQLWarning warning) {
		WarningDetectedEvent warningDetectedEvent = new WarningDetectedEvent(this,warning);
		listeners.forEach(l -> l.WarningDetected(warningDetectedEvent));
	}
	
	/*AGGIUNTA LISTENERS*/
	public void addWarningDetectedEvent(WarningDetectedListener listener) {
		listeners.add(listener);
	}
	/*RIMOZIONE LISTENERS*/
	public void removeWarningDetectedListener(int index) {
		listeners.remove(index);
	}
	public void removeWarningDetectedListener(WarningDetectedListener listener) {
		if(listeners.indexOf(listener)<0){
			throw new IllegalArgumentException("Il listener non è presente nella lista");
		}
		else listeners.remove(listener);
	}
	
	
}
