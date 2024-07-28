package com.example.sms_call_logrebuiltjul2024;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private List<CallLogItem> callLogs;

    public CallLogAdapter(List<CallLogItem> callLogs) {
        this.callLogs = callLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_call, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLogItem callLog = callLogs.get(position);
        holder.textViewFrom.setText("Number: " + callLog.getNumber());
        holder.textViewType.setText("Type: " + callLog.getType());
        holder.textViewTimestamp.setText("Date: " + callLog.getDate());
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFrom;
        public TextView textViewType;
        public TextView textViewTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFrom = itemView.findViewById(R.id.textViewFrom);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
