package com.codegreed_devs.and_pos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Security_Pin extends AppCompatActivity {
    Button onebtn,twobtn,backbtn,threebtn,fourbtn,fivebtn,sixbtn,sevenbtn,eightbtn,ninebtn,okbtn,zerobtn;

    TextView passdisplay,security_header;
    String realpass;
    String displaypass;
    String enteredpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_pin);
        passdisplay=(TextView)findViewById(R.id.passdisplay);
        security_header=(TextView)findViewById(R.id.security_header);
        displaypass="";
        enteredpass="";

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    UserInfo userInfo=ds.getValue(UserInfo.class);
                   if (userInfo.getUserName().equals("pin_number")){
                       realpass=userInfo.getChefNumber();
                       break;
                   }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        onebtn=(Button)findViewById(R.id.onebtn);
        onebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"1";
                passdisplay.setText(displaypass);
            }
        });
        twobtn=(Button)findViewById(R.id.twobtn);
        twobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"2";
                passdisplay.setText(displaypass);
            }
        });
        threebtn=(Button)findViewById(R.id.threebtn);
        threebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"3";
                passdisplay.setText(displaypass);
            }
        });
        fourbtn=(Button)findViewById(R.id.fourbtn);
        fourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"4";
                passdisplay.setText(displaypass);
            }
        });
        fivebtn=(Button)findViewById(R.id.fivebtn);
        fivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"5";
                passdisplay.setText(displaypass);
            }
        });
        sixbtn=(Button)findViewById(R.id.sixbtn);
        sixbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"6";
                passdisplay.setText(displaypass);
            }
        });
        sevenbtn=(Button)findViewById(R.id.sevenbtn);
        sevenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"7";
                passdisplay.setText(displaypass);
            }
        });
        eightbtn=(Button)findViewById(R.id.eightbtn);
        eightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"8";
                passdisplay.setText(displaypass);
            }
        });
        ninebtn=(Button)findViewById(R.id.ninebtn);
        ninebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"9";
                passdisplay.setText(displaypass);
            }
        });
        zerobtn=(Button)findViewById(R.id.zerobtn);
        zerobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaypass=displaypass+"*";
                enteredpass=enteredpass+"0";
                passdisplay.setText(displaypass);
            }
        });
        backbtn=(Button)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredpass.isEmpty()){
                    Intent intent=getIntent();
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }else {

                    displaypass=displaypass.substring(0,displaypass.length()-1);
                    enteredpass=enteredpass.substring(0,enteredpass.length()-1);
                    passdisplay.setText(displaypass);
                }
            }
        });
        okbtn=(Button)findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredpass.equals(realpass)){
                    Intent x=new Intent(Security_Pin.this,MainActivity.class);
                    startActivity(x);
                    finish();
                }else{
                    passdisplay.setText("Wrong Password");
                    displaypass="";
                    enteredpass="";
                }
            }
        });
    }
}
