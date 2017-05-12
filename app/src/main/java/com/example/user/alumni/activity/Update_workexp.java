package com.example.user.alumni.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Update_workexp extends AppCompatActivity implements View.OnClickListener {

    private EditText company,title,location,sdate,edate,jobdesc;
    private Button save,del,save2;
    private ProgressDialog pDialog;
    private Switch switch1;
    TextInputLayout ti;
    TextView tv;
    private String id,comp,tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ti = (TextInputLayout) findViewById(R.id.ed_tx);
        tv = (TextView) findViewById(R.id.tv_presen);
        company = (EditText) findViewById(R.id.a);
        title = (EditText) findViewById(R.id.b);
        location = (EditText) findViewById(R.id.c);
        sdate = (EditText) findViewById(R.id.d);
        sdate.setOnClickListener(this);
        edate = (EditText) findViewById(R.id.e);
        edate.setOnClickListener(this);
        jobdesc = (EditText) findViewById(R.id.f);

        save = (Button) findViewById(R.id.save_update);
        save.setOnClickListener(this);
        save2 = (Button) findViewById(R.id.save_updat);
        save2.setOnClickListener(this);

        id = getIntent().getStringExtra("ID");
        comp = getIntent().getStringExtra("COMPANY");
        tit = getIntent().getStringExtra("TITLE");
        final String loc = getIntent().getStringExtra("LOCATION");
        final String sd = getIntent().getStringExtra("SDATE");
        final String ed = getIntent().getStringExtra("EDATE");
        final String job = getIntent().getStringExtra("JOBDESC");

        Log.v("idfromadapterrrrr",id);
        company.setText(comp);
        title.setText(tit);
        location.setText(loc);
        sdate.setText(sd);
        edate.setText(ed);
        jobdesc.setText(job);

//        save = (Button) findViewById(R.id.save_update);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //String id_lbl = company.getText().toString();
//                String comp_lbl = company.getText().toString();
//                String tit_lbl = title.getText().toString();
//                String loc_lbl = location.getText().toString();
//                String sd_lbl = sdate.getText().toString();
//                String ed_lbl = edate.getText().toString();
//                String job_lbl = jobdesc.getText().toString();
//
//                Log.v("namhjkjhje",comp_lbl);
//
//                if (!id.isEmpty() && !comp.isEmpty() && !tit.isEmpty()) {
//
//                    workExp_update(id,comp_lbl,tit_lbl,loc_lbl,sd_lbl,ed_lbl,job_lbl);
//                } else {
//                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        del = (Button) findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workExp_delete(id);
            }
        });


        switch1 = (Switch) findViewById(R.id.myswitc);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ti.setVisibility(ti.INVISIBLE);
                    tv.setVisibility(tv.VISIBLE);
                    save2.setVisibility(save2.VISIBLE);
                    save.setVisibility(save.GONE);
                } else {
                    ti.setVisibility(ti.VISIBLE);
                    tv.setVisibility(tv.INVISIBLE);
                    save.setVisibility(save.VISIBLE);
                    save2.setVisibility(save2.GONE);
                }
            }
        });
    }

    private void workExp_update(final String Id, final String company, final String title, final String location, final String sdate, final String edate, final String jobdescription) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WORKEXP_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_workexp.this, "successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_workexp.this,Profile_ok.class);
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
                params.put("company", company);
                params.put("title",title);
                params.put("location", location);
                params.put("startdate", sdate);
                params.put("enddate", edate);
                params.put("jobdescription", jobdescription);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void workExp_delete(final String Id) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WORKEXP_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_workexp.this, "Removed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_workexp.this,Profile_ok.class);
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
            case R.id.d:
                new DatePickerDialog(Update_workexp.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.e:
                new DatePickerDialog(Update_workexp.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.save_update:
                //String id_lbl = company.getText().toString();
                String comp_lbl = company.getText().toString();
                String tit_lbl = title.getText().toString();
                String loc_lbl = location.getText().toString();
                String sd_lbl = sdate.getText().toString();
                String ed_lbl = edate.getText().toString();
                String job_lbl = jobdesc.getText().toString();

                Log.v("namhjkjhje",comp_lbl);

                if (!id.isEmpty() && !comp.isEmpty() && !tit.isEmpty()) {

                    workExp_update(id,comp_lbl,tit_lbl,loc_lbl,sd_lbl,ed_lbl,job_lbl);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.save_updat:
                String comp_lb = company.getText().toString();
                String tit_lb = title.getText().toString();
                String loc_lb = location.getText().toString();
                String sd_lb = sdate.getText().toString();
                String ed_lb = "present";
                String job_lb = jobdesc.getText().toString();

                Log.v("namhjkjhje",comp_lb);

                if (!id.isEmpty() && !comp.isEmpty() && !tit.isEmpty()) {

                    workExp_update(id,comp_lb,tit_lb,loc_lb,sd_lb,ed_lb,job_lb);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
