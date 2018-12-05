package com.xyz.digital_cash.dcash.earn;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;

public class LookEarnActivity extends BaseActivity {

    Button btnAdmobInterstial,btnAppLovinInterstial;
    private InterstitialAd mInterstitialAd;
    private Toolbar toolbar_video_wall;


    private AppLovinAd loadedAd;
    AppLovinInterstitialAdDialog interstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_earn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();

        btnAdmobInterstial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());


            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                hideProgressDialog();
                mInterstitialAd.show();
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



        //APPLovin
        interstitialAd = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( this ), this );
        interstitialAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {

            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {

            }
        });
        interstitialAd.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {

            }
        });
        interstitialAd.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
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


                // Load an Interstitial Ad
                AppLovinSdk.getInstance( LookEarnActivity.this ).getAdService().loadNextAd( AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener()
                {
                    @Override
                    public void adReceived(AppLovinAd ad)
                    {
                        loadedAd = ad;
                        interstitialAd.showAndRender( loadedAd );
                    }

                    @Override
                    public void failedToReceiveAd(int errorCode)
                    {
                        // Look at AppLovinErrorCodes.java for list of error codes.
                        LogMe.d("AppLovinErr:",errorCode+"");
                    }
                } );


            }
        });

    }

    private void initializeView() {

        toolbar_video_wall = (Toolbar) findViewById(R.id.toolbar_look_earn);
        toolbar_video_wall.setTitle("Look and Earn");
        setSupportActionBar(toolbar_video_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For Test Run
        //MobileAds.initialize(LookEarnActivity.this,"ca-app-pub-3940256099942544~3347511713");

        //For Live run
        MobileAds.initialize(LookEarnActivity.this,"ca-app-pub-7470896348181474~3905856817");
        mInterstitialAd = new InterstitialAd(LookEarnActivity.this);
        //For Test Run
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //For Live Run
        mInterstitialAd.setAdUnitId("ca-app-pub-7470896348181474/6960731692");

        btnAdmobInterstial = findViewById(R.id.btnAdmobInterstial);
        btnAppLovinInterstial = findViewById(R.id.btnAppLovinInterstial);
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

}
