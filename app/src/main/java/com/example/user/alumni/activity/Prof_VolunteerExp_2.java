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

public class Prof_VolunteerExp_2 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Prof_VolunteerExp.class.getSimpleName();
    EditText organisation,role,cause,startdate,enddate,description;
    Button save,save2;
    ProgressDialog pdialog;
    private String userid;
    TextInputLayout ti;
    TextView tv;
    private Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof__volunteer_exp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pdialog=new ProgressDialog(this);
        pdialog.setCancelable(false);

        ti = (TextInputLayout) findViewById(R.id.ti_v);
        tv = (TextView) findViewById(R.id.tv_v);
        organisation= (EditText) findViewById(R.id.vol_organisation);
        role= (EditText) findViewById(R.id.vol_role);
        cause= (EditText) findViewById(R.id.vol_cause);
        startdate= (EditText) findViewById(R.id.vol_sdate);
        startdate.setOnClickListener(this);
        enddate= (EditText) findViewById(R.id.vol_edate);
        enddate.setOnClickListener(this);
        description= (EditText) findViewById(R.id.vol_desc);
        userid = Integer.toString(AppPrefManager.getUserId(Prof_VolunteerExp_2.this));
        save= (Button) findViewById(R.id.save_vol);
        save.setOnClickListener(this);
        save2 = (Button) findViewById(R.id.save_v);
        save2.setOnClickListener(this);
        switch1 = (Switch) findViewById(R.id.switch0);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ti.setVisibility(ti.GONE);
                    tv.setVisibility(tv.VISIBLE);
                    save.setVisibility(save.GONE);
                    save2.setVisibility(save2.VISIBLE);
                } else {
                    ti.setVisibility(ti.VISIBLE);
                    tv.setVisibility(tv.GONE);
                    save.setVisibility(save.VISIBLE);
                    save2.setVisibility(save2.GONE);
                }
            }
        });
    }

    private void volExp(final String userId,final String organisation_lbl, final String role_lbl, final String cause_lbl, final String startdate_lbl, final String enddate_lbl, final String description_lbl) {

        final String tag_string_req = "req_register";
        pdialog.setMessage("Registering ...");
        //showDialog();

        StringRequest request= new StringRequest(Request.Method.POST, AppConfig.URL_VOL, new Response.Listener<String>() {

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
                    } else {

                        // Error occurred in registration. Get the error
                        // message
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
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                params.put("organisation", organisation_lbl);
                params.put("role", role_lbl);
                params.put("cause", cause_lbl);
                params.put("vol_startdate",startdate_lbl);
                params.put("vol_enddate", enddate_lbl);
                params.put("description", description_lbl);
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
        startdate.setText(sdf.format(myCalendar.getTime()));
        String startdate = sdf.format(myCalendar.getTime());
        return startdate;
    }

    private String updateLabel2() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        enddate.setText(sdf1.format(myCalendar1.getTime()));
        String enddate = sdf1.format(myCalendar1.getTime());
        return enddate;
    }

    private void showDialog() {
        if (!pdialog.isShowing()){
            pdialog.show();
        }
    }
    private void hideDialog(){
        if (!pdialog.isShowing()){
            pdialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vol_sdate:
                new DatePickerDialog(Prof_VolunteerExp_2.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.vol_edate:
                new DatePickerDialog(Prof_VolunteerExp_2.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.save_vol:
                String organisation_lbl = organisation.getText().toString();
                String role_lbl = role.getText().toString();
                String cause_lbl = cause.getText().toString();
                String startdate_lbl = updateLabel1();
                String enddate_lbl = updateLabel2();
                String description_lbl = description.getText().toString();
                if(!organisation_lbl.isEmpty()) {
                    volExp(userid,organisation_lbl, role_lbl, cause_lbl, startdate_lbl, enddate_lbl, description_lbl);
                }else{
                    Toast.makeText(Prof_VolunteerExp_2.this, "Enter your details", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.save_v:
                String organisation_lb = organisation.getText().toString();
                String role_lb = role.getText().toString();
                String cause_lb = cause.getText().toString();
                String startdate_lb = updateLabel1();
                String enddate_lb= "present";
                String description_lb = description.getText().toString();
                if(!organisation_lb.isEmpty()) {
                    volExp(userid,organisation_lb, role_lb, cause_lb, startdate_lb, enddate_lb, description_lb);
                }else{
                    Toast.makeText(Prof_VolunteerExp_2.this, "Enter your details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
