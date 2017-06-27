package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Prof_Introduction_2 extends AppCompatActivity {

        private static final String TAG = Prof_Introduction.class.getSimpleName();
        EditText editText;
        Button button;
        ProgressDialog pDialog;
        private String userId;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prof__introduction);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Progress dialog
            userId = Integer.toString(AppPrefManager.getUserId(com.example.user.alumni.activity.Prof_Introduction_2.this));
            Log.v("uidfromintro",userId);
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            editText = (EditText) findViewById(R.id.about);
            editText.setText(AppPrefManager.getUserIntroduction(com.example.user.alumni.activity.Prof_Introduction_2.this));
            button = (Button) findViewById(R.id.save_about);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String introduction = editText.getText().toString().trim();
                    insert(userId,introduction);
                }
            });
        }

        private void insert(final String userId, final String introduction) {
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            pDialog.setMessage("Registering ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_INTRO, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        Log.v("inner", response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {

                            JSONObject user = jObj.getJSONObject("user");
                            String intro = user.getString("introduction");
                            //Log.d("userintroduction",intro);

                            AppPrefManager.setUserIntroduction(com.example.user.alumni.activity.Prof_Introduction_2.this, intro);
                            //editText.setText(AppPrefManager.getUserIntroduction(Prof_Introduction.this));
                            Toast.makeText(com.example.user.alumni.activity.Prof_Introduction_2.this, "successfully added", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(com.example.user.alumni.activity.Prof_Introduction_2.this,Profile_ok.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "JSONError" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.v("General_Exx", e.getStackTrace().toString());
                        Toast.makeText(getApplicationContext(), "Please try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("uid",userId);
                    params.put("introduction", introduction);
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }


        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case android.R.id.home:
                    Intent intent = new Intent(this, Profile_ok.class);
                    startActivity(intent);
                    this.finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }
        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }