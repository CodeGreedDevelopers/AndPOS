package com.codegreed_devs.and_pos.Tabs_for_menus;

import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.codegreed_devs.and_pos.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 18/02/2018.
 */

public class Tabbed_Menu_Fire {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Tabbed_Menu_Item> tabbed_menu_items=new ArrayList<>();

    /*
    PASS DATABASE REFERENCE
     */
    public Tabbed_Menu_Fire(DatabaseReference db){
        this.db=db;
    }

    public Boolean save(Menu_Item menuItem,String category)
    {
        if(menuItem==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child(category).push().setValue(menuItem);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;

    }

    //RETRIEVE
    public ArrayList<Tabbed_Menu_Item> retrieve()
    {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tabbed_menu_items;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        tabbed_menu_items.clear();
        for (DataSnapshot ds:dataSnapshot.getChildren())
        {
            Tabbed_Menu_Item foods_units=ds.getValue(Tabbed_Menu_Item.class);
            tabbed_menu_items.add(foods_units);
        }
    }
}
