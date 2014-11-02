package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.model.userDetails;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

public class UpdatePersonalDetails extends Activity implements ServerConnectListener {

    // NETWORK
    private ServerConnectListener listener;

    //SharedPreferences
    private SharedPreferences userID;

    //Activity variables
    private String _id;

    // Dialog
    private AlertDialog message_dialog;

    //messages from the server
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";
    private final String MESSAGE_UPDATE_ERROR = "There was a problem modifying the information to the database.";
    private final String MESSAGE_UPDATE_SUCCESS = "Update successful!";
    private final String MESSAGE_EMPTY = "No changes made!";

    public void getUserId() {
        // Get hold of sharedPreferences
        userID = getSharedPreferences(UserDashboard.PREFS_PRIVATE_ID, Context.MODE_PRIVATE);
        _id = userID.getString(UserDashboard.KEY_PRIVATE_ID, "NA");
        listener = this;
        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePersonalDetails.this);
        message_dialog = builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_details);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_personal_details, menu);
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

    @Override
    public void sendCompleted(String result) {

        if (result.equals(MESSAGE_UPDATE_SUCCESS)){
            startActivity(new Intent(UpdatePersonalDetails.this, UserDashboard.class));
        }

        else if (result.equals(MESSAGE_UPDATE_ERROR)){
            setMessageDialogAndShow(MESSAGE_UPDATE_ERROR, true);
        }

        else if (result.equals(MESSAGE_EMPTY)){
            setMessageDialogAndShow(MESSAGE_EMPTY, true);
        }

        else {
            setMessageDialogAndShow(MESSAGE_ERROR_UNKNOWN, true);
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

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }

    public void updateUser(View V) {

        getUserId();

        String dataToSend = null;
        try{
            dataToSend = jsonifyUserData();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(dataToSend != null) {
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_USERS_UPDATE, listener, dataToSend);
            connect.execute();
        }
    }

    private String jsonifyUserData() throws JSONException {

        final EditText firstnameField = (EditText) findViewById(R.id.updated_first_name);
        String firstname = firstnameField.getText().toString();

        final EditText lastnameField = (EditText) findViewById(R.id.updated_last_name);
        String lastname = lastnameField.getText().toString();

        final EditText dobField = (EditText) findViewById(R.id.updated_dob);
        String dob = dobField.getText().toString();

        final EditText emailField = (EditText) findViewById(R.id.updated_email);
        String email = emailField.getText().toString();


        JSONObject dataToSend = new JSONObject();
        dataToSend.put("_id", _id);
        dataToSend.put("first_name", firstname);
        dataToSend.put("last_name", lastname);
        dataToSend.put("dob", dob);
        dataToSend.put("email", email);
        return dataToSend.toString();
    }
}
