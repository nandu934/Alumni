package com.example.user.alumni.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
import com.example.user.alumni.helper.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.user.alumni.R.id.ivEditPic;
import static com.example.user.alumni.activity.ProfileActivity.decodeBase64;

public class MyProfile extends AppCompatActivity {

    CardView cvUpdate;
    Button btUpdate;
    EditText etUserName,etAge,etEmail,etGender,etMobile;
    ImageView ivProfileDp;
    private CircleImageView ciProfile;
    ImageView ivPic;
    ProgressBar pb_image;
    private ProgressDialog pDialog;
    boolean clicked = false;
    private String memberId, memberMobile, memberGender, memberUserName, memberEmail, memberAboutMember, memberDp,memberAge;
    private Uri mCropImageUri;

    private Bitmap bitmap = null;
    private String pathFile = "";
    private String log;
    private String userId;

    public MyProfile() {
        // Required empty public constructor
    }

    public static MyProfile newInstance() {
        return new MyProfile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //etUserName.setText(AppPrefManager.getPrefName(MyProfile.this));
        //memberAge = Integer.toString(AppPrefManager.getUserId(MyProfile.this));


        ciProfile = (CircleImageView) findViewById(R.id.ciProfileDp);
        userId = Integer.toString(AppPrefManager.getUserId(MyProfile.this));
        Log.v("useriddddddddddd",userId);
        // convert string to bitmap
        //Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(MyProfile.this));
        //ciProfileDp.setImageBitmap(myBitmapAgain);

        ivPic = (ImageView) findViewById(R.id.ivEditPic);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkID(memberId);
            }
        });

        setTitle("My Profile");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        initViews();

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //memberId = Integer.toString(AppPreferencesManager.getUserId(MyProfileActivity.this));
                memberUserName = etUserName.getText().toString();
                //memberDp = AppPreferencesManager.getPrefProfileDp(MyProfileActivity.this);
                memberAge = etAge.getText().toString();
                memberEmail = etEmail.getText().toString();
                memberGender = etGender.getText().toString();
                memberMobile = etMobile.getText().toString();
//                userId = Integer.toString(AppPrefManager.getUserId(MyProfile.this));
//                Log.v("useriddddddddddd",userId);

                updateProf(userId,memberUserName,memberAge,memberGender,memberMobile);
            }
        });

        //new Getdemo().execute();

//        String url = "http://www.netibiz.com/alumni/imageupload/127.jpg";
//        Picasso.with(this).load(url).resize(80, 80).into(ciProfile);

        // convert string to bitmap
        //Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(MyProfile.this));
        //ciProfile.setImageBitmap(myBitmapAgain);


        //String url = "http://www.netibiz.com/alumni/imageupload/"+userId+".jpg";
        //getBitmapFromURL(url);

        new MyAsyn().execute();
    }

//*********************22-03*********************

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            ciProfile.setImageBitmap(myBitmap);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class MyAsyn extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pDialog.setMessage("Please Wait ...");
            //showDialog();
            pDialog = new ProgressDialog(MyProfile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL("http://www.netibiz.com/alumni/imageupload/"+userId+".jpg");
                Log.v("okkkkkkkk",userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            //hideDialog();
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Bitmap bm = bmp;
            ciProfile.setImageBitmap(bm);
        }
    }


//**********************************************************************



    private void updateProf(final String userId, final String name, final String age, final String gender, final String mobile) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Please Wait....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_PROFILE, new Response.Listener<String>() {

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
                        String gender = user.getString("gender");
                        String mobile = user.getString("mobile");

                        AppPrefManager.setPrefName(MyProfile.this, name);
                        AppPrefManager.setPrefAge(MyProfile.this, age);
                        AppPrefManager.setPrefGender(MyProfile.this, gender);
                        AppPrefManager.setPrefMobile(MyProfile.this, mobile);

                        etUserName.setText(AppPrefManager.getPrefName(MyProfile.this));
                        etAge.setText(AppPrefManager.getPrefAge(MyProfile.this));
                        etEmail.setText(AppPrefManager.getPrefEmail(MyProfile.this));
                        etGender.setText(AppPrefManager.getPrefGender(MyProfile.this));
                        etMobile.setText(AppPrefManager.getPrefMobile(MyProfile.this));
                        // Launch main activity
                        //Intent intent = new Intent(MyProfile.this,MainActivity.class);
                        //startActivity(intent);
                        //finish();
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
                params.put("uid", userId);
                params.put("name", name);
                params.put("age", age);
                params.put("gender", gender);
                params.put("mobile", mobile);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

//        Intent intent = getIntent();
//        String uname = intent.getStringExtra("name1");
//        String uage = intent.getStringExtra("age1");
//        String uemail = intent.getStringExtra("email1");
//        String ugender = intent.getStringExtra("gender1");
//        String umobile = intent.getStringExtra("mobile1");


        etUserName = (EditText) findViewById(R.id.etUserName);
        setNonEditable(etUserName);
        etAge = (EditText) findViewById(R.id.etAge);
        setNonEditable(etAge);
        etEmail = (EditText) findViewById(R.id.etEmail);
        setNonEditable(etEmail);
        etGender = (EditText) findViewById(R.id.etGender);
        setNonEditable(etGender);
        etMobile = (EditText) findViewById(R.id.etMobile);
        setNonEditable(etMobile);

        etUserName.setText(AppPrefManager.getPrefName(MyProfile.this));
        etAge.setText(AppPrefManager.getPrefAge(MyProfile.this));
        etEmail.setText(AppPrefManager.getPrefEmail(MyProfile.this));
        etGender.setText(AppPrefManager.getPrefGender(MyProfile.this));
        etMobile.setText(AppPrefManager.getPrefMobile(MyProfile.this));


        // cvUpdate = (CardView) findViewById(R.id.cvUpdate);
        btUpdate = (Button) findViewById(R.id.btUpdate);

        ImageView ivEdit = (ImageView) findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {

                    setEditable(etUserName);
                    etUserName.requestFocus();
                    etUserName.setSelection(etUserName.getText().length());
                    setEditable(etAge);
                    setNonEditable(etEmail);
                    setEditable(etGender);
                    setEditable(etMobile);
//                  btUpdate.setVisibility(View.VISIBLE);
                    btUpdate.setVisibility(View.VISIBLE);
                    clicked = true;
                } else {
                    setNonEditable(etUserName);
                    setNonEditable(etAge);
                    setNonEditable(etEmail);
                    setNonEditable(etGender);
                    setNonEditable(etMobile);
//                  btUpdate.setVisibility(View.GONE);
                    btUpdate.setVisibility(View.GONE);
                    clicked = false;
                }
            }
        });
    }

    private void setNonEditable(EditText editText) {
        editText.setClickable(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setFocusableInTouchMode(false);
    }

    private void setEditable(EditText editText) {
        editText.setClickable(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
    }

//    private class Getdemo extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(MyProfile.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(AppConfig.URL_DEMO);
//
//            //Log.e(TAG, "Response from url: " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                        JSONObject jObj = new JSONObject(jsonStr);
//                        JSONObject demo = jObj.getJSONObject("user");
//
//                        log = demo.getString("logid");
//                        Log.d("id",log);
////                        User userList = new User();
////                        userList.setLogid(name);
//
//                    } catch (JSONException e) {
//                    e.printStackTrace();
//                    }
//            } else {
//                //Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            /**
//             * Updating parsed JSON data into ListView
//             * */
////            User userL=new User();
////            etUserName.setText(userL.getLogid());
////            Log.d("id2",userL.getLogid());
//            etUserName.setText(log);
//        }
//
//    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


//    private void checkID(final String userid) {
//        // Tag used to cancel the request
//        String tag_string_req = "req_login";
//
//        pDialog.setMessage("Please Wait ...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DEMO, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//
//                    // Check for error node in json
//                    if (!error) {
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        Log.d("namefromProfile",name);
//                        String age = user.getString("age");
//                        String email = user.getString("email");
//                        String gender = user.getString("gender");
//                        String mobile = user.getString("mobile");
//
//                        etUserName.setText(name);
//                        etAge.setText(age);
//                        etEmail.setText(email);
//                        etGender.setText(gender);
//                        etMobile.setText(mobile);
//
//                        //String age = user.getString("age");
//                        //String email = user.getString("email");
//                        //static int uid = user.getString("uid");
//                        //String gender = user.getString("gender");
//                        //String mobile = user.getString("mobile");
//                        //String created_at = user.getString("created_at");
//
//                        // Inserting row in users table
//                        //db.addUser(name, age, email, gender, mobile, uid, created_at);
//
//                        // Launch main activity
//                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        //startActivity(intent);
//                        //finish();
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Log.v("json_error", e.getMessage());
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("uid", userid);
//                return params;
//            }
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }


}