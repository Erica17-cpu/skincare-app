package com.example.skincare;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MpesaPaymentActivity extends AppCompatActivity {

    private TextView paymentAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_payment);  // Link to your layout

        // Initialize views
        paymentAmountTextView = findViewById(R.id.paymentAmountTextView);

        // Get the total price passed from CartActivity
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);
        paymentAmountTextView.setText("Total amount to pay: Ksh " + totalPrice);
    }
}

