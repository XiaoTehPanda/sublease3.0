package edu.uga.csci.xiao.subleased20;

public class User
{

    private String password;
    private String email;
    private String phoneNum;

    public User(){}

    public User(String password, String email, String phoneNum)
    {
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }




}
