package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.Manager.CartManager;
import com.example.diai_app.R;

public class CheckoutFragment extends Fragment {

    private EditText editTextName, editTextAddress, editTextPhone;
    private Button btnConfirmOrder;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        btnConfirmOrder = view.findViewById(R.id.btnConfirmOrder);

        btnConfirmOrder.setOnClickListener(v -> {
            // Xóa toàn bộ giỏ hàng
            CartManager.getInstance().clearCart();
            Toast.makeText(getActivity(), "Order Confirmed! (COD Only)", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại CartFragment
        });

        return view;
    }
}
