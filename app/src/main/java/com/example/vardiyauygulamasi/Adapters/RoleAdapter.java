package com.example.vardiyauygulamasi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vardiyauygulamasi.Dtos.Role;

import java.util.List;

public class RoleAdapter extends ArrayAdapter<Role> {

    public RoleAdapter(Context context, List<Role> roleList){
        super(context, 0, roleList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Role roller = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item,parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(roller.roleName);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        Role role = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(role.roleName);

        return convertView;
    }
}
