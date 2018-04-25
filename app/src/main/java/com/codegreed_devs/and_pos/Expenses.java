package com.codegreed_devs.and_pos;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.Purchases.Firebase_Helper;
import com.codegreed_devs.and_pos.Purchases.Firebase_Purchase_Save;
import com.codegreed_devs.and_pos.Purchases.Purchased_Items;
import com.codegreed_devs.and_pos.Purchases.Purchased_List_Adapter;
import com.codegreed_devs.and_pos.Purchases.Purchases_Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Expenses extends AppCompatActivity {

    public static ListView expenses_list;
    DatabaseReference databaseReference;
    public static Purchased_List_Adapter list_adapter;
    FireBase_Helper_Expenses firebase_helper;
    Firebase_Purchase_Save firebase_helper_save;
    AlertDialog alertDialog;
    String current_date;
    //    for themes
    String selected_theme;
    SharedPreferences preferences;
    android.support.v7.widget.Toolbar purchases_toolbar;
    LinearLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //theme initialization
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        purchases_toolbar= findViewById(R.id.expenses_toolbar);
        setSupportActionBar(purchases_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //update colors as per theme
        UpdateColors();

        setSupportActionBar(purchases_toolbar);
        current_date= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());

        expenses_list= findViewById(R.id.expenses_list);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Expenses").child(current_date);

        firebase_helper=new FireBase_Helper_Expenses(databaseReference);
        list_adapter=new Purchased_List_Adapter(this,firebase_helper.retrieve());
        expenses_list.invalidateViews();
        expenses_list.setAdapter(list_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.purchases_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            alert_Dialog_fun();
        }

        return super.onOptionsItemSelected(item);
    }
    public void alert_Dialog_fun(){
        AlertDialog.Builder purchase_dialog=new AlertDialog.Builder(Expenses.this);
        LayoutInflater inflater=Expenses.this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_purchase,null);


        final EditText prduct_cost= view.findViewById(R.id.product_cost);
        final EditText product_name= view.findViewById(R.id.product_name);
        TextView textView_header=view.findViewById(R.id.textVie_header);
        textView_header.setText("Add Expense");
        prduct_cost.setHint("Amount");
        product_name.setHint("Enter the Bill type");
        Button add_product_btn= view.findViewById(R.id.add_product_btn);
        header= view.findViewById(R.id.header);

        // start of changing colors of the buttons according to theme
        if(selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    add_product_btn.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    product_name.setBackgroundResource(R.drawable.image_holder_green);
                    prduct_cost.setBackgroundResource(R.drawable.image_holder_green);
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    break;
                case "purple":
                    add_product_btn.setTextColor(getResources().getColor(R.color.colorPrimaryPurple));
                    product_name.setBackgroundResource(R.drawable.image_holder_purple);
                    prduct_cost.setBackgroundResource(R.drawable.image_holder_purple);
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    break;
                default:
                    add_product_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                    product_name.setBackgroundResource(R.drawable.image_holder);
                    prduct_cost.setBackgroundResource(R.drawable.image_holder);
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;

            }
        }

        purchase_dialog.setView(view);
        alertDialog=purchase_dialog.show();

        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prduct_cost.getText().toString().isEmpty()||product_name.getText().toString().isEmpty()){
                    Toast.makeText(Expenses.this, "Fill in all details", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Expenses");
                    String p_name=product_name.getText().toString().trim();
                    String p_cost=prduct_cost.getText().toString().trim();
                    Purchased_Items save_purchased_items=new Purchased_Items();
                    save_purchased_items.setName(p_name);
                    save_purchased_items.setPrice(Double.parseDouble(p_cost));


                    firebase_helper_save=new Firebase_Purchase_Save(reference);
                    firebase_helper_save.save(save_purchased_items,current_date+"/day_data");
                    expenses_list.invalidateViews();


                    Toast.makeText(getApplicationContext(), "Product Successfully Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
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
                    purchases_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));

                    break;

                case "purple":
                    purchases_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));

                    break;

                default:
                    purchases_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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
}
