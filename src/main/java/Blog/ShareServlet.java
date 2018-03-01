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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ShareServlet extends HttpServlet{
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
		String sendTo = req.getParameter("shareEmail");
		String author = req.getParameter("shareAuthor");
		String title = req.getParameter("shareTitle");
		String content = req.getParameter("shareContent");
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
        try {
		    Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("shareposts@blogmx-196500.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(sendTo));
			msg.setSubject(user.getNickname() + " shared a BlogMX post with you!");
			  
			StringBuilder emailContent = new StringBuilder();
			emailContent.append("Hey there! \n" + user.getNickname() + " has shared the following post with you:\n");
			emailContent.append("-------------------------------------------" + "\n");
			String formatedPost = "@" + author + "\n" + title + "\n" + content + "\n";
		    emailContent.append(formatedPost);
			emailContent.append("-------------------------------------------" + "\n");
			emailContent.append("If you would like to see more posts like this be sure to visit BlogMX at https://ee461l-blogmx.appspot.com/ \n");
			emailContent.append("\n BlogMX Team");
			msg.setText(emailContent.toString());
			Transport.send(msg);
        }catch (Exception e) {
			e.printStackTrace();
		}
        resp.sendRedirect("/blog.jsp");
	}
}