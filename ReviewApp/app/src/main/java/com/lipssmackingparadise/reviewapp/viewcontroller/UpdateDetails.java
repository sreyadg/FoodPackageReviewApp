package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.model.userDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UpdateDetails extends Activity implements ServerConnectListener {

    // NETWORK
    private ServerConnectListener listener;

    //SharedPreferences
    private SharedPreferences userID;
    private SharedPreferences userNameLoggedIn;

    //Activity variables
    private String _id;

    // Dialog
    private AlertDialog message_dialog;

    //messages from the server
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        displayUsername();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_details, menu);
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

    public void displayUsername() {

        // Get hold of sharedPreferences
        userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);

        //Get hold of output textview - to display values
        TextView displayUsername = (TextView) findViewById(R.id.displayUsername);

        // Set values form sharedPreferences to the textViews
        displayUsername.setText("Welcome" + " " + userNameLoggedIn.getString(Login.KEY_PRIVATE, "NA") + "!");

        retrieveDetailsFromServerConnect();
    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }



    public void getUserId() {
        // Get hold of sharedPreferences
        userID = getSharedPreferences(UserDashboard.PREFS_PRIVATE_ID, Context.MODE_PRIVATE);
        _id = userID.getString(UserDashboard.KEY_PRIVATE_ID, "NA");
        listener = this;
    }

    public void retrieveDetails(userDetails currentUser){

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

    }

    public void retrieveDetailsFromServerConnect(){

        getUserId();
        if (!_id.isEmpty()) {

            try {
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("_id", _id);
                ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_USERS, listener, dataToSend.toString());
                connect.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setMessageDialogAndShow (MESSAGE_ERROR_UNKNOWN, true);
        }

    }

    public void update_personal_details (View v) {
        startActivity(new Intent(UpdateDetails.this, UpdatePersonalDetails.class));
    }

    public void update_username_password (View v) {
        startActivity(new Intent(UpdateDetails.this, UpdateUsernamePassword.class));
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
                retrieveDetails(currentUser);
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

    @Override
    public void retrieveCompleted(String result) {
        // not used
    }


}
