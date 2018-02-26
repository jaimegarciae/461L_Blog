package Blog;
import java.io.IOException;

import javax.servlet.http.*;

public class BlogServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
      
    response.setContentType("text/plain");

    response.getWriter().println("HelloWorld!");

  }
}