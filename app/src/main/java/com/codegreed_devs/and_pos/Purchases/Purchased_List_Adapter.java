package com.codegreed_devs.and_pos.Purchases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.codegreed_devs.and_pos.R;
import com.codegreed_devs.and_pos.Reports_And_Graphs.FirebaseGet;
import com.codegreed_devs.and_pos.Reports_And_Graphs.ReportsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 20/02/2018.
 */

public class Purchased_List_Adapter extends BaseAdapter {
    Context context;
    View view;
    LayoutInflater layoutInflater;
    ArrayList<Purchased_Items> purchased_items;

    public Purchased_List_Adapter(Context context, ArrayList<Purchased_Items> purchased_items) {
        this.context = context;
        this.purchased_items =purchased_items;
    }

    public Purchased_List_Adapter(ReportsActivity context, ArrayList<FirebaseGet> firebaseGets) {
        this.context = context;
        this.purchased_items =purchased_items;
    }

    @Override
    public int getCount() {
        return purchased_items.size();
    }

    @Override
    public Object getItem(int position) {
        return purchased_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView=layoutInflater.inflate(R.layout.purchaselistlayout,null);
            TextView textview= convertView.findViewById(R.id.txt_pr_name);
            TextView textview2= convertView.findViewById(R.id.txt_cost);
            textview.setText(purchased_items.get(position).getName());
            textview2.setText("Ksh."+purchased_items.get(position).getPrice());


        return convertView;
    }
}
