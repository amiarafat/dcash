package com.xyz.digital_cash.dcash.auth;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class RegisterActivity extends BaseActivity {


    private static final String TAG = "RegisterActivity";
    EditText etName, etEmail, etPass, etPhone, etDOB, etCity, etBikash,etRefferal;
    String  Name, Email, Pass, Phone, DOB, City, Bikash, Referral;

    Button btnRegister;
    private UserPref userPref;


    Calendar myCalender;
    DatePickerDialog.OnDateSetListener date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeValue();


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                LogMe.d("mo::", month + "--" + year + "--" + dayOfMonth);

                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateView(year, month + 1, dayOfMonth);
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogMe.d("reg::","clicked");
                prepareforRegister();

            }
        });

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(RegisterActivity.this, date, myCalender
                        .get(Calendar.YEAR), myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initializeValue() {

        userPref = new UserPref(this);
        myCalender = Calendar.getInstance();

        etName = findViewById(R.id.etRName);
        etEmail = findViewById(R.id.etREmail);
        etPass = findViewById(R.id.etRPass);
        etPhone = findViewById(R.id.etRPhone);
        etDOB = findViewById(R.id.etRDOB);
        etDOB.setFocusable(false);
        etCity = findViewById(R.id.etRCity);
        etBikash = findViewById(R.id.etRBikash);
        etRefferal = findViewById(R.id.etRReferral);

        btnRegister =findViewById(R.id.btnSignUp);

    }

    private void updateView(int y, int m, int d) {

        String month = "", day = "";


            if (m < 10) {
                month = "0" + m;
            } else {
                month = "" + m;
            }

            if (d < 10) {
                day = "0" + d;
            } else {
                day = "" + d;
            }
            etDOB.setText(y + "-" + month + "-" + day);

    }


    private void prepareforRegister() {


        Name = etName.getText().toString();
        Email = etEmail.getText().toString();
        Pass = etPass.getText().toString();
        Phone = etPhone.getText().toString();
        DOB = etDOB.getText().toString();
        City = etCity.getText().toString();
        Bikash = etBikash.getText().toString();
        Referral = etRefferal.getText().toString();



        if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(DOB) || TextUtils.isEmpty(City) || TextUtils.isEmpty(Bikash) || TextUtils.isEmpty(Referral)){

            if(TextUtils.isEmpty(Name)){

                etName.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(Email)){

                etEmail.setError("Please enter your Email!");

            }else if(TextUtils.isEmpty(Pass)){

                etPass.setError("Please enter your Password!");

            }else if(TextUtils.isEmpty(Phone)){

                etPhone.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(DOB)){

                etDOB.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(City)){

                etCity.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(Bikash)){

                etBikash.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(Referral)){

                etRefferal.setError("Please enter your name!");

            }
        }else {

            showProgressDialog();

            register();

        }
    }



    private void register(){

        StringRequest request = new StringRequest(Request.Method.POST,APIConstants.Auth.REGISTER, createMyReqSuccessListener(),createMyReqErrorListener()){

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                String str = "{\"name\":\""+Name+"\",\"email\":\""+Email+"\",\"password\":\""+Pass+"\",\"phone\":\""+Phone+"\",\"birthdate\":\""+DOB+"\",\"city\":\""+City+"\",\"transaction_id\":\""+Bikash+"\",\"referral_id\":\""+Referral+"\"}";

                String adb = "{\"name\": \""+Name+"\",\"email\": \""+Email+"\",\"password\": \""+Pass+"\",\"phone\": \""+Phone+"\",\"birthdate\":\""+DOB+"\",\"city\": \""+City+"\",\"transaction_id\": \""+Bikash+"\",\"referral_id\": \""+Referral+"\"}";
                LogMe.d("data::",adb);
                return adb.getBytes();
            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(request);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogMe.i(TAG,"Ski data from server - "+response);
                hideProgressDialog();

                try {
                    JSONObject jObj = new JSONObject(response);


                    if(jObj.has("success")){

                        if(jObj.getString("success").equals("true")){

                            JSONObject obj = new JSONObject(jObj.getString("data"));
                            userPref.setUserEmail(Email);
                            userPref.setUserPasssword(Pass);

                            Intent in =new Intent(RegisterActivity.this, LoginActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogMe.i(TAG,"Ski error connect - "+error);
                hideProgressDialog();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        LogMe.d("er::",res);
                        JSONObject obj = new JSONObject(res);

                        String errMsg  = obj.getString("message");
                        //View parentLayout = findViewById(android.R.id.content);
                       /* Snackbar.make(parentLayout, errMsg, Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();*/

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(RegisterActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(RegisterActivity.this);
                        }
                        builder.setTitle("ALERT!!!")
                                .setMessage(errMsg)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        };
    }




}
