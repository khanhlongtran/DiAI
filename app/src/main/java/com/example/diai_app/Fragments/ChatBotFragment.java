package com.example.diai_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.Adapter.MessageAdapter;
import com.example.diai_app.DataModel.Message;
import com.example.diai_app.DataModel.User;
import com.example.diai_app.HomeActivity;
import com.example.diai_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBotFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private TextView welcomeTextView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private AppCompatButton applyButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private LinearLayout suggestionsLayout; // Layout chứa các gợi ý
    private TextView tvSuggestFood, tvSuggestMenu, tvSuggestTime; // Các gợi ý
    private OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chatbot;
    }

    @Override
    protected void addOnEventListener() {
        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                addToChat(question, Message.SENT_BY_ME);
                messageEditText.setText("");
                callAPI(question);
                welcomeTextView.setVisibility(View.GONE);
            }
        });

        // Gán sự kiện click cho các TextView đề xuất
        tvSuggestFood.setOnClickListener(v -> handleSuggestionClick(tvSuggestFood.getText().toString()));
        tvSuggestMenu.setOnClickListener(v -> handleSuggestionClick(tvSuggestMenu.getText().toString()));
        tvSuggestTime.setOnClickListener(v -> handleSuggestionClick(tvSuggestTime.getText().toString()));
        applyButton.setOnClickListener(v -> handleApplying());

    }

    private void handleApplying() {
        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MealsByChatBotAI", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_list", "[]");

        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> favoriteMessages = gson.fromJson(json, type);

        if (!favoriteMessages.isEmpty()) {
            // Gọi API với danh sách yêu thích
            callApiWithFavorites(favoriteMessages);
            Toast.makeText(requireContext(), "Đã cập nhật món ăn yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Danh sách yêu thích đang trống!", Toast.LENGTH_SHORT).show();
        }
    }

    private void callApiWithFavorites(List<String> favoriteMessages) {
        // Lấy thông tin user từ SharedPreferences
        requireContext();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);

        User user = null;
        if (userJson != null) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        }

        String userContext = " Chỉ trả lời các bữa ăn, món ăn trong từng bữa, thời gian, ngày tháng và không có gì khác. Vui lòng trả lời ngắn gọn hơn, không linh tinh.";

        if (user != null) {
            userContext += "Tên người dùng là " + user.getFullname() + ". ";
            userContext += "Họ " + user.getAge() + " tuổi, giới tính " + user.getSex() + ", cân nặng " + user.getWeight() + " kg và cao " + user.getHeight() + " cm. ";
            userContext += "Họ mắc bệnh tiểu đường loại " + user.getDiabetesType() + " và " +
                    (user.isHasFamilyHistory() ? "có tiền sử gia đình bị tiểu đường." : "không có tiền sử gia đình bị tiểu đường.") + " ";
            userContext += "Thông tin sức khỏe bổ sung: " + user.getAdditionInfo() + ". ";
        } else {
            userContext += "Không có thông tin người dùng.";
        }

        String favoriteInput = TextUtils.join(", ", favoriteMessages);
        String question = favoriteInput.isEmpty()
                ? "Gợi ý một bữa ăn cho tôi"
                : "Dựa trên sở thích của tôi: " + favoriteInput + ", hãy gợi ý một bữa ăn phù hợp.";

        // Thêm tin nhắn "Typing..." vào danh sách
        messageList.add(new Message("Typing... ", Message.SENT_BY_BOT));

        // Gửi request đến API
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", question);
            jsonBody.put("context", userContext);

            JSONArray historyArray = new JSONArray();
            JSONObject previousMessage = new JSONObject();
            previousMessage.put("input", "Gợi ý bữa ăn");
            previousMessage.put("response", "Sáng: Phở bò, Bánh mì trứng\nTrưa: Cơm gà, Canh chua\nTối: Salad cá hồi, Cháo hải sản");
            historyArray.put(previousMessage);
            jsonBody.put("history", historyArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://api.nlpcloud.io/v1/gpu/finetuned-llama-3-70b/chatbot")
                .header("Authorization", "Token 754352d8634ecf66b2dcde7f8f9920561fc58944")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String result = jsonObject.getString("response");
                        addResponse(result.trim());
                        // Lưu kết quả vào SharedPreferences
                        saveApiResponseToPreferences(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to an error.");
                }
            }
        });
    }

    /**
     * 🌟 Lưu kết quả API vào SharedPreferences dưới dạng danh sách (List<String>)
     */
    private void saveApiResponseToPreferences(String result) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MealsByChatBotAI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_list", "[]");

        // Chuyển JSON thành List<String>
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> mealSuggestions = gson.fromJson(json, type);

        // Thêm kết quả mới vào danh sách (tránh trùng lặp nếu cần)
        if (!mealSuggestions.contains(result)) {
            mealSuggestions.add(result);
        }

        // Chuyển danh sách thành JSON rồi lưu lại
        editor.putString("meal_suggestions", gson.toJson(mealSuggestions));
        editor.apply();
    }

    @Override
    protected void bindView(View view) {
        // Khởi tạo UI components
        recyclerView = view.findViewById(R.id.recycler_view);
        welcomeTextView = view.findViewById(R.id.welcome_text);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);
        // Ánh xạ layout chứa gợi ý
        suggestionsLayout = view.findViewById(R.id.suggestions_layout);
        tvSuggestFood = view.findViewById(R.id.tv_suggest_food);
        tvSuggestMenu = view.findViewById(R.id.tv_suggest_menu);
        tvSuggestTime = view.findViewById(R.id.tv_suggest_time);
        applyButton = view.findViewById(R.id.apply_btn);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
    }

    private void addToChat(String message, String sentBy) {
        requireActivity().runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    private void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    private void handleSuggestionClick(String suggestion) {
        messageEditText.setText(suggestion); // Đưa nội dung gợi ý vào EditText
        suggestionsLayout.setVisibility(View.GONE); // Ẩn layout gợi ý
    }

    private List<JSONObject> historyList = new ArrayList<>(); // Lưu lịch sử

    private void callAPI(String question) {
        requireContext();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);

        User user = null;
        if (userJson != null) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        }

        // Context cá nhân hóa
        String userContext = "This is a conversation between a human and an AI assistant. ";
        userContext += "Focus on answering, don't ask questions back. ";

        if (user != null) {
            userContext += "The user's name is " + user.getFullname() + ". ";
            userContext += "They are " + user.getAge() + " years old, identify as " + user.getSex() + ". ";
            userContext += "They have " + user.getDiabetesType() + " diabetes. ";
        }

        // Hiển thị "Typing..." khi chờ phản hồi
        messageList.add(new Message("Typing...", Message.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", question);
            jsonBody.put("context", userContext);

            // Lịch sử hội thoại
            JSONArray historyArray = new JSONArray(historyList);
            jsonBody.put("history", historyArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://api.nlpcloud.io/v1/gpu/finetuned-llama-3-70b/chatbot")
                .header("Authorization", "Token 754352d8634ecf66b2dcde7f8f9920561fc58944")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String result = jsonObject.getString("response");

                        // Lưu lịch sử hội thoại
                        JSONObject historyEntry = new JSONObject();
                        historyEntry.put("input", question);
                        historyEntry.put("response", result);
                        historyList.add(historyEntry);

                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to an error.");
                }
            }
        });
    }


//    private void callAPI1111(String question) {
//        // Lấy thông tin user từ SharedPreferences
//        requireContext();
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        String userJson = sharedPreferences.getString("loggedInUser", null);
//
//        User user = null;
//        if (userJson != null) {
//            Gson gson = new Gson();
//            user = gson.fromJson(userJson, User.class);
//        }
//
//        // Tạo context cá nhân hóa cho AI
//        String userContext = "This is a conversation between a human and an AI assistant. Focus on answering, don't ask questions back";
//
//        if (user != null) {
//            userContext += "The user's name is " + user.getFullname() + ". ";
//            userContext += "They are " + user.getAge() + " years old, identify as " + user.getSex() + ", and weigh " + user.getWeight() + " kg with a height of " + user.getHeight() + " cm. ";
//            userContext += "They have " + user.getDiabetesType() + " diabetes and " + (user.isHasFamilyHistory() ? "a family history of diabetes." : "no family history of diabetes.") + " ";
//            userContext += "Additional health information: " + user.getAdditionInfo() + ". ";
//        } else {
//            userContext += "No user details available.";
//        }
//
//        userContext += " The AI is called Patrick, an empathetic assistant providing helpful insights.";
//        // Thêm tin nhắn "Typing..." vào danh sách
//        messageList.add(new Message("Typing... ", Message.SENT_BY_BOT));
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("input", question);
//            jsonBody.put("context", userContext);
//
//            JSONArray historyArray = new JSONArray();
//            JSONObject previousMessage = new JSONObject();
//            previousMessage.put("input", "Gợi ý bữa ăn");
//            previousMessage.put("response", "Sáng: Phở bò, Bánh mì trứng\nTrưa: Cơm gà, Canh chua\nTối: Salad cá hồi, Cháo hải sản");
//            historyArray.put(previousMessage);
//            jsonBody.put("history", historyArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
//
//        Request request = new Request.Builder()
//                .url("https://api.nlpcloud.io/v1/gpu/finetuned-llama-3-70b/chatbot")
//                .header("Authorization", "Token 754352d8634ecf66b2dcde7f8f9920561fc58944")
//                .header("Content-Type", "application/json")
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                addResponse("Failed to load response due to " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        String result = jsonObject.getString("response");
//                        addResponse(result.trim());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    addResponse("Failed to load response due to an error.");
//                }
//            }
//        });
//    }
}