package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.Adapter.CartAdapter;
import com.example.diai_app.DataModel.Product;
import com.example.diai_app.Manager.CartManager;
import com.example.diai_app.R;

import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;

    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems());
        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalPrice(); // Cập nhật tổng tiền khi khởi tạo
        btnCheckout.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new CheckoutFragment()); // Mở CheckoutFragment
            transaction.addToBackStack(null); // Cho phép quay lại CartFragment
            transaction.commit();
        });

        return view;
    }

    // Phương thức tính và cập nhật tổng tiền
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : CartManager.getInstance().getCartItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += product.getPrice() * quantity;
        }
        tvTotalPrice.setText("Total: $" + totalPrice);
    }
}
