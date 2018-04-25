package com.codegreed_devs.and_pos.GridView_Elements;

import com.codegreed_devs.and_pos.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 16/02/2018.
 */

public class FirebaseHelper {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Menu_Item> menuItems=new ArrayList<>();

    /*
    PASS DATABASE REFERENCE
     */
    public FirebaseHelper(DatabaseReference db){
        this.db=db;
    }

    public Boolean save(Menu_Item menuItem,String category){
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
    public ArrayList<Menu_Item> retrieve()
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

        return menuItems;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        menuItems.clear();
        for (DataSnapshot ds:dataSnapshot.getChildren())
        {
            Menu_Item foods_units=ds.getValue(Menu_Item.class);
            menuItems.add(foods_units);
        }
        MainActivity.gridView.invalidateViews();
        MainActivity.gridView.setAdapter(MainActivity.gridAdapter);
    }
}
