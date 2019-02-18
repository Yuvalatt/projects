import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement stmt;
	private ResultSet results; 
	private String company;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignInServlet() {
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
		String email = jsonObj.getString("email");
		String password = jsonObj.getString("password");

		if (isValidUser(email, password)) {
			getProducts(company, response);
		}
		else {
			displayError(response);	
		} 		
	}

	private void getProducts(String company, HttpServletResponse response) {		
		String sql_get_products = "SELECT * FROM products WHERE company ='" + company +"'";	
		JSONObject obj = new JSONObject();
		List<JSONObject> products = new ArrayList<>();
		try {
			PrintWriter out = response.getWriter();
			PreparedStatement ps = conn.prepareStatement(sql_get_products);
			results = ps.executeQuery();
			while (results.next()) {              
				JSONObject o = new JSONObject();
				o.put("id", results.getString(1));
				o.put("product_name", results.getString(3));
				products.add(o);
			}
			
			obj.put("found", true);
			obj.put("company", company);
			obj.put("products", products);
			System.out.println(obj.toString());
			out.print(obj);
			out.flush();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}		
	}

	private void displayError(HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		obj.put("found", false);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(obj);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}

	private boolean isValidUser(String email, String password) {
		String name = null;
		String sql_get_company = "SELECT company FROM users WHERE email ='" + email+"' AND password='"+ password +"'";		
		try {
			results = stmt.executeQuery(sql_get_company);
			results.next();
			name = results.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		company = name;
		return (null != name);
	}
}