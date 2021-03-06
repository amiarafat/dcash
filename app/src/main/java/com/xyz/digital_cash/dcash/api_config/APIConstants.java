package com.xyz.digital_cash.dcash.api_config;

public class APIConstants {


    private static final String URL_SLASH = "/";
    private static final String API_DOMAIN = "https://www.digital-cash.xyz"; // api endpoint

    private static final String ENDPOINT = API_DOMAIN + URL_SLASH + "api"+URL_SLASH+"v1";
    public static String ACCTOKENSTARTER = "Bearer ";

    public static class Auth {

        public static final String LOGIN = ENDPOINT + URL_SLASH +"auth"+URL_SLASH+"login";
        public static final String REGISTER = ENDPOINT + URL_SLASH +"auth"+URL_SLASH+"register";
        public static final String TOKEN_REFRESH = ENDPOINT + URL_SLASH +"auth"+URL_SLASH+"refresh";
        public static final String USER_PROFILE = ENDPOINT + URL_SLASH +"auth"+URL_SLASH+"me";
        public static final String LOGOUT = ENDPOINT + URL_SLASH +"auth"+URL_SLASH+"logout";
    }

    public static class Reward{

        public static final String VIDEOREWARD = ENDPOINT + URL_SLASH +"rewards"+URL_SLASH+"video";
        public static final String INTERSTIALREWARD = ENDPOINT + URL_SLASH +"rewards"+URL_SLASH+"interstitial";


    }
}
