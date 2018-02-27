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
      <h1>Post to BlogMX</h1>
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
 	  <p>Click <a href="blog.jsp">here</a> to go back to the home page!</p>
  </body>
</html>