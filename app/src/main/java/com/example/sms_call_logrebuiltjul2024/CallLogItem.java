package com.example.sms_call_logrebuiltjul2024;


public class CallLogItem {
    private String number;
    private String type;
    private String date;

    public CallLogItem(String number, String type, String date) {
        this.number = number;
        this.type = type;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
