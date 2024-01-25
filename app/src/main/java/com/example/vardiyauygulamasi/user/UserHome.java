package com.example.vardiyauygulamasi.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.vardiyauygulamasi.Adapters.ShiftViewAdapter;
import com.example.vardiyauygulamasi.DatabaseHelper;
import com.example.vardiyauygulamasi.Dtos.Shift;
import com.example.vardiyauygulamasi.Dtos.User;
import com.example.vardiyauygulamasi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UserHome extends AppCompatActivity {
    private int selectedDepartmentId = -1;
    private String selectedDate;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shifts_show);

        db = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();

        User userDetail = (User) intent.getSerializableExtra("userDetail");

        selectedDepartmentId = userDetail.departmentId;

        if (selectedDepartmentId != -1){
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

                    table.setLayoutManager(new LinearLayoutManager(UserHome.this));

                    ShiftViewAdapter viewAdapter = new ShiftViewAdapter(shifts, userDetail.tCKN);
                    table.setAdapter(viewAdapter);
                }
            });
        }
    }
}