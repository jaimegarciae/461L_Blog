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
	  <h1>Hello, ${fn:escapeXml(user.nickname)}!</h1>
	  <p>Click <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">here</a> to sign out.</p>
	  <p>Click <a href="createPost.jsp">here</a> to create a post.</p>
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