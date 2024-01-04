package com.example.vardiyauygulamasi.classes;

import java.io.Serializable;

public class User implements Serializable {
    public long tCKN;
    public int roleId;
    public int departmentId;
    public String name;
    public String surName;
    public String password;


    public User(long tCKN, int roleId, int departmentId, String name, String surName, String password) {
        this.tCKN = tCKN;
        this.roleId = roleId;
        this.departmentId = departmentId;
        this.name = name;
        this.surName = surName;
        this.password = password;
    }
}
