package com.openclassrooms.realestatemanager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class DetailFragment extends Fragment {
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private Estate estate;
    private TextView entryAndSoldDate;
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
    private int currentEstate = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        detailPhotosRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if(Utils.isTablet(requireContext()) && Utils.isLandscapeOrientation(requireContext())) {
            configureViewModel();
            getAllEstates();
        }
        return binding.getRoot();
    }

    private void setAdapter(Estate estate){
        if(estate.getPhotosListString() != null) {
            ArrayList<String> photos = Utils.fromStringListToArrayList(estate.getPhotosListString());
            detailPhotosRV.setAdapter(new PhotosRVAdapter(photos, null, estate.getIsSold()));
        }
    }

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
        this.estateViewModel.init();
    }

    private void getAllEstates(){
        assert this.estateViewModel.getAllEstates() != null;
        this.estateViewModel.getAllEstates().observe(getViewLifecycleOwner(), this::updateEstatesList);
    }

    private void updateEstatesList(List<Estate> estates) {
        this.estates.clear();
        this.estates.addAll(estates);
        setCurrentEstate(currentEstate);
    }

    public void setCurrentEstate(int position) {
        currentEstate = position;
        if(estates.size() > 0) {
            estate = estates.get(position);
            setAdapter(estate);
            String entryDate = estate.getEntryDateStr();
            String soldDate = estate.getSoldDateStr();
            entryAndSoldDate.setText(soldDate == null ? getString(R.string.entered_on) + entryDate : getString(R.string.sold_on) + soldDate);
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
                Bitmap map = Utils.getBitmapFromFileName(estate.getStaticMapFileName(), 500, requireContext());
                staticMap.setImageBitmap(map);
            }
        }
    }

    private void setPositionFromAddress(String address){
        if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(requireContext()))) {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(' ', '+') + "&key=" + BuildConfig.MAPS_API_KEY;
            //execute query
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String urlRequestResult = Utils.urlRequest(url);
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
            });
        } else {
            Toast.makeText(getContext(), R.string.no_def_gps , Toast.LENGTH_LONG).show();
        }
    }

    private void setPointsOfInterest(String lat, String lng) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String interests = Utils.getPointsOfInterest(lat, lng, getContext());
            estate.setPointsOfInterest(interests);
            estateViewModel.updateEstate(estate);
        });
    }
}
