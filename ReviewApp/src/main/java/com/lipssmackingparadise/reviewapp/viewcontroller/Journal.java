package com.lipssmackingparadise.reviewapp.viewcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lipssmackingparadise.reviewapp.R;

public class Journal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        setComments();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.journal, menu);
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

    public void setComments(){

        RatingBar get_taste = (RatingBar) findViewById(R.id.ratingBar);
        RatingBar get_packaging = (RatingBar) findViewById(R.id.ratingBar2);
        RatingBar get_smell = (RatingBar) findViewById(R.id.ratingBar3);

        // Set values form sharedPreferences to the textViews
        get_taste.setRating(4);
        get_packaging.setRating(3);
        get_smell.setRating(4);

        RatingBar get_taste2 = (RatingBar) findViewById(R.id.ratingBar4);
        RatingBar get_packaging2 = (RatingBar) findViewById(R.id.ratingBar5);
        RatingBar get_smell2 = (RatingBar) findViewById(R.id.ratingBar6);

        // Set values form sharedPreferences to the textViews
        get_taste2.setRating(5);
        get_packaging2.setRating(4);
        get_smell2.setRating(4);
    }
}
