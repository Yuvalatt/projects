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
import org.json.JSONObject;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement stmt;
	private ResultSet results;  
	
    public SignUpServlet() {
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
		String email = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		
		if (companyExists(company)) {
			displayError(response);	
		}
		else {
			registerCompany(company, email, password);
		}
	}

	private void displayError(HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		obj.put("found", true);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}

	private void registerCompany(String company, String email, String password) {
		String sql_stmt = "INSERT INTO users VALUES(" + null +", '"+ company +"', '"+ email +"', '"+ password +"')";
		try {
			stmt.executeUpdate(sql_stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}

	private boolean companyExists(String company) {
		int count = 0;
		String sql_get_company = "SELECT COUNT(*) FROM users WHERE company ='" + company+"'";		
		try {
			results = stmt.executeQuery(sql_get_company);
			results.next();
			count = results.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (0 != count);
	}
}