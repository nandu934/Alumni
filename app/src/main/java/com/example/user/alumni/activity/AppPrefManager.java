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

    //**************user_workexperience***************

    private static final String WORKEXP_COMPANY = "workexp_company";
    private static final String WORKEXP_TITLE = "workexp_title";
    private static final String WORKEXP_LOCATION = "workexp_location";
    private static final String WORKEXP_STARTDATE = "workexp_startdate";
    private static final String WORKEXP_ENDDATE = "workexp_enddate";
    private static final String WORKEXP_JOBDESCRIPTION = "workexp_jobdescription";


    private static final String PREF_ID = "pref_id";

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
//****************************USER_WORKEXPERIENCE****************************************

    public static String getWorkexpCompany(Context context){
        return getSharedPreferences(context).getString(WORKEXP_COMPANY,"");
    }

    public static void setWorkexpCompany(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_COMPANY, newValue);
        editor.apply();
    }

    public static String getWorkexpTitle(Context context){
        return getSharedPreferences(context).getString(WORKEXP_TITLE,"");
    }

    public static void setWorkexpTitle(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_TITLE, newValue);
        editor.apply();
    }

    public static String getWorkexpLocation(Context context){
        return getSharedPreferences(context).getString(WORKEXP_LOCATION,"");
    }

    public static void setWorkexpLocation(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_LOCATION, newValue);
        editor.apply();
    }

    public static String getWorkexpStartdate(Context context){
        return getSharedPreferences(context).getString(WORKEXP_STARTDATE,"");
    }

    public static void setWorkexpStartdate(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_STARTDATE, newValue);
        editor.apply();
    }

    public static String getWorkexpEnddate(Context context){
        return getSharedPreferences(context).getString(WORKEXP_ENDDATE,"");
    }

    public static void setWorkexpEnddate(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_ENDDATE, newValue);
        editor.apply();
    }

    public static String getWorkexpJobdescription(Context context){
        return getSharedPreferences(context).getString(WORKEXP_JOBDESCRIPTION,"");
    }

    public static void setWorkexpJobdescription(Context context, String newValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(WORKEXP_JOBDESCRIPTION, newValue);
        editor.apply();
    }




    public static int getAID(Context context) {
        return getSharedPreferences(context).getInt(PREF_ID, 0);
    }

    public static void setAID(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_ID, newValue);
        editor.apply();
    }




}
