package com.lipssmackingparadise.reviewapp.server;

/**
 * Created by User on 29/10/2014.
 */
public class ServerUrl {

    private final String DEFAULT_BASE_URL = "http://lsp.nodejitsu.com/";

    public String BASE_URL = DEFAULT_BASE_URL;

    public String URL_USERS = BASE_URL + "users";

    public String URL_USERS_CREATE = BASE_URL + "users/create";

    public String URL_USERS_UPDATE = BASE_URL + "users/update";

    public String URL_EVENTS = BASE_URL + "events";

    public String URL_EVENTS_CREATE = BASE_URL + "events/create";

    public String URL_LOGIN = BASE_URL + "login";

    public String URL_LOGIN_AUTHENTICATE = BASE_URL + "login/authenticate";

    public String URL_REVIEWS_CREATE = BASE_URL + "reviews/create";

    public String URL_REVIEWS_CREATE_SECOND = BASE_URL + "reviews/create/2";

    public String URL_REVIEWS_VIEW = BASE_URL + "reviews/view";

    private static ServerUrl instance;

    public static ServerUrl getInstance(){
        if(instance == null){
            instance = new ServerUrl();
        }
        return instance;
    }

    public void setBaseUrl(String address) {
        instance = new ServerUrl(address);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    private ServerUrl(String address){
        BASE_URL = address;

        URL_USERS = BASE_URL + "users";

        URL_EVENTS = BASE_URL + "events";

    }

    private ServerUrl(){
    }
}
