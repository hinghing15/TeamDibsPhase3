<%@ page language="java" import="cs5530.*" %>
<html>

<body>

<%

	Connector2 connector = new Connector2();
	phase2 ph = new phase2();
	String loginValue = request.getParameter("login");
	String passwordValue = request.getParameter("password");
	boolean loginSuccess = ph.loginUser(connector, loginValue, passwordValue);
	if(loginSuccess == true)
	{
		%>
		<p><b>Success logging in!</b><BR><BR>
		<%  
		response.sendRedirect("UserOptions.jsp?userLogin=" + loginValue +"&userName=" + ph.userName);

	}
	else
	{
		%>
		<p><b>Log in unsuccessful</b><BR><BR>
		<% 
	}
	
%>  

<%
 connector.closeConnection();
%>

</body>
