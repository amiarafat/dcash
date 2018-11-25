package com.xyz.digital_cash.dcash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.earn.IncomeFactory;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.profile.UserProfileActivity;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;

public class DCASHMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "DCASHMainActivity";
    CardView cardEarnByIvite;

    UserPref userPref;
    CardView cardIncomeFactory;
    NavigationView navigationView;

    TextView tvUserNmae, tvuserEmail, tvUserAvailableBalance,tvUserEarningBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcashmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeView();

        getProfileInfo();

    }

    private void getProfileInfo() {

            StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.USER_PROFILE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    LogMe.d("ProfileRes::",response);

                    hideProgressDialog();

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.has("success")) {
                            String ApiSuccess = jsonObject.getString("success");

                            if (ApiSuccess == "true") {


                                JSONObject jObj =new JSONObject(jsonObject.getString("data"));

                                String Name = jObj.getString("name");
                                String Email = jObj.getString("email");
                                String AvailableBalance = jObj.getString("available_balance");
                                String EarningBalance = jObj.getString("earning_balance");

                                tvUserNmae.setText(Name);
                                tvuserEmail.setText(Email);
                                tvUserAvailableBalance.setText(AvailableBalance +"BDT");
                                tvUserEarningBalance.setText(EarningBalance+ " BDT");
                                userPref.setUserEarningBalance(EarningBalance);

                            }
                        }else {

                            Toast.makeText(getApplicationContext(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogMe.d("ProfileRes::",e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hideProgressDialog();
                    LogMe.d(TAG,"er::"+ APIConstants.Auth.USER_PROFILE);

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data

                            int stCode = response.statusCode;
                            View parentLayout = findViewById(android.R.id.content);

                            if (stCode == 500) {

                                /*Snackbar.make(parentLayout, "Server Error! Please try again later", Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();*/
                            } else {
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
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                    LogMe.d(TAG,userPref.getUserAccessToken());

                    return params;
                }
            };
            DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }

    private void initializeView() {

        userPref = new UserPref(DCASHMainActivity.this);

        cardEarnByIvite = findViewById(R.id.cardEarnByIvite);
        cardEarnByIvite.setOnClickListener(this);
        cardIncomeFactory = findViewById(R.id.cardIncomeFactory);
        cardIncomeFactory.setOnClickListener(this);


        View headerView = navigationView.getHeaderView(0);

        tvUserNmae = headerView.findViewById(R.id.tvUserName);
        tvuserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserAvailableBalance = headerView.findViewById(R.id.tvUserCurrentBalance);
        tvUserEarningBalance = headerView.findViewById(R.id.tvUserEarningBalance);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dcashmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/

       if(id == R.id.nav_logout){

           showProgressDialog();
           loggingOut();
       }else if(id == R.id.nav_profile){

           Intent in =new Intent(this,UserProfileActivity.class);
           startActivity(in);
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View v) {

        if( v == cardEarnByIvite){
            Intent in =new Intent(DCASHMainActivity.this,EarnByInvitingActivity.class);
            startActivity(in);
        }
        else if( v == cardIncomeFactory){
            Intent in =new Intent(DCASHMainActivity.this,IncomeFactory.class);
            startActivity(in);
        }

    }
}
