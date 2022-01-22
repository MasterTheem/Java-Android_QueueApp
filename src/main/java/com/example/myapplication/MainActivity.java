package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String[] CAMERAPERMISSION = {Manifest.permission.CAMERA};
    Button button1;
    Button exitbut;
    ImageButton settingb;
    String mytext,value,readString2;
    //TextView testt1,testt2;
    int ss;
    String readString = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permissioncamera
        verifyPermissions();

        button1 = findViewById(R.id.B1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        settingb = findViewById(R.id.settingbut);
        settingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        exitbut = findViewById(R.id.exit);
        exitbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitapp();
            }
        });

        //testt1 = findViewById(R.id.test1);

            readfile();

    }


    private void readfile() {
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
            //testt1.setText(readString);
        } catch (IOException e) {
            //testt1.setText(e.getMessage());
        }
        ss = Integer.parseInt(readString);
        if (ss > 0){
            Intent intent = new Intent(MainActivity.this ,Main5Activity.class);
            startActivity(intent);
        }
    }

    private void exitapp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }

    private void openActivity2(){
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    private void openActivity3(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(MainActivity.this);
        mydialog.setTitle("Only Staff");

        final EditText editText = new EditText(MainActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        mydialog.setView(editText);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                value = datasnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mydialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mytext = editText.getText().toString();
                //Toast.makeText(MainActivity.this,"your password "+mytext,Toast.LENGTH_LONG).show();
                ///////////////////////////

                if(mytext.equals(value)){
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,"Incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mydialog.setNegativeButton("canel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mydialog.show();

        //Intent intent = new Intent(MainActivity.this, Main3Activity.class);
        //startActivity(intent);
    }

    private void verifyPermissions(){
        Log.d(TAG,"verifyPermissions: Checking Permission.");
        int permissioncamera = ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA);

        if (permissioncamera != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,CAMERAPERMISSION,1);
        }
    }



}
