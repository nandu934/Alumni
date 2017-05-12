package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.adapter.Edu_adapter;
import com.example.user.alumni.adapter.Workexp_adapter;
import com.example.user.alumni.adapter.Workexp_adapter_edit;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
import com.example.user.alumni.models.EducationData;
import com.example.user.alumni.models.WorkexpData;
import com.example.user.alumni.models.WorkexpData_edit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exp_Edu extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog pDialog;
    private RecyclerView work_rec,edu_rec;
    private Workexp_adapter_edit wwAdapter;
    private Edu_adapter eAdapter;
    private ImageButton exp,edu;
    private List<WorkexpData> data=new ArrayList<>();
    private WorkexpData_edit workexpData = new WorkexpData_edit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp__edu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        exp = (ImageButton) findViewById(R.id.exp);
        exp.setOnClickListener(this);
        edu = (ImageButton) findViewById(R.id.edu);
        edu.setOnClickListener(this);
        String userId =Integer.toString(AppPrefManager.getUserId(Exp_Edu.this));
        Workexp_view(userId);
        Edu_view(userId);
    }


    private void Workexp_view(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WORKEXP_VIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                List<WorkexpData> data=new ArrayList<>();

                try {
                    JSONArray jArray = new JSONArray(response);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        WorkexpData Data = new WorkexpData();
                        Data.company= json_data.getString("company");
                        Data.title= json_data.getString("title");
                        Data.location= json_data.getString("location");
                        Data.startdate= json_data.getString("startdate");
                        Data.enddate= json_data.getString("enddate");
                        Data.description= json_data.getString("jobdescription");
                        data.add(Data);
                    }

                    // Setup and Handover data to recyclerview
//                    work = (RecyclerView)findViewById(R.id.recycler_workexp_edit);
//                    wAdapter = new Workexp_adapter(Exp_Edu.this, data);
//                    work.setAdapter(wAdapter);
//                    work.setLayoutManager(new LinearLayoutManager(Exp_Edu.this));


                    work_rec = (RecyclerView)findViewById(R.id.workexp_recy);
                    wwAdapter = new Workexp_adapter_edit(Exp_Edu.this, data);
                    work_rec.setAdapter(wwAdapter);
                    work_rec.setLayoutManager(new LinearLayoutManager(Exp_Edu.this));


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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",userId);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void Edu_view(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_EDU_VIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                List<EducationData> dataArrayList=new ArrayList<>();

                try {
                    JSONArray jArray = new JSONArray(response);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        EducationData eduData = new EducationData();
                        eduData.school= json_data.getString("school");
                        eduData.degree= json_data.getString("degree");
                        eduData.fieldofstudy= json_data.getString("fieldofstudy");
                        eduData.grade= json_data.getString("grade");
                        eduData.activities= json_data.getString("activities");
                        eduData.ed_startdate= json_data.getString("ed_startdate");
                        eduData.ed_enddate= json_data.getString("ed_enddate");
                        eduData.ed_description= json_data.getString("ed_jobdescription");
                        dataArrayList.add(eduData);
                    }

                    // Setup and Handover data to recyclerview
                    edu_rec = (RecyclerView)findViewById(R.id.edu_recy);
                    eAdapter = new Edu_adapter(Exp_Edu.this, dataArrayList);
                    edu_rec.setAdapter(eAdapter);
                    edu_rec.setLayoutManager(new LinearLayoutManager(Exp_Edu.this));

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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",userId);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
        switch (v.getId())
        {
            case R.id.exp:
                Intent prof1 = new Intent(this,Prof_WorkEx.class);
                //Intent prof1 = new Intent(this,Workexp_edit.class);
                startActivity(prof1);
                finish();
                break;


//                int position = 0;
//                String itemLabel = "" + workexpData.nextInt(100);
//
//                // Add an item to animals list
//                data.add(position,"" + itemLabel);
//
//                // Notify the adapter that an item inserted
//                mAdapter.notifyItemInserted(position);
//
//                /*
//                   public void scrollToPosition (int position)
//                        Convenience method to scroll to a certain position. RecyclerView does not
//                        implement scrolling logic, rather forwards the call to scrollToPosition(int)
//
//                    Parameters
//                    position : Scroll to this adapter position
//
//                */
//                // Scroll to newly added item position
//                work_rec.scrollToPosition(position);
//
//                // Show the added item label
//                Toast.makeText(mContext,"Added : " + itemLabel,Toast.LENGTH_SHORT).show();
//        }
//                break;
            case R.id.edu:
                Intent prof2 = new Intent(this,Prof_Education.class);
                startActivity(prof2);
                finish();
                break;
        }
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
}
