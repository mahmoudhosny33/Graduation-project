package com.hos.imagepro.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Volunteer")
public class Volunteer implements Serializable {
      private int id;
      private String name;
      @PrimaryKey
      @NonNull
      private String phone;
      private String specialization;
      private String day_available;
      private float time_available;

    public Volunteer(String name, String phone, String specialization, String day_available, float time_available) {
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.day_available = day_available;
        this.time_available = time_available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDay_available() {
        return day_available;
    }

    public void setDay_available(String day_available) {
        this.day_available = day_available;
    }

    public float getTime_available() {
        return time_available;
    }

    public void setTime_available(float time_available) {
        this.time_available = time_available;
    }
}
