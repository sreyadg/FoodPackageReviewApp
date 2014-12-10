package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
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
import android.widget.RatingBar;

import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaggiReview2 extends Activity implements ServerConnectListener {


    private RatingBar smell;
    private String product_name;

    private String smell_rating;

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
        setContentView(R.layout.activity_maggi_review2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maggi_review2, menu);
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

        System.out.println(result);

        if (result.equals(MESSAGE_ADD_REVIEW_ERROR)){
            setMessageDialogAndShow(MESSAGE_ADD_REVIEW_ERROR, true);
        }

        else if (result.equals(MESSAGE_ADD_REVIEW_SUCCESS)){
            share();
        }

        else {
            setMessageDialogAndShow(MESSAGE_ERROR_UNKNOWN, true);
        }

    }

    public void share()

    {
        final Button btnOpenPopup = (Button)findViewById(R.id.add_review_button);
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(btnOpenPopup,10, 50, 30);
    }

    public void redirect (View v) {
        startActivity(new Intent(MaggiReview2.this, OngoingActivity.class));
    }

    public void redirectFacebook (View v) {
        startActivity(new Intent(MaggiReview2.this, RedirectFacebook.class));
    }


    @Override
    public void errorOnServerConnect() {
        //
    }

    public void addReview2 (View v) {
        String dataToSend = null;
        try{
            dataToSend = jsonifyUserData();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(dataToSend != null) {
            ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_REVIEWS_CREATE_SECOND, listener, dataToSend);
            connect.execute();
        }
    }

    private void initialise(){

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MaggiReview2.this);
        message_dialog = builder.create();

        listener = this;
    }

    private String jsonifyUserData() throws JSONException {

        initialise();

        smell = (RatingBar) findViewById(R.id.smell);
        smell_rating = String.valueOf(smell.getRating());

        final EditText commentsField = (EditText) findViewById(R.id.reviewComments);
        String comments = commentsField.getText().toString();

        product_name = "Maggi_Noodles";

        JSONObject dataToSend = new JSONObject();
        dataToSend.put("product_name", product_name);
        dataToSend.put("smell", smell_rating);
        dataToSend.put("comments", comments);
        return dataToSend.toString();
    }
}
