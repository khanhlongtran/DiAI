package com.example.diai_app.settings;

import android.util.Log;

import com.example.diai_app.dataModels.BloodSugarRecord;
import com.example.diai_app.dataModels.Category;
import com.example.diai_app.dataModels.Product;
import com.example.diai_app.dataModels.User;
import com.example.diai_app.dtos.BloodSugarRecordDTO;
import com.example.diai_app.dtos.CategoryDTO;
import com.example.diai_app.dtos.ProductDTO;
import com.example.diai_app.dtos.ProfileDTO;
import com.example.diai_app.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    // Interface Mapper để ánh xạ từ DTO sang model
    public interface Mapper<T, R> {
        R map(T model);
    }

    // Từ DTO sang DataModel (cập nhật)
    public static Category convertToCategoryDataModel(CategoryDTO categoryDTO) {
        return new Category(
                categoryDTO.getId(),
                categoryDTO.getCategoryName(),
                categoryDTO.getImageUrl()
        );
    }

    public static Product convertToProductDataModel(ProductDTO productDTO) {
        Log.d("DataConverter", "convertToProductDataModel: " + productDTO.toString());
        return new Product(
                productDTO.getProductId(),
                productDTO.getProductName(),
                productDTO.getCategory().getCategoryName(), // Lấy tên category từ CategoryDTO, đang lỗi
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getProductImageUrl()
        );
    }

    public static User convertToUserDataModel(UserDTO userDTO) {
        Log.d("TAGTAGTAG", "UserDTO: " + userDTO.toString());
        ProfileDTO profile = userDTO.getProfile();

        Log.d("TAGTAGTAG", "User: " + new User(
                userDTO.getUsername(), // name
                userDTO.getPasswordHash(), // password
                userDTO.getEmail(), // email
                profile != null ? profile.getSex() : "", // sex
                profile != null ? profile.getWeight() : 0.0, // weight
                profile != null ? profile.getFullName() : "", // fullname
                profile != null ? profile.getAge() : 0, // age
                profile != null ? profile.getHeight() : 0.0, // height
                profile != null ? profile.getDiabetesType() : "", // diabetesType
                profile != null ? profile.getAdditionalInfo() : "", // additionInfo
                profile != null && profile.isFamilyHistory() // hasFamilyHistory
        ).toString());
        return new User(
                userDTO.getUsername(), // name
                userDTO.getPasswordHash(), // password
                userDTO.getEmail(), // email
                profile != null ? profile.getSex() : "", // sex
                profile != null ? profile.getWeight() : 0.0, // weight
                profile != null ? profile.getFullName() : "", // fullname
                profile != null ? profile.getAge() : 0, // age
                profile != null ? profile.getHeight() : 0.0, // height
                profile != null ? profile.getDiabetesType() : "", // diabetesType
                profile != null ? profile.getAdditionalInfo() : "", // additionInfo
                profile != null && profile.isFamilyHistory() // hasFamilyHistory
        );
    }

    // Từ DataModel sang DTO
    public static CategoryDTO convertToCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getImageUrl()
        );
    }

    public static ProductDTO convertToProductDTO(Product product) {
        // Tạo CategoryDTO từ tên category (String) của Product
        CategoryDTO categoryDTO = new CategoryDTO(
                0, // Placeholder id, cần ánh xạ nếu có dữ liệu đầy đủ
                product.getCategory(), // Tên category từ Product
                "" // Placeholder imageUrl, cần ánh xạ nếu có dữ liệu đầy đủ
        );
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                categoryDTO
        );
    }


    public static ProfileDTO convertToProfileDTO(User user) {
        return new ProfileDTO(
                user.getFullname(),
                user.getAge(),
                user.getSex(),
                user.getWeight(),
                user.getHeight(),
                user.getDiabetesType(),
                user.isHasFamilyHistory(),
                user.getAdditionInfo()
        );
    }

    public static BloodSugarRecordDTO convertToBloodSugarRecordDTO(BloodSugarRecord record) {
        // Ánh xạ level (int) sang measurement (double) và time sang measurementTime
        return new BloodSugarRecordDTO(
                (double) record.getLevel(), // Chuyển int sang double
                record.getTime(),
                record.getNotes()
        );
    }

    public static UserDTO convertToUserDTO(User user) {
        ProfileDTO profileDTO = convertToProfileDTO(user);
        List<BloodSugarRecordDTO> recordDTOs = null; // Cần thêm logic nếu có
        return new UserDTO(
                0, // Placeholder userId
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                profileDTO,
                recordDTOs != null ? recordDTOs : new ArrayList<>()
        );
    }
}
