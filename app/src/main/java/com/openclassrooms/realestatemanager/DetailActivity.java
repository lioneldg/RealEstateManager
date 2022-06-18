package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private Estate estate;
    private TextView entryAndSoldDate;
    private EstateViewModel estateViewModel;
    private TextView detailDescription;
    private TextView detailPointsOfInterestTitle;
    private TextView detailPointsOfInterest;
    private TextView detailSurface;
    private TextView detailNumberOfRooms;
    private TextView detailNumberOfBathrooms;
    private TextView detailNumberOfBedrooms;
    private TextView detailLocation;
    private RecyclerView detailPhotosRV;
    private ImageView staticMap;
    private int position;
    ActivityResultLauncher<Intent> mStartForResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerMapActivityForResult();
        position = getIntent().getIntExtra("position", 0);
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(getLayoutInflater());
        entryAndSoldDate = binding.entryAndSoldDate;
        detailPointsOfInterestTitle = binding.detailPointsOfInterestTitle;
        detailPointsOfInterest = binding.detailPointsOfInterest;
        detailDescription = binding.detailDescription;
        detailSurface = binding.detailSurface;
        detailNumberOfRooms = binding.detailNumberOfRooms;
        detailNumberOfBathrooms = binding.detailNumberOfBathrooms;
        detailNumberOfBedrooms = binding.detailNumberOfBedrooms;
        detailLocation = binding.detailLocation;
        detailPhotosRV = binding.detailPhotosRV;
        staticMap = binding.staticMap;
        detailPhotosRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setContentView(binding.getRoot());
        configureViewModel();
        getAllEstates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void setAdapter(Estate estate){
        if(estate.getPhotosListString() != null) {
            ArrayList<String> photos = Utils.fromStringListToArrayList(estate.getPhotosListString());
            detailPhotosRV.setAdapter(new PhotosRVAdapter(photos, null, estate.getIsSold()));
        }
    }

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
        this.estateViewModel.init();
    }

    private void getAllEstates(){
        assert this.estateViewModel.getAllEstates() != null;
        this.estateViewModel.getAllEstates().observe(this, this::updateEstatesList);
    }

    private void updateEstatesList(List<Estate> estates) {
        this.estates.clear();
        this.estates.addAll(estates);
        setCurrentEstate(position);
    }

    public void setCurrentEstate(int position) {
        estate = estates.get(position);
        setAdapter(estate);
        String entryDate = Utils.getFormattedDate(new Date(estate.getEntryDate()));
        String soldDate = Utils.getFormattedDate(new Date(estate.getSoldDate()));
        entryAndSoldDate.setText(estate.getSoldDate() > 0 ? getString(R.string.sold_on) + soldDate : getString(R.string.entered_on) + entryDate);
        if(estate.getLat() == null || estate.getLng() == null){
            setPositionFromAddress(estate.getEstateAddress());
        }
        if(estate.getPointsOfInterest() != null) {
            detailPointsOfInterestTitle.setVisibility(View.VISIBLE);
            detailPointsOfInterest.setText(estate.getPointsOfInterest());
        } else {
            detailPointsOfInterestTitle.setVisibility(View.GONE);
        }
        detailDescription.setText(estate.getEstateFullDescription());
        detailSurface.setText(String.valueOf(estate.getEstateSurface()));
        detailNumberOfRooms.setText(String.valueOf(estate.getEstateNumberOfRooms()));
        detailNumberOfBathrooms.setText(String.valueOf(estate.getEstateNbrOfBathrooms()));
        detailNumberOfBedrooms.setText(String.valueOf(estate.getEstateNbrOfBedrooms()));
        detailLocation.setText(estate.getEstateAddress());
        if(estate.getStaticMapFileName() != null) {
            Bitmap map = Utils.getBitmapFromFileName(estate.getStaticMapFileName(), 500, this);
            staticMap.setImageBitmap(map);
        }
    }

    private void registerMapActivityForResult() {
        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        int detail = Objects.requireNonNull(intent).getIntExtra("detailId", 0);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        menu.removeItem(R.id.action_search);
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
            case R.id.action_map:
                this.openMapActivity();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEstate(){
        finish();
        Intent myIntent = new Intent(DetailActivity.this, EditEstateActivity.class);
        startActivity(myIntent);
    }

    private void openMapActivity() {
        if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(this))) {
            Intent myIntent = new Intent(DetailActivity.this, MapsActivity.class);
            mStartForResult.launch(myIntent);
        } else {
            Toast.makeText(this, R.string.no_internet_connection , Toast.LENGTH_LONG).show();
        }
    }

    private void editEstate(){
        Intent myIntent = new Intent(DetailActivity.this, EditEstateActivity.class);
        myIntent.putExtra("position", position);
        startActivity(myIntent);
    }

    private void setPositionFromAddress(String address){
        if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(this))) {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(' ', '+') + "&key=" + BuildConfig.MAPS_API_KEY;
            //execute query
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String urlRequestResult = Utils.urlRequest(url);
                try {
                    try {
                        JSONObject resultObject = new JSONObject(urlRequestResult);
                        JSONArray results = resultObject.getJSONArray("results");
                        JSONObject resultBody = results.getJSONObject(0);
                        JSONObject geometry = resultBody.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        String lat = location.optString("lat");
                        String lng = location.optString("lng");
                        estate.setLat(lat);
                        estate.setLng(lng);
                        setPointsOfInterest(lat, lng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, R.string.no_def_gps , Toast.LENGTH_LONG).show();
        }
    }

    private void setPointsOfInterest(String lat, String lng) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String interests = Utils.getPointsOfInterest(lat, lng, this);
            estate.setPointsOfInterest(interests);
            estateViewModel.updateEstate(estate);
        });
    }
}
