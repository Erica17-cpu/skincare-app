package com.example.skincare;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Map<Product, Integer> cart;
    private OnCartChangeListener onCartChangeListener;
    private DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");

    public ProductAdapter(List<Product> productList, OnCartChangeListener onCartChangeListener) {
        this.productList = productList;
        this.cart = new HashMap<>();
        this.onCartChangeListener = onCartChangeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    private void addToCart(Product product, int quantity) {
        if (quantity <= 0) {
            cartRef.child(product.getName()).removeValue()
                    .addOnSuccessListener(unused -> Log.d("Cart", "Removed from cart: " + product.getName()))
                    .addOnFailureListener(e -> Log.e("Cart", "Failed to remove", e));
            return;
        }

        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("name", product.getName());
        cartItem.put("price", product.getPrice());
        cartItem.put("quantity", quantity);

        cartRef.child(product.getName()).setValue(cartItem)
                .addOnSuccessListener(unused -> Log.d("Cart", "Added to cart: " + product.getName()))
                .addOnFailureListener(e -> Log.e("Cart", "Failed to add", e));
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productImage.setClipToOutline(true);
        holder.productPrice.setText(String.format("Ksh %d", product.getPrice()));

        // Log the assigned image resource
        Log.d("ProductAdapter", "Setting image for " + product.getName() + ": " + product.getImageResource());

        holder.productImage.setImageResource(product.getImageResource());

        // Get current quantity in cart, default is 0
        int currentQuantity = cart.getOrDefault(product, 0);
        holder.productQuantity.setText(String.valueOf(currentQuantity));


        // Handle "+" button click
        holder.buttonAdd.setOnClickListener(v -> {
            int newQuantity = cart.getOrDefault(product, 0) + 1;
            cart.put(product, newQuantity);
            holder.productQuantity.setText(String.valueOf(newQuantity));

            Log.d("ProductAdapter", "Clicked Add to Cart for: " + product.getName());

            addToCart(product, newQuantity); // Call Firebase function

            if (onCartChangeListener != null) {
                onCartChangeListener.onCartUpdated(cart);
            }
        });

        // Handle "-" button click
        holder.buttonMinus.setOnClickListener(v -> {
            int newQuantity = cart.getOrDefault(product, 0) - 1;
            if (newQuantity > 0) {
                cart.put(product, newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
            } else {
                cart.remove(product);
                holder.productQuantity.setText("0");
            }
            if (onCartChangeListener != null) {
                onCartChangeListener.onCartUpdated(cart);
            }
        });

        // Handle "Add to Cart" button click
        holder.buttonAddToCart.setOnClickListener(v -> {
            int finalQuantity = cart.getOrDefault(product, 0);
            if (finalQuantity > 0) {
                addToCart(product, finalQuantity);
            } else {
                Log.d("Cart", "No quantity selected for: " + product.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        ImageButton buttonAdd, buttonMinus;
        Button buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }

    public interface OnCartChangeListener {
        void onCartUpdated(Map<Product, Integer> cart);
    }
}
