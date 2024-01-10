package com.example.vardiyauygulamasi.Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vardiyauygulamasi.Dtos.Shift;

public class ShiftAdapter extends ArrayAdapter<Shift> {

    public ShiftAdapter(Context context, List<Shift> shiftList){
        super(context, 0, shiftList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Shift vardiyalar = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(vardiyalar.userName + " " + vardiyalar.userSurname);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        Shift shift = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(shift.userName + " " + shift.userSurname);

        return convertView;
    }
}
