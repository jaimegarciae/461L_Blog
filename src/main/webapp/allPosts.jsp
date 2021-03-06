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
    <title>blogMX: All Posts</title>
    <link href="css/blog.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
    <div class="container"> 
	  <header>
	    <ul class="blogMX_title"><li><a href="blog.jsp">BlogMX</a></li></ul>
        <nav>
          <ul class="menu">
            <li><a class="active" href="allPosts.jsp">ALL POSTS</a></li>       
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
        <p class="loggeduser">You must be signed in to post and share posts!</p>
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
        <table class="cover_table"><tr>
		  <td><img class="cover_img" src="img/tulum.jpg"></td>
		  <td><img class="cover_img" src="img/tacos.jpg"></td>
		  <td class="cover_title">
			<h2 class="Me">Mé<span class="xi">xi</span><span class="co">co</span></h2>
    		<p class="tagline">Place at the Center of the Moon</p>
		  </td>
		  <td><img class="cover_img" src="img/oro-olimpico.jpg"></td>
		  <td><img class="cover_img" src="img/bellas-artes.jpg"></td>
	  	</tr></table>
	  	<p class="cover_description">A blog on your favorite memories from Mexico</p>
      </section>
    <%
	ObjectifyService.register(BlogPost.class);
	List<BlogPost> blogPosts = ObjectifyService.ofy().load().type(BlogPost.class).list();   
	Collections.sort(blogPosts);
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
	  <%
	  if (user != null) {
	  %>
	          <tr>
	          	<td class="share_cell" colspan="2">
		          <form action="/share" method="post">
		          	<input type="text" name="shareEmail" class="share_email" placeholder="Type email to share">
	        		<input type="hidden" name="shareTitle" value="${fn:escapeXml(postTitle)}">
	        		<input type="hidden" name="shareAuthor" value="${fn:escapeXml(postUser.nickname)}">
	        		<input type="hidden" name="shareContent" value="${fn:escapeXml(postContent)}">
	        		<input class="share_submit" type="submit" value="Share">
	        	  </form>
        		</td>
	          </tr>
	  <% } %>
	        </table>
          </section>
      <%
    } %>      
      <div>
      <table class="footer"><tr>
      	<td><p class="copyright">&copy;2018 - <strong>blogMX</strong></p></td>
      </tr></table>
	  </div>
    </div>
  </body>
</html>