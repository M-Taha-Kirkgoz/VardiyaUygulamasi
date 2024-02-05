package com.example.vardiyauygulamasi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vardiyauygulamasi.DatabaseHelper;
import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.Dtos.Department;
import com.example.vardiyauygulamasi.Adapters.DepartmentsAdapter;
import com.example.vardiyauygulamasi.Dtos.Role;
import com.example.vardiyauygulamasi.Adapters.RoleAdapter;
import com.example.vardiyauygulamasi.Dtos.User;
import com.example.vardiyauygulamasi.Adapters.UserAdapter;

import java.util.ArrayList;

public class UserOperations extends AppCompatActivity {
    private int selectedDepartmentId, selectedRoleId;
    private long selectedTCKN;
    DatabaseHelper db;

    ArrayList<Department> departments;
    ArrayList<Role> roles;
    Dialog alertDialog, confirmDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_operations);

        Button createUser = findViewById(R.id.kullanici_olustur);
        Button updateUser = findViewById(R.id.kullanici_guncelle);

        db = new DatabaseHelper(getApplicationContext());

        departments = db.getAllDepartments();
        roles = db.getAllRoles();

        alertDialog = new Dialog(UserOperations.this);
        alertDialog.setContentView(R.layout.custom_alert_box);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        alertDialog.setCancelable(false);

        TextView alertTitle = alertDialog.findViewById(R.id.title);
        TextView alertBody = alertDialog.findViewById(R.id.body);

        Button btnDialogOk = alertDialog.findViewById(R.id.btnDialogOk);


        confirmDialog = new Dialog(UserOperations.this);
        confirmDialog.setContentView(R.layout.custom_dialog_box);
        confirmDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        confirmDialog.setCancelable(false);

        TextView confirmTitle = confirmDialog.findViewById(R.id.title);
        TextView confirmBody = confirmDialog.findViewById(R.id.body);

        Button btnDialogNo = confirmDialog.findViewById(R.id.btnDialogNo);
        Button btnDialogYes = confirmDialog.findViewById(R.id.btnDialogYes);

        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.user_create);

                Spinner departmentsSpinner = findViewById(R.id.departments);
                Spinner roleSpinner = findViewById(R.id.roles);
                Button userCreate = findViewById(R.id.kullanici_olustur);

                DepartmentsAdapter adapter = new DepartmentsAdapter(UserOperations.this, departments);
                departmentsSpinner.setAdapter(adapter);

                // Dropdown menüden "Departman" seçimi yapılır. Seçilen departmanın "ID" değişkeni tutulur.
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

                // Dropdown menüden "Rol" seçimi yapılır. Seçilen rolün "ID" değişkeni tutulur.
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
                            Toast.makeText(UserOperations.this, "Lütfen Geçerli (11 Haneli) Bir TC Kimlik Numarası Giriniz !", Toast.LENGTH_LONG).show();
                        }

                        else if (db.userIsHave(Long.parseLong(tCNo.getText().toString()))){ // Kaydedilen "TC No" kontrol edilir.

                            alertTitle.setText("Kayıtlı Kullanıcı !");
                            alertBody.setText(tCNo.getText().toString() + " T.C. numarasına sahip bir kullanıcı mevcut !");

                            alertDialog.show();
                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                        }

                        else {
                            long tCKN =  Long.parseLong(tCNo.getText().toString());
                            db.userRegister(tCKN, selectedRoleId, selectedDepartmentId, userName.getText().toString(), userSurname.getText().toString(), userPassword.getText().toString());

                            alertTitle.setText("İşlem Başarılı !");
                            alertBody.setText(userName.getText().toString() + " " + userSurname.getText().toString() + " kullanıcısı başarıyla oluşturuldu.");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                    onBackPressed();

                                    Intent backPage = new Intent(UserOperations.this, UserOperations.class);
                                    startActivity(backPage);
                                }
                            });
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
                    // Seçilen kullanıcının bilgileri, boşluklara doldurulur.
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

                            alertTitle.setText("İşlem Başarılı !");
                            alertBody.setText(selectedTCKN + " T.C. numarasına ait kullanıcı başarıyla güncellendi");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                    onBackPressed();

                                    Intent backPage = new Intent(UserOperations.this, UserOperations.class);
                                    startActivity(backPage);
                                }
                            });
                        }
                    }
                });

                userRemove.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        confirmTitle.setText("Silme İşlemini Onaylıyor Musunuz?");
                        confirmBody.setText(selectedTCKN + " T.C. numarasına sahip kullanıcı silinsin mi?");

                        confirmDialog.show();

                        btnDialogYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.userRemove(selectedTCKN);

                                confirmDialog.dismiss();

                                alertTitle.setText("İşlem Başarılı !");
                                alertBody.setText(selectedTCKN + " T.C. numarasına sahip kullanıcı başarıyla silindi.");

                                alertDialog.show();

                                btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();

                                        onBackPressed();

                                        Intent backPage = new Intent(UserOperations.this, UserOperations.class);
                                        startActivity(backPage);
                                    }
                                });
                            }
                        });

                        btnDialogNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();

                                alertTitle.setText("İptal Edildi !");
                                alertBody.setText("Silme işlemi başarıyla iptal edildi.");

                                alertDialog.show();

                                btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    // Dropdown menülere (Spinner) kullanıcının Departmanını ve Rolünü varsayılan olarak getirir.
    // Tek fonksiyonda hem Departman hem Role işlemi yapılır.
    // Departman mı, rol mü belirtmek amacıyla bir bool değişkeni yer alır.
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