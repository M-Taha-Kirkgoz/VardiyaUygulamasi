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
import com.example.vardiyauygulamasi.MainActivity;
import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.Dtos.Department;
import com.example.vardiyauygulamasi.Adapters.DepartmentsAdapter;

import java.util.ArrayList;

public class DepartmentOperations extends AppCompatActivity {
    private int selectedDepartmentId;
    private String selectedDepartmentName;
    DatabaseHelper db;
    Dialog alertDialog, confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_operations);

        Button createDepartment = findViewById(R.id.departman_olustur);
        Button updateDepartment = findViewById(R.id.departman_guncelle);

        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Department> departments = db.getAllDepartments();

        alertDialog = new Dialog(DepartmentOperations.this);
        alertDialog.setContentView(R.layout.custom_alert_box);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        alertDialog.setCancelable(false);

        TextView alertTitle = alertDialog.findViewById(R.id.title);
        TextView alertBody = alertDialog.findViewById(R.id.body);

        Button btnDialogOk = alertDialog.findViewById(R.id.btnDialogOk);


        confirmDialog = new Dialog(DepartmentOperations.this);
        confirmDialog.setContentView(R.layout.custom_dialog_box);
        confirmDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        confirmDialog.setCancelable(false);

        TextView confirmTitle = confirmDialog.findViewById(R.id.title);
        TextView confirmBody = confirmDialog.findViewById(R.id.body);

        Button btnDialogNo = confirmDialog.findViewById(R.id.btnDialogNo);
        Button btnDialogYes = confirmDialog.findViewById(R.id.btnDialogYes);

        createDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.department_create);

                Button departmentCreate = findViewById(R.id.departmani_kaydet);

                departmentCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText departmentName = findViewById(R.id.department_name);

                        if (departmentName.length() == 0) {
                            Toast.makeText(DepartmentOperations.this, "Lütfen Departman Adını Boş Bırakmayınız !", Toast.LENGTH_SHORT).show();

                        } else if (db.departmentIsHave(departmentName.getText().toString())) {
                            alertTitle.setText("Kayıtlı Departman !");
                            alertBody.setText(departmentName.getText().toString() + " adına sahip bir departman zaten var.");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                        } else {
                            db.departmentCreate(departmentName.getText().toString());

                            alertTitle.setText("İşlem Başarılı !");
                            alertBody.setText(departmentName.getText().toString() + " departmanı başarıyla oluşturuldu.");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                    onBackPressed();

                                    Intent backPage = new Intent(DepartmentOperations.this, DepartmentOperations.class);
                                    startActivity(backPage);
                                }
                            });
                        }
                    }
                });

            }
        });

        updateDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        selectedDepartmentName = departments.get(position).departmentName;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                updateDepartmentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText newDepartmentName = findViewById(R.id.new_department_name);

                        if (newDepartmentName.length() == 0) {

                            Toast.makeText(DepartmentOperations.this, "Lütfen Yeni Bir Departman İsmi Giriniz !", Toast.LENGTH_LONG).show();
                        } else if (db.departmentIsHave(newDepartmentName.getText().toString())) {

                            alertTitle.setText("Kayıtlı Departman !");
                            alertBody.setText(newDepartmentName.getText().toString() + " adına sahip bir departman zaten var.");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                        } else {
                            db.departmentUpdate(selectedDepartmentId, newDepartmentName.getText().toString());

                            alertTitle.setText("İşlem Başarılı !");
                            alertBody.setText(selectedDepartmentName + " departmanı " + newDepartmentName.getText().toString() + " olarak güncellendi.");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                    onBackPressed();

                                    Intent backPage = new Intent(DepartmentOperations.this, DepartmentOperations.class);
                                    startActivity(backPage);
                                }
                            });
                        }
                    }
                });

                removeDepartmentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedDepartmentId == 0) {
                            alertTitle.setText("Seçili Departman Yok !");
                            alertBody.setText("Lütfen bir departmanın seçili olduğundan emin olunuz !");

                            alertDialog.show();

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            Toast.makeText(DepartmentOperations.this, "Lütfen Bir Departmanın Seçili Olduğundan Emin Olunuz !", Toast.LENGTH_LONG).show();
                        } else {

                            confirmTitle.setText("Silme İşlemini Onaylıyor Musunuz?");
                            confirmBody.setText(selectedDepartmentName + " departmanı silinsin mi?");

                            confirmDialog.show();

                            btnDialogYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    db.departmentRemove(selectedDepartmentId);

                                    confirmDialog.dismiss();

                                    alertTitle.setText("İşlem Başarılı !");
                                    alertBody.setText(selectedDepartmentName + " departmanı başarıyla silindi.");

                                    alertDialog.show();

                                    btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();

                                            onBackPressed();

                                            Intent backPage = new Intent(DepartmentOperations.this, DepartmentOperations.class);
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
                    }
                });
            }
        });
    }
}