<%@ page language="java" import="cs5530.*" %>
<html>
<body>
<% 
String userLogin = (String)request.getParameter("userLogin");
Connector2 connector = (Connector2)session.getAttribute("connector");
ReserveRideOptions ResCar = (ReserveRideOptions)session.getAttribute("class");
out.println(ResCar.DisplayRes());
if((String)request.getParameter("act") != "Finish") {
%>
<form name = "ResCar" method="get" action="ReserveCar3.jsp"> 
<input type="Submit" name = "ReserveCarButton" value="Yes" /> 
<input type=hidden name="act" value="Yes">
<input type="Submit" name = "ConfirmReservation" value="No" /> 
<input type=hidden name="act" value="No">
</form>
<%
String ReserveCarButton =  request.getParameter("ReserveCarButton");
String ConfirmReservation =  request.getParameter("ConfirmReservation");
if(ReserveCarButton != null) {
	if(ResCar.ConfirmRes()) {
		out.println("Successfully made reservations!");
	}
	else {
		out.println("Failed to make reservations please try again");
	}
}
else if(ConfirmReservation != null) {
	out.println("Goodbye");
}
}
else {
	out.println("Goodbye");
}
if(connector != null) {
	connector.closeConnection();
}
%> 
</body>
