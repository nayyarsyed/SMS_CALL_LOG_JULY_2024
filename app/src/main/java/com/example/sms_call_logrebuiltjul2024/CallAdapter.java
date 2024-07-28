package com.example.sms_call_logrebuiltjul2024;

import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    private Cursor cursor;

    public CallAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

            holder.textViewNumber.setText("Number: " + number);
            holder.textViewType.setText("Type: " + type);
            holder.textViewDate.setText("Date: " + date);
            holder.textViewDuration.setText("Duration: " + duration);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class CallViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewType, textViewDate, textViewDuration;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
        }
    }
}



/*
package com.example.sms_call_logrebuiltjul2024;

import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    private Cursor cursor;

    public CallAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CallAdapter.ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String timestamp = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

            holder.textViewNumber.setText("Number: " + number);
            holder.textViewType.setText("Type: " + type);
            holder.textViewTimestamp.setText("Timestamp: " + timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNumber;
        public TextView textViewType;
        public TextView textViewTimestamp;

        public ViewHolder(View view) {
            super(view);
            textViewNumber = view.findViewById(R.id.textViewNumber);
            textViewType = view.findViewById(R.id.textViewType);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
        }
    }
}
*/
