package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.addEstate();
    }

    private void addEstate(){
        Intent myIntent = new Intent(MainActivity.this, AddEstate.class);
        startActivity(myIntent);
    }
}
