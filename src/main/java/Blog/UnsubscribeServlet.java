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
		        sendMail(user.getEmail());
		        resp.sendRedirect("/blog.jsp");
				return;
			}
		}
                
        log.info("User " + user.getNickname() + " attempted to usubscribe." + "\n"
        		+ "NOT FOUND IN SUBSCRIBER'S LIST"
        		+ "TimeStamp:" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));

        resp.sendRedirect("/blog.jsp");
    }
    
    public void sendMail(String userEmail) {
        Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
	      Message msg = new MimeMessage(session);
		  msg.setFrom(new InternetAddress("unsubscribe@blogmx-196500.appspotmail.com"));
		  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
		  msg.setSubject("Your BlogMX Subscription");
		  msg.setText("You have now unsubscribed from BlogMX. You won't be recieving any update emails anymore.");
		  Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
