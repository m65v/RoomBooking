package com.team9.bookingsystem;

import com.team9.bookingsystem.Components.SearchableObject;


public class Room implements SearchableObject {

    private MysqlUtil _db;

    private int roomID;
    private String location;
    private String roomSize;
    private int hasProjector;
    private int hasWhiteboard;
    private int hasCoffeeMachine;

    // Default Constructor
    public Room() {
    }

    // New Room with Parameters
    public Room(int roomID,
                String location,
                String roomSize,
                int hasProjector,
                int hasWhiteboard,
                int hasCoffeeMachine) {
        _db = new MysqlUtil();
        this.roomID = roomID;
        this.location = location;
        this.roomSize = roomSize;
        this.hasProjector = hasProjector;
        this.hasWhiteboard = hasWhiteboard;
        this.hasCoffeeMachine = hasCoffeeMachine;
    }

    // Copy Constructor
    public Room(Room room) {
        _db = new MysqlUtil();

        this.roomID = room.roomID;
        this.location = room.location;
        this.roomSize = room.roomSize;
        this.hasProjector = room.hasProjector;
        this.hasWhiteboard = room.hasWhiteboard;
        this.hasCoffeeMachine = room.hasCoffeeMachine;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getHasProjector() {
        return hasProjector;
    }

    public int getHasWhiteboard() {
        return hasWhiteboard;
    }

    public int getHasCoffeeMachine() {
        return hasCoffeeMachine;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setHasProjector(int hasProjector) {
        this.hasProjector = hasProjector;
    }

    public void setHasWhiteboard(int hasWhiteboard) {
        this.hasWhiteboard = hasWhiteboard;
    }

    public void setHasCoffeeMachine(int hasCoffeeMachine) {
        this.hasCoffeeMachine = hasCoffeeMachine;
    }





    public static boolean isValidInput(String location,
                                      String roomSize,
                                      int hasProjector,
                                      int hasWhiteboard,
                                      int hasCoffeeMachine)
    {
        if(location.length() > 30){ return false; }
        if(roomSize.length() > 30){ return false; }
        if(hasProjector > 1 || hasProjector < 0){ return false; }
        if(hasWhiteboard > 1 || hasWhiteboard < 0){ return false; }
        if(hasCoffeeMachine > 1 || hasCoffeeMachine < 0){ return false; }
        return true;
    }

    public String toString(){
        String toReturn = "";
        toReturn += String.format("\n//- %s \n",getLocation());
        toReturn += String.format("//- %s \n",getRoomSize());
        toReturn += String.format("//- %s \n",getHasProjector());
        toReturn += String.format("//- %s \n",getHasWhiteboard());
        toReturn += String.format("//- %s \n",getHasCoffeeMachine());



        return toReturn;
    }



}
