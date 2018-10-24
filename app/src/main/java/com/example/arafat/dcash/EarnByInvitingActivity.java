package com.example.arafat.dcash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EarnByInvitingActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnShareLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_by_inviting);

        initializView();
    }

    private void initializView() {

        btnShareLink = findViewById(R.id.btnShareLink);
        btnShareLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if( v == btnShareLink){


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Use this link and earn \n"+"http://digital-cash.xyz/r/");
            sendIntent.setType("text/plain");
            //startActivity(sendIntent);


            PackageManager packageManager = getPackageManager();
            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(sendIntent);
            } else {
                Log.d("tag::", "No Intent available to handle action");
            }
        }
    }
}
