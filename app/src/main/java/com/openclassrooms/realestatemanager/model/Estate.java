package com.openclassrooms.realestatemanager.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Estate {
    @PrimaryKey(autoGenerate = true)
    private final long id;

    @NonNull
    private final String estateAgent;

    @NonNull
    private final String estateType;

    @NonNull
    private final int estatePrice;

    @NonNull
    private final int estateSurface;

    @NonNull
    private final int estateNumberOfRooms;

    @NonNull
    private final String estateFullDescription;

    @NonNull
    private final String estateAddress;

    public Estate(long id, @NonNull String estateAgent, @NonNull String estateType,  @NonNull int estatePrice,  @NonNull int estateSurface,  @NonNull int estateNumberOfRooms, @NonNull String estateFullDescription, @NonNull String estateAddress) {
        this.id = id;
        this.estateAgent = estateAgent;
        this.estateType = estateType;
        this.estatePrice = estatePrice;
        this.estateSurface = estateSurface;
        this.estateNumberOfRooms = estateNumberOfRooms;
        this.estateFullDescription = estateFullDescription;
        this.estateAddress = estateAddress;
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

    @NonNull
    public int getEstatePrice() {
        return estatePrice;
    }

    @NonNull
    public int getEstateSurface() {
        return estateSurface;
    }

    @NonNull
    public int getEstateNumberOfRooms() {
        return estateNumberOfRooms;
    }

    @NonNull
    public String getEstateFullDescription() {
        return estateFullDescription;
    }

    @NonNull
    public String getEstateAddress() {
        return estateAddress;
    }
}
