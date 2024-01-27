package com.example.vardiyauygulamasi;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vardiyauygulamasi.admin.AdminHome;
import com.example.vardiyauygulamasi.Dtos.User;
import com.example.vardiyauygulamasi.user.UserHome;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    Dialog dialog;
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

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_alert_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        TextView title = dialog.findViewById(R.id.title);
        TextView body = dialog.findViewById(R.id.body);

        Button btnDialogOk = dialog.findViewById(R.id.btnDialogOk);

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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

                    else{
                        title.setText("Kullanıcı Girişi Başarısız !");
                        body.setText("T.C. numarası veya şifre hatalı.");

                        dialog.show();
                    }
                }

                else {
                    title.setText("Kullanıcı Bulunamadı !");
                    body.setText(Long.parseLong(tc_no.getText().toString()) + " T.C. numarasına kayıtlı herhangi bir kullanıcı bulunamadı.");

                    dialog.show();
                }
            }
        });

    }
}