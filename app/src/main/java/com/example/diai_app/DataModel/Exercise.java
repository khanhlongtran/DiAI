package com.example.diai_app.DataModel;

public class Exercise {
    private String title;
    private String videoEmbedCode;

    public Exercise(String title, String videoEmbedCode) {
        this.title = title;
        this.videoEmbedCode = videoEmbedCode;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoEmbedCode() {
        return videoEmbedCode;
    }
}
