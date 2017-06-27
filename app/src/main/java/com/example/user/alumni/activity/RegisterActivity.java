package com.example.user.alumni.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.alumni.R;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;
import com.example.user.alumni.helper.SQLiteHandler;
import com.example.user.alumni.helper.SessionManager;
import com.example.user.alumni.helper.User;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName,inputAge,inputEmail,inputGender,inputMobile,inputPassword,inputconfirm_pwd;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    SharedPreferences pref;
    CountryCodePicker ccp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputAge =  (EditText) findViewById(R.id.age);
        inputEmail = (EditText) findViewById(R.id.email);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        ccp = (CountryCodePicker) findViewById(R.id.id_ccp);
        inputMobile = (EditText) findViewById(R.id.mobilenum);
        inputPassword = (EditText) findViewById(R.id.password);
        inputconfirm_pwd = (EditText) findViewById(R.id.cpassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Register Button Click event
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String name = inputFullName.getText().toString();
                if (!isValidname(name)) {
                    inputFullName.setError("Invalid name");
                    return;
                }
                String age = inputAge.getText().toString();
                if (!isValidage(age)) {
                    inputAge.setError("Invalid");
                    return;
                }
                String email = inputEmail.getText().toString();
                if (!isValidemail(email)) {
                    inputEmail.setError("Invalid mail ID");
                    return;
                }
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                String gender = radioButton.getText().toString();
                if(gender.equals("")){
                    Toast.makeText(RegisterActivity.this, "gender is missing", Toast.LENGTH_LONG).show();
                    return;
                }
                String countrycode = ccp.getSelectedCountryCodeWithPlus().toString();
                String mob = inputMobile.getText().toString();
                String mobile = countrycode+mob;

                if(!isValidPhone(mobile)||mobile.equals("")){
                    Toast.makeText(getApplicationContext(), "invalid mobile number", Toast.LENGTH_LONG).show();
                    return;
                }

                String password = inputPassword.getText().toString();
                String edConf = inputconfirm_pwd.getText().toString();

                if(password.equals("")||edConf.equals("")) {
                    Toast.makeText(getApplicationContext(), "enter your password", Toast.LENGTH_LONG).show();
                    return;
                }

                if(edConf.equals(password) ){
                    registerUser(name, age, email, gender, mobile, password);
                }else{
                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                    inputPassword.setText("");
                    inputconfirm_pwd.setText("");
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String name, final String age, final String email, final String gender, final String mobile, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.v("inner",response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString(AppConfig.NAME);
                        String age = user.getString(AppConfig.AGE);
                        String email = user.getString(AppConfig.EMAIL);
                        String gender = user.getString(AppConfig.GENDER);
                        String mobile = user.getString(AppConfig.MOBILE);
                        String created_at = user.getString("created_at");
                        String set = user.getString("updated_at");

                        User userr = new User();
                        userr.setName(name);
                        userr.setAge(age);
                        userr.setEmail(email);
                        userr.setGender(gender);
                        userr.setMobile(mobile);

                        // Inserting row in users table
                        db.addUser(name, age, email, gender, mobile, created_at);
                        Toast.makeText(getApplicationContext(), "successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"JSONError" + e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.v("General_Exx",e.getStackTrace().toString());
                    Toast.makeText(getApplicationContext(),"Please try again" + e.getMessage(),Toast.LENGTH_LONG).show();

//                    Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();

                }


//                }catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                    Log.v("General_Exx",e.getStackTrace().toString());
//                    Toast.makeText(getApplicationContext(),"Please try again" + e.getMessage(),Toast.LENGTH_LONG).show();
//                }

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
                params.put(AppConfig.NAME, name);
                params.put(AppConfig.AGE,age);
                params.put(AppConfig.EMAIL, email);
                params.put(AppConfig.GENDER, gender);
                params.put(AppConfig.MOBILE, mobile);
                params.put("password", password);


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

    private boolean isValidname(String edname) {
//        String ALPHANUM_PATTERN = "^[a-zA-Z0-9]{5}";
//        String ALPHANUM_PATTERN = "[a-zA-Z]{2}\\d{3}";
        String ALPHANUM_PATTERN="[a-zA-Z][a-zA-Z ]*";
        Pattern pattern = Pattern.compile(ALPHANUM_PATTERN);
        Matcher matcher = pattern.matcher(edname);
        return matcher.matches();
    }
    private boolean isValidage(String edage) {
        String NUM_PATTERN="\\d{1,3}";
        Pattern pattern = Pattern.compile(NUM_PATTERN);
        Matcher matcher = pattern.matcher(edage);
        return matcher.matches();
    }
    private boolean isValidemail(String edemail) {
        String EMAIL_PATTERN="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(edemail);
        return matcher.matches();
    }
    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }
}