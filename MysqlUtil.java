package com.team9.bookingsystem;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.apache.commons.dbcp2.BasicDataSource;


import javax.activation.DataSource;
import javax.imageio.ImageIO;

import java.util.Date;


public class MysqlUtil {

    /**
     * variable declaration
     */
    private final static String URL= "jdbc:mysql://sql.smallwhitebird.com:3306/BookingSystem";;
    private final static String USER = "team9";
    private final static String PASS = "team9";
    private final static String CLASSNAME = "com.mysql.jdbc.Driver";
    private final static BasicDataSource dataSource;

    static {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(CLASSNAME);
        basicDataSource.setUrl(URL);
        basicDataSource.setUsername(USER);
        basicDataSource.setPassword(PASS);
        dataSource = basicDataSource;
    }

   
    public MysqlUtil()
    {
        System.out.println("util Constructor");
    }


    /**
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
//        System.out.println("number of active Connections:");
//            System.out.println(dataSource.getNumActive());
//        System.err.println("nr of idle connections:");
//        System.err.println(dataSource.getNumIdle());

            return dataSource.getConnection();

//              try{
//            Class.forName("com.mysql.jdbc.Driver");
//        }catch (ClassNotFoundException e){
//            e.printStackTrace();
//        }
//
//        return DriverManager.getConnection(path,user,pass);



    }
    // accepts any query, BEWARE can damage database.

    /**
     * @param query
     */
    public void runQuery(String query){
        try(Connection connection = getConnection()){

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("worked");
            rs.close();

        }catch(SQLException e){
        e.printStackTrace();
        }

    }

    /**
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public User loginAndGetUser(String username,String password) throws Exception
    {
        username = "'"+username+"'";
        password = "'"+password+"'";
        // create User Object to populate with database result.
        User toReturn = new User();

        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){



            System.out.println("Connection Established");
            String SQL="SELECT * " + "FROM User WHERE alias="+username+" AND passwd="+password+";";
            System.out.println(SQL);
                // statement
                Statement statement = connection.createStatement();

                // Resultset that holds the result of our query, important that the query only returns one user.
                ResultSet rs = statement.executeQuery(
                      "SELECT * " + "FROM User WHERE alias="+username+" AND passwd="+password+";"
                );

                
                // Nevermind this
                //JSONArray array = resultSetToJson(rs);
                //System.out.println(array.getJSONObject(0).get("passwd"));


                // debug message,
                System.out.println("Checking user");

                // This checks if we hav data.
                if (!rs.isBeforeFirst() ) {
                    System.out.println("No data");
                    statement.close();
                    rs.close();
                    connection.close();
                    throw new Exception("Wrong username or Password");
                }


                // While there are rows left
                while(rs.next()){
                    toReturn.setFirstName(rs.getString("firstname"));
                    toReturn.setLastName(rs.getString("lastname"));
                    toReturn.setPassword(rs.getString("passwd"));
                    toReturn.setUserName(rs.getString("alias"));
                    toReturn.setpNumber(rs.getLong("pnumber"));
                    toReturn.setUserType(rs.getInt("usertype"));
                    toReturn.setStreet(rs.getString("street"));
                    toReturn.setZip(rs.getInt("zip"));



                    
                    int userID = Integer.parseInt(rs.getString("userID"));
                    toReturn.setUserID(userID);
                    //MAYRA End
                    break;
                }

                rs.close();
                statement.close();
                connection.close();
                return toReturn;

        	}catch(SQLException e){
        		e.printStackTrace();

        	}
        	return toReturn;
    	}
    
    //simple function that prints all locations (rooms) to the console
    //Takes no arguments and has no return type
    public void GetLocations() throws Exception
    {
        	//Method that prints all rooms 
            // we have to catch potential SQLExceptions
            try(Connection connection = getConnection()){
            	
                System.out.println("Connection Established");
                String SQL="SELECT location FROM Room;";
                System.out.println(SQL);
                    // statement
                    Statement statement = connection.createStatement();

                    // Resultset that holds the result of our query, important that the query only returns one user.
                    ResultSet rs = statement.executeQuery(SQL);
                    while(rs.next()){
                    	String location = rs.getString("location");
                    	System.out.println(location);
                    }

                    rs.close();
                    statement.close();
                    connection.close();

            }catch(SQLException e){
                e.printStackTrace();

            }

        }
    
    
    //Refactored by Potus to return ArrayList<Booking> instead of Booking[]
    //Takes the userId and returns all the booking the this user has made
    //Return Arraylist<Booking> 
    public ArrayList<Booking> GetUserBookings(int userId)
    {
        	ArrayList<Booking> bookings = new ArrayList<>();
        	String bID;
        	//Method that prints all Bookings
            // we have to catch potential SQLExceptions
            try(Connection connection = getConnection()){
            	
                System.out.println("Connection Established");
                String SQL="SELECT * FROM Bookings WHERE userid = '"+userId+"';";
                System.out.println(SQL);
                    // statement
                    Statement statement = connection.createStatement();

                    // Resultset that holds the result of our query, important that the query only returns one user.
                    ResultSet rs = statement.executeQuery(SQL);

                    while(rs.next()){
                    	Booking booking = new Booking();
                    	booking.setbID(rs.getInt("bID"));
                    	booking.setuserid(rs.getInt("userid"));
                    	booking.setroomID(rs.getInt("roomID"));
                    	booking.setbdate(rs.getString("bdate"));
                    	booking.setbStart(rs.getString("bStart"));
                    	booking.setbEnd(rs.getString("bEnd"));
                    	bookings.add(booking);
                    }
                for(Booking booking : bookings){
                    booking.setUser(getUser(booking.getuserid()));
                    booking.setRoom(getRoom(booking.getroomID()));
                }
                    return bookings;

            }catch(SQLException e){
                e.printStackTrace();

            }

            return null;
        }
    
    //This method takes the roomID (primary key) and returns the corresponding location (room name)
    //Returns the location as a string
    public String GetRoomLocation(int roomID) throws Exception
    {
        	//Method get the location using the roomID
        	String toReturn = "";
            // we have to catch potential SQLExceptions
            try(Connection connection = getConnection()){
            	
                System.out.println("Connection Established");
                String SQL="SELECT location FROM Room WHERE roomID ='"+roomID+"';";
                System.out.println(SQL);
                    // statement
                    Statement statement = connection.createStatement();

                    // Resultset that holds the result of our query, important that the query only returns one user.
                    ResultSet rs = statement.executeQuery(SQL);
                    while(rs.next()){
                    	toReturn = rs.getString("location");
                    	System.out.println(toReturn);
                    }

                    rs.close();
                    statement.close();
                    connection.close();

            }catch(SQLException e){
                e.printStackTrace();

            }
            return toReturn;
    }
    
    
    public ArrayList<String> getLocations(){
        ArrayList<String> locations = new ArrayList<>();
        locations.add("N/A");

        try(Connection connection = getConnection()){

            System.out.println("Connection Established");
            String SQL="SELECT location FROM Room ORDER BY Room.location ASC ";
            System.out.println(SQL);
            // statement
            Statement statement = connection.createStatement();

            // Resultset that holds the result of our query, important that the query only returns one user.
            ResultSet rs = statement.executeQuery(SQL);
            while (rs.next()){
                locations.add(rs.getString("location"));
                //System.out.println(locations);
            }

            rs.close();
            statement.close();
            connection.close();

        }catch(SQLException e){
            e.printStackTrace();

        }

        return locations;
    }
    
    
    //Returns the RoomID (primary key) from the SQL database for a given location (room name)
    public int GetRoomID(String location) throws Exception
    {
        	//Method that prints all rooms 
        	int roomID = 0;
            // we have to catch potential SQLExceptions
            try(Connection connection = getConnection()){
            	
                System.out.println("Connection Established");
                String SQL="SELECT roomID FROM Room WHERE location ='"+location+"';";
                System.out.println(SQL);
                    // statement
                    Statement statement = connection.createStatement();

                    // Resultset that holds the result of our query, important that the query only returns one user.
                    ResultSet rs = statement.executeQuery(SQL);
                    while(rs.next()){
                    	String tmp = rs.getString("roomID");
                    	roomID = Integer.parseInt(tmp);
                    	System.out.println(roomID);
                    }

                    rs.close();
                    statement.close();
                    connection.close();

            }catch(SQLException e){
                e.printStackTrace();

            }
            return roomID;
        }

    //create a class for room, use this class as a return type for BookRoom analog to "loginAndGetUser" 
    // room registration
    // Output confirmation or error.
    // Input  User, Building, Room, Date, Start time, End time, Purpose
    // Check  if input is valid 
    // Check  If room is available
    // Create Room object
    // SQL server
    // http://sql.smallwhitebird.com
    // user team9, password team9
    
    //STRUCTURE for Bookings
    //bid		NULL	INT
    //userId	NULL	INT
    //roomId	NULL	INT
    //bDate		NULL	date
    //bStart	NULL	time
    //bEnd		NULL	time
    
    //Books a room in the SQL database using string input
    //returns a boolean
    public boolean BookRoom(int userId, int roomId, String bDate, String bStart, String bEnd) throws Exception
    {
      
        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){

            System.out.println("Room Registration Connection Established");

            // statement
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO Bookings " +
            			 "(userId, roomId, bDate, bStart, bEnd)" +
            			 " Values ('"+userId+ "','"+roomId+"','"+bDate+"','"+bStart+"','"+bEnd+"')";
            System.out.println("SQL string: "+sql); 
       
            statement.executeUpdate(sql);
           
            statement.close();
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        } 
     return false;   
  } //end public User BookRoom

    //This method removes a booking from the SQL database based on the booking ID (primary key)
    //Returns boolean
    public boolean removeRoomBooking(int bID) throws Exception
    {
      
        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){

            System.out.println("Room De-Registration Connection Established");

            // statement
            Statement statement = connection.createStatement();
            
            String sql = "DELETE FROM Bookings WHERE" +
            			 "(bID ='"+bID+"');";
            System.out.println("SQL string: "+sql); 
       
            statement.executeUpdate(sql);
           
            statement.close();
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        } 
     return false;   
  } //end public User BookRoom
  //userid 		int(11)
  //alias		varchar(20)
  //passwd		varchar(255)
  //firstname 	varchar(20)
  //lastname	varchar(30)
  //pNumber		bigint(20)
  //usertype	varchar(30)
  //street		varchar(30)
  //zip			int(11)
  
  //Adds a user to the SQL Database based on String, long and integer input
  //returns boolean  
  public boolean RegisterUser(String alias, String passwd, String firstname, String lastname, long pNumber, int usertype, String street, int zip)
  {
          
	  // we have to catch potential SQLExceptions
      try(Connection connection = getConnection()){
    	  
    	  
    	  System.out.println("User Registration Connection Established");

    	  // statement
    	  Statement statement = connection.createStatement();

    	  String sql = "INSERT INTO User" +
                   	"(alias, passwd, firstname, lastname, pNumber, usertype, street, zip)" +
                   	" Values ('"+alias+ "','"+passwd+"','"+firstname+"','"+lastname+"',"+pNumber+","+usertype+",'"+street+"',"+zip+")";
      
    	  //System.out.println("SQL string: "+sql); 
           
    	  statement.executeUpdate(sql);
               
          statement.close();
          connection.close();
          System.out.println("true");
          return true;
       }catch(SQLException e){
            e.printStackTrace();
       }

    return false;

    }//end public User RegisterUser
  
  //This method adds a room to the SQL database based in String and integer arguments
  //Returns boolean
  public boolean RegisterRoom(String location, String roomSize, int hasProjector, int hasWhiteBoard, int hasCoffeeMachine)
  {
	  	//roomID int(11)
	    //location char(30)
	    //roomSize char(30)
	    //hasProjector int
	    //hasWhiteBoard int
	    //hasCoffeMachine int
          
	  // we have to catch potential SQLExceptions
      try(Connection connection = getConnection()){
    	  
    	  
    	  System.out.println("User resister room Connection Established");

    	  // statement
    	  Statement statement = connection.createStatement();

    	  String sql = "INSERT INTO Room " +
                   	"(location, roomSize, hasProjector, hasWhiteBoard, hasCoffeeMachine)" +
                   	" Values ('"+location+ "','"+roomSize+"','"+hasProjector+"','"+hasWhiteBoard+"','"+hasCoffeeMachine+"')";
      
    	  System.out.println("SQL string: "+sql); 
           
    	  statement.executeUpdate(sql);
               
          statement.close();
          connection.close();
          return true;
       }catch(SQLException e){
            e.printStackTrace();
       }

    return false;

    }//end public User RegisterRoom

    /**
     * GET ROOMS, COMPOSE ROOM QUERY and TOTAL NUMBER OF ROOMS 
     */
    public int totalNumberOfRooms(){
        int j = 0;
        try(Connection connection = getConnection()){

            System.out.println("\nConnection Established");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Room NATURAL JOIN Bookings;"
            );

            while (rs.next()) {
                j++;
            }
            System.out.println("Rooms total:"+j);

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return j;
    }

    public String composeRoomQuery(String location,
                                   boolean isSmall,
                                   boolean isMedium,
                                   boolean isLarge,
                                   boolean hasProjector,
                                   boolean hasWhiteboard,
                                   boolean hasCoffeeMachine,
                                   String bookingDate,
                                   String timeStart,
                                   String timeEnd){

        System.out.println("inside composemethod");
        if(location=="N/A") location ="";

        String query = "SELECT * FROM Room WHERE Room.roomID> 0 ";

        query += " AND( ";
        if(isSmall && isMedium && isLarge) query += "roomSize = 'S' OR roomSize = 'M' OR roomSize = 'L' )";
        else if(isSmall){
            if(isMedium) query += "roomSize = 'S' OR roomSize = 'M' )";
            else if(isLarge) query += "roomSize = 'S' OR roomSize = 'L' )";
            else  query += "roomSize = 'S')";
        }
        else if(isMedium){
            if(isLarge) query += "roomSize = 'M' OR roomSize = 'L')";
            else query += "roomSize = 'M' )";
        }
        else if(isLarge) query += "roomSize = 'L')";
        else{
            query = "SELECT * FROM Room WHERE Room.roomID> 0 ";
        }


        if(location != null && !location.isEmpty()){query += " AND location = " + "'"+location+"'";}
        System.out.println(query);
        if(hasProjector){query += " AND hasProjector = 1 ";}
        if(hasWhiteboard){query += " AND hasWhiteboard = 1 ";}
        if(hasCoffeeMachine){query += " AND hasCoffeeMachine = 1 ";}
        System.out.println(query);

        System.out.println(query);
        query += "AND Room.roomID NOT IN(SELECT Bookings.roomID FROM Bookings WHERE Bookings.bdate = '" + bookingDate +
                "' AND ( " +
                "('"+timeStart+"' BETWEEN Bookings.bStart AND Bookings.bEnd or '" + timeEnd + "' BETWEEN Bookings.bStart " +
                "AND Bookings.bEnd) OR ( '"+timeStart+"' < Bookings.bStart AND '" + timeEnd + "' > Bookings.bEnd)))";



        System.out.println(query);
        return query + ";";
    }

    public ArrayList<Room> getRooms(String query){ 	//or maybe it should accept a booking class, or room+time+date
        // returns string for now, just for testing

        int roomsNumber = totalNumberOfRooms();

//        BookedRoom[] bridgeRooms = new BookedRoom[roomsNumber];
        ArrayList<Room> bridgeRooms = new ArrayList<>();


        try(Connection connection = getConnection()){

            System.out.println("\nConnection Established\n");

            String locationOfRoom = "", sizeOfRoom = "";
            int projector = 0, whiteboard = 0, coffee = 0, rID = 0, bID=0, uID=0;
            Date date = null, startTime= null, endTime = null;

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnsNumber = rsMetaData.getColumnCount();



            int k = 0;
            System.out.println(query);

            if (!rs.isBeforeFirst() ) {
                return null;

            }

            while (rs.next()) {
                Room room = new Room();
                room.setRoomSize(rs.getString("roomSize"));
                room.setLocation(rs.getString("location"));
                room.setHasProjector(rs.getInt("hasProjector"));
                room.setHasWhiteboard(rs.getInt("hasWhiteboard"));
                room.setHasCoffeeMachine(rs.getInt("hasCoffeeMachine"));
                room.setRoomID(rs.getInt("roomID"));
                bridgeRooms.add(room);
                
                rs.getInt("roomId");
            }
            return bridgeRooms;


        }catch(SQLException e){
            e.printStackTrace();
        }


        return null;
    }
  





    /**
     *Editing and removing a user from the database
     */

    public void updateUser(ArrayList<User> users) throws SQLException{
        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");
            connection.setAutoCommit(false);
            for(User user : users) {

                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        "UPDATE User SET alias = '" + user.getUserName() + "',passwd ='" + user.getPassword() +
                                "',firstname = '" + user.getFirstName() + "',lastname = '" + user.getLastName() +
                                "',pNumber = '" + user.getpNumber() + "',usertype = '" + user.getUserType() +
                                "',street = '" + user.getStreet() + "',zip = '" + user.getZip() +
                                "' WHERE userID= '" + user.getUserID() + "'"
                );
            }
            connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

    }

    public void deleteUser(User user) throws SQLException{
        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "DELETE FROM User WHERE userID= '" + user.getUserID() + "'"
            );
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
    

    /**
     * Searching through users, rooms and bookings in the database
     * Created by Filip on 13/11/15
     */

    public ArrayList<User> getUsers(User user){
        ArrayList<User> userArrayList = new ArrayList<>();

        String userIdQuery ="User.userID = " + user.getUserID()+" AND ";
        String userTypeQuery = "AND usertype LIKE '%%" + user.getUserType()+"%%'";
        String pNumberQuery  = "AND pNumber LIKE '%%"+user.getpNumber()+"%%'";
        String zipQuery      = "AND zip LIKE '%%"+user.getZip()+"%%'";

        if(user.getUserID()==0) { userIdQuery=""; }
        if(user.getUserType()==15  ){ userTypeQuery=""; }
        if(user.getpNumber() == 0) { pNumberQuery = "";}
        if(user.getZip() == 0){zipQuery = "";}

        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM User WHERE "+userIdQuery+" alias LIKE '%%"+
                            user.getUserName()+"%%' AND passwd LIKE '%%"+user.getPassword()+"%%' AND firstname LIKE '%%"+
                            user.getFirstName()+"%%' AND lastname LIKE '%%"+ user.getLastName()+"%%' AND street LIKE '%%"
                            +user.getStreet()+"%%' "+zipQuery+" "+pNumberQuery+" "+userTypeQuery+";"
            );
            System.out.println("SELECT * FROM User WHERE "+userIdQuery+" alias LIKE '%%"+
                    user.getUserName()+"%%' AND passwd LIKE '%%"+user.getPassword()+"%%' AND firstname LIKE '%%"+
                    user.getFirstName()+"%%' AND lastname LIKE '%%"+ user.getLastName()+"%%' AND street LIKE '%%"
                    +user.getStreet()+"%%' "+zipQuery+" "+pNumberQuery+" "+userTypeQuery+";");
            while (rs.next()) {
                User tmpUser = new User();

                tmpUser.setUserID(rs.getInt("userID"));
                tmpUser.setUserName(rs.getString("alias"));
                tmpUser.setPassword(rs.getString("passwd"));
                tmpUser.setFirstName(rs.getString("firstname"));
                tmpUser.setLastName(rs.getString("lastname"));
                tmpUser.setpNumber(rs.getLong("pNumber"));
                tmpUser.setUserType(rs.getInt("usertype"));
                tmpUser.setStreet(rs.getString("street"));
                tmpUser.setZip(rs.getInt("zip"));

                userArrayList.add(tmpUser);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        //list of columns in the USER table
        //userID, alias, passwd, firstname, lastname, pNumber, usertype, street, zip

        return userArrayList;
    }

    public ArrayList<Room> getRooms(Room room, boolean small, boolean medium, boolean large){
        ArrayList<Room> roomArrayList = new ArrayList<>();

        String roomID = ""+room.getRoomID();

        String query="SELECT * FROM Room WHERE roomID = '"+roomID+"' ";
        if(room.getRoomID()==0){ query="SELECT * FROM Room WHERE roomID > 0"; }

        if(small||medium||large) query += " AND( ";
        if(small && medium && large) query += "roomSize = 'S' OR roomSize = 'M' OR roomSize = 'L' )";
        else if(small){
            if(medium) query += "roomSize = 'S' OR roomSize = 'M' )";
            else if(large) query += "roomSize = 'S' OR roomSize = 'L' )";
            else  query += "roomSize = 'S')";
        }
        else if(medium){
            if(large) query += "roomSize = 'M' OR roomSize = 'L')";
            else query += "roomSize = 'M' )";
        }
        else if(large) query += "roomSize = 'L')";
//        else{
//            query = "SELECT * FROM Room WHERE Room.roomID> 0 ";
//        }

        if(room.getHasProjector()>0 || room.getHasWhiteboard()>0 || room.getHasCoffeeMachine()>0) query += " AND( ";
        if(room.getHasProjector()>0 && room.getHasWhiteboard()>0 && room.getHasCoffeeMachine()>0){
            query += "hasProjector = '1' AND hasWhiteboard = '1' AND hasCoffeeMachine = '1' )";
        }
        else if(room.getHasProjector()>0){
            if(room.getHasWhiteboard()>0) query += "hasProjector = '1' AND hasWhiteboard = '1' )";
            else if(room.getHasCoffeeMachine()>0) query += "hasProjector = '1' AND hasCoffeeMachine = '1' )";
            else  query += "hasProjector = '1')";
        }
        else if(room.getHasWhiteboard()>0){
            if(room.getHasCoffeeMachine()>0) query += "hasWhiteboard = '1' AND hasCoffeeMachine = '1')";
            else query += "hasWhiteboard = '1' )";
        }
        else if(room.getHasCoffeeMachine()>0) query += "hasCoffeeMachine = '1')";
//        else{
//            query = "SELECT * FROM Room WHERE Room.roomID> 0 ";
//        }
        System.out.println(query);
        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Room tmpRoom = new Room();

                tmpRoom.setRoomSize(rs.getString("roomSize"));
                tmpRoom.setLocation(rs.getString("location"));
                tmpRoom.setHasProjector(rs.getInt("hasProjector"));
                tmpRoom.setHasWhiteboard(rs.getInt("hasWhiteboard"));
                tmpRoom.setHasCoffeeMachine(rs.getInt("hasCoffeeMachine"));
                tmpRoom.setRoomID(rs.getInt("roomID"));

                roomArrayList.add(tmpRoom);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return roomArrayList;
    }

    public ArrayList<Booking> getBookings(Booking booking){
        ArrayList<Booking> bookingArrayList = new ArrayList<>();

        String bID = ""+ booking.getbID();
        if(booking.getbID()==0){ bID=""; }

        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Bookings WHERE bID = '"+bID+"' AND userid LIKE '%%" +
                            booking.getuserid()+"%%' AND roomID LIKE '%%"+booking.getroomID()+"%%' AND bdate LIKE '%%"
                            +booking.getBdate()+"%%' AND bstart LIKE '%%"+booking.getBStart()+"%%' AND bEnd LIKE '%%"
                            +booking.getBEnd()+"%%'"
            );

            while (rs.next()) {
                Booking tmpBooking = new Booking();

                booking.setbID(rs.getInt("bID"));
                booking.setuserid(rs.getInt("userid"));
                booking.setroomID(rs.getInt("roomID"));
                booking.setbdate(rs.getString("bdate"));
                booking.setbStart(rs.getString("bStart"));
                booking.setbEnd(rs.getString("bEnd"));

                bookingArrayList.add(tmpBooking);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return bookingArrayList;
    }
     /*
     *
     * Get user and return ArrayList of Past Bookings
     *
     */

    public ArrayList<Booking> getPastBookings(User user){
        ArrayList<Booking> bookingArrayList = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat currentHour = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(date);
        String formattedHour = currentHour.format(date);

        try(Connection connection = getConnection()){


            System.out.println("\nUser Connection Established\n");
            System.out.println("getting Past Bookings");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Bookings WHERE userID = "+user.getUserID()+
                            " AND bDate<='"+formattedDate+"' "
            );


            while (rs.next()) {
                Booking booking = new Booking();

                booking.setbID(rs.getInt("bID"));
                booking.setuserid(rs.getInt("userid"));
                booking.setroomID(rs.getInt("roomID"));
                booking.setbdate(rs.getString("bdate"));
                booking.setbStart(rs.getString("bStart"));
                booking.setbEnd(rs.getString("bEnd"));

                bookingArrayList.add(booking);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("got in MySqlUtil Past Bookings");
//        for(Booking booking : bookingArrayList){
//            System.out.println("in loop");
//            booking.setUser(getUser(booking.getuserid()));
//            booking.setRoom(getRoom(booking.getroomID()));
//        }

        return bookingArrayList;
    }
   



    /**
     *
     * Get user and return ArrayList of Future Booking  
     */

    public ArrayList<Booking> getFutureBookings(User user){
        ArrayList<Booking> bookingArrayList = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        SimpleDateFormat currentHour = new SimpleDateFormat("HH:mm:ss");
        String formattedHour = currentHour.format(date);

        try(Connection connection = getConnection()){


            System.out.println("\nUser Connection Established\n");


            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    //  "SELECT * FROM Bookings WHERE userID = "+user.getUserID()+
                    // " AND bDate=>'"+formattedDate+"' "
                    //   );

                    "SELECT * FROM Bookings WHERE userID = "+user.getUserID()+
                            " AND bDate>='"+formattedDate+"' "
            );

            while (rs.next()) {
                Booking booking = new Booking();

                booking.setbID(rs.getInt("bID"));
                booking.setuserid(rs.getInt("userid"));
                booking.setroomID(rs.getInt("roomID"));
                booking.setbdate(rs.getString("bdate"));
                booking.setbStart(rs.getString("bStart"));
                booking.setbEnd(rs.getString("bEnd"));

                bookingArrayList.add(booking);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("got future bookings in mySqlUtil");
//        for(Booking booking : bookingArrayList){
//            System.out.println("in loop");
//            booking.setUser(getUser(booking.getuserid()));
//            booking.setRoom(getRoom(booking.getroomID()));
//        }

        return bookingArrayList;
    }

   

    /**
     * SEARCHING USER, ROOM AND BOOKING METHODS 
     */
    public ArrayList<Booking> getBookings(Room room){
        ArrayList<Booking> bookingArrayList = new ArrayList<>();

        try(Connection connection = getConnection()){

            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Bookings WHERE roomID = " + room.getRoomID() + ""
            );

            while (rs.next()) {
                Booking booking = new Booking();

                booking.setbID(rs.getInt("bID"));
                booking.setuserid(rs.getInt("userid"));
                booking.setroomID(rs.getInt("roomID"));
                booking.setbdate(rs.getString("bdate"));
                booking.setbStart(rs.getString("bStart"));
                booking.setbEnd(rs.getString("bEnd"));

                bookingArrayList.add(booking);
            }

            for(Booking booking : bookingArrayList){
                booking.setUser(getUser(booking.getuserid()));
                booking.setRoom(getRoom(booking.getroomID()));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }



        return bookingArrayList;
    }

    public User getUser(int userID){
        User user = new User();

        try(Connection connection = getConnection()){

//            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM User WHERE userID ="+userID+""
            );

            while (rs.next()) {
                user.setUserID(rs.getInt("userID"));
                user.setUserName(rs.getString("alias"));
                user.setPassword(rs.getString("passwd"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setpNumber(rs.getLong("pNumber"));
                user.setUserType(rs.getInt("usertype"));
                user.setStreet(rs.getString("street"));
                user.setZip(rs.getInt("zip"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return user;
    }


    public Room getRoom(int roomID){

        Room room = new Room();



        try(Connection connection = getConnection())
        {

//            System.out.println("\nUser Connection Established\n");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Room WHERE roomID = " + roomID + ""
            );

            while (rs.next()) {
                room.setRoomSize(rs.getString("roomSize"));
                room.setLocation(rs.getString("location"));
                room.setHasProjector(rs.getInt("hasProjector"));
                room.setHasWhiteboard(rs.getInt("hasWhiteboard"));
                room.setHasCoffeeMachine(rs.getInt("hasCoffeeMachine"));
                room.setRoomID(rs.getInt("roomID"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return room;
    }
   

    /*
     * @param username
     * @return
     */
    public boolean isUsernameAvailable(String username){

        try(Connection connection = getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM User WHERE alias ='"+username+"';");

            if (!rs.isBeforeFirst() ) {
                System.out.println("true");
                return true;
            }
            else {
                System.out.println("false");
                return false;
            }




        }catch(SQLException e){

        }
        return false;

    }

    /**
     *
     * Edits the bookings inside of the database
     *
     */
    public void editBooking(Booking booking){
        // JDBC driver name and database URL
        //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        //static final String DB_URL = "jdbc:mysql://sql.smallwhitebird.com:3306/BookingSystem";

        System.out.println("Inside editBooking");
        //Connection connection = null;

        try(Connection connection = getConnection()){
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to a selected database...");
            System.out.println("Connected database successfully...");

            // statement
            Statement statement = connection.createStatement();

            // Execute a query
            System.out.println("Creating statement...");
            //statement = connection.createStatement();

            String sql = "UPDATE Bookings SET roomID = '"+booking.getroomID()+
                    "', bdate= '"+booking.getBdate()+"', bStart='"+booking.getBStart()+
                    "',bEnd='"+booking.getBEnd()+"', userid='"+booking.getuserid()+"' WHERE bid="+booking.getbID();
            statement.executeUpdate(sql);

        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        System.out.println("Team9 Goodbye!");
        
    }

    
    public boolean BookRoom(Room roomObj) throws Exception
    {
        
        //roomID int(11)
        //location char(30)
        //roomSize char(30)
        //hasProjector int
        //hasWhiteBoard int
        //hasCoffeMachine int
        String location = roomObj.getLocation();
        String roomSize= roomObj.getRoomSize();
        int hasProjector=roomObj.getHasProjector();
        int hasWhiteBoard=roomObj.getHasWhiteboard();
        int hasCoffeeMachine=roomObj.getHasCoffeeMachine();

        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){


            System.out.println("User resister room Connection Established");

            // statement
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO Room " +
                    "(location, roomSize, hasProjector, hasWhiteBoard, hasCoffeeMachine)" +
                    " Values ('"+location+ "','"+roomSize+"','"+hasProjector+"','"+hasWhiteBoard+"','"+hasCoffeeMachine+"')";

            System.out.println("SQL string: "+sql);

            statement.executeUpdate(sql);

            statement.close();
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }
    
   
    //changes the information in a room stored in the SQL database with the contents of a room objects
    //Returns boolean
    public boolean updateRoom(Room room) {
        
        //This method takes the data in the room object and updates the database with the data in the room object

        //SQL ROOM structure
        //roomID int(11)
        //location char(30)
        //roomSize char(30)
        //hasProjector int
        //hasWhiteBoard int
        //hasCoffeMachine int

        int roomID = room.getRoomID();
        int hasCoffeeMachine = room.getHasCoffeeMachine();
        int hasProjector = room.getHasProjector();
        int hasWhiteBoard = room.getHasWhiteboard();
        String roomSize = room.getRoomSize();
        String location = room.getLocation();

        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){

            System.out.println("Edit room Connection Established");

            Statement statement = connection.createStatement();


            String sql = "UPDATE Room " +
                    "SET " +
                    "location='"+location+"',roomSize='"+roomSize+"',hasProjector='"+hasProjector+"'"
                    +",hasWhiteBoard='"+hasWhiteBoard+"',hasCoffeeMachine='"+hasCoffeeMachine+"' "+
                    "WHERE roomID='"+roomID+"'";

            System.out.println("SQL string: "+sql);

            statement.executeUpdate(sql);

            statement.close();
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();

        }

        return false;
//        return false;
    }
    
    
    //This method deletes a room with the RoomID
    //Returns boolean
    public boolean deleteRoom(Room room) {
        
        //This method deletes the Room using the roomID.
        //It also deletes all booking related to this roomID to retain database integrity
        //Note that the bookings have to be deleted before the room is delete due to the
        //foreign key restraints

        //SQL ROOM structure
        //roomID int(11)
        //location char(30)
        //roomSize char(30)
        //hasProjector int
        //hasWhiteBoard int
        //hasCoffeMachine int

        int roomID = room.getRoomID();
        String sql = "";

        // we have to catch potential SQLExceptions
        try(Connection connection = getConnection()){

            System.out.println("Delete room Connection Established");

            Statement statement = connection.createStatement();

            //Delete all booking with this roomID from SQL Database to retain integrity
            sql = "DELETE FROM Bookings "+
                    "WHERE roomID='"+roomID+"';";

            System.out.println("SQL string: "+sql);
            statement.executeUpdate(sql);

            //Delete room from SQL Database
            sql = "DELETE FROM Room "+
                    "WHERE roomID='"+roomID+"';";

            System.out.println("SQL string: "+sql);

            statement.executeUpdate(sql);

            statement.close();
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    //Takes User object, Room object and date strings and then books a room and returns a Booking object
    //Returns Booking object
    public Booking BookRoomNew(User userObj, Room roomObj, String bDate, String bStart, String bEnd) throws Exception
    {
        //Note that using strings to transfer date information can cause SQL errors if the strings are not formatted
        //correctly.

        //TODO How should a series of bookings be handled? BookedRoom[] BookedRoom(roomObj, intbID, userId, date, start, stop, Xtimes)??
        //TODO There should only be one booking with the same Date. needs to be checked, until then use the final intbID

        int iBid = 0;
        int intbID = 0;
        int userId = userObj.getUserID();
        int roomId = roomObj.getRoomID();
        java.util.Date date;
        java.util.Date start;
        java.util.Date stop;

        //creating Date objects so that a BookedRoom object can be returned
        //Warnings suppressed due to Date methods being needed due to the Object definition
        //BookedRoom(roomObj, int, int, date, date, date);

        //try{
        //	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //	date = format.parse(bDate);
        //System.out.println(date);
        //    @SuppressWarnings("deprecation")
        //	DateFormat formatTime = new SimpleDateFormat("H:m");
        //	start = date;
        //	start = formatTime.parse(bStart);
        //	start.setDate(date.getDate());
        //	start.setMonth(date.getMonth());
        //	start.setYear(date.getYear());

        //	stop = date;
        //	stop = formatTime.parse(bEnd);
        //	stop.setDate(date.getDate());
        //	stop.setMonth(date.getMonth());
        //	stop.setYear(date.getYear());
        //} catch(ParseException e){
        //        e.printStackTrace();
        //        System.out.println("Wrong date format for SQL");
        //        throw new RuntimeException(e);
        //}
        //


        try(Connection connection = getConnection()){

            System.out.println("Room Registration Connection Established");

            // statement
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO Bookings " +
                    "(userId, roomId, bDate, bStart, bEnd)" +
                    " Values ('"+userId+ "','"+roomId+"','"+bDate+"','"+bStart+"','"+bEnd+"')";

            System.out.println("SQL string: "+sql);
            statement.executeUpdate(sql);

            String bID = "SELECT bID FROM Bookings WHERE " +
                    "userId = '"+userId+"' AND roomId = '"+roomId+"' AND bDate = '"+bDate+"' AND bStart = '"+bStart+"' AND bEnd = '"+bEnd+"';";

            System.out.println("SQL string: "+bID);

            ResultSet rs = statement.executeQuery(bID);
            while(rs.next()){
                String tmp = rs.getString("bID");
                intbID = Integer.parseInt(tmp);
                System.out.println(intbID);
            }

            Booking toReturn = new Booking(intbID, userObj.getUserID(), roomObj.getRoomID(), bDate, bStart, bEnd);
            rs.close();
            statement.close();
            connection.close();

            return toReturn;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    } 

    /**
     * @param img File object pointing to img to upload to database
     * @param user User to associate with this image
     */
        public void uploadImage(File img,User user){


            try(Connection connection = getConnection()){


                Statement statement = connection.createStatement();

                String checkQuery = "SELECT * FROM Blobs WHERE Blobs.userid = "+user.getUserID()+";";

                ResultSet rs = statement.executeQuery(checkQuery);

                if (!rs.isBeforeFirst()){
                    PreparedStatement ps = null;
                    String insertPicture = "INSERT INTO Blobs(userid,pic) VALUES (?,?)";
                    try{
                        connection.setAutoCommit(false);
                        FileInputStream fileInputStream = new FileInputStream(img);
                        ps = connection.prepareStatement(insertPicture);
                        ps.setInt(1, user.getUserID());
                        ps.setBinaryStream(2,fileInputStream,(int)img.length());
                        ps.executeUpdate();
                        connection.commit();

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                else {

                    PreparedStatement ps = null;
                    String insertPicture = "UPDATE Blobs SET Blobs.pic = ? WHERE Blobs.userid =?;";
                    try{
                        connection.setAutoCommit(false);
                        FileInputStream fileInputStream = new FileInputStream(img);
                        ps = connection.prepareStatement(insertPicture);
                        ps.setInt(2, user.getUserID());
                        ps.setBinaryStream(1,fileInputStream,(int)img.length());
                        ps.executeUpdate();
                        connection.commit();

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }catch(SQLException e){
                    e.printStackTrace();
            }


        }

    /**
     * @param user
     * @return the image downloaded is returned as bufferedImage
     * @throws IOException
     */

        public BufferedImage downloadImage(User user) throws IOException{



            try(Connection connection = getConnection()){

                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT Blobs.pic FROM Blobs WHERE Blobs.userid=" + user.getUserID() + "");
                if(!rs.isBeforeFirst()){
                    return null;
                }
                while(rs.next()){

                    if(rs.getBlob("pic") != null)
                    {

                        Blob blob = rs.getBlob("pic");
                        InputStream inputStream = blob.getBinaryStream();
                        BufferedImage bufferedImage = ImageIO.read(inputStream);
                        return bufferedImage;

                    }

                }



            }catch (SQLException e){
                e.printStackTrace();

            }


            return null;
        }

    public User getUserFromId(int id){


        try(Connection connection = getConnection()){

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from User WHERE User.userID == '" + id + "';");

            if(!rs.isBeforeFirst()){

                return null;
            }
            while(rs.next())
            {
                User toReturn = new User();
                toReturn.setFirstName(rs.getString("firstname"));
                toReturn.setLastName(rs.getString("lastname"));
                toReturn.setUserName(rs.getString("alias"));
                toReturn.setPassword(rs.getString("passwd"));
                toReturn.setZip(rs.getInt("zip"));
                toReturn.setpNumber(rs.getLong("pNumber"));
                toReturn.setStreet(rs.getString("street"));
                toReturn.setUserID(rs.getInt("userID"));
                toReturn.setUserType(rs.getInt("usertype"));
                return toReturn;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkBookingTime(User user, String time){
        try(Connection connection = getConnection()){

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "Select * from bookings WHERE userid='"+user.getUserID() + "' "
            );

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}









