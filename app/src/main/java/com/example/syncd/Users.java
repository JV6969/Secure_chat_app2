package com.example.syncd;

public class Users {
    String profiledp;
    String email;
    String username;
    String password;
    String userid;
    String lastmessgae;
    String status;

    public Users(String id, String name2, String emaill2, String pass2, String profiledp,String status){
        this.userid = id;
        this.username = name2;
        this.email = emaill2;
        this.password = pass2;
        this.profiledp = profiledp;
        this.status=status;
    }

    public String getProfiledp() {
        return profiledp;
    }

    public void setProfiledp(String profiledp) {
        this.profiledp = profiledp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmessgae() {
        return lastmessgae;
    }

    public void setLastmessgae(String lastmessgae) {
        this.lastmessgae = lastmessgae;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
