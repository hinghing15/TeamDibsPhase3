<%@ page language="java" import="cs5530.*" %>
<html>
<head>
<form name="reg" method="get">
Login: <input type="text" name="login" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
Password: <input type="text" name="password" pattern="[A-Za-z0-9]{1,20}" required="required"/><br/>
Name: <input type="text" name="name" pattern="[A-Za-z0-9]{1,20}" required="required"/><br/>
City: <input type="text" name="city" pattern="[A-Za-z0-9]{1,20}" required="required"/><br/>
Phone Number: <input type="text" name="phone" pattern="[A-Za-z0-9]{1,20}" required="required"/><br/>
<input type="Submit" name = "subButton" value="Register" />
</form>
</head>
<body>

<%
	String button = request.getParameter("subButton");
	Connector2 connector = new Connector2();
	if(button != null)
	{
		phase2 ph = new phase2();
		String loginValue = request.getParameter("login");
		String passwordValue = request.getParameter("password");
		String nameValue = request.getParameter("name");
		String cityValue = request.getParameter("city");
		String phoneValue = request.getParameter("phone");
		boolean userCreatedSuccess = ph.createUser(connector, loginValue, passwordValue, nameValue, cityValue, phoneValue);
		if(userCreatedSuccess == true)
		{
			%>
			<p><b>Success creating user. You have been logged in</b><BR><BR>
			<%  
			response.sendRedirect("UserOptions.jsp?userLogin=" + loginValue +"&userName=" + nameValue);
		}
		else
		{
			%>
			<p><b>Account creation unsuccessful</b><BR><BR>
			<% 
		}
	}
%>  

<%
 connector.closeConnection();
%>

</body>
