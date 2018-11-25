package com.xyz.digital_cash.dcash.earn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.auth.LoginActivity;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;

public class VideoWallActivity extends BaseActivity implements RewardedVideoAdListener {


    private static final String TAG = "VideoWallActivity";
    //VIDEO WALL
    RewardedVideoAd rAdd;
    private RewardedVideoAd mRewardedVideoAd;
    Button btnLoadAgain, btnAdColony;
    Toolbar toolbar_video_wall;

    private ProgressBar progress;
    UserPref userPref;
    private String msg = "";
    TextView tvVideoWallEBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_wall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intializeView();




        //TEST APP ID
        //MobileAds.initialize(VideoWallActivity.this, "ca-app-pub-3940256099942544~3347511713");
        //LIVE APP ID
        MobileAds.initialize(VideoWallActivity.this, "ca-app-pub-8456560515447750~2322879071");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(VideoWallActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(VideoWallActivity.this);

        btnLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);
                loadRewardedVideoAd();
            }
        });




        //AdColony

        progress = findViewById(R.id.progress);
        // Construct optional app options object to be sent with configure

    }

    private void intializeView() {
        toolbar_video_wall = (Toolbar) findViewById(R.id.toolbar_video_wall);
        toolbar_video_wall.setTitle("Video Wall");
        setSupportActionBar(toolbar_video_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userPref = new UserPref(this);

        btnLoadAgain = findViewById(R.id.btnVideoLoadAgain);
        tvVideoWallEBalance = findViewById(R.id.tvVideoWallEBalance);

        if(userPref.getUserEarningBalance()!= null){
        tvVideoWallEBalance.setText(userPref.getUserEarningBalance()+" BDT");
        }

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.GONE);
        displayAd();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
        msg = "";
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

        // Load the next rewarded video ad.
        //loadRewardedVideoAd();

        if (!TextUtils.isEmpty(msg)){


        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        //Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.

        getAdmobRewarded();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad---"+ i, Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoCompleted() {
       // Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }


    private void displayAd() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {

        //TEST APP UNIT ID
        //mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());

        //LIVE APP UNIT ID
        mRewardedVideoAd.loadAd("ca-app-pub-8456560515447750/1978164068",new AdRequest.Builder().build());


    }

    @Override
    public void onResume() {
//        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
//        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //      mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void getAdmobRewarded() {


        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Reward.ADMOBREWARD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("admob::",response);

                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("success")) {
                        String ApiSuccess = jsonObject.getString("success");

                        if (ApiSuccess == "true") {

                            msg = jsonObject.getString("message");
                            String data =jsonObject.getString("data");
                            JSONObject jData = new JSONObject(data);
                            String EarningBalance = jData.getString("earning_balance");

                            tvVideoWallEBalance.setText(EarningBalance +" BDT");


                        } else {

                            Toast.makeText(getApplicationContext(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
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

                        LogMe.d("erres::",res);

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                LogMe.d(TAG,userPref.getUserAccessToken());

                return params;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }
}
