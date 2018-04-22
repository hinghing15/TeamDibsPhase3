<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
String actAttribute = request.getParameter("act");
DriverOptions driverOps = new DriverOptions(connector, userLogin);
%>
<%
if( actAttribute == null ){

%>
	<h1>UUber Driver Options
	<form name = "driverOps" method="get" action="DriverOptions.jsp"> 
	<input type="Submit" name = "addCarButton" value="Add a new car" /> 
	<input type=hidden name="act" value="addCar">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

	<form name = "driverOps" method="get" action="DriverOptions.jsp">
	<input type="Submit" name = "updateCarButton" value="Update a car" /> 
	<input type=hidden name="act" value="updateCar">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

	<form name = "driverOps" method="get" action="DriverOptions.jsp">
	<input type="Submit" name = "availButton" value="Add your availablity" /> 
	<input type=hidden name="act" value="avail">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

<%
}
else
{
	if(actAttribute.equals("addCar"))
	{
		%>
		<h1>Input your car info</h1>
		<form name="carCat" method="get" action="DriverOptions.jsp">
			<p>Category:
			<select name="carCategory" >      
			<option>Economy</option>      
			<option>Comfort</option>      
			<option>Luxury</option>  
			</select>   
			</b><BR>Make: <input type="text" name="make" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
			Model: <input type="text" name="model" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
			Year: <input type="text" name="yearS" pattern="[0-9]{4}" required="required"/><br />
			<input type="Submit" name = "subButton" value="Add Car"/>
		<input type=hidden name="act" value="addCar">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>
		<%
		String category = request.getParameter("carCategory");
		String make = request.getParameter("make");
		String model = request.getParameter("model");
		String yearS = request.getParameter("yearS");
		String button = request.getParameter("subButton");
		if(button != null)
		{
		
			boolean acSuccess = driverOps.addNewCar(make, model, category, yearS, userLogin);
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
	else if(actAttribute.equals("updateCar"))
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
		<input type=hidden name="act" value="updateCar">
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
	else if(actAttribute.equals("avail"))
	{	
		%><h2>List of your availablity:</h2><%
		out.println(driverOps.printAv());
		%>
		<h2>Input availablity</h2>
		<form name="update" method="get" action="DriverOptions.jsp">
			</b><BR>Begin (0-23): <input type="text" name="from" pattern="[0-9]|[0-2][0-3]" required="required"/><br />
			End (1-24): <input type="text" name="to" pattern="[1-9]|[0-2][0-4]" required="required"/><br />
			<input type="Submit" name = "subButton" value="Add Availablity"/>
		<input type=hidden name="act" value="avail">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>
		<%
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String button = request.getParameter("subButton");
		if(button != null)
		{
			boolean acSuccess = driverOps.addAv(from, to);
			if(acSuccess)
			{
				out.print("Availablity added! Refresh to see results");
			}
		}
	}
}
%> 
<%
 connector.closeConnection();
%> 
</body>
