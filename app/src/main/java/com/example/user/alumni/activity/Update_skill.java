package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update_skill extends AppCompatActivity {

    private EditText slname,sllevel;
    private Button save,del;
    private ProgressDialog pDialog;
    private Spinner spin;
    private String lev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_skill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
//******************** Values for spinner**********************************************************************************
        spin = (Spinner) findViewById(R.id.spinner2);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lev = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Click here to change your skill level");
        categories.add("Beginner");
        categories.add("Midlevel");
        categories.add("Expert");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin.setAdapter(dataAdapter);
//**************************************************************************************************************************

        slname = (EditText) findViewById(R.id.skillna);
        sllevel = (EditText) findViewById(R.id.skillle);
        final String id = getIntent().getStringExtra("id");
        final String sname = getIntent().getStringExtra("skillname");
        final String slevel = getIntent().getStringExtra("skilllevel");
        Log.v("idfromadapterrrrr",id);
        Log.v("datadfrom",sname+","+slevel);
        slname.setText(sname);
        sllevel.setText(slevel);
        setNonEditable(sllevel);
        save = (Button) findViewById(R.id.skill_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String id_lbl = company.getText().toString();
                String name_lbl = slname.getText().toString();
                //String level_lbl = sllevel.getText().toString();

                if (!id.isEmpty() && !sname.isEmpty()) {

                    edu_update(id,name_lbl,lev);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        del = (Button) findViewById(R.id.skill_delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edu_delete(id);
            }
        });
    }

    private void edu_update(final String Id, final String name, final String level) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SKILL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_skill.this, "successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_skill.this,Profile_ok.class);
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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",Id);
                params.put("skillname", name);
                params.put("skilllevel",level);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void edu_delete(final String Id) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SKILL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_skill.this, "Removed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_skill.this,Profile_ok.class);
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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",Id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
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

    private void setNonEditable(EditText editText) {
        editText.setClickable(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setFocusableInTouchMode(false);
    }
}
