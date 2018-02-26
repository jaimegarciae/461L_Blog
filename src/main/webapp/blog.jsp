<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
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
	  <table>
	    <tr>
	      <td>
	        <form action="/sign" method="post">
	          <textarea name="postTitle" rows="1" cols="60"  placeholder="Your post's title"></textarea>
 	  	    </form>
 	  	  </td>
	    </tr>
	    <tr>
	      <td>
	        <form action="/sign" method="post">
	    	  <textarea name="postContent" rows="3" cols="60" placeholder="Your post's content"></textarea>
 	  	    </form>
 	  	  </td>
	    </tr>
	    <tr>
	      <td><input type="submit" value="Post" ></td>
	    </tr>
	  </table>
	<%
    } else {
	%>
	  <p><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
	  to post to the blog.</p>
	<% } %>
  </body>
</html>