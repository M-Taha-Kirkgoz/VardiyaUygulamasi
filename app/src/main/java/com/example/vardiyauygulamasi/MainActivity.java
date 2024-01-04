package com.example.vardiyauygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vardiyauygulamasi.admin.AdminHome;
import com.example.vardiyauygulamasi.classes.User;
import com.example.vardiyauygulamasi.user.UserHome;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        EditText tc_no = findViewById(R.id.editTCNo);
        EditText pass = findViewById(R.id.editPassword);

        Button login = findViewById(R.id.Login_Btn);

        Button deleteTable = findViewById(R.id.tablo_sil);
        Button createTable = findViewById(R.id.tablo_olustur);

        deleteTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                db.deleteUserTable();
            }
        });

        createTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                db.createUserTable();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean kullanici_varmi = db.userIsHave(Long.parseLong(tc_no.getText().toString()));

                if (kullanici_varmi){
                    boolean login = db.userLogin(Long.parseLong(tc_no.getText().toString()), pass.getText().toString());

                    if (login){
                        Toast.makeText(MainActivity.this, "Kullanici Girisi Basarili", Toast.LENGTH_LONG).show();

                        User userDetail = db.userDetails(Long.parseLong(tc_no.getText().toString()));

                        Intent intnt;

                        if (userDetail.roleId == 1)
                            intnt = new Intent(MainActivity.this, AdminHome.class);
                        else
                            intnt = new Intent(MainActivity.this, UserHome.class);

                        intnt.putExtra("userDetail", userDetail);

                        startActivity(intnt);
                    }

                    else
                        Toast.makeText(MainActivity.this, "Kullanici Girisi Basarisiz", Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(MainActivity.this, "Boyle Bir Kullanici Bulunamadi " + Long.parseLong(tc_no.getText().toString()), Toast.LENGTH_LONG).show();
            }
        });

    }
}