package com.example.user.alumni.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Update_vol extends AppCompatActivity implements View.OnClickListener {

    private EditText org,role,cause,sdate,edate,jobdesc;
    private Button save,del,save1;
    private ProgressDialog pDialog;
    TextInputLayout ti;
    TextView tv;
    Switch switch1;
    private  String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vol);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ti = (TextInputLayout) findViewById(R.id.vol_ti);
        tv = (TextView) findViewById(R.id.vol_tv);
        org = (EditText) findViewById(R.id.vol_o);
        role = (EditText) findViewById(R.id.vol_r);
        cause = (EditText) findViewById(R.id.vol_c);
        sdate = (EditText) findViewById(R.id.vol_sd);
        sdate.setOnClickListener(this);
        edate = (EditText) findViewById(R.id.vol_ed);
        edate.setOnClickListener(this);
        jobdesc = (EditText) findViewById(R.id.vol_des);

        id = getIntent().getStringExtra("id");
        final String organisaton = getIntent().getStringExtra("org");
        final String rol = getIntent().getStringExtra("role");
        final String caus = getIntent().getStringExtra("cause");
        final String sd = getIntent().getStringExtra("sdate");
        final String ed = getIntent().getStringExtra("edate");
        final String job = getIntent().getStringExtra("jobdesc");

        Log.v("idfromadapterrrrr",id);
        org.setText(organisaton);
        role.setText(rol);
        cause.setText(caus);
        sdate.setText(sd);
        edate.setText(ed);
        jobdesc.setText(job);

        save = (Button) findViewById(R.id.vol_save);
        save.setOnClickListener(this);
        save1 = (Button) findViewById(R.id.vol_s);
        save1.setOnClickListener(this);
        del = (Button) findViewById(R.id.vol_delete);
        del.setOnClickListener(this);
        switch1 = (Switch) findViewById(R.id.vol_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ti.setVisibility(ti.INVISIBLE);
                    tv.setVisibility(tv.VISIBLE);
                    save1.setVisibility(save1.VISIBLE);
                    save.setVisibility(save.GONE);
                } else {
                    ti.setVisibility(ti.VISIBLE);
                    tv.setVisibility(tv.INVISIBLE);
                    save.setVisibility(save.VISIBLE);
                    save1.setVisibility(save1.GONE);
                }
            }
        });
    }

    private void edu_update(final String Id, final String organisation, final String role, final String cause,final String sdate, final String edate, final String description) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_VOL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_vol.this, "successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_vol.this,Profile_ok.class);
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
                params.put("organisation", organisation);
                params.put("role",role);
                params.put("cause", cause);
                params.put("vol_startdate", sdate);
                params.put("vol_enddate", edate);
                params.put("description", description);
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

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_VOL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_vol.this, "Removed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_vol.this,Profile_ok.class);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vol_sd:
                new DatePickerDialog(Update_vol.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.vol_ed:
                new DatePickerDialog(Update_vol.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.vol_save:
                //String id_lbl = company.getText().toString();
                String org_lbl = org.getText().toString();
                String role_lbl = role.getText().toString();
                String cause_lbl = cause.getText().toString();
                String sd_lbl = sdate.getText().toString();
                String ed_lbl = edate.getText().toString();
                String job_lbl = jobdesc.getText().toString();

                if (!id.isEmpty() && !org_lbl.isEmpty()) {

                    edu_update(id,org_lbl,role_lbl,cause_lbl,sd_lbl,ed_lbl,job_lbl);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.vol_s:
                //String id_lbl = company.getText().toString();
                String org_lb = org.getText().toString();
                String role_lb = role.getText().toString();
                String cause_lb = cause.getText().toString();
                String sd_lb = sdate.getText().toString();
                String ed_lb = "present";
                String job_lb = jobdesc.getText().toString();

                if (!id.isEmpty() && !org_lb.isEmpty()) {

                    edu_update(id,org_lb,role_lb,cause_lb,sd_lb,ed_lb,job_lb);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.vol_delete:
                edu_delete(id);
                break;
        }
    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };

    Calendar myCalendar1 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(Calendar.YEAR, year);
            myCalendar1.set(Calendar.MONTH, monthOfYear);
            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }
    };
    public String updateLabel1() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdate.setText(sdf.format(myCalendar.getTime()));
        String startdate = sdf.format(myCalendar.getTime());
        return startdate;
    }

    private String updateLabel2() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        edate.setText(sdf1.format(myCalendar1.getTime()));
        String enddate = sdf1.format(myCalendar1.getTime());
        return enddate;
    }
}
