package com.honoursproject.radoslawburkacki.familytrackingapplication.Model;


public class Family {

    Long familyId;
    Long creatorId;
    String familyName;
    String joiningPassword;

    public Family(Long creatorId, String familyName, String joiningPassword) {
        this.creatorId = creatorId;
        this.familyName = familyName;
        this.joiningPassword = joiningPassword;
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
}
