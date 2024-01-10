package com.example.vardiyauygulamasi.admin;

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
import com.example.vardiyauygulamasi.Dtos.Department;
import com.example.vardiyauygulamasi.Adapters.DepartmentsAdapter;

import java.util.ArrayList;

public class DepartmentOperations extends AppCompatActivity {
    private int selectedDepartmentId;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_operations);

        Button createDepartment = findViewById(R.id.departman_olustur);
        Button updateDepartment = findViewById(R.id.departman_guncelle);

        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Department> departments = db.getAllDepartments();

        createDepartment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.department_create);

                Button departmentCreate = findViewById(R.id.departmani_kaydet);

                departmentCreate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        EditText departmentName = findViewById(R.id.department_name);

                        if (departmentName.length() == 0){
                            Toast.makeText(DepartmentOperations.this, "Lütfen Departman Adını Boş Bırakmayınız !", Toast.LENGTH_SHORT).show();
                        }

                        else if (db.departmentIsHave(departmentName.getText().toString())){
                            Toast.makeText(DepartmentOperations.this, "Bu İsme Sahip Bir Departman Zaten Var !", Toast.LENGTH_LONG).show();
                        }

                        else{
                            db.departmentCreate(departmentName.getText().toString());
                        }
                    }
                });

            }
        });

        updateDepartment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.department_update);

                Spinner departmentSpinner = findViewById(R.id.department_spinner);

                Button updateDepartmentBtn = findViewById(R.id.departmani_guncelle);
                Button removeDepartmentBtn = findViewById(R.id.departmani_sil);

                DepartmentsAdapter adapter = new DepartmentsAdapter(DepartmentOperations.this, departments);
                departmentSpinner.setAdapter(adapter);

                departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedDepartmentId = departments.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                updateDepartmentBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        EditText newDepartmentName = findViewById(R.id.new_department_name);

                        if (newDepartmentName.length() == 0){
                            Toast.makeText(DepartmentOperations.this, "Lütfen Yeni Bir Departman İsmi Giriniz !", Toast.LENGTH_LONG).show();
                        }

                        else if (db.departmentIsHave(newDepartmentName.getText().toString())){
                            Toast.makeText(DepartmentOperations.this, "Bu İsme Sahip Bir Departman Zaten Var !", Toast.LENGTH_LONG).show();
                        }

                        else {
                            db.departmentUpdate(selectedDepartmentId, newDepartmentName.getText().toString());
                        }
                    }
                });

                removeDepartmentBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if (selectedDepartmentId == 0){
                            Toast.makeText(DepartmentOperations.this, "Lütfen Bir Departmanın Seçili Olduğundan Emin Olunuz !", Toast.LENGTH_LONG).show();
                        }

                        else {
                            db.departmentRemove(selectedDepartmentId);
                        }
                    }
                });
            }
        });
    }
}