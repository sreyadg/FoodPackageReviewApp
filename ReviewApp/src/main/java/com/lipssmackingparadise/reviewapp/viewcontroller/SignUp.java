package com.lipssmackingparadise.reviewapp.viewcontroller;

import com.lipssmackingparadise.reviewapp.R;
import java.net.UnknownHostException;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mongodb.*;

import com.lipssmackingparadise.reviewapp.model.userDetails;


public class SignUp extends Activity implements ServerConnectListener {

    // NETWORK
    private ServerConnectListener listener;

    // Dialog
    private AlertDialog message_dialog;

    //messages from the server
    private final String MESSAGE_EXISTING_LOGIN_DETAILS = "Error! Login details already exists.";
    private final String MESSAGE_ERROR_ADDING = "There was a problem adding the information to the database.";
    private final String MESSAGE_NEW_USER_SUCCESS = "Added user login details!";

    //SharedPreferences
    private SharedPreferences NewUserNameLoggedIn;
    public static final String PREFS_PRIVATE_NEW_USER = "PREFS_PRIVATE_NEW_USER";
    public static final String KEY_PRIVATE_NEW_USER = "KEY_PRIVATE_NEW_USER";
    private String SharedPreferencesNewUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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

    private void initialise() {
        // Initialise ServerConnectListener
        listener = this;

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        message_dialog = builder.create();
    }

    @Override
    public void retrieveCompleted(String result) {
        // not used
    }

    @Override
    public void sendCompleted(String result) {
        if (result.equals(MESSAGE_NEW_USER_SUCCESS)){
            startActivity(new Intent(SignUp.this, UserDashboard.class));
        }

        else if (result.equals(MESSAGE_ERROR_ADDING)){
            setMessageDialogAndShow(MESSAGE_ERROR_ADDING, true);
        }

        else if (result.equals(MESSAGE_EXISTING_LOGIN_DETAILS)){
            setMessageDialogAndShow(MESSAGE_EXISTING_LOGIN_DETAILS, true);
        }
    }

    @Override
    public void errorOnServerConnect() {
        //to be used
    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }

    public void newUser (View v) throws UnknownHostException {

        initialise();

        final EditText password1Field = (EditText) findViewById(R.id.new_password);
        String password1 = password1Field.getText().toString();

        final EditText password2Field = (EditText) findViewById(R.id.new_retype_password);
        String password2 = password2Field.getText().toString();


        if (!password1.equals(password2)) {
            password2Field.setError("Passwords do not match!");
        }

        else {
            String dataToSend = null;
            try{
                dataToSend = jsonifyUserData();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            if(dataToSend != null) {
                ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_USERS_CREATE, listener, dataToSend);
                connect.execute();
            }

        }
    }

    private String jsonifyUserData() throws JSONException {

        final EditText usernameField = (EditText) findViewById(R.id.new_username);
        String username = usernameField.getText().toString();

        final EditText firstnameField = (EditText) findViewById(R.id.new_first_name);
        String firstname = firstnameField.getText().toString();

        final EditText lastnameField = (EditText) findViewById(R.id.new_last_name);
        String lastname = lastnameField.getText().toString();

        final EditText dobField = (EditText) findViewById(R.id.new_dob);
        String dob = dobField.getText().toString();

        final EditText emailField = (EditText) findViewById(R.id.new_email);
        String email = emailField.getText().toString();

        final EditText password1Field = (EditText) findViewById(R.id.new_password);
        String password1 = password1Field.getText().toString();

        SharedPreferencesNewUsername = username;
        NewUserNameLoggedIn = getSharedPreferences(SignUp.PREFS_PRIVATE_NEW_USER , Context.MODE_PRIVATE);
        SharedPreferences.Editor privateEdit = NewUserNameLoggedIn.edit();
        privateEdit.putString(SignUp.KEY_PRIVATE_NEW_USER, SharedPreferencesNewUsername);
        privateEdit.commit();

        JSONObject dataToSend = new JSONObject();
        dataToSend.put("username", username);
        dataToSend.put("first_name", firstname);
        dataToSend.put("last_name", lastname);
        dataToSend.put("dob", dob);
        dataToSend.put("email", email);
        dataToSend.put("password", password1);
        return dataToSend.toString();
    }

}
