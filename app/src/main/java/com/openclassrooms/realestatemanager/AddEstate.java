package com.openclassrooms.realestatemanager;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.databinding.ActivityAddEstateBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewmodel.EstateViewModel;
import com.openclassrooms.realestatemanager.viewmodel.Injection;
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class AddEstate extends AppCompatActivity {

    private ActivityAddEstateBinding binding;
    private TextView textTitle;
    private TextInputLayout textEstateAgent;
    private TextInputLayout textType;
    private TextInputLayout textPrice;
    private TextInputLayout textSurface;
    private TextInputLayout textNumberOfRooms;
    private TextInputLayout textFullDescription;
    private TextInputLayout textAddress;
    private Button buttonAddPhotos;
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
        binding = ActivityAddEstateBinding.inflate(getLayoutInflater());
        textTitle = binding.textTitle;
        textEstateAgent = binding.textEstateAgent;
        textType = binding.textType;
        textPrice = binding.textPrice;
        textSurface = binding.textSurface;
        textNumberOfRooms = binding.textNumberOfRooms;
        textFullDescription = binding.textFullDescription;
        textAddress = binding.textAddress;
        buttonAddPhotos = binding.buttonAddPhotos;
        buttonSubmitAll = binding.floatingActionButtonSubmitAll;
        View view = binding.getRoot();
        setContentView(view);
    }

    private void listenButtons(){
        buttonSubmitAll.setOnClickListener(view -> {
            newEstate = this.inputsController();
            estateViewModel.createEstate(newEstate);
            //get new db values to print
            estateViewModel.getAllEstates();
            this.finish();
        });
    }
    
    private Estate inputsController() {
        String valueEstateAgent = textEstateAgent.getEditText().getText().toString();
        String valueType = textType.getEditText().getText().toString();
        String valuePrice = textPrice.getEditText().getText().toString();
        String valueSurface = textSurface.getEditText().getText().toString();
        String valueNumberOfRooms = textNumberOfRooms.getEditText().getText().toString();
        String valueFullDescription = textFullDescription.getEditText().getText().toString();
        String valueAddress = textAddress.getEditText().getText().toString();
        boolean isErrorEstateAgent = false;
        boolean isErrorType = false;
        boolean isErrorPrice = false;
        boolean isErrorSurface = false;
        boolean isErrorNumberOfRooms = false;
        boolean isErrorFullDescription = false;
        boolean isErrorAddress = false;
        if(!Utils.isLetterHyphenAndSpace(valueEstateAgent)){
            textEstateAgent.setError("Ne pas saisir de chiffre ou de caractères spéciaux");
            isErrorEstateAgent = true;
        } else if(valueEstateAgent.isEmpty()){
            textEstateAgent.setError("Saisie obligatoire");
            isErrorEstateAgent = true;
        } else {
            textEstateAgent.setError(null);
            isErrorEstateAgent = false;
        }

        if(!Utils.isLetterHyphenAndSpace(valueType)){
            textType.setError("Ne pas saisir de chiffre ou de caractères spéciaux");
            isErrorType = true;
        } else if(valueType.isEmpty()){
            textType.setError("Saisie obligatoire");
            isErrorType = true;
        } else {
            textType.setError(null);
            isErrorType = false;
        }

        if(!Utils.isNumber(valuePrice)){
            textPrice.setError("Saisir un nombre");
            isErrorPrice = true;
        } else if(valuePrice.isEmpty()){
            textPrice.setError("Saisie obligatoire");
            isErrorPrice = true;
        } else {
            textPrice.setError(null);
            isErrorPrice = false;
        }

        if(!Utils.isNumber(valueSurface)){
            textSurface.setError("Saisir un nombre");
            isErrorSurface = true;
        } else if(valueSurface.isEmpty()){
            textSurface.setError("Saisie obligatoire");
            isErrorSurface = true;
        } else {
            textSurface.setError(null);
            isErrorSurface = false;
        }

        if(!Utils.isNumber(valueNumberOfRooms)){
            textNumberOfRooms.setError("Saisir un nombre");
            isErrorNumberOfRooms = true;
        } else if(valueNumberOfRooms.isEmpty()){
            textNumberOfRooms.setError("Saisie obligatoire");
            isErrorNumberOfRooms = true;
        } else {
            textNumberOfRooms.setError(null);
            isErrorNumberOfRooms = false;
        }

        if(valueFullDescription.isEmpty()){
            textFullDescription.setError("Saisie obligatoire");
            isErrorFullDescription = true;
        } else {
            textFullDescription.setError(null);
            isErrorFullDescription = false;
        }

        if(!Utils.isAlphanumHyphenAndSpace(valueAddress)){
            textAddress.setError("Ne pas saisir de chiffre ou de caractères spéciaux");
            isErrorAddress = true;
        } else if(valueAddress.isEmpty()){
            textAddress.setError("Saisie obligatoire");
            isErrorAddress = true;
        } else {
            textAddress.setError(null);
            isErrorAddress = false;
        }
        
        if(!isErrorEstateAgent && !isErrorType && !isErrorPrice && !isErrorAddress && !isErrorFullDescription && !isErrorNumberOfRooms && !isErrorSurface){
            return new Estate(valueEstateAgent, valueType, Integer.parseInt(valuePrice), Integer.parseInt(valueSurface), Integer.parseInt(valueNumberOfRooms), valueFullDescription, valueAddress);
        } else {
            return null;
        }
    };

    // Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = new ViewModelProvider(this, mViewModelFactory).get(EstateViewModel.class);
    }
}
