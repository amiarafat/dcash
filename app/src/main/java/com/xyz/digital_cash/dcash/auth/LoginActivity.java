package com.xyz.digital_cash.dcash.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.xyz.digital_cash.dcash.DCASHMainActivity;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LOGINACTIVITY" ;
    Button btnResetPass, btnRegister;
    Button btnLogin;
    EditText etLoginMail, etLoginPass;
    View parentLayout;

    UserPref userPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeV();


        chekPreviousLogedIn();
    }

    private void chekPreviousLogedIn() {

        if(TextUtils.isEmpty(userPref.getUserEmail())){

            LogMe.d(TAG,"No info about login");
        }else {

            etLoginMail.setText(userPref.getUserEmail());
            etLoginPass.setText(userPref.getUserPasssword());
        }
    }

    private void initializeV() {

        parentLayout = findViewById(android.R.id.content);
        userPref = new UserPref(LoginActivity.this);

        btnResetPass = findViewById(R.id.btnResetPass);
        btnResetPass.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        btnLogin =findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        etLoginMail = findViewById(R.id.etLoginMail);
        etLoginPass = findViewById(R.id.etLoginPass);
    }

    @Override
    public void onClick(View v) {

        if(v == btnResetPass){
            /*Intent in = new Intent(this,PaswwordResetActivity.class);
            startActivity(in);*/

            underConstruction();
        }
        if(v == btnRegister){
            Intent in = new Intent(this,RegisterActivity.class);
            startActivity(in);
        }
        if (v == btnLogin){
           /* Intent in = new Intent(this,DCASHMainActivity.class);
            startActivity(in);*/

           startLoginProcess();
        }
    }

    private void startLoginProcess() {

        String mail = etLoginMail.getText().toString().trim();
        String pass = etLoginPass.getText().toString();

        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){

            if(TextUtils.isEmpty(mail)){

                etLoginMail.setError(" Please give your email!");
            }else if(TextUtils.isEmpty(pass)){

                etLoginPass.setError(" Please give your password!");
            }

        }else {

            if(isNetworkAvailable()) {
                showProgressDialog();
                dCashLogin(mail, pass);
            }else {

                Snackbar.make(parentLayout, "Please Check your internet connection!!", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        }

    }

    private void dCashLogin(final String mail, final String pass) {

            StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    LogMe.d("LoginRes::",response);

                    hideProgressDialog();

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String ApiSuccess = jsonObject.getString("success");

                        if (ApiSuccess == "true"){

                            JSONObject jObj = new JSONObject(jsonObject.getString("data"));
                            String accToken = jObj.getString("access_token");
                            LogMe.d("accTok::",accToken);

                            userPref.setUserAccessToken(accToken);
                            userPref.setUserEmail(mail);
                            userPref.setUserPasssword(pass);


                            Intent in =new Intent(LoginActivity.this,DCASHMainActivity.class);
                            startActivity(in);
                            finish();


                        }else {

                            Toast.makeText(getApplicationContext(), "Error Occured: "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hideProgressDialog();

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data

                            int stCode = response.statusCode;
                            View parentLayout = findViewById(android.R.id.content);

                            if(stCode == 500){

                                Snackbar.make(parentLayout, "Server Error! Please try again later", Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();
                            }else {
                                LogMe.d("er::", res);
                                JSONObject obj = new JSONObject(res);

                                String errMsg = obj.getString("message");

                                Snackbar.make(parentLayout, errMsg, Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();
                            }

                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", mail);
                    params.put("password",pass);
                    return params;
                }
            };
            DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }
}
