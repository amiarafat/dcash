package com.xyz.digital_cash.dcash.earn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.extras.BaseActivity;

public class IncomeFactory extends BaseActivity {

    private static final String TAG = IncomeFactory.class.getSimpleName();

    Toolbar toolbar_income_factory;

    ImageView ivVideoWall,ivFbWall,ivLookEarn, ivYoutube, ivOfferWall;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_factory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initializeView();

        ivVideoWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in =new Intent(IncomeFactory.this, VideoWallActivity.class);
                startActivity(in);
            }
        });

        ivFbWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent in =new Intent(IncomeFactory.this, FBWallActivity.class);
                startActivity(in);*/

                underConstruction();
            }
        });

        ivLookEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(IncomeFactory.this, LookEarnActivity.class);
                startActivity(in);

            }
        });

        ivYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                underConstruction();
            }
        });

        ivOfferWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                underConstruction();
            }
        });
    }



    private void initializeView(){

        toolbar_income_factory = (Toolbar) findViewById(R.id.toolbar_income_factory);
        setSupportActionBar(toolbar_income_factory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ivVideoWall = findViewById(R.id.ivVideoWall);
        ivFbWall = findViewById(R.id.ivFBWall);
        ivLookEarn = findViewById(R.id.ivLookandEarn);

        ivOfferWall = findViewById(R.id.ivOfferWall);
        ivYoutube = findViewById(R.id.ivYoutubeWall);

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
        finish();
    }


}
