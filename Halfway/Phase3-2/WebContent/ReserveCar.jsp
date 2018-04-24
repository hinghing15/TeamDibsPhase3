<%@ page language="java" import="cs5530.*" %>
<html>
<body>
<% 
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
ReserveRideOptions ResCar = new ReserveRideOptions(connector, userLogin);
session.setAttribute("class", ResCar);
session.setAttribute("connection", connector);
%>
<h1>Reserve Car Options
</b><BR>
<form name = "ResCar" method="get" action="ReserveCar2.jsp"> 
<input type="Submit" name = "ReserveCarButton" value="Reserve Car" /> 
<input type=hidden name="act" value="Reserve">
</form>

<form name = "ResCar" method="get" action="ReserveCar3.jsp">
<input type="Submit" name = "ConfirmRes" value="Confirm" /> 
<input type=hidden name="act" value="Finish">
</form> 
</body>
