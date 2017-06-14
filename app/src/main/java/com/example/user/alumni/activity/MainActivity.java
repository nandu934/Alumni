package com.example.user.alumni.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.Manifest;
import com.example.user.alumni.R;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
import com.example.user.alumni.calendar.MainActivity_cal;
import com.example.user.alumni.event.Event_MainActivity;
import com.example.user.alumni.fcm.Main2Activity;
import com.example.user.alumni.fcm.MyVolley;
import com.example.user.alumni.fcm.SharedPrefManager;
import com.example.user.alumni.helper.SQLiteHandler;
import com.example.user.alumni.helper.SessionManager;
import com.example.user.alumni.helper.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    ImageButton profil,groupcontact,events,settings;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private String memberId,email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        memberId = Integer.toString(AppPrefManager.getUserId(MainActivity.this));

        Log.d("useridfrommainactivity",memberId);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        profil= (ImageButton) findViewById(R.id.profile);
        profil.setOnClickListener(this);
        groupcontact= (ImageButton) findViewById(R.id.groupcontact);
        groupcontact.setOnClickListener(this);
        events= (ImageButton) findViewById(R.id.events);
        events.setOnClickListener(this);
        settings= (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(this);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
//        Log.v("name is",name);

        // Displaying the user details on the screen

        //Toast.makeText(this, "Welcome  " +name, Toast.LENGTH_SHORT).show();
        //txtName.setText(name);
        //txtEmail.setText(email);

        // Logout button click event
        email = AppPrefManager.getPrefEmail(MainActivity.this);
        //new sendToken().execute();


        if(AppPrefManager.getUserName(MainActivity.this).length() == 0)
        {
            // call Login Activity
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        else
        {
            // Stay at the current activity.
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }


    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                Intent prof1 = new Intent(this,Profile_ok.class);
                startActivity(prof1);
                finish();
                break;

            case R.id.groupcontact:
                Intent prof=new Intent(this,Main2Activity.class);
                startActivity(prof);
                finish();
//                Intent group=new Intent(this,MyProfile.class);
//                startActivity(group);
//                finish();
                break;

            case R.id.events:
                //Intent event=new Intent(this,Event_MainActivity.class);
                Intent event=new Intent(this,MainActivity_cal.class);
                startActivity(event);
                finish();
                break;

            case R.id.settings:
//                Intent settings=new Intent(this,AB_main.class);
//                startActivity(settings);
//                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                //return true;
            break;
            case R.id.myprof:
                // app icon in action bar clicked; go home
//                Intent intentMy = new Intent(this, MyProfile.class);
//                startActivity(intentMy);
//                this.finish();

                checkID(memberId);
                //return true;
                break;
            case R.id.logout:
                AppPrefManager.clearUserName(this);
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userlanding, menu);
        return true;
    }

    private void checkID(final String userid) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DEMO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        Log.d("namefromProfile",name);
                        String age = user.getString("age");
                        String email = user.getString("email");
                        String gender = user.getString("gender");
                        String mobile = user.getString("mobile");

                        AppPrefManager.setPrefName(MainActivity.this, name);
                        AppPrefManager.setPrefAge(MainActivity.this, age);
                        AppPrefManager.setPrefEmail(MainActivity.this, email);
                        AppPrefManager.setPrefGender(MainActivity.this, gender);
                        AppPrefManager.setPrefMobile(MainActivity.this, mobile);


                        // Launch main activity
                        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
//                        intent.putExtra("name1",name);
//                        intent.putExtra("age1",age);
//                        intent.putExtra("email1",email);
//                        intent.putExtra("gender1",gender);
//                        intent.putExtra("mobile1",mobile);

                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.v("json_error", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        //final String emaill = inputEmail.getText().toString().trim();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER_DEVICE,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
        //mRequestQueue.add(stringRequest);
    }
    public class sendToken extends AsyncTask<Object, Object, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog.setMessage("Please Wait ...");
            showDialog();
        }
        @Override
        protected Void doInBackground(Object... params) {

            sendTokenToServer();
            return null;
        }

        @Override
        protected void onPostExecute(Void bmp) {
            super.onPostExecute(bmp);
            //hideDialog();
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}