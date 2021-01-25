package com.aangps01.hallochat.Model;

public class User {

    private String id;
    private String email;
    private String username;
    private String role;
    private String imageURL;

    public User() {
    }

    public User(String id, String email, String username, String role, String imageURL) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
