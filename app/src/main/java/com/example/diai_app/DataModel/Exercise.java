package com.example.diai_app.DataModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Exercise {
    private String title;
    private String videoEmbedCode;
    private String date; // Thêm trường date

    public Exercise(String title, String videoEmbedCode, int index) {
        this.title = title;
        this.videoEmbedCode = videoEmbedCode;
        this.date = calculateDate(index); // Tính ngày dựa vào index
    }

    private String calculateDate(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, index + 1); // Ngày mai là index = 0, tiếp theo là 1,2,3...

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public String getTitle() {
        return title;
    }

    public String getVideoEmbedCode() {
        return videoEmbedCode;
    }

    public String getDate() { // Getter cho ngày
        return date;
    }
}
