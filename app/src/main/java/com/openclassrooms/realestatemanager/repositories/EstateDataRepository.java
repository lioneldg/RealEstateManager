package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import com.openclassrooms.realestatemanager.database.EstateDao;
import com.openclassrooms.realestatemanager.model.Estate;
import java.util.List;

public class EstateDataRepository {
    private final EstateDao estateDao;

    public EstateDataRepository(EstateDao estateDao){
        this.estateDao = estateDao;
    }

    // READ
    public LiveData<List<Estate>> getAllEstates(){
        return this.estateDao.getAllEstates();
    }

    // CREATE
    public void createEstate(Estate estate) {
        estateDao.addEstate(estate);
    }

}
