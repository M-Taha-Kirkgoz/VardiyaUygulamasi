package com.example.vardiyauygulamasi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vardiyauygulamasi.Dtos.Department;

import java.util.List;

// Bir departman veri dizisi alır ve dropdown menüde kullanabilmek için formatlar.
public class DepartmentsAdapter extends ArrayAdapter<Department> {

    public DepartmentsAdapter(Context context, List<Department> departmentList){
        super(context, 0, departmentList);
    }

    // Dropdown menünün seçili olan itemi ve özellikleri.
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Seçilen departmanı işaretler.
        Department departmanlar = getItem(position);

        // Eğer menü içerisinde veri olmaz ise, boş bir görünüm oluşturur.
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(departmanlar.departmentName);
        textView.setTextColor(Color.parseColor("#cabba5"));

        return convertView;
    }

    // Dropdown açılır menü kısmındaki özellikler.
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Department department = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(department.departmentName);
        textView.setTextColor(Color.parseColor("#cabba5"));

        return convertView;
    }
}
