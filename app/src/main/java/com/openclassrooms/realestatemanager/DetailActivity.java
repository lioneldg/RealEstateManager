package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private TextView detailDescription;
    private TextView detailSurface;
    private TextView detailNumberOfRooms;
    private TextView detailNumberOfBathrooms;
    private TextView detailNumberOfBedrooms;
    private TextView detailLocation;
    private RecyclerView detailPhotosRV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(getLayoutInflater());
        detailDescription = binding.detailDescription;
        detailSurface = binding.detailSurface;
        detailNumberOfRooms = binding.detailNumberOfRooms;
        detailNumberOfBathrooms = binding.detailNumberOfBathrooms;
        detailNumberOfBedrooms = binding.detailNumberOfBedrooms;
        detailLocation = binding.detailLocation;
        detailPhotosRV = binding.detailPhotosRV;
        detailPhotosRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setContentView(binding.getRoot());
        configureViewModel();
        getAllEstates();
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
        setCurrentEstate(getIntent().getIntExtra("position", 0));
    }

    public void setCurrentEstate(int position) {
        Estate estate = estates.get(position);
        setAdapter(estate);
        detailDescription.setText(estate.getEstateFullDescription());
        detailSurface.setText(String.valueOf(estate.getEstateSurface()));
        detailNumberOfRooms.setText(String.valueOf(estate.getEstateNumberOfRooms()));
        detailNumberOfBathrooms.setText(String.valueOf(estate.getEstateNbrOfBathrooms()));
        detailNumberOfBedrooms.setText(String.valueOf(estate.getEstateNbrOfBedrooms()));
        detailLocation.setText(estate.getEstateAddress());
    }
}
