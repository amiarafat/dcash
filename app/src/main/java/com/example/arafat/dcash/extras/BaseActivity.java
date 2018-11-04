package com.example.arafat.dcash.extras;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.arafat.dcash.R;
import com.example.arafat.dcash.auth.LoginActivity;
import com.example.arafat.dcash.shared_pref.UserPref;

public class BaseActivity extends LocalizationActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private Typeface karla_title, karla_regular;
    UserPref userPref;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMe.i(TAG, " called");

        userPref = new UserPref(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));


    }

    /**
     * show Progress dialog
     *
     * @param @null
     */
    public void showProgressDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * hide progress dialog
     *
     * @param @null
     */
    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     * check network is available or not
     *
     * @param context
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    /**
     * log out from app
     *
     * @param context
     */
    public void logout(Context context) {
        LogMe.d(TAG, "logout called from :: " + context);
        SharedPreferences sp = context.getSharedPreferences("UserInfoPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
