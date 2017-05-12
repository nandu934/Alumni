package com.example.user.alumni.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.alumni.R;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.helper.SQLiteHandler;
import com.example.user.alumni.helper.SessionManager;
import com.example.user.alumni.helper.User;

import java.util.HashMap;

public class Main_settings extends AppCompatActivity {

    private Button btnLogout;
    private SessionManager session;
    private SQLiteHandler db;
    private EditText ed_name,ed_age,ed_email,ed_gender,ed_mob;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ed_name= (EditText) findViewById(R.id.edname);
        ed_age= (EditText) findViewById(R.id.edage);
        ed_email= (EditText) findViewById(R.id.edemail);
        ed_gender= (EditText) findViewById(R.id.edgender);
        ed_mob= (EditText) findViewById(R.id.edmobilenum);

        btnLogout = (Button) findViewById(R.id.logou);
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUse();
            }
        });

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String age = user.get("age");
        String email = user.get("email");
        String gender = user.get("gender");
        String mob = user.get("mobile");

        // Displaying the user details on the screen

        Toast.makeText(this, "Welcome " +name, Toast.LENGTH_SHORT).show();
//        User user1 = new User();
//        ed_name.setText(user1.getName());
//        ed_age.setText(user1.getAge());
//        ed_email.setText(user1.getEmail());
//        ed_gender.setText(user1.getGender());
//        ed_mob.setText(user1.getMobile());

        ed_name.setText("Welcome : "+pref.getString(AppConfig.NAME,""));
        ed_email.setText(pref.getString(AppConfig.EMAIL,""));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUse() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
