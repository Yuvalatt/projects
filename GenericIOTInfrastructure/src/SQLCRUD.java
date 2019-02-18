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
	private String product;
	private String info;
	private String id;
	
	public SQLCRUD(String url, String user, String password, String tableName) throws SQLException{		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.url = url;
		this.user = user;
		this.pass = password;
		table = tableName;
		connect();
	}

	public void connect() throws SQLException{
		conn = DriverManager.getConnection(url, user, pass);
		stmt = conn.createStatement();
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
		parseData(data);
		String sql_last_row = "SELECT LAST_INSERT_ID()";
		sql_stmt = "INSERT INTO " + table + " VALUES(" + id +", '"+ product +"', '"+ info +"')";

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

	private void parseData(String data) {
		System.out.println(data);
		String [] str = data.split(":");
		product = str[1];
		info = str[2];
		id = str[3];
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
	
	/*public static void main(String[] args) throws SQLException {
		String company = "tadiran";
		String product = "alpha";
		CRUD<String, Integer> crud = new SQLCRUD("jdbc:mysql://localhost:3306/"+company, "root", "password", product);
		crud.create("tadiran:alpha:12:200:");
	}*/
}