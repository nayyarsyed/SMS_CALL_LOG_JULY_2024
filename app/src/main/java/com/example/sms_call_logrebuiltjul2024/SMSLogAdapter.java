package com.example.sms_call_logrebuiltjul2024;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SMSLogAdapter extends RecyclerView.Adapter<SMSLogAdapter.ViewHolder> {

    private List<SMSLog> smsLogs;

    public SMSLogAdapter(List<SMSLog> smsLogs) {
        this.smsLogs = smsLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_sms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMSLog smsLog = smsLogs.get(position);
        holder.textViewFrom.setText("From: " + smsLog.getFrom());
        holder.textViewBody.setText("Body: " + smsLog.getBody());
        holder.textViewTimestamp.setText("Date: " + smsLog.getDate());
    }

    @Override
    public int getItemCount() {
        return smsLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFrom;
        public TextView textViewBody;
        public TextView textViewTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFrom = itemView.findViewById(R.id.textViewFrom);
            textViewBody = itemView.findViewById(R.id.textViewBody);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
