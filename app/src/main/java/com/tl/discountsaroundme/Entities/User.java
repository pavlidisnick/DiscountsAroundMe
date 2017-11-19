package com.tl.discountsaroundme.Entities;

/**
 * Created by rezu on 10/11/2017.
 */

public class User {
    private String name;
    private String email;
    private String userType;
    private String image;

    public User(){};

    public User (String name , String email, String userType,String image){
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.image = image;
    }
    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUserType() {return userType;}

    public void setUserType(String userType) {this.userType = userType;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }
}
