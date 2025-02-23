package com.example.diai_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.DataModel.Product;
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private final List<Product> productList;
    private final Map<Product, Integer> cartItems;

    // Constructor nhận vào Map<Product, Integer>
    public CartAdapter(Map<Product, Integer> cartItems) {
        this.cartItems = cartItems;
        this.productList = new ArrayList<>(cartItems.keySet()); // Chuyển thành List
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        int quantity = cartItems.get(product);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productImage.setImageResource(product.getImageResId());
        holder.productQuantity.setText("Quantity: " + quantity);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            productImage = itemView.findViewById(R.id.cartProductImage);
            productQuantity = itemView.findViewById(R.id.cartProductQuantity);
        }
    }
}
