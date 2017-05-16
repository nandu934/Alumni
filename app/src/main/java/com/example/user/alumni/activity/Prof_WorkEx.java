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
import com.example.user.alumni.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Prof_WorkEx extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Prof_WorkEx.class.getSimpleName();
    EditText DateEdit,Enddate,company,title,location,jobdescription;
    TextView tv;
    TextInputLayout ti;
    SQLiteHandler dbHandler;
    Button save_wo,save_wor;
    private ProgressDialog pDialog;
    private String userId;
    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof__work_ex);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ti = (TextInputLayout) findViewById(R.id.ed_txt);
        tv = (TextView) findViewById(R.id.tv_present);
        DateEdit = (EditText) findViewById(R.id.sdate);
        DateEdit.setOnClickListener(this);
        Enddate= (EditText) findViewById(R.id.edate);
        Enddate.setOnClickListener(this);
        company= (EditText) findViewById(R.id.companyname);
        title= (EditText) findViewById(R.id.jobtitle);
        location= (EditText) findViewById(R.id.location);
        jobdescription= (EditText) findViewById(R.id.jobdescription);
        userId = Integer.toString(AppPrefManager.getUserId(Prof_WorkEx.this));

        switch1 = (Switch) findViewById(R.id.myswitch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    ti.setVisibility(ti.VISIBLE);
                    tv.setVisibility(tv.INVISIBLE);
                    save_wo.setEnabled(true);
                    //ti.setHintAnimationEnabled(true);
                    //Enddate.clearFocus();
                    //ti.setHintAnimationEnabled(false);
                    //Enddate.setText("");
                } else {
                    ti.setVisibility(ti.INVISIBLE);
                    tv.setVisibility(tv.VISIBLE);
                    save_wor.setEnabled(true);
                    save_wo.setEnabled(false);
                    //Enddate.setVisibility(Enddate.INVISIBLE);

                    //ti.setHintAnimationEnabled(false);
                    //Enddate.clearFocus();
                    //ti.setHintAnimationEnabled(true);
                    //Enddate.setText("Present");
                }
            }
        });

        save_wo= (Button) findViewById(R.id.save_workexp);
        save_wo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String company_lbl=company.getText().toString();
                String title_lbl=title.getText().toString();
                String location_lbl=location.getText().toString();
                String jobdescription_lbl=jobdescription.getText().toString();
                String var_startdate = updateLabel1();
                String var_enddate = updateLabel2();

                if (!company_lbl.isEmpty() && !title_lbl.isEmpty() && !location_lbl.isEmpty()) {

                    workExp(userId,company_lbl, title_lbl, location_lbl,var_startdate,var_enddate,jobdescription_lbl);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params
     * */
    private void workExp(final String userId, final String company, final String title, final String location, final String sdate, final String edate, final String jobdescription) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_WORKEXP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner", response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

//                        JSONObject user = jObj.getJSONObject("user");
//                        String intro = user.getString("introduction");
//                        Log.d("userintroduction",intro);
//
//                        AppPrefManager.setUserIntroduction(Prof_WorkEx.this, intro);
//                        //editText.setText(AppPrefManager.getUserIntroduction(Prof_Introduction.this));

                        Toast.makeText(Prof_WorkEx.this, "successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Prof_WorkEx.this,ProfileActivity.class);
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
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",userId);
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


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            updateLabel1();
        }
    };
    public String updateLabel1() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DateEdit.setText(sdf.format(myCalendar.getTime()));
        String startdate = sdf.format(myCalendar.getTime());
        return startdate;
//        dbHandler = new DBHandler(Prof_WorkEx.this);
//        WorkExpdata workdata=new WorkExpdata();
//        dbHandler.open();
//        workdata.setStartdate(startdate);
//        dbHandler.addWorkExp(workdata);
//        dbHandler.close();
    }

    private String updateLabel2() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        Enddate.setText(sdf1.format(myCalendar1.getTime()));
        String enddate = sdf1.format(myCalendar1.getTime());
        return enddate;
//        dbHandler=new DBHandler(Prof_WorkEx.this);
//        WorkExpdata workdata=new WorkExpdata();
//        dbHandler.open();
//        workdata.setEnddate(enddate);
//        dbHandler.addWorkExp(workdata);
//        dbHandler.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sdate:
                new DatePickerDialog(Prof_WorkEx.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.edate:
                new DatePickerDialog(Prof_WorkEx.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;
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

