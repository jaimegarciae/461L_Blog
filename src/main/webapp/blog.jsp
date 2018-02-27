<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="Blog.BlogPost" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en-US">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=1024">
    <title>blogMX: Home</title>
    <link href="css/blog.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
    <form action="/cronjob" method="post">
        	<input class="button" type="submit" value="SendMails">
    </form>
    <div class="container"> 
	  <header>
	    <h4 class="blogMX_title">blogMX</h4>
        <nav>
          <ul class="menu">
            <li><a class="active" href="blog.jsp">HOME</a></li>
            <li><a href="allPosts.jsp">ALL POSTS</a></li>       
	<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
	%>
	        <li><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">SIGN IN</a></li>
	      </ul>
        </nav>
      </header>
      
      <section class="cover" id="cover">
        <p class="loggeduser">You must be signed in to post!</p>
	<%
    } else {
    	pageContext.setAttribute("user", user);
    %>
    		<li><a href="createPost.jsp">WRITE A POST</a></li>
    		<li><a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">SIGN OUT</a></li>
    	  </ul>
        </nav>
      </header>
      
      <section class="cover" id="cover">
      	<p class="loggeduser">Hello, ${fn:escapeXml(user.nickname)}!</p>     
    <% 
	}    
    %>
        <h2 class="Me">Mé<span class="xi">xi</span><span class="co">co</span></h2>
        <p class="tagline">Place at the Center of the Moon</p>
      </section>
    <%
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
          <section class="post">
	        <table class="post_table">
	          <tr>
		        <td class="post_colLeft"><h1 class="post_title">${fn:escapeXml(postTitle)}</h1></td>
			    <td class="post_colRight"><h3 class="post_date">${fn:escapeXml(postTimestamp)}</h3></td>
			  </tr>
			  <tr>
		        <td class="post_colLeft"><h2 class="post_author">@${fn:escapeXml(postUser.nickname)}</h2></td>
			    <td class="post_colRight"><p class="post_content">${fn:escapeXml(postContent)}</p></td>
	          </tr>
	        </table>
          </section>
      <%
      i++;
      if(i == 5) break;
    } %>
      
      <section class="suscribe_banner">
        <p class="suscribe_invitation">Keep updated with the latest posts!</p>
        <form action="/subscribe" method="post">
        	<input class="button" type="submit" value="Subscribe">
        </form>
      </section>
	
      <div class="footer">
      	<p class="copyright">&copy;2018 - <strong>blogMX</strong></p>
        <form action="/unsubscribe" method="post">
        	<input class="unsubscribe" type="submit" value="Unsubscribe">
        </form>
	  </div>
    </div>
  </body>
</html>