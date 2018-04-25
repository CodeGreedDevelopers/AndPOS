package com.codegreed_devs.and_pos.Tabs_for_menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codegreed_devs.and_pos.R;
import com.codegreed_devs.and_pos.UpdateFoodMenu;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 18/02/2018.
 */

public class Tab_BreakFast extends Fragment {
    ListView breakfast;
    DatabaseReference db;
    Tabbed_Menu_Fire tabbed_menu_fire;
    Tab_List_Adapter tab_list_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.tab_breakfast_layout, container, false);

            breakfast=(ListView)rootView.findViewById(R.id.breakfast_items);
            db= UpdateFoodMenu.db.child("Menu_Item_1");
            tabbed_menu_fire=new Tabbed_Menu_Fire(db);

            tab_list_adapter=new Tab_List_Adapter(getContext(),tabbed_menu_fire.retrieve(),"Menu_Item_1/BreakFast");
            breakfast.invalidateViews();
            breakfast.setAdapter(tab_list_adapter);



            return rootView;

        }
}