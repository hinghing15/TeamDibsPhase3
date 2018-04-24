<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
String actAttribute = request.getParameter("act");
RecordRide RecCar = new RecordRide(connector, userLogin);
%>
<%
while(!actAttribute.equals("ConfirmRec")){
%>
	<h1>Record Car Options
	<form name = "RecCar" method="get" action="RecordRide.jsp"> 
	<input type="Submit" name = "ReserveCarButton" value="Reserve Car" /> 
	<input type=hidden name="act" value="Reserve">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

	<form name = "RecCar" method="get" action="RecordRide.jsp">
	<input type="Submit" name = "ConfirmReservation" value="Finish Reservation" /> 
	<input type=hidden name="act" value="ConfirmRec">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>
<%
	if(actAttribute.equals("Record"))
	{
		%>
		<h1>Possible Cars</h1>
		<% 
		boolean available = RecCar.printAvailableCars();
		out.println(RecCar.printAvailableCars()); //this line probably doesn't work
		if(available) {
		%>
			<h1>Specify your Reservation</h1>
			<form name="RecRide" method="get" action="RecordRide.jsp">
				</b><BR>Vin: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
				Start Hour: <input type="text" name="start" pattern="^([0-1][0-9]|2[0-2]|[0-9])$" required="required"/><br />
				End Hour: <input type="text" name="end" pattern="^([0-1][0-9]|2[0-3]|[0-9])$" required="required"/><br />
				Date: <input type="text" name="date" pattern="^[0-9][0-9][0-9][0-9]-([0][1-9]|1[0-2])-([0-2][0-9]|3[0-1])$" required="required"/><br />
				<input type="Submit" name = "subButton" value="Reserve"/>
			<input type=hidden name="act" value="Record">
			<input type=hidden name = "userLogin" value="<%=userLogin%>">
			</form>
			<%
			String vin = request.getParameter("vin");
			String start = request.getParameter("start");
			String end = request.getParameter("end");
			String date = request.getParameter("date");
			String button = request.getParameter("subButton");
			int startNum;
			int endNum;
			if(button != null)
			{	
				startNum = Integer.parseInt(start);
				endNum = Integer.parseInt(end);
				if(startNum > endNum) {
					out.println("The ending hour has to be greater than the start. All");
					out.println("reserves are kept in the same day (0-23).");
				}
				else {
					out.println(RecCar.SimilarDrivers(vin, startNum, endNum, date));
				}
			}
		}
		else {
			out.println("No cars are available");
			actAttribute = "ConfirmRes";
		}
	}
}
if(actAttribute.equals("ConfirmRec"))
{	
	out.println(RecCar.DisplayRes());
	%> 
	<h1>Reserve Car Options
	<form name = "ResCar" method="get" action="ReserveCar.jsp"> 
	<input type="Submit" name = "ReserveCarButton" value="Yes" /> 
	<input type=hidden name="act" value="Yes">
	</form>

	<form name = "ResCar" method="get" action="ReserveCar.jsp">
	<input type="Submit" name = "ConfirmReservation" value="No" /> 
	<input type=hidden name="act" value="No">
	</form><%
	if(actAttribute.equals("Yes")) {
		if(RecCar.ConfirmRes()) {
			out.println("Successfully made reservation.");
		}
		else {
			out.println("Failed to make reservation, please try again.");
		}
	}
	else if(actAttribute.equals("No")) {
		//Do nothing
	}
	%>
	<h2>Goodbye.</h2><%
}
%> 
<%
 connector.closeConnection();
%> 
</body>
