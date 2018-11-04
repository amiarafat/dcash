package com.example.arafat.dcash.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arafat.dcash.DCASHMainActivity;
import com.example.arafat.dcash.DigitalCash;
import com.example.arafat.dcash.R;
import com.example.arafat.dcash.api_config.APIConstants;
import com.example.arafat.dcash.extras.BaseActivity;
import com.example.arafat.dcash.extras.LogMe;
import com.example.arafat.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LOGINACTIVITY" ;
    Button btnResetPass, btnRegister;
    Button btnLogin;
    EditText etLoginMail, etLoginPass;

    UserPref userPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeV();


        chekPreviousLogedIn();
    }

    private void chekPreviousLogedIn() {

        if(TextUtils.isEmpty(userPref.getUserAccessToken())){

            LogMe.d(TAG,"No info about login");
        }else {

            etLoginMail.setText(userPref.getUserEmail());
            etLoginPass.setText(userPref.getUserPasssword());
        }
    }

    private void initializeV() {

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
            Intent in = new Intent(this,PaswwordResetActivity.class);
            startActivity(in);
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

            showProgressDialog();
            dCashLogin(mail,pass);
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
