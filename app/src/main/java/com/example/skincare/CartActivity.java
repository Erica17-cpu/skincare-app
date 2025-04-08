package com.example.skincare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartList;
    private DatabaseReference cartRef;
    private TextView totalPriceTextView;
    private Button proceedToPayButton;
    private int totalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        proceedToPayButton = findViewById(R.id.proceedToPayButton);

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        loadCartData();

        proceedToPayButton.setOnClickListener(v -> {
            // Create an intent to navigate to the M-Pesa page
            Intent intent = new Intent(CartActivity.this, MpesaPaymentActivity.class);
            intent.putExtra("totalPrice", totalPrice); // Pass the total price to the payment activity
            startActivity(intent);

            // Optional: Show a confirmation message (if needed)
            Toast.makeText(CartActivity.this, "Proceeding to payment...", Toast.LENGTH_SHORT).show();

            // Clear the cart after navigating to the payment page
            cartRef.removeValue();
            cartList.clear();
            cartAdapter.notifyDataSetChanged();
            totalPriceTextView.setText("Total: Ksh 0");


        });
    }

    private void loadCartData() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                totalPrice = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    Integer price = data.child("price").getValue(Integer.class);
                    Integer quantity = data.child("quantity").getValue(Integer.class);

                    if (name != null && price != null && quantity != null && quantity > 0) {
                        totalPrice += price * quantity;
                        cartList.add(new Product(name, 0, price, quantity)); // Image is not used
                    }
                }


                totalPriceTextView.setText("Total: Ksh "+totalPrice);
                cartAdapter.notifyDataSetChanged();
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartActivity", "Failed to load cart", error.toException());
            }
        });
    }
}
