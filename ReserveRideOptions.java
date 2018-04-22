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
	public boolean printAvailableCars() {
		String sql = "SELECT * FROM UC";
		try(PreparedStatement pstmt = con.con.prepareStatement(sql))
		{
			ResultSet result = pstmt.executeQuery();
			if(result.isBeforeFirst())
			{
				System.out.println("Search results:");
				while(result.next())
				{
					System.out.println("Vin: " + result.getString("vin"));
					System.out.println("\t" + "Category: " + result.getString("category") 
											+ "    Year: " + result.getString("year")
				}
				System.out.println();
				return true;
			}
			else
			{
				System.out.println("There are no cars to be reserved");
				return false;
			}
		} 
	}

	//Given a vin, date, time, we return cars that similar uses picked, also adds
	//details to reservation list
	public boolean SimilarDrivers(String vin, int start, int end, String date) {
		ReserveObj temp;
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
						System.out.println("\t" + "Vin: " + result.getString("Owner"));
					}
				}
				System.out.println();
			}
		}
		catch(SQLException e) 
		{
		}
	}

	//Saves Reservation into database
	public void DisplayRes() {
		if(reservations.size() > 0) {
			int totalCost = 0;
			String confirmation = "a";
			System.out.println("Confirm these reservations:");
			for(int i = 0; i < reservations.size(); i++) {			
				System.out.println("VIN:" + reservations.get(i).vin);
				System.out.println("Start Hour:" + String.valueOf(reservations.get(i).start));
				System.out.println("End Hour:" + String.valueOf(reservations.get(i).end));
				System.out.println("Date:" + reservations.get(i).date);
				System.out.println("Cost:" + String.valueOf(reservations.get(i).cost));
				totalCost = totalCost + reservations.get(i).cost;
			}
			System.out.println("Total Cost: " + String.valueOf(totalCost));
			
			System.out.println("yes/no?");
		}
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

//----------------------------------------OLD CODE----------------------------------------//
	public void selectReserveOp()
	{
		int c = 0;
		List<ReserveObj> reservations = new ArrayList<ReserveObj>();
		System.out.println("        Reserve a Ride!     ");
		while(c != 2)
        {
			 String choice = null;
			 System.out.println("1. Reserve Ride");
			 System.out.println("2. Finish"); 
			 System.out.println("Choose an option (1-2): ");
			 
       	 try {
				while ((choice = in.readLine()) == null || choice.length() == 0);
			} catch (IOException e1) { /*ignore*/
			}
       	 try{
       		 c = Integer.parseInt(choice);
       	 }catch (Exception e)
       	 {
       		 continue;
       	 }
       	 if(c == 1) {
       		ReserveObj temp = new ReserveObj();
       		temp.cost  = -1;
       		temp.start  = -1;
       		temp.end  = -1;
       		String sql = "SELECT * FROM UC";
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				ResultSet result = pstmt.executeQuery();
				if(result.isBeforeFirst())
				{
					System.out.println("Search results:");
					while(result.next())
					{
						System.out.println("vin: " + result.getString("vin"));
						System.out.println("\t" + "Category: " + result.getString("category")
												+ "    Year: " + result.getString("year"));
					}
					System.out.println();
				}
				else
				{
					System.out.println("There are no cars to be reserved");
					c = 2;
					continue;
				}
			} 
			catch(SQLException e) {}
       		System.out.println("Please type in the VIN of the car you'd like to reserve");
       		while(temp.vin == null) {
				try
				{
					temp.vin = in.readLine();
				}
				catch (Exception e) 
				{
				}
       		}
       		System.out.println("Please type in the date you'd like the reserve a ride (yyyy-mm-dd):");
       		while(temp.date == null) {
				try
				{
					temp.date = in.readLine();
					Date test = parseDate(temp.date);
					if(test == null) {
						System.out.println("Error: Please type in the date you'd like the reserve a ride (yyyy-mm-dd):");
						temp.date = null;
					}
				}
				catch (Exception e) 
				{
				}
       		}
       		System.out.println("Please type in what hour you'd like to reserve the car (only one integer 0 - 22):");
       		while(temp.start == -1) {
				try
				{
					temp.start = Integer.parseInt(in.readLine());
					if(temp.start > 22 || temp.start < 0) {
						System.out.println("Error: Please type in what hour you'd like to reserve the car (only one integer 0 - 22):");
						temp.start = -1;
					}
					
				}
				catch (Exception e) 
				{
				}
       		}
       		System.out.println("Please type in what hour you'd like your car reservation to end (only one integer 1 - 23)\n"
       				+ "end hour must be after start hour of the same day: ");
       		while(temp.end == -1) {
				try
				{
					temp.end = Integer.parseInt(in.readLine());
					if(temp.end > 23 || temp.end < 1 || temp.end < temp.start) {
						System.out.println("Error: Please type in what hour you'd like your car reservation to end (only one integer 1 - 23)\n"
			       				+ "end hour must be after start hour of the same day: ");
						temp.start = -1;
					}
					else {
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
										System.out.println("vin: " + result.getString("Owner"));
									}
								}
								System.out.println();
							}
						}
						catch(SQLException e) 
						{
						}
					}
				}
				catch (Exception e) 
				{
				}
       		}
       	 }
       }
		if(reservations.size() > 0) {
			int totalCost = 0;
			String confirmation = "a";
			System.out.println("Confirm these reservations:");
			for(int i = 0; i < reservations.size(); i++) {			
				System.out.println("VIN:" + reservations.get(i).vin);
				System.out.println("Start Hour:" + String.valueOf(reservations.get(i).start));
				System.out.println("End Hour:" + String.valueOf(reservations.get(i).end));
				System.out.println("Date:" + reservations.get(i).date);
				System.out.println("Cost:" + String.valueOf(reservations.get(i).cost));
				totalCost = totalCost + reservations.get(i).cost;
			}
			System.out.println("Total Cost: " + String.valueOf(totalCost));
			
			System.out.println("yes/no?");
			while(true)
			{
				try {
					confirmation = in.readLine();
				}
				catch(Exception e){}
				if(confirmation.equals(null)) {
					confirmation = "a";
				}
				if(confirmation.equals("yes") || confirmation.equals("no"))
					break;
			}
			if(confirmation.equals("yes")) {
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
								}
							}
						}
		
					} 
					catch(SQLException e) 
					{
						System.out.println("Failed to insert into Period\n");
					}
				}
			}
		}
	}
}