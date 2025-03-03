package com.example.diai_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.diai_app.Adapter.ExerciseAdapter;
import com.example.diai_app.DataModel.Exercise;
import com.example.diai_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityFragment extends BaseFragment {
    private final OkHttpClient client = new OkHttpClient();
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private Button btnSuggestExercises;
    private androidx.appcompat.widget.SearchView searchView;
    private List<Exercise> exerciseList;
    private List<Exercise> filteredList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exerciseList = new ArrayList<>();
        filteredList = new ArrayList<>(exerciseList);
        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    protected void bindView(View view) {
        btnSuggestExercises = view.findViewById(R.id.btnSuggestExercises);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void addOnEventListener() {
        btnSuggestExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterExercises(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterExercises(newText);
                return true;
            }
        });
    }

    private void filterExercises(String query) {
        filteredList.clear();
        for (Exercise exercise : exerciseList) {
            if (exercise.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(exercise);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void callAPI() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", "Find me 3 exercise videos aerobic on youtube and give me the embedded video code");
            jsonBody.put("context", "AI will search for aerobic exercises on youtube with its embed code. Reply in json form without anything like ```json or ``` with root is \"videos\", that include some videos");

            JSONArray historyArray = new JSONArray();
            JSONObject previousMessage = new JSONObject();
            previousMessage.put("input", "Find me 5 exercise videos aerobic on youtube and give me the embedded video code");
            previousMessage.put("response", "{\n  \"videos\": [\n    {\n      \"title\": \"30-Minute Fat Burning Cardio Workout\",\n      \"url\": \"https://www.youtube.com/watch?v=ml6cT4AZdqI\",\n      \"embed_code\": \"<iframe width=\\\"560\\\" height=\\\"315\\\" src=\\\"https://www.youtube.com/embed/ml6cT4AZdqI\\\" frameborder=\\\"0\\\" allowfullscreen></iframe>\"\n    },\n    {\n      \"title\": \"Low Impact Aerobic Workout for Beginners\",\n      \"url\": \"https://www.youtube.com/watch?v=6X4x9v4U2C0\",\n      \"embed_code\": \"<iframe width=\\\"560\\\" height=\\\"315\\\" src=\\\"https://www.youtube.com/embed/6X4x9v4U2C0\\\" frameborder=\\\"0\\\" allowfullscreen></iframe>\"\n    },\n    {\n      \"title\": \"Dance Aerobics for Beginners\",\n      \"url\": \"https://www.youtube.com/watch?v=0aQ2JZ5E4fU\",\n      \"embed_code\": \"<iframe width=\\\"560\\\" height=\\\"315\\\" src=\\\"https://www.youtube.com/embed/0aQ2JZ5E4fU\\\" frameborder=\\\"0\\\" allowfullscreen></iframe>\"\n    }\n  ]\n}");

            historyArray.put(previousMessage);
            jsonBody.put("history", historyArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://api.nlpcloud.io/v1/gpu/finetuned-llama-3-70b/chatbot")
                .header("Authorization", "Token 754352d8634ecf66b2dcde7f8f9920561fc58944")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Get the full response string
                        String responseString = response.body().string();
                        Log.d("TAG", responseString);

                        // Parse the outer JSON object
                        JSONObject jsonResponse = new JSONObject(responseString);
                        // Extract the string from the "response" key
                        String responseStringFromKey = jsonResponse.getString("response");

                        // Now parse the response string (which should be JSON) into a JSONObject (videos)
                        JSONObject responseJson = new JSONObject(responseStringFromKey);
                        // Extract the array from the "videos" key
                        JSONArray videosArray = responseJson.getJSONArray("videos");

                        // Add videos to exerciseList
                        exerciseList.clear(); // Clear the previous data
                        for (int i = 0; i < videosArray.length(); i++) {
                            JSONObject video = videosArray.getJSONObject(i);
                            String title = video.getString("title");
                            String url = video.getString("url");

                            // Extract the video ID from the URL
                            String videoId = extractVideoId(url);

                            // Thêm bài tập với ngày tương ứng
                            exerciseList.add(new Exercise(title, videoId, i));
                        }

                        // Update RecyclerView on UI thread
                        requireActivity().runOnUiThread(() -> {
                            filteredList.clear();
                            filteredList.addAll(exerciseList);
                            adapter.notifyDataSetChanged();
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Method to extract the video ID from YouTube URL
    private String extractVideoId(String url) {
        String videoId = null;
        try {
            // Extract the video ID from the URL using regex
            Pattern pattern = Pattern.compile("v=([a-zA-Z0-9_-]+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoId;
    }

}