<%@ page language="java" import="cs5530.*" %>
<html>
<body>

<%
Connector2 connector = new Connector2();
String userLogin = (String)request.getParameter("userLogin");
String actAttribute = request.getParameter("act");
SearchOptions searchOps = new SearchOptions(connector, userLogin);
miscHelpers mH = new miscHelpers(connector);

String searchChoice = request.getParameter("searchChoice");
%>
<%
if( actAttribute == null ){

%>
	<h1>Search Options</h1>

	<%-- UC Browsing --%>
	<form name = "UCSearch" method="get" action="Search.jsp"> 
	<input type="Submit" name = "searchCarButton" value="Search for cars" /> 
	<input type=hidden name="act" value="searchCar">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>

	<%-- Useful Feedbacks --%>
	<form name = "UsefulFBSearch" method="get" action="Search.jsp">
	<input type="Submit" name = "usefulFBButton" value="Search for useful feedbacks" /> 
	<input type=hidden name="act" value="searchUsefulFB">
	<input type=hidden name = "userLogin" value="<%=userLogin%>">
	</form>


<%
}
else
{
	if(actAttribute.equals("searchCar"))
	{
		%>
		<h1>UUber Car Search</h1>
		<h3>Leave blank if you do not want to apply search option</h3>
		<form name="carSearch" method="get" action="Search.jsp">
			<p>Category:
			<select name="carCategory" >
			<option></option>       
			<option>Economy</option>      
			<option>Comfort</option>      
			<option>Luxury</option>  
			</select>   
			</b><BR>Model: <input type="text" name="model" pattern="[A-Za-z0-9]{1,20}"/><br />
			City: <input type="text" name="city" pattern="[A-Za-z]{1,20}"/><br />
			<p>Search by:
			<select name="avgSortChoice" >      
			<option>Average score of all feedbacks</option>      
			<option>Average score of trusted user feedbacks</option>      
			</select>   
			<input type="Submit" name = "subButton" value="Search for car(s)"/>
		<input type=hidden name="act" value="searchCar">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		</form>
		<%
		String category = request.getParameter("carCategory");
		String model = request.getParameter("model");
		String city = request.getParameter("city");
		String avgSortChoice = request.getParameter("avgSortChoice");
		String conjSortC = request.getParameter("conjSortC");
		String button = request.getParameter("subButton");
		if(button != null)
		{
			%><h2>Results: </h2><%
			int sortC;
			if(avgSortChoice.equals("Average score of all feedbacks"))
				sortC = 1;
			else
				sortC = 2;
			
			if(model == null)
				model = "";
			if(city == null)
				city = "";
			if(userLogin.equals("null") && sortC == 2)
			{
				out.println("Please login first in order to see results for average score of cars from users you trust.");
			}
			else
				out.println(searchOps.SearchUCOps2(category, city, model, sortC));
		}
	}

	
	else if(actAttribute.equals("searchUsefulFB"))
	{	
		%><h2>List of UUber Drivers:</h2><%
		out.println(mH.printUD());
		%>
		<h2>Input the login of the driver you would like to see the feedback for</h2>
		<form name="update" method="get" action="Search.jsp"> 
			</b><BR>Login: <input type="text" name="login" pattern="[A-Za-z0-9]{1,20}" required="required"/><br />
			<BR>Number of feedbacks you would like to see: <input type="text" name="m" pattern="[0-9]{1,20}" required="required"/><br />
		<input type=hidden name="act" value="searchUsefulFB">
		<input type=hidden name = "userLogin" value="<%=userLogin%>">
		<input type="Submit" name = "subButton" value="Submit"/>
		</form>
		<%
		String login = request.getParameter("login");
		String mString = request.getParameter("m");
		String button = request.getParameter("subButton");
		if(button != null)
		{
			%><h2>Results: </h2><%
			int m = Integer.parseInt(mString);
			out.println(searchOps.viewUsefulFeedbacks(login, m));
		}
		
	}
}
%> 
<%
 connector.closeConnection();
%> 
</body>
