package com.example.sms_call_logrebuiltjul2024;


import android.database.Cursor;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.SMSViewHolder> {

    private Cursor cursor;

    public SMSAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new SMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String from = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
            String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
            String timestamp = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));

            holder.textViewFrom.setText("From: " + from);
            holder.textViewBody.setText("Body: " + body);
            holder.textViewTimestamp.setText("Date: " + timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class SMSViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFrom, textViewBody, textViewTimestamp;

        public SMSViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFrom = itemView.findViewById(R.id.textViewFrom);
            textViewBody = itemView.findViewById(R.id.textViewBody);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}



/*
package com.example.sms_call_logrebuiltjul2024;

import android.database.Cursor;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {

    private Cursor cursor;

    public SMSAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public SMSAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SMSAdapter.ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String from = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
            String to = cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON));
            String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
            String timestamp = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));

            holder.textViewFrom.setText("From: " + from);
            holder.textViewTo.setText("To: " + to);
            holder.textViewBody.setText("Body: " + body);
            holder.textViewTimestamp.setText("Timestamp: " + timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFrom;
        public TextView textViewTo;
        public TextView textViewBody;
        public TextView textViewTimestamp;

        public ViewHolder(View view) {
            super(view);
            textViewFrom = view.findViewById(R.id.textViewFrom);
            textViewTo = view.findViewById(R.id.textViewTo);
            textViewBody = view.findViewById(R.id.textViewBody);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
        }
    }
}
*/
