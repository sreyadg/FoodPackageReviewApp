package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.lipssmackingparadise.reviewapp.R;
import com.lipssmackingparadise.reviewapp.server.ServerConnectListener;
import com.lipssmackingparadise.reviewapp.server.ServerConnect;
import com.lipssmackingparadise.reviewapp.server.ServerUrl;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class NestleCerealReview extends FragmentActivity implements ServerConnectListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestle_cereal_review);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nestle_cereal_review, menu);
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

    @Override
    public void sendCompleted(String result) {
       //
    }

    @Override
    public void errorOnServerConnect() {
        //
    }

}
