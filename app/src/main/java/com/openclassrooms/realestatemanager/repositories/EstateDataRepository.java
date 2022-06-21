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
    public LiveData<List<Estate>> getFilteredEstates(String estateAgent, String estateType, int minPrice, int maxPrice, int minSurface, int maxSurface, int minRooms, int maxRooms, int minBedrooms, int maxBedrooms, int minBathrooms, int maxBathrooms, int soldReq1, int soldReq2, long since) {
        return this.estateDao.getFilteredEstates(estateAgent, estateType, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms, minBedrooms, maxBedrooms, minBathrooms, maxBathrooms, soldReq1, soldReq2, since);
    }

    // CREATE
    public void createEstate(Estate estate) {
        estateDao.addEstate(estate);
    }

    //UPDATE
    public void updateEstate(Estate estate) {estateDao.updateEstate(estate);}

}
