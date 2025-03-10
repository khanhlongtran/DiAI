package com.example.diai_app.DataModel;

import java.io.Serializable;

public class BloodSugarRecord implements Serializable {
    private final int level;
    private final String time;
    private final String notes;

    public BloodSugarRecord(int level, String time, String notes) {
        this.level = level;
        this.time = time;
        this.notes = notes;
    }

    public int getLevel() {
        return level;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }
}
