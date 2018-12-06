package com.xyz.digital_cash.dcash.earn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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


import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.auth.LoginActivity;
import com.xyz.digital_cash.dcash.auth.RegisterActivity;
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
    Button btnAdmobAd, btnAdColony, btnAppLovin;
    Toolbar toolbar_video_wall;

    UserPref userPref;
    private String msg = "";
    TextView tvVideoWallEBalance;

    //TEST
    //final private String APP_ID = "app185a7e71e1714831a49ec7";
    //LIVE
    final private String APP_ID = "appfb36d349893f494aa2";

    //TEST
    //final private String ZONE_ID = "vz1fd5a8b2bf6841a0a4b826";
    //LIVE
    final private String ZONE_ID = "vz8c7a332fbecd4c4bba";
    final private String ZONE_ID_2 = "vz94e0c51c1d41478ca2";
    final private String ZONE_ID_3 = "vz03959fc621cb459a82";
    final private String ZONE_ID_4 = "vz970a5e0884824e68bf";

    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;



    //APPLOVIN
    private AppLovinIncentivizedInterstitial myIncent;

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
        //MobileAds.initialize(VideoWallActivity.this, "ca-app-pub-8456560515447750~2322879071");
        MobileAds.initialize(VideoWallActivity.this, "ca-app-pub-8456560515447750~2322879071");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(VideoWallActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(VideoWallActivity.this);

        btnAdmobAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                loadRewardedVideoAd();
            }
        });

        //AdColony

        btnAdColony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ad!=null) {
                    ad.show();
                }else {
                    Log.d(TAG,"ad is null");
                }
            }
        });


        btnAppLovin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAppLovinRewarded();
            }
        });
    }

    private void intializeView() {

        //START
        // Construct optional app options object to be sent with configure
        AdColonyAppOptions app_options = new AdColonyAppOptions().setUserID("1");

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(this, app_options, APP_ID,ZONE_ID);

        // Optional user metadata sent with the ad options in each request
        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(true)
                .enableResultsDialog(true)
                .setUserMetadata(metadata);

        // Create and set a reward listener
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                // Query reward object for info here
                Log.d( TAG, "onReward" );
            }
        });

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                VideoWallActivity.this.ad = ad;
                //showProgressDialog();
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                hideProgressDialog();
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };

        //END

        //AppLovin
        //Start
        myIncent = AppLovinIncentivizedInterstitial.create(this);
        myIncent.preload(null);
        //end


        toolbar_video_wall = (Toolbar) findViewById(R.id.toolbar_video_wall);
        toolbar_video_wall.setTitle("Video Wall");
        setSupportActionBar(toolbar_video_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userPref = new UserPref(this);

        btnAdmobAd = findViewById(R.id.btnVideoLoadAgain);
        tvVideoWallEBalance = findViewById(R.id.tvVideoWallEBalance);

        btnAdColony = findViewById(R.id.btnAdColonyVideo);
        btnAppLovin = findViewById(R.id.btnAplovinVideo);

        if(userPref.getUserEarningBalance()!= null){
        tvVideoWallEBalance.setText(userPref.getUserEarningBalance()+" BDT");
        }

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        hideProgressDialog();
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

        getVideoRewarded();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad---"+ i, Toast.LENGTH_SHORT).show();
        hideProgressDialog();

        String errMsg ="";
        if(i == 0){
             errMsg = "This error generally occurs in newly created ads. So wait for few hours for the ads to be loaded.";
        }else if(i == 2){
            errMsg ="The ad request was unsuccessful due to network connectivity.";
        }else if(i == 3){
            errMsg ="The ad request was successful, but no ad was returned due to lack of ad inventory.";
        }
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(VideoWallActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(VideoWallActivity.this);
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
        //mRewardedVideoAd.loadAd("ca-app-pub-8456560515447750/1978164068",new AdRequest.Builder().build());
        //mRewardedVideoAd.loadAd("ca-app-pub-8456560515447750/7512100306",new AdRequest.Builder().build());
        mRewardedVideoAd.loadAd("ca-app-pub-8456560515447750/7512100306",new AdRequest.Builder().build());


    }

    public void playAppLovinRewarded(){
        // Check to see if a rewarded video is available.
        if(myIncent.isAdReadyToDisplay()){
            // A rewarded video is available.  Call the show method with the listeners you want to use.
            // We will use the display listener to preload the next rewarded video when this one finishes.
            myIncent.show(this, null, null, new AppLovinAdDisplayListener() {
                @Override
                public void adDisplayed(AppLovinAd appLovinAd) {
                    // A rewarded video is being displayed.
                    getVideoRewarded();
                }
                @Override
                public void adHidden(AppLovinAd appLovinAd) {
                    // A rewarded video was closed.  Preload the next video now.  We won't use a load listener.
                    myIncent.preload(null);
                }

            });
        }
        else{
            // No ad is currently available.  Perform failover logic...
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(VideoWallActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(VideoWallActivity.this);
            }
            builder.setTitle("ALERT!!!")
                    .setMessage("No Ad is in Inventory")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    @Override
    public void onResume() {
//        mRewardedVideoAd.resume(this);
        super.onResume();

        if (ad == null || ad.isExpired()) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            //showProgressDialog();
            AdColony.requestInterstitial(ZONE_ID, listener, adOptions);

        }

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



    private void getVideoRewarded() {


        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Reward.VIDEOREWARD, new Response.Listener<String>() {

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

                            userPref.setUserEarningBalance(EarningBalance);
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
