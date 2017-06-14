package com.example.user.alumni.fcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.activity.MainActivity;
import com.example.user.alumni.app.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    //defining views
    private Button ButtonSendPush;
    private Button ButtonRegister;
    private EditText editTextEmail;
    private ProgressDialog progressDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //getting views from xml
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        ButtonRegister = (Button) findViewById(R.id.buttonRegister);
        ButtonSendPush = (Button) findViewById(R.id.buttonSendNotification);

        //adding listener to view
        ButtonRegister.setOnClickListener(this);
        ButtonSendPush.setOnClickListener(this);
        //new sendToken().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //storing token to mysql server
    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String email = editTextEmail.getText().toString();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(Main2Activity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Main2Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    }


//    public class sendToken extends AsyncTask<Object, Object, Void> {
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            //pDialog.setMessage("Please Wait ...");
//            //showDialog();
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//        @Override
//        protected Void doInBackground(Object... params) {
//
//                sendTokenToServer();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void bmp) {
//            super.onPostExecute(bmp);
//            //hideDialog();
//            // Dismiss the progress dialog
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();
//        }
//    }

    @Override
    public void onClick(View view) {
        if (view == ButtonRegister) {
            sendTokenToServer();
        }
        //starting send notification activity
        if (view == ButtonSendPush) {
            startActivity(new Intent(this, ActivitySendPushNotification.class));
        }
    }
}