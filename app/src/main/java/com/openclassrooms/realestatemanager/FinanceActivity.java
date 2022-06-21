package com.openclassrooms.realestatemanager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.databinding.ActivityFinanceBinding;

import java.util.Objects;

public class FinanceActivity extends AppCompatActivity {
    ActivityFinanceBinding binding;
    TextInputLayout totalAmount, contribution, interestRate, duration;
    TextView result;
    FloatingActionButton validateButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityFinanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setUI();
        this.setValidation();
        super.onCreate(savedInstanceState);
    }

    private void setUI() {
        totalAmount = binding.textTotalAmount;
        contribution = binding.textContribution;
        interestRate = binding.textInterestRate;
        duration = binding.textDuration;
        result = binding.textResult;
        validateButton = binding.floatingActionValidateButton;
        result.setVisibility(View.GONE);
    }

    private void setValidation() {
        validateButton.setOnClickListener(view -> {
            String totalAmountString = Objects.requireNonNull(totalAmount.getEditText()).getText().toString();
            String contributionString = Objects.requireNonNull(contribution.getEditText()).getText().toString();
            String interestRateString = Objects.requireNonNull(interestRate.getEditText()).getText().toString();
            String durationString = Objects.requireNonNull(duration.getEditText()).getText().toString();

            if(totalAmountString.equals("")){
                totalAmount.setError(getString(R.string.mandatoryInput));
            } else {
                totalAmount.setError("");
            }

            if(contributionString.equals("")){
                contribution.setError(getString(R.string.mandatoryInput));
            } else {
                contribution.setError("");
            }

            if(interestRateString.equals("")){
                interestRate.setError(getString(R.string.mandatoryInput));
            } else {
                interestRate.setError("");
            }

            if(durationString.equals("")){
                duration.setError(getString(R.string.mandatoryInput));
            } else {
                duration.setError("");
            }

            if(!totalAmountString.equals("") && !contributionString.equals("") && !interestRateString.equals("") && !durationString.equals("")) {
                int totalAmountInt = Integer.parseInt(totalAmountString);
                int contributionInt = Integer.parseInt(contributionString);
                double interestRateDouble = Double.parseDouble(interestRateString);
                int durationInt = Integer.parseInt(durationString);
                int monthlyPayment = (int)(Utils.getFinanceMonthlyPayment(totalAmountInt, contributionInt, interestRateDouble, durationInt));
                int costOfFinancing = (int)(Utils.getCostOfFinancing(totalAmountInt, contributionInt, interestRateDouble, durationInt));
                String resultText = monthlyPayment + "$" + " " + getString(R.string.by_month) + " " + getString(R.string.during) + " " + durationString + " " + getString(R.string.months) + "\n\n" + getString(R.string.total_cost_of_financing) + " = " + costOfFinancing + "$";
                result.setText(resultText);
                result.setVisibility(View.VISIBLE);
            } else {
                result.setVisibility(View.GONE);
            }


        });
    }
}
