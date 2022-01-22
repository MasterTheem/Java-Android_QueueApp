package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//หน้าโชว์ลำดับคิว
public class AMain4Activity extends AppCompatActivity {
    private Button button;
    String readString = "0",value2="0";
    TextView adminq;
    int readint,value2int;



    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("message");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database2.getReference("adcount");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        textfromdb();

        getqfromadmin();

        adminq = findViewById(R.id.getfromdbcurrent);

        button = findViewById(R.id.B5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });

        adminq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                readint = Integer.parseInt(readString);
                value2int = Integer.parseInt(value2);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (readint == value2int){
                    Toast.makeText(Main4Activity.this,"ถึงคิวแล้ว",Toast.LENGTH_LONG).show();
                    gofinshq();

                }
            }
        });


    }

    private void getqfromadmin() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value2 = snapshot.getValue(String.class);
                adminq.setText(value2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef2.addValueEventListener(valueEventListener);

    }

    private void textfromdb() {

        final TextView textView = (TextView) findViewById(R.id.getfromdb);
        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                readString = datasnapshot.getValue(String.class);
                int nextvalue = Integer.valueOf(readString) + 1;
                textView.setText(readString);
                myRef.setValue(String.valueOf(nextvalue));
                Toast.makeText(Main4Activity.this, "คุณได้ลำดับคิวที่ " + readString, Toast.LENGTH_LONG).show();

                savenumber();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void savenumber() {
        String filename = "myfile";
        FileOutputStream fos;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(readString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openActivity1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("คำเตือน");
        builder.setMessage("กรุณาอย่าออกจากหน้านี้จนกว่าจะถึงคิวให้บริการของคุณ");

        builder.setPositiveButton("ออก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetnum();

                Intent intent = new Intent(Main4Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("ไม่ออก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void resetnum (){
        String filename = "myfile";
        String num0 = "0";
        FileOutputStream fos;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(num0.getBytes());
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void gofinshq (){
        Intent intent = new Intent(this, Main6Activity.class);
        startActivity(intent);

    }

}
