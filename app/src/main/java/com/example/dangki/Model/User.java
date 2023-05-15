package com.example.dangki.Model;

import java.util.Date;
import java.text.SimpleDateFormat;


public class User {
    String id, name, phoneNumber, gender, userName, email, photo, password;
    Date birthdate;
    public User(){}

    public User(String id, String name, String phoneNumber, String gender, String userName,
                String email, String photo, String password, Date birthdate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userName = userName;
        this.email = email;
        this.photo = photo;
        this.password = password;
        this.birthdate = birthdate;
    }

    public User(String name, String phoneNumber, String gender, String userName, Date birthdate,
                String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userName = userName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
    }

    public User(String name, String phoneNumber, String gender, String userName, String email,
                String photo, Date birthdate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userName = userName;
        this.email = email;
        this.photo = photo;
        this.birthdate = birthdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail(){return this.email;}

    public void setEmail(String email){this.email = email;}

    public String getPhoto(){return this.photo;}

    public void setPhoto(String photo){this.photo = photo;}

}
