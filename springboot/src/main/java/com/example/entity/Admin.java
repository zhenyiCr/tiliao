package com.example.entity;

public class Admin extends Account {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String contact;

    public String getAvatar() {return avatar;}
    public void setAvatar(String avatar) {this.avatar = avatar;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

}
