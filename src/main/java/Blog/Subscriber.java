package Blog;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Subscriber{
    @Parent Key<Blog> blogName;
	@Id Long id;
    @Index User user;
    @Index String email;

    private Subscriber() {}
    
    public Subscriber(User user) {
        this.user = user;
        this.email = user.getEmail();
        this.blogName = Key.create(Blog.class, "default");
        this.id = (long) email.hashCode();
    }
    public User getUser() {
        return user;
    }
    
    public String getEmail() {
    	return email;
    }
}