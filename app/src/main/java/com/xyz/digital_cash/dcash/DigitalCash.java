package com.xyz.digital_cash.dcash;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.flurry.android.FlurryAgent;
import com.xyz.digital_cash.dcash.extras.LogMe;

public class DigitalCash extends Application {

    private String TAG = DigitalCash.class.getSimpleName();
    private static RequestQueue requestQueue;
    private static DigitalCash dCash;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static synchronized DigitalCash getDigitalCash() {
        return dCash;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dCash=this;
        AppLovinSdk.initializeSdk(context);


        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);
        FlurryAgent.setLogEvents(true);
        // NOTE: Use your own Flurry API key. This is left here to make sample review easier
        FlurryAgent.init(this, "5GS9QJPJX5F3DDT8527Z");
        Log.i(TAG, "Flurry SDK initialized");

    }



    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(dCash);
        }
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        LogMe.d(TAG, "addToRequestQueue called from :: " + tag);
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setMuvRent(DigitalCash dCash) {
        this.dCash = dCash;
    }
}
