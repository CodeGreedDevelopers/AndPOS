package com.codegreed_devs.and_pos.Tabs_for_menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codegreed_devs.and_pos.R;
import com.codegreed_devs.and_pos.UpdateFoodMenu;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by FakeJoker on 18/02/2018.
 */

public class Tab_Lunch extends Fragment {
    ListView lunch_list;
    DatabaseReference db;
    Tabbed_Menu_Fire tabbed_menu_fire;
    Tab_List_Adapter tab_list_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tab_lunch_layout, container, false);
        lunch_list=(ListView)rootView.findViewById(R.id.lunch_list);
        db= UpdateFoodMenu.db.child("Menu_Item_2");
        tabbed_menu_fire=new Tabbed_Menu_Fire(db);

        tab_list_adapter=new Tab_List_Adapter(getContext(),tabbed_menu_fire.retrieve(),"Menu_Item_2/Lunch");
        lunch_list.invalidateViews();
        lunch_list.setAdapter(tab_list_adapter);


        return rootView;
    }
}