package com.example.arafat.dcash.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.arafat.dcash.DCASHMainActivity;
import com.example.arafat.dcash.R;

public class LoginActivity extends Activity implements View.OnClickListener{

    Button btnResetPass, btnRegister;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeV();
    }

    private void initializeV() {

        btnResetPass = findViewById(R.id.btnResetPass);
        btnResetPass.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        btnLogin =findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == btnResetPass){
            Intent in = new Intent(this,PaswwordResetActivity.class);
            startActivity(in);
        }
        if(v == btnRegister){
            Intent in = new Intent(this,RegisterActivity.class);
            startActivity(in);
        }
        if (v == btnLogin){
            Intent in = new Intent(this,DCASHMainActivity.class);
            startActivity(in);
        }
    }
}
