package com.xyz.digital_cash.dcash.earn;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.vungle.warren.AdConfig;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleNativeAd;
import com.vungle.warren.error.VungleException;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;

public class VungleNewActivity extends BaseActivity {


    private static final String LOG = "VUNGLE::";
    // UI elements
    private Button initButton;
    private Button load_buttons;
    private Button play_buttons;

    // Get your Vungle App ID and Placement ID information from Vungle Dashboard
    final String LOG_TAG = "VungleSampleApp";

    final String app_id = "5c17bbea4e109c0010b7aa9b";
    private final String PlacementReferenceID = "DEFAULT-0602345";

    private RelativeLayout flexfeed_container;
    private VungleNativeAd vungleNativeAd;
    private View nativeAdView;

    private AdConfig adConfig = new AdConfig();
    Toolbar toolbar_vungle;
    private String msg="";
    private UserPref userPref;
    private TextView tvVungleEBalance;
    private String TAG = "VungleNewActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vungle_new);
        toolbar_vungle = (Toolbar) findViewById(R.id.toolbar_vungle);
        toolbar_vungle.setTitle("VUNGLE");
        setSupportActionBar(toolbar_vungle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initUIElements();

        try {

            initSDK();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("er::",e.getMessage());
        }

    }




    private void initSDK() {
        Vungle.init(app_id, getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG, "InitCallback - onSuccess");
                Vungle.loadAd(PlacementReferenceID, vungleLoadAdCallback);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //setButtonState(initButton, false);
                       /* for (int i = 0; i < 1; i++) {
                            setButtonState(load_buttons[i], !Vungle.canPlayAd(placementsList.get(i)));
                            setButtonState(play_buttons[i], Vungle.canPlayAd(placementsList.get(i)));
                        }*/
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(LOG, "InitCallback - onError: " + throwable.getLocalizedMessage());
            }

            @Override
            public void onAutoCacheAdAvailable(final String placementReferenceID) {
                Log.d(LOG, "InitCallback - onAutoCacheAdAvailable" +
                        "\n\tPlacement Reference ID = " + placementReferenceID);
                //setButtonState(play_buttons[0], true);
            }
        });
    }

    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        @Override
        public void onAdStart(final String placementReferenceID) {
            Log.d(LOG, "PlayAdCallback - onAdStart" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

        }

        @Override
        public void onAdEnd(final String placementReferenceID, final boolean completed, final boolean isCTAClicked) {
            Log.d(LOG, "PlayAdCallback - onAdEnd" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tView Completed = " + completed + "" +
                    "\n\tDownload Clicked = " + isCTAClicked);

            getInterstialRewarded();

            Vungle.loadAd(PlacementReferenceID, vungleLoadAdCallback);
        }

        @Override
        public void onError(final String placementReferenceID, Throwable throwable) {
            Log.d(LOG, "PlayAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(VungleNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(VungleNewActivity.this);
            }
            builder.setTitle("ALERT!!!")
                    .setMessage(throwable.getLocalizedMessage())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            checkInitStatus(throwable);
        }
    };

    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(final String placementReferenceID) {

            Log.d(LOG,"LoadAdCallback - onAdLoad" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

        }

        @Override
        public void onError(final String placementReferenceID, Throwable throwable) {
            Log.d(LOG, "LoadAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(VungleNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(VungleNewActivity.this);
            }
            builder.setTitle("ALERT!!!")
                    .setMessage(throwable.getLocalizedMessage())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            checkInitStatus(throwable);

            checkInitStatus(throwable);
        }
    };


    private void checkInitStatus(Throwable throwable) {
        try {
            VungleException ex = (VungleException) throwable;
            Log.d(LOG, ex.getExceptionCode() + "");

            if (ex.getExceptionCode() == VungleException.VUNGLE_NOT_INTIALIZED) {
                initSDK();
            }
        } catch (ClassCastException cex) {
            Log.d(LOG, cex.getMessage());
        }
    }

    private void initUIElements() {
        //TextView vungle_app_id = (TextView)findViewById(R.id.vungle_app_id);
        //vungle_app_id.setText("App ID: " + app_id);

        userPref = new UserPref(this);
        tvVungleEBalance = findViewById(R.id.tvVungleEBalance);
        tvVungleEBalance.setText(userPref.getUserEarningBalance() +" BDT");

        initButton = (Button)findViewById(R.id.init_button);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // setButtonState(initButton, false);
            }
        });

        TextView placement_id_texts;

        placement_id_texts = (TextView)findViewById(R.id.placement_id2);

        load_buttons = (Button)findViewById(R.id.placement_load2);

        play_buttons = (Button)findViewById(R.id.placement_play2);

        placement_id_texts.setText("Placement ID: " + PlacementReferenceID);



            load_buttons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Vungle.loadAd(PlacementReferenceID, vungleLoadAdCallback);

                }
            });

            play_buttons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Vungle.isInitialized() && Vungle.canPlayAd(PlacementReferenceID)) {


                            Vungle.playAd(PlacementReferenceID, null, vunglePlayAdCallback);

                    }
                }
            });



    }
/*

    private void setButtonState(Button button, boolean enabled) {
        button.setEnabled(enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            button.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }
*/


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (vungleNativeAd != null) {
            vungleNativeAd.setAdVisibility(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (vungleNativeAd != null) {
            vungleNativeAd.setAdVisibility(false);
        }
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

    private void getInterstialRewarded() {


        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Reward.INTERSTIALREWARD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("admob::",response);

                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("success")) {
                        String ApiSuccess = jsonObject.getString("success");

                        if (ApiSuccess == "true") {

                            msg = jsonObject.getString("message");
                            String data =jsonObject.getString("data");
                            JSONObject jData = new JSONObject(data);
                            String EarningBalance = jData.getString("earning_balance");
                            userPref.setUserEarningBalance(EarningBalance);
                            tvVungleEBalance.setText(EarningBalance +" BDT");


                        } else {

                            Toast.makeText(getApplicationContext(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
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

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        LogMe.d("erres::",res);

                        int stCode = response.statusCode;
                        View parentLayout = findViewById(android.R.id.content);

                        if(stCode == 500){

                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(VungleNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(VungleNewActivity.this);
                            }
                            builder.setTitle("ALERT!!!")
                                    .setMessage("Server Error! Please try again later")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            LogMe.d("er::", res);
                            JSONObject obj = new JSONObject(res);

                            String errMsg = obj.getString("message");
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(VungleNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(VungleNewActivity.this);
                            }
                            builder.setTitle("ALERT!!!")
                                    .setMessage(errMsg)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                LogMe.d(LOG_TAG,userPref.getUserAccessToken());

                return params;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }

}
