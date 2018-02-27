package Blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;
import com.googlecode.objectify.ObjectifyService;

public class emailServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
		    Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("jaime.garcia.echanove@gmail.com"));
			
			ObjectifyService.register(Subscriber.class);
			List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
			
			for(Subscriber s : subscribers) {
			  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter(s.getEmail())));
			}
			
			msg.setSubject("Your daily BlogMX update");
			  
			ObjectifyService.register(BlogPost.class);
			List<BlogPost> blogPosts = ObjectifyService.ofy().load().type(BlogPost.class).list();
			Collections.sort(blogPosts); 
			filterPosts(blogPosts);
			
			fillEmail(msg, blogPosts);
			
			Transport.send(msg);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void filterPosts(List<BlogPost> blogPosts) {
		List<BlogPost> latestPosts = new ArrayList<BlogPost>();
		for(BlogPost bp : blogPosts) {
			if(lastDay(bp.getDate())) latestPosts.add(bp);
		}
		blogPosts = latestPosts;
	}
	
	public boolean lastDay(Date date) {
		long day = 24 * 60 * 60 * 1000;
		Date rightNow = new Date();
		return date.getTime() > (rightNow.getTime() - day);
	}
	
	public void fillEmail(Message msg, List<BlogPost> blogPosts) {
		StringBuilder emailContent = new StringBuilder();
		for(BlogPost bp : blogPosts) {
			String formatedPost = bp.getUserName() + "\n" + bp.getTitle() + "\n" + bp.getContent() + "\n";
			emailContent.append(formatedPost);
		}
		try {
			msg.setText(emailContent.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}