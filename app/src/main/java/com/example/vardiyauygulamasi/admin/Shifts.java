package com.example.vardiyauygulamasi.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.vardiyauygulamasi.Adapters.DepartmentsAdapter;
import com.example.vardiyauygulamasi.Adapters.ShiftViewAdapter;
import com.example.vardiyauygulamasi.DatabaseHelper;
import com.example.vardiyauygulamasi.Dtos.Department;
import com.example.vardiyauygulamasi.Dtos.Shift;
import com.example.vardiyauygulamasi.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Shifts extends AppCompatActivity {
    private int selectedDepartmentId = -1;
    private String selectedDate;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Department> departments = db.getAllDepartments();

        Spinner departmentSpinner = findViewById(R.id.vardiyalar_departman_sec);

        Button showShifts = findViewById(R.id.vardiyalari_goster);

        if (departments.size() != 0){
            DepartmentsAdapter departmentAdapter = new DepartmentsAdapter(Shifts.this, departments);
            departmentSpinner.setAdapter(departmentAdapter);

            departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDepartmentId = departments.get(position).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedDepartmentId = -1;
                }
            });

            showShifts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedDepartmentId != -1){
                        setContentView(R.layout.shifts_show);

                        CalendarView date = findViewById(R.id.vardiya_tarih);
                        RecyclerView table = findViewById(R.id.liste);

                        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                selectedDate = dateFormat.format(calendar.getTime());

                                ArrayList<Shift> shifts = db.getAllShiftByDateAndDepartmentId(selectedDate, selectedDepartmentId);

                                table.setLayoutManager(new LinearLayoutManager(Shifts.this));

                                ShiftViewAdapter viewAdapter = new ShiftViewAdapter(shifts);
                                table.setAdapter(viewAdapter);

                            }
                        });
                    }
                }
            });
        }
        else {
            showShifts.setEnabled(false);
        }
    }
}