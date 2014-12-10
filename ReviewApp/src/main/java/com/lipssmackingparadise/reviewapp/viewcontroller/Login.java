package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import java.util.Arrays;



public class Login extends Activity implements ServerConnectListener{

    //messages from the server
    private final String MESSAGE_ERROR_ON_EMPTY_LOGIN_DETAILS = "Please enter your username and password";
    private final String MESSAGE_ERROR_WRONG_LOGIN_DETAILS = "Incorrect username/password!";
    private final String MESSAGE_ERROR_UNKNOWN = "Unknown Error!";
    private final String MESSAGE_SUCCESS = "Success!";
    private final String MESSAGE_ERROR_SERVER = "An error occurred while connecting to the server!";
    private final String MESSAGE_FACEBOOK_SUCCESS = "You are now logged in!";

    private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;

    // EditTexts
    private AutoCompleteTextView login_username;
    private EditText login_password;

    //Activity variables
    private String username;
    private String password;

    // NETWORK
    private ServerConnectListener listener;

    // Dialog
    private AlertDialog message_dialog;

    //SharedPreferences Username
    private SharedPreferences userNameLoggedIn;
    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String KEY_PRIVATE = "KEY_PRIVATE";
    private String SharedPreferencesUsername;

    //SharedPreferences Password
    private SharedPreferences userPassword;
    public static final String PREFS_PRIVATE_PASSWORD = "PREFS_PRIVATE_PASSWORD";
    public static final String KEY_PRIVATE_PASSWORD = "KEY_PRIVATE_PASSWORD";
    private String SharedPreferencesPassword;

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
        // not used
    }

    private void intialiseSharedPreferences() {

        login_username = (AutoCompleteTextView) findViewById(R.id.username);
        SharedPreferencesUsername = login_username.getText().toString();
        userNameLoggedIn = getSharedPreferences(Login.PREFS_PRIVATE, Context.MODE_PRIVATE);
        Editor privateEdit = userNameLoggedIn.edit();
        privateEdit.putString(Login.KEY_PRIVATE, SharedPreferencesUsername);
        privateEdit.commit();

        login_password = (EditText) findViewById(R.id.password);
        SharedPreferencesPassword = login_password.getText().toString();
        userPassword = getSharedPreferences(Login.PREFS_PRIVATE_PASSWORD, Context.MODE_PRIVATE);
        Editor privateEditPassword = userPassword.edit();
        privateEditPassword.putString(Login.KEY_PRIVATE_PASSWORD, SharedPreferencesPassword);
        privateEditPassword.commit();

    }

    @Override
    public void sendCompleted(String result) {
        String serverResult = processMessage(result);

        if (serverResult.equals("Success")){
            intialiseSharedPreferences();
            startActivity(new Intent(Login.this, AuthenticateUser.class));
        }

        else if (serverResult.equals("Wrong")){
            setMessageDialogAndShow(MESSAGE_ERROR_WRONG_LOGIN_DETAILS, true);
        }

        else if (serverResult.equals("Error")){
            setMessageDialogAndShow(MESSAGE_ERROR_UNKNOWN, true);
        }

        else {
            setMessageDialogAndShow(MESSAGE_ERROR_UNKNOWN, true);
        }
    }

    @Override
    public void errorOnServerConnect() {
        setMessageDialogAndShow (MESSAGE_ERROR_SERVER, true);
    }

    public String processMessage(String message) {
        if (message.equals(MESSAGE_ERROR_WRONG_LOGIN_DETAILS)){
            return "Wrong";
        }
        else if (message.equals(MESSAGE_SUCCESS)){
            return "Success";
        }
        return "Error";
    }

    private void initialise() {

        // Initialise EditText
        login_username = (AutoCompleteTextView) findViewById(R.id.username);
        login_password = (EditText) findViewById(R.id.password);

        // Initialise Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        message_dialog = builder.create();

        listener = this;
    }

    private void setMessageDialogAndShow (String text, boolean cancelable) {
        if(message_dialog.isShowing()) {
            message_dialog.dismiss();
        }
        message_dialog.setMessage(text);
        message_dialog.setCancelable(cancelable);
        message_dialog.show();
    }

    public void Register_Page (View v) {
        startActivity(new Intent(Login.this, SignUp.class));
    }


    public void Sign_In (View v) {

        authenticateLoginByUsernameAndPassword ();

    }

    public void facebookLogin (View v) {

        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile"))
                    .setCallback(callback));
        } else {
            Session.openActiveSession(this, true, callback);
        }

    }

    private void authenticateLoginByUsernameAndPassword () {

        initialise();

        username = login_username.getText().toString();
        password = login_password.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {

            try {
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("username", username);
                dataToSend.put("password", password);
                ServerConnect connect = new ServerConnect(this, ServerUrl.getInstance().URL_LOGIN, listener, dataToSend.toString());
                connect.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setMessageDialogAndShow (MESSAGE_ERROR_ON_EMPTY_LOGIN_DETAILS, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}
