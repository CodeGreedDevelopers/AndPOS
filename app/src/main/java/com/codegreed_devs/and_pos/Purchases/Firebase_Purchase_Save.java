package com.codegreed_devs.and_pos.Purchases;

import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 22/02/2018.
 */

public class Firebase_Purchase_Save {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Menu_Item> menuItems=new ArrayList<>();

    /*
    PASS DATABASE REFERENCE
     */
    public Firebase_Purchase_Save(DatabaseReference db){
        this.db=db;
    }

    public Boolean save(Purchased_Items menuItem, String date){
        if(menuItem==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child(date).push().setValue(menuItem);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;

    }
}
