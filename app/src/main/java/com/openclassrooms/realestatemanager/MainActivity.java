package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                this.addEstate();
                return true;
            case R.id.action_edit:
                /* DO EDIT */
                return true;
            case R.id.action_search:
                /* DO SEARCH */
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEstate(){
        Intent myIntent = new Intent(MainActivity.this, AddEstate.class);
        startActivity(myIntent);
    }
}
