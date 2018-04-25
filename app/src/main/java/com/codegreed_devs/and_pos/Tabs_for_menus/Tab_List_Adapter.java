package com.codegreed_devs.and_pos.Tabs_for_menus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.codegreed_devs.and_pos.MainActivity;
import com.codegreed_devs.and_pos.Produc_Progress;
import com.codegreed_devs.and_pos.R;
import com.codegreed_devs.and_pos.Reports_And_Graphs.FirebaseGet;
import com.codegreed_devs.and_pos.UpdateFoodMenu;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by FakeJoker on 18/02/2018.
 */

public class Tab_List_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Tabbed_Menu_Item> tabbed_menu_items;
    ArrayList<FirebaseGet> firebaseGetArrayList;
    DatabaseReference databaseReference_get_total;
    String menu_type;

    public Tab_List_Adapter(Context context, ArrayList<Tabbed_Menu_Item> tabbed_menu_items, String menu_type) {
        this.context = context;
        this.tabbed_menu_items =tabbed_menu_items;
        this.menu_type=menu_type;
        firebaseGetArrayList=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return tabbed_menu_items.size();
    }

    @Override
    public Object getItem(int position) {
        return tabbed_menu_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.tab_list_item_layout,null);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Produc_Progress.class);
                intent.putExtra("product_name",tabbed_menu_items.get(position).getName());
                context.startActivity(intent);
            }
        });
        ImageView trash=convertView.findViewById(R.id.trash_update);
        TextView txt_price_update=convertView.findViewById(R.id.txt_price_update);
        txt_price_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_Quantity(position);
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setContentText("Are You sure You want To remove "+tabbed_menu_items.get(position).getName()+" From your menu?");
                pDialog.setConfirmText("Yes");
                pDialog.setCancelText("No");
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        delete_item(menu_type,tabbed_menu_items.get(position).getName());
                        UpdateFoodMenu.viewPager.getAdapter().notifyDataSetChanged();
                        pDialog
                                .setTitleText("Deleted!")
                                .setContentText(tabbed_menu_items.get(position).getName()+" Successfully removed")
                                .setConfirmText("OK")
                                .setCancelText(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                });
                pDialog.show();


            }
        });
        TextView txt_name= convertView.findViewById(R.id.txt_name);
        TextView txt_price= convertView.findViewById(R.id.txt_price_update);
        txt_name.setText(tabbed_menu_items.get(position).getName());
        txt_price.setText("Ksh."+tabbed_menu_items.get(position).getPrice());
        return convertView;
    }
    public  void delete_item(String main_child, final String f_name){
        databaseReference_get_total= FirebaseDatabase.getInstance().getReference().child(main_child);
        databaseReference_get_total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    FirebaseGet each_food=ds.getValue(FirebaseGet.class);
                    if(each_food.getName().equals(f_name)){
                        Toast.makeText(context, ""+ds.getKey(), Toast.LENGTH_SHORT).show();
                        databaseReference_get_total.child(ds.getKey()).setValue(null);
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void update_item(String child_name,final String f_name,Double price){
        final FirebaseGet firebaseGet=new FirebaseGet();
        firebaseGet.setName(f_name);
        firebaseGet.setPrice(price);
        databaseReference_get_total= FirebaseDatabase.getInstance().getReference().child(child_name);
        databaseReference_get_total.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    FirebaseGet each_food=ds.getValue(FirebaseGet.class);
                    if(each_food.getName().equals(f_name)){
                        Toast.makeText(context, ""+ds.getKey(), Toast.LENGTH_SHORT).show();
                        databaseReference_get_total.child(ds.getKey()).setValue(firebaseGet);
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void set_Quantity(final int pos){
        AlertDialog.Builder seta=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2=layoutInflater.inflate(R.layout.set_quantity_layout,null);
        seta.setMessage("Please Enter The New Product Price");
        seta.setIcon(R.mipmap.ic_launcher);
        final EditText price_qt=(EditText)view2.findViewById(R.id.quantity_setter);
        price_qt.setBackgroundResource(R.drawable.image_holder);
        price_qt.setInputType(InputType.TYPE_CLASS_NUMBER);
        price_qt.setMaxWidth(10);
        seta.setTitle("And POS ");
        seta.setView(price_qt);
        seta.setCancelable(true);
        seta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double price=Double.parseDouble(price_qt.getText().toString());
                update_item(menu_type,tabbed_menu_items.get(pos).getName(),price);
            }
        });
        seta.setView(view2).create().show();
    }

}
