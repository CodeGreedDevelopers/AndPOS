package com.codegreed_devs.and_pos;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by FakeJoker on 15/02/2018.
 */

public class CartListAdapter extends ArrayAdapter<Products> {
    private  Context context;
    private  List<Products> products;
    public CartListAdapter(Context context, List<Products> products) {
        super(context,R.layout.cart_products, products);
        this.context=context;
        this.products=products;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.cart_products,parent,false);
        TextView textView=(TextView)view.findViewById(R.id.product);
        TextView txt_quantity=(TextView) view.findViewById(R.id.txt_quantity);
        ImageView trash_image=(ImageView)view.findViewById(R.id.trash);
        ImageView sub_track=(ImageView)view.findViewById(R.id.sub_track);
        ImageView add_btn=(ImageView)view.findViewById(R.id.add_btn);
        txt_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_Quantity(position);
            }
        });
        sub_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double pperunit=products.get(position).getP_price();
                int p_quantity=products.get(position).getQuantity();

                products.get(position).setQuantity(p_quantity-1);
                MainActivity.total_cash=MainActivity.total_cash-pperunit;

                if (products.get(position).getQuantity()==0){
                    products.remove(position);

                }
                CartListAdapter.this.notifyDataSetChanged();
                MainActivity.total_cash_txt.setText("Ksh."+MainActivity.total_cash);
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double pperunit=products.get(position).getP_price();
                int p_quantity=products.get(position).getQuantity();

                products.get(position).setQuantity(p_quantity+1);
                MainActivity.total_cash=MainActivity.total_cash+pperunit;

                CartListAdapter.this.notifyDataSetChanged();
                MainActivity.total_cash_txt.setText("Ksh."+MainActivity.total_cash);
            }
        });
        trash_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.update_bill(products.get(position).getP_price(),products.get(position).getQuantity());
                products.remove(position);
                //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                CartListAdapter.this.notifyDataSetChanged();
            }
        });
        TextView quantity=(TextView)view.findViewById(R.id.txt_quantity);
        textView.setText(String.valueOf(products.get(position).getP_name()));
        quantity.setText(String.valueOf(products.get(position).getQuantity()));
        return view;

    }
    public void set_Quantity(final int pos){
        AlertDialog.Builder seta=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2=layoutInflater.inflate(R.layout.set_quantity_layout,null);
        seta.setMessage("Please Enter The Amount");
        seta.setIcon(R.mipmap.ic_launcher);
        final EditText quantity=(EditText)view2.findViewById(R.id.quantity_setter);
        quantity.setBackgroundResource(R.drawable.image_holder);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantity.setMaxWidth(10);
        seta.setTitle("And POS ");
        seta.setView(quantity);
        seta.setCancelable(true);
        seta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int available_quantity=products.get(pos).getQuantity();
                Double price_per_unit=products.get(pos).getP_price();
                if (quantity.getText().toString().isEmpty()){

                }else{
                    int input_qauntity=Integer.parseInt(quantity.getText().toString());
                    products.get(pos).setQuantity(input_qauntity);
                    MainActivity.total_cash=MainActivity.total_cash+((input_qauntity*price_per_unit)-(available_quantity*price_per_unit));
                    CartListAdapter.this.notifyDataSetChanged();
                    MainActivity.total_cash_txt.setText("Ksh."+MainActivity.total_cash);
                }



            }
        });
        seta.setView(view2).create().show();
    }
}
