package com.example.arafat.dcash.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPref {

    private static final String TAG = UserPref.class.getSimpleName();
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserInfoPref";
    private static final String USER_ACCESS_TOKEN = "UserAccToken";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_PASSSWORD = "UserPass";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    /**
     * Constructor of UserPref
     *
     * @param context
     */
    public UserPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    /**
     * set user access token
     *
     * @param accessToken
     */
    public void setUserAccessToken(String accessToken) {
        editor.putString(USER_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    /**
     * getting user access token
     *
     * @param @null
     */
    public String getUserAccessToken() {
        return sharedPreferences.getString(USER_ACCESS_TOKEN, "");
    }



    /**
     * set user email
     *
     * @param email
     */
    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    /**
     * getting user email
     *
     * @param @null
     */
    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }


    /**
     * set driver access token
     *
     * @param pass
     */
    public void setUserPasssword(String pass) {
        editor.putString(USER_PASSSWORD, pass);
        editor.apply();
    }

    /**
     * getting driver access token
     *
     * @param @null
     */
    public String getUserPasssword() {
        return sharedPreferences.getString(USER_PASSSWORD, "");
    }

}
