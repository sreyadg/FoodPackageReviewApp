package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;
import com.lipssmackingparadise.reviewapp.model.userDetails;
import com.mongodb.util.JSONParseException;


import org.json.JSONException;

public class UserDashboard extends Activity implements ServerConnectListener {

    //SharedPreferences
    private SharedPreferences userNameLoggedIn;
    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String KEY_PRIVATE = "KEY_PRIVATE";
    private String SharedPreferencesUsername;

    //SharedPreferences
    private SharedPreferences userID;
    public static final String PREFS_PRIVATE_ID = "PREFS_PRIVATE_ID";
    public static final String KEY_PRIVATE_ID = "KEY_PRIVATE_ID";
    private String SharedPreferencesID;

    // Dialog
    private AlertDialog message_dialog;

    //Activity variables
    private String username;

    // NETWORK
    private ServerConnectListener listener;

    //messages from the server
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";

    //JSONTAG for json.simple
    public static final String JSONTAG_USERNAME= "username";
    public static final String JSONTAG_PASSWORD = "password";
    public static final String JSONTAG_FIRST_NAME = "first_name";
    public static final String JSONTAG_LAST_NAME = "last_name";
    public static final String JSONTAG_EMAIL = "email";
    public static final String JSONTAG_DOB = "dob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        displayUsername();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_dashboard, menu);
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

    //TODO implement correct password

    public void displayUsername() {

        // Get hold of sharedPreferences
        userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);

        //Get hold of output textview - to display values
        TextView displayUsername = (TextView) findViewById(R.id.displayUsername);

        // Set values form sharedPreferences to the textViews
        displayUsername.setText("Welcome" + " " + userNameLoggedIn.getString(Login.KEY_PRIVATE, "NA") + "!");

        retrieveUserDetails();
    }

    public void displayDetails (userDetails currentUser) {

        TextView userUsername = (TextView) findViewById(R.id.userUsername);
        TextView userFirstName = (TextView) findViewById(R.id.userFirstName);
        TextView userLastName = (TextView) findViewById(R.id.userLastName);
        TextView userDOB = (TextView) findViewById(R.id.userDOB);
        TextView userEmail = (TextView) findViewById(R.id.userEmail);

        // Set values form sharedPreferences to the textViews
        userUsername.setText("Username" + " : " + currentUser.username);
        userFirstName.setText("First Name" + " : " + currentUser.first_name);
        userLastName.setText("Last Name" + " : " + currentUser.last_name);
        userDOB.setText("Email" + " : "  + currentUser.email);
        userEmail.setText("Date of Birth" + " : "  + currentUser.dob);

        SharedPreferencesID = currentUser._id;
        userID = getSharedPreferences(UserDashboard.PREFS_PRIVATE_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor privateEdit = userID.edit();
        privateEdit.putString(UserDashboard.KEY_PRIVATE_ID, SharedPreferencesID);
        privateEdit.commit();

    }

    private void initialise() {

        listener = this;
    }

    private void retrieveUserDetails () {

        initialise();

        // Get hold of sharedPreferences
        userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);
        username = userNameLoggedIn.getString(Login.KEY_PRIVATE, "NA");

        if (!username.isEmpty()) {

            try {
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("username", username);
                ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_USERS, listener, dataToSend.toString());
                connect.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setMessageDialogAndShow (MESSAGE_ERROR_UNKNOWN, true);
        }
    }

    public void update_details (View v) {
        
        startActivity(new Intent(UserDashboard.this, UpdateDetails.class));
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


        try {
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() == 1) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                // Get supervisor details
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                String first_name = jsonObject.getString("first_name");
                String last_name = jsonObject.getString("last_name");
                String email = jsonObject.getString("email");
                String dob = jsonObject.getString("dob");
                String _id = jsonObject.getString("_id");

                userDetails currentUser = new userDetails(username, first_name, last_name, dob, email, password, _id);
                displayDetails(currentUser);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void errorOnServerConnect() {

        //
    }

}
