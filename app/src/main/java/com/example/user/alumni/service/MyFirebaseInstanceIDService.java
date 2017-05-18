package com.example.user.alumni.service;

/**
 * Created by User on 08-05-2017.
 */


import android.util.Log;
import com.example.user.alumni.app.SharedPrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static final String TOKEN_BROADCAST = "myfcmtockenbroadcast";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed_token: " + refreshedToken);
        Log.d(TAG,"tokennnnnnnnnnnnnnn");
        //getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}