package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EstateRVFragment extends Fragment {
    private RecyclerView rv;
    @NonNull
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.openclassrooms.realestatemanager.databinding.FragmentEstateRvBinding binding = FragmentEstateRvBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        rv = binding.estateRv;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        configureViewModel();
        getAllEstates();
        return view;
    }

    private void setAdapter(){
        rv.setAdapter(new EstateRVAdapter(estates, this));
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
        setAdapter();
    }

    protected void setCurrentDetailView(int bindPosition){
        DetailFragment detailFragment = (DetailFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentDetail);
        if(detailFragment != null && detailFragment.isVisible()){
            detailFragment.setCurrentEstate(bindPosition);
        } else {
            Intent myIntent = new Intent(requireActivity(), DetailActivity.class);
            myIntent.putExtra("position", bindPosition);
            startActivity(myIntent);
        }
    }
}
