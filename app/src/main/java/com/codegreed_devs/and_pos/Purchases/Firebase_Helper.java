package com.codegreed_devs.and_pos.Purchases;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 20/02/2018.
 */

public class Firebase_Helper {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Purchased_Items> purchased_items=new ArrayList<>();

    /*
    PASS DATABASE REFERENCE
     */
    public Firebase_Helper(DatabaseReference db){
        this.db=db;
    }

    public Boolean save(Purchased_Items purchasedItems,String category)
    {
        if(purchasedItems==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child(category).push().setValue(purchasedItems);
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
    public ArrayList<Purchased_Items> retrieve()
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

        return purchased_items;
    }

    public void fetchData(DataSnapshot dataSnapshot)
    {
        purchased_items.clear();

        for (DataSnapshot ds:dataSnapshot.getChildren())
        {
            Purchased_Items cost_units=ds.getValue(Purchased_Items.class);
            purchased_items.add(cost_units);
        }
        Purchases_Activity.purchased_list.invalidateViews();

    }


}