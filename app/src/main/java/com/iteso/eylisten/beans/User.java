package com.iteso.eylisten.beans;

public class User {

    private String name;
    private String password;
    private boolean isLooged;

    public User() {
        setName("");
        setPassword("");
        setLooged(false);
    }

    public User(String name, String password, boolean isLooged) {
        this.name = name;
        this.password = password;
        this.isLooged = isLooged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLooged() {
        return isLooged;
    }

    public void setLooged(boolean looged) {
        isLooged = looged;
    }
}
