package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.adapter.Edu_adapter;
import com.example.user.alumni.adapter.Skill_adapter;
import com.example.user.alumni.adapter.VolExp_adapter;
import com.example.user.alumni.adapter.Workexp_adapter;
import com.example.user.alumni.adapter.Workexp_adapter_edit;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
import com.example.user.alumni.models.EducationData;
import com.example.user.alumni.models.SkillData;
import com.example.user.alumni.models.VolExpData;
import com.example.user.alumni.models.WorkexpData;
import com.example.user.alumni.models.WorkexpData_edit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 12-04-2017.
 */

public class Workexp_edit extends AppCompatActivity{


    private RecyclerView work_reclerview;
    private Workexp_adapter_edit mAdapter;
    private ProgressDialog pDialog;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workexp_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        userId =Integer.toString(AppPrefManager.getUserId(Workexp_edit.this));
        Workexp_view(userId);

//        recyclerView = (RecyclerView) findViewById(R.id.workexp_recycler);
//        recyclerView.setNestedScrollingEnabled(false);
//        //recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
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
                    work_reclerview = (RecyclerView)findViewById(R.id.recycler_workexp_edit);
                    mAdapter = new Workexp_adapter_edit(Workexp_edit.this, data);
                    work_reclerview.setAdapter(mAdapter);
                    work_reclerview.setLayoutManager(new LinearLayoutManager(Workexp_edit.this));

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
}

