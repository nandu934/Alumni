package com.example.user.alumni.event;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by User on 17-02-2017.
 */

public class AddEventService extends Service {
    private Handler handler;
    private Runnable runnable;

    public AddEventService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //add event here
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}

