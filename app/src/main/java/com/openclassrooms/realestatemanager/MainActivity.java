package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.model.Estate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> mStartMapsForResult;
    private ActivityResultLauncher<Intent> mStartFilterForResult;
    MasterRVFragment masterRVFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerMapActivityForResult();
        this.registerFilterActivityForResult();
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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

    private void registerMapActivityForResult() {
        mStartMapsForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        int detail = Objects.requireNonNull(intent).getIntExtra("detailId", 0);
                        MasterRVFragment masterRVFragment = (MasterRVFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMaster);
                        assert masterRVFragment != null;
                        masterRVFragment.setCurrentDetailView(detail);
                    }
                });
    }

    private void registerFilterActivityForResult() {
        mStartFilterForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        masterRVFragment = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster)));
                        Intent intent = result.getData();
                        String estateAgent = Objects.requireNonNull(intent).getStringExtra("estateAgentString");
                        String estateType =  Objects.requireNonNull(intent).getStringExtra("estateTypeString");
                        String minPriceString =  Objects.requireNonNull(intent).getStringExtra("minPriceString");
                        int minPrice;
                        String maxPriceString =  Objects.requireNonNull(intent).getStringExtra("maxPriceString");
                        int maxPrice;
                        String minSurfaceString =  Objects.requireNonNull(intent).getStringExtra("minSurfaceString");
                        int minSurface;
                        String maxSurfaceString =  Objects.requireNonNull(intent).getStringExtra("maxSurfaceString");
                        int maxSurface;
                        String minRoomsString =  Objects.requireNonNull(intent).getStringExtra("minRoomsString");
                        int minRooms;
                        String maxRoomsString =  Objects.requireNonNull(intent).getStringExtra("maxRoomsString");
                        int maxRooms;
                        String minBedroomsString =  Objects.requireNonNull(intent).getStringExtra("minBedroomsString");
                        int minBedrooms;
                        String maxBedroomsString =  Objects.requireNonNull(intent).getStringExtra("maxBedroomsString");
                        int maxBedrooms;
                        String minBathroomsString =  Objects.requireNonNull(intent).getStringExtra("minBathroomsString");
                        int minBathrooms;
                        String maxBathroomsString =  Objects.requireNonNull(intent).getStringExtra("maxBathroomsString");
                        int maxBathrooms;
                        String soldType = Objects.requireNonNull(intent).getStringExtra("soldType");
                        int soldReq1, soldReq2;
                        String sinceString = Objects.requireNonNull(intent).getStringExtra("since");
                        long since;

                        if(estateAgent.equals("")) {
                            estateAgent = "%";
                        }

                        if(estateType.equals("")) {
                            estateType = "%";
                        }

                        if(minPriceString.equals("")) {
                            minPrice = 0;
                        } else {
                            minPrice = Integer.parseInt(minPriceString);
                        }

                        if(maxPriceString.equals("")) {
                            maxPrice = 999999999;
                        } else {
                            maxPrice = Integer.parseInt(maxPriceString);
                        }

                        if(minSurfaceString.equals("")) {
                            minSurface = 0;
                        } else {
                            minSurface = Integer.parseInt(minSurfaceString);
                        }

                        if(maxSurfaceString.equals("")) {
                            maxSurface = 999999999;
                        } else {
                            maxSurface = Integer.parseInt(maxSurfaceString);
                        }

                        if(minRoomsString.equals("")) {
                            minRooms = 0;
                        } else {
                            minRooms = Integer.parseInt(minRoomsString);
                        }

                        if(maxRoomsString.equals("")) {
                            maxRooms = 999999999;
                        } else {
                            maxRooms = Integer.parseInt(maxRoomsString);
                        }

                        if(minBedroomsString.equals("")) {
                            minBedrooms = 0;
                        } else {
                            minBedrooms = Integer.parseInt(minBedroomsString);
                        }

                        if(maxBedroomsString.equals("")) {
                            maxBedrooms = 999999999;
                        } else {
                            maxBedrooms = Integer.parseInt(maxBedroomsString);
                        }

                        if(minBathroomsString.equals("")) {
                            minBathrooms = 0;
                        } else {
                            minBathrooms = Integer.parseInt(minBathroomsString);
                        }

                        if(maxBathroomsString.equals("")) {
                            maxBathrooms = 999999999;
                        } else {
                            maxBathrooms = Integer.parseInt(maxBathroomsString);
                        }

                        if(soldType.equals("all")) {
                            soldReq1 = 0;
                            soldReq2 = 1;
                        } else if(soldType.equals("soldOnly")) {
                            soldReq1 = 1;
                            soldReq2 = 1;
                        } else {
                            soldReq1 = 0;
                            soldReq2 = 0;
                        }

                        if(sinceString.equals("")) {
                            since = 0;
                        } else {
                            since = Utils.getTimestampXMonthsAgo(new Date(),Integer.parseInt(sinceString));
                        }

                        masterRVFragment.getFilteredEstates(estateAgent, estateType, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms, minBedrooms, maxBedrooms, minBathrooms, maxBathrooms, soldReq1, soldReq2, since);
                        invalidateOptionsMenu();
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        if(!Utils.isTablet(this) || (Utils.isTablet(this) && !Utils.isLandscapeOrientation(this))){
            menu.removeItem(R.id.action_edit);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        masterRVFragment = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster)));
        MenuItem filterItem = menu.findItem(R.id.action_search);
        filterItem.setIcon(ContextCompat.getDrawable(this, masterRVFragment.isFiltered ? R.drawable.ic_baseline_search_off_24 : R.drawable.ic_baseline_search_24));
        return super.onPrepareOptionsMenu(menu);
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
                masterRVFragment = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster)));
                if(masterRVFragment.isFiltered) {
                    masterRVFragment.estatesIdSavedByInstanceState = null;
                    masterRVFragment.getAllEstates();
                    invalidateOptionsMenu();
                } else {
                    Intent myIntent = new Intent(MainActivity.this, FilterActivity.class);
                    mStartFilterForResult.launch(myIntent);
                }
                return true;
            case R.id.action_map:
                this.openMapActivity();
                return  true;
            case R.id.action_finance:
                this.openFinanceSimulator();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEstate(){
        Intent myIntent = new Intent(MainActivity.this, EditEstateActivity.class);
        startActivity(myIntent);
    }

    private void openMapActivity() {
        if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(this))) {
            masterRVFragment = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster)));
            ArrayList<Estate> estates = masterRVFragment.estates;
            ArrayList<String> estatesId = new ArrayList<>();
            for(int i = 0; i < estates.size(); i++) {
                estatesId.add(String.valueOf(estates.get(i).getId()));
            }
            String stringListEstatesId = Utils.fromArrayListStringToStringList(estatesId);
            Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
            myIntent.putExtra("stringListEstatesId", stringListEstatesId);
            mStartMapsForResult.launch(myIntent);
        } else {
            Toast.makeText(this, R.string.no_internet_connection , Toast.LENGTH_LONG).show();
        }
    }

    private void openFinanceSimulator() {
            Intent myIntent = new Intent(MainActivity.this, FinanceActivity.class);
            startActivity(myIntent);
    }

    private void editEstate(){
        int bindPosition = ((MasterRVFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentMaster))).getBindPosition();
        Intent myIntent = new Intent(MainActivity.this, EditEstateActivity.class);
        myIntent.putExtra("position", bindPosition);
        startActivity(myIntent);
    }
}
