package com.example.diai_app.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.DataModel.Exercise;
import com.example.diai_app.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    private final List<Exercise> exerciseList;

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.bind(exercise.getTitle(), exercise.getVideoEmbedCode(), exercise.getDate());

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView;
        WebView webView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.exerciseTitle);
            webView = itemView.findViewById(R.id.webView);
            dateTextView = itemView.findViewById(R.id.exerciseDate); // Thêm TextView hiển thị ngày

            WebSettings webSettings = webView.getSettings();
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Log.d("WebView Log", "onReceivedError: " + error.getDescription());
                }
            });

            webView.setWebViewClient(new WebViewClient());
        }

        public void bind(String title, String videoId, String date) {
            titleTextView.setText(title);
            dateTextView.setText("Ngày: " + date); // Hiển thị ngày
            // Tạo iframe để nhúng YouTube video
            String videoHtml = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' " +
                    "src='https://www.youtube.com/embed/" + videoId + "' " +
                    "frameborder='0' allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture' allowfullscreen>" +
                    "</iframe></body></html>";

            // Load iframe vào WebView
            webView.loadData(videoHtml, "text/html", "utf-8");
        }
    }
}
