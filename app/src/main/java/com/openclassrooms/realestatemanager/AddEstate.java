package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.databinding.ActivityAddEstateBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import java.util.Objects;

public class AddEstate extends AppCompatActivity {

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
    private FloatingActionButton buttonSubmitAll;
    private EstateViewModel estateViewModel;
    private Estate newEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bindingView();
        this.configureViewModel();
        this.listenButtons();
        
    }

    private void bindingView(){
        com.openclassrooms.realestatemanager.databinding.ActivityAddEstateBinding binding = ActivityAddEstateBinding.inflate(getLayoutInflater());
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
        buttonSubmitAll = binding.floatingActionButtonSubmitAll;
        View view = binding.getRoot();
        setContentView(view);
    }

    private void listenButtons(){
        buttonSubmitAll.setOnClickListener(view -> {
            newEstate = this.inputsController();
            if(newEstate != null) {
                estateViewModel.createEstate(newEstate);
                this.finish();
            }
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
        
        if(!isErrorEstateAgent && !isErrorType && !isErrorPrice && !isErrorAddress && !isErrorFullDescription && !isErrorNumberOfRooms && !isErrorNumberOfBedrooms && !isErrorNumberOfBathrooms && !isErrorSurface && !isErrorCity){
            return new Estate(valueEstateAgent, valueType, Integer.parseInt(valuePrice), Integer.parseInt(valueSurface), Integer.parseInt(valueNumberOfRooms), Integer.parseInt(valueNumberOfBedrooms), Integer.parseInt(valueNumberOfBathrooms), valueFullDescription, valueAddress, valueCity);
        } else {
            return null;
        }
    }

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
    }
}
