package com.xyz.digital_cash.dcash;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
