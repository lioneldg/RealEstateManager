package com.openclassrooms.realestatemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityMapsBinding binding;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private Boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private int selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationPermissionGranted = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedMarker = -1;
        configureViewModel();
        getAllEstates();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            mapFragment.getMapAsync(this);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            locationPermissionGranted = true;
            mapFragment.getMapAsync(this);
        }
    });

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(marker -> {
            if(selectedMarker == Integer.parseInt(String.valueOf(marker.getTag()))) {
                int detailId = Integer.parseInt((String) Objects.requireNonNull(marker.getTag())) - 1;
                Intent respIntent = new Intent();
                respIntent.putExtra("detailId", detailId);
                setResult(Activity.RESULT_OK, respIntent);
                finish();
            } else {
                selectedMarker = Integer.parseInt(String.valueOf(marker.getTag()));
            }
            return false;
        });
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (locationPermissionGranted) {
                getDeviceLocation();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    private void getDeviceLocation() {
        try {
            if (mMap != null && locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            getDeviceLocation();
                        }
                    } else {
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                    String stringListEstatesId = getIntent().getStringExtra("stringListEstatesId");
                    ArrayList<String> arrayListEstateId = Utils.fromStringListToArrayList(stringListEstatesId);
                    //this is filter to show only needed estates
                    for(int i = 0; i < estates.size(); i++) {
                        if(arrayListEstateId.contains(String.valueOf(estates.get(i).getId()))){
                            LatLng position = new LatLng(Double.parseDouble(estates.get(i).getLat()), Double.parseDouble(estates.get(i).getLng()));
                            addMarkerOption(position, estates.get(i).getEstateType() + ' ' + estates.get(i).getEstatePrice() + '$', String.valueOf(estates.get(i).getId()));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    protected void addMarkerOption(LatLng position, String title, String detailId){
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .icon(Utils.vectorToBitmap(getResources(), R.drawable.ic_baseline_home_24)));
        assert marker != null;
        marker.setTag(detailId);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(binding.getRoot().getId(), mapFragment).commit();
        getLocationPermission();
    }
}

      