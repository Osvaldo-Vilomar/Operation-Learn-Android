package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void popularMoviesClick(View view) {

        Intent intent = new Intent(this, Movies.class);
        startActivity(intent);

    }

    public void stockHawkClick(View view) {

        Context context = getApplicationContext();
        CharSequence text = "This button will launch my Stock Hawk App!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void buildItBiggerClick(View view) {

        Context context = getApplicationContext();
        CharSequence text = "This button will launch my Build It Bigger App!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void makeYourAppMaterialClick(View view) {

        Context context = getApplicationContext();
        CharSequence text = "This button will launch my Make Your App Material App!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void goUbiquitousClick(View view) {

        Context context = getApplicationContext();
        CharSequence text = "This button will launch my Go Ubiquitous App!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void capstoneClick(View view) {

        Context context = getApplicationContext();
        CharSequence text = "This button will launch my Capstone App!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
