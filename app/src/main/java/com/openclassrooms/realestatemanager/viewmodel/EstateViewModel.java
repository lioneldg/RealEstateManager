package com.openclassrooms.realestatemanager.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class EstateViewModel extends ViewModel {

    // REPOSITORY
    private final EstateDataRepository estateDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<List<Estate>> allEstates;

    public EstateViewModel(EstateDataRepository estateDataSource, Executor executor) {
        this.estateDataSource = estateDataSource;
        this.executor = executor;
    }

    public void init() {
        if (this.allEstates != null) {
            return;
        }
        allEstates = estateDataSource.getAllEstates();
    }


    @Nullable
    public LiveData<List<Estate>> getAllEstates() {
        return this.allEstates;
    }

    public LiveData<List<Estate>> getFilteredEstates(int isSold) {
        return estateDataSource.getFilteredEstates(isSold);
    }

    public void createEstate(Estate estate) {
        executor.execute(() -> estateDataSource.createEstate(estate));
    }

    public void updateEstate(Estate estate) {
        executor.execute(()-> estateDataSource.updateEstate(estate));
    }
}
