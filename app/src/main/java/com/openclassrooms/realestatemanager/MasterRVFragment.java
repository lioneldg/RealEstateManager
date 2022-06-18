package com.openclassrooms.realestatemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final ArrayList<Estate> estates = new ArrayList<>();
    private EstateViewModel estateViewModel;
    private int bindPosition;
    ActivityResultLauncher<Intent> mStartDetailForResult;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerDetailActivityForResult();
        FragmentEstateRvBinding binding = FragmentEstateRvBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        rv = binding.estateRv;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        configureViewModel();
        getAllEstates();
        return view;
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

    private void getAllEstates(){
        assert this.estateViewModel.getAllEstates() != null;
        this.estateViewModel.getAllEstates().observe(getViewLifecycleOwner(), this::updateEstatesList);
    }

    private void updateEstatesList(List<Estate> estates) {
        if(estates.size() == 0) {
            addEstate();
        }
        this.estates.clear();
        this.estates.addAll(estates);
        setAdapter();
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
        this.bindPosition = bindPosition;
        DetailFragment detailFragment = (DetailFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentDetail);
        if(detailFragment != null && detailFragment.isVisible()){
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
