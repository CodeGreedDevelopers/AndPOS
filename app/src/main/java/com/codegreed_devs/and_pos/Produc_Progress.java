package com.codegreed_devs.and_pos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.codegreed_devs.and_pos.Reports_And_Graphs.FirebaseGet;
import com.codegreed_devs.and_pos.Reports_And_Graphs.ReportsActivity;
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

import java.util.ArrayList;
import java.util.List;

public class Produc_Progress extends AppCompatActivity {
    LineChart lineChart;
    ArrayList<Entry> valsComp1;
    DatabaseReference databaseReference,databaseReference_get_total;
    Toolbar product_progress_toolbar;
    String name,product_name;
    ArrayList<FirebaseGet> firebaseGets;
    Double total=0.0;
    //for themes
    String selected_theme;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //theme initialization
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produc__progress);

        Toolbar toolbar=findViewById(R.id.product_progress_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        product_progress_toolbar=findViewById(R.id.product_progress_toolbar);
        UpdateColors();

        valsComp1= new ArrayList<Entry>();
        firebaseGets=new ArrayList<>();
        lineChart=findViewById(R.id.line_chart_product);
        product_name=getIntent().getStringExtra("product_name");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Sales");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(Produc_Progress.this,""+ dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.getKey();
                    get_data_1("Sales",name+"/day_data");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Produc_Progress.this, "On Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

        valsComp1.add(new Entry(0f, 0f));
        set_View();
    }
    public void set_View(){
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Sales In a Day");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setCircleColor(Color.BLACK);
        setComp1.setColor(Color.BLUE);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate(); // refresh
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
                    if (item_prices.getName().equals(product_name)){
                        total=total+item_prices.getPrice();
                    }
                    //Toast.makeText(ReportsActivity.this, ""+total, Toast.LENGTH_SHORT).show();

                }
                valsComp1.add(new Entry(valsComp1.size()+1,total.floatValue()));
                set_View();
                lineChart.invalidate();


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
                    product_progress_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    break;

                case "purple":
                    product_progress_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    break;

                default:
                    product_progress_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
