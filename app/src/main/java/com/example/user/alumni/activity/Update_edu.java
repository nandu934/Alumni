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

public class Update_edu extends AppCompatActivity implements View.OnClickListener{

    private EditText school,degree,fos,grade,activities,sdate,edate,jobdesc;
    private Button save,del,save2;
    private ProgressDialog pDialog;
    TextInputLayout ti;
    TextView tv;
    private String id,sch;
    private Switch s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_edu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ti = (TextInputLayout) findViewById(R.id.tid);
        tv = (TextView) findViewById(R.id.tv_p);
        school = (EditText) findViewById(R.id.s);
        degree = (EditText) findViewById(R.id.d);
        fos = (EditText) findViewById(R.id.f);
        grade = (EditText) findViewById(R.id.g);
        activities = (EditText) findViewById(R.id.a);
        sdate = (EditText) findViewById(R.id.f_d);
        sdate.setOnClickListener(this);
        edate = (EditText) findViewById(R.id.t_d);
        edate.setOnClickListener(this);
        jobdesc = (EditText) findViewById(R.id.de);

        save = (Button) findViewById(R.id.update_btn);
        save.setOnClickListener(this);
        save2 = (Button) findViewById(R.id.save_up);
        save2.setOnClickListener(this);

        id = getIntent().getStringExtra("id");
        sch = getIntent().getStringExtra("school");
        final String deg = getIntent().getStringExtra("degree");
        final String fo = getIntent().getStringExtra("fos");
        final String grad = getIntent().getStringExtra("grade");
        final String act = getIntent().getStringExtra("activity");
        final String sd = getIntent().getStringExtra("sdate");
        final String ed = getIntent().getStringExtra("edate");
        final String job = getIntent().getStringExtra("jobdesc");

        Log.v("idfromadapterrrrr",id);
        school.setText(sch);
        degree.setText(deg);
        fos.setText(fo);
        grade.setText(grad);
        activities.setText(act);
        sdate.setText(sd);
        edate.setText(ed);
        jobdesc.setText(job);

        s = (Switch) findViewById(R.id.myswi);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        save2 = (Button) findViewById(R.id.save_up);
        save2.setOnClickListener(this);
        save = (Button) findViewById(R.id.update_btn);
        save.setOnClickListener(this);
        del = (Button) findViewById(R.id.delete_btn);
        del.setOnClickListener(this);
    }

    private void edu_update(final String Id, final String school, final String degree, final String fos,final String grade,final String activities,final String sdate, final String edate, final String jobdescription) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_EDU_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_edu.this, "successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_edu.this,Profile_ok.class);
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
                params.put("school", school);
                params.put("degree",degree);
                params.put("fieldofstudy", fos);
                params.put("grade", grade);
                params.put("activities",activities);
                params.put("ed_startdate", sdate);
                params.put("ed_enddate", edate);
                params.put("ed_jobdescription", jobdescription);
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

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_EDU_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(Update_edu.this, "Removed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Update_edu.this,Profile_ok.class);
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
            case R.id.f_d:
                new DatePickerDialog(Update_edu.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.t_d:
                new DatePickerDialog(Update_edu.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.update_btn:
                //String id_lbl = company.getText().toString();
                String school_lbl = school.getText().toString();
                String degree_lbl = degree.getText().toString();
                String fos_lbl = fos.getText().toString();
                String grade_lbl = grade.getText().toString();
                String act_lbl = activities.getText().toString();
                String sd_lbl = sdate.getText().toString();
                String ed_lbl = edate.getText().toString();
                String job_lbl = jobdesc.getText().toString();

                if (!id.isEmpty() && !sch.isEmpty()) {

                    edu_update(id,school_lbl,degree_lbl,fos_lbl,grade_lbl,act_lbl,sd_lbl,ed_lbl,job_lbl);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.save_up:
                //String id_lbl = company.getText().toString();
                String school_lb = school.getText().toString();
                String degree_lb = degree.getText().toString();
                String fos_lb = fos.getText().toString();
                String grade_lb = grade.getText().toString();
                String act_lb = activities.getText().toString();
                String sd_lb = sdate.getText().toString();
                String ed_lb = "present";
                String job_lb = jobdesc.getText().toString();

                if (!id.isEmpty() && !sch.isEmpty()) {

                    edu_update(id,school_lb,degree_lb,fos_lb,grade_lb,act_lb,sd_lb,ed_lb,job_lb);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.delete_btn:
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
