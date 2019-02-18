import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement stmt;
	private ResultSet results;  
	
    public ProductServlet() {
       super();
       connect();
    }
    
    public void connect(){
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/iotCompanies", "root", "password");
			stmt = conn.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestBody = request.getReader().lines().collect(Collectors.joining());
		JSONObject jsonObj = new JSONObject(requestBody);
		String company = jsonObj.getString("company");
		String product = jsonObj.getString("pname");
		
		if (productExists(company, product)) {
			sendResponse(response, false);
		}
		else {
			registerProduct(company, product);
			sendResponse(response, true);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = request.getParameter("param");
		JSONObject job = new JSONObject(json);
		JSONArray items = job.getJSONArray("items");
		removeItems(items);
	}
	
	private void removeItems(JSONArray items) {		
		for (int i = 0; i < items.length(); i++) {
			JSONObject o = items.getJSONObject(i);
			deleteProduct(o.getString("id"));
		}
	}

	private void deleteProduct(String item) {
		String sql_stmt = "DELETE FROM products WHERE id="+item;
		try {
			stmt.executeUpdate(sql_stmt);	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	private void registerProduct(String company, String product) {
		String sql_stmt = "INSERT INTO products VALUES(" + null +", '"+ company +"', '"+ product +"')";
		try {
			stmt.executeUpdate(sql_stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private boolean productExists(String company, String product) {
		int count = 0;
		String sql_get_company = "SELECT COUNT(*) FROM products WHERE company ='" + company +"' AND product_name='"+ product +"'";		
		try {
			results = stmt.executeQuery(sql_get_company);
			results.next();
			count = results.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (0 != count);
	}
	
	private void sendResponse(HttpServletResponse response, boolean state) {
		JSONObject obj = new JSONObject();
		obj.put("success", state);
		obj.put("id", getLastId());
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}

	private int getLastId() {
		int newId = 0;
		String sql_last_row = "SELECT LAST_INSERT_ID()";
		try {
			results = stmt.executeQuery(sql_last_row);
			results.next();
			newId = results.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return newId;
	}
}