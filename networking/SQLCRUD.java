package il.co.ilrd.networking;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLCRUD implements CRUD<String, Integer>{		
	private static final long serialVersionUID = 1L;
	private final String url;
	private final String user;
	private final String pass;
	private String table;
	private String sql_stmt;
	private Connection conn;
	private Statement stmt;
	private ResultSet results;

	public SQLCRUD(String tableName) throws UnknownHostException{		
		url = "jdbc:mysql://localhost:3306/monitor_log";
		user = "root";
		pass = "password";
		table = tableName;
	}

	public SQLCRUD(String url, String user, String password, String tableName) throws UnknownHostException{		
		this.url = url;
		this.user = user;
		this.pass = password;
		table = tableName;
	}

	public void connect(){
		try{
			conn = DriverManager.getConnection(url, user, pass);
			stmt = conn.createStatement();
		}catch(SQLException e) {
			System.out.println("Connection Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void disconnect(){
		try {
			if (null != results) {
				results.close();
			}		
			stmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("Disconnect Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public Integer create(String data) {		
		int newId = 0;			
		String [] line = data.split(":");
		String user_id = line[0];
		String str = line[1];
		String sql_last_row = "SELECT LAST_INSERT_ID()";
		sql_stmt = "INSERT INTO " + table + " VALUES(NULL, '"+ user_id +"', '"+ str +"')";		
		try {
			stmt.executeUpdate(sql_stmt);
			results = stmt.executeQuery(sql_last_row);
			results.next();
			newId = results.getInt(1);  //returns last row number	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return newId;
	}

	@Override
	public String read(Integer id) {
		sql_stmt = "SELECT * FROM " + table +" WHERE id="+id;
		String data = null;
		try {
			results = stmt.executeQuery(sql_stmt);
			if (null != results) {
				results.next();
				data = results.getString("data");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public void update(Integer id, String data) {
		sql_stmt = "UPDATE " + table +" SET data='"+data + "' WHERE line="+id;
		try {
			stmt.executeUpdate(sql_stmt);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Integer line) {
		sql_stmt = "DELETE FROM " + table +" WHERE line="+line;
		try {
			stmt.executeUpdate(sql_stmt);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}