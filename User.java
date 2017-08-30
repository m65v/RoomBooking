package com.team9.bookingsystem;

import com.team9.bookingsystem.Components.SearchableObject;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.awt.image.BufferedImage;

/**
 */
public class User implements SearchableObject {

    private MysqlUtil _db;
    private int	   userID;
    private long   pNumber;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private int userType;
    private String street;
    private String email;
    private int    zip;
    private boolean downloading = false;



    private BufferedImage avatar;

    // Default Constructor
    public User(){}

    // New User with Parameters
    public User(int userID,
                String userName,
                String password,
                String firstName,
                String lastName,
                int userType,
                String street,
                long pNumber,int zip,BufferedImage avatar)
    {
        _db = new MysqlUtil();
        this.userID = userID;
        this.userName  = userName;
        this.password  = password;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.userType  = userType;
        this.street    = street;
        this.zip       = zip;
        this.pNumber = pNumber;
        this.avatar  = avatar;
    }

    // Copy Constructor
    public User(User user)
    {
        _db = new MysqlUtil();

        this.userName  = user.userName;
        this.password  = user.password;
        this.firstName = user.firstName;
        this.lastName  = user.lastName;
        this.userType  = user.userType;
        this.street    = user.street;
        this.zip       = user.zip;
    }

    public int getUserID() {
        return userID;
    }

    public long getpNumber() {
        return pNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getUserType() {
        return userType;
    }

    public String getStreet() {
        return street;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getZip() {
        return zip;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setpNumber(long pNumber) {
        this.pNumber = pNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public boolean isDownloading(){
        return downloading;
    }

    
    public void downloadAvatar(){
        User thisUser = this;
        downloading = true;
        Service<BufferedImage> getUserAvatar = new Service<BufferedImage>() {
            @Override
            protected Task<BufferedImage> createTask() {
                Task<BufferedImage> task = new Task<BufferedImage>() {
                    @Override
                    protected BufferedImage call() throws Exception {
                        MysqlUtil util = new MysqlUtil();
                        BufferedImage image = util.downloadImage(thisUser);
                        if(image != null){
                            return image;
                        }
                        return null;
                    }

                };
                return task;
            }
        };
        getUserAvatar.start();
        getUserAvatar.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(getUserAvatar.getValue() != null){
                    thisUser.setAvatar(getUserAvatar.getValue());
                    System.out.println(getUserAvatar.getValue().toString());
                    System.out.println("downloaded avatar");
                    downloading = false;
                }
            }
        });
        getUserAvatar.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("avatar download failed");
                downloading = false;
            }
        });

    }

    public static boolean isValidInput(String userName,
                                      String password,
                                      String firstName,
                                      String lastName,
                                      String userType,
                                      String street,
                                      int zip)
    {
        if(userName.length() > 20) return false;
        for(char c:firstName.toCharArray()){
            if (Character.isDigit(c)) return false;
        }
        for(char c:lastName.toCharArray()){
            if (Character.isDigit(c)) return false;
        }
        return true;
    }

    public String toString(){
        String toReturn = "";
        toReturn += String.format("\n//- %s \n",getFirstName());
        toReturn += String.format("//- %s \n",getLastName());
        toReturn += String.format("//- %s \n",getUserName());
        toReturn += String.format("//- %s \n",getPassword());
        toReturn += String.format("//- %s \n",getpNumber());
        toReturn += String.format("//- %s \n",getUserType());
        toReturn += String.format("//- %s \n",getStreet());
        toReturn += String.format("//- %s \n",getZip());
        toReturn += String.format("//- %s \n",getUserID());
        return toReturn;
    }


}
