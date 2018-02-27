package Blog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class UnsubscribeServlet extends HttpServlet {
	static {
	    ObjectifyService.register(Subscriber.class);
	}
	
    private static final Logger log = Logger.getLogger(UnsubscribeServlet.class.getName());
 
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
 
		List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();

		for(Subscriber s : subscribers) {
			if(s.getEmail().equals(user.getEmail())) {
				ObjectifyService.ofy().delete().entity(s).now(); 
		        log.info("User " + user.getNickname() + " unsubscribed." + "\n"
		        		+ "TimeStamp:" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));
		        resp.sendRedirect("/blog.jsp");
				return;
			}
		}
                
        log.info("User " + user.getNickname() + " attempted to usubscribe." + "\n"
        		+ "NOT FOUND IN SUBSCRIBER'S LIST"
        		+ "TimeStamp:" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));

        resp.sendRedirect("/blog.jsp");
    }
}
