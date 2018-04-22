<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
	Connector2 connector = new Connector2();
	String userLogin = (String)request.getParameter("userLogin");
	String userName = (String)request.getParameter("userName");
	UserOptions userOps = new UserOptions(connector, userLogin, userName);
	miscHelpers mH = new miscHelpers(connector);
	String actAttribute = request.getParameter("act");
	if(actAttribute == null){
		%>
		<p>Welcome, <% out.print(userName); %>
		<form name = "userOps" method="post">
		</b><input type="Submit" name = "regButton" value="Register as a Driver" />

		<%--Driver Options--%>
		<form name = "driverOps" method="get" action="DriverOptions.jsp"> 
		</b><BR><BR><input type="Submit" name = "driverOpsButton" value="UUber Driver Options" />
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>

		</b><BR><BR><input type="Submit" name = "reserveButton" value="Reserve a ride" />
		</b><BR><BR><input type="Submit" name = "recordButton" value="Record a ride" />

		<%--Favorite Cars--%>
		<form name = "favCars" method="get" action="UserOptions.jsp"> 
		</b><BR><BR><input type="Submit" name = "favButton" value="Favorite a car" />
		<input type=hidden name="act" value="favCar">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>

		</b><BR><BR><input type="Submit" name = "reviewButton" value="Review a car" />
		
		<form name = "usefulnessR" method="get" action="UsefulnessRating.jsp"> 
		</b><BR><BR><input type="Submit" name = "viewButton" value="View cars and reviews" />
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>
		
		
		</b><BR><BR><input type="Submit" name = "reviewUserButton" value="Review a user" />

		<%--Search--%>
		<form name = "search" method="get" action="Search.jsp"> 
		</b><BR><BR><input type="Submit" name = "searchButton" value="Search" />
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>

		<%
		String regButton = request.getParameter("regButton");
		String driverOpsButton = request.getParameter("driverOpsButton"); // DONE
		String reserveButton = request.getParameter("reserveButton");
		String recordButton = request.getParameter("recordButton");
		//String favButton = request.getParameter("favButton");	//DONE
		String reviewButton = request.getParameter("reviewButton");
		String viewButton = request.getParameter("viewButton"); //usefulness rating
		String reviewUserButton = request.getParameter("reviewUserButton");
		String searchButton = request.getParameter("searchButton");

		
		// buttons that redirect to a different jsp file
		if(driverOpsButton != null)
		{
			boolean isDriver = userOps.isDriver(userLogin);
			if(isDriver)
			{	
				response.sendRedirect("DriverOptions.jsp?userLogin=" + userLogin +"&userName=" + userName);
			}
			else
			{
				%>
				<p><b>You are not a driver</b><BR><BR>
				<% 
			}
		}
		if(searchButton != null)
		{
			response.sendRedirect("Search.jsp?userLogin=" + userLogin +"&userName=" + userName);
		}
		if(viewButton != null)
		{
			response.sendRedirect("UsefulnessRating.jsp?userLogin=" + userLogin);
		}
	}
	else
	{
		//buttons without a new jsp. redirects to same jsp
		if(actAttribute.equals("favCar"))
		{
			%>
			<h2>List of all UUber cars:</h2>
			<%out.println(mH.printUC());%>
			<h2>List of your favorite cars:</h2><%
			out.println(userOps.printFavorites());
			%><h2>Select car you would like to favorite:</h2>

			<form name="update" method="get" action="UserOptions.jsp">
			</b><BR>Vin: <input type="text" name="vin" pattern="[0-9]{1,20}" required="required"/><br />
			<input type="Submit" name = "subButton" value="Favorite Car"/>
			<input type=hidden name="act" value="favCar">
			<input type=hidden name = "userLogin" value="<%=userLogin%>">
			</form>

			<%
			String vin = request.getParameter("vin");
			String button = request.getParameter("subButton");
			if(button != null)
			{
				boolean favorited = userOps.addNewFavorite(vin);
				if(favorited)
				{
					out.println("Success favoriting car! Refresh to see.");
				}
				else
				{
					out.println("Failure favoriting car!");
				}
			}
		}
	}
%>  
<%
 connector.closeConnection();
%>
</body>
