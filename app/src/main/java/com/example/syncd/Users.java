package com.example.syncd;

public class Users {
    String id;
    String name;
    String email;
    String imageuri;
    String status;

    // Required empty constructor for Firebase
    public Users() {
    }

    // Your main constructor
    public Users(String id, String name, String email, String imageuri, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageuri = imageuri;
        this.status = status;
    }

    // --- Add Getters and Setters for all fields ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImageuri() { return imageuri; }
    public void setImageuri(String imageuri) { this.imageuri = imageuri; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}