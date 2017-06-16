package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.adapter.Edu_adapter;
import com.example.user.alumni.adapter.Skill_adapter;
import com.example.user.alumni.adapter.VolExp_adapter;
import com.example.user.alumni.models.EducationData;
import com.example.user.alumni.models.SkillData;
import com.example.user.alumni.models.VolExpData;
import com.example.user.alumni.models.WorkexpData;
import com.example.user.alumni.adapter.Workexp_adapter;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
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

public class Profile_ok extends AppCompatActivity implements View.OnClickListener{

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView work_reclerview,edu_recyclerview,vol_recyclerview,s_recyclerview;
    private Workexp_adapter mAdapter;
    private Edu_adapter eAdapter;
    private VolExp_adapter vAdapter;
    private Skill_adapter sAdapter;
    private ProgressDialog pDialog;
    private TextView intro, skillname,skilllevel;
    private ImageButton ib,ib1,ib2,ib3,ib4;
    private String userId,intro_val,skillname_val,skilllev_val;
    private CircleImageView img;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter work_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ok);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        ib = (ImageButton) findViewById(R.id.prof_intro);
        ib.setOnClickListener(this);
        ib2 = (ImageButton) findViewById(R.id.profexp);
        ib2.setOnClickListener(this);
        ib1 = (ImageButton) findViewById(R.id.profedu);
        ib1.setOnClickListener(this);
        ib3 = (ImageButton) findViewById(R.id.profvol);
        ib3.setOnClickListener(this);
        ib4 = (ImageButton) findViewById(R.id.profskill);
        ib4.setOnClickListener(this);

        intro = (TextView) findViewById(R.id.intr);
        skillname = (TextView) findViewById(R.id.skilln);
        skilllevel = (TextView) findViewById(R.id.skilllev);
        img = (CircleImageView) findViewById(R.id.cirle_im);

        userId =Integer.toString(AppPrefManager.getUserId(Profile_ok.this));

        new ProfImg().execute();
        Workexp_view(userId);
        Edu_view(userId);
        Vol_view(userId);
        Intro_view(userId);
        Skill_view(userId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(Profile_ok.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

//        recyclerView = (RecyclerView) findViewById(R.id.workexp_recycler);
//        recyclerView.setNestedScrollingEnabled(false);
//        //recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void Intro_view(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_INTRO_VIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = jsonObject.getJSONObject("user");
                    intro_val = user.getString("introduction");
                    intro.setText(intro_val);

//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("user");
//                    JSONObject obj =jsonArray.getJSONObject(0);
//                    intro_val = obj.getString("introduction");
//                    intro.setText(intro_val);

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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                requestShowKeyboardShortcuts();
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

                   /*     Data.company = json_data.getString("company");
                        Data.title = json_data.getString("title");
                        Data.location = json_data.getString("location");
                        Data.startdate = json_data.getString("startdate");
                        Data.enddate = json_data.getString("enddate");
                        Data.description = json_data.getString("jobdescription");
                        Data.id = json_data.getString("id");
                        data.add(Data);   */

                        Data.setCompany(json_data.getString("company"));
                        Data.setTitle(json_data.getString("title"));
                        Data.setLocation(json_data.getString("location"));
                        Data.setStartdate(json_data.getString("startdate"));
                        Data.setEnddate(json_data.getString("enddate"));
                        Data.setDescription(json_data.getString("jobdescription"));
                        Data.setId(json_data.getString("id"));
                        data.add(Data);
                    }

                    //AppPrefManager.setAID(Profile_ok.this,);

                    // Setup and Handover data to recyclerview
                    work_reclerview = (RecyclerView)findViewById(R.id.workexp_recycler);
                    mAdapter = new Workexp_adapter(Profile_ok.this, data);
                    work_reclerview.setAdapter(mAdapter);
                    work_reclerview.setLayoutManager(new LinearLayoutManager(Profile_ok.this));

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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
//                        eduData.school= json_data.getString("school");
//                        eduData.degree= json_data.getString("degree");
//                        eduData.fieldofstudy= json_data.getString("fieldofstudy");
//                        eduData.grade= json_data.getString("grade");
//                        eduData.activities= json_data.getString("activities");
//                        eduData.ed_startdate= json_data.getString("ed_startdate");
//                        eduData.ed_enddate= json_data.getString("ed_enddate");
//                        eduData.ed_description= json_data.getString("ed_jobdescription");
//                        dataArrayList.add(eduData);


                        eduData.setSchool(json_data.getString("school"));
                        eduData.setDegree(json_data.getString("degree"));
                        eduData.setFieldofstudy(json_data.getString("fieldofstudy"));
                        eduData.setGrade(json_data.getString("grade"));
                        eduData.setActivities(json_data.getString("activities"));
                        eduData.setEd_startdate(json_data.getString("ed_startdate"));
                        eduData.setEd_enddate(json_data.getString("ed_enddate"));
                        eduData.setEd_description(json_data.getString("ed_jobdescription"));
                        eduData.setId(json_data.getString("id"));
                        dataArrayList.add(eduData);
                    }

                    // Setup and Handover data to recyclerview
                    edu_recyclerview = (RecyclerView)findViewById(R.id.edu_recycler);
                    eAdapter = new Edu_adapter(Profile_ok.this, dataArrayList);
                    edu_recyclerview.setAdapter(eAdapter);
                    edu_recyclerview.setLayoutManager(new LinearLayoutManager(Profile_ok.this));

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
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void Vol_view(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_VOL_VIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                List<VolExpData> dArrayList=new ArrayList<>();

                try {
                    JSONArray jArray = new JSONArray(response);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){

                        JSONObject json_data = jArray.getJSONObject(i);
                        VolExpData volData = new VolExpData();
//                        volData.organisation= json_data.getString("organisation");
//                        volData.role= json_data.getString("role");
//                        volData.cause= json_data.getString("cause");
//                        volData.sdate= json_data.getString("startdate");
//                        volData.edate= json_data.getString("enddate");
//                        volData.description= json_data.getString("jobdescription");
//                        dArrayList.add(volData);

                        volData.setOrganisation(json_data.getString("organisation"));
                        volData.setRole(json_data.getString("role"));
                        volData.setCause(json_data.getString("cause"));
                        volData.setSdate(json_data.getString("startdate"));
                        volData.setEdate(json_data.getString("enddate"));
                        volData.setDescription(json_data.getString("jobdescription"));
                        volData.setId(json_data.getString("id"));
                        dArrayList.add(volData);
                    }

                    // Setup and Handover data to recyclerview
                    vol_recyclerview = (RecyclerView)findViewById(R.id.volexp_recycler);
                    vAdapter = new VolExp_adapter(Profile_ok.this, dArrayList);
                    vol_recyclerview.setAdapter(vAdapter);
                    vol_recyclerview.setLayoutManager(new LinearLayoutManager(Profile_ok.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(getApplicationContext(), "JSONError" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("General_Exx", e.getStackTrace().toString());
                   // Toast.makeText(getApplicationContext(), "Please try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void Skill_view(final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SKILL_VIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                List<SkillData> dArrayList=new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject json_data = jsonArray.getJSONObject(i);
                        SkillData sData = new SkillData();
                        sData.setSkillname(json_data.getString("skillname"));
                        sData.setSkilllevel(json_data.getString("skilllevel"));
                        sData.setId(json_data.getString("id"));
                        dArrayList.add(sData);

                    }
                    // Setup and Handover data to recyclerview
                    s_recyclerview = (RecyclerView)findViewById(R.id.skill_recycler);
                    sAdapter = new Skill_adapter(Profile_ok.this, dArrayList);
                    s_recyclerview.setAdapter(sAdapter);
                    s_recyclerview.setLayoutManager(new LinearLayoutManager(Profile_ok.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(getApplicationContext(), "JSONError" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("General_Exx", e.getStackTrace().toString());
                   // Toast.makeText(getApplicationContext(), "Please try again" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    public class ProfImg extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pDialog.setMessage("Please Wait ...");
            //showDialog();
            pDialog = new ProgressDialog(Profile_ok.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL("http://www.netibiz.com/alumni/imageupload/"+userId+".jpg");
                Log.v("okkkkkkkk",userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            //hideDialog();
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Bitmap bm = bmp;
            img.setImageBitmap(bm);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prof_intro:
                Intent prof1 = new Intent(this,Prof_Introduction_2.class);
                startActivity(prof1);
                finish();
                break;
            case R.id.profexp:
                Intent profe = new Intent(this,Prof_WorkEx_2.class);
                startActivity(profe);
                finish();
                break;
            case R.id.profedu:
                Intent profedu = new Intent(this,Prof_Education_2.class);
                startActivity(profedu);
                finish();
                break;
            case R.id.profvol:
                Intent profv = new Intent(this,Prof_VolunteerExp_2.class);
                startActivity(profv);
                finish();
                break;
            case R.id.profskill:
                Intent profsk = new Intent(this,Prof_Skills_2.class);
                startActivity(profsk);
                finish();
                break;
        }
    }

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
