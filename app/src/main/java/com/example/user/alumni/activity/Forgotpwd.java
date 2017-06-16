package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Forgotpwd extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_email,ed_code,ed_password;
    private Button btn;
    private TextInputLayout email_ti;
    private TextView tv;
    ProgressDialog pDialog;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        email_ti = (TextInputLayout) findViewById(R.id.email_ti);
        tv = (TextView) findViewById(R.id.fp_timer);
        ed_email = (EditText) findViewById(R.id.fp_email);
        ed_code = (EditText) findViewById(R.id.fp_code);
        ed_password = (EditText) findViewById(R.id.fp_password);
        tv.setVisibility(View.GONE);
        ed_code.setVisibility(View.GONE);
        ed_password.setVisibility(View.GONE);
        btn = (Button) findViewById(R.id.fp_btn);
        btn.setOnClickListener(this);
    }
    private void initViews(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fp_btn:
                if (!isResetInitiated) {

                    email = ed_email.getText().toString();
                    if (!email.isEmpty()) {
                        pDialog.setMessage("Please Wait ...");
                        showDialog();
                        initiateResetPasswordProcess(email);

                    } else {
                        Toast.makeText(Forgotpwd.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String code = ed_code.getText().toString();
                    String password = ed_password.getText().toString();

                if(!code.isEmpty() && !password.isEmpty()){

                    finishResetPasswordProcess(email,code,password);
                } else {
                    Toast.makeText(Forgotpwd.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void initiateResetPasswordProcess(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SEND_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner",response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        email_ti.setVisibility(View.GONE);
                        ed_email.setVisibility(View.GONE);
                        ed_code.setVisibility(View.VISIBLE);
                        ed_password.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        btn.setText("Change Password");
                        isResetInitiated = true;
                        startCountdownTimer();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        //Log.d("error",errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"JSONError" + e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.v("General_Exx",e.getStackTrace().toString());
                    Toast.makeText(getApplicationContext(),"Please try again" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.EMAIL, email);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void finishResetPasswordProcess(final String email, final String code, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_CHANGE_PWD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner",response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        JSONObject user = jObj.getJSONObject("user");

                        Log.d("email_from_fogpwd",email);
                        Log.d("email_from_fogpwd",code);
                        Log.d("email_from_fogpwd",password);

                        countDownTimer.cancel();
                        isResetInitiated = false;

                        // Launch main activity
                        Intent intent = new Intent(Forgotpwd.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"JSONError" + e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.v("General_Exx",e.getStackTrace().toString());
                    Toast.makeText(getApplicationContext(),"Please try again" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("code", code);
                params.put("password", password);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv.setText("Time remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Time Out ! Request again to reset password.",Toast.LENGTH_LONG).show();
                Intent i = new Intent(Forgotpwd.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        }.start();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
