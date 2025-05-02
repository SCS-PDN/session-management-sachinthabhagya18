import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EnrollServlet")
public class EnrollServlet extends HttpServlet {
    private static final List<Course> COURSE_LIST = Arrays.asList(
            new Course("101", "Mathematics", "Dr. Smith"),
            new Course("102", "Physics", "Dr. Brown"),
            new Course("103", "Chemistry", "Dr. Green")
    );
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 1. Get courseId from URL parameter
        String courseId = request.getParameter("courseId");

        // 2. Get current user's session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 3. Add course to enrolled list in session
        // Find the course object by courseId
        Course selectedCourse = null;
        for (Course c : COURSE_LIST) {
            if (c.getCourseId().equals(courseId)) {
                selectedCourse = c;
                break;
            }
        }

        if (selectedCourse != null) {
            // Get or create the enrolledCourses list in session
            List<Course> enrolledCourses = (List<Course>) session.getAttribute("enrolledCourses");
            if (enrolledCourses == null) {
                enrolledCourses = new ArrayList<>();
            }
            // Prevent duplicate enrollment
            boolean alreadyEnrolled = false;
            for (Course c : enrolledCourses) {
                if (c.getCourseId().equals(courseId)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
            if (!alreadyEnrolled) {
                enrolledCourses.add(selectedCourse);
                session.setAttribute("enrolledCourses", enrolledCourses);
                // 4. Redirect back to DashboardServlet with success message
                response.sendRedirect("DashboardServlet?message=Enrolled+successfully");
            } else {
                response.sendRedirect("DashboardServlet?message=Already+enrolled");
            }
        } else {
            response.sendRedirect("DashboardServlet?message=Course+not+found");
        }
    }
}