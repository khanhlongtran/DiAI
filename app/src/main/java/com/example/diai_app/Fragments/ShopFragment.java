package com.example.diai_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diai_app.Adapter.CategoryAdapter;
import com.example.diai_app.Adapter.ProductAdapter;
import com.example.diai_app.DataModel.Category;
import com.example.diai_app.DataModel.Product;
import com.example.diai_app.Manager.CartManager;
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopFragment extends BaseFragment {
    private androidx.appcompat.widget.SearchView searchView;
    private ImageView cartIcon;
    private RecyclerView categoryRecyclerView, productRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    private TextView cartBadge, bestSellerTv, newArrivalTv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void addOnEventListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
        bestSellerTv.setOnClickListener(v -> listBestSeller());
        newArrivalTv.setOnClickListener(v -> listNewArrival());
        // **Sự kiện click cho cartIcon**
        cartIcon.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new CartFragment());
            transaction.addToBackStack(null); // Cho phép quay lại ShopFragment
            transaction.commit();
        });
    }

    private void listNewArrival() {
        filterProducts("a");
    }

    private void listBestSeller() {
        filterProducts("b");
    }

    @Override
    protected void bindView(View view) {
        searchView = view.findViewById(R.id.searchView);
        cartIcon = view.findViewById(R.id.cartIcon);
        cartBadge = view.findViewById(R.id.cartBadge);
        bestSellerTv = view.findViewById(R.id.tv_best_seller);
        newArrivalTv = view.findViewById(R.id.tv_new_arrival);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        seedData();
        setUpAdapterForRecyclerView();
        // Cập nhật số lượng sản phẩm trong giỏ
        updateCartBadge();
    }

    private void updateCartBadge() {
        int cartSize = CartManager.getInstance().getTotalQuantity(); // Lấy số lượng sản phẩm trong giỏ
        if (cartSize > 0) {
            cartBadge.setText(String.valueOf(cartSize));
            cartBadge.setVisibility(View.VISIBLE);
        } else {
            cartBadge.setVisibility(View.GONE);
        }
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }

    private void setUpAdapterForRecyclerView() {
        // Set Adapter cho Category
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Set Adapter cho Product
        productAdapter = new ProductAdapter(requireContext(), productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productRecyclerView.setAdapter(productAdapter);

    }

    private void seedData() {
        categoryList = Arrays.asList(
                new Category(1, "Thực phẩm chức năng"),
                new Category(2, "Vitamin & Khoáng chất"),
                new Category(3, "Thuốc kê đơn"),
                new Category(4, "Thuốc không kê đơn"),
                new Category(5, "Dược phẩm hỗ trợ tiêu hóa"),
                new Category(6, "Dược phẩm hỗ trợ tim mạch"),
                new Category(7, "Sản phẩm tăng cường miễn dịch"),
                new Category(8, "Thực phẩm bổ sung năng lượng")
        );

// Dữ liệu mẫu (Danh sách sản phẩm)
        productList = Arrays.asList(
                new Product(1, "Viên Uống Bổ Gan", "Thực phẩm chức năng", 250, R.drawable.bogan),
                new Product(2, "Collagen Dạng Nước", "Thực phẩm chức năng", 300, R.drawable.collagen),
                new Product(3, "Nhân Sâm Hàn Quốc", "Thực phẩm chức năng", 500, R.drawable.nhansam),

                new Product(4, "Vitamin C 500mg", "Vitamin & Khoáng chất", 100, R.drawable.vitamin_c),
                new Product(5, "Viên Uống Bổ Sung Kẽm", "Vitamin & Khoáng chất", 120, R.drawable.kem),
                new Product(6, "Viên Uống Canxi D3", "Vitamin & Khoáng chất", 180, R.drawable.canxi),

                new Product(7, "Thuốc Kháng Sinh Amoxicillin", "Thuốc kê đơn", 50, R.drawable.amoxicillin),
                new Product(8, "Thuốc Hạ Huyết Áp Losartan", "Thuốc kê đơn", 90, R.drawable.losartan),
                new Product(9, "Thuốc Trị Dạ Dày Omeprazole", "Thuốc kê đơn", 80, R.drawable.omeprazole),

                new Product(10, "Thuốc Cảm Cúm Paracetamol", "Thuốc không kê đơn", 30, R.drawable.paracetamol),
                new Product(11, "Thuốc Giảm Đau Ibuprofen", "Thuốc không kê đơn", 40, R.drawable.ibuprofen),
                new Product(12, "Siro Ho Dành Cho Trẻ Em", "Thuốc không kê đơn", 70, R.drawable.siroho),

                new Product(13, "Men Tiêu Hóa Dạng Viên", "Dược phẩm hỗ trợ tiêu hóa", 90, R.drawable.mentieudhoa),
                new Product(14, "Men Vi Sinh Dành Cho Người Lớn", "Dược phẩm hỗ trợ tiêu hóa", 110, R.drawable.menvs),

                new Product(15, "Viên Uống Bổ Tim Omega-3", "Dược phẩm hỗ trợ tim mạch", 250, R.drawable.omega3),
                new Product(16, "Viên Uống Hỗ Trợ Huyết Áp", "Dược phẩm hỗ trợ tim mạch", 200, R.drawable.huyetap),

                new Product(17, "Viên Uống Tăng Sức Đề Kháng", "Sản phẩm tăng cường miễn dịch", 190, R.drawable.dekhong),
                new Product(18, "Sâm Ngọc Linh Tự Nhiên", "Sản phẩm tăng cường miễn dịch", 800, R.drawable.samngoclinh),

                new Product(19, "Nước Uống Tăng Lực Hồng Sâm", "Thực phẩm bổ sung năng lượng", 300, R.drawable.nuoctangluc),
                new Product(20, "Thanh Năng Lượng Protein Bar", "Thực phẩm bổ sung năng lượng", 150, R.drawable.proteinbar)
        );

    }
}