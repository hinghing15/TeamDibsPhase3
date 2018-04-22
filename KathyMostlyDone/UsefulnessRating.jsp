<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
String userName = (String)request.getParameter("userName");
UserOptions userOps = new UserOptions(connector, userLogin, userName);
miscHelpers mH = new miscHelpers(connector);
%>
<%
%>
	<h1>List of UUber cars: </h1>
	out.println(mH.printUC());
	<h2>Input the login of the driver you would like to see the feedback for</h2>
	<form name="usefulR" method="get" action="UsefulnessRating.jsp"> 
		<BR>Vin of the car that you would like to see the feedback of: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
	<input type="Submit" name = "subButton" value="Submit"/>
	</form>
	<%
	String vin = request.getParameter("vin");
	String button = request.getParameter("subButton");
	if(button != null)
	{
	
	}

 connector.closeConnection();
 %> 
</body>
