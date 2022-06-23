package com.openclassrooms.realestatemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.FragmentEstateRvBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MasterRVFragment extends Fragment {
    private RecyclerView rv;
    @NonNull
    protected final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private int bindPosition;
    private ActivityResultLauncher<Intent> mStartDetailForResult;
    protected boolean isFiltered;
    protected ArrayList<String> estatesIdSavedByInstanceState;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerDetailActivityForResult();
        if(savedInstanceState != null) {
            String listEstatesIdSavedByInstanceState = savedInstanceState.getString("stringListEstatesId");
            estatesIdSavedByInstanceState = Utils.fromStringListToArrayList(listEstatesIdSavedByInstanceState);
        }
        FragmentEstateRvBinding binding = FragmentEstateRvBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        rv = binding.estateRv;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        configureViewModel();
        getAllEstates();
        if(savedInstanceState != null) {
            isFiltered = savedInstanceState.getBoolean("isFiltered");
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> estatesId = new ArrayList<>();
        for(int i = 0; i < estates.size(); i++) {
            estatesId.add(String.valueOf(estates.get(i).getId()));
        }
        String stringListEstatesId = Utils.fromArrayListStringToStringList(estatesId);
        outState.putString("stringListEstatesId", stringListEstatesId);
        outState.putBoolean("isFiltered", isFiltered);
    }

    private void setAdapter(){
        rv.setAdapter(new MasterRVAdapter(estates, this));
    }

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getContext());
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
        this.estateViewModel.init();
    }

    protected void getAllEstates(){
        isFiltered = false;
        assert this.estateViewModel.getAllEstates() != null;
        this.estateViewModel.getAllEstates().observe(getViewLifecycleOwner(), this::updateEstatesList);
    }

    protected void getFilteredEstates(String estateAgent, String estateType, int minPrice, int maxPrice, int minSurface, int maxSurface, int minRooms, int maxRooms, int minBedrooms, int maxBedrooms, int minBathrooms, int maxBathrooms, int soldReq1, int soldReq2, long since) {
        isFiltered = true;
        this.estateViewModel.getFilteredEstates(estateAgent, estateType, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms, minBedrooms, maxBedrooms, minBathrooms, maxBathrooms, soldReq1, soldReq2, since).observe(getViewLifecycleOwner(), this::updateEstatesList);
    }

    private void updateEstatesList(List<Estate> estates) {
        if(estates.size() == 0 && !isFiltered) {
            addEstate();
        } else if(estates.size() == 0) {
            isFiltered = false;
            requireActivity().invalidateOptionsMenu();
            Toast.makeText(getContext(), R.string.no_result , Toast.LENGTH_LONG).show();
        } else {
            this.estates.clear();
            if (estatesIdSavedByInstanceState != null) {
                //this is filter to show only needed estates
                for (int i = 0; i < estates.size(); i++) {
                    if (estatesIdSavedByInstanceState.contains(String.valueOf(estates.get(i).getId()))) {
                        this.estates.add(estates.get(i));
                    }
                }
            } else {
                this.estates.addAll(estates);
            }
            if (Utils.isTablet(requireContext()) && Utils.isLandscapeOrientation(requireContext())) {
                setCurrentDetailView((int) this.estates.get(0).getId() - 1);
            } else {
                setAdapter();
            }
        }
    }

    private void addEstate(){
        Intent myIntent = new Intent(getActivity(), EditEstateActivity.class);
        startActivity(myIntent);
    }

    private void registerDetailActivityForResult() {
        mStartDetailForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        int detail = Objects.requireNonNull(intent).getIntExtra("detailId", 0);
                        this.setCurrentDetailView(detail);
                    }
                });
    }

    protected void setCurrentDetailView(int bindPosition){
        DetailFragment detailFragment = (DetailFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentDetail);
        this.bindPosition = bindPosition;
        if(Utils.isTablet(requireContext()) && Utils.isLandscapeOrientation(requireContext())){
            assert detailFragment != null;
            detailFragment.setCurrentEstate(bindPosition);
        } else {
            Intent myIntent = new Intent(requireActivity(), DetailActivity.class);
            myIntent.putExtra("position", bindPosition);
            mStartDetailForResult.launch(myIntent);
        }
        setAdapter();
    }

    public int getBindPosition() {
        return bindPosition;
    }
}
