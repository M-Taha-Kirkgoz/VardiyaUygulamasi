package com.example.vardiyauygulamasi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.Dtos.User;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();

        User userDetail = (User) intent.getSerializableExtra("userDetail");

        Button vardiyaIslemleri = findViewById(R.id.vardiya_islemleri);
        Button vardiyalar = findViewById(R.id.vardiyalar);
        Button kullaniciIslemleri = findViewById(R.id.kullanici_islemleri);
        Button departmanIslemleri = findViewById(R.id.departman_islemleri);

        kullaniciIslemleri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent userOperationsIntent = new Intent(AdminHome.this, UserOperations.class);

                startActivity(userOperationsIntent);
            }
        });

        departmanIslemleri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent departmentOperationsIntent = new Intent(AdminHome.this, DepartmentOperations.class);

                startActivity(departmentOperationsIntent);
            }
        });

        vardiyaIslemleri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent shiftOperationsIntent = new Intent(AdminHome.this, ShiftOperations.class);

                startActivity(shiftOperationsIntent);
            }
        });

        vardiyalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent shiftsIntent = new Intent(AdminHome.this, Shifts.class);

                shiftsIntent.putExtra("userTckn", userDetail.tCKN);

                startActivity(shiftsIntent);
            }
        });

    }
}