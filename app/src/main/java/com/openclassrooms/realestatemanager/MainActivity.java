package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //Afficher date d'entrée , date de vente si vendu !!!!!!! jeudi 7 juin
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
