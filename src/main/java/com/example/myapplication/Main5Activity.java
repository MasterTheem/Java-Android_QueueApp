package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main5Activity extends AppCompatActivity {
    TextView adminq,userq,testl;
    Button exit;
    String readString = "0";
    String value2 = "0";
    int readint,value2int;
    boolean mpture = true;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("adcount");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        adminq = findViewById(R.id.getfromdbcurrent2);

        userq = findViewById(R.id.getfromdb2);

        exit = findViewById(R.id.B5_copy);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity1();
            }
        });

        readfile();

        getqfromadmin();


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
                    Toast.makeText(Main5Activity.this,"ถึงคิวแล้ว",Toast.LENGTH_LONG).show();
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
        myRef.addValueEventListener(valueEventListener);


    }

    private void readfile (){
        FileInputStream fis;
        InputStreamReader isr;
        StringBuilder sb = new StringBuilder();

        //TextView tv = (TextView) findViewById(R.id.test1);
        char[] inputbuffer = new char[2048];
        try {
            fis = openFileInput("myfile");
            isr = new InputStreamReader(fis);
            int i;
            while ((i = isr.read(inputbuffer)) != -1) {
                sb.append(inputbuffer, 0, i);
            }
            readString = sb.toString();
            userq.setText(readString);
        } catch (IOException e) {
            userq.setText(e.getMessage());
        }

    }

    private void openactivity1 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("คำเตือน");
        builder.setMessage("กรุณาอย่าออกจากหน้านี้จนกว่าจะถึงคิวให้บริการของคุณ");
        builder.setPositiveButton("ออก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetnumber();

                Intent intent = new Intent(Main5Activity.this , MainActivity.class);
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

    private void resetnumber(){
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

    private void apceptalarm (){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.alarm);
        mp.setLooping(mpture);
        mp.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ถึงคิวของคุญแล้ว");
        builder.setMessage("กดตกลงเพื่อปิดเสียง");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mp.stop();
                mpture = !mpture;
                resetnumber();
            }
        });
        builder.show();
    }

    private void gofinshq (){
        Intent intent = new Intent(this, Main6Activity.class);
        startActivity(intent);
    }

}
