package com.example.vardiyauygulamasi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vardiyauygulamasi.Dtos.Shift;
import com.example.vardiyauygulamasi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShiftViewAdapter extends RecyclerView.Adapter<ShiftViewAdapter.ShiftViewHolder> {
    private ArrayList<Shift> shifts;

    public ShiftViewAdapter(ArrayList<Shift> shifts){
        this.shifts = shifts;
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.satir_layout, parent, false);
        return new ShiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Shift shift = shifts.get(position);
        holder.adSoyadTextView.setText(shift.userName + " " + shift.userSurname);
        holder.baslangicSaatiTextView.setText(timeFormat.format(shift.beginTime.getTime()));
        holder.bitisSaatiTextView.setText(timeFormat.format(shift.endTime.getTime()));
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public static class ShiftViewHolder extends RecyclerView.ViewHolder {
        TextView adSoyadTextView, baslangicSaatiTextView, bitisSaatiTextView;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            adSoyadTextView = itemView.findViewById(R.id.adSoyadTextView);
            baslangicSaatiTextView = itemView.findViewById(R.id.baslangicSaatiTextView);
            bitisSaatiTextView = itemView.findViewById(R.id.bitisSaatiTextView);
        }
    }
}
