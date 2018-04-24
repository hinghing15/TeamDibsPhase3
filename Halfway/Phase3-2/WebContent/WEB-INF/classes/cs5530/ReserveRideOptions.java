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

public class ReserveRideOptions
{
	private Connector2 con;
	private BufferedReader in;
	private String userLogin;
	List<ReserveObj> reservations = new ArrayList<ReserveObj>();
	public ReserveRideOptions(Connector2 con, String userLogin)
	{
		in = new BufferedReader(new InputStreamReader(System.in));
		this.con = con;
		this.userLogin = userLogin;
	}
	
	//Prints out the available cars
	//if there is at least one available car it returns true
	//otherwise return false
	public String printAvailableCars() {
		String display = "";
		String sql = "SELECT * FROM UC";
		try(PreparedStatement pstmt = con.con.prepareStatement(sql))
		{
			ResultSet result = pstmt.executeQuery();
			if(result.isBeforeFirst())
			{
				System.out.println("Search results:");
				while(result.next())
				{
					display += "<BR><b>Vin: </b>" + result.getString("vin") 
					+ "    <b>Category: </b>" + result.getString("category")
					+ "    <b>Year: </b>" + result.getString("year") + "<BR>";
				}
				return display;
			}
			else
			{
				System.out.println("There are no cars to be reserved");
				return display;
			}
		} 
		catch(SQLException e) {
			return display;
		}
	}

	//Given a vin, date, time, we return cars that similar uses picked, also adds
	//details to reservation list
	public String SimilarDrivers(String vin, int start, int end, String date) {
		String display = "";
		ReserveObj temp = new ReserveObj();
		temp.start = start;
		temp.end = end;
		temp.date = date;
		temp.vin = vin;
       	int startInt = temp.start;
		int endInt = temp.end;
		temp.cost = endInt - startInt;
		reservations.add(temp);
		String sqlDup = "SELECT COUNT(*) as TotalRides, R.vin as Owner\r\n" + 
				"FROM Reserve R, (select distinct login from Reserve R where\r\n" + 
				"R.vin = ?) as N WHERE R.login = N.login\r\n" + 
				"GROUP BY R.vin ORDER BY TotalRides DESC;";
		try(PreparedStatement similar = con.con.prepareStatement(sqlDup))
		{
			similar.setString(1,  String.valueOf(temp.vin));
			ResultSet result = similar.executeQuery();
			if(result.isBeforeFirst()) {
				System.out.println("Similar Users picked these cars: ");
				while(result.next())
				{
					if(!result.getString("Owner").equals(temp.vin)) {
						display += "<BR>Vin: " + result.getString("Owner") + "<BR>";
					}
				}
			}
		}
		catch(SQLException e) 
		{
			return display;
		}
		return display;
	}

	public int length() {
		return reservations.size();
	}
	
	//Saves Reservation into database
	public String DisplayRes() {
		String display = "";
		if(reservations.size() > 0) {
			int totalCost = 0;
			String confirmation = "a";
			System.out.println("Confirm these reservations:");
			for(int i = 0; i < reservations.size(); i++) {	
				display += 				"<BR>VIN: " + reservations.get(i).vin
				+ "<BR><b>Start Hour: </b>" + String.valueOf(reservations.get(i).start) 
				+ "    <b>End Hour: </b>" + String.valueOf(reservations.get(i).end)
				+ "    <b>Date: </b>" + reservations.get(i).date
				+ "    <b>Cost: </b>" + String.valueOf(reservations.get(i).cost) + "<BR>";
				totalCost = totalCost + reservations.get(i).cost;
			}
			display += "<BR>Total Cost: " + String.valueOf(totalCost) + "<BR>";
			
			display += "<BR>yes/no?<BR>";
			return display;
		}
		return display;
	}

	public boolean ConfirmRes() {
		for(int i = 0; i < reservations.size(); i++) {
			String sql = "INSERT INTO Period(fromHour, toHour) VALUES(?,?)";
			System.out.println("Here\n");
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				System.out.println("try\n");
				pstmt.setString(1,  String.valueOf(reservations.get(i).start));
				pstmt.setString(2, String.valueOf(reservations.get(i).end));
				int success = pstmt.executeUpdate();
				if(success == 1)
				{
					System.out.println("Success entry\n");
					ResultSet generatedKeys = pstmt.getGeneratedKeys();
					generatedKeys.next();
					int pid = generatedKeys.getInt(1);
					sql = "INSERT INTO Reserve(login, vin, pid, cost, date) VALUES(?,?,?,?,?)";
					try(PreparedStatement pstmt2 = con.con.prepareStatement(sql))
					{
						pstmt2.setString(1,  userLogin);
						pstmt2.setString(2, reservations.get(i).vin);
						pstmt2.setString(3,  String.valueOf(pid));
						pstmt2.setString(4, String.valueOf(reservations.get(i).cost));
						pstmt2.setString(5,  reservations.get(i).date);
						pstmt2.executeUpdate();
					} 
					catch(SQLException e) 
					{
						System.out.println("Failed to insert into Reserve\n");
						sql = "DELETE FROM Period" + "WHERE pid = " + String.valueOf(pid);
						try(PreparedStatement pstmt3 = con.con.prepareStatement(sql))
						{
							pstmt3.executeUpdate();
							return false;
						} 
						catch(SQLException eR) 
						{
							System.out.println("Failed to remove from Period");
							return false;
						}
					}
				}
		
			} 
			catch(SQLException e) 
			{
				System.out.println("Failed to insert into Period\n");
				return false;
			}
		}
		return true;
		
	}
}