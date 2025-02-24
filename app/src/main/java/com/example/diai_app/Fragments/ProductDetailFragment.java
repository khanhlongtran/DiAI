package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.DataModel.Product;
import com.example.diai_app.Manager.CartManager;
import com.example.diai_app.R;

public class ProductDetailFragment extends Fragment {

    private TextView detailProductName, detailProductPrice, detailProductDescription;
    private ImageView detailProductImage;
    private Button btnAddToCart;
    private Product product;
    Button btnIncrease, btnDecrease;
    TextView txtQuantity;
    int quantity = 1;
    private double totalPrice;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        detailProductName = view.findViewById(R.id.detailProductName);
        detailProductPrice = view.findViewById(R.id.detailProductPrice);
        detailProductDescription = view.findViewById(R.id.detailProductDescription);
        detailProductImage = view.findViewById(R.id.detailProductImage);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);

        // Ánh xạ view
        btnIncrease = view.findViewById(R.id.btnIncrease);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        txtQuantity = view.findViewById(R.id.txtQuantity);

        // Xử lý khi bấm nút tăng
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Xử lý khi bấm nút giảm
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                    updateTotalPrice();
                }
            }
        });

        // Hiển thị thông tin sản phẩm
        if (product != null) {
            detailProductName.setText(product.getName());
            totalPrice = product.getPrice(); // Lấy giá gốc
            updateTotalPrice(); // Cập nhật giá ban đầu
            detailProductDescription.setText(product.getName() + " description");
            detailProductImage.setImageResource(product.getImageResId()); // Giả sử dùng resource ID
        }

        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product, quantity);
                Toast.makeText(getActivity(), product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    // Hàm tính và cập nhật lại tổng giá
    private void updateTotalPrice() {
        totalPrice = product.getPrice() * quantity;
        detailProductPrice.setText("$" + totalPrice);
    }
}
