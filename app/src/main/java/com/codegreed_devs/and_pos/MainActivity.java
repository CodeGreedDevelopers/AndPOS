package com.codegreed_devs.and_pos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.GridView_Elements.FirebaseHelper;
import com.codegreed_devs.and_pos.GridView_Elements.GridAdapter;
import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.codegreed_devs.and_pos.Purchases.Firebase_Sales_Save;
import com.codegreed_devs.and_pos.Purchases.Purchases_Activity;
import com.codegreed_devs.and_pos.Reports_And_Graphs.ReportsActivity;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static GridView gridView;
    DatabaseReference  db;
    public static FirebaseHelper firebaseHelper;
    public static GridAdapter gridAdapter;
    AlertDialog alertDialog;
    BluetoothAdapter bluetoothAdapter;
    Double cash_entered;
    LinearLayoutCompat linearLayoutCompat;
    LinearLayout sidenavLayout,header;
    TextView change_holder;
//    for themes
    String selected_theme,mymsg,phone_num;
    SharedPreferences preferences;

    ListView listView;
    List<Products> productsList;
    CartListAdapter cartListAdapter;
    public  static Double total_cash=0.00;
    public static TextView total_cash_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //theme initialization
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting firebase accessible offline
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //getting the grid view and initializing it
        gridView= findViewById(R.id.gridview);
        linearLayoutCompat = findViewById(R.id.pay_layout);

        NavigationView navigationView2 = findViewById(R.id.nav_view);
        View header2=navigationView2.getHeaderView(0);
        sidenavLayout=(LinearLayout)header2.findViewById(R.id.side_nav_header);

        //Update colors as per theme
        UpdateColors();

        //initiializing firebase
        db= FirebaseDatabase.getInstance().getReference().child("Menu_Item_1");
        firebaseHelper=new FirebaseHelper(db);
        
        //initilizing adapter and setting it
        gridAdapter=new GridAdapter(this,firebaseHelper.retrieve());
        gridView.setAdapter(gridAdapter);
        //end of setting adapter to the gridview

        productsList=new ArrayList<>();//this is used to set up the array adapter for list

        cartListAdapter=new CartListAdapter(this,productsList);//initializing custom array
        listView= findViewById(R.id.cart);

        total_cash_txt= findViewById(R.id.total_cash);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,""+firebaseHelper.retrieve().get(position).getName(), Toast.LENGTH_SHORT).show();
                if (productsList.size() != 0) {
                    int pos = searchProduct(firebaseHelper.retrieve().get(position).getName());

                    if (pos != -1) {
                        //Toast.makeText(MainActivity.this, "" + pos, Toast.LENGTH_SHORT).show();
                        int x = productsList.get(pos).getQuantity();
                        productsList.get(pos).setQuantity(x + 1); //items in the cart
                        total_cash = total_cash + Double.parseDouble(String.valueOf(firebaseHelper.retrieve().get(position).getPrice()));
                        total_cash_txt.setText("Ksh." + total_cash);
                        cartListAdapter.notifyDataSetChanged();
                        listView.setAdapter(cartListAdapter);
                    } else {
                        productsList.add(new Products(firebaseHelper.retrieve().get(position).getName(),Double.parseDouble(String.valueOf(firebaseHelper.retrieve().get(position).getPrice())) , 1));
                        total_cash = total_cash + Double.parseDouble(String.valueOf(firebaseHelper.retrieve().get(position).getPrice()));
                        total_cash_txt.setText("Ksh." + total_cash);
//                        Toast.makeText(MainActivity.this, "Also Running", Toast.LENGTH_SHORT).show();
                        cartListAdapter.notifyDataSetChanged();
                        listView.setAdapter(cartListAdapter);
                    }
                } else {
                    productsList.add(new Products(firebaseHelper.retrieve().get(position).getName(),Double.parseDouble(String.valueOf(firebaseHelper.retrieve().get(position).getPrice())), 1));
                    total_cash = total_cash + Double.parseDouble(String.valueOf(firebaseHelper.retrieve().get(position).getPrice()));
                    total_cash_txt.setText("Ksh." + total_cash);
                    //Toast.makeText(MainActivity.this, "Running", Toast.LENGTH_SHORT).show();
                    cartListAdapter.notifyDataSetChanged();
                    listView.setAdapter(cartListAdapter);
                }
            }




        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);



    }
    public void updateGridView(String menu_type){
        db= FirebaseDatabase.getInstance().getReference().child(menu_type);
        firebaseHelper=new FirebaseHelper(db);
        gridAdapter=new GridAdapter(this,firebaseHelper.retrieve());
        gridView.invalidateViews();
        gridView.setAdapter(gridAdapter);
    }
    public static void update_bill(Double price,int quantity){
        Double minus=price*quantity;
        total_cash=total_cash-minus;
        total_cash_txt.setText("Ksh."+total_cash);
    }
    /* this method is used to search from the arraylist ro check of the data is available
        if the data is available it returns the index of the data
        but if not available it returns -1
     */
    public int searchProduct(String p_name){
        for (int p=0;p<productsList.size();p++) {
            if(productsList.get(p).getP_name().equals(p_name)){
                return p;
            }
        }
      return -1;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_drinks) {
            // Handle the camera action
            updateGridView("Menu_Item_4");
        } else if (id == R.id.nav_breakfast) {
            updateGridView("Menu_Item_1");
        } else if (id == R.id.nav_lunch) {
            updateGridView("Menu_Item_2");
        } else if (id == R.id.nav_supper) {
            updateGridView("Menu_Item_3");
        } else if (id == R.id.nav_update_menu) {
            Intent intent=new Intent(this,UpdateFoodMenu.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchases) {
            Intent intent=new Intent(this,Purchases_Activity.class);
            startActivity(intent);
        }else if (id==R.id.nav_reports){
            Intent intent=new Intent(this,ReportsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_expenses) {
            Intent intent = new Intent(this, Expenses.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void alert_Dialog_fun(View v){
        AlertDialog.Builder payment_dialog=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater=MainActivity.this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_payment,null);
        header =view.findViewById(R.id.header);
        change_holder =view.findViewById(R.id.change_holder);
        TabLayout tabLayout= view.findViewById(R.id.payment_slide);
        tabLayout.addTab(tabLayout.newTab().setText("Cash"));
        tabLayout.addTab(tabLayout.newTab().setText("Mpesa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Button add_payment= view.findViewById(R.id.payment_btn);
        final EditText cash_entry= view.findViewById(R.id.cash_entry);
        final TextView total_holder= view.findViewById(R.id.total_holder);
        final TextView change_holder= view.findViewById(R.id.change_holder);
        final TextView paid_holder= view.findViewById(R.id.paid_holder);
        total_holder.append("Ksh."+total_cash);
        cash_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cash_entry.getText().toString().isEmpty()){
                    paid_holder.setText("Piad: Ksh.0.00");
                    change_holder.setText("Ksh. 0.00");
                }else {
                    paid_holder.setText("Piad: Ksh." + cash_entry.getText().toString());
                    cash_entered = Double.parseDouble(cash_entry.getText().toString());
                    change_holder.setText("Ksh." + (cash_entered - total_cash));
                }
            }
        });

        // start of changing colors of the buttons according to theme
        if(selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    add_payment.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    change_holder.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    cash_entry.setBackgroundResource(R.drawable.image_holder_green);
                    break;
                case "purple":
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    add_payment.setTextColor(getResources().getColor(R.color.colorPrimaryPurple));
                    change_holder.setTextColor(getResources().getColor(R.color.colorPrimaryPurple));
                    cash_entry.setBackgroundResource(R.drawable.image_holder_purple);
                    break;
                default:
                    header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    add_payment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    change_holder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    cash_entry.setBackgroundResource(R.drawable.image_holder);
                    break;

            }
        }
        //end of setting theme
        add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(cash_entry.getText().toString())<total_cash){
                    Toast.makeText(MainActivity.this, "Please Enter Enough cash", Toast.LENGTH_SHORT).show();
                }else if (cash_entry.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Some Cash", Toast.LENGTH_SHORT).show();
                }else{
                    paid_holder.setText("Piad: Ksh."+cash_entry.getText().toString());
                    cash_entered=Double.parseDouble(cash_entry.getText().toString());
                    change_holder.setText("Ksh."+(cash_entered-total_cash));
                    printTextBulider();
                    DatabaseReference sv=FirebaseDatabase.getInstance().getReference();
                    Firebase_Sales_Save firebase_helper=new Firebase_Sales_Save(sv);
                    for (Products products:productsList) {
                        String food_name=products.getP_name();
                        int quantity=products.getQuantity();
                        double price=products.getP_price();
                        double total_price= quantity*price;
                        String current_date= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());

//                      Toast.makeText(MainActivity.this, "Food: "+food_name+" Total: "+total_price, Toast.LENGTH_SHORT).show();
                        Menu_Item menu_item=new Menu_Item();
                        menu_item.setPrice((int) total_price);
                        menu_item.setName(food_name);
                        firebase_helper.save(menu_item, "Sales",current_date+"/day_data");

                        //Toast.makeText(MainActivity.this, "Saved successfully"+current_date, Toast.LENGTH_SHORT).show();

                    }

                    send_message();
                    total_cash=0.0;
                    total_cash_txt.setText("Ksh. 0.00");
                    alertDialog.dismiss();

                }
            }
        });

        payment_dialog.setView(view);
        alertDialog=payment_dialog.show();
    }

    public void print_Reciept(final String txt){
         /*
        start of creating a printing adapter

         */
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        //final Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_chicken);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice  mdevice=bluetoothAdapter.getBondedDevices().iterator().next();

        final BluetoothPrinter bluetoothPrinter=new BluetoothPrinter(mdevice);

        bluetoothPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {
            @Override
            public void onConnected() {
                bluetoothPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                bluetoothPrinter.printText("CASA CAFE");
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText("P.O. Box 61");
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText("Kabarak");
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText("Tel: 0703430023");
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText("Time: "+formattedDate);
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText("-------------------------------");
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.printText(txt);
                bluetoothPrinter.addNewLine();
                bluetoothPrinter.finish();
            }

            @Override
            public void onFailed() {
                Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

        //end of bluetooth
    }
    public void printTextBulider(){
        String mywords="";
        for (Products products:productsList) {

            mywords=mywords+products.getQuantity()+" "+products.getP_name()+"\t\t\t"+(products.getP_price()*products.getQuantity())+"\n";

        }
        mywords=mywords+"\n-------------------------------"+"\nTotal Cash: Ksh."+total_cash+"\n Paid : Ksh."+cash_entered+"\nChange : Ksh."+(cash_entered-total_cash)+"\n-------------------------------\n Thank You For Choosing CASA \nCafe Welcome Again\n Powered By:\nAfrimax Technologies Kenya\n\n";
        print_Reciept(mywords);
    }
    public void send_message(){
        mymsg="";
        for (Products products:productsList) {

            mymsg=mymsg+products.getQuantity()+" "+products.getP_name()+"\n";
        }


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    UserInfo userInfo=ds.getValue(UserInfo.class);
                    if (userInfo.getUserName().equals("chef_number")){
                        sendsms(mymsg,userInfo.getChefNumber());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public  void  sendsms(String msg,String ph_num) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(ph_num,null,msg,null,null);
        } catch (Exception ex) {
            Toast.makeText(this, "Chef Not Notified", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        productsList.clear();
        listView.invalidateViews();
    }
    private void UpdateColors(){
        //get the saved theme from preference
        selected_theme=preferences.getString("theme",null);

        //load the user selected theme first before displaying views
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    linearLayoutCompat.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    sidenavLayout.setBackgroundResource(R.drawable.side_nav_bar_green);


                    break;

                case "purple":
                    linearLayoutCompat.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    sidenavLayout.setBackgroundResource(R.drawable.side_nav_bar_purple);
                    break;

                default:
                    linearLayoutCompat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    sidenavLayout.setBackgroundResource(R.drawable.side_nav_bar);
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
