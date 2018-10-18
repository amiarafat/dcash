package com.example.arafat.dcash.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arafat.dcash.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
