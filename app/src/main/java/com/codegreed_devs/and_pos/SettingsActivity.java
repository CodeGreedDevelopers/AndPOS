package com.codegreed_devs.and_pos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.Reports_And_Graphs.FirebaseGet;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {
    String selected_theme,display_name;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Spinner spinner;
    String [] themes={"Select Theme","default","green","purple"};
    Button btn,ok_btn,set_pin;
    android.support.v7.widget.Toolbar settings_toolbar;
    ImageView ic_edt_phone;
    AlertDialog.Builder phone_dialog,pin_dialog;
    AlertDialog alertDialog;
    TextView phone_display;
    EditText phone_no,old_pin,new_pin;
    boolean updated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        spinner = findViewById(R.id.theme);
        settings_toolbar= findViewById(R.id.settings_toolbar);
        setSupportActionBar(settings_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone_display= findViewById(R.id.phone_display);
        //update colors as per theme
        UpdateColors();

        LinearLayout up_pin=findViewById(R.id.set_pin_btn_major);
        up_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_update();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, themes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 1:
                        Utils.changeToTheme(SettingsActivity.this, Utils.THEME_DEFAULT);
                        //update the preferences
                        editor=preferences.edit();
                        editor.putString("theme","default");
                        editor.apply();
                        restartApp();
                        break;

                    case 2:
                        Utils.changeToTheme(SettingsActivity.this, Utils.THEME_GREEN);
                        //update the preferences
                        editor=preferences.edit();
                        editor.putString("theme","green");
                        editor.apply();
                        restartApp();
                        break;

                    case 3:
                        Utils.changeToTheme(SettingsActivity.this, Utils.THEME_PURPLE);
                        //update the preferences
                        editor=preferences.edit();
                        editor.putString("theme","purple");
                        editor.apply();
                        restartApp();
                        break;


                }
                btn=findViewById(R.id.btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SignOut();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ic_edt_phone=findViewById(R.id.ic_edt_phone);
        ic_edt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_phone_number();
            }
        });



    }
    private void UpdateColors(){
        //get the saved theme from preference
        selected_theme=preferences.getString("theme",null);

        //load the user selected theme first before displaying views
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    settings_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    spinner.setBackgroundResource(R.drawable.image_holder_green);

                    break;

                case "purple":
                    settings_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    spinner.setBackgroundResource(R.drawable.image_holder_purple);

                    break;

                default:
                    settings_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    spinner.setBackgroundResource(R.drawable.image_holder);

                    break;

            }

        }

    }

    private void DisplayTheme() {
        //get the saved theme from preference
        selected_theme=preferences.getString("theme",null);

        Utils.onActivityCreateSetTheme(this);

        //load the user selected theme first before displaying views
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    setTheme(R.style.ThemeGreen);

                    break;

                case "purple":
                    setTheme(R.style.ThemePurple);

                    break;

                default:
                    setTheme(R.style.AppTheme);

                    break;

            }

        }
    }
    public void SignOut(){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Return to sign in
                        Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }
    public void restartApp(){
        Intent intent=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void set_phone_number(){
        phone_dialog=new AlertDialog.Builder(SettingsActivity.this);
        final LayoutInflater inflater=SettingsActivity.this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.add_chef_no,null);
        LinearLayout linearLayout=view.findViewById(R.id.header_add_chef);

        phone_dialog.setView(view);
        alertDialog=phone_dialog.show();
        phone_no= view.findViewById(R.id.phone_no);
        ok_btn= view.findViewById(R.id.ok_btn);
        //stting themes
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    ok_btn.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    phone_no.setBackgroundResource(R.drawable.image_holder_green);
                    break;

                case "purple":
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    ok_btn.setTextColor(getResources().getColor(R.color.colorPrimaryPurple));
                    phone_no.setBackgroundResource(R.drawable.image_holder_purple);
                    break;

                default:
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    ok_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                    phone_no.setBackgroundResource(R.drawable.image_holder);
                    break;

            }

        }

        //end of setting theme

        //click listener for the ok button
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ins_phone=phone_no.getText().toString().trim();
                display_name="chef_number";
                if (ins_phone.isEmpty()){
                    Toast.makeText(SettingsActivity.this, "No number added", Toast.LENGTH_SHORT).show();
                }else{
                    final DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("UserInfo")){
                                Toast.makeText(SettingsActivity.this, "Child present", Toast.LENGTH_SHORT).show();
                                //update_it("chef_number",ins_phone);
                                update_it(display_name,ins_phone);
                            }else{
                                UserInfo userInfo=new UserInfo();
                                userInfo.setChefNumber(ins_phone);
                                userInfo.setUserName(display_name);
                                reference.child("UserInfo")
                                        .push()
                                        .setValue(userInfo);
                                Toast.makeText(SettingsActivity.this, "No Child present", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    alertDialog.dismiss();
                    phone_display.setText(ins_phone);



                }
            }
        });
    }

    public void update_it(final String f_name,String num){
        final UserInfo userInfo=new UserInfo();
        userInfo.setUserName(f_name);
        userInfo.setChefNumber(num);
        updated=false;
        final DatabaseReference databaseReference_1= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        databaseReference_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(SettingsActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserInfo each_food=ds.getValue(UserInfo.class);
                    if(each_food.getUserName().equals(f_name)){
                        //Toast.makeText(SettingsActivity.this, ""+ds.getKey(), Toast.LENGTH_SHORT).show();
                        databaseReference_1.child(ds.getKey()).setValue(userInfo);
                        updated=true;
                    }
                }
                if (updated==false){
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("UserInfo");
                    reference.push().setValue(userInfo);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void pin_update(){
        pin_dialog=new AlertDialog.Builder(SettingsActivity.this);
        final LayoutInflater inflater=SettingsActivity.this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.add_security_pin,null);
        LinearLayout linearLayout=view.findViewById(R.id.header_security_pin);

        pin_dialog.setView(view);
        alertDialog=pin_dialog.show();
        old_pin= view.findViewById(R.id.old_pin);
        new_pin= view.findViewById(R.id.new_pin);
        set_pin= view.findViewById(R.id.set_pin_btn);
        //stting themes
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    set_pin.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    old_pin.setBackgroundResource(R.drawable.image_holder_green);
                    new_pin.setBackgroundResource(R.drawable.image_holder_green);
                    break;

                case "purple":
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    set_pin.setTextColor(getResources().getColor(R.color.colorPrimaryPurple));
                    old_pin.setBackgroundResource(R.drawable.image_holder_purple);
                    new_pin.setBackgroundResource(R.drawable.image_holder_purple);
                    break;

                default:
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    set_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
                    new_pin.setBackgroundResource(R.drawable.image_holder);
                    old_pin.setBackgroundResource(R.drawable.image_holder);
                    break;

            }

        }

        //end of setting theme

        //click listener for the ok button
        set_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldpin=old_pin.getText().toString().trim();
                final String newpin=new_pin.getText().toString().trim();
                display_name="pin_number";
                if (oldpin.isEmpty()||newpin.isEmpty()){
                    Toast.makeText(SettingsActivity.this, "Please Fill in all the Details", Toast.LENGTH_SHORT).show();
                }else{
                    final DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("UserInfo")){
                                Toast.makeText(SettingsActivity.this, "Child present", Toast.LENGTH_SHORT).show();
                                //update_it("chef_number",ins_phone);
                                update_it(display_name,newpin);
                            }else{
                                UserInfo userInfo=new UserInfo();
                                userInfo.setChefNumber(newpin);
                                userInfo.setUserName(display_name);
                                reference.child("UserInfo")
                                        .push()
                                        .setValue(userInfo);
                                Toast.makeText(SettingsActivity.this, "No Child present", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    alertDialog.dismiss();



                }
            }
        });

    }
}
