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

public class Prof_Education_2 extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Prof_Education.class.getSimpleName();
    EditText school, degree, field, grade, activities, s_date, e_date, description;
    Button save1,save2;
    private ProgressDialog pdialog;
    private String userid;
    private Switch switch1;
    TextView tv;
    TextInputLayout ti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof__education);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        school = (EditText) findViewById(R.id.school);
        degree = (EditText) findViewById(R.id.degree);
        field = (EditText) findViewById(R.id.field);
        grade = (EditText) findViewById(R.id.grade);
        activities = (EditText) findViewById(R.id.activities);
        s_date = (EditText) findViewById(R.id.f_date);
        s_date.setOnClickListener(this);
        e_date = (EditText) findViewById(R.id.t_date);
        e_date.setOnClickListener(this);
        description = (EditText) findViewById(R.id.description);
        userid = Integer.toString(AppPrefManager.getUserId(Prof_Education_2.this));
        ti = (TextInputLayout) findViewById(R.id.ed);
        tv = (TextView) findViewById(R.id.tv_pres);

        // Progress dialog
        pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
        save1 = (Button) findViewById(R.id.save_btn);
        save1.setOnClickListener(this);
        save2 = (Button) findViewById(R.id.save_bt);
        save2.setOnClickListener(this);


        switch1 = (Switch) findViewById(R.id.mswitch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ti.setVisibility(ti.GONE);
                    tv.setVisibility(tv.VISIBLE);
                    save1.setVisibility(save1.GONE);
                    save2.setVisibility(save2.VISIBLE);
                } else {
                    ti.setVisibility(ti.VISIBLE);
                    tv.setVisibility(tv.GONE);
                    save1.setVisibility(save1.VISIBLE);
                    save2.setVisibility(save2.GONE);
                }
            }
        });


//        save = (Button) findViewById(R.id.save_btn);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String school_lbl = school.getText().toString().trim();
//                Log.v("EEEvalue", school_lbl);
//                String degree_lbl = degree.getText().toString().trim();
//                String field_lbl = field.getText().toString().trim();
//                String grade_lbl = grade.getText().toString().trim();
//                String activities_lbl = activities.getText().toString().trim();
//                String startdate_lbl = updateLabel1();
//                String enddate_lbl = updateLabel2();
//                String description_lbl = description.getText().toString().trim();
//
//                if (!school_lbl.isEmpty()) {
//                    eduExp(userid,school_lbl, degree_lbl, field_lbl, grade_lbl, activities_lbl, startdate_lbl, enddate_lbl, description_lbl);
//                } else {
//                    Toast.makeText(Prof_Education_2.this, "Enter your details", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void eduExp(final String userid,final String school, final String degree, final String fieldofstudy, final String grade, final String activities, final String startdate, final String enddate, final String jobdescription) {

        final String tag_string_req = "req_register";
        pdialog.setMessage("Registering ...");
        //showDialog();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_EDU, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Profile_ok.class));
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "JSONError" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("General_Exx", e.getStackTrace().toString());
                    //Toast.makeText(getApplicationContext(), "Please try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userid);
                params.put("school", school);
                params.put("degree", degree);
                params.put("fieldofstudy", fieldofstudy);
                params.put("grade", grade);
                params.put("activities", activities);
                params.put("ed_startdate", startdate);
                params.put("ed_enddate", enddate);
                params.put("ed_jobdescription", jobdescription);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
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
        s_date.setText(sdf.format(myCalendar.getTime()));
        String startdate = sdf.format(myCalendar.getTime());
        return startdate;
    }

    private String updateLabel2() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        e_date.setText(sdf1.format(myCalendar1.getTime()));
        String enddate = sdf1.format(myCalendar1.getTime());
        return enddate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_date:
                new DatePickerDialog(Prof_Education_2.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.t_date:
                new DatePickerDialog(Prof_Education_2.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.save_btn:
                String school_lbl = school.getText().toString().trim();
                Log.v("EEEvalue", school_lbl);
                String degree_lbl = degree.getText().toString().trim();
                String field_lbl = field.getText().toString().trim();
                String grade_lbl = grade.getText().toString().trim();
                String activities_lbl = activities.getText().toString().trim();
                String startdate_lbl = updateLabel1();
                String enddate_lbl = updateLabel2();
                String description_lbl = description.getText().toString().trim();

                if (!school_lbl.isEmpty()) {
                    eduExp(userid,school_lbl, degree_lbl, field_lbl, grade_lbl, activities_lbl, startdate_lbl, enddate_lbl, description_lbl);
                } else {
                    Toast.makeText(Prof_Education_2.this, "Enter your details", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.save_bt:
                String school_lb = school.getText().toString().trim();
                Log.v("EEEvalue", school_lb);
                String degree_lb = degree.getText().toString().trim();
                String field_lb = field.getText().toString().trim();
                String grade_lb = grade.getText().toString().trim();
                String activities_lb = activities.getText().toString().trim();
                String startdate_lb = updateLabel1();
                String enddate_lb = "present";
                String description_lb = description.getText().toString().trim();

                if (!school_lb.isEmpty()) {
                    eduExp(userid,school_lb, degree_lb, field_lb, grade_lb, activities_lb, startdate_lb, enddate_lb, description_lb);
                } else {
                    Toast.makeText(Prof_Education_2.this, "Enter your details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void showDialog() {
        if (!pdialog.isShowing())
            pdialog.show();
    }
    private void hideDialog(){
        if (!pdialog.isShowing())
            pdialog.dismiss();
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
}
