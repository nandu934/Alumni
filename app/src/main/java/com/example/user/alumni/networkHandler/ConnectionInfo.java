package com.example.user.alumni.networkHandler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by User on 08-03-2017.
 */

public class ConnectionInfo {

    private Context _context;

    public ConnectionInfo(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {

        Log.d("is Connecting", "");

        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
}
