package com.example.diai_app.dtos;

import com.google.firebase.database.PropertyName;

public class BloodSugarRecordDTO {
    @PropertyName("Measurement")
    private double measurement;
    @PropertyName("MeasurementTime")
    private String measurementTime;
    @PropertyName("Notes")
    private String notes;

    // Constructor
    public BloodSugarRecordDTO(double measurement, String measurementTime, String notes) {
        this.measurement = measurement;
        this.measurementTime = measurementTime;
        this.notes = notes;
    }

    public BloodSugarRecordDTO() {
        // Constructor rỗng cho ánh xạ từ Firebase
    }

    // Getters và Setters
    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
