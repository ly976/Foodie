package servletAPI;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import data.DataConnection;
import data.MySQL;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final DataConnection connection = new MySQL();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());	
		 try {
			 //HttpSession getSession() �C This method always returns an HttpSession object. It returns the session object attached with the request, if the request has no session attached, then it creates a new session and return it.
	   		 JSONObject msg = new JSONObject();
	   		 HttpSession session = request.getSession();
	   		 //getAttribute(String name) �C Returns the object bound with the specified name in this session, or null if no object is bound under the name. Some other methods to work with Session attributes are getAttributeNames(), removeAttribute(String name) and setAttribute(String name, Object value).
	   		 if (session.getAttribute("user") == null) {
	   			 response.setStatus(403);
	   			 msg.put("status", "Session Invalid");
	   		 } else {
	   			 String user = (String) session.getAttribute("user");
	   			 String name = connection.getUserName(user);
	   			 msg.put("status", "OK");
	   			 msg.put("user_id", user);
	   			 msg.put("name", name);
	   		 }
	   		 RPCparse.writeOutput(response, msg);
	   	 } catch (JSONException e) {
	   		 // TODO Auto-generated catch block
	   		 e.printStackTrace();
	   	 }
	} // end of doGet()

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		try {
	   		 JSONObject msg = new JSONObject();
	   		 // get request parameters for userID and password
	   		 String user = request.getParameter("user_id");
	   		 String pwd = request.getParameter("password");
	   		 if (connection.verifyLogin(user, pwd)) {
	   			 HttpSession session = request.getSession();
	   			 session.setAttribute("user", user);
	   			 // setting session to expire in 10 minutes
	   			 session.setMaxInactiveInterval(10 * 60);
	   			 // Get user name
	   			 String name = connection.getUserName(user);
	   			 msg.put("status", "OK");
	   			 msg.put("user_id", user);
	   			 msg.put("name", name);
	   		 } else {
	   			 response.setStatus(401);
	   		 }
	   		 RPCparse.writeOutput(response, msg);
	   	 } catch (JSONException e) {
	   		 // TODO Auto-generated catch block
	   		 e.printStackTrace();
	   	 }
	} // end doPost()

} // END