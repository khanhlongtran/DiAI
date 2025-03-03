package com.example.diai_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.DataModel.User;
import com.example.diai_app.Manager.CartManager;
import com.example.diai_app.R;
import com.google.gson.Gson;

import java.util.Calendar;

public class CheckoutFragment extends BaseFragment {

    private EditText editTextName, editTextAddress, editTextPhone;
    private Button btnConfirmOrder;
    private RadioGroup radioGroupPayment;
    private LinearLayout paymentDetailsLayout;
    private EditText editTextCardNumber, editTextCardName, editTextExpiryDate, editTextCVV;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy thông tin user từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);
        if (userJson != null) {
            Gson gson = new Gson();
            User loggedInUser = gson.fromJson(userJson, User.class);
            editTextName.setText(loggedInUser.getFullname());
        }
    }

    @Override
    protected void bindView(View view) {
        editTextName = view.findViewById(R.id.editTextName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        btnConfirmOrder = view.findViewById(R.id.btnConfirmOrder);
        radioGroupPayment = view.findViewById(R.id.radioGroupPayment);

        paymentDetailsLayout = view.findViewById(R.id.paymentDetailsLayout);
        editTextCardNumber = view.findViewById(R.id.editTextCardNumber);
        editTextCardName = view.findViewById(R.id.editTextCardName);
        editTextExpiryDate = view.findViewById(R.id.editTextExpiryDate);
        editTextCVV = view.findViewById(R.id.editTextCVV);

        RadioButton radioCOD = view.findViewById(R.id.radioCOD);
        RadioButton radioZaloPay = view.findViewById(R.id.radioZaloPay);
        RadioButton radioMomo = view.findViewById(R.id.radioMomo);
        RadioButton radioVISA = view.findViewById(R.id.radioVISA);
        RadioButton radioBank = view.findViewById(R.id.radioBank);

        int iconSize = 60; // Kích thước 30x30 dp
        radioCOD.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable(R.drawable.ic_cod, iconSize, iconSize), null, null, null);
        radioZaloPay.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable(R.drawable.ic_zalopay, iconSize, iconSize), null, null, null);
        radioMomo.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable(R.drawable.ic_momo, iconSize, iconSize), null, null, null);
        radioVISA.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable(R.drawable.ic_visa, iconSize, iconSize), null, null, null);
        radioBank.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable(R.drawable.ic_bank, iconSize, iconSize), null, null, null);

    }

    @Override
    protected void addOnEventListener() {

        btnConfirmOrder.setOnClickListener(v -> {

            String name = editTextName.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            String paymentMethod = (String) btnConfirmOrder.getTag();
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng chọn phương thức thanh toán hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            // Validate thông tin người nhận
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin người nhận", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra tên (Full Name) - chỉ chứa chữ cái và khoảng trắng, tối thiểu 2 ký tự
            if (!name.matches("[a-zA-Z ]{2,}")) {
                Toast.makeText(getActivity(), "Tên không hợp lệ (chỉ chứa chữ cái và tối thiểu 2 ký tự)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra địa chỉ (Shipping Address) - không bỏ trống, tối thiểu 5 ký tự
            if (address.length() < 5) {
                Toast.makeText(getActivity(), "Địa chỉ không hợp lệ (tối thiểu 5 ký tự)", Toast.LENGTH_SHORT).show();
                return;
            }


            if (paymentMethod.equals("VISA") || paymentMethod.equals("Ngân hàng")) {
                String cardNumber = editTextCardNumber.getText().toString().trim();
                String cardName = editTextCardName.getText().toString().trim();
                String expiryDate = editTextExpiryDate.getText().toString().trim();
                String cvv = editTextCVV.getText().toString().trim();

                // Validate thông tin thẻ
                if (cardNumber.isEmpty() || cardName.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin thẻ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra số thẻ (Card Number) - chỉ chứa số, độ dài từ 13-19 chữ số
                if (!cardNumber.matches("\\d{13,19}")) {
                    Toast.makeText(getActivity(), "Số thẻ không hợp lệ (13-19 chữ số)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra tên chủ thẻ (Cardholder Name) - chỉ chứa chữ cái và khoảng trắng
                if (!cardName.matches("[a-zA-Z ]+")) {
                    Toast.makeText(getActivity(), "Tên chủ thẻ không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra CVV - chỉ chứa số, độ dài 3 hoặc 4 chữ số
                if (!cvv.matches("\\d{3,4}")) {
                    Toast.makeText(getActivity(), "CVV không hợp lệ (3 hoặc 4 chữ số)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra ngày hết hạn (Expiry Date) - định dạng MM/YY và phải hợp lệ
                if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                    Toast.makeText(getActivity(), "Ngày hết hạn không hợp lệ (MM/YY)", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String[] dateParts = expiryDate.split("/");
                    int expMonth = Integer.parseInt(dateParts[0]);
                    int expYear = Integer.parseInt(dateParts[1]) + 2000; // Chuyển từ YY sang YYYY

                    Calendar now = Calendar.getInstance();
                    int currentMonth = now.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
                    int currentYear = now.get(Calendar.YEAR);

                    // Nếu năm hết hạn nhỏ hơn năm hiện tại hoặc (cùng năm nhưng tháng hết hạn nhỏ hơn tháng hiện tại)
                    if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
                        Toast.makeText(getActivity(), "Thẻ đã hết hạn", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Giả lập xử lý thanh toán
                Toast.makeText(getActivity(), "Đang xử lý thanh toán...", Toast.LENGTH_SHORT).show();
                btnConfirmOrder.setEnabled(false);
                btnConfirmOrder.postDelayed(() -> {
                    btnConfirmOrder.setEnabled(true);
                    CartManager.getInstance().clearCart();
                    Toast.makeText(getActivity(), "Thanh toán thành công bằng " + paymentMethod, Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại CartFragment
                }, 3000); // Giả lập delay 3 giây
            } else {
                CartManager.getInstance().clearCart();
                Toast.makeText(getActivity(), "Order Confirmed! (" + paymentMethod + ")", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String paymentMethod = "";

                if (checkedId == R.id.radioCOD) {
                    paymentMethod = "COD";
                    paymentDetailsLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioZaloPay) {
                    Toast.makeText(getActivity(), "ZaloPay hiện chưa hỗ trợ", Toast.LENGTH_SHORT).show();
                    radioGroupPayment.clearCheck();
                } else if (checkedId == R.id.radioMomo) {
                    Toast.makeText(getActivity(), "Momo hiện chưa hỗ trợ", Toast.LENGTH_SHORT).show();
                    radioGroupPayment.clearCheck();
                } else if (checkedId == R.id.radioVISA || checkedId == R.id.radioBank) {
                    paymentMethod = (checkedId == R.id.radioVISA) ? "VISA" : "Ngân hàng";
                    paymentDetailsLayout.setVisibility(View.VISIBLE);
                }

                btnConfirmOrder.setTag(paymentMethod);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_checkout;
    }

    private Drawable resizeDrawable(int resId, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return new BitmapDrawable(getResources(), scaledBitmap);
    }

}
