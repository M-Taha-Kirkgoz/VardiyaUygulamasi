package com.example.vardiyauygulamasi.Dtos;

import java.sql.Time;
import java.util.Date;

public class Shift {
    public int id;
    public long userTckn;
    public int departmentId;
    public Date date;
    public Date beginTime;
    public Date endTime;
    public String userName;
    public String userSurname;

    public Shift(int id, long userTckn, int departmentId, Date date, Date beginTime, Date endTime, String userName, String userSurname){
        this.id = id;
        this.userTckn = userTckn;
        this.departmentId = departmentId;
        this.date = date;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.userName = userName;
        this.userSurname = userSurname;
    }

}
