package com.openclassrooms.realestatemanager;

import static com.openclassrooms.realestatemanager.Utils.REQUEST_IMAGE_CAPTURE;
import static com.openclassrooms.realestatemanager.Utils.REQUEST_IMAGE_PICK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.databinding.ActivityEditEstateBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditEstateActivity extends AppCompatActivity {

    private TextInputLayout textEstateAgent;
    private TextInputLayout textType;
    private TextInputLayout textPrice;
    private TextInputLayout textSurface;
    private TextInputLayout textNumberOfRooms;
    private TextInputLayout textNumberOfBedrooms;
    private TextInputLayout textNumberOfBathrooms;
    private TextInputLayout textFullDescription;
    private TextInputLayout textAddress;
    private TextInputLayout textCity;
    private ImageButton buttonAddGalleryImage;
    private ImageButton buttonAddPhoto;
    private Button buttonIsSold;
    private FloatingActionButton buttonSubmitAll;
    private RecyclerView detailPhotosRV;
    private EstateViewModel estateViewModel;
    private Estate newEstate;
    private ArrayList<String> photos = new ArrayList<>();
    private final ArrayList<Estate> estates = new ArrayList<>();
    private boolean isEditionMode = false;
    private boolean isSold = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEditionMode = !(getIntent().getIntExtra("position", -1) == -1);
        this.bindingView();
        this.configureViewModel();
        this.getAllEstates();
        this.listenButtons();
        
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(photos.size() > 0) {
            outState.putString("photos", Utils.fromArrayListStringToStringList(photos));
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String p = savedInstanceState.getString("photos");
        if(p != null && !p.equals("")){
            photos = Utils.fromStringListToArrayList(p);
            setAdapter();
        }
    }

    private void bindingView(){
        ActivityEditEstateBinding binding = ActivityEditEstateBinding.inflate(getLayoutInflater());
        textEstateAgent = binding.textEstateAgent;
        textType = binding.textType;
        textPrice = binding.textPrice;
        textSurface = binding.textSurface;
        textNumberOfRooms = binding.textNumberOfRooms;
        textNumberOfBedrooms = binding.textNumberOfBedrooms;
        textNumberOfBathrooms = binding.textNumberOfBathrooms;
        textFullDescription = binding.textFullDescription;
        textAddress = binding.textAddress;
        textCity = binding.textCity;
        buttonAddGalleryImage = binding.buttonAddGalleryImage;
        buttonAddPhoto = binding.buttonAddPhotos;
        buttonIsSold = binding.buttonSold;
        detailPhotosRV = binding.addPhotosRV;
        detailPhotosRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        buttonSubmitAll = binding.floatingActionButtonSubmitAll;
        if(!Utils.hasCamera(this)){
            buttonAddPhoto.setVisibility(View.INVISIBLE);
        }
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setIsSoldButton(boolean isSold) {
        this.isSold = isSold;
        buttonIsSold.setText(isSold ? R.string.not_sold : R.string.sold);
    }

    protected ArrayList<String> getPhotos() {
        return this.photos;
    }

    protected void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    protected void setAdapter(){
        detailPhotosRV.setAdapter(new PhotosRVAdapter(photos, this, isSold));
    }

    private void listenButtons(){
        buttonAddGalleryImage.setOnClickListener(view -> Utils.pickPhoto(this));
        buttonAddPhoto.setOnClickListener(view -> Utils.takePhoto(this));
        buttonIsSold.setOnClickListener(view -> {
            this.setIsSoldButton(!isSold);
            setAdapter();
        });
        buttonSubmitAll.setOnClickListener(view -> {
            newEstate = this.inputsController();
            if(newEstate != null) {
                long timestamp = System.currentTimeMillis();
                newEstate.setSoldDate(isSold ? timestamp : 0);
                if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(this))) {
                    setPositionFromAddress(newEstate.getEstateAddress());
                } else {
                    Toast.makeText(this, R.string.no_def_gps , Toast.LENGTH_LONG).show();
                    if (isEditionMode){
                        estateViewModel.updateEstate(newEstate);
                        Utils.sendNotification(this, getString(R.string.new_estate_added), false);
                    } else {
                        newEstate.setEntryDate(timestamp);
                        estateViewModel.createEstate(newEstate);
                        Utils.sendNotification(this, getString(R.string.estate_amended), false);
                    }
                    this.finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fileName = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fileName = Utils.storeBitmap(this, imageBitmap);
        }
        else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            Bitmap selectedImage = Utils.loadFromUri(photoUri, this);
            fileName = Utils.storeBitmap(this, selectedImage);
        }
        photos.add(fileName);
        setAdapter();
    }

    private void setPositionFromAddress(String address) {
        if(Utils.isInternetAvailable(Utils.getActiveNetworkInfo(this))) {
            //execute query
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                LatLng position = Utils.getPositionFromAddress(address);
                if(position != null) {
                    double lat = position.latitude;
                    double lng = position.longitude;
                    newEstate.setLat(String.valueOf(lat));
                    newEstate.setLng(String.valueOf(lng));
                    setPointsOfInterest(String.valueOf(lat), String.valueOf(lng));
                }
            });
        } else {
            Toast.makeText(this, R.string.no_def_gps , Toast.LENGTH_LONG).show();
        }
    }

    private void setPointsOfInterest(String lat, String lng) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String interests = Utils.getPointsOfInterest(lat, lng, this);
            newEstate.setPointsOfInterest(interests);
            setStaticMap(lat, lng);
        });
    }

    private void setStaticMap(String lat, String lng) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Bitmap mapBitmap = Utils.getStaticMap(lat, lng);
            String staticMapFileName = Utils.storeBitmap(this, mapBitmap);
            newEstate.setStaticMapFileName(staticMapFileName);
            if (isEditionMode) {
                estateViewModel.updateEstate(newEstate);
                Utils.sendNotification(this, getString(R.string.estate_amended), false);
            } else {
                long timestamp = System.currentTimeMillis();
                newEstate.setEntryDate(timestamp);
                estateViewModel.createEstate(newEstate);
                Utils.sendNotification(this, getString(R.string.new_estate_added), false);
            }
            finish();
        });
    }

    private Estate inputsController() {
        String valueEstateAgent = Objects.requireNonNull(textEstateAgent.getEditText()).getText().toString();
        String valueType = Objects.requireNonNull(textType.getEditText()).getText().toString();
        String valuePrice = Objects.requireNonNull(textPrice.getEditText()).getText().toString();
        String valueSurface = Objects.requireNonNull(textSurface.getEditText()).getText().toString();
        String valueNumberOfRooms = Objects.requireNonNull(textNumberOfRooms.getEditText()).getText().toString();
        String valueNumberOfBedrooms = Objects.requireNonNull(textNumberOfBedrooms.getEditText()).getText().toString();
        String valueNumberOfBathrooms = Objects.requireNonNull(textNumberOfBathrooms.getEditText()).getText().toString();
        String valueFullDescription = Objects.requireNonNull(textFullDescription.getEditText()).getText().toString();
        String valueAddress = Objects.requireNonNull(textAddress.getEditText()).getText().toString();
        String valueCity = Objects.requireNonNull(textCity.getEditText()).getText().toString();
        boolean isErrorEstateAgent = false;
        boolean isErrorType = false;
        boolean isErrorPrice = false;
        boolean isErrorSurface = false;
        boolean isErrorNumberOfBedrooms = false;
        boolean isErrorNumberOfBathrooms = false;
        boolean isErrorNumberOfRooms = false;
        boolean isErrorFullDescription = false;
        boolean isErrorAddress = false;
        boolean isErrorCity = false;
        boolean isErrorPhotos = false;
        if(!Utils.isLetterHyphenAndSpace(valueEstateAgent)){
            textEstateAgent.setError(getString(R.string.textOnlyError));
            isErrorEstateAgent = true;
        } else if(valueEstateAgent.isEmpty()){
            textEstateAgent.setError(getString(R.string.mandatoryInput));
            isErrorEstateAgent = true;
        } else {
            textEstateAgent.setError(null);
        }

        if(!Utils.isLetterHyphenAndSpace(valueType)){
            textType.setError(getString(R.string.textOnlyError));
            isErrorType = true;
        } else if(valueType.isEmpty()){
            textType.setError(getString(R.string.mandatoryInput));
            isErrorType = true;
        } else {
            textType.setError(null);
        }

        if(!Utils.isNumber(valuePrice)){
            textPrice.setError(getString(R.string.enterANumber));
            isErrorPrice = true;
        } else if(valuePrice.isEmpty()){
            textPrice.setError(getString(R.string.mandatoryInput));
            isErrorPrice = true;
        } else {
            textPrice.setError(null);
        }

        if(!Utils.isNumber(valueSurface)){
            textSurface.setError(getString(R.string.enterANumber));
            isErrorSurface = true;
        } else if(valueSurface.isEmpty()){
            textSurface.setError(getString(R.string.mandatoryInput));
            isErrorSurface = true;
        } else {
            textSurface.setError(null);
        }

        if(!Utils.isNumber(valueNumberOfRooms)){
            textNumberOfRooms.setError(getString(R.string.enterANumber));
            isErrorNumberOfRooms = true;
        } else if(valueNumberOfRooms.isEmpty()){
            textNumberOfRooms.setError(getString(R.string.mandatoryInput));
            isErrorNumberOfRooms = true;
        } else {
            textNumberOfRooms.setError(null);
        }

        if(!Utils.isNumber(valueNumberOfBedrooms)){
            textNumberOfBedrooms.setError(getString(R.string.enterANumber));
            isErrorNumberOfBedrooms = true;
        } else if(valueNumberOfRooms.isEmpty()){
            textNumberOfBedrooms.setError(getString(R.string.mandatoryInput));
            isErrorNumberOfBedrooms = true;
        } else {
            textNumberOfBedrooms.setError(null);
        }

        if(!Utils.isNumber(valueNumberOfBathrooms)){
            textNumberOfBathrooms.setError(getString(R.string.enterANumber));
            isErrorNumberOfBathrooms = true;
        } else if(valueNumberOfBathrooms.isEmpty()){
            textNumberOfBathrooms.setError(getString(R.string.mandatoryInput));
            isErrorNumberOfBathrooms = true;
        } else {
            textNumberOfBathrooms.setError(null);
        }

        if(valueFullDescription.isEmpty()){
            textFullDescription.setError(getString(R.string.mandatoryInput));
            isErrorFullDescription = true;
        } else {
            textFullDescription.setError(null);
        }

        if(!Utils.isAlphanumHyphenAndSpace(valueAddress)){
            textAddress.setError(getString(R.string.textOnlyError));
            isErrorAddress = true;
        } else if(valueAddress.isEmpty()){
            textAddress.setError(getString(R.string.mandatoryInput));
            isErrorAddress = true;
        } else {
            textAddress.setError(null);
        }

        if(!Utils.isLetterHyphenAndSpace(valueCity)){
            textCity.setError(getString(R.string.textOnlyError));
            isErrorCity = true;
        } else if(valueCity.isEmpty()){
            textCity.setError(getString(R.string.mandatoryInput));
            isErrorCity = true;
        } else {
            textCity.setError(null);
        }

        String photoListString = Utils.fromArrayListStringToStringList(photos);
        if(photos.size() == 0) {
            isErrorPhotos = true;
            Toast.makeText(this, R.string.Add_photos , Toast.LENGTH_LONG).show();
        }

        if(!isErrorEstateAgent && !isErrorType && !isErrorPrice && !isErrorAddress && !isErrorFullDescription && !isErrorNumberOfRooms && !isErrorNumberOfBedrooms && !isErrorNumberOfBathrooms && !isErrorSurface && !isErrorCity && !isErrorPhotos){
            Estate estate;
            if(isEditionMode) {
                estate = estates.get(getIntent().getIntExtra("position", 0));
            } else {
                estate = new Estate();
            }
            estate.setEstateAgent(valueEstateAgent);
            estate.setEstateType(valueType);
            estate.setEstatePrice(Integer.parseInt(valuePrice));
            estate.setEstateSurface(Integer.parseInt(valueSurface));
            estate.setEstateNumberOfRooms(Integer.parseInt(valueNumberOfRooms));
            estate.setEstateNbrOfBedrooms(Integer.parseInt(valueNumberOfBedrooms));
            estate.setEstateNbrOfBathrooms(Integer.parseInt(valueNumberOfBathrooms));
            estate.setEstateFullDescription(valueFullDescription);
            estate.setEstateAddress(valueAddress);
            estate.setEstateCity(valueCity);
            estate.setPhotosListString(photoListString);
            estate.setIsSold(isSold);
            return estate;
        } else {
            return null;
        }
    }

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
        this.estateViewModel.init();
    }

    private void getAllEstates(){
        assert this.estateViewModel.getAllEstates() != null;
        this.estateViewModel.getAllEstates().observe(this, this::updateEstatesList);
    }

    private void updateEstatesList(List<Estate> estates) {
        this.estates.clear();
        this.estates.addAll(estates);
        setCurrentEstate(getIntent().getIntExtra("position", 0));
    }

    public void setCurrentEstate(int position) {
        if(isEditionMode) {
            Estate estate = estates.get(position);
            photos = Utils.fromStringListToArrayList(estate.getPhotosListString());
            Objects.requireNonNull(textEstateAgent.getEditText()).setText(estate.getEstateAgent());
            Objects.requireNonNull(textType.getEditText()).setText(estate.getEstateType());
            Objects.requireNonNull(textPrice.getEditText()).setText(String.valueOf(estate.getEstatePrice()));
            Objects.requireNonNull(textSurface.getEditText()).setText(String.valueOf(estate.getEstateSurface()));
            Objects.requireNonNull(textNumberOfRooms.getEditText()).setText(String.valueOf(estate.getEstateNumberOfRooms()));
            Objects.requireNonNull(textNumberOfBedrooms.getEditText()).setText(String.valueOf(estate.getEstateNbrOfBedrooms()));
            Objects.requireNonNull(textNumberOfBathrooms.getEditText()).setText(String.valueOf(estate.getEstateNbrOfBathrooms()));
            Objects.requireNonNull(textFullDescription.getEditText()).setText(estate.getEstateFullDescription());
            Objects.requireNonNull(textAddress.getEditText()).setText(estate.getEstateAddress());
            Objects.requireNonNull(textCity.getEditText()).setText(estate.getEstateCity());
            this.setIsSoldButton(estate.getIsSold());
            setAdapter();
        }

    }
}
