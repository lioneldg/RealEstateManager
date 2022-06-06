package com.openclassrooms.realestatemanager;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.model.PositionLatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //debuger le contentProvider!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //tests contentProvider !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! lundi 6 juin
    //points d'interets , date d'entré , date de vente , geolocalisation api google!!!!!!! jeudi 7 juin
    //map image de chaque bien !!!!!!!!! jeudi 7 juin
    //Map des biens jeudi 16 juin
    //recherche d'estates!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! jeudi 23 juin
    //simulateur de crédit!!!!!!!!!!!!!!!!!!! jeudi 30 juin
    //tests unitaires!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.openclassrooms.realestatemanager.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetail);
        assert detailFragment != null;
        View detailFragmentView = detailFragment.getView();
        if(Utils.isTablet(this) && Utils.isLandscapeOrientation(this)){
            assert detailFragmentView != null;
            detailFragmentView.setVisibility(View.VISIBLE);
        } else {
            assert detailFragmentView != null;
            detailFragmentView.setVisibility(View.GONE);
        }
        //PositionLatLng pos = Utils.addressToPositionExecutor("1600AmphitheatreParkway,MountainView,CA", this);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Log.d(TAG, "onCreate: "+ pos);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        if(!Utils.isTablet(this) || (Utils.isTablet(this) && !Utils.isLandscapeOrientation(this))){
            menu.removeItem(R.id.action_edit);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                this.addEstate();
                return true;
            case R.id.action_edit:
                this.editEstate();
                return true;
            case R.id.action_search:
                /* DO SEARCH */
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEstate(){
        Intent myIntent = new Intent(MainActivity.this, EditEstateActivity.class);
        startActivity(myIntent);
    }

    private void editEstate(){
        int bindPosition = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster))).getBindPosition();
        Intent myIntent = new Intent(MainActivity.this, EditEstateActivity.class);
        myIntent.putExtra("position", bindPosition);
        startActivity(myIntent);
    }
}
