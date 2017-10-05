package com.honoursproject.radoslawburkacki.familytrackingapplication;


import java.io.Serializable;

public class User implements Serializable{


    private long id;
    private String email;
    private String password;
    private String fname;
    private String lname;

    public User(){

    }

    public User(String email, String password, String fname, String lname) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
