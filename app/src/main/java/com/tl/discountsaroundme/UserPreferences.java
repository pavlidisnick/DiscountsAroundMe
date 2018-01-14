package com.tl.discountsaroundme;

import android.content.SharedPreferences;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 13/1/2018.
 */

public class UserPreferences {

    /**
     * Get  the user shared preferences
     */
    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;

    public UserPreferences() {
        userPref = getApplicationContext().getSharedPreferences("UserPreferences", 0);
        editor = userPref.edit();
    }

    public static void saveDataInt(String Keyname, int intValue) {
        editor.putInt(Keyname, intValue);
        editor.commit();
    }

    public static void saveDataBool(String Keyname, boolean booleanValue) {
        editor.putBoolean(Keyname, booleanValue);
        editor.commit();
    }

    public static int getDataInt(String Keyname) {
        return userPref.getInt(Keyname, 1);
    }

    public static boolean getDataBool(String Keyname) {
        return userPref.getBoolean(Keyname, false);
    }

    public static void saveDataString(String keyname, String stringValue) {
        editor.putString(keyname, stringValue);
        editor.commit();
    }

    public static String getDataString(String Keyname) {
        return userPref.getString(Keyname, "Null");
    }
}
