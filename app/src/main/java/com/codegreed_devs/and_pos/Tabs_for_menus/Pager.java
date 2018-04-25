package com.codegreed_devs.and_pos.Tabs_for_menus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by FakeJoker on 18/02/2018.
 */

public class Pager extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Tab_BreakFast tab1 = new Tab_BreakFast();
                return tab1;
            case 1:
                Tab_Lunch tab2 = new Tab_Lunch();
                return tab2;
            case 2:
                Tab_Supper tab3 = new Tab_Supper();
                return tab3;
            case 3:
                Tab_Drinks tab4 = new Tab_Drinks();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
