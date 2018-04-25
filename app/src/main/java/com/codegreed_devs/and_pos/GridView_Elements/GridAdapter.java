package com.codegreed_devs.and_pos.GridView_Elements;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by FakeJoker on 10/08/2017.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
    View view;
    LayoutInflater layoutInflater;
    ArrayList<Menu_Item> menuItems;

    public GridAdapter(Context context, ArrayList<Menu_Item> menuItems) {
        this.context = context;
        this.menuItems =menuItems;
    }

    @Override
    public int getCount() {

        return menuItems.size();
    }

    @Override
    public Object getItem(int i) {

        return menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view==null){
            view=new View(context);
            view=layoutInflater.inflate(R.layout.sigle_item,null);
            TextView textview= view.findViewById(R.id.textview);
            TextView textview2= view.findViewById(R.id.textview2);
            textview.setText(menuItems.get(i).getName());
            textview2.setText("Ksh."+menuItems.get(i).getPrice());
            ImageView food_image= view.findViewById(R.id.food_image);
//            Toast.makeText(context, "image name: "+image_name, Toast.LENGTH_SHORT).show();
            food_image.setImageURI(Uri.parse(menuItems.get(i).getImage_name()));
//            Picasso.with(context).load("https://drop.ndtv.com/albums/COOKS/pasta-vegetarian/pastaveg_640x480.jpg").into(food_image);
//            ContextWrapper cw = new ContextWrapper(view.getContext());
//            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//            File myImageFile = new File(directory, "my_busaa special.jpeg");
//            Picasso.with(view.getContext()).load(image_name).into(food_image);


        }
        return view;
    }
}
