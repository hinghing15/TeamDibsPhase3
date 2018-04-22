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

public class DriverOptions
{
	private Connector2 con;
	private BufferedReader in;
	private String userLogin;
	public DriverOptions(Connector2 con, String userLogin)
	{
		in = new BufferedReader(new InputStreamReader(System.in));
		this.con = con;
		this.userLogin = userLogin;
	}
	public void selectDriverOp()
	{
		 String choice = null;
	     int c=0;
		 while(c != 4)
		 {
			 System.out.println("        UUber Driver Options     ");
			 System.out.println("1. Add a new UUber Car"); //DONE
			 System.out.println("2. Update a UUber Car"); //DONE
			 System.out.println("3. Add your availablity");
			 System.out.println("4. Go back\n"); //DONE
			 System.out.println("Choose an option (1-4): ");
			 
			 try {
				 while ((choice = in.readLine()) == null || choice.length() == 0);
			 } catch (IOException e1) { /*ignore*/}
			 try{
				 c = Integer.parseInt(choice);
			 }catch (Exception e)
			 {
				 continue;
			 }
			 if (c<1 | c>3)
				 continue;
			 switch(c) {
	       	 
		     	case 1: //Add a new UUber Car
		     		try {
		     		printCars();
		     		String ch = null;
					int c2 = 0;
					String category = null;
					String make = null;
					String model = null;
					String yearS = null;
					String sql=null;
					
					System.out.println("Choose a category: ");
					while(c2<1 | c2>3)
					 {
						 System.out.println("1. Economy");
						 System.out.println("2. Comfort");
						 System.out.println("3. Luxury\n");
						 System.out.println("Choose an option (1-3): ");
						 
						 try {
							 while ((ch = in.readLine()) == null || ch.length() == 0);
						 } catch (IOException e1) { /*ignore*/}
						 try{
							 c2 = Integer.parseInt(choice);
						 }catch (Exception e)
						 {
							 continue;
						 }
						 if (c2<1 | c2>3)
							 continue;
						 switch(c2) {
				       	 
					     	case 1: //Add a new UUber Car
					     		category = "Economy";
					       		break;
					       	case 2: //UUpdate a UUber Car
					       		category = "Comfort";
					       		break;
					       	case 3:
					       		category = "Luxury";
					       		break;
				       	}
			        }
					
					System.out.println("Enter the make of your car: ");
					while((make = in.readLine()) == null || make.length() == 0);
					System.out.println("Enter the model of your car: ");
					while((model = in.readLine()) == null || model.length() == 0);
					System.out.println("Enter the year of your car: ");
					while((yearS = in.readLine()) == null || yearS.length() == 0) 
					{
						try{
							Integer.parseInt(yearS);
						 }catch (Exception e)
						 {
							 System.out.println("Please enter a valid year");
						 }
					}
		     		addNewCar(make, model, category, yearS, userLogin);
		     		} catch(Exception e) {}
		       		break;
		       	case 2: //UUpdate a UUber Car
		       		try{
		       		if(printCars() != null)
		       		{
		       			String choice3 = null;
		    			String choice2 = null;
		    			int c2 = 0;
		    			String category = null;
		    			String make = null;
		    			String model = null;
		    			String yearS = null;
		    			String sql=null;
		    			System.out.println("Type in the vin of the vehicle you would like to update: ");
		    			try 
		    			{
		    				 while ((choice3 = in.readLine()) == null || choice3.length() == 0);
		    				 try 
		    				 {
		    					 Integer.parseInt(choice3);
		    				 } catch (Exception e) {
		    					 System.out.println("Invalid vin");
		    					 return;
		    				 }
		    				 if(!isUsersCar(choice3))
		    				 {
		    					 return;
		    				 }
		    			 } catch (IOException e1) {}
		    			
		    			//update
		    			System.out.println("Choose a category: ");
		    			while(c2<1 | c2>3)
		    			 {
		    				 System.out.println("1. Economy");
		    				 System.out.println("2. Comfort");
		    				 System.out.println("3. Luxury\n");
		    				 System.out.println("Choose an option (1-3): ");
		    				 
		    				 try {
		    					 while ((choice2 = in.readLine()) == null || choice2.length() == 0);
		    				 } catch (IOException e1) { /*ignore*/}
		    				 try{
		    					 c2 = Integer.parseInt(choice2);
		    				 }catch (Exception e)
		    				 {
		    					 continue;
		    				 }
		    				 if (c2<1 | c2>3)
		    					 continue;
		    				 switch(c2) {
		    		       	 
		    			     	case 1: //Add a new UUber Car
		    			     		category = "Economy";
		    			       		break;
		    			       	case 2: //UUpdate a UUber Car
		    			       		category = "Comfort";
		    			       		break;
		    			       	case 3:
		    			       		category = "Luxury";
		    			       		break;
		    		       	}
		    	        }
		    			
		    			System.out.println("Enter the make of your car: ");
		    			while((make = in.readLine()) == null || make.length() == 0);
		    			System.out.println("Enter the model of your: ");
		    			while((model = in.readLine()) == null || model.length() == 0);
		    			System.out.println("Enter the year of your car: ");
		    			while((yearS = in.readLine()) == null || yearS.length() == 0) 
		    			{
		    				try{
		    					Integer.parseInt(yearS);
		    				 }catch (Exception e)
		    				 {
		    					 System.out.println("Please enter a valid year");
		    				 }
		    			}
		       			updateCar(make, model, category, yearS, choice3);
		       		}
		       		} catch(Exception e) {}
		       		break;
		       	case 3:
		       		printAv();
		       		try
		       		{
		       			String fromHour = null;
		    			String toHour = null;
		    			int FH = 0;
		    			int TH = 0;
		    			String sql=null;
		    			
		    			System.out.println("Enter the hour of the day you would like your shift to start (0-23): ");
		    			while (fromHour == null)
		    			{
		    				while((fromHour = in.readLine()) == null || fromHour.length() == 0);
		    				try {
		    					FH = Integer.parseInt(fromHour);
		    					if(FH < 0 | FH > 23)
		    					{
		    						System.out.println("Not a valid time. Try again: ");
		    						fromHour = null;
		    					}
		    				}catch (Exception e)
		    				{
		    					System.out.println("Not a valid time. Try again: ");
		    					fromHour = null;
		    				}
		    			}
		    			
		    			System.out.println("Enter the hour of the day you would like your shift to end (1-24): ");
		    			while (toHour == null)
		    			{
		    				while((toHour = in.readLine()) == null || toHour.length() == 0);
		    				try {
		    					TH = Integer.parseInt(toHour);
		    					if(TH < 1 || TH > 24 || TH <= FH)
		    					{
		    						System.out.println("Not a valid time. Try again: ");
		    						toHour = null;
		    					}
		    				}catch (Exception e)
		    				{
		    					System.out.println("Not a valid time. Try again2: ");
		    					toHour = null;
		    				}
		    			}
			       		addAv(fromHour, toHour);
		       		} catch(Exception e) {}
	       	}
        }
	}
	
	/*
	 * Adds a new car to UC
	 */
	public boolean addNewCar(String make, String model, String category, String yearS, String userLogin)
	{
		try 
		{
			
			String sql = "INSERT INTO UC(category, year, login) VALUES(?,?,?)"; //vin autoincrements
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				String tid = addCarInfo(make, model);
				pstmt.setString(1, category);
				pstmt.setString(2, yearS);
				pstmt.setString(3, userLogin);
				int success = pstmt.executeUpdate();
				if(success == 1)
				{
					ResultSet generatedKeys = pstmt.getGeneratedKeys();
					generatedKeys.next();
					String vin = Integer.toString(generatedKeys.getInt(1));
					String sql2 = "INSERT INTO IsCtypes(vin, tid) VALUES(?,?)";
					try(PreparedStatement pstmt2 = con.con.prepareStatement(sql2))
					{
						pstmt2.setString(1, vin);
						pstmt2.setString(2, tid);
						pstmt2.executeUpdate();
						return true;
					}
					catch(SQLException e) {
					}
					System.out.println("Car has been registered!\n");
				}

			} 
			catch(SQLException e) 
			{
				System.out.println("Car registration has failed!\n");
			}
		}
		catch (Exception e) {  }
		return false;		
	}
	
	/*
	 * Prints all the cars that are owned by the current active user
	 */
	public String printCars()
	{
		try 
		{	
			String sql = "SELECT UC.vin, category, year, UC.login, make, model, address, s.avScore " + 
					"FROM Ctypes, IsCtypes, UU, UC, " + 
					"(SELECT UC.vin, AVG(Feedback.score) as avScore FROM UC LEFT OUTER JOIN Feedback ON UC.vin = Feedback.vin GROUP BY UC.vin) as s " + 
					"WHERE UC.vin = IsCtypes.vin AND IsCtypes.tid = Ctypes.tid AND UU.login = UC.login AND UC.vin = s.vin AND UC.login = ?"; 
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1, userLogin);
				ResultSet result = pstmt.executeQuery();
				if(result.isBeforeFirst())
				{
					System.out.println("Your Registered UUber Cars:");
					String carInfo = "";
					while(result.next())
					{
						System.out.println("vin: " + result.getString("vin"));
						carInfo += 				"<BR>vin: " + result.getString("vin")
												+ "<BR><b>Category: </b>" + result.getString("category") 
												+ "    <b>Make: </b>" + result.getString("make")
												+ "    <b>Model: </b>" + result.getString("model")
												+ "    <b>Year: </b>" + result.getString("year")
												+ "    <b>Owner: </b>" + result.getString("login")
												+ "    <b>City: </b>" + result.getString("address")
												+ "    <b>Average Score: </b>" + result.getString("avScore") + "<BR>";
						System.out.println("\t" + "Category: " + result.getString("category") 
												+ "    Make: " + result.getString("make")
												+ "    Model: " + result.getString("model")
												+ "    Year: " + result.getString("year")
												+ "    Owner: " + result.getString("login")
												+ "    City: " + result.getString("address")
												+ "    Average Score: " + result.getString("avScore"));
					}
					System.out.println();
					return carInfo;
				}
				else
				{
					System.out.println("You do not own any cars.");
				}
			} 
			catch(SQLException e) {}
		}
		catch (Exception e) {}
		return null;
	}
	
	/*
	 * Updates car user selects
	 */
	public boolean updateCar(String make, String model, String category, String yearS, String choice)
	{
		try 
		{
			String sql=null;
			String tid = addCarInfo(make, model);
			String sql2 = "UPDATE IsCtypes SET tid = ? where vin = ?";
			try(PreparedStatement pstmt2 = con.con.prepareStatement(sql2))
			{
				pstmt2.setString(1, tid);
				pstmt2.setString(2, choice);
				pstmt2.executeUpdate();
			}
			catch(SQLException e) {
			}
			sql = "UPDATE UC SET category = ?, year = ? WHERE login = ? AND vin = ?";
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1, category);
				pstmt.setString(2, yearS);
				pstmt.setString(3, userLogin);
				pstmt.setString(4, choice);
				int success = pstmt.executeUpdate();
				if(success == 1)
				{
					System.out.println("Car has been updated!\n");
					return true;
				}

			} 
			catch(SQLException e) 
			{
				System.out.println("Car update has failed!\n");
			}
			
		}catch(Exception e) {
		}
		return false;
		
	}
	/*
	 * Detemines if given vin is users car
	 */
	public boolean isUsersCar(String choice)
	{
		try 
		{
			String sql=null;
				 
			sql = "SELECT * FROM UC WHERE vin = ? and login = ?";
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1,  choice);
				pstmt.setString(2, userLogin);
				ResultSet result = pstmt.executeQuery();
				if(result.next())
				{
					return true;
				}
				else
					System.out.println("Vehicle chosen is not registered by you");
			} 
			catch(SQLException e) 
			{
				System.out.println("Invalid vin");
			}
		}
		catch (Exception e) 
		{ 
			System.out.println("Invalid vin");
		}
		return false; //no car
	}
	
	/*
	 * Adds car info to Ctypes and IsCtypes. Returns tid
	 */
	public String addCarInfo(String make, String model)
	{
		String tid = getCtypeTid(make, model);
		//create new car type if car type doesn't exist already
		if(tid == null)
		{
			try 
			{
				String sql=null;

				sql = "INSERT INTO Ctypes(make, model) VALUES(?,?)";
				try(PreparedStatement pstmt = con.con.prepareStatement(sql))
				{
					pstmt.setString(1,  make);
					pstmt.setString(2, model);
					int success = pstmt.executeUpdate();
					if(success == 1)
					{
						ResultSet generatedKeys = pstmt.getGeneratedKeys();
						generatedKeys.next();
						tid = Integer.toString(generatedKeys.getInt(1));
					}

				} 
				catch(SQLException e) 
				{
				}
			}
			catch (Exception e) 
			{
			}
		}
		return tid;
	}
	
	/*
	 * Returns tid of car type if exists already, otherwise, return null
	 */
	public String getCtypeTid(String make, String model)
	{
		try 
		{
			String sql=null;
				 
			sql = "SELECT * FROM Ctypes WHERE make = ? AND model = ?";
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1,  make);
				pstmt.setString(2,  model);
				ResultSet result = pstmt.executeQuery();
				if(result.next())
				{
					return result.getString("tid");
				}
			} 
			catch(SQLException e) 
			{
			}
		}
		catch (Exception e) 
		{
		}
		return null;
	}
	/*
	 * Prints the driver's already existing availability
	 */
	public String printAv()
	{
		try 
		{	
			String sql = "SELECT * FROM Available, Period WHERE login = ? AND Available.pid = Period.pid"; 
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setString(1, userLogin);
				ResultSet result = pstmt.executeQuery();
				if(result.isBeforeFirst())
				{
					String av = "";
					System.out.println("Your availablity:");
					while(result.next())
					{
						av += "<b>From: </b>" + result.getString("fromHour") + "<b> To: </b>" + result.getString("toHour") + "<BR>";
						System.out.println("\tFrom: " + result.getString("fromHour") + "\tTo: " + result.getString("toHour"));
					}
					System.out.println();
					return av;
				}
				else
				{
					System.out.println("You have no available times");
				}
			} 
			catch(SQLException e) {}
		}
		catch (Exception e) {}
		return "";
	}
	/*
	 * Adds a driver's availability
	 */
	public boolean addAv(String fromHour, String toHour)
	{
		try 
		{
			String sql = null;
			int success = 0;
			ResultSet generatedKeys;
			int pid;
			pid = periodExists(Integer.parseInt(fromHour), Integer.parseInt(toHour));
			if(pid == Integer.MAX_VALUE)
			{
				sql = "INSERT INTO Period(fromHour, toHour) VALUES(?,?)"; //pid autoincrements
				try(PreparedStatement pstmt = con.con.prepareStatement(sql))
				{
					pstmt.setString(1, fromHour);
					pstmt.setString(2, toHour);
					success = pstmt.executeUpdate();
					if(success == 1)
					{
						generatedKeys = pstmt.getGeneratedKeys();
						generatedKeys.next();
						pid = generatedKeys.getInt(1);
					}
	
				} 
				catch(SQLException e) 
				{
					System.out.println("Failed to add available time!\n");
				}
			}
		
			String sql2 = "INSERT INTO Available(login, pid) VALUES(?,?)";
			try(PreparedStatement pstmt2 = con.con.prepareStatement(sql2))
			{
				pstmt2.setString(1, userLogin);
				pstmt2.setInt(2, pid);
				pstmt2.executeUpdate();
				System.out.println("Available time has been added!\n");
				return true;
			}
			catch(SQLException e) {
			}
		}
		catch (Exception e) {  }
		return false;
	}
	/*
	 * Determines if a given period already exists in the Period table
	 */
	private int periodExists(int from, int to)
	{
		try 
		{		 
			String sql = "SELECT * FROM Period WHERE fromHour = ? AND toHour = ?";
			try(PreparedStatement pstmt = con.con.prepareStatement(sql))
			{
				pstmt.setInt(1, from);
				pstmt.setInt(2, to);
				ResultSet result = pstmt.executeQuery();
				if(result.next())
				{
					return result.getInt("pid"); // period exists
				}
			} 
			catch(SQLException e) {}
		}
		catch (Exception e) {}
		return Integer.MAX_VALUE; //period does not exist
	}
}