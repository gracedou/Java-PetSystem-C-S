//*********************************************************************
//*                                                                   *
//* CIS611               Summer Session 2016                Bing Dou  *
//*                                                                   *
//*                    Program Assignment PA06                        * 
//*                                                                   *
//*                       Class Description                           *
//*                                                                   *
//*                                                                   *
//*           	           7/2/2016 Created                           *
//*                                                                   *
//*                    Saved in: PetServer.java                       *
//*                                                                   *
//*********************************************************************
//

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PetServer {
	
	private Statement stmt = null;
	private int port;
	Connection connect;
	
	public PetServer(int port) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		this.port = port;
		initializeDB();
	}
	
	public void initializeDB() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		
		try{
			ServerSocket serverSocket = new ServerSocket(port);	
		
			// Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		
			// Establish a connection
			connect = DriverManager.getConnection("jdbc:mysql://localhost/mydatabase", "root", "");
			System.out.println("Database connected\n");
		
			// Keep listening
			while(true){

				// Listen for a new connection request
				Socket socket = serverSocket.accept();
				System.out.println("Client connecting...");
	        
				// Create a new thread for the connection
				HandleAClient task = new HandleAClient(socket);
	        
				// Start the new thread
				new Thread(task).start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Stopped connection.\n");
		}
	}

	public void view(Message message) throws SQLException{
		
		// Create statement, allowing to issue SQL queries to the database
		stmt = connect.createStatement();
		
		// resultSet gets the result of the SQL query
	    ResultSet resultSet = stmt.executeQuery("select * from mydatabase.pet Where id = " + message.id + ";");
	    
	    if(resultSet.next()){
	    	System.out.println("Found the pet");
	    	message.id = resultSet.getInt("id");
	    	message.name = resultSet.getString("name");
	    	message.ownerFName = resultSet.getString("owner_firstName");
	    	message.ownerLName = resultSet.getString("owner_lastName");
    		
	    	if(resultSet.getString("sex").matches("M")){
	    		message.sex = 'M';
	    	}
	    	else{
	    		message.sex = 'F';
	    	}
	    	message.species = resultSet.getString("species");
	    	message.color = resultSet.getString("color");
	    	message.birth = resultSet.getDate("birth");
	    }
	    else{
	    	System.out.println("Nothing found");
	    }
	}
	
	public void insert(Message message) throws SQLException{
	
		// Create statement, allowing to issue SQL queries to the database
		stmt = connect.createStatement();
		
		// resultSet gets the result of the SQL query
	    ResultSet resultSet = stmt.executeQuery("select * from mydatabase.pet Where id = " + message.id + ";");
	    
	    if(!resultSet.next()){
	    	// Initialize the preparedStatement
	    	PreparedStatement preparedStatement = null; 
	    	
	    	preparedStatement = connect.prepareStatement("insert into mydatabase.pet values (?, ?, ?, ?, ?, ?, ?, ?)");
	    	
	    	preparedStatement.setInt(1, message.id);
	    	preparedStatement.setString(2, message.name);
	    	preparedStatement.setString(3, message.ownerFName);
	    	preparedStatement.setString(4, message.ownerLName);
	    	preparedStatement.setString(5, String.valueOf(message.sex));
	    	preparedStatement.setString(6, message.species);
	    	
	    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
	    	String s = format.format(message.birth); 
	    	
	    	preparedStatement.setString(7, s);
	    	preparedStatement.setString(8, message.color); 
		    
	    	preparedStatement.executeUpdate();
	    	
	    	System.out.println("Inserted!");
	    }
	    else{
	    	System.out.println("Id exist!");
	    	message.birth = null;
	    }
	}
	
	public void update(Message message) throws SQLException{
		// Create statement, allowing to issue SQL queries to the database
		stmt = connect.createStatement();
		
		// resultSet gets the result of the SQL query
	    ResultSet resultSet = stmt.executeQuery("select * from mydatabase.pet Where id = " + message.id + ";");
	    
	    if(resultSet.next()){
	    	// Initialize the preparedStatement
	    	PreparedStatement preparedStatement = null;
	    	
	    	preparedStatement = connect.prepareStatement("update mydatabase.pet SET name = ?, owner_firstName = ?, owner_lastName = ?, sex = ?, species = ?, color = ?, birth = ? WHERE id = ?");

	    	preparedStatement.setString(1, message.name);
	    	preparedStatement.setString(2, message.ownerFName);
	    	preparedStatement.setString(3, message.ownerLName);
	    	preparedStatement.setString(4, String.valueOf(message.sex));
	    	preparedStatement.setString(5, message.species);
	    	preparedStatement.setString(6, message.color);
	    	
	    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
	    	String s = format.format(message.birth); 
	    	preparedStatement.setString(7, s);
	    	preparedStatement.setInt(8, message.id);

	    	preparedStatement.executeUpdate();
	    	
	    	System.out.println("Updated");
	    }
	    else{
	    	System.out.println("The pet does not exist!");
	    	message.birth = null;
	    }
	}
	
	public void delete(Message message) throws SQLException{
		// Create statement, allowing to issue SQL queries to the database
		stmt = connect.createStatement();
		
		// resultSet gets the result of the SQL query
	    ResultSet resultSet = stmt.executeQuery("select * from mydatabase.pet Where id = " + message.id + ";");
	    
	    if(resultSet.next()){
	    	// Initialize the preparedStatement
	    	PreparedStatement preparedStatement = null;
	  
	    	preparedStatement = connect.prepareStatement("delete from mydatabase.pet WHERE id = ?");
	    	preparedStatement.setInt(1, message.id);
	    	preparedStatement.executeUpdate();
	    	
	    	System.out.println("Deleted!");
	    }
	    else{
	    	message.id = -1;
	    	System.out.println("The pet does not exist!");
	    }
	}
	
	class HandleAClient implements Runnable {
	    private Socket socket; // A connected socket

	    /** Construct a thread */
	    public HandleAClient(Socket socket) {
	      this.socket = socket;
	    }

	    /** Run a thread */
	    public void run() {
	      try {
	        // Create data input and output streams
	        ObjectInputStream inputFromClient = new ObjectInputStream(
	          socket.getInputStream());
	        ObjectOutputStream outputToClient = new ObjectOutputStream(
	          socket.getOutputStream());

	        // Continuously serve the client
	        while(true){
	        	Message message = null;	
	    		message = (Message)inputFromClient.readObject();
	    		
	            switch (message.opType) { 
            	case 1: view(message);
    				break;       
            	case 2: insert(message);
    				break;       
            	case 3: update(message);
    				break; 
            	case 4: delete(message);
    				break; 
	    		 }
	    			
	            outputToClient.writeObject(message);; 
	    	  }
	      }
	      catch(IOException e) {
	        //System.err.println(e);
	    	System.out.println("A client stopped connection!");
	      } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException{
		new PetServer(8000);
	}

}
