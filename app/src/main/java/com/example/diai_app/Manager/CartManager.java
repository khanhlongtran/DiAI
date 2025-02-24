package com.example.diai_app.Manager;

import com.example.diai_app.DataModel.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    // Thay đổi từ List sang Map để lưu thêm số lượng
    private final Map<Product, Integer> cartItems;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm sản phẩm với số lượng
    public void addToCart(Product product, int quantity) {
        // Nếu sản phẩm đã tồn tại, tăng số lượng lên
        if (cartItems.containsKey(product)) {
            int currentQuantity = cartItems.get(product);
            cartItems.put(product, currentQuantity + quantity);
        } else {
            // Nếu chưa có thì thêm mới với số lượng được chọn
            cartItems.put(product, quantity);
        }
    }

    // Lấy danh sách sản phẩm trong giỏ hàng cùng với số lượng
    public Map<Product, Integer> getCartItems() {
        return cartItems;
    }

    // Xóa giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }

    // Xóa một sản phẩm cụ thể khỏi giỏ hàng
    public void removeFromCart(Product product) {
        cartItems.remove(product);
    }

    // Cập nhật số lượng của một sản phẩm trong giỏ hàng
    public void updateQuantity(Product product, int quantity) {
        if (cartItems.containsKey(product)) {
            if (quantity > 0) {
                cartItems.put(product, quantity);
            } else {
                cartItems.remove(product);
            }
        }
    }

    // Tính tổng số lượng sản phẩm trong giỏ hàng
    public int getTotalQuantity() {
        int total = 0;
        for (int quantity : cartItems.values()) {
            total += quantity;
        }
        return total;
    }

    // Tính tổng giá trị giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }
}


