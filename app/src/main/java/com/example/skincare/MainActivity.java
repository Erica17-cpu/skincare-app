package com.example.skincare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnCartChangeListener {
    private Spinner categorySpinner;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Map<String, List<Product>> productMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        // Set Up Spinner with Categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(adapter);

        //Ensure "Products" is always the first item in the spinner
        categorySpinner.setSelection(0);

        // Set up product map for each category
        loadProducts();

        // Handle Spinner Selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Do nothing when "Products" is selected
                    return;
                }
                //Proceed with the actual product categories
                String selectedCategory = parent.getItemAtPosition(position).toString();
                updateRecyclerView(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Find the cart button by ID
        ImageButton cartButton = findViewById(R.id.cartButton);

        // Set click listener to open CartActivity
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCartUpdated(Map<Product, Integer> cart) {
            Log.d("Cart", "Cart updated: " + cart.toString());
        }

    // Load product data (dummy example)
    private void loadProducts() {
        productMap = new HashMap<>();

        List<Product> cleansers = new ArrayList<>();
        cleansers.add(new Product("BYOMA", R.drawable.cleanser1, 3000));
        cleansers.add(new Product("CETAPHIL", R.drawable.cleanser2, 2500));
        cleansers.add(new Product("ANUA", R.drawable.cleanser3, 2700));
        productMap.put("Cleansers", cleansers);

        List<Product> moisturizers = new ArrayList<>();
        moisturizers.add(new Product("OASIS", R.drawable.moisturizer1, 4800));
        moisturizers.add(new Product("ELF", R.drawable.moisturizer2, 3400));
        moisturizers.add(new Product("GARNIER", R.drawable.moisturizer3, 1700));
        productMap.put("Moisturizers", moisturizers);

        List<Product> toners = new ArrayList<>();
        toners.add(new Product("BUBBLE", R.drawable.toner1, 4200));
        toners.add(new Product("SIMPLE", R.drawable.toner2, 2800));
        toners.add(new Product("ORDINARY", R.drawable.toner3, 2800));
        productMap.put("Toners", toners);

        List<Product> sunscreens = new ArrayList<>();
        sunscreens.add(new Product("BONDI SANDS", R.drawable.sunscreen1, 2700));
        sunscreens.add(new Product("LA ROCHE-POSAY", R.drawable.sunscreen2, 4800));
        sunscreens.add(new Product("SUPERGOOP", R.drawable.sunscreen3, 6400));
        productMap.put("Sunscreens", sunscreens);
    }

    // Update RecyclerView when a category is selected
    private void updateRecyclerView(String category) {
        List<Product> products = productMap.getOrDefault(category, new ArrayList<>()); // ✅ Prevent null pointer exception

        if (productAdapter == null) {
            productAdapter = new ProductAdapter(products, this);
            recyclerView.setAdapter(productAdapter);
        } else {
            productAdapter.updateProducts(products);

        }
    }
}
