package com.lipssmackingparadise.reviewapp.model;

/**
 * Created by User on 28/10/2014.
 */
public class userDetails {

    public String username;
    public String first_name;
    public String last_name;
    public String dob;
    public String email;
    public String password;
    public String _id;

    public userDetails (String username, String first_name, String last_name, String dob, String email, String password, String _id) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String phone) {
        this.dob = phone;
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

    public void setPassword(String email) {
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


}
