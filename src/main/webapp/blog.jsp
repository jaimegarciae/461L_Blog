<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="Blog.BlogPost" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <body>
	<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
      pageContext.setAttribute("user", user);
	%>
	  <p>Hello, ${fn:escapeXml(user.nickname)}! (You can
	  <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>

	  <form action="/post" method="post">
	  	<table>
	  	  <tr>
	    	<td><textarea name="postTitle" rows="1" cols="60"  placeholder="Your post's title"></textarea></td>
	      </tr>
	      <tr>
			<td><textarea name="postContent" rows="3" cols="60" placeholder="Your post's content"></textarea></td>
		  </tr>
		  <tr>
 	  		<td><input type="submit" value="Post" ><input type="reset" value="Clear"></td>
 	  	  </tr>
 	  	</table>
 	  </form>
	<%
    } else {
	%>
	  <p><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
	  to post to the blog.</p>
	<% 
	}
	ObjectifyService.register(BlogPost.class);
	List<BlogPost> blogPosts = ObjectifyService.ofy().load().type(BlogPost.class).list();   
	Collections.sort(blogPosts); 
	int i = 0;
    for(BlogPost bp : blogPosts) {
      pageContext.setAttribute("postTitle", bp.getTitle());
      pageContext.setAttribute("postContent", bp.getContent());
	  pageContext.setAttribute("postUser", bp.getUser());
	  pageContext.setAttribute("postTimestamp", bp.getTimestamp());
      %>
        <p><b>${fn:escapeXml(postUser.nickname)}</b> ${fn:escapeXml(postTimestamp)}</p>
        <blockquote><b>${fn:escapeXml(postTitle)}</b></blockquote>
        <blockquote>${fn:escapeXml(postContent)}</blockquote>
      <%
      i++;
      if(i == 5) break;
    } %>
    <p>Click <a href="allPosts.jsp">here</a> to view all posts!</p>
  </body>
</html>