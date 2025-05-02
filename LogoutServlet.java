import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 1. Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2. Remove username cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0); // Delete the cookie
                    cookie.setPath("/"); // Important: ensure cookie is deleted for the whole app
                    response.addCookie(cookie);
                }
            }
        }

        // 3. Redirect to login.html
        response.sendRedirect("login.html");
    }
}