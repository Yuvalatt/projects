
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


@WebServlet("/IOTServlet")
public class IOTServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
		String product = jsonObj.getString("product");
		String info = jsonObj.getString("info");
		String id = jsonObj.getString("id");		
		try {
			CRUD<String, Integer> crud = new SQLCRUD("jdbc:mysql://localhost:3306/"+company, "root", "password", product);
			crud.create(company+":"+product+":"+ info + ":" + id + ":");
			response.setStatus(203);
		} catch (SQLException e) {
			System.err.println("SQL CONNECTION ERROR");
			e.printStackTrace();
		}		
	}
}