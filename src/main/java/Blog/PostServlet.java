package Blog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class PostServlet extends HttpServlet {
	static {
	    ObjectifyService.register(BlogPost.class);
	}
	
    private static final Logger log = Logger.getLogger(PostServlet.class.getName());
 
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
 
        String title = req.getParameter("postTitle");
        String content = req.getParameter("postContent");
        
        BlogPost blogPost = new BlogPost(user, title, content);       
        
        ofy().save().entity(blogPost).now();

        if(title == null) {
        	title = "(No title)";
        }
        if (content == null) {
            content = "(No post)";
        }
        
        log.info("Post by user: " + user.getNickname() + "\n"
        		+ "Title: " + title + "\n"
        		+ "Content: " + content + "\n"
        		+ "TimeStamp:" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));

        resp.sendRedirect("/blog.jsp");
    }
}
