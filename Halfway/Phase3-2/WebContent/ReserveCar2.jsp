<%@ page language="java" import="cs5530.*" %>
<html>
<body>
<% 
String userLogin = (String)request.getParameter("userLogin");
String anotherOne = (String)request.getParameter("AnotherOne");
ReserveRideOptions ResCar = (ReserveRideOptions)session.getAttribute("class");

%>
<h1>Specify your Reservation</h1>
<form name="SpefRes" method="get" action="ReserveCar2.jsp">
</b><BR>Vin: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
	Start Hour: <input type="number" name="start" min=0 max=22$" required="required"/><br />
	End Hour: <input type="number" name="end" min=1 max=23$" required="required"/><br />
	Year: <input type="number" name="year" min=2000$" required="required"/><br />
	Month: <input type="number" name="month" min=1 max=12$" required="required"/><br />
	Day: <input type="number" name="day" min=1 max=31$" required="required"/><br />
<input type=hidden name="act" value="Reserve">
<input type=hidden name = "userLogin" value="<%=userLogin%>">
<input type="Submit" name = "AnotherOne" value="Add" /> 
</form>

<%

String vin = request.getParameter("vin");
String start = request.getParameter("start");
String end = request.getParameter("end");
String month = "";
String day = "";
if(request.getParameter("month") != null) {
	if(Integer.parseInt(request.getParameter("month")) < 10) {
		month = "0" + String.valueOf(request.getParameter("month"));
	}
	else {
		month = String.valueOf(request.getParameter("month"));
	}
}
if(request.getParameter("day") != null) {
	if(Integer.parseInt(request.getParameter("day")) < 10) {
		day = "0" + String.valueOf(request.getParameter("day"));
	}
	else {
		day = String.valueOf(request.getParameter("day"));
	}
}
String date = String.valueOf(request.getParameter("year")) + "-" + month + "-" + request.getParameter("day");
String OtherButton = request.getParameter("AnotherOne");
String FinButton = request.getParameter("ConfirmRes");
int startNum;
int endNum;
if(OtherButton != null)
{	
	startNum = Integer.parseInt(start);
	endNum = Integer.parseInt(end);
	%>
	<h2>Other Drivers picked these Vins</h2>
	<%
	out.println((String)ResCar.SimilarDrivers(vin, startNum, endNum, date));
}
if(ResCar.length() > 0) {
%>
<form name = "SpefRes" method="get" action="ReserveCar3.jsp">
<input type="Submit" name = "ConfirmRes" value="Finish" /> 
</form>
<% 
}
%> 
</body>
