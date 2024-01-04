package com.example.vardiyauygulamasi.admin;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.vardiyauygulamasi.classes.User;
import com.example.vardiyauygulamasi.classes.UserAdapter;

import java.util.ArrayList;

public class UserOperations extends AppCompatActivity {
    private int selectedDepartmentId, selectedRoleId;
    private long selectedTCKN;
    DatabaseHelper db;

    ArrayList<Department> departments;
    ArrayList<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_operations);

        Button createUser = findViewById(R.id.kullanici_olustur);
        Button updateUser = findViewById(R.id.kullanici_guncelle);

        db = new DatabaseHelper(getApplicationContext());

        departments = db.getAllDepartments();
        roles = db.getAllRoles();

        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.user_create);

                Spinner departmentsSpinner = findViewById(R.id.departments);
                Spinner roleSpinner = findViewById(R.id.roles);
                Button userCreate = findViewById(R.id.kullaniciyi_sil);

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

        updateUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.user_update);

                ArrayList<User> users = db.getAllUsers();

                Spinner userSpinner = findViewById(R.id.users);
                Spinner departmentSpinner = findViewById(R.id.departments);
                Spinner roleSpinner = findViewById(R.id.roles);

                Button userRemove = findViewById(R.id.kullaniciyi_sil);
                Button userUpdate = findViewById(R.id.kullaniciyi_guncelle);

                EditText tCNo = findViewById(R.id.user_tc_no);
                EditText userName = findViewById(R.id.user_name);
                EditText userSurname = findViewById(R.id.user_surname);
                EditText userPassword = findViewById(R.id.user_pass);


                UserAdapter userAdapter = new UserAdapter(UserOperations.this, users);
                userSpinner.setAdapter(userAdapter);

                DepartmentsAdapter departmentAdapter = new DepartmentsAdapter(UserOperations.this, departments);
                departmentSpinner.setAdapter(departmentAdapter);

                RoleAdapter roleAdapter = new RoleAdapter(UserOperations.this, roles);
                roleSpinner.setAdapter(roleAdapter);

                userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedTCKN = users.get(position).tCKN;
                        selectedRoleId = users.get(position).roleId;
                        selectedDepartmentId = users.get(position).departmentId;

                        tCNo.setText(String.valueOf(selectedTCKN));
                        tCNo.setEnabled(false);
                        userName.setText(users.get(position).name);
                        userSurname.setText(users.get(position).surName);
                        userPassword.setText(users.get(position).password);

                        roleSpinner.setSelection(getPositionById(selectedRoleId, true));
                        departmentSpinner.setSelection(getPositionById(selectedDepartmentId, false));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                
                roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedRoleId = roles.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                
                departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedDepartmentId = departments.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                
                userUpdate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if (userName.length() == 0 || userSurname.length() == 0){
                            Toast.makeText(UserOperations.this, "Lütfen Kullanıcı Adını ve Soyadını Boş Bırakmayınız !", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            db.userUpdate(selectedTCKN, selectedRoleId, selectedDepartmentId, userName.getText().toString(), userSurname.getText().toString(), userPassword.getText().toString());
                        }
                    }
                });

                userRemove.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        db.userRemove(selectedTCKN);
                    }
                });
            }
        });
    }

    private int getPositionById(int id, boolean isRole){

        if (isRole){
            for (int i = 0; i < roles.size(); i++){
                if (roles.get(i).id == id){
                    return i;
                }
            }
        }

        else {
            for (int i = 0; i < departments.size(); i++){
                if (departments.get(i).id == id){
                    return i;
                }
            }
        }

        return 0;
    }
}