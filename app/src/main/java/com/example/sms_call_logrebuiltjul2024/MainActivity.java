
package com.example.sms_call_logrebuiltjul2024;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_SMS_CALL_LOG = 1;
    private RecyclerView recyclerViewSMS;
    private RecyclerView recyclerViewCalls;
    private Button btnShowSMS, btnShowCalls, btnExportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewSMS = findViewById(R.id.recyclerViewSMS);
        recyclerViewCalls = findViewById(R.id.recyclerViewCalls);
        btnShowSMS = findViewById(R.id.btnShowSMS);
        btnShowCalls = findViewById(R.id.btnShowCalls);
        btnExportData = findViewById(R.id.btnExportData);

        recyclerViewSMS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCalls.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_READ_SMS_CALL_LOG);
        } else {
            initializeButtons();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initializeButtons();
            } else {
                Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeButtons() {
        btnShowSMS.setOnClickListener(view -> {
            displaySMSLogs();
            recyclerViewSMS.setVisibility(View.VISIBLE);
            recyclerViewCalls.setVisibility(View.GONE);
        });

        btnShowCalls.setOnClickListener(view -> {
            displayCallLogs();
            recyclerViewSMS.setVisibility(View.GONE);
            recyclerViewCalls.setVisibility(View.VISIBLE);
        });

        btnExportData.setOnClickListener(view -> exportDataToJson());
    }

    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            SMSAdapter smsAdapter = new SMSAdapter(cursor);
            recyclerViewSMS.setAdapter(smsAdapter);
        }
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            CallAdapter callAdapter = new CallAdapter(cursor);
            recyclerViewCalls.setAdapter(callAdapter);
        }
    }

    private void exportDataToJson() {
        ContentResolver contentResolver = getContentResolver();
        Uri smsUri = Telephony.Sms.CONTENT_URI;
        Uri callsUri = CallLog.Calls.CONTENT_URI;
        Cursor smsCursor = contentResolver.query(smsUri, null, null, null, null);
        Cursor callsCursor = contentResolver.query(callsUri, null, null, null, null);

        JSONArray smsArray = new JSONArray();
        JSONArray callsArray = new JSONArray();

        try {
            if (smsCursor != null && smsCursor.moveToFirst()) {
                do {
                    JSONObject smsObject = new JSONObject();
                    smsObject.put("from", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                    smsObject.put("body", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.BODY)));
                    smsObject.put("timestamp", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.DATE)));
                    smsArray.put(smsObject);
                } while (smsCursor.moveToNext());
            }

            if (callsCursor != null && callsCursor.moveToFirst()) {
                do {
                    JSONObject callObject = new JSONObject();
                    callObject.put("number", callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    callObject.put("type", callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.TYPE)));
                    callObject.put("date", callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.DATE)));
                    callObject.put("duration", callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.DURATION)));
                    callsArray.put(callObject);
                } while (callsCursor.moveToNext());
            }

            JSONObject finalObject = new JSONObject();
            finalObject.put("SMS", smsArray);
            finalObject.put("Calls", callsArray);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sms_calls_data.json");

           //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sms_calls_data.json");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(finalObject.toString());
                Toast.makeText(this, "Data exported to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error exporting data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (smsCursor != null) {
                smsCursor.close();
            }
            if (callsCursor != null) {
                callsCursor.close();
            }
        }
    }
}




/*
package com.example.sms_call_logrebuiltjul2024;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms_call_logrebuiltjul2024.CallAdapter;
import com.example.sms_call_logrebuiltjul2024.SMSAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_SMS_CALL_LOG = 1;
    private RecyclerView recyclerViewSMS;
    private RecyclerView recyclerViewCalls;
    private Button btnShowSMS;
    private Button btnShowCalls;
    private Button btnExportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewSMS = findViewById(R.id.recyclerViewSMS);
        recyclerViewCalls = findViewById(R.id.recyclerViewCalls);
        btnShowSMS = findViewById(R.id.btnShowSMS);
        btnShowCalls = findViewById(R.id.btnShowCalls);
        btnExportData = findViewById(R.id.btnExportData);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_SMS_CALL_LOG);
        } else {
            initializeRecyclerViews();
        }

        btnShowSMS.setOnClickListener(v -> {
            recyclerViewSMS.setVisibility(View.VISIBLE);
            recyclerViewCalls.setVisibility(View.GONE);
            displaySMSLogs();
        });

        btnShowCalls.setOnClickListener(v -> {
            recyclerViewSMS.setVisibility(View.GONE);
            recyclerViewCalls.setVisibility(View.VISIBLE);
            displayCallLogs();
        });

        btnExportData.setOnClickListener(v -> exportDataToJSON());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                initializeRecyclerViews();
            } else {
                // Permission denied
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

 */
/*   private void initializeRecyclerViews() {
        recyclerViewSMS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCalls.setLayoutManager(new LinearLayoutManager(this));
    }*//*


    private void initializeRecyclerViews() {
        recyclerViewSMS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCalls.setLayoutManager(new LinearLayoutManager(this));
        displaySMSLogs();
        displayCallLogs();
    }


*/
/*    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            SMSAdapter smsAdapter = new SMSAdapter(cursor);
            recyclerViewSMS.setAdapter(smsAdapter);
        }
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            CallAdapter callAdapter = new CallAdapter(cursor);
            recyclerViewCalls.setAdapter(callAdapter);
        }
    }*//*


    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            SMSAdapter smsAdapter = new SMSAdapter(cursor);
            recyclerViewSMS.setAdapter(smsAdapter);
        }
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            CallAdapter callAdapter = new CallAdapter(cursor);
            recyclerViewCalls.setAdapter(callAdapter);
        }
    }


    private void exportDataToJSON() {
        try {
            JSONArray smsArray = new JSONArray();
            JSONArray callArray = new JSONArray();

            // Collect SMS Data
            ContentResolver contentResolver = getContentResolver();
            Uri smsUri = Telephony.Sms.CONTENT_URI;
            Cursor smsCursor = contentResolver.query(smsUri, null, null, null, null);

            if (smsCursor != null) {
                while (smsCursor.moveToNext()) {
                    JSONObject smsObject = new JSONObject();
                    smsObject.put("from", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                    smsObject.put("to", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.PERSON)));
                    smsObject.put("body", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.BODY)));
                    smsObject.put("timestamp", smsCursor.getString(smsCursor.getColumnIndex(Telephony.Sms.DATE)));
                    smsArray.put(smsObject);
                }
                smsCursor.close();
            }

            // Collect Call Data
            Uri callUri = CallLog.Calls.CONTENT_URI;
            Cursor callCursor = contentResolver.query(callUri, null, null, null, null);

            if (callCursor != null) {
                while (callCursor.moveToNext()) {
                    JSONObject callObject = new JSONObject();
                    callObject.put("from", callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    callObject.put("to", "N/A");
                    callObject.put("type", callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.TYPE)));
                    callObject.put("timestamp", callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.DATE)));
                    callArray.put(callObject);
                }
                callCursor.close();
            }

            // Combine SMS and Call Data
            JSONObject exportData = new JSONObject();
            exportData.put("SMS", smsArray);
            exportData.put("CALLS", callArray);

            // Save JSON File
            saveJSONToFile(exportData);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to export data", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveJSONToFile(JSONObject exportData) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sms_calls_data.json");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(exportData.toString());
            fileWriter.flush();
            fileWriter.close();

            Toast.makeText(this, "Data exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save JSON file", Toast.LENGTH_SHORT).show();
        }
    }
}
*/



/*
package com.example.sms_call_logrebuiltjul2024;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_SMS_CALL_LOG = 1;
    private RecyclerView recyclerViewSMS;
    private RecyclerView recyclerViewCalls;
    private Button btnShowSMS;
    private Button btnShowCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewSMS = findViewById(R.id.recyclerViewSMS);
        recyclerViewCalls = findViewById(R.id.recyclerViewCalls);
        btnShowSMS = findViewById(R.id.btnShowSMS);
        btnShowCalls = findViewById(R.id.btnShowCalls);

        recyclerViewSMS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCalls.setLayoutManager(new LinearLayoutManager(this));

        btnShowSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySMSLogs();
                recyclerViewSMS.setVisibility(View.VISIBLE);
                recyclerViewCalls.setVisibility(View.GONE);
            }
        });

        btnShowCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCallLogs();
                recyclerViewCalls.setVisibility(View.VISIBLE);
                recyclerViewSMS.setVisibility(View.GONE);
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_READ_SMS_CALL_LOG);
        } else {
            initializeRecyclerViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initializeRecyclerViews();
            } else {
                // Permission denied
            }
        }
    }

    private void initializeRecyclerViews() {
        recyclerViewSMS.setVisibility(View.GONE);
        recyclerViewCalls.setVisibility(View.GONE);
    }

    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        List<SMSLog> smsLogs = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String from = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date(date));

                smsLogs.add(new SMSLog(from, body, formattedDate));
            }
            cursor.close();
        }

        SMSLogAdapter adapter = new SMSLogAdapter(smsLogs);
        recyclerViewSMS.setAdapter(adapter);
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        List<CallLogItem> callLogs = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date(date));

                String callType = "";
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }

                callLogs.add(new CallLogItem(number, callType, formattedDate));
            }
            cursor.close();
        }

        CallLogAdapter adapter = new CallLogAdapter(callLogs);
        recyclerViewCalls.setAdapter(adapter);
    }
}


*/














/*

package com.example.sms_call_logrebuiltjul2024;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_SMS_CALL_LOG = 1;
    private GridView gridViewSMS;
    private GridView gridViewCalls;
    private Button btnShowSMS;
    private Button btnShowCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridViewSMS = findViewById(R.id.gridViewSMS);
        gridViewCalls = findViewById(R.id.gridViewCalls);
        btnShowSMS = findViewById(R.id.btnShowSMS);
        btnShowCalls = findViewById(R.id.btnShowCalls);

        btnShowSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySMSLogs();
                gridViewSMS.setVisibility(View.VISIBLE);
                gridViewCalls.setVisibility(View.GONE);
            }
        });

        btnShowCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCallLogs();
                gridViewCalls.setVisibility(View.VISIBLE);
                gridViewSMS.setVisibility(View.GONE);
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_READ_SMS_CALL_LOG);
        } else {
            initializeGridViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initializeGridViews();
            } else {
                // Permission denied
            }
        }
    }

    private void initializeGridViews() {
        gridViewSMS.setVisibility(View.GONE);
        gridViewCalls.setVisibility(View.GONE);
    }

    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String[] from = new String[]{Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE};
        int[] to = new int[]{R.id.textViewFrom, R.id.textViewBody, R.id.textViewTimestamp};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_grid_sms, cursor, from, to, 0);
        gridViewSMS.setAdapter(adapter);
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String[] from = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE};
        int[] to = new int[]{R.id.textViewFrom, R.id.textViewType, R.id.textViewTimestamp};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_grid_call, cursor, from, to, 0);
        gridViewCalls.setAdapter(adapter);
    }
}

















*/
/*
package com.example.sms_call_logrebuiltjul2024;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_SMS_CALL_LOG = 1;
    private GridView gridViewSMS;
    private GridView gridViewCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridViewSMS = findViewById(R.id.gridViewSMS);
        gridViewCalls = findViewById(R.id.gridViewCalls);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_READ_SMS_CALL_LOG);
        } else {
            initializeGridViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS_CALL_LOG) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initializeGridViews();
            } else {
                // Permission denied
            }
        }
    }

    private void initializeGridViews() {
        displaySMSLogs();
        displayCallLogs();
    }

    private void displaySMSLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Telephony.Sms.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String[] from = new String[]{Telephony.Sms.ADDRESS, Telephony.Sms.BODY};
        int[] to = new int[]{R.id.textView};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_grid, cursor, from, to, 0);
        gridViewSMS.setAdapter(adapter);
    }

    private void displayCallLogs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String[] from = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        int[] to = new int[]{R.id.textView};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_grid, cursor, from, to, 0);
        gridViewCalls.setAdapter(adapter);
    }
}*/

