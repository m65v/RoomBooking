package com.team9.bookingsystem;

import java.awt.image.BufferedImage;


 public class Booking {
	//bID
    //userid
    //roomID
    //bdate
    //bStart
    //bEnd

    private MysqlUtil _db;
    private int bID;
    private int userid;
    private int roomID;
    private String bdate;
    private String bStart;
    private String bEnd;
    private User user;
    private Room room;

    // Default Constructor
    public Booking(){}




    // New Room with Parameters
    public Booking(int bID, int userid, int roomID, String bdate, String bStart, String bEnd)
    {
        _db = new MysqlUtil();
        this.bID = bID;
        this.userid  = userid;
        this.roomID  = roomID;
        this.bdate = bdate;
        this.bStart  = bStart;
        this.bEnd = bEnd;
    }

    public Booking(int bID, int userid, int roomID, String bdate, String bStart, String bEnd, User user, Room room)
    {
        _db = new MysqlUtil();
        this.bID = bID;
        this.userid  = userid;
        this.roomID  = roomID;
        this.bdate = bdate;
        this.bStart  = bStart;
        this.bEnd = bEnd;
        this.user = user;
        this.room = room;
    }

    //public final StringProperty roomLocationProperty() { return this.room.getLocation(); }

    public int getbID() {
    	return bID;
    }

    public void setbID(int bID) {
        this.bID = bID;
    }

    public int getuserid() {
        return userid;
    }

    public void setuserid(int userid) {
        this.userid = userid;
    }

    public int getroomID () {
        return roomID;
    }

    public void setroomID(int roomID) {
        this.roomID = roomID;
    }
    public String getBdate() {
        return this.bdate;
    }

    public void setbdate(String bdate) {
        this.bdate =  bdate;
    }

    public String getBStart() {
        return this.bStart;
    }

    public void setbStart(String bStart) {
        this.bStart = bStart;
    }

    public String getBEnd() {
        return this.bEnd;
    }

    public void setbEnd(String bEnd) {
        this.bEnd = bEnd;
    }

    /**
     * User setters/getters
     */
    public User getUser(){ return this.user; }

    public void setUser(User user){ this.user = user; }

    public int getUserUserID(){ return user.getUserID(); }

    public void setUserUserID(int userID){ this.user.setUserID(userID); }

    public long getUserPNumber(){ return this.user.getpNumber(); }

    public void setUserPNumber(long pNumber){ this.user.setpNumber(pNumber); }

    public String getUserName(){ return this.user.getUserName(); }

    public void setUserName(String userName){ this.user.setUserName(userName); }

    public String getUserPassword(){ return user.getPassword(); }

    public void setUserPassword(String userPassword){ this.user.setPassword(userPassword); }

    public String getUserFirstName(){ return this.user.getFirstName(); }

    public void setUserFirstname(String userFirstname){ this.user.setFirstName(userFirstname); }

    public String getUserLastName(){ return this.user.getLastName(); }

    public void setUserLastName(String userLastName){ this.user.setLastName(userLastName); }

    public int getUserType(){ return this.user.getUserType(); }

    public void setUserType(int userType){ this.user.setUserType(userType); }

    public String getUserStreet(){ return this.user.getStreet(); }

    public void setUserStreet(String street){ this.user.setStreet(street); }

    public int getUserZip(){ return this.user.getZip(); }

    public void setUserZip(int zip){ this.user.setZip(zip); }

    public BufferedImage getUserAvatar(){ return this.user.getAvatar(); }

    public void setUserAvatar(BufferedImage avatar){ this.user.setAvatar(avatar); }
   

    /**
     * Room setters/getters
     */
    public Room getRoom(){ return this.room; }

    public void setRoom(Room room){ this.room = room; }

    public int getRoomRoomID(){ return this.room.getRoomID(); }

    public void setRoomRoomID(int roomID){ this.room.setRoomID(room.getRoomID()); }

    public String getRoomLocation(){ return this.room.getLocation(); }

    public void setRoomLocation(String location){ this.room.setLocation(location); }

    public String getRoomSize(){ return this.room.getRoomSize(); }

    public void setRoomSize(String roomSize){ this.room.setRoomSize(roomSize); }

    public int getRoomHasWhiteboard(){ return this.room.getHasWhiteboard(); }

    public void setRoomHasWhiteboard(int hasWhiteboard){ this.room.setHasWhiteboard(hasWhiteboard); }

    public int getRoomHasProjector(){ return this.room.getHasProjector(); }

    public void setRoomHasProjector(int hasProjector){ this.room.setHasProjector(hasProjector); }

    public int getRoomHasCoffeeMachine(){ return this.room.getHasCoffeeMachine(); }

    public void setRoomHasCoffeeMachine(int hasCoffeeMachine){ this.room.setHasCoffeeMachine(hasCoffeeMachine); }
    

    public static boolean isValidInput(int bID, int userid, String roomID, String bdate, String bStart, String bEnd)
    {
        //TODO check for USER and ROOM ID
    	if(roomID.length() > 30) {
        	return false;
        }
        if(bdate.length() > 30) {
        	return false;
        }
        if(bStart.length() > 30) {
        	return false;
        }
        if(bEnd.length() > 30) {
        	return false;
        }
        
    return true;    
    }

    public String toString(){
//    	String location="";
//    	MysqlUtil BookingDB = new MysqlUtil();
//    	try{
//    		location = BookingDB.GetRoomLocation(getroomID());
//    	}catch(Exception e){
//			e.printStackTrace();
//		}
        String toReturn = "";
        toReturn += String.format("Booking ID- %s \n",getbID());
        toReturn += String.format("Room ID %d \n", roomID);
        toReturn += String.format("Dser ID %d \n", userid);
        toReturn += String.format("Start- %s \n", getBStart());
        toReturn += String.format("End- %s \n", getBEnd());
        
        return toReturn;
    }


}










