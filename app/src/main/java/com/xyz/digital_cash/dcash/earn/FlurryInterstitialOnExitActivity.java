package com.xyz.digital_cash.dcash.earn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdInterstitial;
import com.flurry.android.ads.FlurryAdInterstitialListener;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.extras.LogMe;

public class FlurryInterstitialOnExitActivity extends AppCompatActivity {

    private final static String TAG = FlurryInterstitialOnExitActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flurry_interstitial_on_exit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showTransitionAd();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showTransitionAd();
    }

    private void showTransitionAd() {
        FlurryAdInterstitial flurryAdInterstitial = new FlurryAdInterstitial(this, "InterstitialTest");
        flurryAdInterstitial.setListener(new FlurryAdInterstitialListener() {
            @Override
            public void onFetched(FlurryAdInterstitial flurryAdInterstitial) {
                LogMe.i(TAG, "Full screen ad fetched");
                flurryAdInterstitial.displayAd();
            }

            @Override
            public void onRendered(FlurryAdInterstitial flurryAdInterstitial) {

            }

            @Override
            public void onDisplay(FlurryAdInterstitial flurryAdInterstitial) {

            }

            @Override
            public void onClose(FlurryAdInterstitial flurryAdInterstitial) {
                FlurryInterstitialOnExitActivity.this.finish();
            }

            @Override
            public void onAppExit(FlurryAdInterstitial flurryAdInterstitial) {

            }

            @Override
            public void onClicked(FlurryAdInterstitial flurryAdInterstitial) {

            }

            @Override
            public void onVideoCompleted(FlurryAdInterstitial flurryAdInterstitial) {

            }

            @Override
            public void onError(FlurryAdInterstitial flurryAdInterstitial,
                                FlurryAdErrorType flurryAdErrorType, int i) {
                Log.e(TAG, "Full screen ad load error - Error type: " + flurryAdErrorType + " Code: " + i);
                Toast.makeText(FlurryInterstitialOnExitActivity.this, "Ad load failed", Toast.LENGTH_SHORT).show();
                FlurryInterstitialOnExitActivity.this.finish();
            }
        });
        flurryAdInterstitial.fetchAd();
    }
}