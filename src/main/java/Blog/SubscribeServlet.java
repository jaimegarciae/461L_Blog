package Blog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubscribeServlet extends HttpServlet {
	static {
	    ObjectifyService.register(Subscriber.class);
	}
	
    private static final Logger log = Logger.getLogger(SubscribeServlet.class.getName());
 
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
 
        Subscriber s = new Subscriber(user);       
        
        ofy().save().entity(s).now();
        
        Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
	      Message msg = new MimeMessage(session);
		  msg.setFrom(new InternetAddress("subscribe@ee461l-blog-mx.appspotmail.com"));
		  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s.getEmail()));
		  msg.setSubject("Your BlogMX Subscription");
		  msg.setText("Thank you for subscribing to BlogMX! You will recieve an update email every day with the latest posts.");
		  Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        
        log.info("User " + user.getNickname() + " subscribed." + "\n"
        		+ "TimeStamp:" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));

        resp.sendRedirect("/blog.jsp");
    }
}
