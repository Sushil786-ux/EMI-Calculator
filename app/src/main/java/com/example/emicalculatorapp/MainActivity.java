package com.example.emicalculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etLoanAmount, etInterestRate, etTenure;
    Button btnCalculate, btnReset;
    TextView tvEMI, tvInterest, tvTotalPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        etLoanAmount = findViewById(R.id.etLoanAmount);
        etInterestRate = findViewById(R.id.etInterestRate);
        etTenure = findViewById(R.id.etTenure);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvEMI = findViewById(R.id.tvEMI);
        tvInterest = findViewById(R.id.tvInterest);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateEMI();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCalculator();
            }
        });
    }

    private void resetCalculator() {
        etLoanAmount.setText("");
        etInterestRate.setText("");
        etTenure.setText("");

        tvEMI.setText("Monthly EMI: ₹0.00");
        tvInterest.setText("Total Interest: ₹0.00");
        tvTotalPayment.setText("Total Payment: ₹0.00");

    }

    private void calculateEMI() {
        String loanText = etLoanAmount.getText().toString().trim();
        String interestText = etInterestRate.getText().toString().trim();
        String tenureText = etTenure.getText().toString().trim();

        if (loanText.isEmpty() ||
                interestText.isEmpty() ||
                tenureText.isEmpty()) {

            Toast.makeText(this,
                    "Please enter all details",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        try {

            double principal = Double.parseDouble(loanText);
            double annualInterest = Double.parseDouble(interestText);
            double years = Double.parseDouble(tenureText);

            if (principal <= 0 || annualInterest < 0 || years <= 0) {

                Toast.makeText(this,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Convert annual interest rate to monthly rate
            double monthlyRate = annualInterest / 12 / 100;

            // Total number of monthly installments
            double months = years * 12;

            double emi;

            if (monthlyRate == 0) {

                // EMI when interest rate is 0%
                emi = principal / months;

            } else {

                // EMI Formula
                emi = (principal * monthlyRate *
                        Math.pow(1 + monthlyRate, months))
                        /
                        (Math.pow(1 + monthlyRate, months) - 1);
            }

            double totalPayment = emi * months;

            double totalInterest = totalPayment - principal;

            tvEMI.setText(String.format(
                    Locale.US,
                    "Monthly EMI: ₹%.2f",
                    emi
            ));

            tvInterest.setText(String.format(
                    Locale.US,
                    "Total Interest: ₹%.2f",
                    totalInterest
            ));

            tvTotalPayment.setText(String.format(
                    Locale.US,
                    "Total Payment: ₹%.2f",
                    totalPayment
            ));

        } catch (NumberFormatException e) {

            Toast.makeText(this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT).show();
        }

    }
}