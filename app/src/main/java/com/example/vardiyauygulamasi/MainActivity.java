package com.example.vardiyauygulamasi;

import static kotlin.jvm.internal.Reflection.typeOf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                Boolean kullanici_varmi = db.userIsHave(Integer.parseInt(tc_no.getText().toString()));

                if (kullanici_varmi){
                    Boolean login = db.userLogin(Integer.parseInt(tc_no.getText().toString()), pass.getText().toString());

                    if (login)
                        Toast.makeText(MainActivity.this, "Kullanici Girisi Basarili", Toast.LENGTH_LONG).show();

                    else
                        Toast.makeText(MainActivity.this, "Kullanici Girisi Basarisiz", Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(MainActivity.this, "Boyle Bir Kullanici Bulunamadi " + Integer.parseInt(tc_no.getText().toString()), Toast.LENGTH_LONG).show();
            }
        });

    }
}