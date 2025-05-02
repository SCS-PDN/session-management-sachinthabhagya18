import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Map<String, String> VALID_USERS = new HashMap<>();

    static {
        VALID_USERS.put("student1", "pass1");
        VALID_USERS.put("student2", "pass2");
        VALID_USERS.put("admin", "admin123");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 2. Validate credentials
        if (isValidUser(username, password)) {
            // 3. Create session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Store username in cookie with 1-hour expiry
            Cookie userCookie = new Cookie("username", username);
            userCookie.setMaxAge(60 * 60); // 1 hour
            response.addCookie(userCookie);

            // Redirect to dashboard
            response.sendRedirect("DashboardServlet");
        } else {
            // 4. Invalid credentials - redirect with error flag
            response.sendRedirect("login.html?error=invalid_credentials");
        }
    }

    private boolean isValidUser(String username, String password) {
        return VALID_USERS.containsKey(username)
                && VALID_USERS.get(username).equals(password);
    }
}