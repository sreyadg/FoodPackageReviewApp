package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;

import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.model.userDetails;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;


public class AuthenticateUser extends Activity implements ServerConnectListener {

    //SharedPreferences username
    private SharedPreferences userNameLoggedIn;

    //SharedPreferences password
    private SharedPreferences userPassword;

    // NETWORK
    private ServerConnectListener listener;

    // Dialog
    private AlertDialog message_dialog;

    //Activity variables
    private String username;
    private String password;

    //messages from the server
    private final String MESSAGE_ERROR_WRONG_LOGIN_DETAILS = "Incorrect username/password!";
    private final String MESSAGE_NOT_ADMIN = "Not admin";
    private final String MESSAGE_ADMIN = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_user);
        authenticate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authenticate_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialiseSharedPreferences(){

        // Get hold of sharedPreferences
        userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);
        username = userNameLoggedIn.getString(Login.KEY_PRIVATE, "NA");


        // Get hold of sharedPreferences
        userPassword = getSharedPreferences(Login.PREFS_PRIVATE_PASSWORD, Context.MODE_PRIVATE);
        password = userPassword.getString(Login.KEY_PRIVATE_PASSWORD, "NA");

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticateUser.this);
        message_dialog = builder.create();

        listener = this;

    }

    private void authenticate() {

        initialiseSharedPreferences();

        try {
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("username", username);
            dataToSend.put("password", password);
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_LOGIN_AUTHENTICATE, listener, dataToSend.toString());
            connect.execute();

        } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }



    @Override
    public void retrieveCompleted(String result) {
        //
    }

    @Override
    public void sendCompleted(String result) {

        if (result.equals(MESSAGE_ADMIN)){
            startActivity(new Intent(AuthenticateUser.this, AdminAccess.class));
        }

        else if (result.equals(MESSAGE_ERROR_WRONG_LOGIN_DETAILS)){
            setMessageDialogAndShow(MESSAGE_ERROR_WRONG_LOGIN_DETAILS, true);
        }

        else {
            startActivity(new Intent(AuthenticateUser.this, UserDashboard.class)) ;
        }

    }

    @Override
    public void errorOnServerConnect() {
        //
    }
}
