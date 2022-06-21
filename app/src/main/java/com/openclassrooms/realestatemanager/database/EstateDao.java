package com.openclassrooms.realestatemanager.database;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

@Dao
public interface EstateDao {
    // READ
    @Query("SELECT * FROM estate")
    LiveData<List<Estate>> getAllEstates();

    @Query("SELECT * FROM estate")
    Cursor getAllEstatesWithCursor();

    @Query("SELECT * FROM estate WHERE " +
            "(estateAgent LIKE :estateAgent) " +
            "AND (estateType LIKE :estateType) " +
            "AND (estatePrice BETWEEN :minPrice AND :maxPrice) " +
            "AND (estateSurface BETWEEN :minSurface AND :maxSurface) " +
            "AND (estateNumberOfRooms BETWEEN :minRooms AND :maxRooms) " +
            "AND (estateNbrOfBedrooms BETWEEN :minBedrooms AND :maxBedrooms) " +
            "AND (estateNbrOfBathrooms BETWEEN :minBathrooms AND :maxBathrooms) " +
            "AND (isSold BETWEEN :soldReq1 AND :soldReq2) " +
            "AND (entryDate > :since OR soldDate > :since)")
    LiveData<List<Estate>> getFilteredEstates(String estateAgent, String estateType, int minPrice, int maxPrice, int minSurface, int maxSurface, int minRooms, int maxRooms, int minBedrooms, int maxBedrooms, int minBathrooms, int maxBathrooms, int soldReq1, int soldReq2, long since);

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEstate(Estate estate);

    //UPDATE
    @Update
    void updateEstate(Estate estate);

}
