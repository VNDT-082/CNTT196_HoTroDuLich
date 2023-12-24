package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class User_ implements Serializable {
    private String Identifier;
    private Date Created;
    private String Password;
    private String User_UID;
    private String IdDocument;
    private String avarta;
    private boolean author;
    private boolean active;

    public String getIdDocument() {
        return IdDocument;
    }

    public void setIdDocument(String idDocument) {
        IdDocument = idDocument;
    }

    private boolean gen;
    private String fullName;
    private LocalDate dateOfBirth;
    private LocalDateTime lastConnect;
    private ArrayList<Travel> travels;

    public ArrayList<Travel> getTravels() {
        return travels;
    }

    public String getAvarta() {
        return avarta;
    }

    public boolean isAuthor() {
        return author;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isGen() {
        return gen;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getLastConnect() {
        return lastConnect;
    }



    public String getIdentifier() {
            return Identifier;
        }

    public Date getCreated() {
        return Created;
    }

    public String getUser_UID() {
        return User_UID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setIdentifier(String identifier) {
            Identifier = identifier;
        }

    public void setCreated(Date created) {
        Created = created;
    }

    public void setUser_UID(String user_UID) {
        User_UID = user_UID;
    }

    public void setAvarta(String avarta) {
        this.avarta = avarta;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setGen(boolean gen) {
        this.gen = gen;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLastConnect(LocalDateTime lastConnect) {
        this.lastConnect = lastConnect;
    }

    public void setTravels(ArrayList<Travel> travels) {
        this.travels = travels;
    }

    public  User_(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        this.User_UID = currentDateTime.format(formatter).toString();
        this.IdDocument=currentDateTime.format(formatter).toString();
        this.lastConnect=currentDateTime;

    }
    public User_(String identifier, Date created, String password, String user_UID, String avarta, String IdDocument,
                 boolean author, boolean active, boolean gen, String fullName, LocalDate dateOfBirth,
                 LocalDateTime lastConnect) {
        Identifier = identifier;
        Created = created;
        Password = password;
        User_UID = user_UID;
        this.IdDocument=IdDocument;
        this.avarta = avarta;
        this.author = author;
        this.active = active;
        this.gen = gen;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.lastConnect = lastConnect;
    }
}

