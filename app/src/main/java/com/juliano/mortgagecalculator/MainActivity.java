package com.juliano.mortgagecalculator;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    Toolbar topToolBar;
    Spinner spinner;
    Button calcButton;
    TextInputEditText principleInput;
    TextInputEditText interestInput;
    TextInputEditText amorInput;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Ran when the main activity is launched and created
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        topToolBar = (Toolbar) findViewById(R.id.topToolBar); // Find toolbar from layout
        setSupportActionBar(topToolBar);
        getSupportActionBar().setTitle("Mortgage Calculator");

        principleInput = (TextInputEditText) findViewById(R.id.principleInput); //Find the text inputs from the layout
        interestInput = (TextInputEditText) findViewById(R.id.interestInput);
        amorInput = (TextInputEditText) findViewById(R.id.amorInput);

        principleInput.setInputType(InputType.TYPE_CLASS_NUMBER); //Restrict inputs on the text fields
        interestInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amorInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        spinner = (Spinner) findViewById(R.id.paymentSpinner); //Find and setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.payment_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        resultText = (TextView) findViewById(R.id.resultText);

        calcButton = (Button) findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() { //Set the buttons on click event
            @Override
            public void onClick(View view) {
                //Parse the textinputs and perform calculations asked for in assignment
                String principleStr = principleInput.getText().toString();
                String interestStr = interestInput.getText().toString();
                String amortizationStr = amorInput.getText().toString();
                String paymentOption = spinner.getSelectedItem().toString();

                if (!principleStr.isEmpty() && !interestStr.isEmpty() && !amortizationStr.isEmpty()) {

                    int principle = Integer.parseInt(principleStr);
                    double interest = Double.parseDouble(interestStr) ;
                    int amortization = Integer.parseInt(amortizationStr);
                    int paymentsPerYear = 0;

                    switch (paymentOption) {
                        case "Monthly":
                            paymentsPerYear = 12;
                            break;
                        case "Bi-Monthly":
                            paymentsPerYear = 6;
                            break;
                        case "Weekly":
                            paymentsPerYear = 52;
                            break;
                        case "Bi-Weekly":
                            paymentsPerYear = 26;
                            break;
                    }

                    double periodicInterest = interest / paymentsPerYear;
                    int numberOfPayments = amortization * paymentsPerYear;
                    double periodicPayment = (principle * periodicInterest * Math.pow(1 + periodicInterest, numberOfPayments)) /
                            (Math.pow(1 + periodicInterest, numberOfPayments) - 1);

                    resultText.setText(String.format("%s Payment: $%.2f", paymentOption, periodicPayment));

                } else {
                    resultText.setText("Please enter all values");
                }

            }
        });

    }


}