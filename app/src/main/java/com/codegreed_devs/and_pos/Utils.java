package com.codegreed_devs.and_pos;

/**
 * Created by Augustine on 2/24/2018.
 */

import android.app.Activity;
import android.content.Intent;

public class Utils {

    private static int sTheme;

    public final static int THEME_DEFAULT = 0;

    public final static int THEME_GREEN = 1;

    public final static int THEME_PURPLE = 2;



    /**

     * Set the theme of the Activity, and restart it by creating a new Activity

     * of the same type.

     */

    public static void changeToTheme(Activity activity, int theme) {

        sTheme = theme;

        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));

    }



    /** Set the theme of the activity, according to the configuration. */

    public static void onActivityCreateSetTheme(Activity activity) {

        switch (sTheme) {

            default:

            case THEME_DEFAULT:

                break;

            case THEME_GREEN:

                activity.setTheme(R.style.ThemeGreen);

                break;

            case THEME_PURPLE:

                activity.setTheme(R.style.ThemePurple);

                break;

        }

    }
}
