package com.example.user.alumni.service;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //we will save the token in sharedpreferences later

    }
}













//package com.example.user.alumni.service;
//
///**
// * Created by User on 08-05-2017.
// */
//
//
//import android.util.Log;
//import com.example.user.alumni.app.SharedPrefManager;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//    private static final String TAG = "MyFirebaseIIDService";
//    public static final String TOKEN_BROADCAST = "myfcmtockenbroadcast";
//
//    @Override
//    public void onTokenRefresh() {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed_token: " + refreshedToken);
//        Log.d(TAG,"tokennnnnnnnnnnnnnn");
//        //getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
//        storeToken(refreshedToken);
//    }
//
//    private void storeToken(String token) {
//        //saving the token on shared preferences
//        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
//    }
//}