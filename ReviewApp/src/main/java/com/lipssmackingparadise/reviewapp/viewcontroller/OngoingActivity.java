package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.model.userDetails;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OngoingActivity extends Activity implements ServerConnectListener{

    //ACTIVITY VARIABLES
    private String eventType;

    // NETWORK
    private ServerConnectListener listener;

    //messages from the server
    private final String MESSAGE_ERROR_ON_EMPTY_LOGIN_DETAILS = "Please enter your username and password";
    private final String MESSAGE_ERROR_WRONG_LOGIN_DETAILS = "Incorrect username/password!";
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";
    private final String MESSAGE_SUCCESS = "Success!";
    private final String MESSAGE_ERROR_SERVER = "An error occurred while connecting to the server!";

    // Dialog
    private AlertDialog message_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ongoing, menu);
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

//    public void nestle (View v) {
//        startActivity(new Intent(OngoingActivity.this, Nestle.class));
//    }
//
//    public void hershey (View v) {
//        startActivity(new Intent(OngoingActivity.this, Hersheys.class));
//    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }

    private void initialise(){

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(OngoingActivity.this);
        message_dialog = builder.create();

        listener = this;
        eventType = "current";
    }

    public void goToLogin (View v) {
        startActivity(new Intent(OngoingActivity.this, Login.class));
    }

    public void goToBizOpp (View v) {
        startActivity(new Intent(OngoingActivity.this, BizOpp.class));
    }

    public void maggi (View v) {
        startActivity(new Intent(OngoingActivity.this, MaggiMainPage.class));
    }

    private void getEvents(){

        initialise();

        try {
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("eventType", eventType);
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_LOGIN, listener, dataToSend.toString());
            connect.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void retrieveCompleted(String result) {
        //
    }

    @Override
    public void sendCompleted(String result) {

        //

    }

    @Override
    public void errorOnServerConnect() {

        //
    }
}
