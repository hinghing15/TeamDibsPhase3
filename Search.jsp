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
}
%> 
<%
 connector.closeConnection();
%> 
</body>
