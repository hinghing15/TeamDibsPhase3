<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
String actAttribute = request.getParameter("act");
ReserveRideOptions ResCar = new ReserveRideOptions(connector, userLogin);
%>
<%
if( actAttribute == null ){

%>
	<h1>Reserve Car Options
	<form name = "ResCar" method="get" action="ReserveCar.jsp"> 
	<input type="Submit" name = "ReserveCarButton" value="Reserve Car" /> 
	<input type=hidden name="act" value="Reserve">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

	<form name = "ResCar" method="get" action="ReserveCar.jsp">
	<input type="Submit" name = "ConfirmReservation" value="Finish Reservation" /> 
	<input type=hidden name="act" value="ConfirmRes">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>
<%
}
else
{
	if(actAttribute.equals("Reserve"))
	{
		%>
		<h1>Available Cars</h1>
		boolean available = out.println(ResCar.printAvailableCars()); //this line probably doesn't work
		if(available) {
			<h1>Specify your Reservation</h1>
			<form name="SpefRes" method="get" action="ReserveCar.jsp">
				</b><BR>Vin: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
				Start Hour: <input type="text" name="start" pattern="[0-9]{1,20}" required="required"/><br />
				End Hour: <input type="text" name="end" pattern="[0-9]{1,20}" required="required"/><br />
				Date: <input type="text" name="date" pattern="[0-9]{1,20}" required="required"/><br />
				<input type="Submit" name = "subButton" value="Reserve"/>
			<input type=hidden name="act" value="Reserve">
			<input type=hidden name = "userLogin" value="<%=userLogin%>">
			</form>
			<%
			String vin = request.getParameter("vin");
			String start = request.getParameter("start");
			String end = request.getParameter("end");
			String date = request.getParameter("date");
			String button = request.getParameter("subButton");
			int startNum;
			if(button != null)
			{	
				boolean acSuccess = ResCar.SimilarDrivers(vin, model, category, yearS, userLogin);
				if(acSuccess)
				{
					out.print("Car added successfully!");
				}
				else	
				{
					out.print("Failed to add car");
				}
			}
		}
	}
	else if(actAttribute.equals("ConfirmRes"))
	{	
		%><h2>List of your cars:</h2><%
		out.println(driverOps.printCars());
		%>
		<h2>Input your car info</h2>
		<form name="update" method="get" action="DriverOptions.jsp">
			<p>Category:
			<select name="carCategory" >      
			<option>Economy</option>      
			<option>Comfort</option>      
			<option>Luxury</option>  
			</select>   
			</b><BR>Vin: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
			Make: <input type="text" name="make" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
			Model: <input type="text" name="model" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
			Year: <input type="text" name="yearS" pattern="[0-9]{4}" required="required"/><br />
			<input type="Submit" name = "subButton" value="Update Car"/>
		<input type=hidden name="act" value="ConfirmRes">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>
		<%
		String vin = request.getParameter("vin");
		String category = request.getParameter("carCategory");
		String make = request.getParameter("make");
		String model = request.getParameter("model");
		String yearS = request.getParameter("yearS");
		String button = request.getParameter("subButton");
		if(button != null)
		{
			boolean isOwner = driverOps.isUsersCar(vin);
			if(isOwner)
			{
				boolean acSuccess = driverOps.updateCar(make, model, category, yearS, vin);
				if(acSuccess)
				{
					out.print("Car updated successfully! Refresh to see results");
				}
				else	
				{
					out.print("Failed to update car");
				}
			}
			else	
			{
				out.print("This is not your car!");
			}
		}
	}
}
%> 
<%
 connector.closeConnection();
%> 
</body>
