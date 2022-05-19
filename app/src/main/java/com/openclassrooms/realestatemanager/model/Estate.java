package com.openclassrooms.realestatemanager.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Estate {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private final String estateAgent;

    @NonNull
    private final String estateType;

    private final int estatePrice;

    private final int estateSurface;

    private final int estateNumberOfRooms;

    private final int estateNbrOfBedrooms;

    private final int estateNbrOfBathrooms;

    @NonNull
    private final String estateFullDescription;

    @NonNull
    private final String estateAddress;

    @NonNull
    private final String estateCity;

    public Estate(@NonNull String estateAgent, @NonNull String estateType, int estatePrice, int estateSurface, int estateNumberOfRooms, int estateNbrOfBedrooms, int estateNbrOfBathrooms,  @NonNull String estateFullDescription, @NonNull String estateAddress, @NonNull String estateCity) {
        this.estateAgent = estateAgent;
        this.estateType = estateType;
        this.estatePrice = estatePrice;
        this.estateSurface = estateSurface;
        this.estateNumberOfRooms = estateNumberOfRooms;
        this.estateNbrOfBedrooms = estateNbrOfBedrooms;
        this.estateNbrOfBathrooms = estateNbrOfBathrooms;
        this.estateFullDescription = estateFullDescription;
        this.estateAddress = estateAddress;
        this.estateCity = estateCity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getEstateAgent() {
        return estateAgent;
    }

    @NonNull
    public String getEstateType() {
        return estateType;
    }

    public int getEstatePrice() {
        return estatePrice;
    }

    public int getEstateSurface() {
        return estateSurface;
    }

    public int getEstateNumberOfRooms() {
        return estateNumberOfRooms;
    }

    public int getEstateNbrOfBedrooms() {
        return estateNbrOfBedrooms;
    }

    public int getEstateNbrOfBathrooms() {
        return estateNbrOfBathrooms;
    }

    @NonNull
    public String getEstateFullDescription() {
        return estateFullDescription;
    }

    @NonNull
    public String getEstateAddress() {
        return estateAddress;
    }

    @NonNull
    public String getEstateCity() {
        return estateCity;
    }
}
