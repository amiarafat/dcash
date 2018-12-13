package com.xyz.digital_cash.dcash.earn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdInterstitial;
import com.flurry.android.ads.FlurryAdInterstitialListener;
import com.flurry.android.ads.FlurryAdTargeting;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.extras.LogMe;

import java.util.Arrays;

public class FlurryMainActivity extends AppCompatActivity {

    FlurryAdInterstitial mFlurryAdInterstitial;
    private final static String TAG = FlurryMainActivity.class.getSimpleName();

    Toolbar toolbar_flurry_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flurry_main);
        toolbar_flurry_activity = (Toolbar) findViewById(R.id.toolbar_flurry_activity);
        setSupportActionBar(toolbar_flurry_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LogMe.i(TAG, "Loading full screen ad");
        mFlurryAdInterstitial = new FlurryAdInterstitial(FlurryMainActivity.this, "DCASH1");
        mFlurryAdInterstitial.setListener(mAdInterstitialListener);
        mFlurryAdInterstitial.fetchAd();

        Toast.makeText(FlurryMainActivity.this, "Please wait for ad", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mFlurryAdInterstitial != null) {
            mFlurryAdInterstitial.destroy();
        }

        super.onDestroy();
    }



    FlurryAdInterstitialListener mAdInterstitialListener = new FlurryAdInterstitialListener() {
        @Override
        public void onFetched(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Full screen ad fetched");
            flurryAdInterstitial.displayAd();
        }

        @Override
        public void onRendered(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Ad rendered");
        }

        @Override
        public void onDisplay(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Ad displayed");
        }

        @Override
        public void onClose(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Ad closed");
        }

        @Override
        public void onAppExit(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "App closing");
        }

        @Override
        public void onClicked(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Ad clicked");
        }

        @Override
        public void onVideoCompleted(FlurryAdInterstitial flurryAdInterstitial) {
            LogMe.i(TAG, "Video is completed");
            Toast.makeText(FlurryMainActivity.this, "Video completed, where's my reward",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FlurryAdInterstitial flurryAdInterstitial, FlurryAdErrorType flurryAdErrorType, int i) {
            Log.e(TAG, "Full screen ad load error - Error type: " + flurryAdErrorType + " Code: " + i);
            //Toast.makeText(FlurryMainActivity.this, "Ad load failed - try again", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(FlurryMainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(FlurryMainActivity.this);
            }
            builder.setTitle("ALERT!!!")
                    .setMessage("Full screen ad load error - Error type: " + flurryAdErrorType+". Ad was unfilled by server. Could be due to incorrect ad request, incorrect ad space configuration or no fill at request location at the moment")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    };

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
        finish();
    }

}