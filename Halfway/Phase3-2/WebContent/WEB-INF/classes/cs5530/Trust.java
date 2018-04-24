package cs5530;
/*Driver Registration
Add a new UUber Car
Update a UUber Car*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Trust {
	private Connector2 con;
	String userLogin;
	String sql;
	public Trust(String login, Connector2 conn) {
		con = conn;
		userLogin = login;
	}

	public String GetFeedBack(String login) {
		String returnedItem = "";
		sql = "SELECT * FROM Feedback WHERE login = ?";
	 	try(PreparedStatement pstmt = con.con.prepareStatement(sql))
		 {
			 pstmt.setString(1,  login);
			 ResultSet result = pstmt.executeQuery();
			 if(!result.isBeforeFirst())
			 {
				 return "User has not made any feedback";
			 }
			 else {
				 System.out.println("Selected User Feedback History: ");
				 while(result.next())
					{
						returnedItem += "<BR><b>Text: </b>" + result.getString("text") +
						 "<b>Score: </b> " + result.getString("score")
						+ "<b>Vin: </b>" + result.getString("vin")
						+ "           <b>Date: </b>" + result.getString("fbdate") + "<BR>";
					}
			 }
		}  
		 catch(SQLException Se) {}
		 return returnedItem;
	}

	public boolean RateOtherUsers(String trust, String ratedLogin) {
		String check = "Select * from Trust t WHERE t.login1 = ? " +
		"AND t.login2 = ?;";
		try(PreparedStatement pCheck = con.con.prepareStatement(check))
		{
			pCheck.setString(1, ratedLogin);
			pCheck.setString(2,  userLogin);
			ResultSet result = pCheck.executeQuery();
			if(result.isBeforeFirst())
			{
				System.out.println("Search results:");
				while(result.next())
				{
					sql = "UPDATE Trust t SET t.isTrusted = ? WHERE t.login1 = ? AND t.login2 = ?";
					try(PreparedStatement pstmt = con.con.prepareStatement(sql))
					{
						pstmt.setString(1, trust);
						pstmt.setString(2,  ratedLogin);
						pstmt.setString(3, userLogin);
						int success = pstmt.executeUpdate();
						if(success == 1)
						{
							System.out.println("You have reviewed " + ratedLogin);
						}
					} 
					catch(SQLException e) 
					{
						return false;
					}
				}
				System.out.println();
			}
			else
			{
				sql = "INSERT INTO Trust(login1, login2, isTrusted) VALUES(?,?,?)";
				try(PreparedStatement pstmt = con.con.prepareStatement(sql))
				{
					pstmt.setString(1, ratedLogin);
					pstmt.setString(2,  userLogin);
					pstmt.setString(3, trust);
					int success = pstmt.executeUpdate();
					if(success == 1)
					{
						System.out.println("You have reviewed " + ratedLogin);
					}
				} 
				catch(SQLException e) 
				{
					return false;
				}
			}
		} 
		catch(SQLException e) 
		{
			return false;
		}
		return true;
	}
}
	//System.out.println("Type in the login of the User you'd like to look at: ");
	//miscH.printUU();
	//String answer = null;
	//while(answer == null) {
	//	try {
	//		answer = in.readLine();
	//		if(answer == null) {
	//			answer = " ";
	//		}
	//		if(!miscH.validUser(answer)) {
	//			System.out.println("Invalid User, try again: ");
	//			answer = null;
	//		}
	//	}
	//	catch(Exception e) {
	//	}
	//}