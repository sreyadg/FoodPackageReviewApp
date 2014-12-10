package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;


import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaggiReview1 extends Activity implements ServerConnectListener {

    private RatingBar taste;
    private RatingBar packaging;
    private String product_name;

    private String taste_rating;
    private String packaging_rating;

    // NETWORK
    private ServerConnectListener listener;

    // Dialog
    private AlertDialog message_dialog;

    //messages from the server
    private final String MESSAGE_ADD_REVIEW_ERROR = "There was a problem adding the information to the database.";
    private final String MESSAGE_ADD_REVIEW_SUCCESS = "Added review!";
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maggi_review1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maggi_review1, menu);
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

        System.out.println(result);

        if (result.equals(MESSAGE_ADD_REVIEW_ERROR)){
            setMessageDialogAndShow(MESSAGE_ADD_REVIEW_ERROR, true);
        }

        else if (result.equals(MESSAGE_ADD_REVIEW_SUCCESS)){
            startActivity(new Intent(MaggiReview1.this, MaggiReview2.class));
        }

        else {
            setMessageDialogAndShow(MESSAGE_ERROR_UNKNOWN, true);
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
    public void errorOnServerConnect() {
        //
    }

    public void addReview (View v) {
        String dataToSend = null;
        try{
            dataToSend = jsonifyUserData();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(dataToSend != null) {
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_REVIEWS_CREATE, listener, dataToSend);
            connect.execute();
        }
    }

    private void initialise(){

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MaggiReview1.this);
        message_dialog = builder.create();

        listener = this;
    }

    private String jsonifyUserData() throws JSONException {

        initialise();

        taste = (RatingBar) findViewById(R.id.taste);
        taste_rating = String.valueOf(taste.getRating());

        packaging = (RatingBar) findViewById(R.id.packaging);
        packaging_rating= String.valueOf(packaging.getRating());

        product_name = "Maggi_Noodles";

        JSONObject dataToSend = new JSONObject();
        dataToSend.put("product_name", product_name);
        dataToSend.put("taste", taste_rating);
        dataToSend.put("packaging", packaging_rating);
        return dataToSend.toString();
    }
}
