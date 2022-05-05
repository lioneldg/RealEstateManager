package com.openclassrooms.realestatemanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

@Dao
public interface EstateDao {
    // READ
    @Query("SELECT * FROM estate")
    LiveData<List<Estate>> getAllEstates();

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addEstate(Estate estate);
}
