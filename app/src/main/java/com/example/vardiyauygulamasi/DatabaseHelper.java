package com.example.vardiyauygulamasi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.vardiyauygulamasi.classes.Department;
import com.example.vardiyauygulamasi.classes.Role;
import com.example.vardiyauygulamasi.classes.Shift;
import com.example.vardiyauygulamasi.classes.User;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Attributes;

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

    private static final String CreateUsers = "CREATE TABLE users (TCKN LONG PRIMARY KEY, " +
            "RoleId int NOT NULL," +
            "DepartmentId int NOT NULL," +
            "Name text NOT NULL," +
            "SurName text NOT NULL," +
            "Password text DEFAULT ''," +
            "FOREIGN KEY (RoleId) REFERENCES roles (ID)," +
            "FOREIGN KEY (DepartmentId) REFERENCES departments (ID)" +
            ")";


    //DEFAULT (strftime ('%d-%m-%d %H-%M-%s', 'now', 'localtime'))

    private static final String CreateShifts = "CREATE TABLE shifts (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserTCKN long NOT NULL," +
            "DepartmentId int NOT NULL," +
            "Date date DEFAULT (strftime ('%d-%m-%d %H-%M-%s', 'now', 'localtime'))," +
            "BeginTime time," +
            "EndTime time," +
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
                "VALUES (1, 1, 1, 'Admin', 'Admin', '0000')");
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
                "VALUES (1, 1, 1, 'Admin', 'Admin', '0000')");
    }

    //==============================================================================================

    public void deleteUserTable()
    {
        write.execSQL("DROP TABLE users");
        write.execSQL("DROP TABLE shifts");

    }

    public void createUserTable(){
        write.execSQL(CreateUsers);
        write.execSQL(CreateShifts);

        write.execSQL("INSERT INTO users " +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES (1, 1, 1, 'Admin', 'Admin', '0000')");
    }

    //==============================================================================================

    public boolean userIsHave (long tCKN){
        Cursor crs = read.rawQuery("SELECT * FROM users WHERE TCKN = ("+tCKN+")", null);

        return crs.moveToFirst();
    }

    public boolean userRoleIsAdmin (int userRoleId){
        Cursor crs = read.rawQuery("SELECT * FROM roles WHERE ID = ("+userRoleId+")", null);

        String role = crs.getString(1);

        return role.equals("Admin") ? true : false;
    }

    public String whatIsUserDepartment (int id){
        Cursor crs = read.rawQuery("SELECT * FROM departments WHERE ID = ("+id+")", null);

        return crs.getString(1);
    }

    //================================== User Operations ===================================//

    public void userRegister (long tCKN, int roleId, int departmentId, String name, String surName, String password){
        write.execSQL("INSERT INTO users" +
                "(TCKN, RoleId, DepartmentId, Name, SurName, Password)" +
                "VALUES ("+tCKN+", "+roleId+", "+departmentId+", '"+name+"', '"+surName+"', '"+password+"')");

        Toast.makeText(cnt, "Kullanici Basariyla Kaydedildi !", Toast.LENGTH_SHORT).show();
    }

    public boolean userLogin (long tCKN, String password){
        Cursor crs = read.rawQuery("SELECT * FROM users " +
                "WHERE TCKN = ("+tCKN+") AND Password = ('"+password+"')", null);

        return crs.moveToFirst();
    }

    public void userUpdate(long tCKN, int roleId, int departmentId, String name, String surName, String password){
        write.execSQL("UPDATE users " +
                "SET RoleId = "+roleId+"," +
                "DepartmentId = "+departmentId+"," +
                "Name = '"+name+"'," +
                "SurName = '"+surName+"'," +
                "Password = '"+password+"'" +
                "WHERE TCKN = "+tCKN+" ");

        Toast.makeText(cnt, "Kullanıcı Güncelleme İşlemi Başarılı !", Toast.LENGTH_SHORT).show();
    }

    public void userRemove(long tCKN){
        write.execSQL("DELETE FROM users WHERE TCKN = "+tCKN+" ");

        Toast.makeText(cnt, "Kullanıcı Silme İşlemi başarılı !", Toast.LENGTH_SHORT).show();
    }

    public User userDetails(long tCKN){
        Cursor crs = read.rawQuery("SELECT * FROM users " +
                "WHERE TCKN = ("+tCKN+")", null);

        if (crs.moveToFirst()){
            return new User(
                    crs.getLong(0), // TCKN
                    crs.getInt(1), // RoleId
                    crs.getInt(2), // DepartmentId
                    crs.getString(3), // Name
                    crs.getString(4), // SurName
                    crs.getString(5) // Password
            );
        }

        else{
            return null;
        }
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> kullanicilar = new ArrayList<>();

        Cursor crs = read.rawQuery("SELECT * FROM users", null);

        while(crs.moveToNext()){
            kullanicilar.add(new User(
                    crs.getLong(0), // TCKN
                    crs.getInt(1), // RoleId
                    crs.getInt(2), // DepartmentId
                    crs.getString(3), // Name
                    crs.getString(4), // SurName
                    crs.getString(5) // Password
            ));
        }

        crs.close();

        return kullanicilar;
    }

    public ArrayList<User> getAllUserByDepartments(int departmentId){
        ArrayList<User> kullanicilar = new ArrayList<>();

        Cursor crs = read.rawQuery("SELECT * FROM users " +
                "WHERE DepartmentId = "+departmentId+" ", null);

        while(crs.moveToNext()){
            kullanicilar.add(new User(
                    crs.getLong(0), // TCKN
                    crs.getInt(1), // RoleId
                    crs.getInt(2), // DepartmentId
                    crs.getString(3), // Name
                    crs.getString(4), // SurName
                    crs.getString(5) // Password
            ));
        }

        crs.close();

        return kullanicilar;
    }

    //================================== Department Operations ===================================//
    
    public void departmentCreate(String departmentName){
        write.execSQL("INSERT INTO departments" +
                "(DepartmentName)" +
                "VALUES ('"+departmentName+"')");

        Toast.makeText(cnt, "Departman Oluşturma İşlemi Başarılı !", Toast.LENGTH_SHORT).show();
    }
    
    public void departmentRemove(int id){
        write.execSQL("DELETE FROM departments WHERE ID = "+id+" ");

        Toast.makeText(cnt, "Departman Silme İşlemi Başarılı !", Toast.LENGTH_SHORT).show();
    }
    
    public void departmentUpdate(int id, String departmentName){
        write.execSQL("UPDATE departments " +
                "SET DepartmentName = '"+departmentName+"'" +
                "WHERE ID = "+id+"");

        Toast.makeText(cnt, "Departman Güncelleme İşlemi Başarılı !", Toast.LENGTH_SHORT).show();
    }

    public boolean departmentIsHave(String departmentName){
        Cursor crs = read.rawQuery("SELECT * FROM departments WHERE DepartmentName = '"+departmentName+"' ", null);

        return crs.moveToFirst();
    }

    public ArrayList<Department> getAllDepartments(){
        ArrayList<Department> departmanlar = new ArrayList<>();

        Cursor crs = read.rawQuery("SELECT * FROM departments", null);

        while (crs.moveToNext()){
            departmanlar.add(new Department(
                    crs.getInt(0),
                    crs.getString(1)
            ));
        }

        crs.close();

        return departmanlar;
    }

    public ArrayList<Role> getAllRoles(){
        ArrayList<Role> roller = new ArrayList<>();

        Cursor crs = read.rawQuery("SELECT * FROM roles", null);

        while(crs.moveToNext()){
            roller.add(new Role(
                    crs.getInt(0),
                    crs.getString(1)
            ));
        }

        crs.close();

        return roller;
    }

    //================================== Shift Operations ===================================//

    public void shiftCreate(long tCKN, int departmentId, String date, String beginTime, String endTime){
        write.execSQL("INSERT INTO shifts " +
                "(UserTCKN, DepartmentId, Date, BeginTime, EndTime)" +
                "VALUES ("+tCKN+", "+departmentId+", '"+date+"', '"+beginTime+"', '"+endTime+"')");

        Toast.makeText(cnt, "Vardiya Ekleme İşlemi Başarıyla Tamamlandı !", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Shift> getAllShiftByDateAndDepartmentId(String date, int departmentId){
        ArrayList<Shift> vardiyalar = new ArrayList<>();

        Cursor crs = read.rawQuery("SELECT * FROM shifts " +
                "JOIN users on users.TCKN = shifts.UserTCKN " +
                "WHERE Date = '"+date+"' AND shifts.DepartmentId = "+departmentId+" ", null);

        while(crs.moveToNext()){
            int id = crs.getInt(0);
            long userTckn = crs.getLong(1);
            int shfDepartmentId = crs.getInt(2);
            String dateStr = crs.getString(3);
            String beginTimeStr = crs.getString(4);
            String endTimeStr = crs.getString(5);
            String userName = crs.getString(crs.getColumnIndexOrThrow("Name"));
            String userSurname = crs.getString(crs.getColumnIndexOrThrow("SurName"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date shfDate = null;

            try{
                shfDate = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date beginTime = null;
            Date endTime = null;

            try{
                beginTime = timeFormat.parse(beginTimeStr);
                endTime = timeFormat.parse(endTimeStr);
            } catch (ParseException e ){
                e.printStackTrace();
            }

            vardiyalar.add(new Shift(
                    id,
                    userTckn,
                    shfDepartmentId,
                    shfDate,
                    beginTime,
                    endTime,
                    userName,
                    userSurname
            ));
        }

        crs.close();

        return vardiyalar;
    }

    public void shiftRemove(int id){
        write.execSQL("DELETE FROM shifts WHERE ID = "+id+"");

        Toast.makeText(cnt, "Vardiya Silme İşlemi Başarılı !", Toast.LENGTH_SHORT).show();
    }

//    public ArrayList<Shift> getAllShift(){
//        ArrayList<Shift> vardiyalar = new ArrayList<>();
//
//        Cursor crs = read.rawQuery("SELECT * FROM shifts ", null);
//
//        while(crs.moveToNext()){
//            int id = crs.getInt(0);
//            long userTckn = crs.getLong(1);
//            int shfDepartmentId = crs.getInt(2);
//            String dateStr = crs.getString(3);
//            String beginTimeStr = crs.getString(4);
//            String endTimeStr = crs.getString(5);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date shfDate = null;
//
//            try{
//                shfDate = dateFormat.parse(dateStr);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//            Date beginTime = null;
//            Date endTime = null;
//
//            try{
//                beginTime = timeFormat.parse(beginTimeStr);
//                endTime = timeFormat.parse(endTimeStr);
//            } catch (ParseException e ){
//                e.printStackTrace();
//            }
//
//            vardiyalar.add(new Shift(
//                    id,
//                    userTckn,
//                    shfDepartmentId,
//                    shfDate,
//                    beginTime,
//                    endTime
//            ));
//        }
//
//        crs.close();
//
//        return vardiyalar;
//    }
}
