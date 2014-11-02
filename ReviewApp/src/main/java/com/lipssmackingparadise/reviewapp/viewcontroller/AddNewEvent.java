package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;

import java.net.UnknownHostException;
import java.util.*;

import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class AddNewEvent extends Activity implements ServerConnectListener {

    // NETWORK
    private ServerConnectListener listener;

    // Dialog
    private AlertDialog message_dialog;

//    //Activity variables
//    private String username;
//    private String password;

    //messages from the server
    private final String MESSAGE_ERROR_UNSUCCESSFUL_UPDATE = "There was a problem adding the information to the database.";
    private final String MESSAGE_EVENT_ADD_SUCCESS = "Added event details!";
    private final String MESSAGE_UNKNOWN_ERROR = "Unknown Error!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_event, menu);
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
    public void retrieveCompleted(String result) {
        //
    }

    @Override
    public void sendCompleted(String result) {

        if (result.equals(MESSAGE_EVENT_ADD_SUCCESS)){
            startActivity(new Intent(AddNewEvent.this, AdminAccess.class));
        }

        else if (result.equals(MESSAGE_ERROR_UNSUCCESSFUL_UPDATE)){
            setMessageDialogAndShow(MESSAGE_ERROR_UNSUCCESSFUL_UPDATE, true);
        }

        else {
            setMessageDialogAndShow(MESSAGE_UNKNOWN_ERROR, true);
        }

    }

    @Override
    public void errorOnServerConnect() {
        //
    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }

    private void initialise() {

        // Initialise ServerConnectListener
        listener = this;

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEvent.this);
        message_dialog = builder.create();

    }

    public void create_event (View v) throws UnknownHostException {

        initialise();

        String db_name = "current";

        final EditText companyField = (EditText) findViewById(R.id.product_company);
        String company_product = companyField.getText().toString();

        final EditText productNameField = (EditText) findViewById(R.id.product_name);
        String product_name = productNameField.getText().toString();

        final EditText productDescriptionField = (EditText) findViewById(R.id.product_description);
        String product_description = productDescriptionField.getText().toString();

        final EditText startEventField = (EditText) findViewById(R.id.event_start);
        String event_start = startEventField.getText().toString();

        final EditText endEventField = (EditText) findViewById(R.id.event_end);
        String event_end = endEventField.getText().toString();

        try{
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("company_product", company_product);
            dataToSend.put("product_name", product_name);
            dataToSend.put("product_description", product_description);
            dataToSend.put("event_start", event_start);
            dataToSend.put("event_end", event_end);
            dataToSend.put("db_name", db_name);
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_EVENTS_CREATE, listener, dataToSend.toString());
            connect.execute();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
