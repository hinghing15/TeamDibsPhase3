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

public class RecordRide
{
	private Connector2 con;
	private BufferedReader in;
	private String userLogin;
	List<String> allowedVins = new ArrayList<String>();
	public RecordRide(Connector2 con, String userLogin)
	{
		in = new BufferedReader(new InputStreamReader(System.in));
		this.con = con;
		this.userLogin = userLogin;
	}
	
	//Prints out the available cars
	//if there is at least one available car it returns true
	//otherwise return false
	public String printAvailableCars() {
		String returnItem = "";
		String sql = "select * from UC U WHERE U.vin NOT IN \r\n" + 
   				"(select vin from Ride R WHERE (R.fromHour >= ? AND R.fromHour <= ? AND R.date = ?) OR (R.toHour >= ? AND R.toHour <= ? AND R.date = ?))\r\n" + 
   				"AND U.vin in (select vin from UC U2 WHERE U2.login in \r\n" + 
   				"(select login from Available A where A.pid in \r\n" + 
   				"(select pid from Period P WHERE P.fromHour <= ? AND P.toHour >= ?)));";
		try(PreparedStatement pstmt = con.con.prepareStatement(sql))
		{
			pstmt.setString(1,  String.valueOf(temp.start));
			pstmt.setString(2, String.valueOf(temp.end));
			pstmt.setString(3, temp.date);
			pstmt.setString(4, String.valueOf(temp.start));
			pstmt.setString(5, String.valueOf(temp.end));
			pstmt.setString(6, temp.date);
			pstmt.setString(7, String.valueOf(temp.start));
			pstmt.setString(8, String.valueOf(temp.end));
			ResultSet result = pstmt.executeQuery();
			if(result.isBeforeFirst())
			{
				System.out.println("Search results:");
				while(result.next())
				{
					System.out.println("vin: " + result.getString("vin"));
					System.out.println("\t" + "Category: " + result.getString("category")
											+ "    Year: " + result.getString("year"));
					allowedVins.add(result.getString("vin"));
				}
				System.out.println();
			}
			else
			{
				System.out.println("There are no cars available");
				return false;
			}
		} 
		catch(SQLException e) {
			System.out.println("Fail");
			return false;
		}
		return true;
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
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1,  String.valueOf(reservations.get(i).start));
				pstmt.setString(2, String.valueOf(reservations.get(i).end));
				int success = pstmt.executeUpdate();
				if(success == 1)
				{
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