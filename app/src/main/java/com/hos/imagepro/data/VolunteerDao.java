package com.hos.imagepro.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hos.imagepro.room.Volunteer;

import java.util.List;


@Dao
public interface VolunteerDao {
    @Insert
    public void insertVolunteer(Volunteer volunteer);

    @Update
    public void updateVolunteer(Volunteer volunteer);

    @Query("Select * from Volunteer")
    public List<Volunteer> getAllVolunteer();

    @Query("Select * From  Volunteer where name=:Name and phone=:Phone")
    Volunteer getVolunteer(String Name, String Phone);
}
