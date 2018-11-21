package com.example.arafat.dcash.earn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arafat.dcash.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class VideoWallActivity extends AppCompatActivity implements RewardedVideoAdListener {


    //VIDEO WALL
    RewardedVideoAd rAdd;
    private RewardedVideoAd mRewardedVideoAd;
    Button btnLoadAgain;

    Toolbar toolbar_video_wall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_wall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLoadAgain = findViewById(R.id.btnVideoLoadAgain);
        toolbar_video_wall = (Toolbar) findViewById(R.id.toolbar_video_wall);
        setSupportActionBar(toolbar_video_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //TEST APP ID
        MobileAds.initialize(VideoWallActivity.this, "ca-app-pub-3940256099942544~3347511713");

        //LIVE APP ID
        //MobileAds.initialize(IncomeFactory.this, "ca-app-pub-7470896348181474~3905856817");


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(VideoWallActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(VideoWallActivity.this);

        btnLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadRewardedVideoAd();
            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();

        displayAd();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

        // Load the next rewarded video ad.
        //loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }


    private void displayAd() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {

        //TEST APP UNIT ID
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());

        //LIVE APP UNIT ID
        //mRewardedVideoAd.loadAd("ca-app-pub-7470896348181474/2918834935",new AdRequest.Builder().build());


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

}
