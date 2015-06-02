// Prashanth Rajaram (pxr2737)
// Database Systems - CSE 3330 - Fall 2010
// Project 3 - mysql.java

/*
Instructions
	First set classpath: export set CLASSPATH=.:~/<foldername>/mysql-connector-java.jar:$CLASSPATH 
	To compile & run: javac mysql.java followed by java SQL
	Pre-defined username = test (for quick program execution testing) or execute normally to see full functionality 
*/

import java.sql.*;
import java.util.*;
import java.lang.*;
import java.io.*;

final class SQL {
    final static String user = "mysql_username";
    final static String password = "mysql_password";
    final static String db = "mysql_db";
    final static String jdbc = "jdbc:mysql://localhost:3306/"+db+"?user="+user+"&password="+password;
	static int choice = 0;
	static int miniChoice = 0;
	static Scanner scan = new Scanner(System.in);
	static InputStreamReader isr = new InputStreamReader(System.in);
	static BufferedReader br = new BufferedReader(isr);
	static String userName;
	static Random generator = new Random();
	static int start = 1;
	static int totalStock = 0;
	    
	public static void startSession() throws Exception
	{
		String name,query;
		String cartNo;
		int randomCartNo,currentCartNo;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		
		System.out.println(" [Starting Customer Session] ");
		System.out.println(" Please enter your name: ");
		
		name = br.readLine();
		
		query = "SELECT customerName from Customer where customerName = '"+name+"'";
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next())
		{
	
		}
		else
		{
			System.out.println(" It seems like you are a new customer. You must add yourself to the database before you can start a session. ");
			System.out.println(" What would you like to do? : ");
			System.out.println(" 1.Add Yourself To The Database (Add New Customer) ");
			System.out.println(" 2.Exit ");
			
			miniChoice = scan.nextInt();
			
			if(miniChoice == 1)
			{
				addNewCustomer();
				return;
			}
			else
			{
				return;
			}
			
			
		}	
		
		userName = name;
		System.out.println(" Current User: "+userName+" ");
		
		//Deletes everything in current shopping basket
		query = "DELETE from ShoppingBasket where 1";
		stmt.executeUpdate(query);
		System.out.println(" Shopping Basket Cleared. ");
		//Generating Random 4 Digit Cart Number
		randomCartNo = generator.nextInt(8999) + 1000;
		query = "SELECT basketID from ShoppingBasket";
		rs = stmt.executeQuery(query);
				while (rs.next())
				{
					currentCartNo = Integer.parseInt(rs.getString("cartID"));
					if(randomCartNo == currentCartNo)
						randomCartNo = generator.nextInt(8999) + 1000;
				}
				rs.close();		
		
		cartNo = Integer.toString(randomCartNo);
		//Creating entry for new user session in shopping basket
		query = "INSERT INTO ShoppingBasket (basketID, customerName) VALUES ('"+cartNo+"', '"+userName+"')";
		stmt.executeUpdate(query);
		
		System.out.println(" Shopping Basket Updated! ");
		System.out.println(" You have been added to the system: ");
		query = "SELECT * from ShoppingBasket";
		rs = stmt.executeQuery(query);
		System.out.println(" ID     Name");
		System.out.println(" _________________________ ");
			while (rs.next())
				System.out.println(" "+rs.getString("basketID")+"   "+rs.getString("customerName")+" ");
		
		System.out.println("\n 		[Session Started]	 ");
	}
	
	public static void searchSession() throws Exception
	{
		String title,name,query;
		int recordNo = 1;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		
		System.out.println(" [Search Database] ");
		System.out.println(" Please select an option below: ");
		System.out.println(" 1.Search by Title ");
		System.out.println(" 2.Search by Author Name ");
		System.out.println(" 3.View All Books ");
		System.out.println(" 4.Exit Search");
		miniChoice = scan.nextInt();
		if(miniChoice == 1)
		{
			System.out.println("Enter title of book:");
			title = br.readLine();
			
			//Search by book title
			query = "SELECT * from Book,Author,WrittenBy where Book.title='"+title+"' AND Author.ssn = WrittenBy.ssn AND Book.ISBN = WrittenBy.ISBN";
			ResultSet rs = stmt.executeQuery(query);
				if(rs.next())
				{
					System.out.println("   ISBN   Title	 Author  Year  Price  Publisher ");
					System.out.println(" ______________________________________________________________________________________ ");
					
					do
					{
						System.out.println("("+recordNo+") "+rs.getString("ISBN")+" "+rs.getString("title")+" "+rs.getString("Author.name")+" "+rs.getString("year")+" $"+rs.getString("price")+" "+rs.getString("publisher")+" \n");
						++recordNo;
					} while(rs.next());
					
					System.out.println(" ______________________________________________________________________________________ ");
				}
				else
				{
					System.out.println(" Sorry! No records found with that title ");
				}
				rs.close();
				
			searchSession();
		}
		else if(miniChoice == 2)
		{
			System.out.println("Enter author name:");
			name = br.readLine();
			
			//Search by author name
			query = "SELECT * from Book,Author,WrittenBy where name='"+name+"'"+"AND Author.ssn = WrittenBy.ssn AND Book.ISBN = WrittenBy.ISBN";
			ResultSet rs = stmt.executeQuery(query);
						
			if(rs.next())
			{
				System.out.println("   ISBN	 Title	 Author	 Year	Price	Publisher ");
				System.out.println(" ______________________________________________________________________________________ ");
				do
				{
					System.out.println("("+recordNo+") "+rs.getString("ISBN")+" "+rs.getString("title")+" "+rs.getString("Author.name")+" "+rs.getString("year")+" $"+rs.getString("price")+" "+rs.getString("publisher")+" \n");
					++recordNo;
				} while(rs.next());
				
				System.out.println(" ______________________________________________________________________________________ ");
			}
			else
			{
				System.out.println(" Sorry! No records found with that title ");
			}

			rs.close();
			
			searchSession();
			
		}
		else if(miniChoice == 3)
		{
			System.out.println(" 		|Books in CheapBooks Database| 		\n");
			System.out.println("   ISBN	 Title	 Author	 Year	Price	Publisher ");
			System.out.println(" ______________________________________________________________________________________ ");
			
			query = "SELECT * from Book,Author,WrittenBy WHERE Author.ssn = WrittenBy.ssn AND Book.ISBN = WrittenBy.ISBN";
			ResultSet rs = stmt.executeQuery(query);
						
			while(rs.next())
			{
				System.out.println("("+recordNo+") "+rs.getString("ISBN")+" "+rs.getString("title")+" "+rs.getString("Author.name")+" "+rs.getString("year")+" $"+rs.getString("price")+" "+rs.getString("publisher")+" ");
				++recordNo;
			}
			
			System.out.println(" ______________________________________________________________________________________ ");
		}
		else if(miniChoice == 4)
		{
			return;
		}
		else
		{
			System.out.println("Wrong choice entered");
			searchSession();
		}
		
	
	
	}
	
	public static void purchaseSession() throws Exception
	{
		String ISBN = null,query,query2,query3,basketID = null,quantity = null;
		int aQuantity=0;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		System.out.println(" [Starting Purchase Session] ");
		if(userName == null)
		{
			System.out.println(" Sorry! You have not started a session yet ");
			System.out.println(" Do you want to start a session now or exit? ");
			System.out.println(" 1.Start Session ");
			System.out.println(" 2.Exit ");
			miniChoice = scan.nextInt();
			
			if(miniChoice == 1)
			{
				System.out.println("\n Please create your session and then start your purchase session ");
				startSession();
			}
			if(miniChoice == 2)
			{
				return;
			}
			
		}
		else
		{
		System.out.println(" Hello "+userName+" ");
		System.out.println("Please enter ISBN of the book you want to add to your basket: ");
		
		ISBN = br.readLine();
		
		System.out.println(" ISBN	Title	Author	Year	Price	Publisher ");
		System.out.println(" _____________________________________________________________________ ");
		
		query = "SELECT * from Book,Author,WrittenBy WHERE Book.ISBN='"+ISBN+"' AND Author.ssn = WrittenBy.ssn AND Book.ISBN = WrittenBy.ISBN";
		ResultSet rs = stmt.executeQuery(query);
					
				if(rs.next())
				{
					do
					{
						System.out.println(" "+rs.getString("ISBN")+" "+rs.getString("title")+" "+rs.getString("Author.name")+" "+rs.getString("year")+" $"+rs.getString("price")+" "+rs.getString("publisher")+" \n");
					
					} while (rs.next());
					
					rs.close();
		
					query = "SELECT basketID from ShoppingBasket where customerName = '"+userName+"'";
					rs = stmt.executeQuery(query);
					while (rs.next())
						basketID = rs.getString("basketID");
					rs.close();
					
					//Checks database for total stock among all warehouses and displays to user. Error handling for invalid input follows
					query = "SELECT Stocks.number from Stocks,Warehouse,Book where Stocks.ISBN='"+ISBN+"' AND Stocks.warehouseCode = Warehouse.warehouseCode AND Book.ISBN = Stocks.ISBN";
					rs = stmt.executeQuery(query);
					while(rs.next())
					{
						aQuantity = aQuantity + Integer.parseInt(rs.getString("Stocks.number"));
					}
					System.out.println("Checking Database...");
					
					System.out.println("Please enter the exact quantity of this item you want to purchase ("+Integer.toString(aQuantity)+" copies available)");
					if(aQuantity == 0)
					{
						System.out.println("\nSorry! There are no copies left in the warehouse! Please check back later\n");
					}
					else
					{
						quantity = br.readLine();
						if(Integer.parseInt(quantity) > aQuantity)
						{
							quantity = Integer.toString(aQuantity);
							System.out.println("The quantity you entered is greater than the quantity available in our warehouse stocks. We have added the maximum quantity available to your shopping basket.");
						}	
						
						query = "SELECT * from Contains,ShoppingBasket,Book,Customer WHERE ShoppingBasket.customerName='"+userName+"' AND ShoppingBasket.basketID ='"+basketID+"' AND Book.ISBN='"+ISBN+"' AND Book.ISBN = Contains.ISBN AND ShoppingBasket.basketID = Contains.basketID AND ShoppingBasket.customerName = Customer.customerName";
						rs = stmt.executeQuery(query);
						if(rs.next())
						{
							query2 = "SELECT number FROM Contains WHERE basketID='"+basketID+"' AND ISBN='"+ISBN+"'";
							ResultSet rs2 = stmt.executeQuery(query2);
							while(rs2.next())
							{
								quantity = quantity + Integer.parseInt(rs.getString("number"));
							}
							//Update contains for shopping basket if the item is already in basket
							query3 = "UPDATE Contains SET number='"+quantity+"' WHERE basketID='"+basketID+"' AND ISBN='"+ISBN+"'";
							stmt.executeUpdate(query3);
						}
						else
						{
							//Insert into contains for shopping basket if the item has not been added to basket
							query2 = "INSERT into Contains (ISBN,basketID,number) VALUES('"+ISBN+"', '"+basketID+"', '"+quantity+"')";
							stmt.executeUpdate(query2);
						}
						totalStock = aQuantity;
					}
					//Recursive Menu
					System.out.println(" Do you want to continue purchasing? ");
					System.out.println(" 1.Yes (Continues purchase session to purchase another Book) ");
					System.out.println(" 2.No  (Ends Purchase Session) ");
					miniChoice = scan.nextInt();
					if(miniChoice == 1)
					{
						purchaseSession();
					}
					else
					{
						return;
					}
									
				}
				else
				{
					System.out.println(" Sorry! No books found with the ISBN entered ");
					
					System.out.println(" What do you want to do? : ");
					System.out.println(" 1.Search Again ");
					System.out.println(" 2.Exit ");
					
					miniChoice = scan.nextInt();
					if(miniChoice == 1)
					{
						purchaseSession();
					}
					else
					{
						return;
					}
				}
		
		}		
	}
	
	public static void addNewCustomer() throws Exception
	{
		String name,address,email,phoneString,query;
		long phone;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		
		System.out.println(" [Add New Customer] ");
		System.out.println(" 1.Continue ");
		System.out.println(" 2.Exit Add New Customer ");
		
		miniChoice = scan.nextInt();
		if (miniChoice == 1)
		{
			System.out.println(" Enter Name of the customer: ");
			name = br.readLine();
			if(name == null)
			{
				System.out.println("Sorry! Invalid Name.");
				addNewCustomer();			
			}
			else
			{
				//User Input
				System.out.println(" Enter Address of the customer: ");
				address = br.readLine();
				System.out.println(" Enter email of the customer: ");
				email = br.readLine();
				System.out.println(" Enter Phone No of the customer (Numbers only): ");
				phoneString = br.readLine();
				phone = Long.parseLong(phoneString);
				
				
				//Update Customer Table
				query = "INSERT INTO Customer (customerName, address, email, phone) VALUES ('"+name+"', '"+address+"', '"+email+"', '"+phone+"')";

				stmt.executeUpdate(query);
		
				System.out.println(" New Customer Record inserted: ");		
				System.out.println(" CustomerName  Address  Email  Phone");
				System.out.println(" _______________________________________________________ ");
			
				query = "SELECT * from Customer where customerName = '"+name+"'";
		
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next())
					System.out.println(" "+rs.getString("customerName")+" "+rs.getString("address")+" "+rs.getString("email")+" "+rs.getString("phone")+" ");
				rs.close();
				
			}
		}
		else if (miniChoice == 2)
		{
			return;
		}
		else
		{
			System.out.println("Wrong choice entered!");
			addNewCustomer();
		}
		
	}
	
	
	public static void endSession() throws Exception
	{
		String query,basketID = null;
		int price,index,currentQuantity = 0,flag = 0;
		String wQuantity,wCode;
		float totalPrice = 0;
		List<String> isbnList = new ArrayList<String>();
		List<String> priceList = new ArrayList<String>();
		List<String> quantityList = new ArrayList<String>();
		
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		Statement stmt2 = con.createStatement();
		Statement stmt3 = con.createStatement();	
		
		System.out.println(" [End Session] ");
		
		if(userName == null)
		{
			System.out.println(" Sorry! You have not started a session yet");
			System.out.println(" Do you want to start a session now or exit? ");
			System.out.println(" 1.Start Session ");
			System.out.println(" 2.Exit ");
			miniChoice = scan.nextInt();
			
			if(miniChoice == 1)
			{
				System.out.println("\n Please create your session and then start your purchase session before checking out and ending your session. ");
				startSession();				
			}
			if(miniChoice == 2)
			{
				return;
			}
			
		}
		else
		{
			System.out.println(" Hello "+userName+", are you ready to check out and end your session?  ");
			System.out.println(" 1.Continue ");
			System.out.println(" 2.Exit ");
		
			miniChoice = scan.nextInt();
			if(miniChoice == 1)
			{
				query = "SELECT basketID from ShoppingBasket WHERE customerName='"+userName+"'";
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next())
					basketID = rs.getString("basketID");
				rs.close();
				
				query = "SELECT ISBN,number from Contains WHERE basketID='"+basketID+"'";
				rs = stmt.executeQuery(query); 
				while(rs.next())
				{
					isbnList.add(rs.getString("ISBN"));
					quantityList.add(rs.getString("number"));
				}
				rs.close();
				
				for(index=0;index<isbnList.size();index++)
				{
					query = "SELECT price from Book where ISBN='"+isbnList.get(index)+"'";
					rs = stmt.executeQuery(query);
					while(rs.next())
					{
						priceList.add(rs.getString("price"));
					}
					
				}
				//Shopping Summary
				System.out.println("        "+userName+"'s Shopping Summary      ");
				System.out.println(" _________________________________________ ");
				System.out.println(" Book	Price	QTY");
				query = "SELECT Book.title,Book.price,Contains.number from Contains,ShoppingBasket,Book,Customer WHERE Contains.basketID = ShoppingBasket.basketID AND Contains.ISBN = Book.ISBN AND ShoppingBasket.customerName = Customer.customerName AND ShoppingBasket.customerName='"+userName+"'";
				rs = stmt.executeQuery(query);
				if(rs.next())
				{
					
					do
					{
						System.out.println(" "+rs.getString("Book.title")+" $"+rs.getString("Book.price")+" "+rs.getString("Contains.number")+" ");
					} while(rs.next());
				}
				System.out.println(" ________________________________________ ");
				
				for(index=0;index<isbnList.size();index++)
				{
					//calculating total cost for each item in the user's shopping basket
					totalPrice = totalPrice + ( new Float(priceList.get(index)) * new Float(quantityList.get(index)));
				}
				System.out.println(" Total Cost for this order: 	$"+totalPrice+" ");
				
				for(index=0;index<isbnList.size();index++)
				{
					query = "SELECT Stocks.number,Stocks.warehouseCode from Stocks,Warehouse,Book WHERE Stocks.warehouseCode = Warehouse.warehouseCode AND Stocks.ISBN = Book.ISBN AND Stocks.ISBN ='"+isbnList.get(index)+"' GROUP BY Stocks.warehouseCode";
					rs = stmt.executeQuery(query);
					while(rs.next())
					{
						if(flag == 1 && currentQuantity == 0)
						{
							break;
						}
						
						
						wCode = rs.getString("Stocks.warehouseCode");
						wQuantity = rs.getString("Stocks.number");
						currentQuantity = Integer.parseInt(quantityList.get(index));
						
						if(Integer.parseInt(wQuantity) >= currentQuantity)
						{
							wQuantity = Integer.toString(Integer.parseInt(wQuantity) - Integer.parseInt(quantityList.get(index)));
							//Update stocks table with new values after order
							stmt2.executeUpdate("UPDATE Stocks SET Stocks.number ='"+wQuantity+"' WHERE Stocks.ISBN ='"+isbnList.get(index)+"' AND Stocks.warehouseCode ='"+wCode+"'");
							//Send order to warehouse that has quantity
							System.out.println(" Your order has been placed. Requests have been sent to our warehouses. ");
							stmt3.executeUpdate("INSERT INTO ShippingOrder (ISBN,warehouseCode,customerName,number) VALUES ('"+isbnList.get(index)+"', '"+wCode+"','"+userName+"', '"+quantityList.get(index)+"')");
							break;
						}
						else if(Integer.parseInt(wQuantity) < currentQuantity && Integer.parseInt(wQuantity) != 0)
						{
							currentQuantity = Integer.parseInt(quantityList.get(index)) - Integer.parseInt(wQuantity);
							//Update stocks table with new values after order when quantity is maxed out in current warehouse
							stmt2.executeUpdate("UPDATE Stocks SET Stocks.number ='0' WHERE Stocks.ISBN ='"+isbnList.get(index)+"' AND Stocks.warehouseCode ='"+wCode+"'");
							//Send order to warehouses if quantity is maxed out in current warehouse
							System.out.println(" Your order has been placed. Requests have been sent to our warehouses. ");
							stmt3.executeUpdate("INSERT INTO ShippingOrder (ISBN,warehouseCode,customerName,number) VALUES ('"+isbnList.get(index)+"', '"+wCode+"','"+userName+"', '"+wQuantity+"')");
							flag = 1;
						}
						else
						{}
						
					}
					flag = 0;
					currentQuantity = 0;
				}
				
				//Clearing basket,contains,user session
				query = "DELETE FROM ShoppingBasket WHERE customerName='"+userName+"'";
				stmt.executeUpdate(query);
				query = "DELETE FROM Contains WHERE basketID='"+basketID+"'";
				stmt.executeUpdate(query);
				userName = null;
				System.out.println("\n 		[Session Ended]		 ");
			}
			else if(miniChoice == 2)
			{
				return;
			}
			else
			{
				System.out.println("Wrong choice entered!");
				endSession();
			}
		
		}
	}
	
	public static void viewBasket() throws Exception
	{
		String query;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		if(userName == null)
		{
			System.out.println(" Sorry! You have not started a session yet");
			System.out.println(" Do you want to start a session now or exit? ");
			System.out.println(" 1.Start Session ");
			System.out.println(" 2.Exit ");
			miniChoice = scan.nextInt();
			
			if(miniChoice == 1)
			{
				System.out.println("\n Please create your session and then view your basket contents ");
				startSession();
				
			}
			if(miniChoice == 2)
			{
				return;
			}
		}
		else
		{
			query = "SELECT ShoppingBasket.basketID,ShoppingBasket.customerName,Book.title,Book.price,Contains.number from Contains,ShoppingBasket,Book,Customer WHERE Contains.basketID = ShoppingBasket.basketID AND Contains.ISBN = Book.ISBN AND ShoppingBasket.customerName = Customer.customerName AND ShoppingBasket.customerName='"+userName+"'";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
			{
				System.out.println("        "+userName+"'s Shopping Basket"+"     ");
				System.out.println(" ________________________________________________ ");
				System.out.println(" ID	 Book  Price	QTY");
				do
				{
					System.out.println(" "+rs.getString("ShoppingBasket.basketID")+"  "+rs.getString("Book.title")+"  $"+rs.getString("Book.price")+"  "+rs.getString("Contains.number")+" ");
				} while(rs.next());
			}
			else
			{
				System.out.println("         "+userName+"'s Shopping Basket"+"     ");
				System.out.println(" ________________________________________________ ");
				System.out.println(" 			(empty)				");
			}
			
		}
	}
	
	public static void editBasket() throws Exception
	{
		String query,query2,query3;
		int itemNo = 1,delNo = 1,updateNo = 1,delChoice = 0,updateChoice = 0,quantity = 0;
		String basketID = null,ISBN;
		Connection con = DriverManager.getConnection(jdbc);
		Statement stmt = con.createStatement();
		if(userName == null)
		{
			System.out.println(" Sorry! You have not started a session yet");
			System.out.println(" Do you want to start a session now or exit? ");
			System.out.println(" 1.Start Session ");
			System.out.println(" 2.Exit ");
			miniChoice = scan.nextInt();
			
			if(miniChoice == 1)
			{
				System.out.println("\n Please create your session and then view or edit your basket contents ");
				startSession();
				
			}
			if(miniChoice == 2)
			{
				return;
			}
		}
		else
		{
			query = "SELECT ShoppingBasket.basketID,ShoppingBasket.customerName,Book.title,Book.price,Contains.number from Contains,ShoppingBasket,Book,Customer WHERE Contains.basketID = ShoppingBasket.basketID AND Contains.ISBN = Book.ISBN AND ShoppingBasket.customerName = Customer.customerName AND ShoppingBasket.customerName='"+userName+"'";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
			{
				System.out.println("        "+userName+"'s Shopping Basket"+"     ");
				System.out.println(" ________________________________________________ ");
				System.out.println("   ID	Book  Price	QTY");
				do
				{
					System.out.println("("+itemNo+")  "+rs.getString("ShoppingBasket.basketID")+"  "+rs.getString("Book.title")+"  $"+rs.getString("Book.price")+"  "+rs.getString("Contains.number")+" ");
					++itemNo;
				} while(rs.next());
			
			
			}
			else
			{
				System.out.println("         "+userName+"'s Shopping Basket"+"     ");
				System.out.println(" ________________________________________________ ");
				System.out.println(" 			(empty)				");
				System.out.println(" No items in basket to edit ");
				return;
			}
			// Edit Basket Options
			System.out.println("\n 1.Remove Item | 2. Update Quantity | 3. Clear Basket(Remove All Items) | 4. Exit ");
			System.out.println("\n Choice: ");
			miniChoice = scan.nextInt();
			//Remove 1 item by selecting the index of item in basket
			if(miniChoice == 1)
			{
				if(itemNo>2)
				{
					itemNo = itemNo-1;
					System.out.println(" Select item to remove from basket (1-"+itemNo+" available) ");
					delChoice = scan.nextInt();
				}
				else
				{
					delChoice = 1;
				}
				
				query = "select ShoppingBasket.basketID,ShoppingBasket.customerName,Book.ISBN from ShoppingBasket,Book,Contains,Customer where ShoppingBasket.basketID = Contains.basketID AND Contains.ISBN = Book.ISBN AND ShoppingBasket.customerName = Customer.customerName";
				rs = stmt.executeQuery(query);
				while(rs.next())
				{
					if(delNo == delChoice)
					{
						basketID = rs.getString("ShoppingBasket.basketID");
						ISBN = rs.getString("Book.ISBN");
						
						query = "DELETE FROM Contains WHERE basketID='"+basketID+"' AND ISBN ='"+ISBN+"'";
						stmt.executeUpdate(query);
						System.out.println(" Item Removed! ");
						break;
					}
					++delNo;
				}
			}
			//Update quantity of item by selecting index of item in basket
			else if(miniChoice == 2)
			{
				if(itemNo>2)
				{
					itemNo = itemNo-1;
					System.out.println(" Select item to update from basket (1-"+itemNo+" available) ");
					updateChoice = scan.nextInt();
				}
				else
				{
					updateChoice = 1;
				}
				
				query = "select ShoppingBasket.basketID,ShoppingBasket.customerName,Book.ISBN from ShoppingBasket,Book,Contains,Customer where ShoppingBasket.basketID = Contains.basketID AND Contains.ISBN = Book.ISBN AND ShoppingBasket.customerName = Customer.customerName";
				rs = stmt.executeQuery(query);
				while(rs.next())
				{
					if(updateNo == updateChoice)
					{
						basketID = rs.getString("ShoppingBasket.basketID");
						ISBN = rs.getString("Book.ISBN");
						
						System.out.println(" Enter quantity to update: ");
						quantity = scan.nextInt();
						
						query = "UPDATE Contains SET number ='"+Integer.toString(quantity)+"' WHERE basketID='"+basketID+"' AND ISBN ='"+ISBN+"'";
						stmt.executeUpdate(query);
						System.out.println(" Item Updated! ");
						break;
					}
					++updateNo;
				}
				
			}
			//Clear basket completely
			else if(miniChoice == 3)
			{				
				query = "SELECT basketID from ShoppingBasket where customerName='"+userName+"'";
				rs = stmt.executeQuery(query);
				while(rs.next())
				{
					basketID = rs.getString("basketID");
				}
				query = "DELETE FROM ShoppingBasket WHERE customerName='"+userName+"'";
				stmt.executeUpdate(query);
				query = "DELETE FROM Contains WHERE basketID='"+basketID+"'";
				stmt.executeUpdate(query);
				System.out.println(" Basket Cleared! ");
			}			
			else if(miniChoice == 4)
			{
				return;
			}
			else
			{
				System.out.println(" Wrong choice entered! ");
				editBasket();
			}
					
		
		
		
		}
	}
	
	
	public static void intro()
	{
		//Welcome screen with output statements
		System.out.println("\n       Welcome to CheapBooks     ");
		System.out.println("---------------------------------------------");
		System.out.println("\nPlease Choose one of the following options:");
		System.out.println("(1)Start a new customer session");
		System.out.println("(2)Search Session");
		System.out.println("(3)Purchase Session");
		System.out.println("(4)View Basket");
		System.out.println("(5)Edit Basket");
		System.out.println("(6)End Customer Session");
		System.out.println("(7)Add New Customer");
		System.out.println("(8)Quit Menu (Closes Connections)");
		System.out.println("---------------------------------------------");
	}
	
	
	public static void main ( String[] args ) throws Exception {
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	Connection con = DriverManager.getConnection(jdbc);
	Statement stmt = con.createStatement();
	
	
	
	/*Main Menu Switch*/
	while(true)
	{
		intro();
		System.out.println("Your choice:");
		choice = scan.nextInt();
				
		switch(choice)
		{
			case 1:
				startSession();
				break;
			case 2:
				searchSession();
				break;
			case 3:
				purchaseSession();
				break;
			case 4:
				viewBasket();
				break;
			case 5:
				editBasket();
				break;
			case 6:
				endSession();
				break;
			case 7:
				addNewCustomer();
				break;
			case 8:
				stmt.close();
				con.close();
				System.out.println("\nThank you for shopping with CheapBooks! Please come back again\n");
				System.exit(0);
			default:
				System.out.println("Incorrect choice entered");
				break;
		
		}
	}
    
	}
}
