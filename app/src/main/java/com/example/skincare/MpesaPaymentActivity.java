package com.example.skincare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MpesaPaymentActivity extends AppCompatActivity {

    private TextView paymentAmountTextView;
    private Button proceedPaymentButton;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_payment);

        // Initialize views
        paymentAmountTextView = findViewById(R.id.paymentAmountTextView);
        proceedPaymentButton = findViewById(R.id.proceedPaymentButton);

        // Get the total price passed from CartActivity
        totalPrice = getIntent().getIntExtra("totalPrice", 0);
        paymentAmountTextView.setText("Total amount to pay: Ksh " + totalPrice);

        // Proceed to payment
        proceedPaymentButton.setOnClickListener(v -> showPhoneNumberDialog());
    }

    private void showPhoneNumberDialog() {
        EditText phoneInput = new EditText(this);
        phoneInput.setHint("Enter phone number (e.g. 2547XXXXXXXX)");

        new AlertDialog.Builder(this)
                .setTitle("M-Pesa Payment")
                .setMessage("Enter your phone number")
                .setView(phoneInput)
                .setPositiveButton("Proceed", (dialog, which) -> {
                    String phoneNumber = phoneInput.getText().toString().trim();
                    if (!phoneNumber.isEmpty()) {
                        showPinDialog(phoneNumber);
                    } else {
                        Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPinDialog(String phoneNumber) {
        EditText pinInput = new EditText(this);
        pinInput.setHint("Enter M-Pesa PIN");

        new AlertDialog.Builder(this)
                .setTitle("M-Pesa Payment")
                .setMessage("Enter your PIN")
                .setView(pinInput)
                .setPositiveButton("Proceed", (dialog, which) -> {
                    String pin = pinInput.getText().toString().trim();
                    if (!pin.isEmpty()) {
                        // Simulate the payment process
                        simulatePayment(phoneNumber, pin);
                    } else {
                        Toast.makeText(this, "PIN is required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void simulatePayment(String phoneNumber, String pin) {
        // Simulate a delay for payment processing (using background thread)
        new Thread(() -> {
            try {
                // Simulate a delay for payment processing (2 seconds)
                Thread.sleep(2000);

                // Once payment is processed, update the UI on the main thread
                runOnUiThread(() -> {
                    // Simulate the successful transaction
                    Toast.makeText(MpesaPaymentActivity.this, "Transaction completed successfully!", Toast.LENGTH_SHORT).show();
                    clearCartAndProceed();
                });

            } catch (InterruptedException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MpesaPaymentActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void clearCartAndProceed() {
        // Simulate clearing the cart (add logic to clear cart if needed)
        // For now, we will directly move to the AddressActivity

        Intent intent = new Intent(MpesaPaymentActivity.this, AddressActivity.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
