package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.model.userDetails;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import android.widget.RatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewMaggiReview extends Activity implements ServerConnectListener{

    // NETWORK
    private ServerConnectListener listener;

    //SharedPreferences
    private SharedPreferences userNameLoggedIn;
    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String KEY_PRIVATE = "KEY_PRIVATE";
    private String SharedPreferencesUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_maggi_review);
        retrieveReview();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_maggi_review, menu);
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

        try {
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() == 1) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                // Get supervisor details
                String taste = jsonObject.getString("taste");
                String packaging = jsonObject.getString("packaging");
                String smell = jsonObject.getString("smell");
                String comments = jsonObject.getString("comments");

                // Get hold of sharedPreferences
                userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);
                //Get hold of output textview - to display values
                TextView displayUsername = (TextView) findViewById(R.id.displayUsernameComments);
                // Set values form sharedPreferences to the textViews


                float tasteRating = Float.parseFloat(taste);
                float packagingRating = Float.parseFloat(packaging);
                float smellRating = Float.parseFloat(smell);

                RatingBar get_taste = (RatingBar) findViewById(R.id.setTaste);
                RatingBar get_packaging = (RatingBar) findViewById(R.id.setPackaging);
                RatingBar get_smell = (RatingBar) findViewById(R.id.setSmell);
                TextView get_comments = (TextView) findViewById(R.id.reviewComments);

                // Set values form sharedPreferences to the textViews
                get_taste.setRating(tasteRating);
                get_packaging.setRating(packagingRating);
                get_smell.setRating(smellRating);
                get_comments.setText(comments);
                displayUsername.setText(userNameLoggedIn.getString(Login.KEY_PRIVATE, "NA") + " " + "wrote : ");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void backPage (View v) {
        startActivity(new Intent(ViewMaggiReview.this, OngoingActivity.class));
    }

    @Override
    public void errorOnServerConnect() {

        //
    }

    private void retrieveReview() {

        String product_name = "Maggi_Noodles";
        listener = this;

        try {
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("product_name", product_name);
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_REVIEWS_VIEW, listener, dataToSend.toString());
            connect.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
