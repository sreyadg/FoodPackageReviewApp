package com.lipssmackingparadise.reviewapp.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by User on 29/10/2014.
 */
public class ServerConnect extends AsyncTask<String, Integer, String> {

    private final String TAG = "ServerConnect";

    private final String MESSAGE_DATA_CONNECTING = "Communicating with server...";
    private final String MESSAGE_TRANSACTION_SUCCESS = "Done";
    private final String MESSAGE_TRANSACTION_FAIL = "Fail to connect to server";
    private final String MESSAGE_NO_INTERNET = "There is no network connection";

    private enum STATE {
        RETRIEVE, SEND, SUCCESS, FAIL, NO_INTERNET
    }

    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;

    private ServerConnectListener listener;

    private Context context;
    private STATE state;
    private String url;
    private String result;
    private String outputData;


    // Connect to server with no send data
    public ServerConnect(Context context, String url, ServerConnectListener listener) {
        this.context = context;
        this.url = url;
        state = STATE.RETRIEVE;
        this.listener = listener;
        initialiseHttpClient ();
    }
    // Connect to server with send data
    public ServerConnect(Context context, String url,
                         ServerConnectListener listener, String outputData) {
        this.context = context;
        this.url = url;
        state = STATE.SEND;
        this.listener = listener;
        this.outputData = outputData;
        initialiseHttpClient ();
    }

    public static boolean hasNetworkConnection (Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void determineNetworkConnection () {
        if (!hasNetworkConnection()) {
            state = STATE.NO_INTERNET;
        }
    }

    private void createToastMessage (String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private boolean hasNetworkConnection () {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initialiseHttpClient () {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        httpClient = new DefaultHttpClient(httpParams);
    }

    @Override
    protected void onPostExecute(String result) {
        determineAndEndProcessByState ();
    }

    @Override
    protected String doInBackground(String... params) {
        determineAndRunProcessByState ();
        return null;
    }

    @Override
    protected void onPreExecute() {
        determineNetworkConnection ();
        createToastMessage (determineToastMessage ());
    }

    private String determineToastMessage () {
        switch (state) {
            case RETRIEVE:
            case SEND:
                return MESSAGE_DATA_CONNECTING;
            case NO_INTERNET:
                return MESSAGE_NO_INTERNET;
            case FAIL:
                return MESSAGE_TRANSACTION_FAIL;
            case SUCCESS:
                return MESSAGE_TRANSACTION_SUCCESS;
            default:
                return null;
        }
    }

    private void determineAndRunProcessByState () {
        switch (state) {
            case RETRIEVE:
                retrieveData ();
                break;
            case SEND:
                sendingData ();
                break;
            default:
                break;
        }
    }

    private void determineAndEndProcessByState () {
        switch (state) {
            case RETRIEVE:
                state = STATE.SUCCESS;
                createToastMessage (determineToastMessage ());
                listener.retrieveCompleted(result);
                break;
            case SEND:
                state = STATE.SUCCESS;
                createToastMessage (determineToastMessage ());
                listener.sendCompleted(result);
                break;
            case NO_INTERNET:
            case FAIL:
                createToastMessage (determineToastMessage ());
                listener.errorOnServerConnect();
                break;
            default:
                break;
        }
    }

    private void retrieveData() {
        try {
            httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                BufferedInputStream inputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                }
            } else {
                state = STATE.FAIL;
            }
        } catch (Exception e) {
            state = STATE.FAIL;
            e.printStackTrace();
        }
    }

    private String convertInputStreamToString(BufferedInputStream inputStream) {
        BufferedReader bufferedReader =
                new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        StringBuilder result = new StringBuilder();
        try {
            while((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            state = STATE.FAIL;
        }
        return result.toString();
    }

    private void sendingData () {
        try {
            httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(outputData);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                state = STATE.FAIL;
            } else {
                BufferedInputStream inputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                }
            }
        } catch (Exception e) {
            state = STATE.FAIL;
            e.printStackTrace();
        }
    }


}
