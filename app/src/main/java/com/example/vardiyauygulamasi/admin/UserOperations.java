package com.example.vardiyauygulamasi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vardiyauygulamasi.DatabaseHelper;
import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.classes.Department;
import com.example.vardiyauygulamasi.classes.DepartmentsAdapter;
import com.example.vardiyauygulamasi.classes.Role;
import com.example.vardiyauygulamasi.classes.RoleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserOperations extends AppCompatActivity {
    private int selectedDepartmentId, selectedRoleId;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_operations);

        Button createUser = findViewById(R.id.kullanici_olustur);

        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Department> departments = db.getAllDepartments();
        ArrayList<Role> roles = db.getAllRoles();

        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.user_create);

                Spinner departmentsSpinner = findViewById(R.id.departments);
                Spinner roleSpinner = findViewById(R.id.roles);
                Button userCreate = findViewById(R.id.kullanici_kaydet);

                DepartmentsAdapter adapter = new DepartmentsAdapter(UserOperations.this, departments);
                departmentsSpinner.setAdapter(adapter);

                departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedDepartmentId = departments.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                RoleAdapter adapterRole = new RoleAdapter(UserOperations.this, roles);
                roleSpinner.setAdapter(adapterRole);

                roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                        selectedRoleId = roles.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent){

                    }
                });

                userCreate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        EditText tCNo = findViewById(R.id.user_tc_no);
                        EditText userName = findViewById(R.id.user_name);
                        EditText userSurname = findViewById(R.id.user_surname);
                        EditText userPassword = findViewById(R.id.user_pass);

                        if (tCNo.length() == 0 || userName.length() == 0 || userSurname.length() == 0){
                            Toast.makeText(UserOperations.this, "Şifre Hariç Tüm Alanlar Doldurulmalı !", Toast.LENGTH_LONG).show();
                        }

                        else if (tCNo.length() != 11){
                            Toast.makeText(UserOperations.this, "Lütfen Geçerli Bir TC Kimlik Numarası Giriniz !", Toast.LENGTH_LONG).show();
                        }

                        else if (db.userIsHave(Long.parseLong(tCNo.getText().toString()))){
                            Toast.makeText(UserOperations.this, tCNo.getText().toString() + " Bu Kullanıcı Daha Önce Kayıt Edilmiş !", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            long tCKN =  Long.parseLong(tCNo.getText().toString());
                            db.userRegister(tCKN, selectedRoleId, selectedDepartmentId, userName.getText().toString(), userSurname.getText().toString(), userPassword.getText().toString());
                        }
                    }
                });

            }

        });
    }
}