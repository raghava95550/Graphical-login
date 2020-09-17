package com.example.graphicallogin;

public class Users
{
    String userId;
    String name;
    String emailId;
    String address;
    String phoneNo;
    String passwd;

    public Users()
    {

    }

    public Users(String userId,String name, String emailId, String address, String phoneNo,String passwd) {
        this.userId=userId;
        this.name = name;
        this.emailId = emailId;
        this.address = address;
        this.phoneNo = phoneNo;
        this.passwd = passwd;
    }

    public String getUserId()
    {
        return  userId;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPasswd() {return passwd; }

}
