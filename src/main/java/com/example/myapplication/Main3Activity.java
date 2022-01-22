package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//หน้าตั้งค่า
public class Main3Activity extends AppCompatActivity {
    public Button button,delete,nextqueue;
    Context context;
    CharSequence chartext;
    int duration,nxadcount;
    Toast toast;
    String adcount;
    TextView textView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database2.getReference("adcount");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        button = findViewById(R.id.B4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });

        delete = findViewById(R.id.B6);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
                builder.setTitle("Are you sure");
                builder.setMessage("จะรีเซ็ทคิว user และคิวปัจจุบัน");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {setzero();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        nextqueue = findViewById(R.id.nextq);
        nextqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getnext();
            }
        });

        textView = findViewById(R.id.adminq);

        admincount();

    }

    private void admincount() {
        ValueEventListener valueEventListener2 = (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adcount = snapshot.getValue(String.class);
                textView.setText(adcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef2.addValueEventListener(valueEventListener2);
    }

    private void getnext(){
        nxadcount = Integer.valueOf(adcount)+1;
        myRef2.setValue(String.valueOf(nxadcount));
    }

    private void setzero() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int setzero = Integer.valueOf(1);
                myRef.setValue(String.valueOf(setzero));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addValueEventListener(valueEventListener);

        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int setzero2 = Integer.valueOf(0);
                myRef2.setValue(String.valueOf(setzero2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef2.addValueEventListener(valueEventListener2);

        context = getApplicationContext();
        chartext = "SuccessResetQueue";
        toast = Toast.makeText(context,chartext,duration);
        toast.show();
    }

    private void openActivity1(){
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        startActivity(intent);
    }

}
