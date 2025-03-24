package com.example.diai_app.manager;

import android.util.Log;

import com.example.diai_app.dataModels.BloodSugarRecord;
import com.example.diai_app.dtos.BloodSugarRecordDTO;
import com.example.diai_app.dtos.UserDTO;
import com.example.diai_app.settings.DataConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseManager {
    private static FirebaseDatabaseManager instance;
    private DatabaseReference dbRef;

    // Constructor private để áp dụng Singleton
    private FirebaseDatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(); // Trỏ tới root
        Log.d("FirebaseDatabaseManager", "Initialized: " + dbRef.toString());
    }

    // Lấy instance duy nhất
    public static synchronized FirebaseDatabaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseManager();
        }
        return instance;
    }

    // Đọc danh sách dữ liệu và ánh xạ từ DTO sang Model class
    public <T, R> void readListData(String nodePath,
                                    Class<T> dtoClass,
                                    Class<R> modelClass,
                                    DataConverter.Mapper<T, R> mapper,
                                    OnDataReadListener<List<R>> listener) {
        dbRef.child(nodePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FirebaseDatabaseManager", "Reading " + nodePath + ", Exists: " + snapshot.exists());
                if (snapshot.exists()) {
                    List<R> modelList = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Log.d("FirebaseDatabaseManager", "Raw Firebase data: " + childSnapshot.getValue());
                        T item = childSnapshot.getValue(dtoClass);
                        if (item != null) {
                            try {
                                Log.d("FirebaseDatabaseManager", "Before mapping (T): " + item.toString());
                                R model = mapper.map(item);
                                Log.d("FirebaseDatabaseManager", "After mapping (R): " + model.toString());
                                modelList.add(model);
                            } catch (Exception e) {
                                Log.e("FirebaseDatabaseManager", "Error mapping item: " + item, e);
                            }
                        } else {
                            Log.d("FirebaseDatabaseManager", "Item is null for key: " + childSnapshot.getKey());
                        }
                    }
                    listener.onSuccess(modelList);
                } else {
                    listener.onFailure(new Exception("No data found at " + nodePath));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("FirebaseDatabaseManager", "Error reading " + nodePath, error.toException());
                listener.onFailure(error.toException());
            }
        });
    }

    public void updateUserByEmail(String email, Map<String, Object> updates, OnDataUpdateListener listener) {
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        UserDTO userDTO = userSnapshot.getValue(UserDTO.class);
                        if (userDTO != null && email.equals(userDTO.getEmail())) {
                            // Lấy reference của user cần update
                            DatabaseReference userRef = userSnapshot.getRef();

                            // Cập nhật dữ liệu
                            userRef.updateChildren(updates).addOnSuccessListener(aVoid -> listener.onSuccess()).addOnFailureListener(listener::onFailure);
                            return;
                        }
                    }
                    listener.onFailure(new Exception("No user found with email: " + email));
                } else {
                    listener.onFailure(new Exception("No users found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure(error.toException());
            }
        });
    }

    public void createUser(UserDTO user, OnDataWriteListener listener) {
        dbRef.child("users").setValue(user).addOnSuccessListener(aVoid -> listener.onSuccess()).addOnFailureListener(listener::onFailure);
    }

    public void addBloodSugarRecord(String email, BloodSugarRecord record, OnDataUpdateListener listener) {
        DatabaseReference usersRef = dbRef.child("users");

        usersRef.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey(); // Lấy ID của user
                        DatabaseReference recordsRef = usersRef.child(userId).child("bloodSugarRecords");
                        BloodSugarRecordDTO recordDTO = DataConverter.convertToBloodSugarRecordDTO(record);
                        // Thêm bản ghi mới vào danh sách
                        recordsRef.push().setValue(recordDTO).addOnSuccessListener(aVoid -> listener.onSuccess()).addOnFailureListener(listener::onFailure);
                        break; // Chỉ cần tìm thấy 1 user là đủ
                    }
                } else {
                    listener.onFailure(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.toException());
            }
        });
    }


    // Đọc một user cụ thể theo UserID và trả về UserDTO
    public void readUserById(int userId, OnDataReadListener<UserDTO> listener) {
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Đọc dữ liệu từ Firebase dưới dạng UserDTO
                        UserDTO userDTO = userSnapshot.getValue(UserDTO.class);
                        if (userDTO != null && userDTO.getUserId() == userId) {
                            listener.onSuccess(userDTO);
                            return;
                        }
                    }
                    listener.onFailure(new Exception("No user found with UserID: " + userId));
                } else {
                    listener.onFailure(new Exception("No users found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("FirebaseDatabaseManager", "Error reading user", error.toException());
                listener.onFailure(error.toException());
            }
        });
    }

    // Interface để xử lý callback khi đọc dữ liệu
    public interface OnDataReadListener<T> {
        void onSuccess(T data);

        void onFailure(Exception e);
    }

    // Interface để xử lý callback khi ghi dữ liệu
    public interface OnDataWriteListener {
        void onSuccess();

        void onFailure(Exception e);
    }

    public interface OnDataUpdateListener {
        void onSuccess();

        void onFailure(Exception e);
    }

}
