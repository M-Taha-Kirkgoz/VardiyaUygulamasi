package com.example.vardiyauygulamasi.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vardiyauygulamasi.DatabaseHelper;
import com.example.vardiyauygulamasi.R;
import com.example.vardiyauygulamasi.Dtos.Department;
import com.example.vardiyauygulamasi.Adapters.DepartmentsAdapter;
import com.example.vardiyauygulamasi.Dtos.Shift;
import com.example.vardiyauygulamasi.Adapters.ShiftAdapter;
import com.example.vardiyauygulamasi.Dtos.User;
import com.example.vardiyauygulamasi.Adapters.UserAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShiftOperations extends AppCompatActivity {
    private int selectedDepartmentId;
    private long selectedUserTCKN;
    private String selectedDate;
    private String formattedBeginTime;
    private String formattedEndTime;
    private int selectedShiftId;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_operations);

        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Department> departments = db.getAllDepartments();

        Spinner departmentSpinner = findViewById(R.id.departman_sec);

        Button createShift = findViewById(R.id.vardiya_ata);
        Button canceledShift = findViewById(R.id.vardiya_iptal);

        DepartmentsAdapter departmentAdapter = new DepartmentsAdapter(ShiftOperations.this, departments);
        departmentSpinner.setAdapter(departmentAdapter);


        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartmentId = departments.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.shift_create);

                ArrayList<User> users = db.getAllUserByDepartments(selectedDepartmentId);

                Spinner userSpinner = findViewById(R.id.kullanici_sec);

                CalendarView date = findViewById(R.id.calendarView);

                EditText beginTime = findViewById(R.id.beginTime);
                EditText endTime = findViewById(R.id.endTime);

                Button createShift = findViewById(R.id.vardiya_olustur);

                if (users.size() != 0) {

                    UserAdapter userAdapter = new UserAdapter(ShiftOperations.this, users);
                    userSpinner.setAdapter(userAdapter);

                    userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedUserTCKN = users.get(position).tCKN;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, (month), dayOfMonth);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            selectedDate = dateFormat.format(calendar.getTime());
                        }
                    });

                    createShift.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] beginTimeParts = beginTime.getText().toString().split(":");
                            String[] endTimeParts = endTime.getText().toString().split(":");

                            String editBeginHour, editBeginMinute, editEndHour, editEndMinute;

                            if (beginTimeParts.length == 2) {

                                int beginHour = Integer.parseInt(beginTimeParts[0]);
                                int beginMinute = Integer.parseInt(beginTimeParts[1]);

                                int endHour = Integer.parseInt(endTimeParts[0]);
                                int endMinute = Integer.parseInt(endTimeParts[1]);

                                if (beginHour >= 24 || endHour >= 24) {
                                    Toast.makeText(ShiftOperations.this, "Lütfen Geçerli Bir Saat Dilimi Giriniz ! ( 0 - 23 )", Toast.LENGTH_SHORT).show();
                                } else if (beginMinute >= 60 || endMinute >= 60) {
                                    Toast.makeText(ShiftOperations.this, "Lütfen Geçerli Bir Dakika Dilimi Giriniz ! ( 0 - 59 )", Toast.LENGTH_SHORT).show();
                                } else {
                                    if ((beginHour < endHour) && selectedDate != null) {
                                        if (!db.shiftIsHave(selectedDate, selectedUserTCKN)) {
                                            editBeginHour = beginHour < 10 ? "0" + String.valueOf(beginHour) : String.valueOf(beginHour);
                                            editBeginMinute = beginMinute < 10 ? "0" + String.valueOf(beginMinute) : String.valueOf(beginMinute);

                                            editEndHour = endHour < 10 ? "0" + String.valueOf(endHour) : String.valueOf(endHour);
                                            editEndMinute = endMinute < 10 ? "0" + String.valueOf(endMinute) : String.valueOf(endMinute);

                                            formattedBeginTime = editBeginHour + ":" + editBeginMinute;
                                            formattedEndTime = editEndHour + ":" + editEndMinute;

                                            db.shiftCreate(selectedUserTCKN, selectedDepartmentId, selectedDate, formattedBeginTime, formattedEndTime);
                                        } else {
                                            Toast.makeText(ShiftOperations.this, "Belirlenen Tarihte Kullanıcının Bir Vardiya Kaydı Var Zaten !", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ShiftOperations.this, "Baslangic saati bitis saatinden kucuk olmali !", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(ShiftOperations.this, "Lütfen Geçerli Bir Zaman Formatı Giriniz ! ( Saat : Dakika )", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    createShift.setEnabled(false);
                }
            }
        });

        canceledShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.shift_update);

                Button shiftRemove = findViewById(R.id.vardiya_sil);

                CalendarView date = findViewById(R.id.tarih_sec);

                Spinner userFilterByDate = findViewById(R.id.filtreli_kullanici);

                date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, (month), dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        selectedDate = dateFormat.format(calendar.getTime());

                        ArrayList<Shift> shifts = db.getAllShiftByDateAndDepartmentId(selectedDate, selectedDepartmentId);
                        selectedShiftId = -1;


                        if (shifts.size() != 0) {
                            ShiftAdapter shiftAdapter = new ShiftAdapter(ShiftOperations.this, shifts);
                            userFilterByDate.setAdapter(shiftAdapter);

                            Toast.makeText(ShiftOperations.this, "girdi", Toast.LENGTH_SHORT).show();

                            userFilterByDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedShiftId = shifts.get(position).id;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            shiftRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (selectedShiftId > -1) {
                                        db.shiftRemove(selectedShiftId);
                                    }
                                }
                            });
                        } else {
                            shiftRemove.setEnabled(false);
                        }
                    }
                });
            }
        });
    }
}