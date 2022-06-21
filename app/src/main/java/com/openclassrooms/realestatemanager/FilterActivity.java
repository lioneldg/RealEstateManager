package com.openclassrooms.realestatemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.databinding.ActivityFilterBinding;

import java.util.Objects;

public class FilterActivity extends AppCompatActivity {
    ActivityFilterBinding binding;
    TextInputLayout estateAgent;
    TextInputLayout estateType;
    TextInputLayout minPrice;
    TextInputLayout maxPrice;
    TextInputLayout minSurface;
    TextInputLayout maxSurface;
    TextInputLayout minRooms;
    TextInputLayout maxRooms;
    TextInputLayout minBedrooms;
    TextInputLayout maxBedrooms;
    TextInputLayout minBathrooms;
    TextInputLayout maxBathrooms;
    TextInputLayout sinceMonth;
    RadioButton allRadioButton;
    RadioButton soldOnlyRadioButton;
    RadioButton notSoldOnlyRadioButton;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setIU();
    }

    private void setIU() {
        estateAgent = binding.textEstateAgent;
        estateType = binding.textType;
        minPrice = binding.textMinimumPrice;
        maxPrice = binding.textMaximumPrice;
        minSurface = binding.textMinimumSurface;
        maxSurface = binding.textMaximumSurface;
        minRooms = binding.textMinimumNumberOfRooms;
        maxRooms = binding.textMaximumNumberOfRooms;
        minBedrooms = binding.textMinimumNumberOfBedrooms;
        maxBedrooms = binding.textMaximumNumberOfBedrooms;
        minBathrooms = binding.textMinimumNumberOfBathrooms;
        maxBathrooms = binding.textMaximumNumberOfBathrooms;
        sinceMonth =binding.textSince;
        allRadioButton = binding.radioButtonAll;
        soldOnlyRadioButton = binding.radioButtonSoldOnly;
        notSoldOnlyRadioButton = binding.radioButtonNotSoldOnly;
        floatingActionButton = binding.floatingActionButtonSubmitAll;
        floatingActionButton.setOnClickListener(view -> this.validate());
    }

    private void validate() {
        String estateAgentString = Objects.requireNonNull(estateAgent.getEditText()).getText().toString();
        String estateTypeString =  Objects.requireNonNull(estateType.getEditText()).getText().toString();
        String minPriceString =  Objects.requireNonNull(minPrice.getEditText()).getText().toString();
        String maxPriceString =  Objects.requireNonNull(maxPrice.getEditText()).getText().toString();
        String minSurfaceString =  Objects.requireNonNull(minSurface.getEditText()).getText().toString();
        String maxSurfaceString =  Objects.requireNonNull(maxSurface.getEditText()).getText().toString();
        String minRoomsString =  Objects.requireNonNull(minRooms.getEditText()).getText().toString();
        String maxRoomsString =  Objects.requireNonNull(maxRooms.getEditText()).getText().toString();
        String minBedroomsString =  Objects.requireNonNull(minBedrooms.getEditText()).getText().toString();
        String maxBedroomsString =  Objects.requireNonNull(maxBedrooms.getEditText()).getText().toString();
        String minBathroomsString =  Objects.requireNonNull(minBathrooms.getEditText()).getText().toString();
        String maxBathroomsString =  Objects.requireNonNull(maxBathrooms.getEditText()).getText().toString();
        String soldType = allRadioButton.isChecked() ? "all" : soldOnlyRadioButton.isChecked() ? "soldOnly" : "notSoldOnly";
        String since = Objects.requireNonNull(sinceMonth.getEditText()).getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("estateAgentString", estateAgentString);
        resultIntent.putExtra("estateTypeString", estateTypeString);
        resultIntent.putExtra("minPriceString", minPriceString);
        resultIntent.putExtra("maxPriceString", maxPriceString);
        resultIntent.putExtra("minSurfaceString", minSurfaceString);
        resultIntent.putExtra("maxSurfaceString", maxSurfaceString);
        resultIntent.putExtra("minRoomsString", minRoomsString);
        resultIntent.putExtra("maxRoomsString", maxRoomsString);
        resultIntent.putExtra("minBedroomsString", minBedroomsString);
        resultIntent.putExtra("maxBedroomsString", maxBedroomsString);
        resultIntent.putExtra("minBathroomsString", minBathroomsString);
        resultIntent.putExtra("maxBathroomsString", maxBathroomsString);
        resultIntent.putExtra("soldType", soldType);
        resultIntent.putExtra("since", since);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
