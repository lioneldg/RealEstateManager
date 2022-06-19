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

    @Query("SELECT * FROM estate WHERE :isSold = isSold")
    LiveData<List<Estate>> getFilteredEstates(int isSold);

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEstate(Estate estate);

    //UPDATE
    @Update
    void updateEstate(Estate estate);

}
