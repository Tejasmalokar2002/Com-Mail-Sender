package com.example.Eidiko.Mail.Sender.model;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String data; // New field for 'Data'
    private List<String> fileNames; // Updated to handle multiple files

    // Constructor
    public User(String name, String email, String data, List<String> fileNames) {
        this.name = name;
        this.email = email;
        this.data = data; // Include data
        this.fileNames = fileNames; // Store multiple filenames
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}

