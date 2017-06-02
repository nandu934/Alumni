package com.example.user.alumni.event;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.user.alumni.R;
import com.example.user.alumni.fcm.Main2Activity;

import java.util.Calendar;

public class Create_Event extends AppCompatActivity {

    private EditText edittext,ed_time;
    private TextView tv1;
    private BroadcastReceiver broadcastReceiver;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ed_time = (EditText) findViewById(R.id.time_view_edit);
        edittext = (EditText) findViewById(R.id.event_date);
        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("DATE");
        edittext.setText(s);

        ed_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Create_Event.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //tv1 = (TextView) findViewById(R.id.tv);
        //tv1.setText(SharedPrefManager.getInstance(Create_Event.this).getDeviceToken());
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                tv1.setText(SharedPrefManager.getInstance(Create_Event.this).getDeviceToken());
//            }
//        };
//        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIDService.TOKEN_BROADCAST));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this,Event_MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
