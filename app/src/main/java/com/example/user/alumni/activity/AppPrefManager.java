package com.example.user.alumni.activity;

/**
 * Created by User on 14-03-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class AppPrefManager {

    public static final String APP_SETTINGS = "ida_prefs";

    // preference for user
    private static final String PREF_USER_ID = "pref_id_users";
    private static final String PREF_NAME = "pref_name" ;
    private static final String PREF_AGE = "pref_age" ;
    private static final String PREF_EMAIL = "pref_email" ;
    private static final String PREF_GENDER = "pref_gender" ;
    private static final String PREF_MOBILE = "pref_mobile" ;
    private static final String PREF_PROFILE_DP = "pref_profiledp";

    private static final String USER_INTRODUCTION = "user_intro";

    private static final String PREF_ID = "pref_id";

    private static final String PREF_USER_NAME= "username";

    private AppPrefManager() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt(PREF_USER_ID, 0);
    }

    public static void setUserId(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_USER_ID, newValue);
        editor.apply();
    }

    public static String getPrefName(Context context) {
        return getSharedPreferences(context).getString(PREF_NAME, "");
    }

    public static void setPrefName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_NAME, newValue);
        editor.apply();
    }

    //AppPrefManager.setPrefName(MainActivity.this, name);
    //etUserName.setText(AppPrefManager.getPrefName(MyProfile.this));

    //https://www.simplifiedcoding.net/android-login-example-using-php-mysql-and-volley/
    //https://www.simplifiedcoding.net/android-volley-tutorial-to-get-json-from-server/

    public static String getPrefAge(Context context){
        return getSharedPreferences(context).getString(PREF_AGE,"");
    }

    public static void setPrefAge(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_AGE, newValue);
        editor.apply();
    }

    public static String getPrefEmail(Context context){
        return getSharedPreferences(context).getString(PREF_EMAIL,"");
    }

    public static void setPrefEmail(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_EMAIL, newValue);
        editor.apply();
    }

    public static String getPrefGender(Context context){
        return getSharedPreferences(context).getString(PREF_GENDER,"");
    }

    public static void setPrefGender(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_GENDER, newValue);
        editor.apply();
    }

    public static String getPrefMobile(Context context){
        return getSharedPreferences(context).getString(PREF_MOBILE,"");
    }

    public static void setPrefMobile(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_MOBILE, newValue);
        editor.apply();
    }

    public static String getPrefProfileDp(Context context) {
        return getSharedPreferences(context).getString(PREF_PROFILE_DP, "");
    }

    public static void setPrefProfileDp(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PROFILE_DP, newValue);
        editor.apply();
    }


    //******************************************************************************

    public static String getUserIntroduction(Context context){
        return getSharedPreferences(context).getString(USER_INTRODUCTION,"");
    }

    public static void setUserIntroduction(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_INTRODUCTION, newValue);
        editor.apply();
    }

    //*****************for auto login*************************************************

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor edit = getSharedPreferences(ctx).edit();
        edit.putString(PREF_USER_NAME, userName);
        edit.commit();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor edit = getSharedPreferences(ctx).edit();
        edit.clear(); //clear all stored data
        edit.commit();
    }
}
