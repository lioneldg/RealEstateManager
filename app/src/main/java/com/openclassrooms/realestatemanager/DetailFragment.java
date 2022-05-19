package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private TextView detailDescription;
    private TextView detailSurface;
    private TextView detailNumberOfRooms;
    private TextView detailNumberOfBathrooms;
    private TextView detailNumberOfBedrooms;
    private TextView detailLocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(getLayoutInflater());
        detailDescription = binding.detailDescription;
        detailSurface = binding.detailSurface;
        detailNumberOfRooms = binding.detailNumberOfRooms;
        detailNumberOfBathrooms = binding.detailNumberOfBathrooms;
        detailNumberOfBedrooms = binding.detailNumberOfBedrooms;
        detailLocation = binding.detailLocation;
        if(Utils.isTablet(requireContext()) && Utils.isLandscapeOrientation(requireContext())) {
            configureViewModel();
            getAllEstates();
        }
        return binding.getRoot();
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
        boolean isInitialisation = this.estates.isEmpty();
        this.estates.clear();
        this.estates.addAll(estates);
        if(isInitialisation) {
            setCurrentEstate(0);
        } else {
            setCurrentEstate(estates.size() - 1);
        }
    }

    public void setCurrentEstate(int position) {
        Estate estate = estates.get(position);
        detailDescription.setText(estate.getEstateFullDescription());
        detailSurface.setText(String.valueOf(estate.getEstateSurface()));
        detailNumberOfRooms.setText(String.valueOf(estate.getEstateNumberOfRooms()));
        detailNumberOfBathrooms.setText(String.valueOf(estate.getEstateNbrOfBathrooms()));
        detailNumberOfBedrooms.setText(String.valueOf(estate.getEstateNbrOfBedrooms()));
        detailLocation.setText(estate.getEstateAddress());
    }
}
