package com.example.user.alumni.event;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.alumni.R;
import com.example.user.alumni.activity.AppPrefManager;
import com.example.user.alumni.activity.MainActivity;
import com.example.user.alumni.fragment.Create_Evnt_Frag;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Event_MainActivity extends AppCompatActivity {

    private Button btn;

    private final String[] PERMISSION_CALENDAR =
            new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
    List<String> permissionsNeeded = new ArrayList<>();
    boolean isFirstTime = true;
    private static final int REQUEST_MULTIPLE_PERMISSION = 123;
    private static final String TAG = "CalendarTest";
    private TextView calendarNameTxtView;
    private Hashtable hashTable;
    private String calendarName,email,admin;
    private CustomCalendarView calendarView;
    private Button opencal;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__main);

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        opencal = (Button) findViewById(R.id.openCalendarBtn);
        email = AppPrefManager.getPrefEmail(Event_MainActivity.this);
        admin = "nandu934@gmail.com";
        Log.v("email1",email);
        Log.v("email2",admin);


        //Initialize CustomCalendarView from layout
        //calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(Event_MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Event_MainActivity.this, Create_Event.class);
                i.putExtra("DATE",df.format(date));
                startActivity(i);
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Toast.makeText(Event_MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


//        if(email.equals(admin)){
//            calendarView.setVisibility(View.VISIBLE);
//        } else {
//            opencal.setVisibility(View.VISIBLE);
//        }


        btn = (Button) findViewById(R.id.frag);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Create_Evnt_Frag ev = new Create_Evnt_Frag();
//                fragmentTransaction.add(R.id.your_placeholder,ev,"hello");
//                fragmentTransaction.commit();

                // Begin the transaction
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.add(R.id.your_placeholder, new Create_Evnt_Frag());
                // or ft.replace(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                ft.commit();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarNameTxtView = (TextView) findViewById(R.id.calendarNameTxtView);
        if (Build.VERSION.SDK_INT >= 23) {
            if (verifyPermission()) return;
        }
        startAddEventService();
    }

    public void openCalendar(View view) {
        long startMillis = Calendar.getInstance().getTimeInMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(intent);
    }

    public void makeNewCalendarEntry(String title,
                                     String description, String location,
                                     long startTime, long endTime,
                                     boolean allDay, boolean hasAlarm,
                                     int calendarId, int selectedReminderValue) {

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        if (allDay) {
            values.put(CalendarContract.Events.ALL_DAY, true);
        }

        if (hasAlarm) {
            values.put(CalendarContract.Events.HAS_ALARM, true);
        }
        Uri uri = null;
        //Get current timezone
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(TAG, "Timezone retrieved=>" + TimeZone.getDefault().getID());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the c2ase where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            Log.i(TAG, "Uri returned=>"+uri.toString());
            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());

            if (hasAlarm)
            {
                ContentValues reminders = new ContentValues();
                reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, selectedReminderValue);
                Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
            }
        }
    }

    @TargetApi(23)
    private boolean verifyPermission() {
        if (checkAllPermission()) {
            if (permissionsNeeded.size() > 0 && !isFirstTime) {
                for (String permission : permissionsNeeded) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        displayPermissionDialog("Would like to grant access" +
                                        " to TestCalendar to access your calendar",
                                PERMISSION_CALENDAR, REQUEST_MULTIPLE_PERMISSION);
                        break;
                    }
                }
            } else {
                isFirstTime = false;
                requestPermissions(PERMISSION_CALENDAR, REQUEST_MULTIPLE_PERMISSION);
            }
            return true;
        }
        return false;
    }

    @TargetApi(23)
    private boolean checkAllPermission() {
        boolean isPermissionRequired = false;
        for (String permission : PERMISSION_CALENDAR) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
                isPermissionRequired = true;
            }
        }
        return isPermissionRequired;
    }

    @TargetApi(23)
    private void displayPermissionDialog(String msg, final String[] permission, final int resultCode) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        requestPermissions(permission, resultCode);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION:
                boolean isStartActivity = true;
                int position = 0;
                for (int permission : grantResults) {
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        //DO NOTHING
                        Log.i(TAG, "Permission is granted");
                    } else {
                        isStartActivity = false;
                        position = permission;
                    }
                }
                if (isStartActivity) {
                    startAddEventService();
                } else {
                    String msg = String.format(Locale.ENGLISH, "%s permission is missing ", position == 1 ?
                            "Calendar" : "Read and write");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startAddEventService() {
        hashTable = listCalendarId(this);
        if (hashTable != null) {
            Enumeration e = hashTable.keys();
            if (e.hasMoreElements()) {
                calendarName = (String) e.nextElement();
                calendarNameTxtView.setText(calendarName);
                final long oneHour = 1000 * 60 * 60;
                final long tenMinutes = 1000 * 60 * 10;

                long oneHourFromNow = (new Date()).getTime() + oneHour;
                long tenMinutesFromNow = (new Date()).getTime() + tenMinutes;


                String calendarString = calendarName;

                int calendar_id = Integer.parseInt((String) hashTable.get(calendarString));
                Toast.makeText(this,""+calendar_id,Toast.LENGTH_SHORT).show();
                makeNewCalendarEntry("Test", "Add event", "Somewhere",tenMinutesFromNow,tenMinutesFromNow+oneHour,false,true,calendar_id,3);
            }
        }
        /*Intent intent = new Intent(MainActivity_cal.this,AddEventService.class);
        startService(intent);*/
    }

    public static Hashtable listCalendarId(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        if (managedCursor.moveToFirst())
        {
            String calName;
            String calID;
            //int count = 0;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            Hashtable<String,String> calendarIdTable = new Hashtable<>();

            /*do
            {*/
            calName = managedCursor.getString(nameCol);
            calID = managedCursor.getString(idCol);
            Log.v(TAG, "CalendarName:" + calName + " ,id:" + calID);
            calendarIdTable.put(calName,calID);
            //count++;
            /*} while (managedCursor.moveToNext());*/
            managedCursor.close();

            return calendarIdTable;
        }
        return null;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
