package com.xyz.digital_cash.dcash.extras;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.auth.LoginActivity;
import com.xyz.digital_cash.dcash.earn.VideoWallActivity;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;

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


    public void loggingOut() {

        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.LOGOUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("LoginRes::",response);

                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if(jsonObject.has("success")) {
                        String ApiSuccess = jsonObject.getString("success");

                        if (ApiSuccess == "true") {

                            logout(BaseActivity.this);

                        }
                    }else {

                        Toast.makeText(getApplicationContext(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                LogMe.d(TAG,"er::"+ APIConstants.Auth.LOGOUT);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                LogMe.d(TAG,userPref.getUserAccessToken());

                return params;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void underConstruction(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(BaseActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(BaseActivity.this);
        }
        builder.setTitle("ALERT!!!")
                .setMessage("This item is under construction now. Will be available soon!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
