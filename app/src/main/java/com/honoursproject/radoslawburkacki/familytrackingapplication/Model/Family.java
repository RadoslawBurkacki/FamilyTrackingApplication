package com.honoursproject.radoslawburkacki.familytrackingapplication.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Family implements Serializable {

    Long familyId;
    Long creatorId;
    String familyName;
    String joiningPassword;
    List<User> familyMembers = new ArrayList<User>();

    public Family(Long creatorId, String familyName, String joiningPassword, List<User> familyMembers) {
        this.creatorId = creatorId;
        this.familyName = familyName;
        this.joiningPassword = joiningPassword;
        this.familyMembers = familyMembers;
    }

    public Family(){

    }

    public Long getId() {
        return familyId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getJoiningPassword() {
        return joiningPassword;
    }

    public void setJoiningPassword(String joiningPassword) {
        this.joiningPassword = joiningPassword;
    }

    public List<User> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<User> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public void addFamilyMember(User u){
        familyMembers.add(u);
    }

    @Override
    public String toString() {
        String a="";
        for(User u : familyMembers){
            a= a+ ", familyMembers=" + u.toString();
        }
        return "Family{" +
                "familyId=" + familyId +
                ", creatorId=" + creatorId +
                ", familyName='" + familyName + '\'' +
                ", joiningPassword='" + joiningPassword + '\'' +
                a+




                '}';


    }
}
