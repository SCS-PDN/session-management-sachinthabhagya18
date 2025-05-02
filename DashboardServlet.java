import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    // 2. Create a list of courses (hardcoded)
    private static final List<Course> COURSE_LIST = Arrays.asList(
            new Course("101", "Mathematics", "Dr. Smith"),
            new Course("102", "Physics", "Dr. Brown"),
            new Course("103", "Chemistry", "Dr. Green")
    );
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 1. Check if user is logged in (session)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 3. Store courses in request attribute
        request.setAttribute("courses", COURSE_LIST);

        // Also pass enrolled courses if any (from session)
        List<Course> enrolledCourses = (List<Course>) session.getAttribute("enrolledCourses");
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        request.setAttribute("enrolledCourses", enrolledCourses);

        // Pass any message (e.g., after enrollment)
        String message = request.getParameter("message");
        if (message != null) {
            request.setAttribute("message", message);
        }

        // 4. Forward to dashboard.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
        dispatcher.forward(request, response);
    }
}