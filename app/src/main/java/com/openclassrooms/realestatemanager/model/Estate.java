package com.openclassrooms.realestatemanager.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Estate {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String estateAgent;

    private String estateType;

    private int estatePrice;

    private int estateSurface;

    private int estateNumberOfRooms;

    private int estateNbrOfBedrooms;

    private int estateNbrOfBathrooms;

    private String estateFullDescription;

    private String estateAddress;

    private String estateCity;

    private String photosListString;

    private boolean isSold;

    private  String lat;

    private String lng;

    private String pointsOfInterest;

    private long entryDate;

    private long soldDate;

    private String staticMapFileName;

    public Estate(){}

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

    public String getPhotosListString() {
        return photosListString;
    }

    public boolean getIsSold() {
        return isSold;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPointsOfInterest() {
        return pointsOfInterest;
    }

    public long getEntryDate() {
       return this.entryDate;
    }

    public long getSoldDate() {
        return this.soldDate;
    }

    public String getStaticMapFileName() {
        return staticMapFileName;
    }

    public void setEstateAgent(@NonNull String estateAgent) {
        this.estateAgent = estateAgent;
    }

    public void setEstateType(@NonNull String estateType) {
        this.estateType = estateType;
    }

    public void setEstatePrice(int estatePrice) {
        this.estatePrice = estatePrice;
    }

    public void setEstateSurface(int estateSurface) {
        this.estateSurface = estateSurface;
    }

    public void setEstateNumberOfRooms(int estateNumberOfRooms) {
        this.estateNumberOfRooms = estateNumberOfRooms;
    }

    public void setEstateNbrOfBedrooms(int estateNbrOfBedrooms) {
        this.estateNbrOfBedrooms = estateNbrOfBedrooms;
    }

    public void setEstateNbrOfBathrooms(int estateNbrOfBathrooms) {
        this.estateNbrOfBathrooms = estateNbrOfBathrooms;
    }

    public void setEstateFullDescription(@NonNull String estateFullDescription) {
        this.estateFullDescription = estateFullDescription;
    }

    public void setEstateAddress(@NonNull String estateAddress) {
        this.estateAddress = estateAddress;
    }

    public void setEstateCity(@NonNull String estateCity) {
        this.estateCity = estateCity;
    }

    public void setPhotosListString(@NonNull String photosListString) {
        this.photosListString = photosListString;
    }

    public void setIsSold(boolean sold) {
        isSold = sold;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setPointsOfInterest(String pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public void setEntryDate(long entryDate) {
        this.entryDate = entryDate;
    }

    public void setSoldDate(long soldDate) {
        this.soldDate = soldDate;
    }

    public void setStaticMapFileName(String staticMapFileName) {
        this.staticMapFileName = staticMapFileName;
    }
}
