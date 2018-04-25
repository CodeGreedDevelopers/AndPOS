package com.codegreed_devs.and_pos.Reports_And_Graphs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;
import com.codegreed_devs.and_pos.R;


import com.codegreed_devs.and_pos.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.Locale;


public class ReportsActivity extends AppCompatActivity {
    public static ListView reports_list;
    LineChart line_chart;
    DatabaseReference databaseReference,purchase_reference;
    Toolbar reports_toolbar;
    String name;
    DatabaseReference databaseReference_get_total;
    ArrayList<FirebaseGet> firebaseGets;
    Double total=0.0,total_2=0.0;
    List<Entry> valsComp1,valsComp2;
    //    for themes
    String selected_theme;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //theme initialization
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar=findViewById(R.id.reports_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reports_toolbar=findViewById(R.id.reports_toolbar);

        UpdateColors();
        reports_list= findViewById(R.id.reports_list);
        line_chart= findViewById(R.id.line_chart);
        valsComp1= new ArrayList<Entry>();
        valsComp2 = new ArrayList<Entry>();
        firebaseGets=new ArrayList<>();
        String current_date= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Sales");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(ReportsActivity.this,""+ dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.getKey();
                    get_data_1("Sales",name+"/day_data");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, "On Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

        purchase_reference=FirebaseDatabase.getInstance().getReference().child("Purchases");
        purchase_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nm = ds.getKey();
                    //Toast.makeText(ReportsActivity.this, "day"+nm, Toast.LENGTH_SHORT).show();
                    get_data_2("Purchases",nm+"/day_data");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        valsComp1.add(new Entry(0f, 0f));
        valsComp2.add(new Entry(0f, 0f));

        set_View();

    }
    public void set_View(){
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Sales In a Day");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setCircleColor(Color.BLACK);
        setComp1.setColor(Color.BLUE);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Purchases In a Day");
        setComp2.setColor(Color.RED);
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData data = new LineData(dataSets);
        line_chart.setData(data);
        line_chart.invalidate(); // refresh
    }
    public  void get_data_1(String main_child,String sub_child){
        firebaseGets.clear();

        databaseReference_get_total= FirebaseDatabase.getInstance().getReference().child(main_child).child(sub_child);
        databaseReference_get_total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total=0.0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    FirebaseGet item_prices=ds.getValue(FirebaseGet.class);
                    firebaseGets.add(item_prices);
                    total=total+item_prices.getPrice();
                    //Toast.makeText(ReportsActivity.this, ""+total, Toast.LENGTH_SHORT).show();

                }
                valsComp1.add(new Entry(valsComp1.size()+1,total.floatValue()));
                set_View();
                line_chart.invalidate();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public  void get_data_2(String main_child,String sub_child){
        firebaseGets.clear();
        databaseReference_get_total= FirebaseDatabase.getInstance().getReference().child(main_child).child(sub_child);
        databaseReference_get_total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total_2=0.0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    FirebaseGet item_price_p=ds.getValue(FirebaseGet.class);
                    firebaseGets.add(item_price_p);

                    //Toast.makeText(ReportsActivity.this, "Purchases:"+item_price_p.getPrice(), Toast.LENGTH_SHORT).show();
                    total_2=total_2+item_price_p.getPrice();

                }
                valsComp2.add(new Entry(valsComp2.size()+1,total_2.floatValue()));
                set_View();
                line_chart.invalidate();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    reports_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    break;

                case "purple":
                    reports_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    break;

                default:
                    reports_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
