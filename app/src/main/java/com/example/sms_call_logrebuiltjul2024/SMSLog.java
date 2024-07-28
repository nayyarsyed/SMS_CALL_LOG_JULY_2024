package com.example.sms_call_logrebuiltjul2024;

public class SMSLog {
    private String from;
    private String body;
    private String date;

    public SMSLog(String from, String body, String date) {
        this.from = from;
        this.body = body;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
