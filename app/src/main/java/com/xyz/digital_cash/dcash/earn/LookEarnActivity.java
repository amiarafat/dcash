package com.xyz.digital_cash.dcash.earn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.MobileAds;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.facebook.ads.*;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;


public class LookEarnActivity extends BaseActivity {

    Button btnAdmobInterstial,btnAppLovinInterstial, btnFacebookInterstial, btnFlurryInterstial;
    private InterstitialAd mInterstitialAdmobAd;
    private Toolbar toolbar_video_wall;
    UserPref userPref;


    private AppLovinAd loadedAd;
    AppLovinInterstitialAdDialog interstitialAppLovinAd;


    //Facebook Audiance
    //FACEBOOK INTERSTIAL
    private com.facebook.ads.InterstitialAd interstitialFacebookAd ;
    private static final String PLACEMENT_ID = "579785942451116_579786835784360";
    private String msg = "";
    private String TAG = LookEarnActivity.class.getSimpleName();
    private TextView tvLookEarnEBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_earn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();

        //Admob Interstial
        btnAdmobInterstial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                mInterstitialAdmobAd.loadAd(new AdRequest.Builder().build());


            }
        });
        mInterstitialAdmobAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                hideProgressDialog();
                mInterstitialAdmobAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

                hideProgressDialog();

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LookEarnActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LookEarnActivity.this);
                }
                builder.setTitle("ALERT!!!")
                        .setMessage("The interstitial wasn't loaded yet. Error Code: "+errorCode)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                getInterstialRewarded();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });



        //APPLovin Interstial
        interstitialAppLovinAd = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( this ), this );
        interstitialAppLovinAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                getInterstialRewarded();
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {

            }
        });
        interstitialAppLovinAd.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {

            }
        });
        interstitialAppLovinAd.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {

            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {

            }
        });
        btnAppLovinInterstial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                // Load an Interstitial Ad
                AppLovinSdk.getInstance( LookEarnActivity.this ).getAdService().loadNextAd( AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener()
                {
                    @Override
                    public void adReceived(AppLovinAd ad)
                    {
                        hideProgressDialog();
                        loadedAd = ad;
                        interstitialAppLovinAd.showAndRender( loadedAd );
                    }

                    @Override
                    public void failedToReceiveAd(int errorCode)
                    {

                        hideProgressDialog();
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(LookEarnActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(LookEarnActivity.this);
                        }
                        builder.setTitle("ALERT!!!")
                                .setMessage(errorCode)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        LogMe.d("AppLovinErr:",errorCode+"");
                    }
                } );
            }
        });


        //Facebook Interstial App
        btnFacebookInterstial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();

                interstitialFacebookAd.setAdListener(new InterstitialAdExtendedListener() {
                    @Override
                    public void onInterstitialActivityDestroyed() {

                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        getInterstialRewarded();
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {

                        hideProgressDialog();

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(LookEarnActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(LookEarnActivity.this);
                        }
                        builder.setTitle("ALERT!!!")
                                .setMessage(adError.getErrorCode()+" : "+adError.getErrorMessage())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        LogMe.d("aderr::",adError.getErrorCode()+"---"+adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        hideProgressDialog();
                        interstitialFacebookAd.show();
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });

                interstitialFacebookAd.loadAd();
            }
        });


        btnFlurryInterstial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in =new Intent(LookEarnActivity.this,FlurryMainActivity.class);
                startActivity(in);
            }
        });

    }

    private void initializeView() {

        toolbar_video_wall = (Toolbar) findViewById(R.id.toolbar_look_earn);
        toolbar_video_wall.setTitle("Look and Earn");
        setSupportActionBar(toolbar_video_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userPref = new UserPref(this);

        tvLookEarnEBalance = findViewById(R.id.tvLookEarnEBalance);
        tvLookEarnEBalance.setText(userPref.getUserEarningBalance()+" BDT");

        //For Test Run
        //MobileAds.initialize(LookEarnActivity.this,"ca-app-pub-3940256099942544~3347511713");

        //For Live run
        MobileAds.initialize(LookEarnActivity.this,"ca-app-pub-8456560515447750~2322879071");
        mInterstitialAdmobAd = new InterstitialAd(LookEarnActivity.this);
        //For Test Run
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //For Live Run
        mInterstitialAdmobAd.setAdUnitId("ca-app-pub-8456560515447750/4494096867");
        btnAdmobInterstial = findViewById(R.id.btnAdmobInterstial);
        btnAppLovinInterstial = findViewById(R.id.btnAppLovinInterstial);


        //FACEEBOOK AUDIANCE
        btnFacebookInterstial = findViewById(R.id.btnFacebookInterstial);
        interstitialFacebookAd = new com.facebook.ads.InterstitialAd(this, PLACEMENT_ID);

        btnFlurryInterstial= findViewById(R.id.btnFlurryInterstial);
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


    private void getInterstialRewarded() {


        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Reward.INTERSTIALREWARD, new Response.Listener<String>() {

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
                            tvLookEarnEBalance.setText(EarningBalance +" BDT");


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

                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(LookEarnActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(LookEarnActivity.this);
                            }
                            builder.setTitle("ALERT!!!")
                                    .setMessage("Server Error! Please try again later")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            LogMe.d("er::", res);
                            JSONObject obj = new JSONObject(res);

                            String errMsg = obj.getString("message");
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(LookEarnActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(LookEarnActivity.this);
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
