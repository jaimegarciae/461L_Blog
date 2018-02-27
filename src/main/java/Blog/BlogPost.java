package Blog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class BlogPost implements Comparable<BlogPost> {
    @Id Long id;
    User user;
    String title;
    String content;
    Date date;
    
    private BlogPost() {}
    
    public BlogPost(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        date = new Date();
    }
    public User getUser() {
        return user;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public String getContent() {
        return content;
    }

    public String getTimestamp() {
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
    	String timeStamp = sdf.format(date); 
        return timeStamp;
    }

    @Override
    public int compareTo(BlogPost other) {
        if (date.before(other.date)) {
            return 1;
        } else if (date.after(other.date)) {
            return -1;
        }
        return 0;
     }
}