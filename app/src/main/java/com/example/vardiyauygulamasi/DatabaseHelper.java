package com.example.vardiyauygulamasi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.w3c.dom.Text;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shifts.db";
    private static final int Version = 1;
    private final Context cnt;

    private final SQLiteDatabase read = this.getReadableDatabase();
    private final SQLiteDatabase write = this.getWritableDatabase();

    private static final String CreateRoles = "CREATE TABLE roles (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "RoleName text NOT NULL" +
            ")";

    private static final String CreateDepartments = "CREATE TABLE departments (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "DepartmentName text NOT NULL" +
            ")";

    private static final String CreateUsers = "CREATE TABLE users (TCKN INTEGER PRIMARY KEY, " +
            "RoleId int NOT NULL," +
            "DepartmentId int NOT NULL," +
            "Name text NOT NULL," +
            "SurName text NOT NULL," +
            "Password text DEFAULT ''," +
            "FOREIGN KEY (RoleId) REFERENCES roles (ID)," +
            "FOREIGN KEY (DepartmentId) REFERENCES departments (ID)" +
            ")";

    private static final String CreateShifts = "CREATE TABLE shifts (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserTCKN int," +
            "DepartmentId int," +
            "Date datetime DEFAULT (strftime ('%d-%m-%d %H-%M-%s', 'now', 'localtime'))," +
            "BeginTime text," +
            "EndTime text," +
            "FOREIGN KEY (UserTCKN) REFERENCES users (TCKN)," +
            "FOREIGN KEY (DepartmentId) REFERENCES departments (ID)" +
            ")";

    //==============================================================================================

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, Version);
        cnt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateRoles);
        db.execSQL(CreateDepartments);
        db.execSQL(CreateUsers);
        db.execSQL(CreateShifts);

        db.execSQL("INSERT INTO roles (RoleName) VALUES('Admin'),('Calisan')");
        db.execSQL("INSERT INTO departments (DepartmentName) VALUES ('Deneme')");
        db.execSQL("INSERT INTO users " +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES (012, 1, 1, 'Admin', 'Admin', '0000')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(CreateRoles);
        db.execSQL(CreateDepartments);
        db.execSQL(CreateUsers);
        db.execSQL(CreateShifts);

        db.execSQL("INSERT INTO roles (RoleName) VALUES(('Admin'),('Calisan'))");
        db.execSQL("INSERT INTO departments (DepartmentName) VALUES ('Deneme')");
        db.execSQL("INSERT INTO users " +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES (012, 1, 1, 'Admin', 'Admin', '0000')");
    }

    //==============================================================================================

    public void deleteUserTable(){
        write.execSQL("DROP TABLE users");
    }

    public void createUserTable(){
        write.execSQL(CreateUsers);

        write.execSQL("INSERT INTO users " +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES (012, 1, 1, 'Admin', 'Admin', '0000')");
    }

    //==============================================================================================

    public boolean userIsHave (int tCKN){
        Cursor crs = read.rawQuery("SELECT * FROM users WHERE TCKN = ("+tCKN+")", null);

        return crs.moveToFirst();
    }

    public boolean userRoleIsAdmin (int id){
        Cursor crs = read.rawQuery("SELECT * FROM roles WHERE ID = ("+id+")", null);

        String role = crs.getString(1);

        return role == "Admin";
    }

    public String whatIsUserDepartment (int id){
        Cursor crs = read.rawQuery("SELECT * FROM departments WHERE ID = ("+id+")", null);

        return crs.getString(1);
    }

    public void userRegister (int tCKN, int roleId, int departmentId, String name, String surName, String password){
        write.execSQL("INSERT INTO users" +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES ("+tCKN+", "+roleId+", "+departmentId+", '"+name+"', '"+surName+"', '"+password+"')");
    }

    public boolean userLogin (int tCKN, String password){
        Cursor crs = read.rawQuery("SELECT * FROM users " +
                "WHERE TCKN = ("+tCKN+") AND Password = ('"+password+"')", null);

        return crs.moveToFirst();
    }
}
