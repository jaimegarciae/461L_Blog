package Blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;
import com.googlecode.objectify.ObjectifyService;

public class emailServlet extends HttpServlet{
	
	static {
	  ObjectifyService.register(Subscriber.class);
	  ObjectifyService.register(BlogPost.class);
	}
	
    private static final Logger log = Logger.getLogger(emailServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

        try {
		    Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("dailyupdate@ee461l-blog-mx.appspotmail.com"));
						
			List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
			
			for(Subscriber s : subscribers) {
			  msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(s.getEmail()));
			}
			
			msg.setSubject("Your daily BlogMX update");
			  
			List<BlogPost> blogPosts = ObjectifyService.ofy().load().type(BlogPost.class).list();
			Collections.sort(blogPosts); 
			List<BlogPost> latestPosts  = filterPosts(blogPosts);
			
			if(!latestPosts.isEmpty()) {
			  fillEmail(msg, latestPosts);
			  Transport.send(msg);
			  log.info("email sent to subscribers");
			} else {
				log.info("No posts made in last 24 hours, no email sent");
			}
			
        }catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BlogPost> filterPosts(List<BlogPost> blogPosts) {
		List<BlogPost> latestPosts = new ArrayList<BlogPost>();
		for(BlogPost bp : blogPosts) {
			log.info("Post being checked: " + bp.getTitle() + " made on " + bp.getTimestamp());
			if(lastDay(bp.getDate())) {
				latestPosts.add(bp);
				log.info("Post added: " + bp.getTitle() + " made on " + bp.getTimestamp());
			}
		}
		return (ArrayList<BlogPost>) latestPosts;
	}
	
	public boolean lastDay(Date date) {
		long day = 24 * 60 * 60 * 1000;
		Date rightNow = new Date();
		log.info("Right now: " + rightNow.getTime() + "\n"
				+ "Post made:" + date.getTime() + "\n"
				+ "lastDay:" + (date.getTime() > (rightNow.getTime() - day)));
		return date.getTime() > (rightNow.getTime() - day);
	}
	
	public void fillEmail(Message msg, List<BlogPost> blogPosts){
		StringBuilder emailContent = new StringBuilder();
		for(BlogPost bp : blogPosts) {
			String formatedPost = "@" + bp.getUserName() + "\n" + bp.getTitle() + "\n" + bp.getContent() + "\n";
			emailContent.append(formatedPost);
			emailContent.append("-------------------------------------------" + "\n");
		}
		try {
			msg.setText(emailContent.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}