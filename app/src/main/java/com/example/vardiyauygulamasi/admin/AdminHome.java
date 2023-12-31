package com.example.vardiyauygulamasi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.classes.User;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();

        User userDetail = (User) intent.getSerializableExtra("userDetail");

        Toast.makeText(this, userDetail.name.toString(), Toast.LENGTH_LONG).show();
    }
}