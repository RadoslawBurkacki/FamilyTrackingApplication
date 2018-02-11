package com.honoursproject.radoslawburkacki.familytrackingapplication.Model;


import java.io.Serializable;

public class User implements Serializable{


    private long id;
    private String email;
    private String password;
    private String fname;
    private String lname;

    public User(){

    }

    public User(Long id, String email, String password, String fname, String lname) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                '}';
    }
}
